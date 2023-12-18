/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package se.zaleth.adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author criz_
 */
public class Puzzle16 {
    
    private enum Heading {
        NORTH(1,"UP"), WEST(2,"LEFT"), SOUTH(4,"DOWN"), EAST(8,"RIGHT");
        public final int value;
        public final String name;
        Heading(int v, String s) {
            value = v;
            name = s;
        }
    };
    
    private class Beam {
        Heading dir;
        int x,y;

        public Beam(Heading dir, int x, int y) {
            this.dir = dir;
            this.x = x;
            this.y = y;
        }
        
        private void trace() {
            int[][] passed = new int[heats.length][heats[0].length];
            while(y >= 0 && y < grid.length && x >= 0 && x < grid[y].length) {
                if((passed[y][x] & dir.value) > 0) {
                    System.out.println("Loop after " + 0 + " steps");
                    return;
                }
                passed[y][x] |= dir.value;
                //System.out.println("Step " + steps.size() + ": " + this);
                switch(grid[y][x]) {
                    case '.':
                        break;

                    case '/':
                        switch(dir) {
                            case NORTH:
                                dir = Heading.EAST;
                                break;
                            case WEST:
                                dir = Heading.SOUTH;
                                break;
                            case SOUTH:
                                dir = Heading.WEST;
                                break;
                            case EAST:
                                dir = Heading.NORTH;
                                break;
                        }
                        break;

                    case '\\':
                        switch(dir) {
                            case NORTH:
                                dir = Heading.WEST;
                                break;
                            case WEST:
                                dir = Heading.NORTH;
                                break;
                            case SOUTH:
                                dir = Heading.EAST;
                                break;
                            case EAST:
                                dir = Heading.SOUTH;
                                break;
                        }
                        break;

                    case '-':
                        switch(dir) {
                            case NORTH:
                            case SOUTH:
                                dir = Heading.EAST;
                                Beam b = new Beam(Heading.WEST, x, y);
                                if(!beams.contains(b))
                                    beams.add(b);
                                break;
                            case WEST:
                            case EAST:
                                break;
                        }
                        break;

                    case '|':
                        switch(dir) {
                            case NORTH:
                            case SOUTH:
                                break;
                            case WEST:
                            case EAST:
                                dir = Heading.NORTH;
                                Beam b = new Beam(Heading.SOUTH, x, y);
                                if(!beams.contains(b))
                                    beams.add(b);
                                break;
                        }
                        break;
                }
                heats[y][x]++;
                switch(dir) {
                    case NORTH:
                        y--;
                        break;
                    case WEST:
                        x--;
                        break;
                    case SOUTH:
                        y++;
                        break;
                    case EAST:
                        x++;
                        break;
                }
            }
            System.out.println("Exit going " + dir.name);
        }
        @Override
        public boolean equals(Object o) {
            if(o instanceof Beam) {
                Beam b = (Beam) o;
                //System.out.println("test " + this + " ?= " + b);
                return x == b.x && y == b.y && dir == b.dir;
            }
            return false;
        }
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.dir);
            hash = 97 * hash + this.x;
            hash = 97 * hash + this.y;
            return hash;
        }
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder("(");
            str.append(y);
            str.append(",");
            str.append(x);
            str.append(") ");
            str.append(dir.name);
            return str.toString();
        }
    }
    
    private final File input;
    private final List<Beam> beams = new ArrayList<>();
    private char[][] grid;
    private int[][] heats;
    
    public Puzzle16(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<String> lines = new ArrayList<>();
        String line;
        int sum = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            System.out.println("Line: " + line);
            lines.add(line);
        }
        grid = new char[lines.size()][];
        heats = new int[grid.length][];
        for(int i = 0; i < grid.length; i++) {
            grid[i] = lines.get(i).toCharArray();
            heats[i] = new int[grid[i].length];
        }
        //new Beam(Heading.EAST, 0, 0).trace();
        beams.add(new Beam(Heading.EAST, 0, 0));
        while(!beams.isEmpty()) {
            System.out.println("Beams: " + beams.size());
            Beam b = beams.remove(0);
            b.trace();
        }
        sum = score();
        return "" + sum;
    }
    
    private int score() {
        int sum = 0;
        for(int y = 0; y < heats.length; y++) {
            StringBuilder str = new StringBuilder();
            for(int x = 0; x < heats[y].length; x++) {
                if(heats[y][x] > 0) {
                    str.append('#');
                    sum++;
                } else {
                    str.append('.');
                }
            }
            System.out.println(str.toString());
        }
        return sum;
    }
    
}

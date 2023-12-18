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
public class Puzzle17 {
    
    private enum Heading {
        NORTH(1,"UP"), WEST(2,"LEFT"), SOUTH(4,"DOWN"), EAST(8,"RIGHT");
        public final int value;
        public final String name;
        Heading(int v, String s) {
            value = v;
            name = s;
        }
    };
        
    private final File input;
    private int[][] heats;
    
    public Puzzle17(File in) {
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
        heats = new int[lines.size()][];
        for(int y = 0; y < heats.length; y++) {
            StringBuilder str = new StringBuilder();
            char[] row = lines.get(y).toCharArray();
            heats[y] = new int[row.length];
            for(int x = 0; x < heats[y].length; x++) {
                heats[y][x] = ((int) row[x] - '0');
                str.append(heats[y][x]);
            }
            System.out.println("Row:  " + str.toString());
        }
        int right = move(1, 0, Heading.EAST, 2, ((int) Math.floor((heats.length + heats[0].length) * 1.3)));
        int down = move(0, 1, Heading.SOUTH, 2, ((int) Math.floor((heats.length + heats[0].length) * 1.3)));
        /*if(heats[0][1] < heats[1][0]) {
            sum = move(1, 0, Heading.EAST, 2, ((int) Math.floor((heats.length + heats[0].length) * 1.3)));
        } else {
            sum = move(0, 1, Heading.SOUTH, 2, ((int) Math.floor((heats.length + heats[0].length) * 1.3)));
        }*/
        sum = Math.min(right, down);
        return "" + sum;
    }
    
    private int move(int x, int y, Heading dir, int moves, int ttl) {
        int maxY = heats.length;
        int maxX = heats[y].length;
        if(ttl < 1)
            return 0;
        if((y == maxY - 1 && x == maxX - 2) || (y == maxY - 2 && x == maxX - 1)) {
            return heats[maxY - 1][maxX - 1];
        }
        System.out.println("(" + x + "," + y + ") ttl=" + ttl + " heading: " + dir.name);
        int loss = heats[y][x];
        int left = 9, right = 9, ahead = 9;
        switch(dir) {
            case NORTH:
                if(moves > 0 && y > 0) {
                    ahead = move(x, y+1, dir, moves - 1, ttl - 1); //heats[y-1][x];
                }
                if(x < (maxX-1)) {
                    right = move(x+1, y, Heading.EAST, 2, ttl - 1); //heats[y][x+1];
                }
                /*if(x > 0) {
                    left = heats[y][x-1];
                }*/
                System.out.println("Left: " + left + " Ahead: " + ahead + " Right: " + right);
                /*if(ahead <= left && ahead <= right) {
                    return loss + move(x, y+1, dir, moves - 1, ttl - 1);
                /*} else if(right < left && right < ahead) {
                    return loss + move(x+1, y, Heading.EAST, 2, ttl - 1);*/
                //}
                //return loss + move(x-1, y, Heading.WEST, 2, ttl - 1);
                //return loss + move(x+1, y, Heading.EAST, 2, ttl - 1);
                return loss + Math.min(ahead, right);
            case WEST:
                if(moves > 0 && x > 0) {
                    ahead = heats[y][x-1];
                }
                if(y < (maxY-1)) {
                    left = heats[y+1][x];
                }
                /*if(y > 0) {
                    right = heats[y-1][x];
                }*/
                System.out.println("Left: " + left + " Ahead: " + ahead + " Right: " + right);
                if(ahead <= left && ahead <= right) {
                    return loss + move(x+1, y, dir, moves - 1, ttl - 1);
                /*} else if(left < right && left < ahead) {
                    return loss + move(x, y+1, Heading.SOUTH, 2, ttl - 1);*/
                }
                //return loss + move(x, y-1, Heading.NORTH, 2, ttl - 1);
                return loss + move(x, y+1, Heading.SOUTH, 2, ttl - 1);
            case SOUTH:
                if(moves > 0 && y < (maxY-1)) {
                    ahead = heats[y+1][x];
                }
                if(x < (maxX-1)) {
                    left = heats[y][x+1];
                }
                if(x > 0) {
                    right = heats[y][x-1];
                }
                System.out.println("Left: " + left + " Ahead: " + ahead + " Right: " + right);
                if(ahead <= left && ahead <= right) {
                    return loss + move(x, y+1, dir, moves - 1, ttl - 1);
                } else if(right <= left && right <= ahead) {
                    return loss + move(x-1, y, Heading.WEST, 2, ttl - 1);
                }
                return loss + move(x+1, y, Heading.EAST, 2, ttl - 1);
            case EAST:
                if(moves > 0 && x < (maxX-1)) {
                    ahead = heats[y][x+1];
                }
                if(y < (maxY-1)) {
                    right = heats[y+1][x];
                }
                if(y > 0) {
                    left = heats[y-1][x];
                }
                System.out.println("Left: " + left + " Ahead: " + ahead + " Right: " + right);
                if(ahead <= left && ahead <= right) {
                    return loss + move(x+1, y, dir, moves - 1, ttl - 1);
                } else if(left <= right && left <= ahead) {
                    return loss + move(x, y-1, Heading.NORTH, 2, ttl - 1);
                }
                return loss + move(x, y+1, Heading.SOUTH, 2, ttl - 1);
        }
        return 0;
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

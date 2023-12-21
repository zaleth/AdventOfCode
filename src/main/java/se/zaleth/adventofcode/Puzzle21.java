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
import java.util.Set;

/**
 *
 * @author criz_
 */
public class Puzzle21 {
    
    private class Triple {
        int x, y;
        int steps;

        public Triple(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }
        
        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Triple))
                return false;
            Triple t = (Triple) o;
            return x == t.x && y == t.y && steps == t.steps;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + this.x;
            hash = 83 * hash + this.y;
            hash = 83 * hash + this.steps;
            return hash;
        }
    }
    
    private final File input;
    private final int steps;
    
    public Puzzle21(File in, int steps) {
        input = in;
        this.steps = steps;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<String> rows = new ArrayList<>();
        List<Triple> list = new ArrayList<>();
        String line;
        int startX = -1, startY = -1;
        int sum = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            rows.add(line);
            if(line.contains("S")) {
                startY = sum;
                startX = line.indexOf("S");
            }
            sum++;
        }
        char[][] map = new char[rows.size()][];
        for(int i = 0; i < rows.size(); i++) {
            map[i] = rows.get(i).toCharArray();
        }
        list.add(new Triple(startX, startY, steps));
        map[startY][startX] = '.';
        int height = sum - 1;
        sum = 0;
        int width = map[0].length - 1;
        int step = 1;
        while(!list.isEmpty()) {
            Set<Triple> added = new HashSet<>();
            for(Triple t : list) {
                if(map[t.y][t.x] == '.') {
                    if(t.steps == 0) {
                        sum++;
                        map[t.y][t.x] = 'O';
                    }
                }
                if(t.steps > 0 && t.x > 0 && map[t.y][t.x-1] == '.')
                    added.add(new Triple(t.x-1, t.y, t.steps - 1));
                if(t.steps > 0 && t.x < width && map[t.y][t.x+1] == '.')
                    added.add(new Triple(t.x+1, t.y, t.steps-1));
                if(t.steps > 0 && t.y > 0 && map[t.y-1][t.x] == '.')
                    added.add(new Triple(t.x, t.y-1, t.steps - 1));
                if(t.steps > 0 && t.y < height && map[t.y+1][t.x] == '.')
                    added.add(new Triple(t.x, t.y+1, t.steps-1));
            }
            System.out.println("Step " + step++ + ": " + added.size());
            //sum = added.size();
            list.clear();
            list.addAll(added);
        }
        /*for(int y = 0; y < map.length; y++) {
            StringBuilder str = new StringBuilder();
            for(int x = 0; x < map[y].length; x++) {
                str.append(map[y][x]);
            }
            System.out.println(str.toString());
        }*/
        return "" + sum;
    }
    
}

/*
...........
.....###.#.
.###.##.O#.
.O#O#O.O#..
O.O.#.#.O..
.##O.O####.
.##.O#O..#.
.O.O.O.##..
.##.#.####.
.##O.##.##.
...........
*/
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package se.zaleth.adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author criz_
 */
public class Puzzle2 {
    
    private static final int MAX_RED = 12, MAX_GREEN = 13, MAX_BLUE = 14;
    private static final Map<String,Integer> LIMIT = new HashMap<>();
    
    static {
        LIMIT.put("red", MAX_RED);
        LIMIT.put("green", MAX_GREEN);
        LIMIT.put("blue", MAX_BLUE);
    }
    
    private final File input;
    
    public Puzzle2(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line;
        int sum = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            System.out.println("Line: " + line);
            int gameNum = Integer.parseInt(line.split(":")[0].split(" ")[1]);
            System.out.println("Game #" + gameNum);
            boolean passed = true;
            for(String str : line.split(":")[1].split(";")) {
                for(String show : str.split(",")) {
                    show = show.trim();
                    System.out.print("  " + show);
                    int count = Integer.parseInt(show.split(" ")[0]);
                    String color = show.split(" ")[1];
                    if(LIMIT.get(color) < count)
                        passed = false;
                }
                System.out.println("");
            }
            if(passed) {
                sum += gameNum;
            }
        }
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line;
        int sum = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            System.out.println("Line: " + line);
            int gameNum = Integer.parseInt(line.split(":")[0].split(" ")[1]);
            System.out.println("Game #" + gameNum);
            Map<String,Integer> min = new HashMap<>();
            for(String str : line.split(":")[1].split(";")) {
                for(String show : str.split(",")) {
                    show = show.trim();
                    //System.out.print("  " + show);
                    int count = Integer.parseInt(show.split(" ")[0]);
                    String color = show.split(" ")[1];
                    if(min.getOrDefault(color, 0) < count) {
                        min.put(color, count);
                    }
                }
                //System.out.println("");
            }
            int power = min.get("red") * min.get("green") * min.get("blue");
            System.out.println("Power: " + min.get("red") + "*" + min.get("green") + "*" + min.get("blue") + "=" + power);
            sum += power;
        }
        return "" + sum;
    }

}

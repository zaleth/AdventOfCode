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
public class Puzzle3 {
    
    private class Gear {
        
        private final int[] values;
        
        public Gear() {
            values = new int[3];
        }
        
        public void addNumber(int num) {
            if(values[0] == 0)
                values[0] = num;
            else if(values[1] == 0)
                values[1] = num;
            else
                values[2] = num;
        }
        
        public boolean isGear() {
            return values[2] == 0 && values[1] != 0;
        }
        
        public int getRatio() {
            return isGear() ? values[0] * values[1] : 0;
        }
        
    }
    
    private final Map<Integer,Gear> gears = new HashMap<>();
    private final File input;
    private String line, lastLine, nextLine;
    private int lineIndex, startIndex, lineSize, lineNum;
    
    public Puzzle3(File in) {
        input = in;
        lineIndex = 0;
        lineNum = 0;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        int sum = 0, num;
        boolean running = true;
        line = "." + in.readLine() + ".";
        lineSize = line.length() - 2;
        lastLine = padLine(line.length());
        
        while(running) {
            line = line.trim();
            if(line.isBlank())
                continue;
            System.out.println("Line: " + line);
            lineNum++;
            nextLine = in.readLine();
            if(nextLine == null) {
                running = false;
                nextLine = padLine(line.length());
            }
            nextLine = "." + nextLine + ".";
            lineIndex = 0;
            while((num = getNextNumber(line)) != -1) {
                if(isEngineNumber()) {
                    System.out.println("  EngineNumber: " + num);
                    sum += num;
                }
            }
            // ...
            lastLine = line;
            line = nextLine;
        }
        System.out.println("Size: " + lineSize + "*" + lineNum);
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        int sum = 0, num;
        boolean running = true;
        line = "." + in.readLine() + ".";
        lineSize = line.length() - 2;
        lastLine = padLine(line.length());
        
        while(running) {
            line = line.trim();
            if(line.isBlank())
                continue;
            System.out.println("Line: " + line);
            lineNum++;
            nextLine = in.readLine();
            if(nextLine == null) {
                running = false;
                nextLine = padLine(line.length());
            }
            nextLine = "." + nextLine + ".";
            lineIndex = 0;
            while((num = getNextNumber(line)) != -1) {
                testForGear(num);
            }
            // ...
            lastLine = line;
            line = nextLine;
        }
        sum = gears.values().stream().filter(g -> g.isGear()).map(g -> g.getRatio()).reduce(0, Integer::sum);
        System.out.println("Size: " + lineSize + "*" + lineNum);
        return "" + sum;
    }
    
    private String padLine(int len) {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < len; i++)
            str.append('.');
        return str.toString();
    }
    
    private int getNextNumber(String str) {
        int ret = -1;
        while(lineIndex < str.length()) {
            char c = str.charAt(lineIndex);
            if(ret < 0) {
                if(Character.isDigit(c)) {
                    startIndex = lineIndex;
                    ret = ((int) c) - '0';
                }
            } else {
                if(Character.isDigit(c)) {
                    ret = ret * 10 + ((int) c) - '0';
                } else {
                    return ret;
                }
            }
            lineIndex++;
        }
        return ret;
    }
    
    private boolean isEngineNumber() {
        if(line.charAt(startIndex - 1) != '.' || line.charAt(lineIndex) !=  '.')
            return true;
        for(int i = startIndex - 1; i <= lineIndex; i++) {
            if(lastLine.charAt(i) != '.' || nextLine.charAt(i) != '.')
                return true;
        }
        return false;
    }
    
    private void testForGear(int num) {
        int coord;
        if(line.charAt(startIndex - 1) == '*') {
            coord = lineSize * lineNum + startIndex - 1;
            if(!gears.containsKey(coord))
                gears.put(coord, new Gear());
            gears.get(coord).addNumber(num);
        }

        if(line.charAt(lineIndex) ==  '*') {
            coord = lineSize * lineNum + lineIndex;
            if(!gears.containsKey(coord))
                gears.put(coord, new Gear());
            gears.get(coord).addNumber(num);
        }
            
        for(int i = startIndex - 1; i <= lineIndex; i++) {
            if(lastLine.charAt(i) == '*') {
                coord = lineSize * (lineNum - 1) + i;
                if(!gears.containsKey(coord))
                    gears.put(coord, new Gear());
                gears.get(coord).addNumber(num);
            }
        }
        for(int i = startIndex - 1; i <= lineIndex; i++) {
            if(nextLine.charAt(i) == '*') {
                coord = lineSize * (lineNum + 1) + i;
                if(!gears.containsKey(coord))
                    gears.put(coord, new Gear());
                gears.get(coord).addNumber(num);
            }
        }
    }
    
}

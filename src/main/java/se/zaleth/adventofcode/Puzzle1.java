/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package se.zaleth.adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author criz_
 */
public class Puzzle1 {
    
    private static final String NUMBERS[] = { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
    
    private final File input;
    
    public Puzzle1(File in) {
        solveLine("eighthree");
        solveLine("sevenine");
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
            sum += firstNumber(line) * 10 + lastNumber(line);
        }
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line;
        int sum = 0;
        int lines = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            lines++;
            System.out.println("Line: " + line);
            int firstIndex = firstNumberIndex(line);
            int firstIndex2 = firstWordIndex(line);
            if(firstIndex < 0 || (firstIndex2 > -1 && firstIndex2 < firstIndex))
                firstIndex = firstIndex2;
            int lastIndex = lastNumberIndex(line);
            int lastIndex2 = lastWordIndex(line);
            if(lastIndex < 0 || lastIndex2 > lastIndex)
                lastIndex = lastIndex2;
            System.out.println("First: " + firstIndex + " Last: " + lastIndex + " Sum: " + (extract(line, firstIndex) * 10 + extract(line, lastIndex)));
            sum += extract(line, firstIndex) * 10 + extract(line, lastIndex);
        }
        System.out.println("Lines: " + lines);
        return "" + sum;
    }
    
    private void solveLine(String line) {
            System.out.println("Line: " + line);
            int firstIndex = firstNumberIndex(line);
            int firstIndex2 = firstWordIndex(line);
            if(firstIndex < 0 || (firstIndex2 > -1 && firstIndex2 < firstIndex))
                firstIndex = firstIndex2;
            int lastIndex = lastNumberIndex(line);
            int lastIndex2 = lastWordIndex(line);
            if(lastIndex < 0 || lastIndex2 > lastIndex)
                lastIndex = lastIndex2;
            System.out.println("First: " + firstIndex + " Last: " + lastIndex + " Sum: " + (extract(line, firstIndex) * 10 + extract(line, lastIndex)));
    }
    
    private int firstNumber(String str) {
        char[] chars = str.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            if(Character.isDigit(chars[i])) {
                return Integer.parseInt(str.substring(i, i+1));
            }
        }
        throw new RuntimeException("No digit found in '" + str + "'");
    }
    
    private int lastNumber(String str) {
        char[] chars = str.toCharArray();
        for(int i = chars.length - 1; i >= 0; i--) {
            if(Character.isDigit(chars[i])) {
                return Integer.parseInt(str.substring(i, i+1));
            }
        }
        throw new RuntimeException("No digit found in '" + str + "'");
    }
    
    private int firstNumberIndex(String str) {
        char[] chars = str.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            if(Character.isDigit(chars[i])) {
                //System.out.println("FirstNumberIndex " + i);
                return i;
            }
        }
        return -1;
    }
    
    private int lastNumberIndex(String str) {
        char[] chars = str.toCharArray();
        for(int i = chars.length - 1; i >= 0; i--) {
            if(Character.isDigit(chars[i])) {
                //System.out.println("LastNumberIndex " + i);
                return i;
            }
        }
        return -1;
    }
    
    private int firstWordIndex(String str) {
        int idx = -1;
        for(int i = 0; i < NUMBERS.length; i++) {
            if(idx < 0)
                idx = str.indexOf(NUMBERS[i]);
            else {
                int i2 = str.indexOf(NUMBERS[i]);
                if(i2 >= 0 && i2 < idx)
                    idx = i2;
            }
        } 
        return idx;
    }
    
    private int lastWordIndex(String str) {
        int idx = -1;
        for(int i = 0; i < NUMBERS.length; i++) {
            if(idx < 0)
                idx = str.lastIndexOf(NUMBERS[i]);
            else {
                if(str.lastIndexOf(NUMBERS[i]) > idx)
                    idx = str.lastIndexOf(NUMBERS[i]);
            }
        } 
        return idx;
    }
    
    private int extract(String str, int idx) {
        if(Character.isDigit(str.charAt(idx))) {
            return Integer.parseInt(str.substring(idx, idx+1));
        } else {
            str = str.substring(idx);
            for(int i = 0; i < NUMBERS.length; i++) {
                if(str.startsWith(NUMBERS[i]))
                    return i + 1;
            }
        }
        return -1;
    }
    
}

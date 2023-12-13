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
import java.util.List;

/**
 *
 * @author criz_
 */
public class Puzzle13 {
    
    private final File input;
    
    public Puzzle13(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<String> pattern = new ArrayList<>();
        String line;
        int sum = 0, score;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank()) {
                score = reflectScore(pattern);
                System.out.println("Score: " + score);
                sum += score;
                pattern.clear();
            } else {
                //System.out.println("Line: " + line);
                pattern.add(line);
            }
        }
        score = reflectScore(pattern);
        System.out.println("Score: " + score);
        sum += score;
        return "" + sum;
    }
    
    private int reflectScore(List<String> list) {
        int[] rows = findScore(list);
        int[] cols = findScore(transpose(list));
        return (rows[0] > cols[0]) ? rows[1] * 100 : cols[1];
    }
    
    /*private int findScore(List<String> list) {
        int center, score = 0;
        int removed = 0;
        
        if(list.size() % 2 == 1) {
            // remove either first or last
            if(list.get(0).equals(list.get(list.size() - 2))) {
                // last line go; first one matches second from end
                list.remove(list.size() - 1);
            } else if(list.get(1).equals(list.get(list.size() - 1))) {
                // first line go
                list.remove(0);
                // this row gets included in the score
                removed = 1;
            } else {
                System.out.println("Neither end matches");
                return 0;
            }
        }
        // we know the list now has an even number of rows
        int limit = list.size() / 2;
        for(center = 0; center < limit; center++) {
            String left = list.get(center);
            String right = list.get(list.size() - center - 1);
            if(!left.equals(right)) {
                System.out.println("Mismatch: " + left + " != " + right);
                return 0;
            }
            System.out.println("Match " + left + " == " + right);
        }
        return limit + removed;
    }*/
    
    private int[] findScore(List<String> list) {
        int[] ret = new int[2];
        int center, score = 0;
        int removed = 0;
        int limit = list.size() - 1;
        
        for(center = 0; center < limit; center++) {
            String left = list.get(center);
            String right = list.get(center + 1);
            if(left.equals(right)) {
                System.out.println("Row: " + center + " Match: " + left + " == " + right);
                break;
            }
        }
        if(center < limit) {
            // are we left or right of center?
            if(limit - center < center) {
                // right side, make sure all rows to the end mirror
                //System.out.println("Go down");
                for(score = 1; score + center < limit; score++) {
                    String left = list.get(center + score + 1);
                    String right = list.get(center - score);
                    //System.out.println("Row: " + score + " Match: " + left + " ?= " + right);
                    if(!left.equals(right))
                        return ret;
                }
            } else {
                // left side, make sure all rows to the start mirror
                //System.out.println("Go up");
                for(score = 1; score < center; score++) {
                    String left = list.get(center + score + 1);
                    String right = list.get(center - score);
                    //System.out.println("Row: " + score + " Match: " + left + " ?= " + right);
                    if(!left.equals(right))
                        return ret;
                }
            }
            ret[0] = score;
            ret[1] = center + 1;
        }
        return ret;
    }
    
    private List<String> transpose(List<String> list) {
        List<String> ret = new ArrayList<>();
        int len = list.get(0).length();
        for(int x = 0; x < len; x++) {
            StringBuilder str = new StringBuilder();
            for(String line : list) {
                str.append(line.charAt(x));
            }
            String s = str.toString();
            ret.add(s);
        }
        return ret;
    }
    
}

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
public class Puzzle6 {
    
    private final File input;
    
    public Puzzle6(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line;
        long sum = 1;
        line = in.readLine().split(":")[1].trim();
        long[] times = getNumArray(line);
        System.out.println("Times:");
        printArray(times);
        line = in.readLine().split(":")[1].trim();
        long[] limit = getNumArray(line);
        System.out.println("Distance:");
        printArray(limit);
        for(int i = 0; i < times.length; i++) {
            if(times[i] == 0)
                continue;
            sum *= getNumRoots(times[i], limit[i]);
        }
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line;
        long sum = 1;
        line = in.readLine().split(":")[1].trim();
        long times = Long.parseLong(line.replace(" ", ""));
        System.out.println("Times: " + times);
        line = in.readLine().split(":")[1].trim();
        long limit = Long.parseLong(line.replace(" ", ""));
        System.out.println("Distance: " + limit);
        sum = getNumRoots(times, limit);
        return "" + sum;
    }

    /**
     * In a race of t seconds with the button pressed for p seconds,
     * our boat will travel p * (t - p) meters. This produces a parabola
     * given by the equation pt - p^2. We want the integer roots of p
     * that lets us travel more than d meters, or pt - p^2 - d = 0.
     * @param time Duration of the race
     * @param distance Current record we need to beat
     * @return Number of integer roots that lets us beat the given distance
     */
    private long getNumRoots(long time, long distance) {
        double inner = Math.sqrt((time * time) - 4*distance);
        System.out.println(" Time: " + time + " Dist: " + distance + " Inner: " + inner);
        double root1 = (time + inner) / 2;
        double root2 = (time - inner) / 2;
        System.out.println("  Roots: " + root1 + " " + root2);
        if(inner == Math.ceil(inner)) {
            root2 += 1;
        }
        if(inner == Math.floor(inner)) {
            root1 -= 1;
        }
        root1 = Math.floor(root1);
        root2 = Math.ceil(root2);
        System.out.println("  Roots: " + root1 + " " + root2);
        return ((long)(root1 - root2) + 1);
    }
    
    private long[] getNumArray(String str) {
        String[] num = str.split(" ");
        long[] ret = new long[num.length];
        int idx = 0;
        for(int i = 0; i < num.length; i++) {
            if(num[i].isBlank())
                continue;
            try {
                ret[idx++] = Long.parseLong(num[i]);
            } catch(NumberFormatException e) {
            }
        }
        return startOf(ret, idx);
    }
    
    private long[] startOf(long[] array, int length) {
        long[] ret = new long[length];
        for(int i = 0; i < length; i++)
            ret[i] = array[i];
        return ret;
    }
    
    private void printArray(long[] array) {
        for(int i = 0; i < array.length; i++) {
            if(i % 20 == 0) {
                //System.out.print("\n" + i + ":");
            }
            System.out.print(" " + array[i]);
        }
        System.out.println("");
    }
    
}

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author criz_
 */
public class Puzzle9 {
    
    private final File input;
    
    public Puzzle9(File in) {
        input = in;
    }
    
    // 1930746032
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<Long> numbers;
        long[] array;
        long sum = 0;
        String line;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            array = getNumArray(line);
            numbers = new ArrayList<>();
            while(array != null) {
                //printArray(array);
                numbers.add(array[0]);
                array = reduce(array);
            }
            sum += numbers.stream().reduce(0L, Long::sum);
            //System.out.println("Sum: " + sum + " Next: " + acc);
        }
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<Long> numbers;
        long[] array;
        long sum = 0, acc;
        String line;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            array = getNumArray2(line);
            numbers = new ArrayList<>();
            while(array != null) {
                //printArray(array);
                numbers.add(array[0]);
                array = reduce2(array);
            }
            acc = 0;
            for(int i = numbers.size(); i > 0 ; i--)
                acc = numbers.get(i - 1) - acc;
            sum += acc;
            System.out.println("Sum: " + sum + " Next: " + acc);
        }
        return "" + sum;
    }
    
    // Reverses array, use for part1 
    private long[] getNumArray(String str) {
        String[] num = str.split(" ");
        long[] ret = new long[num.length];
        for(int i = 0; i < num.length; i++) {
            try {
                ret[num.length - i - 1] = Long.parseLong(num[i]);
            } catch(NumberFormatException e) {
            }
        }
        return ret;
    }
    
    private long[] getNumArray2(String str) {
        String[] num = str.split(" ");
        long[] ret = new long[num.length];
        for(int i = 0; i < num.length; i++) {
            try {
                ret[i] = Long.parseLong(num[i]);
            } catch(NumberFormatException e) {
            }
        }
        return ret;
    }
    
    private long[] reduce(long[] array) {
        long[] ret = new long[array.length - 1];
        boolean nonZero = false;
        for(int i = 0; i < ret.length; i++) {
            ret[i] = array[i] - array[i + 1];
            if(ret[i] != 0)
                nonZero = true;
        }
        if(nonZero)
            return ret;
        return null;
    }
    
    private long[] reduce2(long[] array) {
        long[] ret = new long[array.length - 1];
        boolean nonZero = false;
        for(int i = 0; i < ret.length; i++) {
            ret[i] = array[i + 1] - array[i];
            if(ret[i] != 0)
                nonZero = true;
        }
        if(nonZero)
            return ret;
        return null;
    }
    
    private void printArray(long[] array) {
        for(int i = 0; i < array.length; i++) {
            /*if(i % 20 == 0) {
                System.out.print("\n" + i + ":");
            }*/
            System.out.print(" " + array[i]);
        }
        System.out.println("");
    }
    
}

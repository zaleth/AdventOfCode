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
public class Puzzle14 {
    
    private final File input;
    
    public Puzzle14(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<String> list = new ArrayList<>();
        String line;
        int sum = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            System.out.println(line);
            list.add(line);
        }
        System.out.println(" -- break --");
        list = transpose(list);
        for(int i = 0; i < list.size(); i++) {
            //System.out.println(list.get(i));
            //System.out.println("Line " + i);
            String s = rollNorth(list.get(i));
            System.out.println(s);
            sum += scoreRow(s);
        }
        System.out.println(" -- break --");
        list = transpose(list);
        for(int i = 0; i < list.size(); i++) {
            //System.out.println(list.get(i));
            //System.out.println("Line " + i);
            String s = rollNorth(list.get(i));
            System.out.println(s);
            sum += scoreRow(s);
        }
        return "" + sum;
    }
    
    private String rollNorth(String row) {
        int first = 0;
        int free = row.indexOf(".");
        //System.out.println("  " + row);
        while(free > -1 && (first = row.indexOf("O", free)) > -1) {
            char[] cells = row.toCharArray();
            int tmp = row.indexOf("#", free);
            //System.out.println("  Free " + free + " first " + first + " tmp " + tmp);
            if(tmp < 0 || first < tmp) {
                cells[free] = 'O';
                cells[first] = '.';
            } else {
                cells[first] = '.';
                while(cells[first - 1] == '.')
                    first--;
                cells[first] = 'O';
                free = first;
            }
            row = new String(cells);
            free = row.indexOf(".", free);
            //System.out.println("  " + row);
        }
        return row;
    }
    
    private int scoreRow(String row) {
        int sum = 0, len = row.length();
        for(int i = 0; i < len; i++) {
            if(row.charAt(i) == 'O')
                sum += (len - i);
        }
        return sum;
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

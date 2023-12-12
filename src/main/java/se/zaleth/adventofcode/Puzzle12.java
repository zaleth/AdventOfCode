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
public class Puzzle12 {
    
    private final File input;
    
    public Puzzle12(File in) {
        input = in;
        int[] a = {3, 2, 1};
        //System.out.println("Total: " + decode("?###????????", a));
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line;
        int sum = 0, ways;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            String map = line.split(" ")[0];
            int[] groups = getNumArray(line.split(" ")[1]);
            System.out.print("Line: " + map);
            printArray(groups);
            ways = decode(map, groups);
            System.out.println("  Ways: " + ways);
            sum += ways;
        }
        return "" + sum;
    }

    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line;
        int sum = 0, ways, lines = 0, exp;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            ways = 0;
            String[] halves = line.split(" ");
            //System.out.println("Orig: " + halves[0]);
            if(decode(halves[0], getNumArray(halves[1])) == 1) {
                sum += 1;
                lines++;
                continue;
            }
            String map = shrink(unfold(halves[0], '?', 5));
            int[] groups = getNumArray(unfold(halves[1], ',', 5));
            lines++;
            //System.out.println("Unfold: " + map);
            
            map = simplify(map, groups);
            exp = map.split("\\?").length;
            //System.out.println("Line: " + lines + " (2^" + exp + ")");
            /*if(exp > 10) {
                System.out.print("Fails: " + map);
                printArray(groups);
            } else {
                ways = decode(map, groups);
            }
            System.out.println("  Ways: " + ways);*/
            sum += ways;
        }
        return "" + sum;
    }

    private int decode(String row, int[] broken) {
        int ways = 1;
        int idx = 0;
        if(row == null || row.isBlank())
            return 0;
        if(broken.length < 1)
            return 0;
        if(row.length() == (broken.length - 1 + reduce(broken)))
            return 1;
        if(row.indexOf('?') < 0)
            return matches(row, broken);
        String[] halves = row.split("\\?", 2);
        return decode(halves[0] + '.' + halves[1], broken) + decode(halves[0] + '#' + halves[1], broken);
    }
    
    private int matches(String row, int[] broken) {
        int[] array = copy(broken);
        char[] springs = row.toCharArray();
        int idx = 0, bpos = 0;
        int i = 0;
        if(countGroups(row) != broken.length)
            return 0;
        //System.out.print("Line: " + row);
        //printArray(broken);
        while(i < (springs.length) && springs[i] == '.')
            i++;
        for(; i < springs.length; i++) {
            if(springs[i] == '.') {
                if(array[0] == 0) {
                    array = pop(array);
                    while(i < (springs.length - 1) && springs[i+1] == '.')
                        i++;
                } else {
                    //System.out.println(i + ". = 0");
                    return 0;
                }
            } else {
                if(array.length == 0 || array[0] == 0) {
                    //System.out.println(i + "# = 0");
                    return 0;
                }
                array[0]--;
            }
        }
        if(array.length == 0 || (array.length == 1 && array[0] == 0)) {
            //System.out.println(" = 1");
            return 1;
        }
        return 0;
    }
    
    private String shrink(String in) {
        StringBuilder str = new StringBuilder();
        int idx = 0;
        //System.out.println("Shrink: " + in);
        while(idx < in.length()) {
            if(in.charAt(idx) == '.') {
                str.append('.');
                while(in.charAt(idx) == '.') {
                    idx++;
                    if(idx == in.length())
                        return str.toString();
                }
            }
            str.append(in.charAt(idx++));
        }
        return str.toString();
    }
    
    private int countGroups(String str) {
        int groups = 0, idx = 0;
        while(idx < str.length()) {
            if(idx < 0)
                return groups;
            if(str.charAt(idx) == '.') {
                idx = str.indexOf("#", idx);
            } else {
                idx = str.indexOf(".", idx);
                groups++;
            }
        }
        return groups;
    }
    
    private int[] getNumArray(String str) {
        String[] num = str.split(",");
        int[] ret = new int[num.length];
        for(int i = 0; i < num.length; i++) {
            try {
                ret[i] = Integer.parseInt(num[i]);
            } catch(NumberFormatException e) {
            }
        }
        return ret;
    }
    
    private int[] copy(int[] array) {
        int[] ret = new int[array.length];
        System.arraycopy(array, 0, ret, 0, array.length);
        return ret;
    }
    
    private int[] pop(int[] array) {
        int[] ret;
        if(array[0] > 1) {
            ret = new int[array.length];
            System.arraycopy(array, 0, ret, 0, array.length);
            ret[0] = ret[0] - 1;
        } else {
            ret = new int[array.length - 1];
            System.arraycopy(array, 1, ret, 0, ret.length);
        }
        return ret;
    }
    
    private int reduce(int[] array) {
        int sum = 0;
        for(int i = 0; i < array.length; i++)
            sum += array[i];
        return sum;
    }
    
    private void printArray(int[] array) {
        for(int i = 0; i < array.length; i++) {
            System.out.print(" " + array[i]);
        }
        System.out.println("");
    }
    
    private String unfold(String base, char sep, int times) {
        StringBuilder str = new StringBuilder(base);
        //System.out.println("Base '" + base + "' Sep '" + sep + "' Times: " + times);
        for(int i = 1; i < times; i++) {
            str.append(sep);
            str.append(base);
        }
        return str.toString();
    }
    
    private String simplify(String str, int[] array) {
        String[] parts = str.split("\\.");
        List<String> list = new ArrayList<>();
        System.out.println(str);
        printArray(array);
        for(String s : parts) {
            if(s == null || s.isBlank())
                continue;
            //System.out.println("  " + s);
            list.add(s);
        }
        for(int idx = array.length - 1; idx >= 0; idx--) {
            if(list.get(idx).length() == array[idx]) {
                
            }
        }
        return "";
    }
    
}

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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author criz_
 */
public class Puzzle5 {
    
    private static final int MAX_INDEX = 100;
    
    private class Mapping {
        
        private class Range {
            
            long start, stop, offset;
            
            public Range(long start, long length, long offset) {
                this.start = start;
                this.stop = start + length;
                this.offset = offset;
            }
            
        }
        
        private final Set<Range> ranges = new HashSet<>();
        
        public void addMapping(long start, long length, long offset) {
            //System.out.println("  Map " + start + " to " + (start + offset) + " for length " + length);
            ranges.add(new Range(start, length, offset));
        }
        
        public long map(long index) {
            for(Range r : ranges) {
                if(index >= r.start && index < r.stop) {
                    //System.out.println("Mapping " + index + " to " + (index + r.offset));
                    return index + r.offset;
                }
            }
            return index;
        }
        
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            for(int i = 0; i < MAX_INDEX; i++) {
                if(i % 20 == 0) {
                    str.append("\n" + i + ":");
                }
                str.append(" " + map(i));
            }
            return str.toString();
        }
        
    }
    
    private class Range {
        
        private long from, to;

        public Range(long from, long to) {
            this.from = from;
            this.to = to;
        }

        public long getFrom() {
            return from;
        }

        public void setFrom(long from) {
            this.from = from;
        }
        
        public long getTo() {
            return to;
        }
        
        public void setTo(long to) {
            this.to = to;
        }
                
    }
    
    private final File input;
    
    public Puzzle5(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line, from = "seed", to;
        long sum = 0;
        long[] seeds = getNumArray(in.readLine().split(":")[1].trim()), target = copy(seeds);
        printArray(seeds);
        Mapping map;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            line = line.split(" ")[0];
            //System.out.println("Line: " + line);
            String str = line.split("-")[0];
            if(!str.equals(from)) {
                System.out.println("Non-linear progression: expect '" + from + "', got '" + str + "'");
                return "Fail";
            }
            to = line.split("-")[2];
            System.out.println("Mapping " + from + " to " + to);
            map = new Mapping();
            while((line = in.readLine()) != null && !line.isBlank()) {
                long[] range = getNumArray(line);
                map.addMapping(range[1], range[2], range[0] - range[1]);
            }
            //printArray(map);
            //System.out.println(map);
            for(int i = 0; i < target.length; i++) {
                target[i] = map.map(target[i]);
            }
            printArray(target);
            from = to;
        }
        sum = min(target);
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line, from = "seed", to;
        Set<Range> ranges = new HashSet<>();
        long sum = 0;
        long[] seeds = getNumArray(in.readLine().split(":")[1].trim()), target = copy(seeds);
        for(int i = 0; i < seeds.length; i += 2) {
            ranges.add(new Range(seeds[i], seeds[i] + seeds[i+1] - 1));
        }
        printRanges(ranges);
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            line = line.split(" ")[0];
            //System.out.println("Line: " + line);
            String str = line.split("-")[0];
            if(!str.equals(from)) {
                System.out.println("Non-linear progression: expect '" + from + "', got '" + str + "'");
                return "Fail";
            }
            to = line.split("-")[2];
            System.out.println("Mapping " + from + " to " + to);
            while((line = in.readLine()) != null && !line.isBlank()) {
                Set<Range> updated = new HashSet<>();
                // dBase, sBase, len
                long[] range = getNumArray(line);
                System.out.println("  " + line);
                for(Range r : ranges) {
                    long start = r.getFrom();
                    long end = r.getTo();
                    if(start >= (range[1] + range[2]) || end < range[1]) {
                        updated.add(r);
                    } else {
                        // do we have some unaffected numbers at start that need to be snipped off to their own range?
                        if(start < range[1]) {
                            updated.add(new Range(r.getFrom(), range[1] - 1));
                            start = range[1];
                        }
                        // do we have some unaffected numbers at end that need to be snipped off to their own range?
                        if(end >= range[1] + range[2]) {
                            updated.add(new Range(range[1] + range[2], r.getTo()));
                            end = range[1] + range[2] - 1;
                        }
                        // finally, the bit in the middle
                        long delta = range[0] - range[1];
                        updated.add(new Range(start + delta, end + delta));
                    }
                }
                mergeRanges(updated);
                ranges = updated;
                printRanges(ranges);
            }
            from = to;
        }
        sum = min(target);
        return "" + sum;
    }
    
    private long[] getNumArray(String str) {
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
    
    private int[] getRange(int length) {
        int[] ret = new int[length];
        for(int i = 0; i < length; i++)
            ret[i] = i;
        return ret;
    }
    
    private long[] copy(long[] array) {
        long[] ret = new long[array.length];
        for(int i = 0; i < array.length; i++)
            ret[i] = array[i];
        return ret;
    }
    
    private int max(int[] array) {
        int maxVal = array[0];
        int index = 0;
        for(int i = 1; i < array.length; i++) {
            if(array[i] > maxVal) {
                maxVal = array[i];
                index = i;
            }
        }
        return maxVal;
    }
    
    private long min(long[] array) {
        long minVal = array[0];
        int index = 0;
        for(int i = 1; i < array.length; i++) {
            if(array[i] < minVal) {
                minVal = array[i];
                index = i;
            }
        }
        return minVal;
    }
    
    // In-place modification
    private void mergeRanges(Set<Range> ranges) {
        List<Range> list = new ArrayList<>(ranges);
        list.sort((r1, r2) -> ((int) (r1.getFrom() - r2.getFrom())));
        ranges.clear();
        Iterator<Range> it = list.iterator();
        Range last, curr;
        for(last = it.next(); it.hasNext(); ) {
            curr = it.next();
            if(last.getTo() + 1 == curr.getFrom()) {
                last.setTo(curr.getTo());
            } else {
                ranges.add(last);
                last = curr;
            }
        }
        ranges.add(last);
    }
    
    private void printArray(long[] array) {
        for(int i = 0; i < array.length; i++) {
            if(i % 20 == 0) {
                System.out.print("\n" + i + ":");
            }
            System.out.print(" " + array[i]);
        }
        System.out.println("");
    }
    
    private void printRanges(Set<Range> ranges) {
        for(Range range : ranges) {
            for(long i = range.getFrom(); i < range.getTo(); i++)
                System.out.print(" " + i);
            System.out.println("");
        }
    }
    
}

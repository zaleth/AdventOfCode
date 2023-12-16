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
import java.util.Map;

/**
 *
 * @author criz_
 */
public class Puzzle15 {
    
    private class Lens {
        String label;
        int focus;
        public Lens(String label, int focus) {
            this.label = label;
            this.focus = focus;
        }
        @Override
        public String toString() {
            return "[" + label + " " + focus + "]";
        }
    }
    
    private class Box {
        List<Lens> slots = new ArrayList<>();
        
        public void add(String label, int focus) {
            for(int i = 0; i < slots.size(); i++) {
                if(slots.get(i).label.equals(label)) {
                    slots.set(i, new Lens(label, focus));
                    /*while(i <= slots.size()) {
                        slots.set(i, slots.get(i + 1));
                    }*/
                    return;
                }
            }
            slots.add(new Lens(label, focus));
        }
        
        public Lens pull(String label) {
            Lens ret = null;
            for(int i = 0; i < slots.size(); i++) {
                if(slots.get(i).label.equals(label)) {
                    ret = slots.remove(i);
                    /*while(i <= slots.size()) {
                        slots.set(i, slots.get(i + 1));
                    }*/
                    break;
                }
            }
            if(ret != null) {
            }
            return ret;
        }
        
        public int score() {
            int sum = 0;
            for(int i = 0; i < slots.size(); i++) {
                sum += (i+1) * slots.get(i).focus;
            }
            return sum;
        }
        
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            for(Lens l : slots) {
                if(str.length() > 0)
                    str.append(" ");
                str.append(l);
            }
            return str.toString();
        }
    }
    
    private final File input;
    
    public Puzzle15(File in) {
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
            for(String str : line.split(",")) {
                //System.out.println("H(" + str + ")=" + hash(str));
                sum += hash(str);
            }
        }
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        Box[] boxes = new Box[256];
        String line;
        int sum = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            System.out.println("Line: " + line);
            for(String str : line.split(",")) {
                String[] halves;
                int idx;
                if(str.indexOf('-') > -1) {
                    halves = str.split("-");
                    idx = hash(halves[0]);
                    if(boxes[idx] != null) {
                        boxes[idx].pull(halves[0]);
                    }
                } else {
                    halves = str.split("=");
                    idx = hash(halves[0]);
                    int focus = Integer.parseInt(halves[1]);
                    if(boxes[idx] == null) {
                        boxes[idx] = new Box();
                    }
                    boxes[idx].add(halves[0], focus);
                }
                /*System.out.println("After '" + str + "'");
                for(int i = 0; i < 256; i++) {
                    if(boxes[i] != null)
                        System.out.println("Box " + i + ": " + boxes[i]);
                }*/
                //System.out.println("H(" + halves[0] + ")=" + hash(halves[0]));
                //sum += hash(str);
            }
        }
        for(int i = 0; i < 256; i++) {
            if(boxes[i] != null)
                sum += (i+1) * boxes[i].score();
        }
        return "" + sum;
    }
    
    private int hash(String input) {
        int current = 0;
        for(int i = 0; i < input.length(); i++) {
            int chr = (int) input.charAt(i);
            current += chr;
            current *= 17;
            current = current & 0xFF; // quicker mod 256
        }
        return current;
    }
    
}

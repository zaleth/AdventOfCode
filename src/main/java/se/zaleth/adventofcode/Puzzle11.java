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
public class Puzzle11 {
    
    private class Graph {
        
        private class Node {
            int x, y;

            public Node(int x, int y) {
                this.x = x;
                this.y = y;
                //System.out.println(this);
            }
            
            public int distanceTo(Node n) {
                return Math.abs(x - n.x) + Math.abs(y - n.y);
            }
            
            @Override
            public String toString() {
                return "(" + x + "," + y + ")";
            }
        }
        
        private List<Node> nodes = new ArrayList<>();
        
        public void addNode(int x, int y) {
            nodes.add(new Node(x, y));
        }
        
        public long spanningDistance() {
            Node from = nodes.remove(0), best = null;
            long dist = 0;
            while(!nodes.isEmpty()) {
                long minDist = -1;
                for(Node node : nodes) {
                    if(minDist < 0 || from.distanceTo(node) < minDist) {
                        best = node;
                        minDist = from.distanceTo(node);
                    }
                }
                dist += minDist;
                //System.out.println(from + " -> " + best + " = " + minDist);
                from = best;
                nodes.remove(from);
            }
            return dist;
        }
        
        public long connectedDistance() {
            long dist = 0;
            while(!nodes.isEmpty()) {
                Node from = nodes.remove(0);
                for(Node node : nodes) {
                    dist += from.distanceTo(node);
                    //System.out.println(from + " -> " + node + " = " + from.distanceTo(node));
                }
            }
            return dist;
        }
    }
    
    // set SCALE = 1 to solve part1
    private final static int SCALE = 999999;
    private final File input;
    
    public Puzzle11(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<String> rows = new ArrayList<>();
        List<Integer> blankRows = new ArrayList<>(), blankCols = new ArrayList<>();
        String line;
        long sum = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //sum = line.length();
            //System.out.println("Line: " + line);
            if(!line.contains("#"))
                blankRows.add((int)sum);
            rows.add(line);
            sum++;
        }
        rows = transpose(rows);
        sum = 0;
        for(String str : rows) {
            if(!str.contains("#"))
                blankCols.add((int)sum);
            sum++;
        }
        rows = transpose(rows);
        /*for(String str : rows)
            System.out.println(str);*/
        Graph g = new Graph();
        int addRows = 0, addCols;
        System.out.println("AddRows: " + blankRows.size());
        printArray(blankRows);
        System.out.println("AddCols: " + blankCols.size());
        printArray(blankCols);
        for(int y = 0; y < rows.size(); y++) {
            String s = rows.get(y);
            if(addRows < blankRows.size() && blankRows.get(addRows) < y) {
                addRows++;
            }
            addCols = 0;
            for(int x = 0; x < s.length(); x++) {
                if(addCols < blankCols.size() && blankCols.get(addCols) < x) {
                    addCols++;
                }
                if(s.charAt(x) == '#') {
                    //System.out.print("(" + x + "," + y + ") -> ");
                    g.addNode(x + addCols * SCALE, y + addRows * SCALE);
                }
            }
        }
        sum = g.connectedDistance();
        return "" + sum;
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
    
    private void printArray(List<Integer> array) {
        for(int i = 0; i < array.size(); i++) {
            System.out.print(" " + array.get(i));
        }
        System.out.println("");
    }
    
}

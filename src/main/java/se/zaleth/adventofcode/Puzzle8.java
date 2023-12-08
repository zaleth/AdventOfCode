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
import java.util.stream.Collectors;

/**
 *
 * @author criz_
 */
public class Puzzle8 {
    
    private final File input;
    
    public Puzzle8(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        Map<String,String> network = new HashMap<>();
        String line, dir, room = "AAA";
        int sum = 0, idx = 0;
        dir = in.readLine().trim();
        
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            String key = line.split(" ")[0];
            String left = line.split("\\(")[1].split(",")[0].trim();
            String right = line.split(",")[1].trim().substring(0, 3);
            //System.out.println(key + " -> " + left + ", " + right);
            network.put(key + "L", left);
            network.put(key + "R", right);
        }
        while(!"ZZZ".equals(room)) {
            room = network.get(room + dir.charAt(idx++));
            sum++;
            if(idx >= dir.length())
                idx = 0;
        }
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        Map<String,String> network = new HashMap<>();
        String line, dir;
        //String[] rooms = new String[0];
        List<String> rooms;
        int idx = 0;
        long sum = 0, z = 0;
        dir = in.readLine().trim();
        
        System.out.println("Dirs: " + dir.length());
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            String key = line.split(" ")[0];
            String left = line.split("\\(")[1].split(",")[0].trim();
            String right = line.split(",")[1].trim().substring(0, 3);
            //System.out.println(key + " -> " + left + ", " + right);
            network.put(key + "L", left);
            network.put(key + "R", right);
        }
        rooms = network.keySet().stream().filter(s -> s.endsWith("AL")).map(s -> s.substring(0, 3)).collect(Collectors.toList());
        System.out.println("Rooms: " + rooms.size());
        rooms.stream().forEach(s -> System.out.println(" " + s));
        while(z != rooms.size()) {
            if(sum % 100000000L == 0L)
                System.out.println("Turn " + sum);
            for(int i = 0; i < rooms.size(); i++) {
                //System.out.println(" " + rooms.get(i) + "," + dir.charAt(idx) + " -> " + network.get(rooms.get(i) + dir.charAt(idx)));
                rooms.set(i, network.get(rooms.get(i) + dir.charAt(idx)));
            }
            sum++;
            z = rooms.stream().filter(s -> s.endsWith("Z")).count();
            if(rooms.stream().filter(s -> s.endsWith("A")).count() == 6) {
                System.out.println("Infinite loop after " + sum + " turns!");
                return "";
            }
            if(++idx >= dir.length())
                idx = 0;
        }
        return "" + sum;
    }
    /**
     * Room 0: 23147 = 79 x 293
     * Room 1: 20803 = 71 x 293
     * Room 2: 19631 = 67 x 293
     * Room 3: 12599 = 43 x 293
     * Room 4: 21389 = 73 x 293
     * Room 5: 17873 = 61 x 293
     * @return
     * @throws IOException 
     */
    public String solve3() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        Map<String,String> network = new HashMap<>();
        String line, dir;
        //String[] rooms = new String[0];
        List<String> rooms;
        int[] factor;
        int sum = 0, idx, common = 293;
        long z = 0;
        dir = in.readLine().trim();
        
        System.out.println("Dirs: " + dir.length());
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            String key = line.split(" ")[0];
            String left = line.split("\\(")[1].split(",")[0].trim();
            String right = line.split(",")[1].trim().substring(0, 3);
            //System.out.println(key + " -> " + left + ", " + right);
            network.put(key + "L", left);
            network.put(key + "R", right);
        }
        rooms = network.keySet().stream().filter(s -> s.endsWith("AL")).map(s -> s.substring(0, 3)).collect(Collectors.toList());
        System.out.println("Rooms: " + rooms.size());
        factor = new int[rooms.size()];
        rooms.stream().forEach(s -> System.out.println(" " + s));
        /*while(z != rooms.size()) {
            if(sum % 100000000L == 0L)
                System.out.println("Turn " + sum);*/
            for(int i = 0; i < rooms.size(); i++) {
                idx = 0;
                sum = 0;
                String room = rooms.get(i);
                while(!room.endsWith("Z")) {
                    room = network.get(room + dir.charAt(idx));
                    sum++;
                    if(++idx >= dir.length())
                        idx = 0;
                }
                System.out.println("Room " + i + ": " + sum);
                factor[i] = sum / common;
                //System.out.println(" " + rooms.get(i) + "," + dir.charAt(idx) + " -> " + network.get(rooms.get(i) + dir.charAt(idx)));
                //rooms.set(i, network.get(rooms.get(i) + dir.charAt(idx)));
            }
            /*sum++;
            z = rooms.stream().filter(s -> s.endsWith("Z")).count();
            if(rooms.stream().filter(s -> s.endsWith("A")).count() == 6) {
                System.out.println("Infinite loop after " + sum + " turns!");
                return "";
            }
            if(++idx >= dir.length())
                idx = 0;*/
        //}
        z = common;
        for(int i = 0; i < factor.length; i++)
            z *= factor[i];
        return "" + z;
    }
    
}

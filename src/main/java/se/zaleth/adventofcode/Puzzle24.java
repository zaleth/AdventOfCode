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
public class Puzzle24 {
    
    private class Point3 {
        double x, y, z;

        public Point3(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public Point3(String str) {
            String[] vals = str.split(", ");
            x = Double.parseDouble(vals[0].trim());
            y = Double.parseDouble(vals[1].trim());
            z = Double.parseDouble(vals[2].trim());
        }
        @Override
        public String toString() {
            return "(" + x + "," + y + "," + z + ")";
        }
    }
    
    private class Hail {
        Point3 pos, vel;
        
        public Hail(String str) {
            String[] halves = str.split(" @ ");
            pos = new Point3(halves[0]);
            vel = new Point3(halves[1]);
        }
        
        public Point3 atTime(double t) {
            return new Point3(pos.x + vel.x*t, pos.y + vel.y*t, pos.z + vel.z*t);
        }
        /**
         * px + vx*t1 = h.px + h.vx*t2 =>
         * t1 = (h.px - px + h.vx*t2) / vx
         * py + vy*t1 = h.py + h.vy*t2 =>
         * py + vy * ((h.px - px + h.vx*t2) / vx) = h.py +h.vy*t2 =>
         * py - h.py + vy * ((h.px - px) / vx) = t2*h.vy - t2*(vy*h.vx)/vx 
         * py - h.py + vy * ((h.px - px) / vx) = t2 (h.vy - ((vy*h.vx)/vx ))
         * 
         * Now that we know time t2 when H is at the intersection point, we
         * can easily calculate that point and from that find time t1 when
         * the first particle gets there.
         * 
         * @param h
         * @return 
         */
        public Point3 intersects(Hail h) {
            try {
                double t2 = pos.y - h.pos.y + vel.y * (h.pos.x - pos.x) / vel.x;
                t2 /= (h.vel.y - (vel.y*h.vel.x /vel.x));
                double t1 = h.pos.x + h.vel.x*t2 - pos.x;
                t1 /= vel.x;
                if(t1 < 0 || t2 < 0)
                    return null;
                //System.out.println("Time t1: " + t1 + " Point: " + atTime(t1));
                //System.out.println("Time t2: " + t2 + " Point: " + h.atTime(t2));
                return atTime(t1);
            } catch(Exception e) {
                
            }
            return null;
        }
        
        @Override
        public String toString() {
            return "Pos: " + pos + "\nVel: " + vel;
        }
    }
    
    private final File input;
    private final double min, max;
    
    public Puzzle24(File in, double min, double max) {
        input = in;
        this.min = min;
        this.max = max;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<Hail> list = new ArrayList<>();
        String line;
        int sum = 0;
        short count = 1;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            Hail hail = new Hail(line);
            /*if(!brick.isLine()) {
                System.out.println("Big brick: " + brick);
            }*/
            //System.out.println(hail);
            list.add(hail);
        }
        System.out.println("# of hails: " + list.size());
        for(int j = 0; j < list.size(); j++) {
            Hail h = list.get(j);
            for(int i = j+1; i < list.size(); i++) {
                Point3 p = h.intersects(list.get(i));
                if(p != null && p.x >= min && p.x <= max && p.y >= min && p.y <= max) {
                    //System.out.println("Hit: " + p);
                    sum++;
                }
            }
        }
        return "" + sum;
    }
    
}

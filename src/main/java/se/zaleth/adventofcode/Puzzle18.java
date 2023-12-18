/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package se.zaleth.adventofcode;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author criz_
 */
public class Puzzle18 {
    
    private final File input;
    private char[][] map;
    
    public Puzzle18(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<String> orders = new ArrayList<>();
        String line;
        int sum = 0, width = 1, height = 1;
        int x = 0, y = 0, startX = 0, startY = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            String[] parts = line.split(" ");
            int d = Integer.parseInt(parts[1]);
            switch(parts[0].charAt(0)) {
                case 'R':
                    x += d;
                    break;
                case 'L':
                    x -= d;
                    break;
                case 'U':
                    y -= d;
                    break;
                case 'D':
                    y += d;
                    break;
            }
            if(x < 0) {
                startX -= x;
                width -= x;
                x = 0;
            } else if(x > width) {
                width = x;
            }
            if(y < 0) {
                startY -= y;
                width -= y;
                y = 0;
            } else if(y > height) {
                height = y;
            }
            orders.add(parts[0] + " " + parts[1]);
        }
        width++;
        height++;
        System.out.println("Dim: " + width + " * " + height + " Start: " + startX + "," + startY);
        map = new char[height][width];
        for(y = 0; y < height; y++)
            for(x = 0; x < width; x++)
                map[y][x] = '.';
        x = startX;
        y = startY;
        map[y][x] = '#';
        for(String s : orders) {
            //System.out.println(s);
            String[] parts = s.split(" ");
            int d = Integer.parseInt(parts[1]);
            switch(parts[0].charAt(0)) {
                case 'R':
                    while(d-- > 0)
                        map[y][++x] = '#';
                    break;
                case 'L':
                    while(d-- > 0)
                        map[y][--x] = '#';
                    break;
                case 'U':
                    while(d-- > 0)
                        map[--y][x] = '#';
                    break;
                case 'D':
                    while(d-- > 0)
                        map[++y][x] = '#';
                    break;
            }
        }
        BufferedImage outline = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(y = 0; y < height; y++) {
            for(x = 0; x < width; x++) {
                outline.setRGB(x, y, map[y][x] == '#' ? 0xFFFFFF : 0);
            }
            //System.out.println(new String(map[y]));
        }
        System.out.println("Dig in!");
        floodFill(startX + 1, startY + 1);
        for(y = 0; y < height; y++)
            for(x = 0; x < width; x++)
                if(map[y][x] == '#')
                    sum++;
        BufferedImage filledIn = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(y = 0; y < height; y++) {
            for(x = 0; x < width; x++) {
                filledIn.setRGB(x, y, map[y][x] == '#' ? 0xFFFFFF : 0);
            }
            //System.out.println(new String(map[y]));
        }
        AdventOfCode debug = AdventOfCode.getFrame();
        JPanel panel = new JPanel();
        panel.add(new JLabel(new ImageIcon(outline)));
        panel.add(new JLabel(new ImageIcon(filledIn)));
        debug.add(panel);
        debug.refresh();
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<String> orders = new ArrayList<>();
        List<Point> coords = new ArrayList<>();
        String line;
        long sum = 0, width = 1, height = 1;
        int x = 1, y = 1, startX = 0, startY = 0;
        coords.add(new Point(x, y));
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            String[] parts = line.split(" ");
            //System.out.println("Dist: " + parts[2].substring(2, 7));
            int d = Integer.parseInt(parts[2].substring(2, 7), 16);
            System.out.println("RDLU".charAt(parts[2].charAt(7) - '0') + " " + d);
            switch(parts[2].charAt(7)) {
                case '0':
                    x += d;
                    break;
                case '2':
                    x -= d;
                    break;
                case '3':
                    y -= d;
                    break;
                case '1':
                    y += d;
                    break;
            }
            coords.add(new Point(x, y));
            if(x < startX)
                startX = x;
            if(y < startY)
                startY = y;
            /*if(x < 0) {
                startX -= x;
                width -= x;
                x = 0;
            } else if(x > width) {
                width = x;
            }
            if(y < 0) {
                startY -= y;
                width -= y;
                y = 0;
            } else if(y > height) {
                height = y;
            }
            orders.add(parts[0] + " " + parts[1]);*/
        }
        System.out.println("Offset: " + startX + ", " + startY);
        //coords.add(new Point(1, 1));
        sum = trapArea(coords);
        return "" + sum;
    }
    
    private void floodFill(int startX, int startY) {
        List<Point> queue = new ArrayList<>();
        int width = map[0].length;
        int height = map.length;
                
        queue.add((new Point(startX, startY)));
        while(!queue.isEmpty()) {
            Point p = queue.remove(0);
            if(map[p.y][p.x] == '.') {
                map[p.y][p.x] = '#';
                if(p.x > 0)
                    queue.add(new Point(p.x-1, p.y));
                if(p.x < width)
                    queue.add(new Point(p.x+1, p.y));
                if(p.y > 0)
                    queue.add(new Point(p.x, p.y-1));
                if(p.y < height)
                    queue.add(new Point(p.x, p.y+1));
            }
        }
    }
    
    private long trapArea(List<Point> list) {
        Point p1 = list.get(list.size() - 1), p2 = list.get(0);
        double sum = 0;
        for(int i = 1; i < list.size(); i++) {
            p1 = list.get(i-1);
            p2 = list.get(i);
            System.out.println(p1 + " " + p2 + " acc " + sum);
            sum += (p1.x - p2.x) * (p1.y + p2.y);
        }
        return (long) Math.floor(sum / 2);
    }
}

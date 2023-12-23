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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author criz_
 */
public class Puzzle23 {
    
    private enum Direction { NORTH(0), WEST(1), SOUTH(2), EAST(3); final int value; Direction(int val) {value = val;} };
    
    private class Point {
        final int x, y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Point))
                return false;
            Point p = (Point) o;
            if(y != p.y)
                return false;
            return x == p.x;
        }
        @Override
        public String toString() {
            return "(" + y + "," + x + ")";
        }
    }
    private class Corridor {
        private static final List<Corridor> list = new ArrayList<>();
        public static Corridor find(Point p) {
            for(Corridor c : list) {
                if((c.start.x == p.x || c.end.x == p.x) && (c.start.y == p.y || c.end.y == p.y))
                    return c;
            }
            return null;
        }
        public static Iterator<Corridor> iterator() {
            return list.iterator();
        }
        Point start, end;
        int length;
        boolean oneWay;

        public Corridor(Point start, Point end, int length, boolean oneWay) {
            this.start = start;
            this.end = end;
            this.length = length;
            this.oneWay = oneWay;
            list.add(this);
            System.out.println(this);
        }
        
        @Override
        public String toString() {
            return "from " + start + " to " + end;
        }
    }
    private class Fork {
        final Point coords;
        final Point[] exits = new Point[4];
        public Fork(int x, int y) {
            coords = new Point(x, y);
        }
        public Fork(Point p) {
            coords = p;
        }
    }
    
    private final File input;
    Map<Direction,Point> starts = new HashMap<>();
    Map<Point,Fork> forks = new HashMap<>();
    Corridor start = null, end;
    char[][] map;

    public Puzzle23(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<String> lines = new ArrayList<>();
        String line;
        int sum = 0;
        int x, y = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            System.out.println("Line: " + line);
            lines.add(line);
        }
        map = new char[lines.size()][];
        for(int i = 0; i < map.length; i++) {
            map[i] = lines.get(i).toCharArray();
        }
        System.out.println("Dim: " + map.length + "x" + map[0].length);
        x = lines.get(0).indexOf('.');
        starts.put(Direction.NORTH, new Point(x, y));
        while(!starts.isEmpty()) {
            Map<Direction,Point> map = new HashMap<>(starts);
            starts.clear();
            for(Map.Entry<Direction,Point> e : map.entrySet()) {
                explore(e.getValue(), e.getKey());
            }
        }
        for(Corridor c : Corridor.list) {
            sum += c.length;
        }
        sum += forks.size();
        //explore(new Point(x, y), Direction.NORTH);
        start = Corridor.find(new Point(x, y));
        return "" + sum;
    }
    
    private Corridor explore(Point start, Direction from) {
        Corridor c = Corridor.find(start);
        if(c != null)
            return c;
        Point last = null;
        int x = start.x, y = start.y;
        int len = 1;
        System.out.println("Start at (" + y + "," + x + ")");
        do {
            Map<Direction,Point> check = new HashMap<>();
            int dir = 0;
            if(from != Direction.NORTH && map[y-1][x] != '#') {
                /*if(map[y-1][x] == 'v') {
                    // can't go up the icy slope
                    return null;
                }*/
                dir += 1 << Direction.NORTH.value;
                check.put(Direction.SOUTH, new Point(x, y-1));
            }
            if(from != Direction.WEST && map[y][x-1] != '#') {
                /*if(map[y][x-1] == '>') {
                    // can't go up the icy slope
                    return null;
                }*/
                dir += 1 << Direction.WEST.value;
                check.put(Direction.EAST, new Point(x-1, y));
            }
            if(from != Direction.SOUTH && map[y+1][x] != '#') {
                /*if(map[y+1][x] == '^') {
                    // can't go up the icy slope
                    return null;
                }*/
                dir += 1 << Direction.SOUTH.value;
                check.put(Direction.NORTH, new Point(x, y+1));
            }
            if(from != Direction.EAST && map[y][x+1] != '#') {
                /*if(map[y][x+1] == '<') {
                    // can't go up the icy slope
                    return null;
                }*/
                dir += 1 << Direction.EAST.value;
                check.put(Direction.WEST, new Point(x+1, y));
            }
            System.out.println(" (" + y + "," + x + ") Go " + dir );
            switch(dir) {
                case 0: // dead end
                    return null;
                case 1: //Direction.NORTH
                    //System.out.println(" Go NORTH");
                    last = new Point(x, y);
                    from = Direction.SOUTH;
                    y--;
                    len++;
                    break;
                case 2: //Direction.WEST
                    //System.out.println(" Go WEST");
                    last = new Point(x, y);
                    from = Direction.EAST;
                    x--;
                    len++;
                    break;
                case 4: //Direction.SOUTH
                    //System.out.println(" Go SOUTH");
                    last = new Point(x, y);
                    from = Direction.NORTH;
                    y++;
                    len++;
                    break;
                case 8: //Direction.EAST
                    //System.out.println(" Go EAST");
                    last = new Point(x, y);
                    from = Direction.WEST;
                    x++;
                    len++;
                    break;
                default: // This is a fork
                    System.out.println(" End at (" + y + "," + x + ")");
                    Point end = new Point(x, y);
                    c = new Corridor(start, last, len, false);
                    Fork fork = new Fork(end);
                    fork.exits[from.value] = last;
                    for(Map.Entry<Direction,Point> e : check.entrySet()) {
                        fork.exits[e.getKey().value] = e.getValue();
                    }
                    starts.putAll(check);
                    forks.put(fork.coords, fork);
                    return c;
            }
        } while(y < map.length-1);
        Point end = new Point(x, y);
        return new Corridor(start, end, len, false);
    }
    
    private Point move(int x, int y, Direction from) {
        switch(from) {
            case NORTH:
                y++;
                break;
            case WEST:
                x++;
                break;
            case SOUTH:
                y--;
                break;
            case EAST:
                x--;
                break;
        }
        return new Point(x, y);
    }
}

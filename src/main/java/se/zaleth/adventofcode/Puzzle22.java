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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * So, all bricks are "lines" or various length, meaning they are 1x1xL cubes in size.
 * They therefore have one of three orientations (or four, if we want to treat 1x1x1
 * separate). 
 * @author criz_
 */
public class Puzzle22 {
    
    private enum Orientation {
        X, Y, Z, CUBE;
    }
    
    private class Point3 {
        int x, y, z;

        public Point3(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public Point3(String str) {
            String[] vals = str.split(",");
            x = Integer.parseInt(vals[0]);
            y = Integer.parseInt(vals[1]);
            z = Integer.parseInt(vals[2]);
        }
        @Override
        public String toString() {
            return "(" + x + "," + y + "," + z + ")";
        }
    }
    
    private class Brick implements Comparable<Brick> {
        final Set<Short> restsOn = new HashSet<>(), supports = new HashSet<>();
        Point3 left, right;
        Orientation o;
        short id;
        public Brick(String str, short id) {
            String[] halves = str.split("~");
            left = new Point3(halves[0]);
            right = new Point3(halves[1]);
            this.id = id;
            if(left.x != right.x) {
                o = Orientation.X;
                if(left.x > right.x) {
                    Point3 t = left;
                    left = right;
                    right = t;
                }
            } else if(left.y != right.y) {
                o = Orientation.Y;
                if(left.y > right.y) {
                    Point3 t = left;
                    left = right;
                    right = t;
                }
            } else if(left.z != right.z) {
                o = Orientation.Z;
                if(left.z > right.z) {
                    Point3 t = left;
                    left = right;
                    right = t;
                }
            } else
                o = Orientation.CUBE;
        }
        public boolean fall() {
            switch(o) {
                case X:
                    if(left.z == 1) {
                        restsOn.clear();
                        return false;
                    }
                    for(int x = left.x; x <= right.x; x++) {
                        if(field[left.z-1][left.y][x] > 0) {
                            short bid = field[left.z-1][left.y][x];
                            restsOn.add(bid);
                            bricks.get(bid - 1).supports.add(id);
                        }
                    }
                    if(!restsOn.isEmpty())
                        return false;
                    if(!supports.isEmpty()) {
                        for(short s : supports) {
                            bricks.get(s - 1).restsOn.remove(id);
                        }
                        supports.clear();
                    }
                    for(int x = left.x; x <= right.x; x++) {
                        field[left.z][left.y][x] = 0;
                        field[left.z-1][left.y][x] = id;
                    }
                    left.z--;
                    right.z--;
                    return true;
                case Y:
                    if(left.z == 1) {
                        restsOn.clear();
                        return false;
                    }
                    for(int y = left.y; y <= right.y; y++) {
                        if(field[left.z-1][y][left.x] > 0) {
                            short bid = field[left.z-1][y][left.x];
                            restsOn.add(bid);
                            bricks.get(bid - 1).supports.add(id);
                        }
                    }
                    if(!restsOn.isEmpty())
                        return false;
                    if(!supports.isEmpty()) {
                        for(short s : supports) {
                            bricks.get(s - 1).restsOn.remove(id);
                        }
                        supports.clear();
                    }
                    for(int y = left.y; y <= right.y; y++) {
                        field[left.z][y][left.x] = 0;
                        field[left.z-1][y][left.x] = id;
                    }
                    left.z--;
                    right.z--;
                    return true;
                case Z:
                    if(left.z == 1) {
                        restsOn.clear();
                        return false;
                    }
                    if(field[left.z-1][left.y][left.x] > 0) {
                        short bid = field[left.z-1][left.y][left.x];
                        restsOn.add(bid);
                        bricks.get(bid - 1).supports.add(id);
                    }
                    if(!restsOn.isEmpty())
                        return false;
                    if(!supports.isEmpty()) {
                        for(short s : supports) {
                            bricks.get(s - 1).restsOn.remove(id);
                        }
                        supports.clear();
                    }
                    field[right.z][left.y][left.x] = 0;
                    field[left.z-1][left.y][left.x] = id;
                    left.z--;
                    right.z--;
                    return true;
            }
            if(left.z == 1) {
                restsOn.clear();
                return false;
            }
            if(field[left.z-1][left.y][left.x] > 0) {
                short bid = field[left.z-1][left.y][left.x];
                restsOn.add(bid);
                bricks.get(bid - 1).supports.add(id);
                return false;
            }
            if(!supports.isEmpty()) {
                for(short s : supports) {
                    bricks.get(s - 1).restsOn.remove(id);
                }
                supports.clear();
            }
            field[left.z][left.y][left.x] = 0;
            field[left.z-1][left.y][left.x] = id;
            left.z--;
            return true;
        }
        public void place() {
            //System.out.println(this);
            switch(o) {
                case X:
                    for(int x = left.x; x <= right.x; x++) {
                        field[left.z][left.y][x] = id;
                    }
                    break;
                case Y:
                    for(int y = left.y; y <= right.y; y++) {
                        field[left.z][y][left.x] = id;
                    }
                    break;
                case Z:
                    for(int z = left.z; z <= right.z; z++) {
                        field[z][left.y][left.x] = id;
                    }
                    break;
            }
            field[left.z][left.y][left.x] = id;
        }
        public int getHeight() {
            return Math.min(left.z, right.z);
        }
        public int getLength() {
            switch(o) {
                case X:
                    return Math.abs(left.x - right.x + 1);
                case Y:
                    return Math.abs(left.y - right.y + 1);
                case Z:
                    return Math.abs(left.z - right.z + 1);
                case CUBE:
                    return 1;
            }
            return 1;
        }
        public boolean isLine() {
            return left.x == right.x || left.y == right.y;
        }
        public boolean canRemove() {
            if(supports.isEmpty())
                return true;
            for(short bid : supports) {
                if(bricks.get(bid - 1).restsOn.size() < 2)
                    return false;
            }
            return true;
        }
        public int chains() {
            Set<Short> list = new HashSet<>(supports), removed = new HashSet<>();
            int sum = 0;
            if(canRemove())
                return 0;
            removed.add(id);
            System.out.println("Chains " + id);
            while(!list.isEmpty()) {
                Set<Brick> check = new HashSet<>();
                for(short s : list) {
                    System.out.println(" Check " + s);
                    Brick b = bricks.get(s-1);
                    Set<Short> test = new HashSet<>(b.restsOn);
                    test.removeAll(removed);
                    if(test.isEmpty() && !removed.contains(b.id)) {
                        System.out.println("  FALLS!");
                        sum++;
                        check.add(b);
                        removed.add(b.id);
                    }
                }
                list.clear();
                check.stream().forEach(b -> list.addAll(b.supports));
            }
            System.out.println("Sum=" + sum);
            return sum;
        }
        @Override
        public int compareTo(Brick b) {
            if(left.z < b.left.z)
                return -1;
            if(left.z > b.left.z)
                return 1;
            return 0;
        }
        @Override
        public String toString() {
            StringBuilder b = new StringBuilder("ID ");
            b.append(id);
            b.append(": ");
            b.append(left);
            b.append(" ~ ");
            b.append(right);
            b.append("canRemove: ");
            b.append(canRemove());
            if(!restsOn.isEmpty()) {
                b.append(" RestOn:");
                for(short s : restsOn) {
                    b.append(" ");
                    b.append(s);
                }
            }
            if(!supports.isEmpty()) {
                b.append(" Support:");
                for(short s : supports) {
                    b.append(" ");
                    b.append(s);
                    b.append(" [");
                    for(short t : bricks.get(s - 1).restsOn) {
                        b.append(" ");
                        b.append(t);
                    }
                    b.append(" ]");
                }
            }
            return b.toString();
        }
    }
    
    private final File input;
    List<Brick> bricks = new ArrayList<>();
    short[][][] field;
    
    public Puzzle22(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<Brick> falling = new ArrayList<>();
        List<Brick> resting = new ArrayList<>();
        String line;
        int sum = 0;
        short count = 1;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            Brick brick = new Brick(line, count++);
            /*if(!brick.isLine()) {
                System.out.println("Big brick: " + brick);
            }*/
            bricks.add(brick);
        }
        int maxX = bricks.stream().mapToInt(b -> Math.max(b.left.x, b.right.x)).max().orElse(0) + 1;
        int maxY = bricks.stream().mapToInt(b -> Math.max(b.left.y, b.right.y)).max().orElse(0) + 1;
        int maxZ = bricks.stream().mapToInt(b -> Math.max(b.left.z, b.right.z)).max().orElse(0) + 1;
        System.out.println("# bricks: " + bricks.size() + " Dim: " + maxX + "x" + maxY + "x" + maxZ);
        field = new short[maxZ][maxY][maxX];
        //initField(); // set all points to false
        bricks.stream().forEach(b -> b.place());
        //bricks.sort(null);
        List<Brick> list = new ArrayList<>(bricks);
        while(!list.isEmpty()) {
            for(Brick b : list) {
                //printXZ();
                if(b.fall())
                    falling.add(b);
                else
                    resting.add(b);
            }
            list = falling;
            falling = new ArrayList<>();
        }
        boolean falls = true;
        while(falls) {
            falls = false;
            for(Brick b : resting)
                if(b.fall())
                    falls = true;
        }
        System.out.println("Total: " + bricks.size() + " Resting: " + resting.size());
        //printXZ();
        bricks.stream().forEach(b -> System.out.println(b));
        for(Brick b : bricks) {
            if(b.canRemove())
                sum++;
        }
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<Brick> falling = new ArrayList<>();
        List<Brick> resting = new ArrayList<>();
        String line;
        int sum = 0;
        short count = 1;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            Brick brick = new Brick(line, count++);
            /*if(!brick.isLine()) {
                System.out.println("Big brick: " + brick);
            }*/
            bricks.add(brick);
        }
        int maxX = bricks.stream().mapToInt(b -> Math.max(b.left.x, b.right.x)).max().orElse(0) + 1;
        int maxY = bricks.stream().mapToInt(b -> Math.max(b.left.y, b.right.y)).max().orElse(0) + 1;
        int maxZ = bricks.stream().mapToInt(b -> Math.max(b.left.z, b.right.z)).max().orElse(0) + 1;
        System.out.println("# bricks: " + bricks.size() + " Dim: " + maxX + "x" + maxY + "x" + maxZ);
        field = new short[maxZ][maxY][maxX];
        //initField(); // set all points to false
        bricks.stream().forEach(b -> b.place());
        //bricks.sort(null);
        List<Brick> list = new ArrayList<>(bricks);
        while(!list.isEmpty()) {
            for(Brick b : list) {
                //printXZ();
                if(b.fall())
                    falling.add(b);
                else
                    resting.add(b);
            }
            list = falling;
            falling = new ArrayList<>();
        }
        boolean falls = true;
        while(falls) {
            falls = false;
            for(Brick b : resting)
                if(b.fall())
                    falls = true;
        }
        System.out.println("Total: " + bricks.size() + " Resting: " + resting.size());
        //printXZ();
        //bricks.stream().forEach(b -> System.out.println(b));
        for(Brick b : bricks) {
            sum += b.chains();
        }
        return "" + sum;
    }
    
    private void printXZ() {
        System.out.println(" XZ |  YZ");
        for(int z = field.length; z > 1; z--) {
            StringBuilder b = new StringBuilder();
            for(int x = 0; x < field[z-1][0].length; x++) {
                int count = 0;
                for(int y = 0; y < field[z-1].length; y++)
                    if(field[z-1][y][x] > 0)
                        count++;
                if(count > 1)
                    b.append('#');
                else if(count == 1)
                    b.append('*');
                else
                    b.append(' ');
            }
            b.append(" | ");
            for(int y = 0; y < field[z-1].length; y++) {
                int count = 0;
                for(int x = 0; x < field[z-1][y].length; x++)
                    if(field[z-1][y][x] > 0)
                        count++;
                if(count > 1)
                    b.append('#');
                else if(count == 1)
                    b.append('*');
                else
                    b.append(' ');
            }
            System.out.println(b.toString());
        }
        StringBuilder b = new StringBuilder();
        for(int x = 0; x < field[0][0].length; x++) {
            b.append('-');
        }
        System.out.println(b.toString());
    }
}

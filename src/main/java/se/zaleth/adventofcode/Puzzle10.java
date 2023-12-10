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
public class Puzzle10 {
    
    private final File input;
    
    public Puzzle10(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<String> rows = new ArrayList<>();
        String line;
        int row = -1, col = -1;
        int sum = 1;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            if(line.contains("S")) {
                row = rows.size();
                col = line.indexOf("S");
            }
            rows.add(line);
        }
        System.out.println("Start: (" + row + "," + col + ")");
        int fx = -1, fy = -1, sx = -1, sy = -1;
        switch(rows.get(row - 1).charAt(col)) {
            case '|':
            case '7':
            case 'F':
                fx = col;
                fy = row - 1;
        }
        switch(rows.get(row + 1).charAt(col)) {
            case '|':
            case 'L':
            case 'J':
                if(fx < 0) {
                    fx = col;
                    fy = row + 1;
                } else {
                    sx = col;
                    sy = row + 1;
                }
        }
        switch(rows.get(row).charAt(col - 1)) {
            case '-':
            case 'L':
            case 'F':
                if(fx < 0) {
                    fx = col - 1;
                    fy = row;
                } else {
                    sx = col + 1;
                    sy = row;
                }
        }
        switch(rows.get(row).charAt(col + 1)) {
            case '-':
            case '7':
            case 'J':
                if(fx < 0) {
                    fx = col + 1;
                    fy = row;
                } else {
                    sx = col + 1;
                    sy = row;
                }
        }
        
        sx = col;
        sy = row;
        while(rows.get(fy).charAt(fx) != 'S') {
            switch(rows.get(fy).charAt(fx)) {
                case '|':
                    sx = fx;
                    if(sy < fy) {
                        sy = fy;
                        fy++;
                    } else {
                        sy = fy;
                        fy--;
                    }
                    break;
                    
                case '-':
                    sy = fy;
                    if(sx < fx) {
                        sx = fx;
                        fx++;
                    } else {
                        sx = fx;
                        fx--;
                    }
                    break;
                    
                case 'L':
                    if(sy < fy) {
                        sx = fx;
                        sy = fy;
                        fx++;
                    } else {
                        sx = fx;
                        sy = fy;
                        fy--;
                    }
                    break;
                    
                case 'J':
                    if(sy < fy) {
                        sx = fx;
                        sy = fy;
                        fx--;
                    } else {
                        sx = fx;
                        sy = fy;
                        fy--;
                    }
                    break;
                    
                case '7':
                    if(sy > fy) {
                        sx = fx;
                        sy = fy;
                        fx--;
                    } else {
                        sx = fx;
                        sy = fy;
                        fy++;
                    }
                    break;
                    
                case 'F':
                    if(sy > fy) {
                        sx = fx;
                        sy = fy;
                        fx++;
                    } else {
                        sx = fx;
                        sy = fy;
                        fy++;
                    }
                    break;
                    
            }
            sum++;
        }
        System.out.println("Loop len: " + sum);
        sum /= 2;
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<String> rows = new ArrayList<>();
        List<String> coords = new ArrayList<>();
        String line = '.' + in.readLine() + '.';
        int row = -1, col = -1;
        int sum = line.length();
        rows.add(padLine(sum));
        if(line.contains("S")) {
            row = rows.size();
            col = line.indexOf("S");
        }
        rows.add(line);
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            line = '.' + line + '.';
            //System.out.println("Line: " + line);
            if(line.contains("S")) {
                row = rows.size();
                col = line.indexOf("S");
            }
            rows.add(line);
        }
        rows.add(padLine(sum));
        System.out.println("Start: (" + row + "," + col + ")");
        int fx = -1, fy = -1, sx = -1, sy = -1;
        int top = row, left = col, bottom = 0, right = 0;
        switch(rows.get(row - 1).charAt(col)) {
            case '|':
            case '7':
            case 'F':
                fx = col;
                fy = row - 1;
        }
        switch(rows.get(row + 1).charAt(col)) {
            case '|':
            case 'L':
            case 'J':
                if(fx < 0) {
                    fx = col;
                    fy = row + 1;
                } else {
                    sx = col;
                    sy = row + 1;
                }
        }
        switch(rows.get(row).charAt(col - 1)) {
            case '-':
            case 'L':
            case 'F':
                if(fx < 0) {
                    fx = col - 1;
                    fy = row;
                } else {
                    sx = col + 1;
                    sy = row;
                }
        }
        switch(rows.get(row).charAt(col + 1)) {
            case '-':
            case '7':
            case 'J':
                if(fx < 0) {
                    fx = col + 1;
                    fy = row;
                } else {
                    sx = col + 1;
                    sy = row;
                }
        }
        
        sx = col;
        sy = row;
        while(rows.get(fy).charAt(fx) != 'S') {
            coords.add("" + fy + "," + fx);
            switch(rows.get(fy).charAt(fx)) {
                case '|':
                    sx = fx;
                    if(sy < fy) {
                        sy = fy;
                        fy++;
                    } else {
                        sy = fy;
                        fy--;
                    }
                    break;
                    
                case '-':
                    sy = fy;
                    if(sx < fx) {
                        sx = fx;
                        fx++;
                    } else {
                        sx = fx;
                        fx--;
                    }
                    break;
                    
                case 'L':
                    if(sy < fy) {
                        sx = fx;
                        sy = fy;
                        fx++;
                    } else {
                        sx = fx;
                        sy = fy;
                        fy--;
                    }
                    break;
                    
                case 'J':
                    if(sy < fy) {
                        sx = fx;
                        sy = fy;
                        fx--;
                    } else {
                        sx = fx;
                        sy = fy;
                        fy--;
                    }
                    break;
                    
                case '7':
                    if(sy > fy) {
                        sx = fx;
                        sy = fy;
                        fx--;
                    } else {
                        sx = fx;
                        sy = fy;
                        fy++;
                    }
                    break;
                    
                case 'F':
                    if(sy > fy) {
                        sx = fx;
                        sy = fy;
                        fx++;
                    } else {
                        sx = fx;
                        sy = fy;
                        fy++;
                    }
                    break;
                    
            }
            if(fx < left)
                left = fx;
            if(fx > right)
                right = fx;
            if(fy < top)
                top = fy;
            if(fy > bottom)
                bottom = fy;
        }
        System.out.println("Enclosed: (" + top + "," + left + ") to (" + bottom + "," + right + ")");
        char[][] data = new char[bottom - top + 1][];
        for(int y = top; y <= bottom; y++) {
            data[y - top] = rows.get(y).substring(left, right + 1).toCharArray();
            //data.add(rows.get(y).substring(left, right));
            //System.out.println(rows.get(y).substring(left, right + 1));
            //for(int x = left; x <= right; x++) 
        }
        for(String str : coords) {
            int x, y;
            x = Integer.parseInt(str.split(",")[1]);
            y = Integer.parseInt(str.split(",")[0]);
            //System.out.println("(" + y + "," + x + ")");
            char chr = ' ';
            switch(data[y - top][x - left]) {
                case '-':
                    chr = '=';
                    break;
                case '|':
                    chr = 'H';
                    break;
                case 'L':
                case '7':
                    chr = '\\';
                    break;
                case 'J':
                case 'F':
                    chr = '/';
                    break;
            }
            data[y - top][x - left] = chr;
        }
        sum = 0;
        boolean isInside;
        for(int y = 0; y <= (bottom - top); y++) {
            for(int x = 0; x <= (right - left); x++) {
                if("|-FJL7.".contains("" + data[y][x])) {
                    isInside = false;
                    fx = x;
                    while(fx > -1)
                        if("=H/\\".contains(new String("" + data[y][fx--])))
                            isInside = !isInside;
                    if(isInside) {
                        isInside = false;
                        fx = x;
                        while(fx < data[y].length)
                            if("=H/\\".contains(new String("" + data[y][fx++])))
                                isInside = !isInside;
                        if(isInside) {
                            isInside = false;
                            fy = y;
                            while(fy > -1)
                                if("=H/\\".contains(new String("" + data[fy--][x])))
                                    isInside = !isInside;
                            if(isInside) {
                                isInside = false;
                                fy = y;
                                while(fy < data.length)
                                    if("=H/\\".contains(new String("" + data[fy++][x])))
                                        isInside = !isInside;
                                if(isInside) {
                                    data[y][x] = 'O';
                                    sum++;
                                }
                            }
                        }
                    }
                }
            }
            System.out.println(new String(data[y]));
        }
        //sum /= 2;
        return "" + sum;
    }
    
    private String padLine(int len) {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < len; i++)
            str.append('.');
        return str.toString();
    }
    
}

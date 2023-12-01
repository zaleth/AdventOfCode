/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package se.zaleth.adventofcode;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author criz_
 */
public class AdventOfCode {

    private static final File INPUT_DIR = new File(String.join(File.separator, "src", "main", "inputs"));
    
    public static void main(String[] args) {
        System.out.println("Current dir: " + INPUT_DIR.getAbsolutePath());
        
        try {
            Puzzle1 puzzle = new Puzzle1(new File(INPUT_DIR, "day1-input.txt"));
            //Puzzle1 puzzle = new Puzzle1(new File(INPUT_DIR, "test.txt"));
            System.out.println("Result: " + puzzle.solve2());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
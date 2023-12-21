/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package se.zaleth.adventofcode;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author criz_
 */
public class AdventOfCode extends JFrame {

    private static final File INPUT_DIR = new File(String.join(File.separator, "src", "main", "inputs"));
    private static AdventOfCode debug;
    
    public static AdventOfCode getFrame() {
        if(debug == null)
            debug = new AdventOfCode();
        return debug;
    }
    
    public static void main(String[] args) {
        System.out.println("Current dir: " + INPUT_DIR.getAbsolutePath());
        try {
            Puzzle21 puzzle = new Puzzle21(new File(INPUT_DIR, "day21-input.txt"), 64);
            //Puzzle21 puzzle = new Puzzle21(new File(INPUT_DIR, "test.txt"), 6);
            System.out.println("Result: " + puzzle.solve());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public AdventOfCode() {
        super("AdventOfCode debug");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void refresh() {
        this.setVisible(false);
        this.pack();
        this.setVisible(true);
    }
    
}

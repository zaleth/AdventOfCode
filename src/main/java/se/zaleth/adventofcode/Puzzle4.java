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

/**
 *
 * @author criz_
 */
public class Puzzle4 {
    
    private class Card implements Comparable<Card> {
        
        private final Set<Integer> winners;
        private final Set<Integer> numbers;
        private final int index;
        
        public Card(int i, String str) {
            winners = getNumArray(str.split("\\|")[0]);
            numbers = getNumArray(str.split("\\|")[1]);
            index = i;
        }
        
        public int getIndex() {
            return index;
        }
        
        public int getWins() {
            Set<Integer> count = new HashSet<>(numbers);
            count.retainAll(winners);
            return count.size();
        }
        
        @Override
        public int compareTo(Card card) {
            return Integer.compare(index, card.index);
        }
        
    }
    
    private final File input;
    
    public Puzzle4(File in) {
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
            line = line.split(":")[1];
            System.out.println("Line: " + line);
            Set<Integer> winners = getNumArray(line.split("\\|")[0]);
            Set<Integer> numbers = getNumArray(line.split("\\|")[1]);
            numbers.retainAll(winners);
            int count = numbers.size();
            if(count > 0) {
                sum += 1 << (count - 1);
            }
        }
        return "" + sum;
    }
    
    /**
     * Original, slow solution. Takes over 12 sec 
     * to get correct result.
     * @return Total number of cards (14814534)
     * @throws IOException 
     */
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<Card> cards = new ArrayList<>();
        String line;
        int sum = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            line = line.split(":")[1];
            System.out.println("Line: " + line);
            cards.add(new Card(sum++, line));
        }
        sum = 0;
        List<Card> deck = new ArrayList<>(cards);
        while(deck.size() > 0) {
            List<Card> wins = new ArrayList<>();
            for(Card card : deck) {
                sum++;
                for(int i = card.getWins(); i > 0; i--) {
                    wins.add(cards.get(card.getIndex() + i));
                }
            }
            wins.sort((c1, c2) -> Integer.compare(c1.index, c2.index));
            deck = wins;
        }
        return "" + sum;
    }
    
    /**
     * Honor restored. Solves part2 in half a second
     * @return Total number of cards (14814534)
     * @throws IOException 
     */
    public String solve3() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        int[] cards = getArray(250, 1);
        String line;
        int sum = 0;
        int lineCount = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            line = line.split(":")[1];
            Set<Integer> winners = getNumArray(line.split("\\|")[0]);
            Set<Integer> numbers = getNumArray(line.split("\\|")[1]);
            numbers.retainAll(winners);
            int count = numbers.size();
            System.out.println("Line: " + lineCount + " Wins: " + count + " Count: " + cards[lineCount]);
            sum += cards[lineCount];
            lineCount++;
            for(int i = 0; i < count; i++) {
                cards[lineCount + i] += cards[lineCount - 1];
            }
        }
        return "" + sum;
    }
    
    private Set<Integer> getNumArray(String str) {
        String[] num = str.split(" ");
        Set<Integer> ret = new HashSet<>();
        for(int i = 0; i < num.length; i++) {
            try {
                ret.add(Integer.parseInt(num[i]));
            } catch(NumberFormatException e) {
            }
        }
        return ret;
    }
    
    private int[] getArray(int length, int init) {
        int[] ret = new int[length];
        for(int i = 0; i < length; i++)
            ret[i] = init;
        return ret;
    }
    
}

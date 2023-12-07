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
import java.util.Collections;
import java.util.List;

/**
 *
 * @author criz_
 */
public class Puzzle7 {
    
    private class Hand implements Comparable<Hand> {
        
        private static final String ORDER = "AKQJT98765432";
        private static final String[] TYPES = { "Nothing", "One pair", "Two pairs", "Three of a kind", "Full House", "Four of a kind", "Five of a kind" };
        
        String cards;
        long bet;
        int type;
        
        public Hand(String str) {
            cards = str.split(" ")[0];
            bet = Long.parseLong(str.split(" ")[1]);
            type = calcType();
        }
        
        public long getBet() {
            return bet;
        }
        
        private int calcType() {
            char[] chars = cards.toCharArray();
            int len = chars.length;
            int same = 0;
            int twos = -1, threes = -1;
            for(int i = 1; i < len; i++) {
                if(chars[i] == chars[0])
                    same++;
            }
            if(same > 2)
                return same + 2;
            if(same == 2)
                threes = 0;
            if(same == 1)
                twos = 0;
            // done with first card
            for(int i = 1; i < len; i++) {
                if(chars[i] == chars[0])
                    continue;
                if(i > 1 && chars[i] == chars[1])
                    continue;
                if(i > 2 && chars[i] == chars[2])
                    continue;
                if(i > 3 && chars[i] == chars[3])
                    continue;
                same = 0;
                for(int j = i+1; j < len; j++)
                    if(chars[i] == chars[j])
                        same++;
                //System.out.println("card " + i + ": " + cards[i] + " same: " + same);
                if(same == 3) {
                    return 5;
                }
                if(same == 2) {
                    if(twos > -1) {
                        //System.out.println("2+3 = full house");
                        return 4;
                    }
                    threes = i;
                }
                if(same == 1) {
                    if(threes > -1) {
                        //System.out.println("3+2 = full house");
                        return 4;
                    }
                    if(twos > -1) {
                        //System.out.println("two pair");
                        return 2;
                    }
                    twos = i;
                }
            }
            if(threes > -1)
                return 3;
            if(twos > -1)
                return 1;
            return 0;
        }
        
        @Override
        public int compareTo(Hand c) {
            if(type > c.type)
                return -1;
            if(c.type > type)
                return 1;
            for(int i = 0; i < cards.length(); i++) {
                if(ORDER.indexOf(cards.charAt(i)) < ORDER.indexOf(c.cards.charAt(i)))
                    return -1;
                if(ORDER.indexOf(cards.charAt(i)) > ORDER.indexOf(c.cards.charAt(i)))
                    return 1;
            }
            return 0;
        }
        
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder(cards);
            str.append(": ");
            str.append(TYPES[type]);
            return str.toString();
        }
    }
    
    private class Hand2 implements Comparable<Hand2> {
        
        private static final String ORDER = "AKQT98765432J";
        private static final String[] TYPES = { "Nothing", "One pair", "Two pairs", "Three of a kind", "Full House", "Four of a kind", "Five of a kind" };
        
        String cards;
        long bet;
        int type;
        
        public Hand2(String str) {
            cards = str.split(" ")[0];
            bet = Long.parseLong(str.split(" ")[1]);
            type = calcType();
        }
        
        public long getBet() {
            return bet;
        }
        
        private int calcType() {
            char[] chars = cards.toCharArray();
            int len = chars.length;
            int same = 0;
            int jokers = 0;
            int twos = -1, threes = -1;
            if(chars[0] == 'J') {
                jokers++;
            } else {
                for(int i = 1; i < len; i++) {
                    if(chars[i] == chars[0] || chars[i] == 'J')
                        same++;
                }
                if(same > 2)
                    return same + 2;
                if(same == 2)
                    threes = 0;
                if(same == 1)
                    twos = 0;
            }
            // done with first card
            for(int i = 1; i < len; i++) {
                if(chars[i] == 'J') {
                    jokers++;
                } else {
                    if(chars[i] == chars[0])
                        continue;
                    if(i > 1 && chars[i] == chars[1])
                        continue;
                    if(i > 2 && chars[i] == chars[2])
                        continue;
                    if(i > 3 && chars[i] == chars[3])
                        continue;
                    same = 0;
                    for(int j = i+1; j < len; j++)
                        if(chars[i] == chars[j] || chars[i] == 'J')
                            same++;
                    //System.out.println("card " + i + ": " + cards[i] + " same: " + same);
                    if(same == 3 && jokers > 0) {
                        return 6;
                    }
                    if(same == 3 || (same == 2 && jokers > 0) || (same == 1 && jokers > 1) || jokers == 3) {
                        return 5;
                    }
                    if(same == 2) {
                        if(twos > -1) {
                            //System.out.println("2+3 = full house");
                            return 4;
                        }
                        threes = i;
                    }
                    if(same == 1) {
                        if(threes > -1) {
                            //System.out.println("3+2 = full house");
                            return 4;
                        }
                        if(twos > -1 && jokers > 0) {
                            //System.out.println("two pair");
                            return 4;
                        }
                        if(twos > -1) {
                            //System.out.println("two pair");
                            return 2;
                        }
                        twos = i;
                    }
                }
            }
            System.out.println("Hand: " + cards + " Jokers: " + jokers);
            if(threes > -1) {
                if(jokers > 0)
                    return 4;
                return 3;
            }
            if(twos > -1) {
                if(jokers > 0)
                    return 3;
                return 1;
            }
            switch(jokers) {
                case 5:
                case 4:
                    return 6;
                case 3:
                    return 5;
                case 2:
                    return 3;
                case 1:
                    return 1;
            }
            return 0;
        }

        @Override
        public int compareTo(Hand2 c) {
            if(type > c.type)
                return -1;
            if(c.type > type)
                return 1;
            for(int i = 0; i < cards.length(); i++) {
                if(ORDER.indexOf(cards.charAt(i)) < ORDER.indexOf(c.cards.charAt(i)))
                    return -1;
                if(ORDER.indexOf(cards.charAt(i)) > ORDER.indexOf(c.cards.charAt(i)))
                    return 1;
            }
            return 0;
        }
        
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder(cards);
            str.append(": ");
            str.append(TYPES[type]);
            return str.toString();
        }
    }
    
    private final File input;
    
    public Puzzle7(File in) {
        input = in;
        System.out.println(new Hand("Q8888 1"));
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line, from = "seed", to;
        List<Hand> hands = new ArrayList<>();
        long sum = 0;
        
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //line = line.split(" ")[0];
            
            //System.out.println("Cards: " + line + " Type: " + getType(line));
            hands.add(new Hand(line));
        }
        Collections.sort(hands);
        for(int i = 0; i < hands.size(); i++) {
            System.out.println("" + i + " " + hands.get(i));
            sum += hands.get(i).getBet() * (hands.size() - i);
        }
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line, from = "seed", to;
        List<Hand2> hands = new ArrayList<>();
        long sum = 0;
        
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //line = line.split(" ")[0];
            
            //System.out.println("Cards: " + line + " Type: " + getType(line));
            hands.add(new Hand2(line));
        }
        Collections.sort(hands);
        for(int i = 0; i < hands.size(); i++) {
            System.out.println("" + i + " " + hands.get(i));
            sum += hands.get(i).getBet() * (hands.size() - i);
        }
        return "" + sum;
    }

}

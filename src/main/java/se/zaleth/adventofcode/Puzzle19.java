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
public class Puzzle19 {
    
    private class Bound {
        int minX, maxX;
        int minM, maxM;
        int minA, maxA;
        int minS, maxS;
        String rule;
        
        public Bound(String str) {
            minX = minM = minA = minS = 1;
            maxX = maxM = maxA = maxS = 4000;
            rule = str;
        }
        
        public Bound lessThan(char var, int val, String str) {
            Bound b = new Bound(str);
            b.minX = minX;
            b.minM = minM;
            b.minA = minA;
            b.minS = minS;
            b.maxX = maxX;
            b.maxM = maxM;
            b.maxA = maxA;
            b.maxS = maxS;
            switch(var) {
                case 'x':
                    b.maxX = val - 1;
                    break;
                case 'm':
                    b.maxM = val - 1;
                    break;
                case 'a':
                    b.maxA = val - 1;
                    break;
                case 's':
                    b.maxS = val - 1;
                    break;
            }
            return b;
        }
        
        public Bound moreThan(char var, int val, String str) {
            Bound b = new Bound(str);
            b.minX = minX;
            b.minM = minM;
            b.minA = minA;
            b.minS = minS;
            b.maxX = maxX;
            b.maxM = maxM;
            b.maxA = maxA;
            b.maxS = maxS;
            switch(var) {
                case 'x':
                    b.minX = val + 1;
                    break;
                case 'm':
                    b.minM = val + 1;
                    break;
                case 'a':
                    b.minA = val + 1;
                    break;
                case 's':
                    b.minS = val + 1;
                    break;
            }
            return b;
        }
        
        public int min(char var) {
            switch(var) {
                case 'x':
                    return minX;
                case 'm':
                    return minM;
                case 'a':
                    return minA;
                case 's':
                    return minS;
            }
            throw new IllegalArgumentException("Unknown var '" + var + "'");
        }
        
        public int max(char var) {
            switch(var) {
                case 'x':
                    return maxX;
                case 'm':
                    return maxM;
                case 'a':
                    return maxA;
                case 's':
                    return maxS;
            }
            throw new IllegalArgumentException("Unknown var '" + var + "'");
        }
                
        public long score() {
            return ((long)(maxX - minX + 1)) * ((long)(maxM - minM + 1)) * ((long)(maxA - minA + 1)) * ((long)(maxS - minS + 1));
        }
        
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder("Score: ");
            str.append(score());
            str.append("\n  ");
            str.append(minX);
            str.append(" <= X <= ");
            str.append(maxX);
            str.append("\n  ");
            str.append(minM);
            str.append(" <= M <= ");
            str.append(maxM);
            str.append("\n  ");
            str.append(minA);
            str.append(" <= A <= ");
            str.append(maxA);
            str.append("\n  ");
            str.append(minS);
            str.append(" <= S <= ");
            str.append(maxS);
            return str.toString();
        }
    }
    
    private class Part {
        int x, m, a, s;

        public Part(String part) {
            for(String str : part.split(",")) {
                char var = str.charAt(0);
                int val = Integer.parseInt(str.substring(2));
                switch(var) {
                    case 'x':
                        x = val;
                        break;
                    case 'm':
                        m = val;
                        break;
                    case 'a':
                        a = val;
                        break;
                    case 's':
                        s = val;
                        break;
                }
            }
        }
        
        public Part(int x, int m, int a, int s) {
            this.x = x;
            this.m = m;
            this.a = a;
            this.s = s;
        }
        
        public int get(char var) {
            switch(var) {
                case 'x':
                    return x;
                case 'm':
                    return m;
                case 'a':
                    return a;
                case 's':
                    return s;
            }
            throw new IllegalArgumentException("Unknown var '" + var + "'");
        }
        
        public int score() {
            return x + m + a + s;
        }
    }
    
    private class Rule {
        static final int LESS_THAN = 1;
        static final int GREATER_THAN = 2;
        static final int ACCEPT = 3;
        static final int REJECT = 4;
        static final int JUMP = 5;
        
        final String rule;
        final int op, val;
        final char var;

        public Rule(String str) {
            if(str.equals("A")) {
                var = ' ';
                op = ACCEPT;
                val = 0;
                rule = "";
            } else if(str.equals("R")) {
                var = ' ';
                op = REJECT;
                val = 0;
                rule = "";
            } else if(str.indexOf(':') > -1) {
                var = str.charAt(0);
                op = str.charAt(1) == '<' ? LESS_THAN : GREATER_THAN;
                val = Integer.parseInt(str.substring(2, str.indexOf(':')));
                rule = str.substring(str.indexOf(':') + 1);
            } else {
                var = ' ';
                op = JUMP;
                val = 0;
                rule = str;
            }
        }
        
        public Rule(char var, int op, int val, String rule) {
            this.var = var;
            this.op = op;
            this.val = val;
            this.rule = rule;
        }
        
        public String apply(Part part) {
            switch(op) {
                case LESS_THAN:
                    if(part.get(var) < val)
                        return rule;
                    break;
                case GREATER_THAN:
                    if(part.get(var) > val)
                        return rule;
                    break;
                case ACCEPT:
                    return "A";
                case REJECT:
                    return "R";
                case JUMP:
                    return rule;
            }
            return "";
        }
        
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            switch(op) {
                case LESS_THAN:
                    str.append(var);
                    str.append("<");
                    str.append(val);
                    str.append(":");
                    str.append(rule);
                    break;
                case GREATER_THAN:
                    str.append(var);
                    str.append(">");
                    str.append(val);
                    str.append(":");
                    str.append(rule);
                    break;
                case ACCEPT:
                    return "ACCEPT";
                case REJECT:
                    return "REJECT";
                case JUMP:
                    return rule;
            }
            return str.toString();
        }
    }
    
    private final Map<String,List<Rule>> chain;
    private final File input;
    
    public Puzzle19(File in) {
        input = in;
        chain = new HashMap<>();
    }
    
    public String solve() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<Part> parts = new ArrayList<>();
        String line;
        int sum = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                break;
            String name = line.substring(0, line.indexOf("{"));
            String rules = line.substring(line.indexOf("{") + 1, line.length() - 1);
            System.out.println(name + ": " + rules);
            List<Rule> list = chain.get(name);
            if(list == null)
                list = new ArrayList<>();
            for(String rule : rules.split(",")) {
                list.add(new Rule(rule));
            }
            chain.put(name, list);
        }
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                break;
            line = line.substring(1, line.length() - 1);
            System.out.println("Part: " + line);
            parts.add(new Part(line));
        }
        while(!parts.isEmpty()) {
            Part part = parts.remove(0);
            String rail = "in";
            while(!"R".equals(rail) && !"A".equals(rail)) {
                List<Rule> list = chain.get(rail);
                Iterator<Rule> it = list.iterator();
                while(it.hasNext()) {
                    Rule rule = it.next();
                    rail = rule.apply(part);
                    if(rail.isBlank()) {
                        // do nothing, try to match next rule
                    } else break;
                }
            }
            if("A".equals(rail)) {
                sum += part.score();
            }
        }
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(input));
        List<Bound> parts = new ArrayList<>();
        String line;
        long sum = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                break;
            String name = line.substring(0, line.indexOf("{"));
            String rules = line.substring(line.indexOf("{") + 1, line.length() - 1);
            //System.out.println(name + ": " + rules);
            List<Rule> list = chain.get(name);
            if(list == null)
                list = new ArrayList<>();
            for(String rule : rules.split(",")) {
                list.add(new Rule(rule));
            }
            chain.put(name, list);
        }
        /* We can skip the parts
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                break;
            line = line.substring(1, line.length() - 1);
            System.out.println("Part: " + line);
            parts.add(new Part(line));
        }*/
        parts.add(new Bound("in"));
        while(!parts.isEmpty()) {
            Bound part = parts.remove(0);
            String rail = part.rule;
            while(!"R".equals(rail) && !"A".equals(rail)) {
                List<Rule> list = chain.get(rail);
                Iterator<Rule> it = list.iterator();
                while(!"R".equals(rail) && !"A".equals(rail) && it.hasNext()) {
                    Rule rule = it.next();
                    //System.out.println("Rule: " + rule);
                    switch(rule.op) {
                        case Rule.LESS_THAN:
                            if(part.min(rule.var) < rule.val)
                                parts.add(part.lessThan(rule.var, rule.val, rule.rule));
                            part = part.moreThan(rule.var, rule.val - 1, rail);
                            break;
                        case Rule.GREATER_THAN:
                            if(part.max(rule.var) > rule.val)
                                parts.add(part.moreThan(rule.var, rule.val, rule.rule));
                            part = part.lessThan(rule.var, rule.val + 1, rail);
                            break;
                        case Rule.ACCEPT:
                            rail = "A";
                            break;
                        case Rule.REJECT:
                            rail = "R";
                            break;
                        case Rule.JUMP:
                            rail = part.rule = rule.rule;
                            list = chain.get(rail);
                            it = list.iterator();
                            break;
                    }
                }
            }
            if("A".equals(rail)) {
                //System.out.println(part);
                sum += part.score();
            }
        }
        return "" + sum;
    }
    
}

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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author criz_
 */
public class Puzzle20 {
    
    private abstract class Node {
        List<Node> outputs = new ArrayList<>();
        String name;
        public Node(String name) {
            this.name = name;
        }
        public void addNode(Node node) {
            outputs.add(node);
        }
        public boolean hasOutput(String name) {
            return outputs.stream().anyMatch(n -> n.name.equals(name));
        }
        public abstract List<Signal> receive(Signal input);
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder("  ");
            if(this instanceof FlipFlop)
                str.append("%");
            if(this instanceof Conjunction)
                str.append("&");
            str.append(name);
            str.append(" -> ");
            for(Node dest : outputs) {
                str.append(dest.name);
                str.append(", ");
            }
            return str.toString();
        }
    }
    
    private class Signal {
        Node source, target;
        boolean value;

        public Signal(Node source, Node target, boolean value) {
            this.source = source;
            this.target = target;
            this.value = value;
        }
        
    }
    
    private class FlipFlop extends Node {
        boolean state = false;
        public FlipFlop(String name) {
            super(name);
        }
        @Override
        public List<Signal> receive(Signal input) {
            List<Signal> list = new ArrayList<>();
            if(!input.value) {
                state = !state;
                for(Node node : outputs) {
                    //System.out.println(name + (state ? " high " : " low ") + node.name);
                    list.add(new Signal(this, node, state));
                }
            }
            return list;
        }
    }
    
    private class Conjunction extends Node {
        Map<Node,Boolean> mem = new HashMap<>();
        public Conjunction(String name) {
            super(name);
        }
        public void addInput(Node node) {
            mem.put(node, false);
        }
        @Override
        public List<Signal> receive(Signal input) {
            List<Signal> list = new ArrayList<>();
            mem.put(input.source, input.value);
            boolean allHigh = true;
            for(Node node : mem.keySet()) {
                if(!mem.get(node))
                    allHigh = false;
            }
            for(Node node : outputs) {
                //System.out.println(name + (allHigh ? " low " : " high ") + node.name);
                list.add(new Signal(this, node, !allHigh));
            }
            return list;
        }
    }
    
    private class Broadcast extends Node {
        public Broadcast(String name) {
            super(name);
        }
        @Override
        public List<Signal> receive(Signal input) {
            List<Signal> list = new ArrayList<>();
            for(Node node : outputs) {
                //System.out.println(name + (input.value ? " high " : " low ") + node.name);
                list.add(new Signal(this, node, input.value));
            }
            return list;
        }
    }
    
    private class Sink extends Node {
        public Sink(String name) {
            super(name);
        }
        @Override
        public List<Signal> receive(Signal input) {
            return new ArrayList<>();
        }
    }
    
    private class Target extends Node {
        public Target(String name) {
            super(name);
        }
        @Override
        public List<Signal> receive(Signal input) {
            if(input.value)
                return new ArrayList<>();
            return null;
        }
    }
    
    private final File input;
    private static final Map<String,Node> nodes = new HashMap<>();
    
    public Puzzle20(File in) {
        input = in;
    }
    
    public String solve() throws IOException {
        Map<Node,String> temp = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line;
        long sum = 0;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            System.out.println("Line: " + line);
            String[] halves = line.split("->");
            String name = halves[0].substring(1).trim();
            Node node;
            switch(halves[0].charAt(0)) {
                case '%':
                    node = new FlipFlop(name);
                    break;
                case '&':
                    node = new Conjunction(name);
                    break;
                default:
                    node = new Broadcast(name);
                    break;
            }
            System.out.println(" Add node " + name);
            nodes.put(name, node);
            temp.put(node, halves[1].trim());
        }
        for(Node node : temp.keySet()) {
            for(String s : temp.get(node).split(",")) {
                if(!nodes.containsKey(s.trim())) {
                    nodes.put(s.trim(), new Sink(s.trim()));
                }
                Node dest = nodes.get(s.trim());
                if(dest instanceof Conjunction)
                    ((Conjunction) dest).addInput(node);
                System.out.println("Connect " + node.name + " to " + dest.name + " (" + s + ")");
                node.addNode(dest);
            }
        }
        int round = 0;
        long high = 0, low = 0;
        boolean iState = false;
        while(!iState && round < 1000) {
            List<Signal> inputs = new ArrayList<>();
            inputs.add(new Signal(null, nodes.get("roadcaster"), false));
            while(!inputs.isEmpty()) {
                List<Signal> outputs = new ArrayList<>();
                for(Signal s : inputs) {
                    if(s.value)
                        high++;
                    else
                        low++;
                    outputs.addAll(s.target.receive(s));
                }
                System.out.println(" hi=" + high + " lo=" + low);
                inputs = outputs;
            }
            iState = initState();
            round++;
            System.out.println("Round " + round + ": Init State " + iState);
        }
        int times = 1000 / round;
        high *= times;
        low *= times;
        sum = high * low;
        return "" + sum;
    }
    
    public String solve2() throws IOException {
        Map<Node,String> temp = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line;
        long sum = 1;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(line.isBlank())
                continue;
            //System.out.println("Line: " + line);
            String[] halves = line.split("->");
            String name = halves[0].substring(1).trim();
            Node node;
            switch(halves[0].charAt(0)) {
                case '%':
                    node = new FlipFlop(name);
                    break;
                case '&':
                    node = new Conjunction(name);
                    break;
                default:
                    node = new Broadcast(name);
                    break;
            }
            //System.out.println(" Add node " + name);
            nodes.put(name, node);
            temp.put(node, halves[1].trim());
        }
        nodes.put("rx", new Target("rx"));
        for(Node node : temp.keySet()) {
            for(String s : temp.get(node).split(",")) {
                if(!nodes.containsKey(s.trim())) {
                    nodes.put(s.trim(), new Sink(s.trim()));
                }
                Node dest = nodes.get(s.trim());
                if(dest instanceof Conjunction)
                    ((Conjunction) dest).addInput(node);
                //System.out.println("Connect " + node.name + " to " + dest.name + " (" + s + ")");
                node.addNode(dest);
            }
        }
        Node root = nodes.get("roadcaster");
        while(root.outputs.size() == 1)
            root = root.outputs.get(0);
        Node end = nodes.get("rx");
        //List<Node> list = nodes.values().stream().filter(n -> n.hasOutput(end.name)).collect(Collectors.toList());
        List<Node> list = new ArrayList<>();
        list.add(end);
        System.out.println("Ends:");
        for(Node node : list) {
            System.out.println(node);
        }
        for(Node start : root.outputs) {
            List<String> lines = new ArrayList<>();
            for(Node node : getCycle(start, list))
                lines.add(node.toString());
            long round = countCycles(start.name, lines);
            System.out.println(" Cycle rounds: " + round);
            sum *= round;
        }
        return "" + sum;
    }
    
    private boolean initState() {
        boolean flipsOn = nodes.values().stream().filter(n -> n instanceof FlipFlop).anyMatch(n -> ((FlipFlop)n).state);
        boolean memHigh = nodes.values().stream().filter(n -> n instanceof Conjunction).anyMatch(n -> ((Conjunction)n).mem.values().stream().anyMatch(b -> b));
        return !flipsOn && !memHigh;
    }
    
    private List<Node> getCycle(Node start, List<Node> ends) {
        List<Node> list = new ArrayList<>();
        List<Node> work = new ArrayList<>();
        work.add(start);
        while(!work.isEmpty()) {
            Node node = work.remove(0);
            if(list.contains(node))
                continue;
            list.add(node);
            List<Node> set = node.outputs;
            for(Node n : set) {
                if(!list.contains(n) && !ends.contains(n))
                    work.add(n);
            }
        }
        return list;
    }
    
    private long countCycles(String startName, List<String> lines) {
        Map<Node,String> temp = new HashMap<>();
        Map<String,Node> net = new HashMap<>();
        for(String line : lines) {
            line = line.trim();
            if(line.isBlank())
                continue;
            String[] halves = line.split("->");
            String name = halves[0].substring(1).trim();
            Node node;
            switch(halves[0].charAt(0)) {
                case '%':
                    node = new FlipFlop(name);
                    break;
                case '&':
                    node = new Conjunction(name);
                    break;
                default:
                    node = new Broadcast(name);
                    break;
            }
            net.put(name, node);
            temp.put(node, halves[1].trim());
        }
        net.put("rx", new Target("rx"));
        for(Node node : temp.keySet()) {
            for(String s : temp.get(node).split(",")) {
                if(s == null || s.isBlank())
                    continue;
                if(!net.containsKey(s.trim())) {
                    net.put(s.trim(), new Sink(s.trim()));
                }
                Node dest = net.get(s.trim());
                if(dest instanceof Conjunction)
                    ((Conjunction) dest).addInput(node);
                node.addNode(dest);
            }
        }
        long round = 0;
        boolean running = true;
        while(running) {
            List<Signal> inputs = new ArrayList<>();
            inputs.add(new Signal(null, net.get(startName), false));
            while(running && !inputs.isEmpty()) {
                List<Signal> outputs = new ArrayList<>();
                for(Signal s : inputs) {
                    List<Signal> l = s.target.receive(s);
                    if(l == null) {
                        running = false;
                        break;
                    }
                    outputs.addAll(l);
                }
                inputs = outputs;
            }
            round++;
        }
        return round;
    }
}

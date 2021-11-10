package com.vik.advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10Take2 {
    static class Node {
        Integer data;
        Set<Node> children;

        public Node(int item) {
            data = item;
            children = new LinkedHashSet<>();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Node)) {
                return false;
            }

            Node node = (Node) o;

            return Objects.equals(data, node.data);
        }

        @Override
        public int hashCode() {
            return data != null ? data.hashCode() : 0;
        }
    }

    static class Tree {
        //Root of the Binary Tree
        Node root;

        /* Function to get the count of leaf nodes in a binary tree*/
        long getLeafCount() {
            return getLeafCount(root);
        }

        long getLeafCount(Node node) {
            if (node == null) {
                return 0;
            }
            if (node.children.isEmpty()) {
                return 1;
            } else {
                return node.children.stream().map(this::getLeafCount).mapToLong(Long::valueOf).sum();
            }
        }


    }

    /* Driver program to test above functions */
    public static void main(String args[]) throws IOException {
//        /* create a tree */
//        Tree tree = new Tree();
//        tree.root = new Node(1);
//        tree.root.left = new Node(2);
//        tree.root.right = new Node(3);
//        tree.root.left.left = new Node(4);
//        tree.root.left.right = new Node(5);
//
//        /* get leaf count of the abve tree */
//        System.out.println("The leaf count of binary tree is : "
//            + tree.getLeafCount());

        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day10.txt"))) {
            final List<Integer> joltageRatings = bufferedReader.lines()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

            int jolts = 3;
            joltageRatings.add(0);
            Collections.sort(joltageRatings);
            joltageRatings.add(findMaxJoltageRatings(joltageRatings, jolts));


            part2(joltageRatings, jolts);


        }

    }

    private static void part2(List<Integer> joltageRatings, int jolts) {
        final Map<Integer, Set<Integer>> visited = new TreeMap<>();
        final Set<Integer> allValues = new HashSet<>(joltageRatings);

        for (Integer joltage : joltageRatings) {
            IntStream.range(1, jolts + 1).forEach(value -> {
                final int o = joltage + value;
                if (allValues.contains(o)) {
                    visited.computeIfAbsent(joltage, integer -> new HashSet<>()).add(o);
                }
            });
        }

        int endJoltage = joltageRatings.get(joltageRatings.size() - 1);
        final Map<Integer, AtomicInteger> counts = new HashMap<>();
        visitNode(endJoltage, visited, 0, counts);
        System.out.println(counts);
    }

    private static boolean visitNode(int endJoltage, Map<Integer, Set<Integer>> visited, int node, Map<Integer, AtomicInteger> counts) {
        if (counts.containsKey(node)) {
            counts.computeIfAbsent(node, i -> new AtomicInteger()).incrementAndGet();
            return true;
        }
        if (visited.containsKey(node)) {

            final Set<Integer> integers = visited.get(node);
            if (integers.contains(endJoltage)) {
                counts.computeIfAbsent(node, integer -> new AtomicInteger()).incrementAndGet();
                return true;
            }

            for (Integer integer : integers) {
                if (counts.containsKey(integer)) {
                    counts.computeIfAbsent(integer, i -> new AtomicInteger()).incrementAndGet();
                } else {
                    visitNode(endJoltage, visited, integer, counts);
                }
            }

        }

        return false;
    }

    private static int findMaxJoltageRatings(List<Integer> numbers, int jolts) {

        return numbers.get(numbers.size() - 1) + jolts;

    }

}

package com.vik.advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day9 {
    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day9.txt"))) {
            final long[] numbers = bufferedReader.lines().mapToLong(Long::parseLong).toArray();


            int size = findPart1(numbers, 25, 25);

            long number = 26796446;
//            long number = 127;

            long minNumber = Long.MAX_VALUE;
            long maxNumber = 0;


            int index = 0;
            long sum = 0;
            boolean found = false;
            List<Long> n = new ArrayList<>();
            do {
                for (int i = index; i < numbers.length; i++) {
                    final long currentNUmber = numbers[i];
                    sum += currentNUmber;

                    n.add(currentNUmber);

                    if (sum > number) {
                        index++;
                        sum =0;
                        n.clear();
                        break;
                    } else if (sum == number) {
                        found = true;
                        break;
                    }
                }
            } while (!found);

            final List<Long> longs = n.stream().sorted().collect(Collectors.toList());
            System.out.println(longs.get(0) + longs.get(longs.size()-1));
        }


    }

    private static int findPart1(long[] numbers, int preamble, int setSize) {
        int index = preamble;
        long number;
        do {
            number = numbers[index];
            boolean found = false;
            for (int i = index - setSize; i < index -1; i++) {
                for (int j = i +1; j < index; j++) {
                    if (number == numbers[i] + numbers[j]) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                break;
            }
            index++;

        } while(true);

        System.out.println(number);
        return index;
    }

    private static int numberOfInnerBags(Map<String, Map<String, Integer>> containers, String bag) {
        int count = 0;
        final Map<String, Integer> innerBags = containers.get(bag);
        if (innerBags == null || innerBags.isEmpty()) {
            return count;
        }

        for (Map.Entry<String, Integer> entry : innerBags.entrySet()) {
            String s = entry.getKey();
            Integer innerCount = entry.getValue();
            final int innerBagsCount = numberOfInnerBags(containers, s);
            count += innerCount * (1 +  innerBagsCount);
        }

        return count;

    }

    private static  Set<String> numberOfContainers(Map<String, Set<String>> containers, Set<String> bags, String bag) {
        final Set<String> innerBags = containers.get(bag);
        if (innerBags != null && !innerBags.isEmpty()) {
            bags.addAll(innerBags);
            for (String containerBag: innerBags) {
                numberOfContainers(containers, bags, containerBag);
            }
        }
        return bags;
    }
}

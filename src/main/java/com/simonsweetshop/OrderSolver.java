package com.simonsweetshop;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OrderSolver {

    public Map<Integer, Integer> solve(int orderSize, Set<Integer> packSizes) {

        if (orderSize <= 0) {
            throw new IllegalArgumentException("Invalid order size!");
        }

        if (packSizes == null || packSizes.isEmpty()) {
            throw new IllegalArgumentException("Invalid pack sizes!");
        }

        NavigableSet<Integer> sortedPackSizes = new TreeSet<>(packSizes).descendingSet();

        Integer minimumPackSize = sortedPackSizes.last();

        /**
         * We normalize the order size: we cannot send any sweets less than the minimum pack size
         */
        int normalizedOrderSize = orderSize;
        if (normalizedOrderSize % minimumPackSize > 0) {
            normalizedOrderSize = (normalizedOrderSize / minimumPackSize) * minimumPackSize + minimumPackSize;
        }

        List<Map<Integer, Integer>> solutions = new ArrayList<>();

        loopPackSizes(normalizedOrderSize, packSizes, ImmutableMap.of(), solution -> solutions.add(solution));

        List<Map<Integer, Integer>> sortedSolutions = solutions.stream()
                .sorted(Comparator.comparing(o -> o.values().stream().reduce(0, Integer::sum)))
                .collect(Collectors.toList());

        return sortedSolutions.get(0);
    }

    private void loopMultipliers(int packSize, int maxMultiplier, int orderSize, Set<Integer> packSizes, Map<Integer, Integer> solution, Consumer<Map<Integer, Integer>> solutionConsumer) {
        for (int multiplier = 1; multiplier <= maxMultiplier; multiplier++) {
            int packOrderSize = packSize * multiplier;
            int newOrderSize = orderSize-packOrderSize;
            if (newOrderSize == 0) {
                solutionConsumer.accept(ImmutableMap.<Integer, Integer>builder().putAll(solution).put(packSize, multiplier).build());
            } else {
                loopPackSizes(newOrderSize,
                        packSizes.stream().filter(ps -> ps != packSize).collect(Collectors.toSet()),
                        ImmutableMap.<Integer, Integer>builder().putAll(solution).put(packSize, multiplier).build(),
                        solutionConsumer
                );
            }
        }
    }

    private void loopPackSizes(int orderSize, Set<Integer> packSizes, Map<Integer, Integer> solution, Consumer<Map<Integer, Integer>> solutionConsumer) {
        for (int packSize : packSizes) {
            int packSizeMaxMultiplier = orderSize / packSize;
            if (packSizeMaxMultiplier > 0) {
                loopMultipliers(packSize, packSizeMaxMultiplier, orderSize, packSizes, solution, solutionConsumer);
            }
        }
    }
}

package com.simonsweetshop;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

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

        Map<Integer, Integer> solution = new HashMap<>();

        for (int packSize : sortedPackSizes) {
            int countForPackSize = 0;
            while (normalizedOrderSize >= packSize) {
                ++countForPackSize;
                normalizedOrderSize -= packSize;
            }
            if (countForPackSize > 0) {
                solution.put(packSize, countForPackSize);
            }
        }
        return solution;
    }
}

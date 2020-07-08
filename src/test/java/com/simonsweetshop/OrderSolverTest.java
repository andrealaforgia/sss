package com.simonsweetshop;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class OrderSolverTest {

    private static final Set<Integer> DEFAULT_PACK_SIZES = ImmutableSet.of(
            250, 500, 1000, 2000, 5000
    );

    private int orderSize;
    private Map<Integer, Integer>  orderSolution;
    private final OrderSolver orderSolver = new OrderSolver();
    private Set<Integer> packSizes;

    @BeforeEach
    void setUp() {
        this.packSizes = DEFAULT_PACK_SIZES;
    }

    @Test
    public void shouldSolveStaticOrdersWithDefaultPackSizes() {
        givenTheOrderSizeIs(1);
        whenSolvingTheOrder();
        thenTheOrderShouldBeSolvedAs(ImmutableMap.of(
                250, 1
        ));

        givenTheOrderSizeIs(250);
        whenSolvingTheOrder();
        thenTheOrderShouldBeSolvedAs(ImmutableMap.of(
                250, 1
        ));

        givenTheOrderSizeIs(251);
        whenSolvingTheOrder();
        thenTheOrderShouldBeSolvedAs(ImmutableMap.of(
                500, 1
        ));

        givenTheOrderSizeIs(501);
        whenSolvingTheOrder();
        thenTheOrderShouldBeSolvedAs(ImmutableMap.of(
                500, 1,
                250, 1
        ));

        givenTheOrderSizeIs(12001);
        whenSolvingTheOrder();
        thenTheOrderShouldBeSolvedAs(ImmutableMap.of(
                5000, 2,
                2000, 1,
                250, 1
        ));
    }

    private void thenTheOrderShouldBeSolvedAs(ImmutableMap<Integer, Integer> expectedSolution) {
        assertThat(this.orderSolution).isEqualTo(expectedSolution);
    }

    private void whenSolvingTheOrder() {
        orderSolution = orderSolver.solve(orderSize, this.packSizes);
    }

    private void givenTheOrderSizeIs(int orderSize) {
        this.orderSize = orderSize;
    }
}
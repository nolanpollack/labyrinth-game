package com.northeastern.labyrinth.Strategy;

/**
 * The factory class for {@link IStrategy}.
 */
public class PlayerStrategyFactory {

    /**
     * Return corresponding {@link IStrategy} based on the given strategy name.
     */
    public static IStrategy getStrategyByName(String strategyName) {
        switch (strategyName) {
            case "Riemann":
                return new RiemannStrategy();
            case "Euclid":
                return new EuclidStrategy();
            default:
                throw new IllegalArgumentException("Invalid Strategy Name");
        }
    }

}

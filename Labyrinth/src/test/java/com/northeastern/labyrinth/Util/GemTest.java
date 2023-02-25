package com.northeastern.labyrinth.Util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GemTest {
    @Test
    public void VERIFY_DECODE_TO_ENUM() {
        assertEquals(Gem.AMETHYST, Gem.decodeToEnum("AMETHYST"));
        assertEquals(Gem.AMETHYST, Gem.decodeToEnum("amethyst"));
        assertEquals(Gem.ALMANDINE_GARNET, Gem.decodeToEnum("ALMANDINE_GARNET"));
        assertEquals(Gem.ALMANDINE_GARNET, Gem.decodeToEnum("ALMAnDINE-GaRNeT"));
    }
}

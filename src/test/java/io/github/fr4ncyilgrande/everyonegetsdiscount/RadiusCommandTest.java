package io.github.fr4ncyilgrande.everyonegetsdiscount;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RadiusCommandTest {

    @Test
    void formatsAllOnlineMode() {
        assertEquals("all online players (-1)", RadiusCommand.formatRadius(-1.0D));
    }

    @Test
    void formatsWholeAndDecimalRadii() {
        assertEquals("players within 0 blocks", RadiusCommand.formatRadius(0.0D));
        assertEquals("players within 100 blocks", RadiusCommand.formatRadius(100.0D));
        assertEquals("players within 12.50 blocks", RadiusCommand.formatRadius(12.5D));
    }
}

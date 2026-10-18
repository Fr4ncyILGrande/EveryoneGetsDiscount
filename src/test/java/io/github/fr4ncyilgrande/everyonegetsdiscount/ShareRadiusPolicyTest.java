package io.github.fr4ncyilgrande.everyonegetsdiscount;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShareRadiusPolicyTest {

    private static final UUID PLAYER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID CURING_PLAYER_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void acceptsAllOnlineAndNonNegativeFiniteRadii() {
        assertTrue(ShareRadiusPolicy.isValid(-1.0D));
        assertTrue(ShareRadiusPolicy.isValid(0.0D));
        assertTrue(ShareRadiusPolicy.isValid(100.5D));
    }

    @Test
    void rejectsUnsupportedAndNonFiniteRadii() {
        assertFalse(ShareRadiusPolicy.isValid(-2.0D));
        assertFalse(ShareRadiusPolicy.isValid(Double.NaN));
        assertFalse(ShareRadiusPolicy.isValid(Double.POSITIVE_INFINITY));
        assertFalse(ShareRadiusPolicy.isValid(Double.NEGATIVE_INFINITY));
    }

    @Test
    void neverSharesAgainWithTheCuringPlayer() {
        assertFalse(ShareRadiusPolicy.shouldShare(
                CURING_PLAYER_ID,
                CURING_PLAYER_ID,
                true,
                0.0D,
                ShareRadiusPolicy.ALL_ONLINE
        ));
    }

    @Test
    void allOnlineModeIgnoresWorldAndDistance() {
        assertTrue(ShareRadiusPolicy.shouldShare(
                PLAYER_ID,
                CURING_PLAYER_ID,
                false,
                Double.POSITIVE_INFINITY,
                ShareRadiusPolicy.ALL_ONLINE
        ));
    }

    @Test
    void radiusModeIncludesTheBoundaryInTheSameWorld() {
        assertTrue(ShareRadiusPolicy.shouldShare(
                PLAYER_ID,
                CURING_PLAYER_ID,
                true,
                10_000.0D,
                100.0D
        ));
    }

    @Test
    void radiusModeExcludesPlayersOutsideOrInAnotherWorld() {
        assertFalse(ShareRadiusPolicy.shouldShare(
                PLAYER_ID,
                CURING_PLAYER_ID,
                true,
                10_000.0001D,
                100.0D
        ));
        assertFalse(ShareRadiusPolicy.shouldShare(
                PLAYER_ID,
                CURING_PLAYER_ID,
                false,
                0.0D,
                100.0D
        ));
    }
}

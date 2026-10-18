package io.github.fr4ncyilgrande.everyonegetsdiscount;

import java.util.UUID;

final class ShareRadiusPolicy {

    static final double ALL_ONLINE = -1.0D;

    private ShareRadiusPolicy() {
    }

    static boolean isValid(double radius) {
        return Double.isFinite(radius) && (radius == ALL_ONLINE || radius >= 0.0D);
    }

    static boolean shouldShare(
            UUID playerId,
            UUID curingPlayerId,
            boolean sameWorld,
            double distanceSquared,
            double radius
    ) {
        if (curingPlayerId != null && curingPlayerId.equals(playerId)) {
            return false;
        }

        if (radius == ALL_ONLINE) {
            return true;
        }

        if (!sameWorld || !Double.isFinite(distanceSquared) || distanceSquared < 0.0D) {
            return false;
        }

        return Math.sqrt(distanceSquared) <= radius;
    }
}

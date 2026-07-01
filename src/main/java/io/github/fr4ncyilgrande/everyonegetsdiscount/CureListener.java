package io.github.fr4ncyilgrande.everyonegetsdiscount;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public final class CureListener implements Listener {

    private static final String MAJOR_POSITIVE = "MAJOR_POSITIVE";
    private static final String MINOR_POSITIVE = "MINOR_POSITIVE";

    private static final int MAJOR_POSITIVE_SINGLE_CURE_VALUE = 20;
    private static final int MINOR_POSITIVE_SINGLE_CURE_VALUE = 25;
    private static final long REPUTATION_APPLY_DELAY_TICKS = 5L;

    private final Plugin plugin;
    private final Logger logger;
    private boolean warnedMissingReputationApi;

    public CureListener(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVillagerCured(EntityTransformEvent event) {
        if (event.getTransformReason() != EntityTransformEvent.TransformReason.CURED) {
            return;
        }

        if (!(event.getEntity() instanceof ZombieVillager zombieVillager)) {
            return;
        }

        if (!(event.getTransformedEntity() instanceof Villager villager)) {
            return;
        }

        UUID villagerId = villager.getUniqueId();
        UUID curingPlayerId = getCuringPlayerId(zombieVillager);
        List<UUID> sharedPlayerIds = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID playerId = player.getUniqueId();
            if (curingPlayerId != null && curingPlayerId.equals(playerId)) {
                continue;
            }

            sharedPlayerIds.add(playerId);
        }

        if (sharedPlayerIds.isEmpty()) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(
                plugin,
                () -> applySharedDiscounts(villagerId, sharedPlayerIds),
                REPUTATION_APPLY_DELAY_TICKS
        );
    }

    private void applySharedDiscounts(UUID villagerId, List<UUID> sharedPlayerIds) {
        Entity entity = Bukkit.getEntity(villagerId);
        if (!(entity instanceof Villager villager) || !villager.isValid()) {
            return;
        }

        for (UUID playerId : sharedPlayerIds) {
            if (!applySharedCureReputation(villager, playerId)) {
                warnMissingReputationApi();
                return;
            }
        }
    }

    private static UUID getCuringPlayerId(ZombieVillager zombieVillager) {
        OfflinePlayer curingPlayer = zombieVillager.getConversionPlayer();
        return curingPlayer == null ? null : curingPlayer.getUniqueId();
    }

    private static boolean applySharedCureReputation(Villager villager, UUID playerId) {
        return applyPaperReputation(villager, playerId) || applySpigotReputation(villager, playerId);
    }

    private static boolean applySpigotReputation(Villager villager, UUID playerId) {
        try {
            Class<?> villagerClass = Class.forName("org.bukkit.entity.Villager");
            Class<?> reputationTypeClass = Class.forName("org.bukkit.entity.Villager$ReputationType");

            Method getReputation = villagerClass.getMethod("getReputation", UUID.class, reputationTypeClass);
            Method setReputation = villagerClass.getMethod("setReputation", UUID.class, reputationTypeClass, int.class);

            Object majorPositive = reputationTypeClass.getField(MAJOR_POSITIVE).get(null);
            Object minorPositive = reputationTypeClass.getField(MINOR_POSITIVE).get(null);

            setSpigotReputationAtLeast(
                    villager,
                    playerId,
                    getReputation,
                    setReputation,
                    majorPositive,
                    MAJOR_POSITIVE_SINGLE_CURE_VALUE
            );
            setSpigotReputationAtLeast(
                    villager,
                    playerId,
                    getReputation,
                    setReputation,
                    minorPositive,
                    MINOR_POSITIVE_SINGLE_CURE_VALUE
            );
            return true;
        } catch (Exception | LinkageError ignored) {
            return false;
        }
    }

    private static void setSpigotReputationAtLeast(
            Villager villager,
            UUID playerId,
            Method getReputation,
            Method setReputation,
            Object reputationType,
            int minimumValue
    ) throws ReflectiveOperationException {
        int currentValue = ((Number) getReputation.invoke(villager, playerId, reputationType)).intValue();
        int newValue = Math.max(currentValue, minimumValue);
        setReputation.invoke(villager, playerId, reputationType, newValue);
    }

    private static boolean applyPaperReputation(Villager villager, UUID playerId) {
        try {
            Class<?> villagerClass = Class.forName("org.bukkit.entity.Villager");
            Class<?> reputationClass = Class.forName("com.destroystokyo.paper.entity.villager.Reputation");
            Class<?> reputationTypeClass = Class.forName("com.destroystokyo.paper.entity.villager.ReputationType");

            Method getVillagerReputation = villagerClass.getMethod("getReputation", UUID.class);
            Method setVillagerReputation = villagerClass.getMethod("setReputation", UUID.class, reputationClass);
            Method getReputationValue = reputationClass.getMethod("getReputation", reputationTypeClass);
            Method setReputationValue = reputationClass.getMethod("setReputation", reputationTypeClass, int.class);

            Object reputation = getVillagerReputation.invoke(villager, playerId);
            if (reputation == null) {
                return false;
            }

            Object majorPositive = reputationTypeClass.getField(MAJOR_POSITIVE).get(null);
            Object minorPositive = reputationTypeClass.getField(MINOR_POSITIVE).get(null);

            setPaperReputationAtLeast(
                    reputation,
                    getReputationValue,
                    setReputationValue,
                    majorPositive,
                    MAJOR_POSITIVE_SINGLE_CURE_VALUE
            );
            setPaperReputationAtLeast(
                    reputation,
                    getReputationValue,
                    setReputationValue,
                    minorPositive,
                    MINOR_POSITIVE_SINGLE_CURE_VALUE
            );

            setVillagerReputation.invoke(villager, playerId, reputation);
            return true;
        } catch (Exception | LinkageError ignored) {
            return false;
        }
    }

    private static void setPaperReputationAtLeast(
            Object reputation,
            Method getReputationValue,
            Method setReputationValue,
            Object reputationType,
            int minimumValue
    ) throws ReflectiveOperationException {
        int currentValue = ((Number) getReputationValue.invoke(reputation, reputationType)).intValue();
        int newValue = Math.max(currentValue, minimumValue);
        setReputationValue.invoke(reputation, reputationType, newValue);
    }

    private void warnMissingReputationApi() {
        if (warnedMissingReputationApi) {
            return;
        }

        warnedMissingReputationApi = true;
        logger.warning("EveryoneGetsDiscount could not access a supported villager reputation API. No shared cure discounts were applied.");
    }
}

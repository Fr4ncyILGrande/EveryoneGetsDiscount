package io.github.fr4ncyilgrande.everyonegetsdiscount;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class EveryoneGetsDiscount extends JavaPlugin {

    private static final double DEFAULT_SHARE_RADIUS = -1.0D;

    private double shareRadius = DEFAULT_SHARE_RADIUS;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadShareRadius();

        RadiusCommand radiusCommand = new RadiusCommand(this);
        org.bukkit.command.PluginCommand command = Objects.requireNonNull(
                getCommand("everyonegetsdiscount"),
                "Command everyonegetsdiscount is missing from plugin.yml"
        );
        command.setExecutor(radiusCommand);
        command.setTabCompleter(radiusCommand);

        getServer().getPluginManager().registerEvents(new CureListener(this), this);
        getLogger().info("EveryoneGetsDiscount enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("EveryoneGetsDiscount disabled.");
    }

    public double getShareRadius() {
        return shareRadius;
    }

    public void setShareRadius(double shareRadius) {
        this.shareRadius = shareRadius;
        getConfig().set("share-radius", shareRadius);
        saveConfig();
    }

    public void reloadShareRadius() {
        double configuredRadius = readConfiguredShareRadius();
        if (!isValidShareRadius(configuredRadius)) {
            getLogger().warning("Invalid share-radius in config.yml: " + configuredRadius + ". Using -1 instead.");
            configuredRadius = DEFAULT_SHARE_RADIUS;
        }

        shareRadius = configuredRadius;
    }

    private double readConfiguredShareRadius() {
        Object configuredValue = getConfig().get("share-radius", DEFAULT_SHARE_RADIUS);
        if (configuredValue instanceof Number number) {
            return number.doubleValue();
        }

        if (configuredValue instanceof String stringValue) {
            try {
                return Double.parseDouble(stringValue);
            } catch (NumberFormatException ignored) {
                getLogger().warning("Invalid share-radius in config.yml: " + stringValue + ". Using -1 instead.");
                return DEFAULT_SHARE_RADIUS;
            }
        }

        getLogger().warning("Invalid share-radius in config.yml. Using -1 instead.");
        return DEFAULT_SHARE_RADIUS;
    }

    private static boolean isValidShareRadius(double shareRadius) {
        return Double.isFinite(shareRadius) && (shareRadius == DEFAULT_SHARE_RADIUS || shareRadius >= 0.0D);
    }
}

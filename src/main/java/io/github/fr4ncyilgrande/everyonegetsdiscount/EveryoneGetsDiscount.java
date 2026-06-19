package io.github.fr4ncyilgrande.everyonegetsdiscount;

import org.bukkit.plugin.java.JavaPlugin;

public final class EveryoneGetsDiscount extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CureListener(getLogger()), this);
        getLogger().info("EveryoneGetsDiscount enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("EveryoneGetsDiscount disabled.");
    }
}

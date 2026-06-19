EveryoneGetsDiscount

In vanilla Minecraft, curing a zombie villager rewards only the player who did the curing. On a multiplayer server, that means one person gets cure discounts while everyone else pays full price - even if they helped set up the trap, supplied the golden apple, or simply happen to share the trading hall.

EveryoneGetsDiscount fixes that. When any zombie villager is cured, every other player currently online receives the same vanilla cure gossip values on that villager automatically, with no commands, no setup, and no interference with any other game mechanic. The curing player keeps Minecraft’s normal vanilla cure discount, while the plugin shares that same type of discount with the rest of the online server.
How it works

The plugin listens for the moment a zombie villager finishes transforming back into a villager. At that point, Minecraft applies its normal cure gossip to the curing player, and the plugin applies the same two vanilla gossip entries to the other online players:

    MAJOR_POSITIVE (+20) - the main cure discount. Permanent, never decays, maxes out after a single cure. This is the bulk of the trading discount and stays on that villager forever.

    MINOR_POSITIVE (+25) - a secondary bonus, identical to the one vanilla applies. Fades at a rate of 1 point every 20 real-world minutes, disappearing completely after roughly 8 hours.

Both values are taken directly from vanilla's own gossip system. No artificial price reductions, no multiplier changes - just the standard cure discount, shared. Final trade prices may still differ between players if they already had different reputation with that villager before the cure.
Features

    Vanilla-accurate values - the plugin uses the exact same gossip types and single-cure amounts Minecraft assigns when a player cures a villager themselves.

    No configuration needed - install and forget; there are no commands, config files, or permissions to manage.

    No proximity requirement - players receive the discount regardless of which world or dimension they are in; being online is enough.

    Only the cured villager is affected - other nearby villagers are not touched by the plugin (vanilla gossip propagation between villagers continues normally on its own).

    Respects cancellation - if another plugin cancels the cure transformation, no discount is applied.

    Players offline during the cure miss out - the discount is distributed at the exact moment of transformation; late joiners are not included.

    Lightweight - a single event listener with no persistent state, no database, no scheduler.

The plugin does not touch trade prices, merchant recipes, or any economy plugin's data. It only interacts with the villager gossip system, the same way vanilla does.

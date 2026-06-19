![Group trading with villagers](https://cdn.modrinth.com/data/cached_images/4fde5d9f877844a8d51796d085d45dbae977ea31.jpeg)

# EveryoneGetsDiscount

In vanilla Minecraft, curing a zombie villager rewards only the player who did the curing. On a multiplayer server, that means one person gets cure discounts while everyone else pays full price - even if they helped set up the trap, supplied the golden apple, or simply happen to share the trading hall.

**EveryoneGetsDiscount fixes that**. When any zombie villager is cured, every other player currently online receives the same vanilla cure gossip values on that villager automatically, with no commands, no setup, and no interference with any other game mechanic. The curing player keeps Minecraft’s normal vanilla cure discount, while the plugin shares that same type of discount with the rest of the online server.

## How it works

The plugin listens for the moment a zombie villager finishes transforming back into a villager. At that point, Minecraft applies its normal cure gossip to the curing player, and the plugin applies the same two vanilla gossip entries to the other online players:

• `MAJOR_POSITIVE (+20)` - the main cure discount. Permanent, never decays, maxes out after a single cure. This is the bulk of the trading discount and stays on that villager forever.

• `MINOR_POSITIVE (+25)` - a secondary bonus, identical to the one vanilla applies. Fades at a rate of 1 point every 20 real-world minutes, disappearing completely after roughly 8 hours.

Both values are taken directly from vanilla's own gossip system. No artificial price reductions, no multiplier changes - just the standard cure discount, shared. Final trade prices may still differ between players if they already had different reputation with that villager before the cure.

## Features

1. **Vanilla-accurate values** - the plugin uses the exact same gossip types and single-cure amounts Minecraft assigns when a player cures a villager themselves.
2. **No configuration needed** - install and forget; there are no commands, config files, or permissions to manage.
3. **No proximity requirement** - players receive the discount regardless of which world or dimension they are in; being online is enough.
4. **Only the cured villager is affected** - other nearby villagers are not touched by the plugin (vanilla gossip propagation between villagers continues normally on its own).
5. **Respects cancellation** - if another plugin cancels the cure transformation, no discount is applied.
6. **Players offline during the cure miss out** - the discount is distributed at the exact moment of transformation; late joiners are not included.
7. **Lightweight** - a single event listener with no persistent state, no database, no scheduler.

The plugin does not touch trade prices, merchant recipes, or any economy plugin's data. It only interacts with the villager gossip system, the same way vanilla does.

## FAQ

**Do players need to be near the villager to get the discount?**  
No. Any player who is online at the moment of the cure receives the shared discount, regardless of where they are in the server. The curing player receives Minecraft’s normal vanilla cure discount.

**Does the discount last forever?**  
The main component (`MAJOR_POSITIVE`) is permanent and matches vanilla exactly - one cure already brings it to its maximum value. A secondary bonus (`MINOR_POSITIVE`) fades over roughly 8 real-world hours, after which the permanent discount remains.

**Does it affect Hero of the Village or raid mechanics?**  
Not at all. Hero of the Village is a separate potion effect from completing raids. The two bonuses stack normally and neither interferes with the other.

**Does curing the same villager multiple times give more discount?**  
No. The shared discount does not stack extra reductions from repeated cures. The plugin keeps online players at the vanilla single-cure gossip values: `MAJOR_POSITIVE (+20)` and `MINOR_POSITIVE (+25)`. If a player already has equal or higher reputation with that villager, the plugin does not lower it.

**Does it work with other economy or trading plugins?**  
The plugin only writes to the vanilla villager gossip system. Whether other plugins read that data or replace it entirely with their own pricing is up to those plugins.

<p align="center">
  <a href="https://discord.gg/Zn5d9d2pBU" target="_blank">
    <img src="https://i.ibb.co/8Dh71sx0/Discord-logo.png" alt="Discord" width="300">
  </a>
</p>
<p align="center">
  <em>Join the Discord if you have any questions or need support!</em>
</p>

# Changelog

## 1.0.1

- Delays shared reputation application by 5 ticks after cure completion.
- Keeps the eligible player list captured at the exact cure moment.
- Uses Paper/Purpur's reputation API first when available, with Spigot as a fallback.
- Keeps the no-stacking behavior for repeated cures: shared players are kept at the vanilla single-cure values instead of receiving extra repeated discounts.
- Gives Paper extra time to finish villager conversion, brain, profession, and trade state initialization before the plugin writes reputation data.

This release is a conservative hotfix for reports of villagers rerolling trades or becoming unemployed after cures on Paper 26.1.2 Build 72.

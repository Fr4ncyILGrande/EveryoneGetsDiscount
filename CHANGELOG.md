# Changelog

## 1.0.1

- Delays shared reputation application by 5 ticks after cure completion.
- Keeps the eligible player list captured at the exact cure moment.
- Adds `/egd radius <blocks|-1>` for admin-controlled sharing range.
- Adds `config.yml` with `share-radius: -1` by default.
- Rejects invalid radius values such as negative values other than `-1`, `NaN`, and infinity.
- Uses Paper/Purpur's reputation API first when available, with Spigot as a fallback.
- Keeps the no-stacking behavior for repeated cures: shared players are kept at the vanilla single-cure values instead of receiving extra repeated discounts.
- Gives Paper extra time to finish villager conversion, brain, profession, and trade state initialization before the plugin writes reputation data.
- Keeps `api-version: "26.1.2"` to support `26.1.2` while remaining compatible with newer `26.x` server versions.

This release is a conservative hotfix for reports of villagers rerolling trades or becoming unemployed after cures on Paper 26.1.2 Build 72.

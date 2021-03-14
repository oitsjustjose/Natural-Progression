# Natural Progression Changelog (1.16)

## 1.4.0

### Added

- Compatibility for the new 1.16 Nether Woods -- whoops 🙄
- Compatibility for several modded woods (INFINITELY many thanks to [sargunv's](https://github.com/sargunv-mc-mods/Natural-Progression/tree/data-fixes) data fixes). This includes:
    - Atmospheric
    - Autumnity
    - Bloomful
    - Botania (livingwood)
    - Endergetic Expansion
    - Oh the Biomes You'll Go
    - Traverse
    - Upgrade Aquatic
- New tags for bypassing the "You cannot break this block without <TOOLTYPE>" message
    - Add any tool/item to the tag `natural-progression:override_axes` to bypass the check for an axe
    - Add any tool/item to the tag `natural-progression:override_pickaxes` to bypass the check for a pick

### Fixed

- Plank recipes being removed for ALL machines/recipes. Now they're only removed 

### Changed

- Removed `removeAllPlankRecipes` config option -- everything is datapack powered now thanks to [sargunv](https://github.com/sargunv-mc-mods).

## 1.3.0

### Initial Port to 1.16.x!

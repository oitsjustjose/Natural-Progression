# Natural Progression Changelog (1.19.x)


## 2.3.6

### Added:

- New config option for the "Incorrect Tool" damage - [thank you Saereth!](https://github.com/oitsjustjose/Natural-Progression/pull/83)
- Config option to control the Knapping mechanic entirely

### Fixed:

- Some of the Create Pebbles having a Missing Texture

## 2.3.5

### Added:

- New **Item** Tags for ignoring tool types:

  "Wooden" and "Stone" tools are determined by the tier of block they can break, not the material they're made of, so
  these tags should hopefully help:
    - `natprog:allowed_wooden_tools` is a list of tools which have the same tier as Wood that can still be used
      when `toolNeutering` is enabled
    - `natprog:allowed_stone_tools` is a list of tools which have the same tier as
      Stone that can still be used when `toolNeutering` is enabled

- New **Block** Tags for blacklisting which blocks twigs and pebbles can be placed on:
    - `natprog:wont_support_pebble`
    - `natprog:wont_support_twig`

### Changed:

- Renamed tag `natprog:override_axes` to `natprog:considered_as_axe` for clarity
- Renamed tag `natprog:override_pickaxes` to `natprog:considered_as_pickaxe` for clarity
- Config entry `pebblePlacementBlacklist` has been removed in favor of `natprog:wont_support_pebble`
- Config entry `twigPlacementBlacklist` has been removed in favor of `natprog:wont_support_twig`
- Internal refactor to match new dev style (Thanks UnrealEngine!). Should see no practical change.
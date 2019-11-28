# Natural Progression Changelog

## 1.1.2

### Added

- Chance config for knapping to not succeed (e.g. not result in flint)
- Ground-type blocks (e.g. gravel, dirt, grass) are now harder to break without the right tool

### Fixed

- Fixed pebbles placing while you're trying to knap if you look at a block

### Changed

- When knapping, only the swinging arm's pebble will break
- One of the crafting sounds for non-wood recipes
- Sounds for knapping success vs. failure

## 1.1.1

### Fixed

- Server Thread occasionally hanging
- Red Sandstone not dropping its Pebbles

### Changed

- Pebbles will no longer replace blocks other than air and water

## 1.1.0

### Added

- Red Sandstone Pebbles
- Cobbled Red Sandstone

### Changed

- **Important**: Plank recipes are **no longer dynamic**. This means that if you want a plank recipe, you'll need to make it by hand (**even though it may be removed automatically**), but this _does_ add compatability with any mod out there, and for custom more difficult recipes! See [this file](https://gist.github.com/oitsjustjose/11a78df64d55cbcb00c3a7b646db729b#file-damage-tool-example-json) for how to format a recipe like this. Yes, this means that you can use this recipe type to make _any_ recipe that requires a tool, and it will damage (or break if no durability left) that tool. This also takes into account the Unbreaking enchantment!

### Fixed

- Log spam from old crafting recipe handlers

### Removed

- JEI Compat -- It's not needed anymore!!

## 1.0.8

### Added

- Ability to strip logs via crafting using a saw
- JEI support for sawing and stripping
- JEI description pages for pebbles and how to use them
- Wood tool items and swords - even if craftable - are unusable

### Changed

- Dimension blacklist config setting to a dimension whitelist
- You can now use stripped wood to craft planks if stripped woods are required for crafting them.

### Fixed

- JustEnoughResources saying pebbles are enchantable

## 1.0.7b

### Fixed

- Fixes implemented in 1.0.6b not carrying over in 1.0.7 (you guessed it, forgot to push on one computer before working on another one 😐)

## 1.0.7

### Added

- Stand-in Quark compatability (until Vazkii hopefully adds it)

## 1.0.6

### Added

- Sandstone Pebbles and Cobbled Sandstone
- Config option for notifying the player that they can't break `<x>` block without `<y>` tool

### Changed

- The pebble chosen to place on thet surface is now based on what stones are underneath
- Hardened code for improved reliability 😊

## 1.0.5

### Fixed

- Server crash due to tooltips

## 1.0.4

### Added

- Inability to punch hard blocks (e.g. stone-like, anvil-like and metal-like blocks)

### Changed

- Texture and Model for Pebbles (they're much cuter now :blush:)
- New Cobbled Diorite Texture (matches new diorite better)

## 1.0.3

### Added

- Tooltip to Axe / Saw Items to indicate how to craft planks
- Config option for Pebbles to be replaceable with other blocks when building

### Changed

- Pebbles can be replaceable with other blocks when building

## 1.0.2

### Added

- Trying to break wooden blocks with an empty hand will damage you at random. This is called "Splintering" and it has its own death source name.

### Changed

- Undo the notification cooldown for not being able to break wood blocks

### Fixed

- A dumb issue with breaking wood-blocks

## 1.0.1

### Changed

- Stone/Andesite/Diorite/Granite now drop 1 to 4 pebbles instead of 4 to 9.
- You can no longer break _any_ wooden-type block without an axe

### Fixed

- Cobbled Andesite, Diorite and Granite not dropping themselves
- Air blocks replacing water blocks above a water-logged pebble

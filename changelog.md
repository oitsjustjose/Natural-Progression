# Natural Progression Changelog

<<<<<<< HEAD
## 1.0.6b

### Fixed

- Sandstone not dropping sandstone pebbles
- Sandstone pebbles not dropping themselves
=======
## 1.0.7

### Added

- Stand-in Quark compatability (until Vazkii hopefully adds it)
>>>>>>> fd45aab6d4868231406968ce39f2abc21a624108

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

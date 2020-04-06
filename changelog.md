# Natural Progression Changelog

## 1.3.0b

**Hotfix for twigs not dropping sticks**

## 1.3.0

### Added

- **Twigs**: These are sticks that generate on the ground. Break or right-click them to get sticks, instead of having to break leaves (as annoying as that is...).
  - Twig frequency has its own config option and can be completely disabled if you don't care for them.
  - Twig textures rotate randomly.
  - Twig textures rely on your current resource pack's stick texture, so they'll always fit in!
- **Advancements**: Instead of relying on annoying tooltips, an advancements system has been made to assist players step-by-step down their new progression line.
  - Additionally, the old early-game advancements have been removed or shifted to line up with progression changed (e.g. Acquire Hardware follows after getting a bone pick, stone and wood tool achievements removed, etc.).

### Removed

- Help Tooltips
- Sounds

## 1.2.2

### Changed

- Bone Drop Addition now works using the entity's name (e.g. `minecraft:bat`) instead of modifying the internalized loot table.
- Config layout for adding bone drops to entities. **You will need to change this again if you changed it before!**.

## 1.2.1

### Removed

- Passable Leaves: There's a problem with making the collision box empty for the leaf, and that's the fact that many blocks check that shape to determine if they can stay. This includes vines, for example, which would update randomly and break themselves off of the (transparent) leaves because their collision box isn't "solid".

## 1.2.0

### Added

- Furthered Progression

  - Bones drop from those entities / blocks specified in the config (add your own even!)
  - Percentage of bone drop also specified in config
  - Bones can be knapped into 1-2 bone shards by holding a bone in one hand and flint in another
  - Bone Shards can be used to make a Bone Knife (early-game, decent weapon)
  - Bone Shards can be used to make a Bone Pickaxe (early-game, decent pick capable of gathering iron-level blocks)

- Config option to disable harder-to-break soils
- Passable Leaves

  - Can be disabled
  - Can (at least usually) momentarily reduce fall damage

- Stone Tool Recipe Removal (with config option)

## 1.1.3

### Added

- Tooltips to wooden tools indicating that they will not function
- Help tooltips for pebbles

### Changed

- Plank and log stripping recipes now use tags
  - Saws are replaced with `natural-progression:saw`
  - Axes are replaced with `natural-progression:axe`
- Tooltip for crafting only applies to items within the `natural-progression:axe` or `natural-progression:saw` tags.

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

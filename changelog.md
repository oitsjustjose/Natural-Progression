# Natural Progression Changelog (1.20.1)

## 2.3.5

### Initial Port to 1.20.1 and Neo

### Added:

- Support for Bamboo & Cherry Wood in Sawing/Axing Recipes

### Changed:

- If you were using the `makeGroundBlocksHarder` feature, the blocks that are considered "ground" are now determined via
  the Block tag `natprog:earthy_blocks`
- If you were using the ability to require tools to break *all* wood-based blocks, you will now need to add *every
  single block* to the tag `natprog:woods_requiring_tool` because Mojang decided to remove a **fundamental** component
  of Blocks that has been there since before I started modding in Beta 1.7.3 😡
- Same as above, but for the requirement of a pickaxe-adjacent tool to mine stone-based blocks
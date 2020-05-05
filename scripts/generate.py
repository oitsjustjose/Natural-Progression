#!/usr/bin/env python3

from common.wood_type import WoodType

WOODS = [
    WoodType.default_wood("minecraft", "oak"),
    WoodType.default_wood("minecraft", "spruce"),
    WoodType.default_wood("minecraft", "birch"),
    WoodType.default_wood("minecraft", "jungle"),
    WoodType.default_wood("minecraft", "dark_oak"),
    WoodType.default_wood("minecraft", "acacia"),
    WoodType.default_wood("atmospheric", "aspen"),
    WoodType.default_wood("atmospheric", "kousa"),
    WoodType.default_wood("atmospheric", "yucca"),
    WoodType.default_wood("atmospheric", "rosewood").replace(
        wood="atmospheric:rosewood", stripped_wood="atmospheric:stripped_rosewood"
    ),
    WoodType.default_wood("autumnity", "maple"),
    WoodType.default_wood("autumnity", "sappy_maple").replace(
        stripped_log="autumnity:stripped_maple_log",
        stripped_wood="autumnity:stripped_maple_wood",
        planks="autumnity:maple_planks",
        never_override=True,
    ),
    WoodType.default_wood("bloomful", "wisteria"),
    WoodType.legacy_wood("botania", "livingwood").replace(log="botania:livingwood"),
    WoodType.suffix_wood("midnight", "shadowroot"),
    WoodType.suffix_wood("midnight", "dark_willow"),
    WoodType.suffix_wood("midnight", "dead_wood").replace(
        wood="midnight:dead_wood", stripped_wood="midnight:dead_wood_stripped"
    ),
    WoodType.legacy_stem("midnight", "bogshroom"),
    WoodType.legacy_stem("midnight", "nightshroom"),
    WoodType.legacy_stem("midnight", "dewshroom"),
    WoodType.legacy_stem("midnight", "viridshroom"),
    WoodType.default_wood("swampexpansion", "willow"),
    WoodType.default_wood("traverse", "fir"),
    WoodType.quite_weird(
        namespace="upgrade_aquatic",
        log="driftwood_log",
        wood="driftwood",
        planks="driftwood_planks",
        stripped_log="driftwood_log_stripped",
        stripped_wood="driftwood_stripped",
    ),
    WoodType.quite_weird(
        namespace="endergetic",
        log="poise_stem",
        wood="poise_wood",
        planks="poise_planks",
        stripped_log="poise_stem_stripped",
        stripped_wood="poise_wood_stripped",
    ),
    WoodType.quite_weird(
        namespace="endergetic",
        log="poise_stem_glowing",
        wood="poise_wood_glowing",
        planks="poise_planks",
        stripped_log="poise_stem_stripped",
        stripped_wood="poise_wood_stripped",
    ).replace(never_override=True),
]


def main():
    recipes = [
        progression.recipe
        for wood_type in WOODS
        for progression in wood_type.progressions
    ]

    seen = dict()
    num_dups = 0
    for recipe in recipes:
        if recipe.path in seen and seen[recipe.path] != recipe.data:
            print(f"found conflicting path '{recipe.path}'")
            num_dups += 1
        seen[recipe.path] = recipe.data

    if num_dups > 0:
        print(f"ERROR: Found {num_dups} conflicting paths, which is greater than 0")
        exit(1)

    for recipe in recipes:
        recipe.dump()


if __name__ == "__main__":
    main()

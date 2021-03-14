#!/usr/bin/env python3

from common.wood_type import WoodType

WOODS = [
    WoodType.default_wood("biomesoplenty", "fir")
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

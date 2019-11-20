"""
Author: oitsjustjose | oitsjustjose@github

A Python script for generating Natural Progression recipes
"""

import json
import os
from pip._vendor.colorama import Fore

def genSawStripping(saws: list, strip_map: dict) -> None:
    """
    Generates recipes for stripping logs with a saw
    """
    for log in strip_map:
        stripped_log = strip_map[log]

        json_raw = {
            "type": "natural-progression:damage_tools",
            "ingredients": [
                {
                    "item": log
                },
                [{"item": saw} for saw in saws]
            ],
            "result": {
                "item": stripped_log,
                "count": 1
            }
        }

        id_1 = log[log.index(":") + 1:]
        id_2 = stripped_log[stripped_log.index(":") + 1:]

        filename = "stripping_{}_to_{}".format(id_1, id_2)

        if os.path.exists(filename):
            addon = 0
            while os.path.exists(filename + "_{}".format(addon)):
                addon += 1
            filename += "_{}".format(addon)


        with open(filename + ".json", "w") as file:
            file.write(json.dumps(json_raw))
            print(Fore.BLUE + "Wrote file {}".format(filename) + Fore.RESET)

        del stripped_log

def genSawPlanking(saws: list, plank_map: dict) -> None:
    """
    Generates recipes for saws with planks
    """
    for stripped_log in plank_map:
        plank = plank_map[stripped_log]

        json_raw = {
            "type": "natural-progression:damage_tools",
            "ingredients": [
                {
                    "item": stripped_log
                },
                [{"item": saw} for saw in saws]
            ],
            "result": {
                "item": plank,
                "count": 4
            }
        }

        id_1 = stripped_log[stripped_log.index(":") + 1:]
        id_2 = plank[plank.index(":") + 1:]

        filename = "sawing_{}_to_{}".format(id_1, id_2)

        if os.path.exists(filename):
            addon = 0
            while os.path.exists(filename + "_{}".format(addon)):
                addon += 1
            filename += "_{}".format(addon)


        with open(filename + ".json", "w") as file:
            file.write(json.dumps(json_raw))
            print(Fore.BLUE + "Wrote file {}".format(filename) + Fore.RESET)

        del plank

def genAxePlanking(axes: list, plank_map:dict) -> None:
    """
    Generates recipes for axes with planks
    """

    for stripped_log in plank_map:
        plank = plank_map[stripped_log]

        json_raw = {
            "type": "natural-progression:damage_tools",
            "ingredients": [
                {
                    "item": stripped_log
                },
                [{"item": axe} for axe in axes]
            ],
            "result": {
                "item": plank,
                "count": 1
            }
        }

        id_1 = stripped_log[stripped_log.index(":") + 1:]
        id_2 = plank[plank.index(":") + 1:]

        filename = "axing_{}_to_{}".format(id_1, id_2)

        if os.path.exists(filename):
            addon = 0
            while os.path.exists(filename + "_{}".format(addon)):
                addon += 1
            filename += "_{}".format(addon)


        with open(filename + ".json", "w") as file:
            file.write(json.dumps(json_raw))
            print(Fore.BLUE + "Wrote file {}".format(filename) + Fore.RESET)

        del plank


def main() -> None:
    """Generates recipes"""
    axes = ["minecraft:stone_axe", "minecraft:iron_axe", "minecraft:diamond_axe", "natural-progression:flint_hatchet"]
    saws = ["natural-progression:basic_saw", "natural-progression:improved_saw"]

    strip_map = {
        "minecraft:oak_log": "minecraft:stripped_oak_log",
        "minecraft:spruce_log": "minecraft:stripped_spruce_log",
        "minecraft:birch_log": "minecraft:stripped_birch_log",
        "minecraft:jungle_log": "minecraft:stripped_jungle_log",
        "minecraft:dark_oak_log": "minecraft:stripped_dark_oak_log",
        "minecraft:acacia_log": "minecraft:stripped_acacia_log",
        "minecraft:oak_wood": "minecraft:stripped_oak_wood",
        "minecraft:spruce_wood": "minecraft:stripped_spruce_wood",
        "minecraft:birch_wood": "minecraft:stripped_birch_wood",
        "minecraft:jungle_wood": "minecraft:stripped_jungle_wood",
        "minecraft:dark_oak_wood": "minecraft:stripped_dark_oak_wood",
        "minecraft:acacia_wood": "minecraft:stripped_acacia_wood",
    }

    plank_map = {
        "minecraft:stripped_oak_log": "minecraft:oak_planks",
        "minecraft:stripped_spruce_log": "minecraft:spruce_planks",
        "minecraft:stripped_birch_log": "minecraft:birch_planks",
        "minecraft:stripped_jungle_log": "minecraft:jungle_planks",
        "minecraft:stripped_dark_oak_log": "minecraft:dark_oak_planks",
        "minecraft:stripped_acacia_log": "minecraft:acacia_planks",
        "minecraft:stripped_oak_wood": "minecraft:oak_planks",
        "minecraft:stripped_spruce_wood": "minecraft:spruce_planks",
        "minecraft:stripped_birch_wood": "minecraft:birch_planks",
        "minecraft:stripped_jungle_wood": "minecraft:jungle_planks",
        "minecraft:stripped_dark_oak_wood": "minecraft:dark_oak_planks",
        "minecraft:stripped_acacia_wood": "minecraft:acacia_planks"
    }

    genAxePlanking(axes, plank_map)
    genSawPlanking(saws, plank_map)
    genSawStripping(saws, strip_map)

    

if __name__ == "__main__":
    main()

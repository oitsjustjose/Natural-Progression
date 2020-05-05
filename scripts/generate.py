#!/usr/bin/env python3

import json
import os
from typing import NamedTuple, Set, Tuple, Callable, List, Optional


class WoodType(NamedTuple):
    log: Optional[str]
    wood: Optional[str]
    planks: str
    stripped_log: str
    stripped_wood: Optional[str]


class WoodSet(NamedTuple):
    namespace: str
    wood_types: Set[WoodType]


class DamageToolsRecipe(NamedTuple):
    namespace: str
    action: str
    original: str
    tool: str
    result: str
    amount: int
    override: bool


def vanilla_named_wood_type(name: str) -> WoodType:
    return WoodType(
        log=name + "_log",
        wood=name + "_wood",
        planks=name + "_planks",
        stripped_log="stripped_" + name + "_log",
        stripped_wood="stripped_" + name + "_wood",
    )


def legacy_wood_type(log: str, planks: str) -> WoodType:
    return WoodType(
        log=None, wood=None, planks=planks, stripped_log=log, stripped_wood=None,
    )


def midnight_named_wood_type(name: str) -> WoodType:
    return WoodType(
        log=name + "_log",
        wood=name + "_wood",
        planks=name + "_planks",
        stripped_log=name + "_stripped_log",
        stripped_wood=name + "_stripped_wood",
    )


def midnight_stem_type(name: str) -> WoodType:
    return legacy_wood_type(log=name + "_stem", planks=name + "_planks")


WOOD_SETS = [
    WoodSet(
        namespace="minecraft",
        wood_types={
            vanilla_named_wood_type("oak"),
            vanilla_named_wood_type("spruce"),
            vanilla_named_wood_type("birch"),
            vanilla_named_wood_type("jungle"),
            vanilla_named_wood_type("dark_oak"),
            vanilla_named_wood_type("acacia"),
        },
    ),
    WoodSet(
        namespace="atmospheric",
        wood_types={
            vanilla_named_wood_type("rosewood")._replace(
                wood="rosewood", stripped_wood="stripped_rosewood"
            ),
            vanilla_named_wood_type("yucca"),
            vanilla_named_wood_type("kousa"),
            vanilla_named_wood_type("aspen"),
        },
    ),
    WoodSet(
        namespace="autumnity",
        wood_types={
            vanilla_named_wood_type("maple"),
            vanilla_named_wood_type("sappy_maple")._replace(
                stripped_log="stripped_maple_log",
                stripped_wood="stripped_maple_wood",
                planks="maple_planks",
            ),
        },
    ),
    WoodSet(namespace="bloomful", wood_types={vanilla_named_wood_type("wisteria")}),
    WoodSet(namespace="swampexpansion", wood_types={vanilla_named_wood_type("willow")}),
    WoodSet(
        namespace="botania",
        wood_types={legacy_wood_type(log="livingwood", planks="livingwood_planks")},
    ),
    WoodSet(
        namespace="midnight",
        wood_types={
            midnight_named_wood_type("shadowroot"),
            midnight_named_wood_type("dark_willow"),
            midnight_named_wood_type("dead_wood")._replace(
                wood="dead_wood", stripped_wood="dead_wood_stripped",
            ),
            midnight_stem_type("bogshroom"),
            midnight_stem_type("nightshroom"),
            midnight_stem_type("dewshroom"),
            midnight_stem_type("viridshroom"),
        },
    ),
    WoodSet(namespace="traverse", wood_types={vanilla_named_wood_type("fir")}),
    WoodSet(
        namespace="upgrade_aquatic",
        wood_types={
            WoodType(
                log="driftwood_log",
                wood="driftwood",
                planks="driftwood_planks",
                stripped_log="driftwood_log_stripped",
                stripped_wood="driftwood_stripped",
            ),
        },
    ),
    WoodSet(
        namespace="endergetic",
        wood_types={
            WoodType(
                log="poise_stem",
                wood="poise_wood",
                planks="poise_planks",
                stripped_log="poise_stem_stripped",
                stripped_wood="poise_wood_stripped",
            ),
            WoodType(
                log="poise_stem_glowing",
                wood="poise_wood_glowing",
                planks="poise_planks",
                stripped_log="poise_stem_stripped",
                stripped_wood="poise_wood_stripped",
            ),
        },
    ),
]


def damage_tools_recipe(
        action: str, namespace: str, original: str, tool: str, result: str, amount: int, override: bool,
) -> Tuple[str, str]:
    if override:
        path = f"data/{namespace}/recipes/{result}.json"
    else:
        path = f"data/natural-progression/recipes/wood/{action}/{namespace}/{action}_{original}_to_{result}.json"
    return (
        path,
        json.dumps(
            {
                "conditions": [{"type": "forge:mod_loaded", "modid": namespace}],
                "type": "natural-progression:damage_tools",
                "ingredients": [
                    {"item": f"{namespace}:{original}"},
                    {"tag": f"natural-progression:{tool}"},
                ],
                "result": {"item": f"{namespace}:{result}", "count": amount},
            },
            indent=4,
        ),
    )


def damage_tools_recipe_set(
        action: str,
        pairs: Callable[[WoodType], List[Tuple[str, str, bool]]],
        tool: str,
        amount: int,
) -> Set[Tuple[str, str]]:
    return {
        damage_tools_recipe(
            action=action,
            namespace=wood_set.namespace,
            original=original,
            tool=tool,
            result=result,
            amount=amount,
            override=override
        )
        for wood_set in WOOD_SETS
        for wood_type in wood_set.wood_types
        for original, result, override in pairs(wood_type)
        if original is not None and result is not None
    }


STRIPPING_RECIPES = damage_tools_recipe_set(
    action="stripping",
    pairs=lambda wood_type: [
        (wood_type.log, wood_type.stripped_log, False),
        (wood_type.wood, wood_type.stripped_wood, False),
    ],
    tool="saw",
    amount=1,
)

AXING_RECIPES = damage_tools_recipe_set(
    action="axing",
    pairs=lambda wood_type: [
        (wood_type.stripped_log, wood_type.planks, False),
        (wood_type.stripped_wood, wood_type.planks, False),
    ],
    tool="axe",
    amount=1,
)

SAWING_RECIPES = damage_tools_recipe_set(
    action="sawing",
    pairs=lambda wood_type: [
        (wood_type.stripped_log, wood_type.planks, True),
        (wood_type.stripped_wood, wood_type.planks, False),
    ],
    tool="saw",
    amount=4,
)


def dump(file_name: str, data: str):
    file_name = f"src/main/resources/{file_name}"
    os.makedirs(os.path.dirname(file_name), exist_ok=True)
    with open(file_name, "w") as fp:
        fp.write(data)


def main():
    for name, data in STRIPPING_RECIPES:
        dump(file_name=name, data=data)
    for name, data in AXING_RECIPES:
        dump(file_name=name, data=data)
    for name, data in SAWING_RECIPES:
        dump(file_name=name, data=data)


if __name__ == '__main__':
    main()

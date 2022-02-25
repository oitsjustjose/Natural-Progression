#!/usr/bin/env python3

from common.mod_stone import ModStones
from common.pebble_assets import PebbleAssetGenerator
from common.pebble_drops import PebbleDropGenerator
from common.pebble_recipes import PebbleRecipeGenerator

create_stones = ModStones(
    "create",
    ["asurine", "crimsite", "limestone", "ochrum", "scorchia", "scoria", "veridium"],
)
create_pag = PebbleAssetGenerator(create_stones)
create_pdg = PebbleDropGenerator(create_stones)
create_prg = PebbleRecipeGenerator(create_stones)

create_prg.generate()
create_pag.generate_blockstates()
create_pag.generate_block_models()
create_pag.generate_item_models()

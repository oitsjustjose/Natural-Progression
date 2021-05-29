#!/usr/bin/env python3

from common.mod_stone import ModStones
from common.pebble_assets import PebbleAssetGenerator
from common.pebble_drops import PebbleDropGenerator
from common.pebble_recipes import PebbleRecipeGenerator

quark_stones = ModStones(
    "quark", ["marble", "limestone", "jasper", "slate", "voidstone"]
)
quark_pag = PebbleAssetGenerator(quark_stones)
quark_pdg = PebbleDropGenerator(quark_stones)

# quark_pdg.generate()
# quark_pag.generate_blockstates()
# quark_pag.generate_block_models()
# quark_pag.generate_item_models()

create_stones = ModStones(
    "create",
    ["limestone", "weathered_limestone", "dolomite", "gabbro", "scoria", "dark_scoria"],
)
create_pag = PebbleAssetGenerator(create_stones)
create_pdg = PebbleDropGenerator(create_stones)
create_prg = PebbleRecipeGenerator(create_stones)

# create_pdg.generate()
create_prg.generate()
# create_pag.generate_blockstates()
# create_pag.generate_block_models()
# create_pag.generate_item_models()

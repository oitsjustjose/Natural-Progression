import json
import os
from .mod_stone import ModStones

BLOCK_DROPS_ROOT = "./out/data/natural-progression/loot_tables/blocks"


class PebbleDropGenerator:
    def __init__(self, m: ModStones):
        self._stones = m
        self._modid = m.get_modid()
        self._prep_out()

    def _prep_out(self) -> None:
        if not os.path.exists("./out"):
            os.mkdir("./out")
        if not os.path.exists("./out/data"):
            os.mkdir("./out/data")
        if not os.path.exists("./out/data/natural-progression"):
            os.mkdir("./out/data/natural-progression")
        if not os.path.exists("./out/data/natural-progression/loot_tables"):
            os.mkdir("./out/data/natural-progression/loot_tables")
        if not os.path.exists("./out/data/natural-progression/loot_tables/blocks"):
            os.mkdir("./out/data/natural-progression/loot_tables/blocks")

    def generate(self) -> None:
        lt = None
        with open("./pebble_loot_table.json", "r") as file:
            lt = json.loads(file.read())

        for stone in self._stones._stones:
            fn = f"{self._modid}_{stone}_pebble.json"
            with open(f"{BLOCK_DROPS_ROOT}/{fn}", "w+") as output:
                lt["pools"][0]["entries"][0][
                    "name"
                ] = f"natural-progression:{self._modid}_{stone}_pebble"
                output.write(json.dumps(lt))

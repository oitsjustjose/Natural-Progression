import json
import os
from .mod_stone import ModStones

RECIPES_ROOT = "./out/data/natural-progression/recipes/crafting"

class PebbleRecipeGenerator:
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
        if not os.path.exists("./out/data/natural-progression/recipes"):
            os.mkdir("./out/data/natural-progression/recipes")
        if not os.path.exists("./out/data/natural-progression/recipes/crafting"):
            os.mkdir("./out/data/natural-progression/recipes/crafting")

    def generate(self) -> None:
        rec = None
        with open("./pebble_cobble_rec.json", "r") as file:
            rec = json.loads(file.read())
        
        for stone in self._stones._stones:
            fn = f"{self._modid}_{stone}_pebble_to_cobble.json"
            with open(f"{RECIPES_ROOT}/{fn}", "w+") as output:
                rec["key"]["P"]["item"] = f"natural-progression:{self._modid}_{stone}_pebble"
                rec["result"]["item"] = f"{self._modid}:{stone}_cobblestone"
                output.write(json.dumps(rec))
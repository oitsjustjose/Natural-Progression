import json
import os

from .mod_stone import ModStones

BLOCK_STATES_ROOT = "./out/assets/natprog/blockstates"
BLOCK_MODELS_ROOT = "./out/assets/natprog/models/block"
ITEM_MODELS_ROOT = "./out/assets/natprog/models/item"


class PebbleAssetGenerator:
    def __init__(self, m: ModStones):
        self._stones = m
        self._modid = m.get_modid()
        self._prep_out()

    def _prep_out(self) -> None:
        if not os.path.exists("./out"):
            os.mkdir("./out")
        if not os.path.exists("./out/assets"):
            os.mkdir("./out/assets")
        if not os.path.exists("./out/assets/natprog"):
            os.mkdir("./out/assets/natprog")
        if not os.path.exists("./out/assets/natprog/blockstates"):
            os.mkdir("./out/assets/natprog/blockstates")
        if not os.path.exists("./out/assets/natprog/models"):
            os.mkdir("./out/assets/natprog/models")
        if not os.path.exists("./out/assets/natprog/models/block"):
            os.mkdir("./out/assets/natprog/models/block")
        if not os.path.exists("./out/assets/natprog/models/item"):
            os.mkdir("./out/assets/natprog/models/item")

    def generate_blockstates(self) -> None:
        bst = None
        with open("./pebble_blockstate.json", "r") as file:
            bst = json.loads(file.read())

        for stone in self._stones._stones:
            fn = f"{self._modid}_{stone}_pebble.json"
            with open(f"{BLOCK_STATES_ROOT}/{fn}", "w+") as output:
                bst["variants"][""][
                    "model"
                ] = f"natprog:block/{self._modid}_{stone}_pebble"
                output.write(json.dumps(bst))

    def generate_block_models(self) -> None:
        model = None
        with open("./pebble_block_model.json", "r") as file:
            model = json.loads(file.read())
        for stone in self._stones._stones:
            fn = f"{self._modid}_{stone}_pebble.json"
            with open(f"{BLOCK_MODELS_ROOT}/{fn}", "w") as output:
                model["textures"]["1161658297"] = f"{self._modid}:block/{stone}"
                model["textures"]["particle"] = f"{self._modid}:block/{stone}"
                output.write(json.dumps(model))

    def generate_item_models(self) -> None:
        model = None
        with open("./pebble_item_model.json", "r") as file:
            model = json.loads(file.read())
        for stone in self._stones._stones:
            fn = f"{self._modid}_{stone}_pebble.json"
            with open(f"{ITEM_MODELS_ROOT}/{fn}", "w") as output:
                model["parent"] = f"natprog:block/{self._modid}_{stone}_pebble"
                output.write(json.dumps(model))

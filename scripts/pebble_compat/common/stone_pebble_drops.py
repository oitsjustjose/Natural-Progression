"""
This file, unlike pebble_drops, makese it
so that supported mods' stones drop pebbles.
"""

import json
import os
from .mod_stone import ModStones

class StonePebbleDropGenerator:
    def __init__(self, m: ModStones, drops_path: str):
        self._stones = m
        self._modid = m.get_modid()
        self._root = drops_path
        self._prep_out()

    def _prep_out(self):
        
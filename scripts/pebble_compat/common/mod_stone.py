#!/usr/bin/env python3
from typing import List


class ModStones:
    def __init__(self, modid, stones, tex_path_has_s=False):
        self._modid = modid
        self._stones = stones
        self._path_has_s = tex_path_has_s

    def get_stones(self) -> List[str]:
        return list(map(lambda x: f"{self._modid}:{x}", self._stones))

    def get_modid(self) -> str:
        return self._modid

    def does_tex_path_have_s(self) -> bool:
        return self._path_has_s

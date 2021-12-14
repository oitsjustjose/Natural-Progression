#!/usr/bin/env python3
import itertools
from typing import NamedTuple, Optional, List

from .common import DataJsonFile


class WoodProgression(NamedTuple):
    tool_tag: str
    original_item: str
    result_item: str
    result_count: int
    should_override: bool = False

    @property
    def path(self) -> str:
        base = "src/main/resources"
        result_ns = self.result_item.split(":")[0]
        result_name = self.result_item.split(":")[1]
        if self.should_override:
            return f"{base}/data/{result_ns}/recipes/{result_name}.json"
        else:
            original_name = self.original_item.split(":")[1]
            tool_name = self.tool_tag.split(":")[1]
            base += "/data/natprog/recipes/wood"
            return (
                f"{base}/{result_ns}/{tool_name}/{original_name}_to_{result_name}.json"
            )

    @property
    def recipe(self) -> DataJsonFile:
        return DataJsonFile(
            path=self.path,
            data={
                "conditions": [
                    {
                        "type": "forge:mod_loaded",
                        "modid": self.result_item.split(":")[0],
                    }
                ],
                "type": "natprog:damage_tools",
                "ingredients": [{"item": self.original_item}, {"tag": self.tool_tag}],
                "result": {"item": self.result_item, "count": self.result_count},
            },
        )


class WoodType(NamedTuple):
    planks: str
    log: str
    wood: Optional[str] = None
    stripped_log: Optional[str] = None
    stripped_wood: Optional[str] = None
    never_override: bool = False

    def _get_stripping_progressions(self) -> List[WoodProgression]:
        ret = []
        if self.stripped_log is not None:
            ret.append(
                WoodProgression(
                    tool_tag="natprog:saw",
                    original_item=self.log,
                    result_count=1,
                    result_item=self.stripped_log,
                )
            )
        if self.wood is not None and self.stripped_wood is not None:
            ret.append(
                WoodProgression(
                    tool_tag="natprog:saw",
                    original_item=self.wood,
                    result_count=1,
                    result_item=self.stripped_wood,
                )
            )
        return ret

    def _get_chopping_progressions(
        self, count: int, tool: str
    ) -> List[WoodProgression]:
        ret = []
        # only allow unstripped log if stripped log doesn't exist
        if self.stripped_log is not None:
            ret.append(
                WoodProgression(
                    tool_tag=f"natprog:{tool}",
                    original_item=self.stripped_log,
                    result_count=count,
                    result_item=self.planks,
                    should_override=tool == "saw" and not self.never_override,
                )
            )
        else:
            ret.append(
                WoodProgression(
                    tool_tag=f"natprog:{tool}",
                    original_item=self.log,
                    result_count=count,
                    result_item=self.planks,
                    should_override=tool == "saw" and not self.never_override,
                )
            )

        # only allow unstripped wood if stripped wood doesn't exist
        if self.stripped_wood is not None:
            ret.append(
                WoodProgression(
                    tool_tag=f"natprog:{tool}",
                    original_item=self.stripped_wood,
                    result_count=count,
                    result_item=self.planks,
                )
            )
        elif self.wood is not None:
            ret.append(
                WoodProgression(
                    tool_tag=f"natprog:{tool}",
                    original_item=self.wood,
                    result_count=count,
                    result_item=self.planks,
                )
            )
        return ret

    @property
    def progressions(self) -> List[WoodProgression]:
        return list(
            itertools.chain(
                self._get_stripping_progressions(),
                self._get_chopping_progressions(count=1, tool="axe"),
                self._get_chopping_progressions(count=4, tool="saw"),
            )
        )

    @staticmethod
    def default_wood(namespace: str, wood_name: str):
        """Vanilla-style wood block names"""
        return WoodType(
            planks=f"{namespace}:{wood_name}_planks",
            log=f"{namespace}:{wood_name}_log",
            wood=f"{namespace}:{wood_name}_wood",
            stripped_log=f"{namespace}:stripped_{wood_name}_log",
            stripped_wood=f"{namespace}:stripped_{wood_name}_wood",
        )

    @staticmethod
    def default_stem(namespace: str, wood_name: str):
        """Vanilla-style shroom-wood block names (new in 1.16)"""
        return WoodType(
            planks=f"{namespace}:{wood_name}_planks",
            log=f"{namespace}:{wood_name}_stem",
            wood=f"{namespace}:{wood_name}_hyphae",
            stripped_log=f"{namespace}:stripped_{wood_name}_stem",
            stripped_wood=f"{namespace}:stripped_{wood_name}_hyphae",
        )

    @staticmethod
    def legacy_wood(namespace: str, wood_name: str):
        """Vanilla-style wood block names without stripping or wood"""
        return WoodType(
            planks=f"{namespace}:{wood_name}_planks",
            log=f"{namespace}:{wood_name}_log",
        )

    @staticmethod
    def legacy_stem(namespace: str, wood_name: str):
        """Vanilla-style shroom-wood block names without stripping or hyphae (for The Midnight)"""
        return WoodType(
            planks=f"{namespace}:{wood_name}_planks",
            log=f"{namespace}:{wood_name}_stem",
        )

    @staticmethod
    def suffix_wood(namespace: str, wood_name: str):
        """Weird naming scheme used by The Midnight"""
        return WoodType(
            planks=f"{namespace}:{wood_name}_planks",
            log=f"{namespace}:{wood_name}_log",
            wood=f"{namespace}:{wood_name}_wood",
            stripped_log=f"{namespace}:{wood_name}_stripped_log",
            stripped_wood=f"{namespace}:{wood_name}_stripped_wood",
        )

    @staticmethod
    def quite_weird(
        namespace: str,
        planks: str,
        log: str,
        wood: Optional[str],
        stripped_log: Optional[str],
        stripped_wood: Optional[str],
    ):
        return WoodType(
            planks=f"{namespace}:{planks}",
            log=f"{namespace}:{log}",
            wood=f"{namespace}:{wood}",
            stripped_log=f"{namespace}:{stripped_log}",
            stripped_wood=f"{namespace}:{stripped_wood}",
        )

    def replace(self, **kwargs):
        return self._replace(**kwargs)

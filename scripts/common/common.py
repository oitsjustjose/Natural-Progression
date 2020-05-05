#!/usr/bin/env python3
import json
import os
from typing import Any, Optional, List, TypedDict, NamedTuple


def dump_json(path: str, data: Any):
    with open(path, "w") as outfile:
        json.dump(data, outfile, indent=4)


class DataJsonFile(NamedTuple):
    path: str
    data: Any

    def dump(self):
        os.makedirs(os.path.dirname(self.path), exist_ok=True)
        with open(self.path, "w") as outfile:
            json.dump(self.data, outfile, indent=4)

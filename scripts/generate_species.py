#!/usr/bin/env python3
"""Create EN -> KO species dictionary from Cobblemon language resources.
No special Python dependencies. Fails the build instead of silently shipping an empty dictionary.
"""
import json
import re
import sys
from pathlib import Path

english=json.loads(Path(sys.argv[1]).read_text(encoding="utf-8"))
korean=json.loads(Path(sys.argv[2]).read_text(encoding="utf-8"))
pattern=re.compile(r"^cobblemon\.species\..+\.name$")
result={}
for key,en in english.items():
    if not pattern.match(key): continue
    ko=korean.get(key)
    if not isinstance(en,str) or not isinstance(ko,str): continue
    if not en.strip() or not ko.strip() or en.strip()==ko.strip(): continue
    if "\t" in en+ko or "\n" in en+ko: continue
    result[en.strip()]=ko.strip()
# Add aliases only if official species string is not already provided.
# Do not translate random unknown names or the player nickname.
if len(result) < 900:
    raise SystemExit(f"Only {len(result)} names found. Source structure may have changed; stop build.")
out=Path(sys.argv[3]); out.parent.mkdir(parents=True,exist_ok=True)
out.write_text("".join(f"{en}\t{ko}\n" for en,ko in sorted(result.items(),key=lambda x:x[0].casefold())),encoding="utf-8")
print(f"Generated {len(result)} EN -> KO species mappings")
print('Examples:', {s:result.get(s) for s in ('Charmander','Torchic','Staravia','Charizard','Pikachu')})

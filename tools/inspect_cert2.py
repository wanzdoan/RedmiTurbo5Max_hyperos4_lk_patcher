import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent))
import lk_image as lk

dash_data = open('lk_hyperos3_patched_unlockBLandMore/dash.img', 'rb').read()
h4_data = open('lk_hyperos4_original/lk.img', 'rb').read()
h3_data = open('lk_hyperos3_original/lk.img', 'rb').read()

dash_c2 = {img.name: dash_data[img.payload_offset:img.payload_end] for img in lk.parse_container(dash_data)}['cert2']
h4_c2 = {img.name: h4_data[img.payload_offset:img.payload_end] for img in lk.parse_container(h4_data)}['cert2']
h3_c2 = {img.name: h3_data[img.payload_offset:img.payload_end] for img in lk.parse_container(h3_data)}['cert2']

print("H3 orig cert2 (982 bytes) head:", h3_c2[:32].hex())
print("H4 orig cert2 (982 bytes) head:", h4_c2[:32].hex())
print("Dash    cert2 (1162 bytes) head:", dash_c2[:32].hex())

print("\nDash cert2 printable strings:")
import re
for m in re.finditer(rb'[A-Za-z0-9_./@ -]{4,}', dash_c2):
    print(" ", m.group(0).decode())

print("\nH4 orig cert2 printable strings:")
for m in re.finditer(rb'[A-Za-z0-9_./@ -]{4,}', h4_c2):
    print(" ", m.group(0).decode())

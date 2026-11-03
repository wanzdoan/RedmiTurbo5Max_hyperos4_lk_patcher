#!/usr/bin/env python3
"""
MTK lk.img container tool.

MediaTek bootloader images (lk.img / dash.img) are a flat sequence of
sub-images. Each sub-image is:

    offset 0x000 : u32  magic       = 0x58881688
    offset 0x004 : u32  payload_len
    offset 0x008 : char name[12]    (NUL padded, e.g. "lk", "cert1")
    offset 0x014 : ...              (header padding, 0xFF filled)
    offset 0x200 : payload          (payload_len bytes)
    next header  : align_up(0x200 + payload_len, 0x10)

The first sub-image is normally "lk"; cert1/cert2 are the signature
chain for the preceding payload, bl2_ext/aee/dtb/dtbo follow.

Commands:
    list     <image>                          show sub-images
    extract  <image> <name> <output>          extract one payload
    extract-all <image> <outdir>              extract every payload + manifest.json
    patch    <image> <name> <output> <spec>   patch payload bytes, keep container
    hash     <image>                          sha256 of file and payloads

Patch spec is a comma separated list of OFFSET=HEXBYTES, where OFFSET is
relative to the start of the payload, e.g.:

    patch lk.img lk out.img 0x1234=1F2003D5,0x2000=00000000

The payload size is never changed, so the container headers stay valid.
Signature blocks (cert1/cert2) are NOT recomputed; on a locked device the
preloader will reject a modified image.
"""

from __future__ import annotations

import argparse
import hashlib
import json
import struct
import sys
from dataclasses import dataclass, asdict
from pathlib import Path

MAGIC = 0x58881688
HEADER_SIZE = 0x200
ALIGN = 0x10


@dataclass
class SubImage:
    index: int
    name: str
    header_offset: int
    payload_offset: int
    payload_size: int
    total_size: int
    sha256: str

    @property
    def payload_end(self) -> int:
        return self.payload_offset + self.payload_size


def align_up(value: int, alignment: int = ALIGN) -> int:
    return (value + alignment - 1) & ~(alignment - 1)


def parse_container(data: bytes) -> list[SubImage]:
    images: list[SubImage] = []
    off = 0
    index = 0
    while off + HEADER_SIZE <= len(data):
        magic, size = struct.unpack_from("<II", data, off)
        if magic != MAGIC:
            break
        name = data[off + 8:off + 20].split(b"\x00", 1)[0].decode("ascii", "replace")
        payload_off = off + HEADER_SIZE
        payload_end = payload_off + size
        if payload_end > len(data):
            raise ValueError(
                f"sub-image {index} ({name}) at {off:#x} claims {size:#x} bytes, "
                f"past end of file ({len(data):#x})"
            )
        payload = data[payload_off:payload_end]
        images.append(
            SubImage(
                index=index,
                name=name,
                header_offset=off,
                payload_offset=payload_off,
                payload_size=size,
                total_size=HEADER_SIZE + size,
                sha256=hashlib.sha256(payload).hexdigest(),
            )
        )
        index += 1
        off = align_up(payload_end)

    if not images:
        raise ValueError("no MTK sub-images found (magic 0x58881688)")

    if off < len(data):
        tail = data[off:]
        if any(tail):
            print(
                f"warning: {len(tail)} trailing non-zero bytes at {off:#x}",
                file=sys.stderr,
            )
    return images


def read_image(path: Path) -> bytes:
    return path.read_bytes()


def find_image(images: list[SubImage], name: str) -> SubImage:
    matches = [i for i in images if i.name == name]
    if not matches:
        known = ", ".join(sorted({i.name for i in images}))
        raise SystemExit(f"sub-image {name!r} not found (available: {known})")
    return matches[0]


def cmd_list(args: argparse.Namespace) -> int:
    path = Path(args.image)
    data = read_image(path)
    images = parse_container(data)
    print(f"{path}  ({len(data):,} bytes, sha256 {hashlib.sha256(data).hexdigest()})")
    print(f"{'#':>2}  {'name':<12} {'header':>10} {'payload':>10} {'size':>10}  sha256")
    for img in images:
        print(
            f"{img.index:>2}  {img.name:<12} {img.header_offset:#10x} "
            f"{img.payload_offset:#10x} {img.payload_size:#10x}  {img.sha256[:16]}"
        )
    return 0


def cmd_extract(args: argparse.Namespace) -> int:
    path = Path(args.image)
    data = read_image(path)
    img = find_image(parse_container(data), args.name)
    out = Path(args.output)
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_bytes(data[img.payload_offset:img.payload_end])
    print(
        f"{img.name}: {img.payload_size:,} bytes -> {out} "
        f"(file offset {img.payload_offset:#x})"
    )
    return 0


def cmd_extract_all(args: argparse.Namespace) -> int:
    path = Path(args.image)
    data = read_image(path)
    images = parse_container(data)
    outdir = Path(args.outdir)
    outdir.mkdir(parents=True, exist_ok=True)

    manifest = {
        "source": str(path.resolve()),
        "source_size": len(data),
        "source_sha256": hashlib.sha256(data).hexdigest(),
        "sub_images": [],
    }
    for img in images:
        out = outdir / f"{img.index:02d}_{img.name}.bin"
        out.write_bytes(data[img.payload_offset:img.payload_end])
        entry = asdict(img)
        entry["extracted_to"] = str(out)
        manifest["sub_images"].append(entry)
        print(f"{img.name:<12} {img.payload_size:>10,} bytes -> {out}")

    manifest_path = outdir / "manifest.json"
    manifest_path.write_text(json.dumps(manifest, indent=2), encoding="utf-8")
    print(f"manifest -> {manifest_path}")
    return 0


def parse_patch_spec(spec: str) -> list[tuple[int, bytes]]:
    patches: list[tuple[int, bytes]] = []
    for item in spec.split(","):
        item = item.strip()
        if not item:
            continue
        if "=" not in item:
            raise SystemExit(f"bad patch item {item!r}, expected OFFSET=HEX")
        off_s, hex_s = item.split("=", 1)
        off = int(off_s, 0)
        raw = bytes.fromhex(hex_s.replace(" ", ""))
        if not raw:
            raise SystemExit(f"empty patch at {off_s}")
        patches.append((off, raw))
    if not patches:
        raise SystemExit("empty patch spec")
    return patches


def cmd_patch(args: argparse.Namespace) -> int:
    path = Path(args.image)
    data = bytearray(read_image(path))
    img = find_image(parse_container(bytes(data)), args.name)
    patches = parse_patch_spec(args.spec)

    for off, raw in patches:
        if off < 0 or off + len(raw) > img.payload_size:
            raise SystemExit(
                f"patch {off:#x}+{len(raw)} outside {img.name} payload "
                f"(size {img.payload_size:#x})"
            )
        start = img.payload_offset + off
        old = bytes(data[start:start + len(raw)])
        data[start:start + len(raw)] = raw
        print(f"{off:#08x}: {old.hex()} -> {raw.hex()}")

    out = Path(args.output)
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_bytes(bytes(data))
    print(f"wrote {out} ({len(data):,} bytes)")
    return 0


def cmd_hash(args: argparse.Namespace) -> int:
    path = Path(args.image)
    data = read_image(path)
    print(f"file    {hashlib.sha256(data).hexdigest()}  {path}")
    for img in parse_container(data):
        print(f"{img.name:<12} {img.sha256}  ({img.payload_size:#x} bytes)")
    return 0


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(
        description="Inspect/extract/patch MediaTek lk.img containers"
    )
    sub = parser.add_subparsers(dest="command", required=True)

    p = sub.add_parser("list", help="list sub-images")
    p.add_argument("image")
    p.set_defaults(func=cmd_list)

    p = sub.add_parser("extract", help="extract one sub-image payload")
    p.add_argument("image")
    p.add_argument("name")
    p.add_argument("output")
    p.set_defaults(func=cmd_extract)

    p = sub.add_parser("extract-all", help="extract all payloads + manifest")
    p.add_argument("image")
    p.add_argument("outdir")
    p.set_defaults(func=cmd_extract_all)

    p = sub.add_parser("patch", help="patch payload bytes in place")
    p.add_argument("image")
    p.add_argument("name")
    p.add_argument("output")
    p.add_argument("spec", help="OFFSET=HEX[,OFFSET=HEX...]")
    p.set_defaults(func=cmd_patch)

    p = sub.add_parser("hash", help="hash file and payloads")
    p.add_argument("image")
    p.set_defaults(func=cmd_hash)

    return parser


def main(argv: list[str] | None = None) -> int:
    args = build_parser().parse_args(argv)
    try:
        return args.func(args)
    except ValueError as exc:
        raise SystemExit(f"error: {exc}") from exc


if __name__ == "__main__":
    raise SystemExit(main())

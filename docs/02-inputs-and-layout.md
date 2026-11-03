# 02 — Input và layout

## Ba LK input

| File | Byte | SHA256 |
|---|---:|---|
| `lk_hyperos3_original/lk.img` | 4082944 | `e23e07cab7754cc99c9484765b9ec758b2c8d5110dfb3e0eb6009415719bf4cb` |
| `lk_hyperos3_patched_unlockBL/dash.img` | 4082928 | `e164fb22511d93584afab2183c275374d7ab892dd22e3a2132c4f3b1e245ac18` |
| `lk_hyperos4_original/lk.img` | 4087536 | `82b4e1f2547b501f1e61987b9251337318a72a7c091e8965c28b664b5efac95b` |

## Quy ước

Whole-file offset tính từ byte 0 toàn lk.img. Payload offset tính từ đầu payload của một member, không gồm header 512 byte. VA là địa chỉ liên kết dùng khi phân tích. Không thay thế ba loại địa chỉ này cho nhau.

Magic record là u32 LE `0x58881688` (`88 16 88 58`). Word +4 là declared payload size, tên ở +8, header 512 byte. End là exclusive và gồm padding căn 16 byte.

```text
payload_start = record_offset + 0x200
record_end = align16(payload_start + declared_size)
```

## Layout hybrid đã bàn giao

| # | Member | Header offset | Payload start | Size | End | Nguồn |
|---:|---|---:|---:|---:|---:|---|
| 0 | `lk` | `0x0` | `0x200` | 1284936 | `0x139d50` | H4 patch 16 byte |
| 1 | `cert1` | `0x139d50` | `0x139f50` | 1733 | `0x13a620` | H4 nguyên vẹn |
| 2 | `cert2` | `0x13a620` | `0x13a820` | 1162 | `0x13acb0` | H4 DER + prefix |
| 3 | `bl2_ext` | `0x13acb0` | `0x13aeb0` | 1079256 | `0x242690` | H3 nguyên vẹn |
| 4 | `cert1` | `0x242690` | `0x242890` | 1733 | `0x242f60` | H3 nguyên vẹn |
| 5 | `cert2` | `0x242f60` | `0x243160` | 982 | `0x243540` | H3 nguyên vẹn |
| 6 | `aee` | `0x243540` | `0x243740` | 1005488 | `0x338ef0` | H4 nguyên vẹn |
| 7 | `cert1` | `0x338ef0` | `0x3390f0` | 1733 | `0x3397c0` | H4 nguyên vẹn |
| 8 | `cert2` | `0x3397c0` | `0x3399c0` | 982 | `0x339da0` | H4 nguyên vẹn |
| 9 | `lk_main_dtb` | `0x339da0` | `0x339fa0` | 536097 | `0x3bcdd0` | H4 nguyên vẹn |
| 10 | `cert1` | `0x3bcdd0` | `0x3bcfd0` | 1733 | `0x3bd6a0` | H4 nguyên vẹn |
| 11 | `cert2` | `0x3bd6a0` | `0x3bd8a0` | 982 | `0x3bdc80` | H4 nguyên vẹn |
| 12 | `lk_dtbo` | `0x3bdc80` | `0x3bde80` | 156075 | `0x3e4030` | H4 nguyên vẹn |
| 13 | `cert1` | `0x3e4030` | `0x3e4230` | 1733 | `0x3e4900` | H4 nguyên vẹn |
| 14 | `cert2` | `0x3e4900` | `0x3e4b00` | 982 | `0x3e4ee0` | H4 nguyên vẹn |

## Mapping native

| Payload | Linked base | Ghi chú |
|---|---|---|
| Main H4 LK | `0xffff000090700000` | VA = base + whole-file offset - 0x200 |
| BL2 H3/H4 | `0xffff0000b8000000` | Các offset BL2 trong docs thường thuộc payload |
| AEE H4 | `0xffff0000be000000` | Suy ra từ startup literal pool |

ELF carrier đặt memory extent cho Ghidra, không phải linker sections gốc và không phải firmware để nạp. Mapping preloader mới bổ sung trong review riêng không tự thay đổi tính chất mô phỏng của probe cũ.

## Nguồn thành phần

BL2 H3 lấy từ H3 original, cả header/payload/padding và cert1/cert2. Main H4 chỉ sửa 16 byte; main cert2 thêm prefix. Main cert1 và toàn bộ triple AEE/main DTB/DTBO giữ H4. Donor H3 patched khác build, AEE và DTBO có thay đổi khác; không bê nguyên diff donor sang H4.

Xem [container_inventory.json](../fresh_analysis/container_chain/container_inventory.json) và [build manifest](../fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.json) cho danh tính chi tiết.

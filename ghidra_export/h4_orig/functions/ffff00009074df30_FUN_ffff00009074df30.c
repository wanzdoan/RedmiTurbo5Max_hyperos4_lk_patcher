// ffff00009074df30  size=452  FUN_ffff00009074df30

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009074df30(void)

{
  uint uVar1;
  uint uVar2;
  
  if ((DAT_ffff000090879400 & 1) == 0) {
    uVar1 = thunk_FUN_ffff00009074b22c(0x69);
    uVar2 = thunk_FUN_ffff00009074b22c(0x6a);
    thunk_FUN_ffff00009074b22c(0x6b);
    thunk_FUN_ffff00009074b22c(0x6c);
    thunk_FUN_ffff00009074b22c(0x6d);
    thunk_FUN_ffff00009074b22c(0x8d);
    if (uVar1 != 0 || uVar2 != 0) {
      _DAT_ffff0000130b0100 =
           uVar1 & 1 | (uVar1 >> 1 & 1) << 8 | (uVar1 >> 2 & 1) << 0x10 | (uVar1 >> 3 & 1) << 0x18;
      _DAT_ffff0000130b0200 =
           uVar1 >> 1 & 0x100 | uVar1 >> 8 & 1 | (uVar1 >> 10 & 1) << 0x10 |
           (uVar1 >> 0xb & 1) << 0x18;
      _DAT_ffff0000130b01c4 = (uVar1 & 0x20) << 3 | uVar1 >> 4 & 1 | (uVar1 >> 6 & 1) << 0x10;
      _DAT_ffff0000130b02c4 = uVar1 >> 5 & 0x100 | uVar1 >> 0xc & 1 | (uVar1 >> 0xe & 1) << 0x10;
      _DAT_ffff0000130b0300 =
           uVar1 >> 9 & 0x100 | uVar1 >> 0x10 & 1 | uVar1 >> 2 & 0x10000 |
           (uVar1 >> 0x13 & 1) << 0x18;
      _DAT_ffff0000130b03c4 = uVar1 >> 0xd & 0x100 | uVar1 >> 0x14 & 1 | uVar1 >> 6 & 0x10000;
      _DAT_ffff0000130b0400 =
           uVar1 >> 0x11 & 0x100 | uVar1 >> 0x18 & 1 |
           uVar1 >> 10 & 0x10000 | uVar1 >> 3 & 0x1000000;
      _DAT_ffff0000130b04c4 = uVar1 >> 0x15 & 0x100 | uVar1 >> 0x1c & 1 | uVar1 >> 0xe & 0x10000;
      _DAT_ffff0000130b0500 =
           uVar2 & 1 | (uVar2 >> 1 & 1) << 8 | (uVar2 >> 2 & 1) << 0x10 | (uVar2 >> 3 & 1) << 0x18;
      _DAT_ffff0000130b05c4 = (uVar2 & 0x20) << 3 | uVar2 >> 4 & 1 | (uVar2 >> 6 & 1) << 0x10;
      DAT_ffff000090879400 = 1;
      _DAT_ffff0000130b0108 = _DAT_ffff0000130b0100;
      _DAT_ffff0000130b01c8 = _DAT_ffff0000130b01c4;
      _DAT_ffff0000130b0208 = _DAT_ffff0000130b0200;
      _DAT_ffff0000130b02c8 = _DAT_ffff0000130b02c4;
      _DAT_ffff0000130b0308 = _DAT_ffff0000130b0300;
      _DAT_ffff0000130b03c8 = _DAT_ffff0000130b03c4;
      _DAT_ffff0000130b0408 = _DAT_ffff0000130b0400;
      _DAT_ffff0000130b04c8 = _DAT_ffff0000130b04c4;
      _DAT_ffff0000130b0508 = _DAT_ffff0000130b0500;
      _DAT_ffff0000130b05c8 = _DAT_ffff0000130b05c4;
    }
  }
  return;
}



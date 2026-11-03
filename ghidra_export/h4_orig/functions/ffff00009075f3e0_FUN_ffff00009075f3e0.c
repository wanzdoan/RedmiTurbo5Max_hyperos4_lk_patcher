// ffff00009075f3e0  size=304  FUN_ffff00009075f3e0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075f3e0(void)

{
  uint uVar1;
  
  _DAT_ffff00009087bf98 = 3;
  DAT_ffff00009087bf9c = 1;
  _DAT_ffff00009087bfa0 = 0;
  uVar1 = _DAT_ffff00001c004e58 & 0x40000000;
  _DAT_ffff00001c004e58 = _DAT_ffff00001c004e58 | 4;
  while (uVar1 == 0) {
    FUN_ffff00009075ee68();
    uVar1 = _DAT_ffff00001c004e58 >> 0x1e & 1;
  }
  _DAT_ffff00009087bfa0 = _DAT_ffff00009087bfa0 + 1;
  _DAT_ffff00009087bfa4 = 0;
  FUN_ffff00009077cea0(0x32);
  _DAT_ffff00001c004e58 = _DAT_ffff00001c004e58 | 8;
  while (-1 < (int)_DAT_ffff00001c004e58) {
    FUN_ffff00009075ee68();
  }
  _DAT_ffff00009087bfa4 = 0;
  _DAT_ffff00009087bfa0 = _DAT_ffff00009087bfa0 + 1;
  uVar1 = _DAT_ffff00001c004e58 & 0xffffffed | 1;
  if ((_DAT_ffff00001c004e58 & 0x8000000) != 0) {
    uVar1 = _DAT_ffff00001c004e58 & 0xf9ffffed | 0x2000001;
  }
  _DAT_ffff00001c004e58 = uVar1;
  _DAT_ffff00001c00d0e0 = 0x400000;
  return;
}



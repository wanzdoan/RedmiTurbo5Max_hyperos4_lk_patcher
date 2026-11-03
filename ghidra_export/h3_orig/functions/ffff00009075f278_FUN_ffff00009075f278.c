// ffff00009075f278  size=304  FUN_ffff00009075f278

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075f278(void)

{
  uint uVar1;
  
  _DAT_ffff00009087bf00 = 4;
  DAT_ffff00009087bf04 = 1;
  _DAT_ffff00009087bf08 = 0;
  uVar1 = _DAT_ffff00001c004e5c & 0x40000000;
  _DAT_ffff00001c004e5c = _DAT_ffff00001c004e5c | 4;
  while (uVar1 == 0) {
    FUN_ffff00009075ebd0();
    uVar1 = _DAT_ffff00001c004e5c >> 0x1e & 1;
  }
  _DAT_ffff00009087bf08 = _DAT_ffff00009087bf08 + 1;
  _DAT_ffff00009087bf0c = 0;
  FUN_ffff00009077cbd8(0x32);
  _DAT_ffff00001c004e5c = _DAT_ffff00001c004e5c | 8;
  while (-1 < (int)_DAT_ffff00001c004e5c) {
    FUN_ffff00009075ebd0();
  }
  _DAT_ffff00009087bf0c = 0;
  _DAT_ffff00009087bf08 = _DAT_ffff00009087bf08 + 1;
  uVar1 = _DAT_ffff00001c004e5c & 0xffffffed | 1;
  if ((_DAT_ffff00001c004e5c & 0x8000000) != 0) {
    uVar1 = _DAT_ffff00001c004e5c & 0xf9ffffed | 0x2000001;
  }
  _DAT_ffff00001c004e5c = uVar1;
  _DAT_ffff00001c00d0e0 = 0x800000;
  return;
}



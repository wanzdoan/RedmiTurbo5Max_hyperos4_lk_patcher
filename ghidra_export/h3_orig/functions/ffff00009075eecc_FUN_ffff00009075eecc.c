// ffff00009075eecc  size=316  FUN_ffff00009075eecc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075eecc(void)

{
  uint uVar1;
  
  _DAT_ffff00009087bf00 = 1;
  DAT_ffff00009087bf04 = 1;
  _DAT_ffff00009087bf08 = 0;
  uVar1 = _DAT_ffff00001c004e54 & 0x40000000;
  _DAT_ffff00001c004e54 = _DAT_ffff00001c004e54 | 4;
  while (uVar1 == 0) {
    FUN_ffff00009075ebd0();
    uVar1 = _DAT_ffff00001c004e54 >> 0x1e & 1;
  }
  _DAT_ffff00009087bf08 = _DAT_ffff00009087bf08 + 1;
  _DAT_ffff00009087bf0c = 0;
  FUN_ffff00009077cbd8(0x32);
  _DAT_ffff00001c004e54 = _DAT_ffff00001c004e54 | 8;
  while (-1 < (int)_DAT_ffff00001c004e54) {
    FUN_ffff00009075ebd0();
  }
  _DAT_ffff00009087bf0c = 0;
  _DAT_ffff00009087bf08 = _DAT_ffff00009087bf08 + 1;
  uVar1 = _DAT_ffff00001c004e54 & 0x1000;
  _DAT_ffff00001c004e54 = _DAT_ffff00001c004e54 & 0xfffffeed | 1;
  while (uVar1 != 0) {
    FUN_ffff00009075ebd0();
    uVar1 = _DAT_ffff00001c004e54 >> 0xc & 1;
  }
  _DAT_ffff00009087bf0c = 0;
  _DAT_ffff00009087bf08 = _DAT_ffff00009087bf08 + 1;
  _DAT_ffff00001c004e54 = _DAT_ffff00001c004e54 & 0xffffffdf | 0x40;
  _DAT_ffff00001c00d0e0 = 0x200000;
  return;
}



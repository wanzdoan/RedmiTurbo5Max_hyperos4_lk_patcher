// ffff00009075f164  size=316  FUN_ffff00009075f164

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075f164(void)

{
  uint uVar1;
  
  _DAT_ffff00009087bf98 = 1;
  DAT_ffff00009087bf9c = 1;
  _DAT_ffff00009087bfa0 = 0;
  uVar1 = _DAT_ffff00001c004e54 & 0x40000000;
  _DAT_ffff00001c004e54 = _DAT_ffff00001c004e54 | 4;
  while (uVar1 == 0) {
    FUN_ffff00009075ee68();
    uVar1 = _DAT_ffff00001c004e54 >> 0x1e & 1;
  }
  _DAT_ffff00009087bfa0 = _DAT_ffff00009087bfa0 + 1;
  _DAT_ffff00009087bfa4 = 0;
  FUN_ffff00009077cea0(0x32);
  _DAT_ffff00001c004e54 = _DAT_ffff00001c004e54 | 8;
  while (-1 < (int)_DAT_ffff00001c004e54) {
    FUN_ffff00009075ee68();
  }
  _DAT_ffff00009087bfa4 = 0;
  _DAT_ffff00009087bfa0 = _DAT_ffff00009087bfa0 + 1;
  uVar1 = _DAT_ffff00001c004e54 & 0x1000;
  _DAT_ffff00001c004e54 = _DAT_ffff00001c004e54 & 0xfffffeed | 1;
  while (uVar1 != 0) {
    FUN_ffff00009075ee68();
    uVar1 = _DAT_ffff00001c004e54 >> 0xc & 1;
  }
  _DAT_ffff00009087bfa4 = 0;
  _DAT_ffff00009087bfa0 = _DAT_ffff00009087bfa0 + 1;
  _DAT_ffff00001c004e54 = _DAT_ffff00001c004e54 & 0xffffffdf | 0x40;
  _DAT_ffff00001c00d0e0 = 0x200000;
  return;
}



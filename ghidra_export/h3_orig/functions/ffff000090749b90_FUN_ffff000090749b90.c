// ffff000090749b90  size=88  FUN_ffff000090749b90

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749b90(int param_1)

{
  if (param_1 == 0) {
    _DAT_ffff00000c1b0280 = _DAT_ffff00000c1b0280 & 0xfeeeffff;
    _DAT_ffff00000c1b0284 = _DAT_ffff00000c1b0284 & 0xffffeeee;
  }
  else {
    _DAT_ffff00000c1b0280 = _DAT_ffff00000c1b0280 | 0x1110000;
    _DAT_ffff00000c1b0284 = _DAT_ffff00000c1b0284 | 0x1111;
  }
  return;
}



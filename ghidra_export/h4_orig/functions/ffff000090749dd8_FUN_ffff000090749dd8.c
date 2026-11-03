// ffff000090749dd8  size=80  FUN_ffff000090749dd8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749dd8(int param_1)

{
  if (param_1 == 0) {
    _DAT_ffff00000c1b0280 = _DAT_ffff00000c1b0280 & 0xffffeffe;
    _DAT_ffff00000c1b0294 = _DAT_ffff00000c1b0294 & 0xfffffffe;
  }
  else {
    _DAT_ffff00000c1b0280 = _DAT_ffff00000c1b0280 | 0x1001;
    _DAT_ffff00000c1b0294 = _DAT_ffff00000c1b0294 | 1;
  }
  return;
}



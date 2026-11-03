// ffff000090749d70  size=52  FUN_ffff000090749d70

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749d70(int param_1)

{
  uint uVar1;
  
  uVar1 = 0;
  if (param_1 != 0) {
    uVar1 = 0xff;
  }
  _DAT_ffff00000c1b027c = _DAT_ffff00000c1b027c & 0xffffff00 | uVar1;
  return;
}



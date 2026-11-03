// ffff000090749a68  size=52  FUN_ffff000090749a68

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749a68(int param_1)

{
  uint uVar1;
  
  uVar1 = 0;
  if (param_1 != 0) {
    uVar1 = 0x10001;
  }
  _DAT_ffff00000c1b0270 = _DAT_ffff00000c1b0270 & 0xfffefffe | uVar1;
  return;
}



// ffff000090749b0c  size=52  FUN_ffff000090749b0c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749b0c(int param_1)

{
  uint uVar1;
  
  uVar1 = 0;
  if (param_1 != 0) {
    uVar1 = 0x1ffff00;
  }
  _DAT_ffff00000c1b027c = _DAT_ffff00000c1b027c & 0xfe0000ff | uVar1;
  return;
}



// ffff000090749de0  size=52  FUN_ffff000090749de0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749de0(int param_1)

{
  uint uVar1;
  
  uVar1 = 0x1e;
  if (param_1 != 0) {
    uVar1 = 0;
  }
  _DAT_ffff00000c1b02dc = _DAT_ffff00000c1b02dc & 0xffffffe1 | uVar1;
  return;
}



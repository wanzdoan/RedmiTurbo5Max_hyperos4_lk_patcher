
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749d34(int param_1)

{
  uint uVar1;
  
  uVar1 = 0;
  if (param_1 != 0) {
    uVar1 = 0x1fff07ff;
  }
  _DAT_ffff00000c1b0278 = _DAT_ffff00000c1b0278 & 0xe000f800 | uVar1;
  return;
}



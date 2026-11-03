// ffff000090749ed0  size=56  FUN_ffff000090749ed0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749ed0(int param_1)

{
  uint uVar1;
  
  uVar1 = 0x8a080000;
  if (param_1 != 0) {
    uVar1 = 0x8a080002;
  }
  _DAT_ffff00000c1b0298 = _DAT_ffff00000c1b0298 & 0xfffc | uVar1;
  return;
}



// ffff000090749e44  size=56  FUN_ffff000090749e44

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749e44(int param_1)

{
  uint uVar1;
  
  uVar1 = 0;
  if (param_1 != 0) {
    uVar1 = 0x14;
  }
  _DAT_ffff000010156008 = _DAT_ffff000010156008 & 0xffffffeb | uVar1;
  return;
}



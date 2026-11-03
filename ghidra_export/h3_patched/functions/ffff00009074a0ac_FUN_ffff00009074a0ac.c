// ffff00009074a0ac  size=64  FUN_ffff00009074a0ac

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009074a0ac(int param_1)

{
  uint uVar1;
  
  uVar1 = 6;
  if (param_1 != 0) {
    uVar1 = 0x26;
  }
  _DAT_ffff00001c03005c = _DAT_ffff00001c03005c & 0xfff83fd9 | uVar1;
  return;
}



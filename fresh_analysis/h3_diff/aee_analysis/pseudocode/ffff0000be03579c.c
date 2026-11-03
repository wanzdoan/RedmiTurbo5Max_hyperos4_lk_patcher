
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be03579c(uint param_1)

{
  uint uVar1;
  
  uVar1 = 0x2200004d;
  if ((param_1 & 1) == 0) {
    uVar1 = 0x22000004;
  }
  _DAT_ffff00001c010000 = _DAT_ffff00001c010000 & 0xddffffb0 | uVar1;
  if ((param_1 & 1) != 0) {
    _DAT_ffff00001c010008 = 0x1971;
  }
  return;
}



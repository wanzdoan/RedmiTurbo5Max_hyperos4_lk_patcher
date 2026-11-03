
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009074ab74(int param_1)

{
  ulong uVar1;
  
  _DAT_ffff00001326f010 = 0x6b32970a;
  if (param_1 != 0) {
    _DAT_ffff00001326f010 = 0;
  }
  uVar1 = 0x1001;
  do {
    if ((_DAT_ffff00001326f000 >> 1 & 1) == 0) {
      return 0;
    }
    uVar1 = uVar1 - 1;
  } while (1 < uVar1);
  return 0x1000000;
}



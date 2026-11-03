// ffff00009074a930  size=96  FUN_ffff00009074a930

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009074a930(undefined4 *param_1,undefined4 param_2)

{
  ulong uVar1;
  
  uVar1 = 0x1001;
  while ((_DAT_ffff00001326f000 >> 1 & 1) != 0) {
    uVar1 = uVar1 - 1;
    if (uVar1 < 2) {
      return 0x1000000;
    }
  }
  uVar1 = 0x1001;
  *param_1 = param_2;
  do {
    if ((_DAT_ffff00001326f000 >> 1 & 1) == 0) {
      return 0;
    }
    uVar1 = uVar1 - 1;
  } while (1 < uVar1);
  return 0x1000000;
}




/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009074ac28(void)

{
  uint uVar1;
  ulong uVar2;
  
  uVar1 = _DAT_ffff00001326f000;
  uVar2 = 0x100001;
  _DAT_ffff00001326f000 = _DAT_ffff00001326f000 | 4;
  do {
    if ((uVar1 & 1) != 0) {
      return 0;
    }
    uVar2 = uVar2 - 1;
  } while (1 < uVar2);
  return 0x2000000;
}



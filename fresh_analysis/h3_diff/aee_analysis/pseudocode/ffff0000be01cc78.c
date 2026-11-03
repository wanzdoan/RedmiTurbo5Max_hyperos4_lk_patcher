
long FUN_ffff0000be01cc78(void)

{
  long *plVar1;
  long *plVar2;
  long lVar3;
  
  FUN_ffff0000be01a730(&LAB_ffff0000be0d6218,0xffffffff);
  plVar1 = (long *)(DAT_ffff0000be111cc8 + 8);
  plVar2 = plVar1 + (ulong)(DAT_ffff0000be111d00 - 1) * 3;
  if (plVar2 < plVar1) {
    lVar3 = 0;
  }
  else {
    lVar3 = 0;
    do {
      if (*plVar1 != 0) {
        lVar3 = plVar1[1] + lVar3;
      }
      plVar1 = plVar1 + 3;
    } while (plVar1 <= plVar2);
  }
  if (DAT_ffff0000be112910 != 0) {
    plVar1 = (long *)(DAT_ffff0000be111cc8 + 0xc18);
    do {
      if (*plVar1 != 0) {
        lVar3 = plVar1[1] + lVar3;
      }
      plVar1 = plVar1 + 0x13;
    } while (plVar1 <= (long *)(DAT_ffff0000be111cc8 + 0x5780U));
  }
  FUN_ffff0000be01a7c8(&LAB_ffff0000be0d6218);
  return lVar3;
}




long FUN_ffff00009071b008(void)

{
  long *plVar1;
  long *plVar2;
  long lVar3;
  
  FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
  plVar1 = (long *)(DAT_ffff000090850c90 + 8);
  plVar2 = plVar1 + (ulong)(DAT_ffff00009085acd0 - 1) * 3;
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
  if (DAT_ffff00009085b8e0 != 0) {
    plVar1 = (long *)(DAT_ffff000090850c90 + 0xc18);
    do {
      if (*plVar1 != 0) {
        lVar3 = plVar1[1] + lVar3;
      }
      plVar1 = plVar1 + 0x13;
    } while (plVar1 <= (long *)(DAT_ffff000090850c90 + 0x5780U));
  }
  FUN_ffff0000907177d0(&LAB_ffff000090818208);
  return lVar3;
}



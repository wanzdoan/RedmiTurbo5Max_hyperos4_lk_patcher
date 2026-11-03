// File offset entry: 0xdbfc

long FUN_ffff0000b800dbfc(void)

{
  long *plVar1;
  long *plVar2;
  long lVar3;
  
  FUN_ffff0000b800ad90(&LAB_ffff0000b80adcf8,0xffffffff);
  plVar1 = (long *)(DAT_ffff0000b8115508 + 8);
  plVar2 = plVar1 + (ulong)(DAT_ffff0000b8115548 - 1) * 3;
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
  if (DAT_ffff0000b8116158 != 0) {
    plVar1 = (long *)(DAT_ffff0000b8115508 + 0xc18);
    do {
      if (*plVar1 != 0) {
        lVar3 = plVar1[1] + lVar3;
      }
      plVar1 = plVar1 + 0x13;
    } while (plVar1 <= (long *)(DAT_ffff0000b8115508 + 0x5780U));
  }
  FUN_ffff0000b800ae28(&LAB_ffff0000b80adcf8);
  return lVar3;
}



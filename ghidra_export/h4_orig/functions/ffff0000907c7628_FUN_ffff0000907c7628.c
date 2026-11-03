// ffff0000907c7628  size=12  FUN_ffff0000907c7628

long FUN_ffff0000907c7628(long param_1)

{
  ulong uVar1;
  uint uVar2;
  long lVar3;
  long lVar4;
  ulong uVar5;
  ulong uVar6;
  
  lVar4 = *(long *)(param_1 + 8);
  if (lVar4 == 0) {
    return 0;
  }
  lVar3 = lVar4 * 0x20 + 0x20;
  do {
    lVar4 = lVar4 + -1;
    if (lVar4 == 0) {
      uVar2 = **(uint **)(param_1 + 0x10);
      lVar3 = 0x20;
      break;
    }
    uVar2 = (*(uint **)(param_1 + 0x10))[lVar4];
    lVar3 = lVar3 + -0x20;
  } while (uVar2 == 0);
  if ((int)uVar2 < 0) {
    return lVar3;
  }
  uVar5 = 0x80000000;
  uVar6 = 0;
  do {
    uVar1 = uVar6 + 1;
    if (0x1e < uVar6) break;
    uVar5 = uVar5 >> 1;
    uVar6 = uVar1;
  } while (((uint)uVar5 & uVar2) == 0);
  return lVar3 - uVar1;
}



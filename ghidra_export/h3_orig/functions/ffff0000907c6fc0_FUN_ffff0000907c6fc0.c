// ffff0000907c6fc0  size=120  FUN_ffff0000907c6fc0

long FUN_ffff0000907c6fc0(long param_1)

{
  ulong uVar1;
  uint uVar2;
  long lVar3;
  long in_x9;
  ulong uVar4;
  ulong uVar5;
  
  lVar3 = in_x9 * 0x20 + 0x20;
  do {
    in_x9 = in_x9 + -1;
    if (in_x9 == 0) {
      uVar2 = **(uint **)(param_1 + 0x10);
      lVar3 = 0x20;
      break;
    }
    uVar2 = (*(uint **)(param_1 + 0x10))[in_x9];
    lVar3 = lVar3 + -0x20;
  } while (uVar2 == 0);
  if ((int)uVar2 < 0) {
    return lVar3;
  }
  uVar4 = 0x80000000;
  uVar5 = 0;
  do {
    uVar1 = uVar5 + 1;
    if (0x1e < uVar5) break;
    uVar4 = uVar4 >> 1;
    uVar5 = uVar1;
  } while (((uint)uVar4 & uVar2) == 0);
  return lVar3 - uVar1;
}



// ffff00009079d340  size=312  FUN_ffff00009079d340

undefined8 FUN_ffff00009079d340(long param_1,long param_2)

{
  uint uVar1;
  uint uVar2;
  bool bVar3;
  undefined8 uVar4;
  long lVar5;
  uint uVar6;
  long lVar7;
  uint uVar8;
  int iVar9;
  long lVar10;
  long lVar11;
  uint uVar12;
  
  uVar12 = *(uint *)(param_2 + 8);
  uVar6 = *(uint *)(param_1 + 8);
  if (uVar6 < uVar12) {
    if ((*(uint *)(param_1 + 4) < uVar12) &&
       (uVar4 = FUN_ffff00009079f598(param_1,uVar12), (int)uVar4 != 0)) {
      return uVar4;
    }
    *(uint *)(param_1 + 8) = uVar12;
    uVar6 = uVar12;
  }
  else if (uVar12 == 0) {
    return 0;
  }
  lVar7 = 0;
  uVar8 = 0;
  lVar5 = *(long *)(param_1 + 0x10);
  lVar10 = *(long *)(param_2 + 0x10);
  uVar2 = uVar12;
  do {
    uVar2 = uVar2 - 1;
    uVar1 = uVar8 + *(ushort *)(lVar5 + lVar7) + (uint)*(ushort *)(lVar10 + lVar7);
    uVar8 = uVar1 >> 0x10;
    *(short *)(lVar5 + lVar7) = (short)uVar1;
    lVar7 = lVar7 + 2;
  } while (uVar2 != 0);
  bVar3 = (uVar1 & 0xffff0000) != 0;
  if (0xffff < uVar1 && uVar12 < uVar6) {
    lVar10 = 0;
    do {
      lVar11 = lVar10 * 2;
      iVar9 = (int)lVar10;
      lVar10 = lVar10 + 1;
      uVar8 = *(ushort *)(lVar5 + lVar7 + lVar11) + 1;
      bVar3 = (uVar8 & 0xffff) != uVar8;
      *(short *)(lVar5 + lVar7 + lVar11) = (short)uVar8;
    } while (bVar3 && uVar12 + iVar9 + 1 < uVar6);
    uVar12 = uVar12 + (int)lVar10;
  }
  if (!bVar3) {
    return 0;
  }
  uVar8 = uVar6 + 1;
  if (uVar6 != 0xffffffff) {
    if (*(uint *)(param_1 + 4) < uVar8) {
      uVar4 = FUN_ffff00009079f598(param_1,uVar8);
      if ((int)uVar4 != 0) {
        return uVar4;
      }
      lVar5 = *(long *)(param_1 + 0x10);
    }
    *(uint *)(param_1 + 8) = uVar8;
  }
  *(undefined2 *)(lVar5 + (ulong)uVar12 * 2) = 1;
  return 0;
}



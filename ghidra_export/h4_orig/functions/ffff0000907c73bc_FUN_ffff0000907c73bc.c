// ffff0000907c73bc  size=364  FUN_ffff0000907c73bc

undefined8 FUN_ffff0000907c73bc(undefined2 *param_1,undefined2 *param_2,char param_3)

{
  undefined2 *puVar1;
  undefined4 *puVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  long lVar5;
  ulong uVar6;
  long lVar7;
  ulong uVar8;
  undefined4 *puVar9;
  undefined4 *puVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  
  uVar13 = *(ulong *)(param_2 + 4);
  if (10000 < uVar13) {
    return 0xfffffff0;
  }
  uVar6 = *(ulong *)(param_1 + 4);
  uVar8 = uVar13;
  if (*(ulong *)(param_1 + 4) < uVar13) {
    lVar5 = FUN_ffff00009077d5a8(uVar13,4);
    if (lVar5 == 0) {
      return 0xfffffff0;
    }
    if (*(long *)(param_1 + 8) != 0) {
      FUN_ffff00009077eb20(lVar5,*(long *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
      FUN_ffff0000907cca9c(*(undefined8 *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
      thunk_FUN_ffff0000907c314c(*(undefined8 *)(param_1 + 8));
    }
    *(ulong *)(param_1 + 4) = uVar13;
    uVar8 = *(ulong *)(param_2 + 4);
    *(long *)(param_1 + 8) = lVar5;
    uVar6 = uVar13;
  }
  puVar1 = param_1;
  if (param_3 != '\0') {
    puVar1 = param_2;
  }
  *param_1 = *puVar1;
  if (uVar8 != 0) {
    puVar10 = *(undefined4 **)(param_2 + 8);
    puVar9 = *(undefined4 **)(param_1 + 8);
    uVar13 = uVar8;
    do {
      puVar2 = puVar9;
      if (param_3 != '\0') {
        puVar2 = puVar10;
      }
      uVar13 = uVar13 - 1;
      puVar10 = puVar10 + 1;
      *puVar9 = *puVar2;
      puVar9 = puVar9 + 1;
    } while (uVar13 != 0);
  }
  uVar13 = uVar6 - uVar8;
  if (uVar8 <= uVar6 && uVar13 != 0) {
    lVar5 = *(long *)(param_1 + 8);
    if (1 < uVar13) {
      uVar11 = uVar13 & 0xfffffffffffffffe;
      lVar7 = uVar8 * 4;
      uVar8 = uVar8 + uVar11;
      puVar10 = (undefined4 *)(lVar5 + lVar7 + 4);
      uVar12 = uVar11;
      do {
        uVar4 = puVar10[-1];
        uVar3 = *puVar10;
        if (param_3 != '\0') {
          uVar4 = 0;
          uVar3 = 0;
        }
        uVar12 = uVar12 - 2;
        puVar10[-1] = uVar4;
        *puVar10 = uVar3;
        puVar10 = puVar10 + 2;
      } while (uVar12 != 0);
      if (uVar13 == uVar11) {
        return 0;
      }
    }
    lVar7 = uVar6 - uVar8;
    puVar10 = (undefined4 *)(lVar5 + uVar8 * 4);
    do {
      uVar4 = *puVar10;
      if (param_3 != '\0') {
        uVar4 = 0;
      }
      lVar7 = lVar7 + -1;
      *puVar10 = uVar4;
      puVar10 = puVar10 + 1;
    } while (lVar7 != 0);
  }
  return 0;
}



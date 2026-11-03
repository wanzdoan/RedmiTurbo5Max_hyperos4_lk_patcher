// ffff0000907c7de8  size=624  FUN_ffff0000907c7de8

undefined8 FUN_ffff0000907c7de8(undefined2 *param_1,undefined2 *param_2,undefined2 *param_3)

{
  uint uVar1;
  undefined2 *puVar2;
  uint uVar3;
  bool bVar4;
  undefined8 uVar5;
  uint *puVar6;
  ulong uVar7;
  ulong uVar8;
  uint *puVar9;
  uint *puVar10;
  ulong uVar11;
  long lVar12;
  uint uVar13;
  ulong uVar14;
  long lVar15;
  long lVar16;
  
  puVar2 = param_2;
  if (param_1 != param_3) {
    puVar2 = param_3;
  }
  if (((param_2 != param_1) && (param_1 != param_3)) &&
     (uVar5 = FUN_ffff0000907c7260(param_1), (int)uVar5 != 0)) {
    return uVar5;
  }
  uVar7 = *(ulong *)(puVar2 + 4);
  lVar16 = uVar7 * -4;
  *param_1 = 1;
  do {
    uVar8 = uVar7;
    if (uVar8 == 0) {
      return 0;
    }
    lVar16 = lVar16 + 4;
    uVar7 = uVar8 - 1;
  } while (*(int *)(*(long *)(puVar2 + 8) + uVar8 * 4 + -4) == 0);
  if (uVar8 < 0x2711) {
    uVar7 = *(ulong *)(param_1 + 4);
    if (uVar7 < uVar8) {
      puVar6 = (uint *)FUN_ffff00009077d5a8(uVar8,4);
      if (puVar6 == (uint *)0x0) {
        return 0xfffffff0;
      }
      if (*(long *)(param_1 + 8) != 0) {
        FUN_ffff00009077eb20(puVar6,*(long *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
        FUN_ffff0000907cca9c(*(undefined8 *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
        thunk_FUN_ffff0000907c314c(*(undefined8 *)(param_1 + 8));
      }
      *(ulong *)(param_1 + 4) = uVar8;
      *(uint **)(param_1 + 8) = puVar6;
      uVar7 = uVar8;
    }
    else {
      puVar6 = *(uint **)(param_1 + 8);
    }
    uVar13 = 0;
    uVar11 = 0;
    lVar15 = 8 - lVar16;
    uVar14 = uVar8 + 2;
    lVar12 = *(long *)(puVar2 + 8);
    puVar9 = puVar6;
    do {
      uVar3 = *(uint *)(lVar12 + uVar11 * 4);
      uVar11 = uVar11 + 1;
      uVar1 = *puVar9 + uVar13;
      bVar4 = CARRY4(*puVar9,uVar13);
      uVar13 = (uint)CARRY4(uVar1,uVar3);
      if (bVar4) {
        uVar13 = uVar13 + 1;
      }
      puVar10 = puVar9 + 1;
      *puVar9 = uVar1 + uVar3;
      puVar9 = puVar10;
    } while (uVar8 != uVar11);
    if (uVar13 != 0) {
      if (uVar7 <= uVar8) {
        uVar8 = uVar8 + 1;
        if (10000 < uVar8) {
          return 0xfffffff0;
        }
        if (uVar7 < uVar8) {
          puVar6 = (uint *)FUN_ffff00009077d5a8(uVar8,4);
          if (puVar6 == (uint *)0x0) {
            return 0xfffffff0;
          }
          if (*(long *)(param_1 + 8) != 0) {
            FUN_ffff00009077eb20(puVar6,*(long *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
            FUN_ffff0000907cca9c(*(undefined8 *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
            thunk_FUN_ffff0000907c314c(*(undefined8 *)(param_1 + 8));
          }
          *(ulong *)(param_1 + 4) = uVar8;
          *(uint **)(param_1 + 8) = puVar6;
          uVar7 = uVar8;
        }
        puVar10 = (uint *)((long)puVar6 + (4 - lVar16));
      }
      uVar1 = *puVar10;
      *puVar10 = uVar1 + uVar13;
      if (CARRY4(uVar1,uVar13)) {
        do {
          if (uVar14 - 1 < uVar7) {
            puVar10 = puVar10 + 1;
          }
          else {
            if (10000 < uVar14) {
              return 0xfffffff0;
            }
            if (uVar7 < uVar14) {
              puVar6 = (uint *)FUN_ffff00009077d5a8(uVar14,4);
              if (puVar6 == (uint *)0x0) {
                return 0xfffffff0;
              }
              if (*(long *)(param_1 + 8) != 0) {
                FUN_ffff00009077eb20(puVar6,*(long *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
                FUN_ffff0000907cca9c(*(undefined8 *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
                thunk_FUN_ffff0000907c314c(*(undefined8 *)(param_1 + 8));
              }
              *(ulong *)(param_1 + 4) = uVar14;
              *(uint **)(param_1 + 8) = puVar6;
              uVar7 = uVar14;
            }
            puVar10 = (uint *)((long)puVar6 + lVar15);
          }
          uVar13 = *puVar10;
          uVar14 = uVar14 + 1;
          lVar15 = lVar15 + 4;
          *puVar10 = uVar13 + 1;
        } while (uVar13 == 0xffffffff);
        return 0;
      }
    }
    return 0;
  }
  return 0xfffffff0;
}



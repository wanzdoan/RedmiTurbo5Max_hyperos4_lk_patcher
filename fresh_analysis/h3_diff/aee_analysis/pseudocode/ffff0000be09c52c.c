
undefined8 FUN_ffff0000be09c52c(long param_1,ulong param_2)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  ulong uVar4;
  long lVar5;
  long lVar6;
  ulong uVar7;
  uint *puVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  uint uVar12;
  ulong uVar13;
  
  uVar4 = *(ulong *)(param_1 + 8);
  if (uVar4 == 0) {
    lVar6 = 0;
  }
  else {
    lVar5 = uVar4 * 0x20 + 0x20;
    uVar10 = uVar4;
    do {
      uVar10 = uVar10 - 1;
      if (uVar10 == 0) {
        uVar3 = **(uint **)(param_1 + 0x10);
        lVar5 = 0x20;
        break;
      }
      uVar3 = (*(uint **)(param_1 + 0x10))[uVar10];
      lVar5 = lVar5 + -0x20;
    } while (uVar3 == 0);
    if ((int)uVar3 < 0) {
      lVar6 = 0;
    }
    else {
      uVar10 = 0x80000000;
      uVar7 = 0;
      do {
        uVar9 = uVar7 + 1;
        if (0x1e < uVar7) break;
        uVar10 = uVar10 >> 1;
        uVar7 = uVar9;
      } while (((uint)uVar10 & uVar3) == 0);
      lVar6 = -uVar9;
    }
    lVar6 = lVar6 + lVar5;
  }
  uVar10 = lVar6 + param_2;
  if (uVar4 * 0x20 < uVar10) {
    uVar7 = uVar10 >> 5;
    if ((uVar10 & 0x1f) != 0) {
      uVar7 = uVar7 + 1;
    }
    if (10000 < uVar7) {
      return 0xfffffff0;
    }
    if (uVar4 < uVar7) {
      lVar6 = FUN_ffff0000be05f250(uVar7,4);
      if (lVar6 == 0) {
        return 0xfffffff0;
      }
      if (*(long *)(param_1 + 0x10) != 0) {
        FUN_ffff0000be0607b0(lVar6,*(long *)(param_1 + 0x10),*(long *)(param_1 + 8) << 2);
        FUN_ffff0000be0a174c(*(undefined8 *)(param_1 + 0x10),*(long *)(param_1 + 8) << 2);
        thunk_FUN_ffff0000be09937c(*(undefined8 *)(param_1 + 0x10));
      }
      *(ulong *)(param_1 + 8) = uVar7;
      *(long *)(param_1 + 0x10) = lVar6;
      uVar4 = uVar7;
    }
  }
  uVar7 = param_2 >> 5;
  uVar10 = param_2 & 0x1f;
  if (0x1f < param_2) {
    uVar9 = uVar4;
    if (uVar7 < uVar4) {
      lVar6 = *(long *)(param_1 + 0x10);
      do {
        lVar5 = uVar9 * 4;
        uVar9 = uVar9 - 1;
        *(undefined4 *)(lVar6 + -4 + lVar5) = *(undefined4 *)(lVar6 + uVar7 * -4 + -4 + lVar5);
      } while (uVar7 < uVar9);
    }
    if (uVar9 != 0) {
      lVar6 = *(long *)(param_1 + 0x10);
      if (uVar9 == 1) {
        uVar11 = 1;
      }
      else {
        uVar11 = uVar9 & 1;
        lVar5 = lVar6 + uVar9 * 4 + -4;
        uVar13 = uVar9 & 0xfffffffffffffffe;
        do {
          uVar13 = uVar13 - 2;
          *(undefined8 *)(lVar5 + -4) = 0;
          lVar5 = lVar5 + -8;
        } while (uVar13 != 0);
        if (uVar9 == (uVar9 & 0xfffffffffffffffe)) goto LAB_ffff0000be09c6dc;
      }
      do {
        uVar9 = uVar11 - 1;
        *(undefined4 *)(lVar6 + -4 + uVar11 * 4) = 0;
        uVar11 = uVar9;
      } while (uVar9 != 0);
    }
  }
LAB_ffff0000be09c6dc:
  if (uVar10 == 0) {
    return 0;
  }
  uVar9 = uVar4 - uVar7;
  if (uVar7 <= uVar4 && uVar9 != 0) {
    lVar6 = *(long *)(param_1 + 0x10);
    uVar3 = 0x20 - (int)uVar10;
    if (uVar9 < 2) {
      uVar12 = 0;
      uVar11 = uVar7;
    }
    else {
      uVar13 = uVar9 & 0xfffffffffffffffe;
      uVar12 = 0;
      uVar11 = uVar7 + uVar13;
      puVar8 = (uint *)(lVar6 + uVar7 * 4 + 4);
      uVar7 = uVar13;
      do {
        uVar2 = puVar8[-1];
        uVar1 = *puVar8;
        uVar7 = uVar7 - 2;
        puVar8[-1] = uVar2 << uVar10 | uVar12;
        *puVar8 = uVar1 << uVar10 | uVar2 >> (ulong)(uVar3 & 0x1f);
        uVar12 = uVar1 >> (ulong)(uVar3 & 0x1f);
        puVar8 = puVar8 + 2;
      } while (uVar7 != 0);
      if (uVar9 == uVar13) {
        return 0;
      }
    }
    lVar5 = uVar4 - uVar11;
    puVar8 = (uint *)(lVar6 + uVar11 * 4);
    do {
      uVar2 = *puVar8;
      lVar5 = lVar5 + -1;
      *puVar8 = uVar2 << uVar10 | uVar12;
      puVar8 = puVar8 + 1;
      uVar12 = uVar2 >> (ulong)(uVar3 & 0x1f);
    } while (lVar5 != 0);
    return 0;
  }
  return 0;
}




undefined8 FUN_ffff0000be09c798(long param_1,ulong param_2)

{
  uint uVar1;
  uint uVar2;
  undefined8 uVar3;
  ulong uVar4;
  ulong uVar5;
  long lVar6;
  ulong uVar7;
  long lVar8;
  undefined4 *puVar9;
  uint uVar10;
  ulong uVar11;
  ulong uVar12;
  uint *puVar13;
  ulong uVar14;
  
  uVar5 = *(ulong *)(param_1 + 8);
  uVar7 = param_2 >> 5;
  if ((uVar5 < uVar7) || ((uVar4 = param_2 & 0x1f, uVar4 != 0 && (uVar5 == uVar7)))) {
    uVar3 = FUN_ffff0000be09c1d8(param_1,0);
    return uVar3;
  }
  if (0x1f < param_2) {
    uVar14 = uVar5 - uVar7;
    if (uVar14 != 0) {
      puVar9 = *(undefined4 **)(param_1 + 0x10);
      uVar11 = uVar14;
      do {
        uVar11 = uVar11 - 1;
        *puVar9 = puVar9[uVar7];
        puVar9 = puVar9 + 1;
      } while (uVar11 != 0);
    }
    uVar7 = uVar5 - uVar14;
    if (uVar14 <= uVar5 && uVar7 != 0) {
      lVar8 = *(long *)(param_1 + 0x10);
      if (1 < uVar7) {
        uVar12 = uVar7 & 0xfffffffffffffffe;
        lVar6 = lVar8 + uVar14 * 4 + 4;
        uVar11 = uVar12;
        do {
          *(undefined8 *)(lVar6 + -4) = 0;
          uVar11 = uVar11 - 2;
          lVar6 = lVar6 + 8;
        } while (uVar11 != 0);
        uVar14 = uVar14 + uVar12;
        if (uVar7 == uVar12) goto LAB_ffff0000be09c84c;
      }
      lVar6 = uVar5 - uVar14;
      puVar9 = (undefined4 *)(lVar8 + uVar14 * 4);
      do {
        lVar6 = lVar6 + -1;
        *puVar9 = 0;
        puVar9 = puVar9 + 1;
      } while (lVar6 != 0);
    }
  }
LAB_ffff0000be09c84c:
  if ((uVar4 != 0) && (uVar5 != 0)) {
    lVar8 = *(long *)(param_1 + 0x10);
    uVar2 = 0x20 - (int)uVar4;
    if (uVar5 < 2) {
      uVar10 = 0;
      uVar7 = uVar5;
    }
    else {
      uVar10 = 0;
      uVar7 = uVar5 & 1;
      puVar13 = (uint *)(lVar8 + uVar5 * 4 + -4);
      uVar14 = uVar5 & 0xfffffffffffffffe;
      do {
        uVar14 = uVar14 - 2;
        uVar1 = *puVar13 >> uVar4 | uVar10;
        uVar10 = puVar13[-1] << (ulong)(uVar2 & 0x1f);
        puVar13[-1] = puVar13[-1] >> uVar4 | *puVar13 << (ulong)(uVar2 & 0x1f);
        *puVar13 = uVar1;
        puVar13 = puVar13 + -2;
      } while (uVar14 != 0);
      if (uVar5 == (uVar5 & 0xfffffffffffffffe)) {
        return 0;
      }
    }
    lVar8 = lVar8 + -4;
    do {
      lVar6 = uVar7 * 4;
      uVar7 = uVar7 - 1;
      uVar1 = *(uint *)(lVar8 + lVar6);
      *(uint *)(lVar8 + lVar6) = uVar1 >> uVar4 | uVar10;
      uVar10 = uVar1 << (ulong)(uVar2 & 0x1f);
    } while (uVar7 != 0);
  }
  return 0;
}



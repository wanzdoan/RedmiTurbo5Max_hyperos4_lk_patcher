// ffff0000907c7b14  size=304  FUN_ffff0000907c7b14

undefined8 FUN_ffff0000907c7b14(long param_1,ulong param_2)

{
  uint uVar1;
  uint uVar2;
  uint in_w8;
  undefined4 in_register_00004044;
  ulong in_x9;
  long lVar3;
  long in_x11;
  long lVar4;
  undefined4 *puVar5;
  ulong uVar6;
  uint uVar7;
  ulong uVar8;
  ulong uVar9;
  uint *puVar10;
  ulong uVar11;
  
  if (0x1f < param_2) {
    uVar6 = in_x9 - in_x11;
    if (uVar6 != 0) {
      puVar5 = *(undefined4 **)(param_1 + 0x10);
      uVar11 = uVar6;
      do {
        uVar11 = uVar11 - 1;
        *puVar5 = puVar5[in_x11];
        puVar5 = puVar5 + 1;
      } while (uVar11 != 0);
    }
    uVar11 = in_x9 - uVar6;
    if (uVar6 <= in_x9 && uVar11 != 0) {
      lVar4 = *(long *)(param_1 + 0x10);
      if (1 < uVar11) {
        uVar8 = uVar11 & 0xfffffffffffffffe;
        lVar3 = lVar4 + uVar6 * 4 + 4;
        uVar9 = uVar8;
        do {
          *(undefined8 *)(lVar3 + -4) = 0;
          uVar9 = uVar9 - 2;
          lVar3 = lVar3 + 8;
        } while (uVar9 != 0);
        uVar6 = uVar6 + uVar8;
        if (uVar11 == uVar8) goto LAB_ffff0000907c7b9c;
      }
      lVar3 = in_x9 - uVar6;
      puVar5 = (undefined4 *)(lVar4 + uVar6 * 4);
      do {
        lVar3 = lVar3 + -1;
        *puVar5 = 0;
        puVar5 = puVar5 + 1;
      } while (lVar3 != 0);
    }
  }
LAB_ffff0000907c7b9c:
  if ((CONCAT44(in_register_00004044,in_w8) != 0) && (in_x9 != 0)) {
    lVar4 = *(long *)(param_1 + 0x10);
    uVar2 = 0x20 - in_w8;
    if (in_x9 < 2) {
      uVar7 = 0;
      uVar6 = in_x9;
    }
    else {
      uVar7 = 0;
      uVar6 = in_x9 & 1;
      puVar10 = (uint *)(lVar4 + in_x9 * 4 + -4);
      uVar11 = in_x9 & 0xfffffffffffffffe;
      do {
        uVar11 = uVar11 - 2;
        uVar1 = *puVar10 >> (ulong)(in_w8 & 0x1f) | uVar7;
        uVar7 = puVar10[-1] << (ulong)(uVar2 & 0x1f);
        puVar10[-1] = puVar10[-1] >> (ulong)(in_w8 & 0x1f) | *puVar10 << (ulong)(uVar2 & 0x1f);
        *puVar10 = uVar1;
        puVar10 = puVar10 + -2;
      } while (uVar11 != 0);
      if (in_x9 == (in_x9 & 0xfffffffffffffffe)) {
        return 0;
      }
    }
    lVar4 = lVar4 + -4;
    do {
      lVar3 = uVar6 * 4;
      uVar6 = uVar6 - 1;
      uVar1 = *(uint *)(lVar4 + lVar3);
      *(uint *)(lVar4 + lVar3) = uVar1 >> (ulong)(in_w8 & 0x1f) | uVar7;
      uVar7 = uVar1 << (ulong)(uVar2 & 0x1f);
    } while (uVar6 != 0);
  }
  return 0;
}



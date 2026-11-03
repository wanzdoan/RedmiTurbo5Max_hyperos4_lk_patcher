
undefined8 FUN_ffff0000be0789a8(long param_1,uint param_2)

{
  uint uVar1;
  int iVar2;
  ushort uVar3;
  uint uVar4;
  uint uVar5;
  undefined8 uVar6;
  long lVar7;
  uint uVar8;
  uint uVar9;
  undefined2 *puVar10;
  ulong uVar11;
  ulong uVar12;
  long lVar13;
  ulong uVar14;
  ushort *puVar15;
  uint uVar16;
  
  if ((param_2 & 0xffff) < 0x10) {
    lVar7 = *(long *)(param_1 + 0x10);
    uVar16 = *(uint *)(param_1 + 8);
  }
  else {
    uVar16 = *(uint *)(param_1 + 8);
    uVar4 = param_2 >> 4 & 0xfff;
    uVar1 = uVar16 + uVar4;
    if (uVar16 < uVar1) {
      if ((*(uint *)(param_1 + 4) < uVar1) &&
         (uVar6 = FUN_ffff0000be07bc60(param_1,uVar1), (int)uVar6 != 0)) {
        return uVar6;
      }
      *(uint *)(param_1 + 8) = uVar1;
      uVar16 = uVar1;
    }
    lVar7 = *(long *)(param_1 + 0x10);
    if (-1 < (int)(uVar16 + ~uVar4)) {
      puVar10 = (undefined2 *)(lVar7 + (ulong)(uVar16 + ~uVar4) * 2);
      uVar1 = uVar16;
      do {
        uVar1 = uVar1 - 1;
        *(undefined2 *)(lVar7 + (ulong)uVar1 * 2) = *puVar10;
        puVar10 = puVar10 + -1;
      } while (0 < (int)(uVar1 - uVar4));
    }
    if (uVar4 < 2) {
      uVar4 = 1;
    }
    uVar12 = (ulong)uVar4;
    if (uVar4 < 2) {
      uVar14 = 0;
    }
    else {
      uVar11 = 0;
      uVar14 = uVar12 & 0xffe;
      do {
        lVar13 = uVar11 * 2;
        uVar11 = uVar11 + 2;
        *(undefined4 *)(lVar7 + lVar13) = 0;
      } while (uVar14 != uVar11);
      if (uVar14 == uVar12) goto LAB_ffff0000be078a98;
    }
    do {
      *(undefined2 *)(lVar7 + uVar14 * 2) = 0;
      uVar14 = uVar14 + 1;
    } while (uVar12 != uVar14);
  }
LAB_ffff0000be078a98:
  uVar1 = param_2 & 0xf;
  uVar5 = 0x10 - uVar1;
  uVar4 = -1 << (ulong)(param_2 & 0xf);
  if (((uint)(*(ushort *)(lVar7 + (ulong)(uVar16 - 1) * 2) >> (ulong)(uVar5 & 0x1f)) &
      (uVar4 ^ 0xffffffff)) != 0) {
    uVar6 = FUN_ffff0000be07bc60(param_1,uVar16 + 1);
    if ((int)uVar6 != 0) {
      return uVar6;
    }
    lVar7 = *(long *)(param_1 + 0x10);
  }
  if (uVar16 != 0) {
    uVar4 = ~uVar4;
    uVar12 = (ulong)uVar16;
    if (uVar16 == 1) {
      uVar14 = 0;
      uVar8 = 0;
LAB_ffff0000be078b48:
      lVar13 = uVar12 - uVar14;
      puVar15 = (ushort *)(lVar7 + uVar14 * 2);
      uVar9 = uVar8;
      do {
        lVar13 = lVar13 + -1;
        uVar8 = *puVar15 >> (ulong)(uVar5 & 0x1f) & uVar4;
        *puVar15 = *puVar15 << (ulong)uVar1 | (ushort)uVar9;
        puVar15 = puVar15 + 1;
        uVar9 = uVar8;
      } while (lVar13 != 0);
    }
    else {
      uVar14 = uVar12 & 0xfffffffe;
      uVar8 = 0;
      puVar15 = (ushort *)(lVar7 + 2);
      uVar11 = uVar14;
      do {
        uVar3 = puVar15[-1];
        uVar11 = uVar11 - 2;
        puVar15[-1] = uVar3 << (ulong)uVar1 | (ushort)uVar8;
        uVar8 = *puVar15 >> (ulong)(uVar5 & 0x1f) & uVar4;
        *puVar15 = *puVar15 << (ulong)uVar1 | uVar3 >> (ulong)(uVar5 & 0x1f) & (ushort)uVar4;
        puVar15 = puVar15 + 2;
      } while (uVar11 != 0);
      if (uVar14 != uVar12) goto LAB_ffff0000be078b48;
    }
    if (uVar8 != 0) {
      iVar2 = *(int *)(param_1 + 8);
      *(short *)(lVar7 + (ulong)uVar16 * 2) = (short)uVar8;
      uVar1 = iVar2 + 1;
      goto joined_r0xffff0000be078b9c;
    }
  }
  uVar1 = *(uint *)(param_1 + 8);
joined_r0xffff0000be078b9c:
  uVar12 = (ulong)uVar1;
  if (1 < uVar1) {
    do {
      if (*(short *)(lVar7 + -2 + uVar12 * 2) != 0) goto LAB_ffff0000be078bc0;
      uVar12 = uVar12 - 1;
    } while (1 < (uint)uVar12);
    uVar12 = 1;
  }
LAB_ffff0000be078bc0:
  *(int *)(param_1 + 8) = (int)uVar12;
  return 0;
}



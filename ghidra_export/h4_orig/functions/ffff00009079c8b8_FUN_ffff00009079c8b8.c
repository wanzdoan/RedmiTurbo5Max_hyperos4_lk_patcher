// ffff00009079c8b8  size=556  FUN_ffff00009079c8b8

void FUN_ffff00009079c8b8(undefined1 *param_1,uint param_2)

{
  uint uVar1;
  int iVar2;
  ushort uVar3;
  ushort uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  uint uVar9;
  ulong uVar10;
  long lVar11;
  ulong uVar12;
  undefined2 *puVar13;
  ulong uVar14;
  uint uVar15;
  ulong uVar16;
  int iVar17;
  ulong uVar18;
  uint uVar19;
  undefined2 *puVar20;
  
  puVar20 = *(undefined2 **)(param_1 + 0x10);
  uVar9 = *(uint *)(param_1 + 8);
  uVar10 = (ulong)uVar9;
  if ((param_2 & 0xffff) < 0x10) {
LAB_ffff00009079c9c0:
    uVar9 = (int)uVar10 - 1;
    uVar12 = (ulong)uVar9;
    if ((int)uVar9 < 0) goto LAB_ffff00009079ca9c;
  }
  else {
    uVar8 = param_2 >> 4 & 0xfff;
    if (uVar8 < uVar9) {
      lVar11 = uVar10 - uVar8;
      puVar13 = puVar20;
      do {
        lVar11 = lVar11 + -1;
        *puVar13 = puVar13[uVar8];
        puVar13 = puVar13 + 1;
      } while (lVar11 != 0);
      if (uVar8 <= uVar9) {
        uVar16 = (ulong)(uVar9 - uVar8);
        uVar12 = uVar10;
        if (uVar10 < uVar16 + 1) {
          uVar12 = uVar16 + 1;
        }
        uVar12 = uVar12 - uVar16;
        if (1 < uVar12) {
          uVar14 = uVar12 & 0xfffffffffffffffe;
          puVar13 = puVar20 + uVar16 + 1;
          uVar18 = uVar14;
          do {
            *(undefined4 *)(puVar13 + -1) = 0;
            uVar18 = uVar18 - 2;
            puVar13 = puVar13 + 2;
          } while (uVar18 != 0);
          uVar16 = uVar14 + uVar16;
          if (uVar12 == uVar14) goto joined_r0xffff00009079c9a0;
        }
        do {
          uVar12 = uVar16 + 1;
          puVar20[uVar16] = 0;
          uVar16 = uVar12;
        } while (uVar12 < uVar10);
      }
joined_r0xffff00009079c9a0:
      while (1 < uVar9) {
        if (puVar20[uVar10 - 1] != 0) goto LAB_ffff00009079c9c0;
        uVar10 = uVar10 - 1;
        uVar9 = (uint)uVar10;
      }
      uVar10 = 1;
      goto LAB_ffff00009079c9c0;
    }
    FUN_ffff00009077ee50(puVar20,0,(ulong)*(uint *)(param_1 + 4) << 1);
    uVar12 = 0;
    uVar10 = 1;
    *param_1 = 0;
  }
  iVar17 = (int)uVar12;
  iVar2 = iVar17;
  if (-1 < iVar17) {
    iVar2 = 0;
  }
  uVar8 = param_2 & 0xf;
  uVar5 = ~(-1 << (ulong)(param_2 & 0xf));
  uVar7 = 0x10 - uVar8;
  uVar9 = (iVar17 - iVar2) + 1;
  if (uVar9 < 2) {
    uVar15 = 0;
    uVar16 = uVar12;
  }
  else {
    uVar1 = uVar9 & 0xfffffffe;
    uVar15 = 0;
    uVar16 = (ulong)(iVar17 - uVar1);
    uVar19 = uVar1;
    do {
      uVar6 = (int)uVar12 - 1;
      iVar2 = uVar15 << (ulong)(uVar7 & 0x1f);
      uVar19 = uVar19 - 2;
      uVar3 = puVar20[uVar12];
      uVar4 = puVar20[uVar6];
      uVar15 = uVar4 & uVar5;
      puVar20[uVar12] = uVar3 >> (ulong)uVar8 | (ushort)iVar2;
      puVar20[uVar6] = uVar4 >> (ulong)uVar8 | (ushort)((uVar3 & uVar5) << (ulong)(uVar7 & 0x1f));
      uVar12 = (ulong)((int)uVar12 - 2);
    } while (uVar19 != 0);
    if (uVar9 == uVar1) goto LAB_ffff00009079ca9c;
  }
  do {
    iVar2 = uVar15 << (ulong)(uVar7 & 0x1f);
    uVar12 = uVar16 - 1;
    uVar15 = (ushort)puVar20[uVar16] & uVar5;
    puVar20[uVar16] = (ushort)puVar20[uVar16] >> (ulong)uVar8 | (ushort)iVar2;
    uVar16 = uVar12;
  } while (0 < (int)uVar12 + 1);
LAB_ffff00009079ca9c:
  if (1 < (uint)uVar10) {
    uVar10 = uVar10 & 0xffffffff;
    do {
      if (*(short *)(*(long *)(param_1 + 0x10) + -2 + uVar10 * 2) != 0) goto LAB_ffff00009079cacc;
      uVar10 = uVar10 - 1;
    } while (1 < (uint)uVar10);
    uVar10 = 1;
  }
LAB_ffff00009079cacc:
  *(int *)(param_1 + 8) = (int)uVar10;
  return;
}



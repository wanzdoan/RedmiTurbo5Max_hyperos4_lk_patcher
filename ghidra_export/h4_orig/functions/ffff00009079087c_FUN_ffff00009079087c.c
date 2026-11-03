// ffff00009079087c  size=356  FUN_ffff00009079087c

undefined8 FUN_ffff00009079087c(undefined8 param_1,long *param_2)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  uint uVar6;
  bool bVar7;
  undefined8 uVar8;
  long lVar9;
  long lVar10;
  uint uVar11;
  ulong uVar12;
  ulong uVar13;
  undefined4 *puVar14;
  ulong uVar15;
  uint uVar16;
  long lVar17;
  long lVar18;
  
  lVar17 = param_2[4];
  uVar11 = *(int *)(lVar17 + 0x20) + 0x3ffff;
  if (uVar11 < 0x40000) {
    *(undefined2 *)(*param_2 + 0x1c) = 0;
    return 0;
  }
  uVar16 = uVar11 >> 0x12;
  uVar8 = *(undefined8 *)(lVar17 + 0x28);
  lVar18 = param_2[3];
  *(ushort *)(*param_2 + 0x1c) = (ushort)(uVar11 >> 0x12);
  lVar9 = FUN_ffff00009077b334(uVar8);
  uVar11 = *(uint *)(lVar17 + 0x20);
  if (uVar16 < 2) {
    uVar16 = 1;
  }
  uVar12 = (ulong)uVar16;
  if (uVar16 < 2) {
    uVar13 = 0;
    lVar10 = lVar9;
  }
  else {
    uVar13 = uVar12 & 0x3ffe;
    uVar16 = uVar11 - 1;
    puVar14 = (undefined4 *)(lVar18 + 0x10);
    lVar10 = lVar9 + uVar13 * 0x40000;
    uVar11 = uVar11 + (int)uVar13 * -0x40000;
    uVar15 = uVar13;
    do {
      lVar1 = lVar9 + 0x40000;
      uVar6 = uVar16 - 0x40000;
      uVar2 = 0x3ffff;
      if (uVar16 + 1 < 0x40001) {
        uVar2 = uVar16;
      }
      uVar16 = uVar16 - 0x80000;
      uVar3 = 0x3ffff;
      if (0xfffbfffe < uVar16) {
        uVar3 = uVar6;
      }
      uVar15 = uVar15 - 2;
      puVar14[-1] = uVar2;
      *puVar14 = (int)lVar1;
      puVar14[-4] = (int)lVar9;
      puVar14[-3] = (int)((ulong)lVar9 >> 0x20);
      lVar9 = lVar9 + 0x80000;
      puVar14[3] = uVar3;
      puVar14[1] = (int)((ulong)lVar1 >> 0x20);
      puVar14 = puVar14 + 8;
    } while (uVar15 != 0);
    if (uVar13 == uVar12) goto LAB_ffff000090790998;
  }
  puVar14 = (undefined4 *)(lVar18 + uVar13 * 0x10);
  lVar9 = uVar12 - uVar13;
  do {
    iVar5 = uVar11 - 1;
    bVar7 = uVar11 < 0x40000;
    uVar11 = uVar11 - 0x40000;
    iVar4 = 0x3ffff;
    if (bVar7 || uVar11 == 0) {
      iVar4 = iVar5;
    }
    lVar9 = lVar9 + -1;
    *puVar14 = (int)lVar10;
    puVar14[1] = (int)((ulong)lVar10 >> 0x20);
    lVar10 = lVar10 + 0x40000;
    puVar14[3] = iVar4;
    puVar14 = puVar14 + 4;
  } while (lVar9 != 0);
LAB_ffff000090790998:
  if (*(int *)(param_2[4] + 8) == 1) {
    FUN_ffff000090707c50(*(undefined8 *)(lVar17 + 0x28));
  }
  else if (*(int *)(param_2[4] + 8) == 2) {
    FUN_ffff000090707c90();
  }
  else {
    FUN_ffff000090707c70();
  }
  return 0;
}



// ffff0000907c3a10  size=652  FUN_ffff0000907c3a10

void FUN_ffff0000907c3a10(long param_1,long param_2,long param_3,uint *param_4,long param_5)

{
  int iVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  undefined2 *puVar5;
  uint uVar6;
  int iVar7;
  uint uVar8;
  ulong uVar9;
  uint *puVar10;
  long lVar11;
  uint uVar12;
  int iVar13;
  long lVar14;
  long lVar15;
  long lVar16;
  int iVar17;
  int iVar18;
  ulong uVar19;
  long lVar20;
  long local_68;
  
  lVar20 = 4;
  if (*(int *)(param_5 + 0x14) != 0) {
    lVar20 = 0x18;
  }
  iVar3 = *(int *)(param_5 + 8);
  iVar4 = *(int *)(param_5 + lVar20);
  lVar20 = (long)iVar4;
  FUN_ffff00009077de54
            (s__s__entry_ffff0000907e70db,s_fill_rect_with_content_by_16bit__ffff0000907d8a25);
  uVar12 = (uint)((ulong)param_2 >> 0x20);
  iVar13 = (int)((ulong)param_3 >> 0x20);
  if ((int)uVar12 < iVar13) {
    lVar16 = param_2 >> 0x20;
    uVar8 = (uint)param_2;
    iVar7 = (int)param_3;
    uVar9 = (ulong)(int)uVar8;
    iVar1 = ~uVar12 + iVar4 + iVar4 * uVar8;
    lVar14 = ((long)iVar3 * 2 + lVar16 * -2 + -2) * lVar20 + (long)(int)uVar8 * -2 + param_1 + -2;
    local_68 = lVar16;
    do {
      if ((int)uVar8 < iVar7) {
        lVar11 = 0;
        puVar10 = param_4;
        lVar15 = lVar14;
        uVar19 = uVar9;
        iVar18 = iVar1;
        iVar17 = iVar4 * (~uVar8 + iVar3);
        do {
          iVar2 = *(int *)(param_5 + 0x10);
          if (iVar2 == 0x10e) {
            puVar5 = (undefined2 *)(param_1 + (local_68 + iVar17) * 2);
          }
          else if (iVar2 == 0xb4) {
            puVar5 = (undefined2 *)(lVar15 + (long)*(int *)(param_5 + 4) * 2);
          }
          else if (iVar2 == 0x5a) {
            puVar5 = (undefined2 *)(param_1 + (long)iVar18 * 2);
          }
          else {
            puVar5 = (undefined2 *)(param_1 + local_68 * lVar20 * 2 + uVar19 * 2);
          }
          param_4 = puVar10 + 1;
          uVar12 = *puVar10;
          if (*(int *)(param_5 + 0x2c) == 0xb) {
            uVar6 = (uVar12 & 0xf8) << 8 | uVar12 >> 0x13 & 0x1f;
          }
          else {
            uVar6 = uVar12 >> 8 & 0xf800 | uVar12 >> 3 & 0x1f;
          }
          uVar6 = uVar6 | uVar12 >> 5 & 0x7e0;
          *puVar5 = (short)uVar6;
          if ((lVar11 == 0 || uVar9 - (long)(iVar7 + -1) == lVar11) &&
              (local_68 == lVar16 || local_68 == param_3 + -0x100000000 >> 0x20)) {
            FUN_ffff00009077de54
                      (s__s__d__dst_addr__0x_08x__color_a_ffff0000907ed7ac,
                       s_fill_rect_with_content_by_16bit__ffff0000907d8a25,0xfe,uVar6,uVar12,
                       local_68,uVar19 & 0xffffffff);
          }
          uVar19 = uVar19 + 1;
          iVar18 = iVar18 + iVar4;
          lVar11 = lVar11 + -1;
          lVar15 = lVar15 + -2;
          iVar17 = iVar17 - iVar4;
          puVar10 = param_4;
        } while (uVar9 - (long)iVar7 != lVar11);
      }
      lVar14 = lVar14 + lVar20 * -2;
      local_68 = local_68 + 1;
      iVar1 = iVar1 + -1;
    } while ((int)local_68 != iVar13);
  }
  return;
}



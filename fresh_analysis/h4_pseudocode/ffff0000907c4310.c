
void FUN_ffff0000907c4310(long param_1,long param_2,long param_3,undefined2 *param_4,long param_5)

{
  int iVar1;
  undefined2 uVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  ulong uVar6;
  long lVar7;
  undefined2 *puVar8;
  int iVar9;
  int iVar10;
  int iVar11;
  long lVar12;
  undefined2 *puVar13;
  long lVar14;
  long lVar15;
  
  lVar15 = 4;
  if (*(int *)(param_5 + 0x14) != 0) {
    lVar15 = 0x18;
  }
  iVar4 = *(int *)(param_5 + lVar15);
  iVar3 = *(int *)(param_5 + 8);
  FUN_ffff00009077e11c
            (s__s__entry_ffff0000907e721f,s_fill_rect_with_content_by_16bit__ffff0000907f249b);
  iVar9 = (int)((ulong)param_2 >> 0x20);
  iVar10 = (int)((ulong)param_3 >> 0x20);
  iVar5 = iVar10 - iVar9;
  if (iVar5 != 0 && iVar9 <= iVar10) {
    iVar9 = (int)param_2;
    iVar10 = (int)param_3;
    uVar6 = param_2 >> 0x20;
    do {
      if (iVar9 < iVar10) {
        lVar14 = 0;
        lVar12 = param_1 + (long)iVar9 * -2 + -2 + (long)iVar4 * ((long)iVar3 * 2 + -2 + uVar6 * -2)
        ;
        lVar15 = (long)iVar9;
        iVar11 = iVar5 * iVar9;
        do {
          iVar1 = *(int *)(param_5 + 0x10);
          if (iVar1 == 0x10e) {
            lVar7 = lVar15 + (long)iVar4 * (long)(int)(iVar3 + ~(uint)uVar6);
LAB_ffff0000907c44fc:
            puVar8 = (undefined2 *)(param_1 + lVar7 * 2);
            puVar13 = param_4 + (long)iVar11 + uVar6;
          }
          else {
            puVar13 = param_4;
            if (iVar1 == 0xb4) {
              puVar8 = (undefined2 *)(lVar12 + (long)*(int *)(param_5 + 4) * 2);
              param_4 = param_4 + 1;
            }
            else {
              if (iVar1 == 0x5a) {
                lVar7 = (int)(iVar4 * ((uint)uVar6 + 1)) - lVar15;
                goto LAB_ffff0000907c44fc;
              }
              puVar8 = (undefined2 *)(param_1 + uVar6 * (long)iVar4 * 2 + lVar15 * 2);
              param_4 = param_4 + 1;
            }
          }
          uVar2 = *puVar13;
          *puVar8 = uVar2;
          if ((lVar14 == 0 || (long)(iVar10 + -1) - (long)iVar9 == lVar14) &&
              (uVar6 == param_2 >> 0x20 || uVar6 == param_3 + -0x100000000 >> 0x20)) {
            FUN_ffff00009077e11c
                      (s__s__d___show_logo_common_dst_add_ffff0000907dd7b7,
                       s_fill_rect_with_content_by_16bit__ffff0000907f249b,0x12f,uVar2,*puVar13,
                       uVar6 & 0xffffffff,param_2 + lVar14);
          }
          lVar15 = lVar15 + 1;
          lVar14 = lVar14 + 1;
          iVar11 = iVar11 + iVar5;
          lVar12 = lVar12 + -2;
        } while ((iVar9 - iVar10) + (int)lVar14 != 0);
      }
      uVar6 = uVar6 + 1;
    } while (uVar6 != param_3 >> 0x20);
  }
  return;
}




void FUN_ffff0000907c3a68
               (long param_1,long param_2,long param_3,uint *param_4,long param_5,int param_6)

{
  int iVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  uint uVar6;
  long lVar7;
  long lVar8;
  uint uVar9;
  int iVar10;
  int iVar11;
  uint *puVar12;
  long lVar13;
  long lVar14;
  long lVar15;
  long lVar16;
  ulong uVar17;
  ulong uVar18;
  int iVar19;
  
  lVar15 = 4;
  if (*(int *)(param_5 + 0x14) != 0) {
    lVar15 = 0x18;
  }
  iVar3 = *(int *)(param_5 + 8);
  iVar4 = *(int *)(param_5 + lVar15);
  lVar15 = (long)iVar4;
  FUN_ffff00009077e11c
            (s__s__entry_ffff0000907e721f,s_fill_rect_with_content_by_32bit__ffff0000907df215);
  uVar9 = (uint)((ulong)param_2 >> 0x20);
  iVar11 = (int)((ulong)param_3 >> 0x20);
  if ((int)uVar9 < iVar11) {
    uVar17 = param_2 >> 0x20;
    uVar6 = (uint)param_2;
    lVar7 = (long)(int)uVar6;
    iVar5 = (int)param_3;
    iVar1 = ~uVar9 + iVar4 + iVar4 * uVar6;
    lVar13 = ((long)iVar3 * 4 + uVar17 * -4 + -4) * lVar15 + (long)(int)uVar6 * -4 + param_1 + -4;
    uVar18 = uVar17;
    do {
      if ((int)uVar6 < iVar5) {
        lVar16 = 0;
        lVar8 = lVar7;
        lVar14 = lVar13;
        iVar19 = iVar1;
        iVar10 = iVar4 * (~uVar6 + iVar3);
        do {
          iVar2 = *(int *)(param_5 + 0x10);
          if (iVar2 == 0x10e) {
            puVar12 = (uint *)(param_1 + (uVar18 + (long)iVar10) * 4);
          }
          else if (iVar2 == 0xb4) {
            puVar12 = (uint *)(lVar14 + (long)*(int *)(param_5 + 4) * 4);
          }
          else if (iVar2 == 0x5a) {
            puVar12 = (uint *)(param_1 + (long)iVar19 * 4);
          }
          else {
            puVar12 = (uint *)(param_1 + uVar18 * lVar15 * 4 + lVar8 * 4);
          }
          if (*(int *)(param_5 + 0xc) == 0x20) {
            uVar9 = *param_4;
            if (param_6 == 0x20) {
              if (*(int *)(param_5 + 0x2c) == 0x10) {
                *puVar12 = uVar9 & 0xff000000 | uVar9 & 0xff00 | (uVar9 & 0xff) << 0x10 |
                           uVar9 >> 0x10 & 0xff;
              }
              else {
LAB_ffff0000907c3cc4:
                *puVar12 = uVar9;
              }
            }
            else {
              if (*(int *)(param_5 + 0x2c) == 0x10) {
                uVar9 = (uVar9 & 0x7e0) << 5 | (uVar9 & 0x1f) << 3 | (uVar9 >> 0xb & 0x1f) << 0x13 |
                        0xff000000;
                goto LAB_ffff0000907c3cc4;
              }
              *puVar12 = (uVar9 & 0x7e0) << 5 | uVar9 << 0x13 | uVar9 >> 8 & 0xf8 | 0xff000000;
            }
          }
          else {
            FUN_ffff00009077e11c
                      (s__s__d__not_support_bits_per_pixe_ffff0000907dd78f,
                       s_fill_point_buffer_ffff0000907d3c86,0x59);
          }
          if ((lVar16 == 0 || (iVar5 + -1) - lVar7 == lVar16) &&
              (uVar18 == uVar17 || uVar18 == param_3 + -0x100000000 >> 0x20)) {
            FUN_ffff00009077e11c
                      (s__s__d__dst_addr__0x_08x__color_a_ffff0000907ed92f,
                       s_fill_rect_with_content_by_32bit__ffff0000907df215,0x98,*puVar12,*param_4,
                       uVar18 & 0xffffffff,uVar6 + (int)lVar16);
          }
          param_4 = param_4 + 1;
          lVar8 = lVar8 + 1;
          iVar19 = iVar19 + iVar4;
          lVar16 = lVar16 + 1;
          lVar14 = lVar14 + -4;
          iVar10 = iVar10 - iVar4;
        } while (iVar5 - lVar7 != lVar16);
      }
      uVar18 = uVar18 + 1;
      lVar13 = lVar13 + lVar15 * -4;
      iVar1 = iVar1 + -1;
    } while ((int)uVar18 != iVar11);
  }
  return;
}



// ffff0000907c3d5c  size=808  FUN_ffff0000907c3d5c

void FUN_ffff0000907c3d5c
               (long param_1,long param_2,long param_3,ushort *param_4,long param_5,int param_6)

{
  int iVar1;
  ushort uVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  uint uVar6;
  ulong uVar7;
  int iVar8;
  long lVar9;
  int iVar10;
  int iVar11;
  long lVar12;
  ushort *puVar13;
  long lVar14;
  uint *puVar15;
  long lVar16;
  
  lVar14 = 4;
  if (*(int *)(param_5 + 0x14) != 0) {
    lVar14 = 0x18;
  }
  iVar4 = *(int *)(param_5 + lVar14);
  iVar3 = *(int *)(param_5 + 8);
  FUN_ffff00009077e11c
            (s__s__entry_ffff0000907e721f,s_fill_rect_with_content_by_32bit__ffff0000907f7894);
  iVar8 = (int)((ulong)param_2 >> 0x20);
  iVar10 = (int)((ulong)param_3 >> 0x20);
  iVar5 = iVar10 - iVar8;
  if (iVar5 != 0 && iVar8 <= iVar10) {
    iVar8 = (int)param_2;
    iVar10 = (int)param_3;
    uVar7 = param_2 >> 0x20;
    do {
      if (iVar8 < iVar10) {
        lVar16 = 0;
        lVar12 = param_1 + (long)iVar8 * -4 + -4 + (long)iVar4 * ((long)iVar3 * 4 + -4 + uVar7 * -4)
        ;
        lVar14 = (long)iVar8;
        iVar11 = iVar5 * iVar8;
        do {
          iVar1 = *(int *)(param_5 + 0x10);
          if (iVar1 == 0x10e) {
            lVar9 = lVar14 + (long)iVar4 * (long)(int)(iVar3 + ~(uint)uVar7);
LAB_ffff0000907c3f54:
            puVar15 = (uint *)(param_1 + lVar9 * 4);
            puVar13 = param_4 + (long)iVar11 + uVar7;
          }
          else {
            puVar13 = param_4;
            if (iVar1 == 0xb4) {
              puVar15 = (uint *)(lVar12 + (long)*(int *)(param_5 + 4) * 4);
              param_4 = param_4 + 1;
            }
            else {
              if (iVar1 == 0x5a) {
                lVar9 = (int)(iVar4 * ((uint)uVar7 + 1)) - lVar14;
                goto LAB_ffff0000907c3f54;
              }
              puVar15 = (uint *)(param_1 + uVar7 * (long)iVar4 * 4 + lVar14 * 4);
              param_4 = param_4 + 1;
            }
          }
          if (*(int *)(param_5 + 0xc) == 0x20) {
            uVar2 = *puVar13;
            uVar6 = (uint)uVar2;
            if (param_6 == 0x20) {
              if (*(int *)(param_5 + 0x2c) == 0x10) {
                uVar6 = CONCAT22(uVar2,uVar2) & 0xffff00;
              }
              *puVar15 = uVar6;
            }
            else if (*(int *)(param_5 + 0x2c) == 0x10) {
              *puVar15 = (uVar2 & 0x7e0) << 5 | (uVar6 & 0x1f) << 3 | (uint)(uVar2 >> 0xb) << 0x13 |
                         0xff000000;
            }
            else {
              *puVar15 = (uVar2 & 0x7e0) << 5 | uVar6 << 0x13 | uVar2 >> 8 & 0xf8 | 0xff000000;
            }
          }
          else {
            FUN_ffff00009077e11c
                      (s__s__d__not_support_bits_per_pixe_ffff0000907dd78f,
                       s_fill_point_buffer_ffff0000907d3c86,0x59);
          }
          if ((lVar16 == 0 || (long)(iVar10 + -1) - (long)iVar8 == lVar16) &&
              (uVar7 == param_2 >> 0x20 || uVar7 == param_3 + -0x100000000 >> 0x20)) {
            FUN_ffff00009077e11c
                      (s__s__d__dst_addr__0x_08x__color_a_ffff0000907ed92f,
                       s_fill_rect_with_content_by_32bit__ffff0000907f7894,0xc9,*puVar15,*puVar13,
                       uVar7 & 0xffffffff,param_2 + lVar16);
          }
          lVar14 = lVar14 + 1;
          lVar16 = lVar16 + 1;
          lVar12 = lVar12 + -4;
          iVar11 = iVar11 + iVar5;
        } while ((iVar8 - iVar10) + (int)lVar16 != 0);
      }
      uVar7 = uVar7 + 1;
    } while (uVar7 != param_3 >> 0x20);
  }
  return;
}



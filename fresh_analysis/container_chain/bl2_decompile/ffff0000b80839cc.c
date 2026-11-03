
void FUN_ffff0000b80839cc(long param_1,long param_2,long param_3,uint param_4,long param_5)

{
  int iVar1;
  int iVar2;
  int iVar3;
  uint *puVar4;
  uint *puVar5;
  int iVar6;
  long lVar7;
  int iVar8;
  
  lVar7 = 4;
  if (*(int *)(param_5 + 0x14) != 0) {
    lVar7 = 0x18;
  }
  iVar3 = *(int *)(param_5 + lVar7);
  iVar2 = *(int *)(param_5 + 8);
  FUN_ffff0000b804c33c
            (s__s__entry_ffff0000b80921b3,s_fill_rect_with_color_by_32bit_ffff0000b808f7a7);
  if ((int)((ulong)param_2 >> 0x20) < (int)((ulong)param_3 >> 0x20)) {
    iVar6 = (int)param_2;
    param_2 = param_2 >> 0x20;
    do {
      if (iVar6 < (int)param_3) {
        puVar5 = (uint *)(param_1 + (long)iVar6 * -4 + -4 + (long)iVar3 * 4 * (iVar2 - param_2));
        lVar7 = (long)iVar6;
        iVar8 = (int)param_3 - iVar6;
        do {
          iVar1 = *(int *)(param_5 + 0x10);
          if (iVar1 == 0x10e) {
            puVar4 = (uint *)(param_1 +
                             (lVar7 + (long)iVar3 * (long)(int)(iVar2 + ~(uint)param_2)) * 4);
          }
          else {
            puVar4 = puVar5;
            if (iVar1 != 0xb4) {
              if (iVar1 == 0x5a) {
                puVar4 = (uint *)(param_1 + ((int)(iVar3 * ((uint)param_2 + 1)) - lVar7) * 4);
              }
              else {
                puVar4 = (uint *)(param_1 + param_2 * iVar3 * 4 + lVar7 * 4);
              }
            }
          }
          if (*(int *)(param_5 + 0xc) == 0x20) {
            if (*(int *)(param_5 + 0x2c) == 0x10) {
              *puVar4 = param_4 & 0xff000000 | param_4 & 0xff00 | (param_4 & 0xff) << 0x10 |
                        param_4 >> 0x10 & 0xff;
            }
            else {
              *puVar4 = param_4;
            }
          }
          else {
            FUN_ffff0000b804c33c
                      (s__s__d__not_support_bits_per_pixe_ffff0000b808cd1d,
                       s_fill_point_buffer_ffff0000b8087cee,0x59);
          }
          lVar7 = lVar7 + 1;
          puVar5 = puVar5 + -1;
          iVar8 = iVar8 + -1;
        } while (iVar8 != 0);
      }
      param_2 = param_2 + 1;
    } while (param_2 != param_3 >> 0x20);
  }
  return;
}



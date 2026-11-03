
void FUN_ffff0000b8083de8
               (long param_1,ulong param_2,ulong param_3,undefined4 param_4,undefined8 *param_5)

{
  uint *puVar1;
  int iVar2;
  uint uVar3;
  int iVar4;
  long lVar5;
  long lVar6;
  undefined2 *puVar7;
  long lVar8;
  undefined2 *puVar9;
  int iVar10;
  int iVar11;
  int iVar12;
  int iVar13;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  FUN_ffff0000b804c33c(s__s__entry_ffff0000b80921b3,s_fill_rect_with_color_ffff0000b80921be);
  iVar10 = (int)param_3;
  iVar11 = (int)param_2;
  iVar12 = (int)(param_2 >> 0x20);
  iVar13 = (int)(param_3 >> 0x20);
  if ((iVar11 <= iVar10) && (iVar12 <= iVar13)) {
    if (*(int *)((long)param_5 + 0xc) == 0x20) {
      local_80 = param_5[2];
      uStack_78 = param_5[3];
      local_70 = param_5[4];
      uStack_68 = param_5[5];
      local_90 = *param_5;
      uStack_88 = param_5[1];
      FUN_ffff0000b80839cc(param_1,param_2,param_3,param_4,&local_90);
    }
    else {
      if (*(int *)((long)param_5 + 0xc) != 0x10) {
        FUN_ffff0000b804c33c
                  (s__s__d__unsupported_physical_scre_ffff0000b808b464,
                   s_fill_rect_with_color_ffff0000b80921be,0x1b2,
                   *(undefined4 *)((long)param_5 + 0x24));
        return;
      }
      iVar2 = *(int *)(param_5 + 2);
      iVar4 = *(int *)(param_5 + 1);
      puVar1 = (uint *)((long)param_5 + 4);
      if (*(int *)((long)param_5 + 0x14) != 0) {
        puVar1 = (uint *)(param_5 + 3);
      }
      uVar3 = *puVar1;
      FUN_ffff0000b804c33c
                (s__s__entry_ffff0000b80921b3,s_fill_rect_with_color_by_16bit_ffff0000b80888bd);
      if (iVar12 < iVar13) {
        lVar8 = (long)param_2 >> 0x20;
        do {
          if (iVar11 < iVar10) {
            puVar9 = (undefined2 *)
                     (param_1 + (long)iVar11 * 2 +
                     (-(ulong)(uVar3 >> 0x1f) & 0xfffffffe00000000 | (ulong)uVar3 << 1) * lVar8);
            lVar5 = (long)iVar10 - (long)iVar11;
            lVar6 = (long)iVar11;
            do {
              if (iVar2 == 0x10e) {
                puVar7 = (undefined2 *)
                         (param_1 + (lVar6 + (int)((iVar4 + ~(uint)lVar8) * uVar3)) * 2);
              }
              else if (iVar2 == 0xb4) {
                puVar7 = (undefined2 *)
                         (param_1 + -2 + (iVar4 - lVar8) * (long)(int)uVar3 * 2 + lVar6 * -2);
              }
              else {
                puVar7 = puVar9;
                if (iVar2 == 0x5a) {
                  puVar7 = (undefined2 *)(param_1 + ((int)(uVar3 * ((uint)lVar8 + 1)) - lVar6) * 2);
                }
              }
              lVar6 = lVar6 + 1;
              puVar9 = puVar9 + 1;
              lVar5 = lVar5 + -1;
              *puVar7 = (short)param_4;
            } while (lVar5 != 0);
          }
          lVar8 = lVar8 + 1;
        } while (lVar8 != (long)param_3 >> 0x20);
      }
    }
    return;
  }
  FUN_ffff0000b804c33c
            (s__s__d__drawing_width_or_height_i_ffff0000b8088894,s_check_rect_valid_ffff0000b808daa6
             ,0x66);
  FUN_ffff0000b804c33c
            (s__s__d__rect_left___d__right___d__ffff0000b809b2da,s_check_rect_valid_ffff0000b808daa6
             ,0x68,param_2 & 0xffffffff,param_3 & 0xffffffff,param_2 >> 0x20,param_3 >> 0x20);
  return;
}



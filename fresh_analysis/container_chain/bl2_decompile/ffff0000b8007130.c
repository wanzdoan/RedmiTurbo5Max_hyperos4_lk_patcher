
void FUN_ffff0000b8007130(int *param_1,uint param_2)

{
  uint uVar1;
  char *pcVar2;
  char *pcVar3;
  uint uVar4;
  int iVar5;
  uint uVar6;
  undefined1 auVar7 [16];
  long lVar8;
  bool bVar9;
  bool bVar10;
  int iVar11;
  int iVar12;
  ulong uVar13;
  ushort local_54 [2];
  
  uVar13 = (ulong)param_2;
  local_54[0] = 0;
  if (param_2 < 2) {
    if ((&DAT_ffff0000b80ad950)[uVar13 * 0x2e] == 0) {
      FUN_ffff0000b800f4dc
                (&DAT_ffff0000b80ad928 + uVar13 * 0xb8,
                 *(undefined4 *)(&DAT_ffff0000b80ad994 + uVar13 * 0xb8));
    }
    *(undefined4 *)((&DAT_ffff0000b80ad948)[uVar13 * 0x17] + 0xc) = 0xdf847;
  }
  else {
    FUN_ffff0000b804c33c
              (s__s__d__bat_idx__u__BUG__ffff0000b809c0f8,
               s_check_and_init_gauge_cust_data_ffff0000b8096e2d,0x80,uVar13);
  }
  lVar8 = uVar13 * 0xb8;
  uVar6 = *(uint *)(&DAT_ffff0000b80ad8f0 + lVar8);
  iVar5 = *(int *)(&DAT_ffff0000b80ad8ec + lVar8);
  iVar12 = *(int *)(&DAT_ffff0000b80ad8f4 + lVar8);
  if ((DAT_ffff0000b810c080 != 0) ||
     (DAT_ffff0000b810c080 = FUN_ffff0000b8020e20(s_mt6379_ffff0000b8094000),
     DAT_ffff0000b810c080 != 0)) {
    iVar11 = FUN_ffff0000b802127c
                       (DAT_ffff0000b810c080,*(undefined4 *)(&DAT_ffff0000b80ad984 + uVar13 * 0xb8),
                        local_54,2);
    if (iVar11 == 0) {
      bVar9 = (local_54[0] & 0x8000) == 0;
      pcVar2 = s_charging_ffff0000b809a638;
      if (!bVar9) {
        pcVar2 = s_discharging_ffff0000b80898ba;
      }
      uVar4 = (uint)local_54[0];
      if (!bVar9) {
        uVar4 = local_54[0] ^ 0xffff;
      }
      bVar10 = local_54[0] != 0;
      uVar1 = 0;
      if (bVar10) {
        uVar1 = uVar4;
      }
      auVar7 = SEXT816((long)(int)uVar1 * (long)iVar12) * SEXT816(0x29f16b11c6d1e109);
      uVar4 = *(uint *)(&DAT_ffff0000b80ad8f0 + lVar8);
      pcVar3 = s_discharging_ffff0000b80898ba;
      if (bVar10) {
        pcVar3 = pcVar2;
      }
      iVar12 = (int)(auVar7._8_8_ >> 0xe) - (auVar7._12_4_ >> 0x1f);
      (&DAT_ffff0000b80ad990)[uVar13 * 0xb8] = bVar10 && bVar9;
      if ((0x32 < uVar4) || ((1L << ((ulong)uVar4 & 0x3f) & 0x4000000100420U) == 0)) {
        iVar11 = iVar12 * 0x32;
        iVar12 = 0;
        if (uVar6 != 0) {
          iVar12 = iVar11 / (int)uVar6;
        }
      }
      FUN_ffff0000b804c33c
                (s__s__d____s__s__FG_CURRENT_0x_x_c_ffff0000b8085f12,
                 s_mt6379_read_im_current_ffff0000b8099846,0x17a,
                 (&PTR_s_mt6379_bat1_ffff0000b80ad8d0)[uVar13],pcVar3);
      *param_1 = (iVar12 * iVar5) / 1000;
    }
    else {
      FUN_ffff0000b804c33c
                (s__s__d__Failed_to_read__s_FG_IMP_ffff0000b80906e8,
                 s_mt6379_read_im_current_ffff0000b8099846,0x156,
                 (&PTR_s_mt6379_bat1_ffff0000b80ad8d0)[uVar13]);
    }
  }
  return;
}



// ffff000090707868  size=564  FUN_ffff000090707868

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090707868(int *param_1,uint param_2)

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
    if ((&DAT_ffff000090816940)[uVar13 * 0x2e] == 0) {
      FUN_ffff00009071f37c
                (&DAT_ffff000090816918 + uVar13 * 0xb8,
                 *(undefined4 *)(&DAT_ffff000090816984 + uVar13 * 0xb8));
    }
    *(undefined4 *)((&DAT_ffff000090816938)[uVar13 * 0x17] + 0xc) = 0xdf847;
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__bat_idx__u__BUG__ffff0000907f906b,
               s_check_and_init_gauge_cust_data_ffff0000907ef184,0x80,uVar13);
  }
  lVar8 = uVar13 * 0xb8;
  uVar6 = *(uint *)(&DAT_ffff0000908168e0 + lVar8);
  iVar5 = *(int *)(&DAT_ffff0000908168dc + lVar8);
  iVar12 = *(int *)(&DAT_ffff0000908168e4 + lVar8);
  if ((_DAT_ffff00009083d0f0 != 0) ||
     (_DAT_ffff00009083d0f0 = FUN_ffff000090736ed0(s_mt6379_ffff0000907ea40e),
     _DAT_ffff00009083d0f0 != 0)) {
    iVar11 = FUN_ffff00009073732c
                       (_DAT_ffff00009083d0f0,*(undefined4 *)(&DAT_ffff000090816974 + uVar13 * 0xb8)
                        ,local_54,2);
    if (iVar11 == 0) {
      bVar9 = (local_54[0] & 0x8000) == 0;
      pcVar2 = s_charging_ffff0000907f5aa7;
      if (!bVar9) {
        pcVar2 = s_discharging_ffff0000907d7226;
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
      uVar4 = *(uint *)(&DAT_ffff0000908168e0 + lVar8);
      pcVar3 = s_discharging_ffff0000907d7226;
      if (bVar10) {
        pcVar3 = pcVar2;
      }
      iVar12 = (int)(auVar7._8_8_ >> 0xe) - (auVar7._12_4_ >> 0x1f);
      (&DAT_ffff000090816980)[uVar13 * 0xb8] = bVar10 && bVar9;
      if ((0x32 < uVar4) || ((1L << ((ulong)uVar4 & 0x3f) & 0x4000000100420U) == 0)) {
        iVar11 = iVar12 * 0x32;
        iVar12 = 0;
        if (uVar6 != 0) {
          iVar12 = iVar11 / (int)uVar6;
        }
      }
      FUN_ffff00009077de54
                (s__s__d____s__s__FG_CURRENT_0x_x_c_ffff0000907d0942,
                 s_mt6379_read_im_current_ffff0000907f3cb8,0x17a,
                 (&PTR_s_mt6379_bat1_ffff0000908168c0)[uVar13],pcVar3);
      *param_1 = (iVar12 * iVar5) / 1000;
    }
    else {
      FUN_ffff00009077de54
                (s__s__d__Failed_to_read__s_FG_IMP_ffff0000907e3fdd,
                 s_mt6379_read_im_current_ffff0000907f3cb8,0x156,
                 (&PTR_s_mt6379_bat1_ffff0000908168c0)[uVar13]);
    }
  }
  return;
}



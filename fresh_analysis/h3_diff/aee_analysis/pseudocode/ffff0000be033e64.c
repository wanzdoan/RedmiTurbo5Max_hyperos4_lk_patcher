
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000be033e64(long param_1,long param_2)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  long lVar5;
  ulong uVar6;
  uint uVar7;
  uint uVar8;
  
  FUN_ffff0000be05fd98
            (s__s__d___MMUP___s____ffff0000be0bf9b4,s_mmup_crash_dump_ffff0000be0a3644,0xe5,
             s_mmup_crash_dump_ffff0000be0a3644);
  DAT_ffff0000be1217d0 = param_1;
  FUN_ffff0000be05fd98(s__MMUP__mmup_dump_ramdump___p_ffff0000be0a4a7b,param_1);
  lVar5 = FUN_ffff0000be05f1bc(0x4000);
  uVar2 = DAT_ffff0000be1217d8;
  if (lVar5 == 0) {
    FUN_ffff0000be05fd98
              (s__MMUP___s_output_buffer_allocate_ffff0000be0b67ab,
               s_mmup_crash_dump_ffff0000be0a3644);
    return 0;
  }
  uVar6 = (ulong)DAT_ffff0000be1217d8;
  FUN_ffff0000be05fd98(s_c1_rst_0x_08x_ffff0000be0bf960,_DAT_ffff000031a0d004);
  FUN_ffff0000be05fd98(s_c1_general_ctrl_0x_08x_ffff0000be0a5d36,_DAT_ffff000031a0d01c);
  FUN_ffff0000be05fd98(s_c1_cache_debug_0x_08x_ffff0000be0be53d,_DAT_ffff000031a70000);
  FUN_ffff0000be05fd98
            (s__s__d___MMUP__l1c_size___0x_x____ffff0000be0b54d9,s_mmup_do_l1cdump_ffff0000be0ab922,
             0x6e,uVar6,0xffff000031a50000);
  if (uVar2 == 0) {
    FUN_ffff0000be05fd98(s__MMUP__mmup_do_l1cdump_failed__l_ffff0000be0b67d4,0);
  }
  else {
    FUN_ffff0000be0607b0(DAT_ffff0000be1217d0,0xffff000031a50000,uVar6);
  }
  iVar3 = FUN_ffff0000be033a0c(lVar5);
  if (iVar3 == 0) {
    FUN_ffff0000be05fd98(s__MMUP__mmup_do_regdump_failed__r_ffff0000be0a4a99,0);
    uVar7 = uVar2;
  }
  else {
    FUN_ffff0000be0607b0(DAT_ffff0000be1217d0 + uVar6,lVar5,iVar3);
    uVar7 = iVar3 + uVar2;
  }
  iVar4 = FUN_ffff0000be033bc4(lVar5);
  if (iVar4 == 0) {
    FUN_ffff0000be05fd98(s__MMUP__mmup_do_tbufdump_failed__t_ffff0000be0a5d4e,0);
  }
  else {
    FUN_ffff0000be0607b0(DAT_ffff0000be1217d0 + (ulong)uVar7,lVar5,iVar4);
    uVar7 = iVar4 + uVar7;
  }
  if ((ulong)_DAT_ffff000031800024 - 0xffffce7ffffc < 0xffff000031860000) {
    uVar8 = *(uint *)(&DAT_ffff000031800018 + _DAT_ffff000031800024);
    if (0x200000 < uVar8) {
      FUN_ffff0000be05fd98
                (s__MMUP__error__s_dram_size_0x_x_i_ffff0000be0a3609,
                 s_mmup_do_dramdump_ffff0000be0aa576,uVar8);
      goto LAB_ffff0000be034064;
    }
    FUN_ffff0000be05fd98
              (s__s__d___MMUP__mmup_dump_sec_dump_ffff0000be0ae9aa,
               s_mmup_do_dramdump_ffff0000be0aa576,0xd3,DAT_ffff0000be1217f8);
    lVar1 = 0;
    if (param_2 != 0) {
      lVar1 = param_2 + (ulong)DAT_ffff0000be1217f8;
    }
    FUN_ffff0000be05fd98
              (s__s__d___MMUP__dram_size___0x_x____ffff0000be0bd3b1,
               s_mmup_do_dramdump_ffff0000be0aa576,0xd8,uVar8,lVar1);
    if ((uVar8 != 0) && (lVar1 != 0)) {
      FUN_ffff0000be0607b0(DAT_ffff0000be1217d0 + (ulong)uVar7,lVar1,uVar8);
      uVar7 = (uVar8 + 3 & 0x7ffffc) + uVar7;
      goto LAB_ffff0000be034078;
    }
  }
  else {
    FUN_ffff0000be05fd98
              (s__MMUP__error__s_mmup_region_info_ffff0000be0bf96f,
               s_mmup_do_dramdump_ffff0000be0aa576);
LAB_ffff0000be034064:
    uVar8 = 0;
  }
  FUN_ffff0000be05fd98(s__MMUP__mmup_do_dramdump_failed__d_ffff0000be0ad917,uVar8);
LAB_ffff0000be034078:
  FUN_ffff0000be05fd98
            (s__MMUP___s_size__l2tcm_0x_x__l1c__ffff0000be0b424d,s_mmup_crash_dump_ffff0000be0a3644,
             0,uVar2,iVar3,iVar4,uVar8);
  FUN_ffff0000be05fd98(s__MMUP__mmup_dump_accumulate_size_ffff0000be0b203b,uVar7);
  thunk_FUN_ffff0000be09937c(lVar5);
  return uVar7;
}



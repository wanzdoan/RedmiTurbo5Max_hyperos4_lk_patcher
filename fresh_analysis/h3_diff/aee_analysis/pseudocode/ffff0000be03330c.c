
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000be03330c(long param_1)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  long lVar6;
  ulong uVar7;
  uint uVar8;
  uint uVar9;
  
  FUN_ffff0000be05fd98
            (s__s__d___VCP___s____ffff0000be0b9e2d,s_vcp_crash_dump_ffff0000be0bc0ba,0x175,
             s_vcp_crash_dump_ffff0000be0bc0ba);
  DAT_ffff0000be121780 = param_1;
  FUN_ffff0000be05fd98(s__VCP__vcp_dump_ramdump___p_ffff0000be0bd395,param_1);
  lVar6 = FUN_ffff0000be05f1bc(0x4000);
  uVar3 = DAT_ffff0000be121788;
  if (lVar6 == 0) {
    FUN_ffff0000be05fd98
              (s__VCP___s_output_buffer_allocate_f_ffff0000be0a4a28,
               s_vcp_crash_dump_ffff0000be0bc0ba);
    return 0;
  }
  uVar7 = (ulong)DAT_ffff0000be121788;
  FUN_ffff0000be05fd98
            (s__s__d___VCP___s_l2tcm_size___0x__ffff0000be0b54a7,s_vcp_do_l2tcmdump_ffff0000be0a812a
             ,0x81,s_vcp_do_l2tcmdump_ffff0000be0a812a,uVar7,0xffff000031800000);
  if (uVar3 == 0) {
    FUN_ffff0000be05fd98(s__VCP__vcp_do_l2tcmdump_failed__l_ffff0000be0b0e25,0);
  }
  else {
    FUN_ffff0000be0607b0(DAT_ffff0000be121780,0xffff000031800000,uVar7);
  }
  iVar2 = DAT_ffff0000be121790;
  uVar8 = DAT_ffff0000be121788;
  iVar4 = DAT_ffff0000be121790 - DAT_ffff0000be121788;
  FUN_ffff0000be05fd98(s_c0_rst_0x_08x_ffff0000be0bc07f,_DAT_ffff000031a0a004);
  FUN_ffff0000be05fd98(s_c0_general_ctrl_0x_08x_ffff0000be0a91ae,_DAT_ffff000031a0a01c);
  FUN_ffff0000be05fd98(s_c0_cache_debug_0x_08x_ffff0000be0b8bd3,_DAT_ffff000031a70000);
  FUN_ffff0000be05fd98
            (s__s__d___VCP__l1c_size___0x_x___d_ffff0000be0ab8df,s_vcp_do_l1cdump_ffff0000be0bf951,
             0x90,iVar4,0xffff000031a30000);
  iVar2 = iVar2 - uVar8;
  if (iVar2 == 0) {
    FUN_ffff0000be05fd98(s__VCP__vcp_do_l1cdump_failed__l1c_ffff0000be0c2ecd,0);
    uVar8 = uVar3;
  }
  else {
    FUN_ffff0000be0607b0(DAT_ffff0000be121780 + uVar7,0xffff000031a30000,iVar2);
    uVar8 = iVar2 + uVar3;
  }
  iVar4 = FUN_ffff0000be032f10(lVar6);
  if (iVar4 == 0) {
    FUN_ffff0000be05fd98(s__VCP__vcp_do_regdump_failed__reg_ffff0000be0a5d0a,0);
  }
  else {
    FUN_ffff0000be0607b0(DAT_ffff0000be121780 + (ulong)uVar8,lVar6,iVar4);
    uVar8 = iVar4 + uVar8;
  }
  iVar5 = FUN_ffff0000be0330c8(lVar6);
  if (iVar5 == 0) {
    FUN_ffff0000be05fd98(s__VCP__vcp_do_tbufdump_failed__tb_ffff0000be0ac992,0);
  }
  else {
    FUN_ffff0000be0607b0(DAT_ffff0000be121780 + (ulong)uVar8,lVar6,iVar5);
    uVar8 = iVar5 + uVar8;
  }
  FUN_ffff0000be05fd98
            (s__s__d___VCP__actual_dram_size___0_ffff0000be0c0b40,s_vcp_do_dramdump_ffff0000be0bd385
             ,0x156,_DAT_ffff000031800018);
  uVar9 = DAT_ffff0000be1217a8 - DAT_ffff0000be1217a0;
  if (uVar9 < 0x200001) {
    FUN_ffff0000be05fd98
              (s__s__d___VCP__vcp_dump_sec_dump_o_ffff0000be0bc08e,
               s_vcp_do_dramdump_ffff0000be0bd385,0x163,DAT_ffff0000be1217b0);
    lVar1 = 0;
    if (DAT_ffff0000be121780 != 0) {
      lVar1 = DAT_ffff0000be121780 + (ulong)DAT_ffff0000be1217b0;
    }
    FUN_ffff0000be05fd98
              (s__s__d___VCP__dram_size___0x_x____ffff0000be0b9e6c,
               s_vcp_do_dramdump_ffff0000be0bd385,0x168,uVar9,lVar1);
    if ((uVar9 != 0) && (lVar1 != 0)) {
      FUN_ffff0000be0607b0(DAT_ffff0000be121780 + (ulong)uVar8,lVar1,uVar9);
      uVar8 = (uVar9 + 3 & 0x7ffffc) + uVar8;
      goto LAB_ffff0000be033618;
    }
  }
  else {
    FUN_ffff0000be05fd98
              (s__VCP__error__s_dram_size_0x_x_is_ffff0000be0c5388,
               s_vcp_do_dramdump_ffff0000be0bd385,uVar9);
    uVar9 = 0;
  }
  FUN_ffff0000be05fd98(s__VCP__vcp_do_dramdump_failed__dr_ffff0000be0b30d5,uVar9);
LAB_ffff0000be033618:
  FUN_ffff0000be05fd98
            (s__VCP___s_size__l2tcm_0x_x__l1c_0_ffff0000be0b9e9b,s_vcp_crash_dump_ffff0000be0bc0ba,
             uVar3,iVar2,iVar4,iVar5,uVar9);
  FUN_ffff0000be05fd98(s__VCP__vcp_dump_accumulate_size__0_ffff0000be0b9ee0,uVar8);
  iVar4 = FUN_ffff0000be033e64(DAT_ffff0000be121780 + (ulong)uVar8);
  FUN_ffff0000be05fd98(s__VCP__dump_total_size_0x_x_ffff0000be0afd7f,iVar4 + uVar8);
  _DAT_ffff000031a70000 = 0;
  thunk_FUN_ffff0000be09937c(lVar6);
  return iVar4 + uVar8;
}



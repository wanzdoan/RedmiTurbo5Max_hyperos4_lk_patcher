
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000b802a13c(long param_1)

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
  
  FUN_ffff0000b804c33c
            (s__s__d___VCP___s____ffff0000b809733e,s_vcp_crash_dump_ffff0000b8098f1b,0x175,
             s_vcp_crash_dump_ffff0000b8098f1b);
  DAT_ffff0000b8124f98 = param_1;
  FUN_ffff0000b804c33c(s__VCP__vcp_dump_ramdump___p_ffff0000b8099d8f,param_1);
  lVar6 = FUN_ffff0000b804bccc(0x4000);
  uVar3 = DAT_ffff0000b8124fa0;
  if (lVar6 == 0) {
    FUN_ffff0000b804c33c
              (s__VCP___s_output_buffer_allocate_f_ffff0000b8086566,
               s_vcp_crash_dump_ffff0000b8098f1b);
    return 0;
  }
  uVar7 = (ulong)DAT_ffff0000b8124fa0;
  FUN_ffff0000b804c33c
            (s__s__d___VCP___s_l2tcm_size___0x__ffff0000b80936d4,s_vcp_do_l2tcmdump_ffff0000b808900f
             ,0x81,s_vcp_do_l2tcmdump_ffff0000b808900f,uVar7,0xffff000031800000);
  if (uVar3 == 0) {
    FUN_ffff0000b804c33c(s__VCP__vcp_do_l2tcmdump_failed__l_ffff0000b808fe01,0);
  }
  else {
    FUN_ffff0000b804cd40(DAT_ffff0000b8124f98,0xffff000031800000,uVar7);
  }
  iVar2 = DAT_ffff0000b8124fa8;
  uVar8 = DAT_ffff0000b8124fa0;
  iVar4 = DAT_ffff0000b8124fa8 - DAT_ffff0000b8124fa0;
  FUN_ffff0000b804c33c(s_c0_rst_0x_08x_ffff0000b8098ee0,_DAT_ffff000031a0a004);
  FUN_ffff0000b804c33c(s_c0_general_ctrl_0x_08x_ffff0000b8089cfa,_DAT_ffff000031a0a01c);
  FUN_ffff0000b804c33c(s_c0_cache_debug_0x_08x_ffff0000b8096541,_DAT_ffff000031a70000);
  FUN_ffff0000b804c33c
            (s__s__d___VCP__l1c_size___0x_x___d_ffff0000b808ba53,s_vcp_do_l1cdump_ffff0000b809b9c1,
             0x90,iVar4,0xffff000031a30000);
  iVar2 = iVar2 - uVar8;
  if (iVar2 == 0) {
    FUN_ffff0000b804c33c(s__VCP__vcp_do_l1cdump_failed__l1c_ffff0000b809e7ef,0);
    uVar8 = uVar3;
  }
  else {
    FUN_ffff0000b804cd40(DAT_ffff0000b8124f98 + uVar7,0xffff000031a30000,iVar2);
    uVar8 = iVar2 + uVar3;
  }
  iVar4 = FUN_ffff0000b8029d40(lVar6);
  if (iVar4 == 0) {
    FUN_ffff0000b804c33c(s__VCP__vcp_do_regdump_failed__reg_ffff0000b808739b,0);
  }
  else {
    FUN_ffff0000b804cd40(DAT_ffff0000b8124f98 + (ulong)uVar8,lVar6,iVar4);
    uVar8 = iVar4 + uVar8;
  }
  iVar5 = FUN_ffff0000b8029ef8(lVar6);
  if (iVar5 == 0) {
    FUN_ffff0000b804c33c(s__VCP__vcp_do_tbufdump_failed__tb_ffff0000b808c805,0);
  }
  else {
    FUN_ffff0000b804cd40(DAT_ffff0000b8124f98 + (ulong)uVar8,lVar6,iVar5);
    uVar8 = iVar5 + uVar8;
  }
  FUN_ffff0000b804c33c
            (s__s__d___VCP__actual_dram_size___0_ffff0000b809c8fb,s_vcp_do_dramdump_ffff0000b8099d7f
             ,0x156,_DAT_ffff000031800018);
  uVar9 = DAT_ffff0000b8124fc0 - DAT_ffff0000b8124fb8;
  if (uVar9 < 0x200001) {
    FUN_ffff0000b804c33c
              (s__s__d___VCP__vcp_dump_sec_dump_o_ffff0000b8098eef,
               s_vcp_do_dramdump_ffff0000b8099d7f,0x163,DAT_ffff0000b8124fc8);
    lVar1 = 0;
    if (DAT_ffff0000b8124f98 != 0) {
      lVar1 = DAT_ffff0000b8124f98 + (ulong)DAT_ffff0000b8124fc8;
    }
    FUN_ffff0000b804c33c
              (s__s__d___VCP__dram_size___0x_x____ffff0000b809737d,
               s_vcp_do_dramdump_ffff0000b8099d7f,0x168,uVar9,lVar1);
    if ((uVar9 != 0) && (lVar1 != 0)) {
      FUN_ffff0000b804cd40(DAT_ffff0000b8124f98 + (ulong)uVar8,lVar1,uVar9);
      uVar8 = (uVar9 + 3 & 0x7ffffc) + uVar8;
      goto LAB_ffff0000b802a448;
    }
  }
  else {
    FUN_ffff0000b804c33c
              (s__VCP__error__s_dram_size_0x_x_is_ffff0000b80a055a,
               s_vcp_do_dramdump_ffff0000b8099d7f,uVar9);
    uVar9 = 0;
  }
  FUN_ffff0000b804c33c(s__VCP__vcp_do_dramdump_failed__dr_ffff0000b8091a3f,uVar9);
LAB_ffff0000b802a448:
  FUN_ffff0000b804c33c
            (s__VCP___s_size__l2tcm_0x_x__l1c_0_ffff0000b80973ac,s_vcp_crash_dump_ffff0000b8098f1b,
             uVar3,iVar2,iVar4,iVar5,uVar9);
  FUN_ffff0000b804c33c(s__VCP__vcp_dump_accumulate_size__0_ffff0000b80973f1,uVar8);
  iVar4 = FUN_ffff0000b802a9ac(DAT_ffff0000b8124f98 + (ulong)uVar8);
  FUN_ffff0000b804c33c(s__VCP__dump_total_size_0x_x_ffff0000b808f046,iVar4 + uVar8);
  _DAT_ffff000031a70000 = 0;
  thunk_FUN_ffff0000b8082558(lVar6);
  return iVar4 + uVar8;
}



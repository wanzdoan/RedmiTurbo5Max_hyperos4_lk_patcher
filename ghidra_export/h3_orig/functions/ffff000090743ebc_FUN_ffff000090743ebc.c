// ffff000090743ebc  size=916  FUN_ffff000090743ebc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090743ebc(long param_1)

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
  
  FUN_ffff00009077de54
            (s__s__d___VCP___s____ffff0000907efbff,s_vcp_crash_dump_ffff0000907f2e16,0x175,
             s_vcp_crash_dump_ffff0000907f2e16);
  _DAT_ffff000090877468 = param_1;
  FUN_ffff00009077de54(s__VCP__vcp_dump_ramdump___p_ffff0000907f4ac5,param_1);
  lVar6 = FUN_ffff00009077d24c(0x4000);
  uVar3 = _DAT_ffff000090877470;
  if (lVar6 == 0) {
    FUN_ffff00009077de54
              (s__VCP___s_output_buffer_allocate_f_ffff0000907d1473,
               s_vcp_crash_dump_ffff0000907f2e16);
    return 0;
  }
  uVar7 = (ulong)_DAT_ffff000090877470;
  FUN_ffff00009077de54
            (s__s__d___VCP___s_l2tcm_size___0x__ffff0000907e95da,s_vcp_do_l2tcmdump_ffff0000907d6361
             ,0x81,s_vcp_do_l2tcmdump_ffff0000907d6361,uVar7,0xffff000031800000);
  if (uVar3 == 0) {
    FUN_ffff00009077de54(s__VCP__vcp_do_l2tcmdump_failed__l_ffff0000907e2fff,0);
  }
  else {
    FUN_ffff00009077e858(_DAT_ffff000090877468,0xffff000031800000,uVar7);
  }
  iVar2 = _DAT_ffff000090877478;
  uVar8 = _DAT_ffff000090877470;
  iVar4 = _DAT_ffff000090877478 - _DAT_ffff000090877470;
  FUN_ffff00009077de54(s_c0_rst_0x_08x_ffff0000907f2ddb,_DAT_ffff000031a0a004);
  FUN_ffff00009077de54(s_c0_general_ctrl_0x_08x_ffff0000907d7bd1,_DAT_ffff000031a0a01c);
  FUN_ffff00009077de54(s_c0_cache_debug_0x_08x_ffff0000907ee353,_DAT_ffff000031a70000);
  FUN_ffff00009077de54
            (s__s__d___VCP__l1c_size___0x_x___d_ffff0000907daf33,s_vcp_do_l1cdump_ffff0000907f836c,
             0x90,iVar4,0xffff000031a30000);
  iVar2 = iVar2 - uVar8;
  if (iVar2 == 0) {
    FUN_ffff00009077de54(s__VCP__vcp_do_l1cdump_failed__l1c_ffff0000907fd635,0);
    uVar8 = uVar3;
  }
  else {
    FUN_ffff00009077e858(_DAT_ffff000090877468 + uVar7,0xffff000031a30000,iVar2);
    uVar8 = iVar2 + uVar3;
  }
  iVar4 = FUN_ffff000090743ac0(lVar6);
  if (iVar4 == 0) {
    FUN_ffff00009077de54(s__VCP__vcp_do_regdump_failed__reg_ffff0000907d2f1e,0);
  }
  else {
    FUN_ffff00009077e858(_DAT_ffff000090877468 + (ulong)uVar8,lVar6,iVar4);
    uVar8 = iVar4 + uVar8;
  }
  iVar5 = FUN_ffff000090743c78(lVar6);
  if (iVar5 == 0) {
    FUN_ffff00009077de54(s__VCP__vcp_do_tbufdump_failed__tb_ffff0000907dc9a5,0);
  }
  else {
    FUN_ffff00009077e858(_DAT_ffff000090877468 + (ulong)uVar8,lVar6,iVar5);
    uVar8 = iVar5 + uVar8;
  }
  FUN_ffff00009077de54
            (s__s__d___VCP__actual_dram_size___0_ffff0000907f9de5,s_vcp_do_dramdump_ffff0000907f4ab5
             ,0x156,_DAT_ffff000031800018);
  uVar9 = _DAT_ffff000090877490 - _DAT_ffff000090877488;
  if (uVar9 < 0x200001) {
    FUN_ffff00009077de54
              (s__s__d___VCP__vcp_dump_sec_dump_o_ffff0000907f2dea,
               s_vcp_do_dramdump_ffff0000907f4ab5,0x163,_DAT_ffff000090877498);
    lVar1 = 0;
    if (_DAT_ffff000090877468 != 0) {
      lVar1 = _DAT_ffff000090877468 + (ulong)_DAT_ffff000090877498;
    }
    FUN_ffff00009077de54
              (s__s__d___VCP__dram_size___0x_x____ffff0000907efc3e,
               s_vcp_do_dramdump_ffff0000907f4ab5,0x168,uVar9,lVar1);
    if ((uVar9 != 0) && (lVar1 != 0)) {
      FUN_ffff00009077e858(_DAT_ffff000090877468 + (ulong)uVar8,lVar1,uVar9);
      uVar8 = (uVar9 + 3 & 0x7ffffc) + uVar8;
      goto LAB_ffff0000907441c8;
    }
  }
  else {
    FUN_ffff00009077de54
              (s__VCP__error__s_dram_size_0x_x_is_ffff000090800c70,
               s_vcp_do_dramdump_ffff0000907f4ab5,uVar9);
    uVar9 = 0;
  }
  FUN_ffff00009077de54(s__VCP__vcp_do_dramdump_failed__dr_ffff0000907e63f2,uVar9);
LAB_ffff0000907441c8:
  FUN_ffff00009077de54
            (s__VCP___s_size__l2tcm_0x_x__l1c_0_ffff0000907efc6d,s_vcp_crash_dump_ffff0000907f2e16,
             uVar3,iVar2,iVar4,iVar5,uVar9);
  FUN_ffff00009077de54(s__VCP__vcp_dump_accumulate_size__0_ffff0000907efcb2,uVar8);
  iVar4 = FUN_ffff0000907448a4(_DAT_ffff000090877468 + (ulong)uVar8);
  FUN_ffff00009077de54(s__VCP__dump_total_size_0x_x_ffff0000907e1761,iVar4 + uVar8);
  _DAT_ffff000031a70000 = 0;
  thunk_FUN_ffff0000907c2ad8(lVar6);
  return iVar4 + uVar8;
}



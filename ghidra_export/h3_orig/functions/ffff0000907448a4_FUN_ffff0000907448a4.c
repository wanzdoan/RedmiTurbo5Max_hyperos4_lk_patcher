// ffff0000907448a4  size=756  FUN_ffff0000907448a4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000907448a4(long param_1,long param_2)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  long lVar5;
  ulong uVar6;
  uint uVar7;
  uint uVar8;
  
  FUN_ffff00009077de54
            (s__s__d___MMUP___s____ffff0000907f83cf,s_mmup_crash_dump_ffff0000907cfa1e,0xe5,
             s_mmup_crash_dump_ffff0000907cfa1e);
  _DAT_ffff0000908774b8 = param_1;
  FUN_ffff00009077de54(s__MMUP__mmup_dump_ramdump___p_ffff0000907d149b,param_1);
  lVar5 = FUN_ffff00009077d24c(0x4000);
  uVar2 = _DAT_ffff0000908774c0;
  if (lVar5 == 0) {
    FUN_ffff00009077de54
              (s__MMUP___s_output_buffer_allocate_ffff0000907eaece,
               s_mmup_crash_dump_ffff0000907cfa1e);
    return 0;
  }
  uVar6 = (ulong)_DAT_ffff0000908774c0;
  FUN_ffff00009077de54(s_c1_rst_0x_08x_ffff0000907f837b,_DAT_ffff000031a0d004);
  FUN_ffff00009077de54(s_c1_general_ctrl_0x_08x_ffff0000907d2f4a,_DAT_ffff000031a0d01c);
  FUN_ffff00009077de54(s_c1_cache_debug_0x_08x_ffff0000907f65b6,_DAT_ffff000031a70000);
  FUN_ffff00009077de54
            (s__s__d___MMUP__l1c_size___0x_x____ffff0000907e960c,s_mmup_do_l1cdump_ffff0000907daf60,
             0x6e,uVar6,0xffff000031a50000);
  if (uVar2 == 0) {
    FUN_ffff00009077de54(s__MMUP__mmup_do_l1cdump_failed__l_ffff0000907eaef7,0);
  }
  else {
    FUN_ffff00009077e858(_DAT_ffff0000908774b8,0xffff000031a50000,uVar6);
  }
  iVar3 = FUN_ffff00009074444c(lVar5);
  if (iVar3 == 0) {
    FUN_ffff00009077de54(s__MMUP__mmup_do_regdump_failed__r_ffff0000907d14b9,0);
    uVar7 = uVar2;
  }
  else {
    FUN_ffff00009077e858(_DAT_ffff0000908774b8 + uVar6,lVar5,iVar3);
    uVar7 = iVar3 + uVar2;
  }
  iVar4 = FUN_ffff000090744604(lVar5);
  if (iVar4 == 0) {
    FUN_ffff00009077de54(s__MMUP__mmup_do_tbufdump_failed__t_ffff0000907d2f62,0);
  }
  else {
    FUN_ffff00009077e858(_DAT_ffff0000908774b8 + (ulong)uVar7,lVar5,iVar4);
    uVar7 = iVar4 + uVar7;
  }
  if ((ulong)_DAT_ffff000031800024 - 0xffffce7ffffc < 0xffff000031860000) {
    uVar8 = *(uint *)(&DAT_ffff000031800018 + _DAT_ffff000031800024);
    if (0x200000 < uVar8) {
      FUN_ffff00009077de54
                (s__MMUP__error__s_dram_size_0x_x_i_ffff0000907cf9e3,
                 s_mmup_do_dramdump_ffff0000907d946c,uVar8);
      goto LAB_ffff000090744aa4;
    }
    FUN_ffff00009077de54
              (s__s__d___MMUP__mmup_dump_sec_dump_ffff0000907dfbd1,
               s_mmup_do_dramdump_ffff0000907d946c,0xd3,_DAT_ffff0000908774d8);
    lVar1 = 0;
    if (param_2 != 0) {
      lVar1 = param_2 + (ulong)_DAT_ffff0000908774d8;
    }
    FUN_ffff00009077de54
              (s__s__d___MMUP__dram_size___0x_x____ffff0000907f4ae1,
               s_mmup_do_dramdump_ffff0000907d946c,0xd8,uVar8,lVar1);
    if ((uVar8 != 0) && (lVar1 != 0)) {
      FUN_ffff00009077e858(_DAT_ffff0000908774b8 + (ulong)uVar7,lVar1,uVar8);
      uVar7 = (uVar8 + 3 & 0x7ffffc) + uVar7;
      goto LAB_ffff000090744ab8;
    }
  }
  else {
    FUN_ffff00009077de54
              (s__MMUP__error__s_mmup_region_info_ffff0000907f838a,
               s_mmup_do_dramdump_ffff0000907d946c);
LAB_ffff000090744aa4:
    uVar8 = 0;
  }
  FUN_ffff00009077de54(s__MMUP__mmup_do_dramdump_failed__d_ffff0000907de342,uVar8);
LAB_ffff000090744ab8:
  FUN_ffff00009077de54
            (s__MMUP___s_size__l2tcm_0x_x__l1c__ffff0000907e7b59,s_mmup_crash_dump_ffff0000907cfa1e,
             0,uVar2,iVar3,iVar4,uVar8);
  FUN_ffff00009077de54(s__MMUP__mmup_dump_accumulate_size_ffff0000907e4c4e,uVar7);
  thunk_FUN_ffff0000907c2ad8(lVar5);
  return uVar7;
}



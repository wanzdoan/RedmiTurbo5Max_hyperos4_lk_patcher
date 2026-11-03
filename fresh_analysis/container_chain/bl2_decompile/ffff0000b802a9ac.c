
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000b802a9ac(long param_1,long param_2)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  long lVar5;
  ulong uVar6;
  uint uVar7;
  uint uVar8;
  
  FUN_ffff0000b804c33c
            (s__s__d___MMUP___s____ffff0000b809ba24,s_mmup_crash_dump_ffff0000b8085533,0xe5,
             s_mmup_crash_dump_ffff0000b8085533);
  DAT_ffff0000b8124fe0 = param_1;
  FUN_ffff0000b804c33c(s__MMUP__mmup_dump_ramdump___p_ffff0000b808658e,param_1);
  lVar5 = FUN_ffff0000b804bccc(0x4000);
  uVar2 = DAT_ffff0000b8124fe8;
  if (lVar5 == 0) {
    FUN_ffff0000b804c33c
              (s__MMUP___s_output_buffer_allocate_ffff0000b8094772,
               s_mmup_crash_dump_ffff0000b8085533);
    return 0;
  }
  uVar6 = (ulong)DAT_ffff0000b8124fe8;
  FUN_ffff0000b804c33c(s_c1_rst_0x_08x_ffff0000b809b9d0,_DAT_ffff000031a0d004);
  FUN_ffff0000b804c33c(s_c1_general_ctrl_0x_08x_ffff0000b80873c7,_DAT_ffff000031a0d01c);
  FUN_ffff0000b804c33c(s_c1_cache_debug_0x_08x_ffff0000b809aac2,_DAT_ffff000031a70000);
  FUN_ffff0000b804c33c
            (s__s__d___MMUP__l1c_size___0x_x____ffff0000b8093706,s_mmup_do_l1cdump_ffff0000b808ba80,
             0x6e,uVar6,0xffff000031a50000);
  if (uVar2 == 0) {
    FUN_ffff0000b804c33c(s__MMUP__mmup_do_l1cdump_failed__l_ffff0000b809479b,0);
  }
  else {
    FUN_ffff0000b804cd40(DAT_ffff0000b8124fe0,0xffff000031a50000,uVar6);
  }
  iVar3 = FUN_ffff0000b802a554(lVar5);
  if (iVar3 == 0) {
    FUN_ffff0000b804c33c(s__MMUP__mmup_do_regdump_failed__r_ffff0000b80865ac,0);
    uVar7 = uVar2;
  }
  else {
    FUN_ffff0000b804cd40(DAT_ffff0000b8124fe0 + uVar6,lVar5,iVar3);
    uVar7 = iVar3 + uVar2;
  }
  iVar4 = FUN_ffff0000b802a70c(lVar5);
  if (iVar4 == 0) {
    FUN_ffff0000b804c33c(s__MMUP__mmup_do_tbufdump_failed__t_ffff0000b80873df,0);
  }
  else {
    FUN_ffff0000b804cd40(DAT_ffff0000b8124fe0 + (ulong)uVar7,lVar5,iVar4);
    uVar7 = iVar4 + uVar7;
  }
  if ((ulong)_DAT_ffff000031800024 - 0xffffce7ffffc < 0xffff000031860000) {
    uVar8 = *(uint *)(&DAT_ffff000031800018 + _DAT_ffff000031800024);
    if (0x200000 < uVar8) {
      FUN_ffff0000b804c33c
                (s__MMUP__error__s_dram_size_0x_x_i_ffff0000b80854f8,
                 s_mmup_do_dramdump_ffff0000b808ab5d,uVar8);
      goto LAB_ffff0000b802abac;
    }
    FUN_ffff0000b804c33c
              (s__s__d___MMUP__mmup_dump_sec_dump_ffff0000b808e0c2,
               s_mmup_do_dramdump_ffff0000b808ab5d,0xd3,DAT_ffff0000b8125000);
    lVar1 = 0;
    if (param_2 != 0) {
      lVar1 = param_2 + (ulong)DAT_ffff0000b8125000;
    }
    FUN_ffff0000b804c33c
              (s__s__d___MMUP__dram_size___0x_x____ffff0000b8099dab,
               s_mmup_do_dramdump_ffff0000b808ab5d,0xd8,uVar8,lVar1);
    if ((uVar8 != 0) && (lVar1 != 0)) {
      FUN_ffff0000b804cd40(DAT_ffff0000b8124fe0 + (ulong)uVar7,lVar1,uVar8);
      uVar7 = (uVar8 + 3 & 0x7ffffc) + uVar7;
      goto LAB_ffff0000b802abc0;
    }
  }
  else {
    FUN_ffff0000b804c33c
              (s__MMUP__error__s_mmup_region_info_ffff0000b809b9df,
               s_mmup_do_dramdump_ffff0000b808ab5d);
LAB_ffff0000b802abac:
    uVar8 = 0;
  }
  FUN_ffff0000b804c33c(s__MMUP__mmup_do_dramdump_failed__d_ffff0000b808d249,uVar8);
LAB_ffff0000b802abc0:
  FUN_ffff0000b804c33c
            (s__MMUP___s_size__l2tcm_0x_x__l1c__ffff0000b8092779,s_mmup_crash_dump_ffff0000b8085533,
             0,uVar2,iVar3,iVar4,uVar8);
  FUN_ffff0000b804c33c(s__MMUP__mmup_dump_accumulate_size_ffff0000b8090d7f,uVar7);
  thunk_FUN_ffff0000b8082558(lVar5);
  return uVar7;
}



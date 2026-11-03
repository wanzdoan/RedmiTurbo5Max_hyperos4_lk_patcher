// ffff00009072b354  size=516  FUN_ffff00009072b354

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009072b354(code *param_1)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  char *pcVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  long local_48;
  
  lVar3 = FUN_ffff00009077d24c(0x94000);
  if (lVar3 == 0) {
    pcVar4 = s__mcupm__memory_allocat_for_outpu_ffff0000907d0dd6;
LAB_ffff00009072b3c8:
    FUN_ffff00009077de54(pcVar4);
    return;
  }
  if (_DAT_ffff00000011b180 == 0xdfd987) {
    thunk_FUN_ffff0000907c2ad8(lVar3);
    pcVar4 = s__mcupm__No_need_to_do_coredump_ffff0000907ddd3f;
    goto LAB_ffff00009072b3c8;
  }
  FUN_ffff00009077eb88(lVar3,0,0x94000);
  local_48 = 0;
  if (_DAT_ffff00000c2cf5c8 == 0xdeadbeef) {
    pcVar4 = s__mcupm__TCM_data_be_lost_in_slee_ffff0000907d0e00;
LAB_ffff00009072b490:
    FUN_ffff00009077de54(pcVar4);
  }
  else {
    if (_DAT_ffff00000c2cf590 == 0) {
      pcVar4 = s__mcupm__mcupm_region_info___0__s_ffff0000907e9051;
      goto LAB_ffff00009072b490;
    }
    if (_DAT_ffff00000c2cf5c8 == 0) {
      FUN_ffff00009077de54(s_MCUPM__DM_ADDR___0__try_to_get_c_ffff0000907fcfdf);
      uVar6 = 0x80000;
      FUN_ffff00009077e858(lVar3,0xffff00000c250000,0x80000);
      goto LAB_ffff00009072b498;
    }
    iVar2 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_mcupm_reserved_ffff000090802222,0x100000,&local_48,0xc
                       ,_DAT_ffff00000c2cf5c8 & 0xfffff000,0,1);
    uVar1 = _DAT_ffff00000c2cf5c8;
    if (iVar2 == 0) {
      iVar2 = FUN_ffff00009077b06c(local_48);
      FUN_ffff00009077e858(lVar3,local_48 + (int)(uVar1 - iVar2),_DAT_ffff00000c2cf5cc);
      FUN_ffff000090707ce4(local_48,0x100000);
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_48);
      uVar6 = 0x84000;
      goto LAB_ffff00009072b498;
    }
    FUN_ffff00009077de54
              (s_set_mmu_failed_err____d__dram_ad_ffff0000907d0e33,iVar2,local_48,
               _DAT_ffff00000c2cf5c8);
  }
  uVar6 = 0;
LAB_ffff00009072b498:
  uVar5 = FUN_ffff00009077b06c(lVar3);
  (*param_1)(uVar5,uVar6);
  thunk_FUN_ffff0000907c2ad8(lVar3);
  if (_DAT_ffff00000c2cf59c != -0x21524111) {
    FUN_ffff00009077de54(s__mcupm__TCM_data_might_be_lost__P_ffff0000907f2822,_DAT_ffff00000c2cf59c)
    ;
  }
  return;
}




void FUN_ffff000090787854(undefined8 param_1,uint param_2)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  
  if (1 < param_2) {
    FUN_ffff00009077e11c
              (s__LK_RECOVERY___s_failed__mode_is_ffff0000907e3b29,
               s_read_write_misc_partition_info_ffff0000907eee97,1);
    return;
  }
  lVar2 = FUN_ffff00009077c028(&DAT_ffff0000907dbf67);
  if (lVar2 == 0) {
    FUN_ffff00009077e11c(s__LK_RECOVERY__open_misc_fail_ffff0000907e3b0b);
    return;
  }
  if (param_2 == 0) {
    iVar1 = FUN_ffff00009077c0bc(lVar2,param_1,0,0x800);
    if (0 < iVar1) goto LAB_ffff000090787910;
    pcVar3 = s__LK_RECOVERY__read_bootloader_me_ffff0000907db987;
  }
  else {
    iVar1 = FUN_ffff00009077c144();
    if (0 < iVar1) goto LAB_ffff000090787910;
    pcVar3 = s__LK_RECOVERY__write_bootloader_m_ffff0000907dd443;
  }
  FUN_ffff00009077e11c(pcVar3);
LAB_ffff000090787910:
  FUN_ffff00009077bfac(lVar2);
  return;
}



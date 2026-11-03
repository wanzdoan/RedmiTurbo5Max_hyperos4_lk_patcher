// ffff000090787594  size=212  FUN_ffff000090787594

void FUN_ffff000090787594(undefined8 param_1,uint param_2)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  
  if (1 < param_2) {
    FUN_ffff00009077de54
              (s__LK_RECOVERY___s_failed__mode_is_ffff0000907e3a41,
               s_read_write_misc_partition_info_ffff0000907eed0f,1);
    return;
  }
  lVar2 = FUN_ffff00009077bd60(&DAT_ffff0000907dbe45);
  if (lVar2 == 0) {
    FUN_ffff00009077de54(s__LK_RECOVERY__open_misc_fail_ffff0000907e3a23);
    return;
  }
  if (param_2 == 0) {
    iVar1 = FUN_ffff00009077bdf4(lVar2,param_1,0,0x800);
    if (0 < iVar1) goto LAB_ffff000090787650;
    pcVar3 = s__LK_RECOVERY__read_bootloader_me_ffff0000907db865;
  }
  else {
    iVar1 = FUN_ffff00009077be7c();
    if (0 < iVar1) goto LAB_ffff000090787650;
    pcVar3 = s__LK_RECOVERY__write_bootloader_m_ffff0000907dd389;
  }
  FUN_ffff00009077de54(pcVar3);
LAB_ffff000090787650:
  FUN_ffff00009077bce4(lVar2);
  return;
}



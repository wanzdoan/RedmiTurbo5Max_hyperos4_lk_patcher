// ffff000090787050  size=164  FUN_ffff000090787050

int FUN_ffff000090787050(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  
  iVar1 = FUN_ffff00009077e820(*DAT_ffff0000908399d0,&DAT_ffff0000907d36b1,2);
  if (iVar1 == 0) {
    iVar1 = 0;
    pcVar2 = s_no_need_to_verify_CN_Sku_version_ffff0000907dd354;
  }
  else {
    iVar1 = FUN_ffff00009077e820(param_1,&DAT_ffff0000907d36b1,2);
    if (iVar1 == 0) {
      FUN_ffff00009077de54(s_CN_hardware_not_allow_to_flash_G_ffff0000907eece3);
      iVar1 = FUN_ffff000090787370();
      if (iVar1 != 0) {
        pcVar2 = s_reboot_recovery_failed__ffff0000907fc463;
        goto LAB_ffff0000907870dc;
      }
    }
    iVar1 = 0;
    pcVar2 = s_vbmeta_sku_and_hwc_match__ffff0000907dec4a;
  }
LAB_ffff0000907870dc:
  FUN_ffff00009077de54(pcVar2);
  return iVar1;
}



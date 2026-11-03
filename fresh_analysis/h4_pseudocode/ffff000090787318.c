
int FUN_ffff000090787318(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  
  iVar1 = FUN_ffff00009077eae8(param_1,&DAT_ffff0000907d36ee,2);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff00009077eae8(*(undefined8 *)PTR_DAT_ffff0000908399f0,&DAT_ffff0000907d36ee,2);
    if (iVar1 != 0) {
      FUN_ffff00009077e11c(s_CN_hardware_not_allow_to_flash_G_ffff0000907eee54);
      iVar1 = FUN_ffff000090787630();
      if (iVar1 != 0) {
        pcVar2 = s_reboot_recovery_failed__ffff0000907fc5ff;
        goto LAB_ffff00009078739c;
      }
    }
    iVar1 = 0;
    pcVar2 = s_vbmeta_sku_and_hwc_match__ffff0000907deced;
  }
  else {
    iVar1 = 0;
    pcVar2 = s_no_need_to_verify_GL_hardware__c_ffff0000907e6cf7;
  }
LAB_ffff00009078739c:
  FUN_ffff00009077e11c(pcVar2);
  return iVar1;
}



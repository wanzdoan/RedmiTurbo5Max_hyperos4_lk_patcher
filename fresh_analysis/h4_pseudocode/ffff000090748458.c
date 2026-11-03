
undefined8 FUN_ffff000090748458(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  char *pcVar3;
  
  iVar1 = FUN_ffff000090746fd0
                    (param_1,s__soc_wifi_page_pool_ffff0000907fbbe4,
                     s__wifi_page_pool_ffff0000907f67d6);
  if (iVar1 < 1) {
    FUN_ffff00009077e11c
              (s___s__wifi_page_pool_dts_not_foun_ffff0000907db0d9,s_wifi_pre_setup_ffff0000907d9558
               ,iVar1);
  }
  else {
    iVar2 = FUN_ffff000090747044(param_1,iVar1,s_cma_mem_ffff0000907f863a);
    if (iVar2 != 0) {
      DAT_ffff0000908776f8 = FUN_ffff000090747044(param_1,iVar1,s_emi_size_ffff0000907efec8);
      DAT_ffff00009081a748 = FUN_ffff000090747044(param_1,iVar1,s_mpu_protect_ffff0000907d9567);
    }
    FUN_ffff00009077e11c
              (s___s__wfdma2_mpu_protect___d__siz_ffff0000907d1574,s_wifi_pre_setup_ffff0000907d9558
               ,DAT_ffff00009081a748,DAT_ffff0000908776f8,iVar2);
  }
  iVar1 = FUN_ffff000090746fd0
                    (param_1,s__soc_wifi_tx_cma_ffff0000907d6496,s__wifi_tx_cma_ffff0000907f2ff9);
  if (iVar1 < 1) {
    pcVar3 = s___s__tx_cma_dts_not_found__tx_cm_ffff0000908029a8;
  }
  else {
    DAT_ffff0000908776fc = FUN_ffff000090747044(param_1,iVar1,s_emi_size_ffff0000907efec8);
    pcVar3 = s___s__size___x__ffff0000907f8642;
    iVar1 = DAT_ffff0000908776fc;
  }
  FUN_ffff00009077e11c(pcVar3,s_wifi_pre_setup_ffff0000907d9558,iVar1);
  FUN_ffff00009077e11c(s___s__DONE_ffff0000907d9573,s_wifi_pre_setup_ffff0000907d9558);
  return 0;
}



// ffff0000907481c0  size=368  FUN_ffff0000907481c0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907481c0(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  char *pcVar3;
  
  iVar1 = FUN_ffff000090746d38
                    (param_1,s__soc_wifi_page_pool_ffff0000907fba48,
                     s__wifi_page_pool_ffff0000907f6635);
  if (iVar1 < 1) {
    FUN_ffff00009077de54
              (s___s__wifi_page_pool_dts_not_foun_ffff0000907dafb7,s_wifi_pre_setup_ffff0000907d94c1
               ,iVar1);
  }
  else {
    iVar2 = FUN_ffff000090746dac(param_1,iVar1,s_cma_mem_ffff0000907f849e);
    if (iVar2 != 0) {
      _DAT_ffff000090877660 = FUN_ffff000090746dac(param_1,iVar1,s_emi_size_ffff0000907efd40);
      DAT_ffff00009081a728 = FUN_ffff000090746dac(param_1,iVar1,s_mpu_protect_ffff0000907d94d0);
    }
    FUN_ffff00009077de54
              (s___s__wfdma2_mpu_protect___d__siz_ffff0000907d1537,s_wifi_pre_setup_ffff0000907d94c1
               ,DAT_ffff00009081a728,_DAT_ffff000090877660,iVar2);
  }
  iVar1 = FUN_ffff000090746d38
                    (param_1,s__soc_wifi_tx_cma_ffff0000907d6400,s__wifi_tx_cma_ffff0000907f2e56);
  if (iVar1 < 1) {
    pcVar3 = s___s__tx_cma_dts_not_found__tx_cm_ffff0000908027a4;
  }
  else {
    _DAT_ffff000090877664 = FUN_ffff000090746dac(param_1,iVar1,s_emi_size_ffff0000907efd40);
    pcVar3 = s___s__size___x__ffff0000907f84a6;
    iVar1 = _DAT_ffff000090877664;
  }
  FUN_ffff00009077de54(pcVar3,s_wifi_pre_setup_ffff0000907d94c1,iVar1);
  FUN_ffff00009077de54(s___s__DONE_ffff0000907d94dc,s_wifi_pre_setup_ffff0000907d94c1);
  return 0;
}



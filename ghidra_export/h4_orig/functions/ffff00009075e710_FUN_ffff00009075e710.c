// ffff00009075e710  size=624  FUN_ffff00009075e710

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075e710(void)

{
  undefined *puVar1;
  int iVar2;
  char *pcVar3;
  char *pcVar4;
  uint uVar5;
  undefined4 uVar6;
  
  iVar2 = FUN_ffff000090784ca8();
  puVar1 = &DAT_ffff0000907dc806;
  if (iVar2 != 0) {
    puVar1 = &DAT_ffff0000907e49dd;
  }
  FUN_ffff00009077e11c
            (s__s__GPU_DFD_s_is_enabled_ffff000090801366,s__GPU_DFD__ffff0000907e513e,puVar1);
  if ((_DAT_ffff000010001600 >> 0x13 & 1) == 0) {
    pcVar3 = s__s__GPU_DFD_was_not_triggered__0_ffff0000907f6e30;
    uVar5 = _DAT_ffff000010001600;
LAB_ffff00009075e83c:
    FUN_ffff00009077e11c(pcVar3,s__GPU_DFD__ffff0000907e513e,uVar5);
    iVar2 = FUN_ffff000090739f80(1);
    if (iVar2 < 0) {
      pcVar3 = s__s__set_gpu_latch_en_fail_ffff0000907f6e56;
    }
    else {
      iVar2 = FUN_ffff000090739f1c(1);
      if (iVar2 < 0) {
        pcVar3 = s__s__set_rg_drm_mfg_req_mode_fail_ffff0000907f8a9a;
      }
      else {
        iVar2 = FUN_ffff000090784ca8();
        uVar6 = 10000;
        if (iVar2 != 1) {
          uVar6 = 2000;
        }
        iVar2 = FUN_ffff000090739e14(uVar6,0);
        if (iVar2 < 0) {
          pcVar3 = s__s__set_dfd_timeout_fail_ffff0000907eb5c7;
        }
        else {
          iVar2 = FUN_ffff000090739ca0(1);
          if (iVar2 < 0) {
            pcVar3 = s__s__start_dfd_count_fail_ffff0000907f8abc;
          }
          else {
            if ((_DAT_ffff00001000d060 & 1) != 0) {
              return;
            }
            iVar2 = FUN_ffff000090739fe0(0);
            if (iVar2 < 0) goto LAB_ffff00009075e900;
            FUN_ffff00009077e11c
                      (s__s_after_MFG_PWR_ON_DISABLE_SETT_ffff0000907dffcf,
                       s__GPU_DFD__ffff0000907e513e);
            FUN_ffff00009077cea0(10);
            iVar2 = FUN_ffff000090739fe0(1);
            if (iVar2 < 0) {
              pcVar3 = s__s__set_mfg_pwr_on_fail_ffff0000907ece90;
            }
            else {
              iVar2 = FUN_ffff00009073a038(0);
              if (-1 < iVar2) {
                pcVar3 = s__s_after_MFG_PWR_ON_ENABLE_SETTI_ffff0000907e1c33;
                pcVar4 = s__GPU_DFD__ffff0000907e513e;
                goto LAB_ffff00009075e940;
              }
              pcVar3 = s__s__clear_mfg_pwr_en_fail_ffff0000907e35ee;
            }
          }
        }
      }
    }
  }
  else {
    FUN_ffff00009077e11c
              (s__s__GPU_DFD_was_triggered__start_ffff0000907e5148,s__GPU_DFD__ffff0000907e513e);
    _DAT_ffff00004b800500 = _DAT_ffff00004b800500 & 0xfffffffe;
    FUN_ffff00009077e11c
              (s__s__after_release_RST_B_MFG_RPC__ffff0000907dfff4,s__GPU_DFD__ffff0000907e513e,
               _DAT_ffff00004b800500);
    _DAT_ffff00004b800500 = _DAT_ffff00004b800500 | 1;
    FUN_ffff00009077e11c
              (s__s__after_set_RST_B_MFG_RPC_MFG1_ffff0000907d4d22,s__GPU_DFD__ffff0000907e513e,
               _DAT_ffff00004b800500);
    FUN_ffff00009077e11c(s__s__disable_MFG_DFD_CON_0_ffff0000907e51a5,s__GPU_DFD__ffff0000907e513e);
    _DAT_ffff000048500a00 = 0;
    FUN_ffff00009077e11c
              (s__s__before_clear_wdt_mfg_pwr_on__ffff0000907ee9c0,s__GPU_DFD__ffff0000907e513e,
               _DAT_ffff00001000d060);
    iVar2 = FUN_ffff000090739fe0(0);
    if (iVar2 < 0) {
LAB_ffff00009075e900:
      pcVar3 = s__s__clear_mfg_pwr_on_fail_ffff000090802c83;
    }
    else {
      iVar2 = FUN_ffff00009073a038(1);
      if (-1 < iVar2) {
        pcVar3 = s__s__after_clear_wdt_mfg_pwr_on__D_ffff0000907e5171;
        uVar5 = _DAT_ffff00001000d060;
        goto LAB_ffff00009075e83c;
      }
      pcVar3 = s__s__set_mfg_pwr_en_fail_ffff0000907fa5a4;
    }
  }
  pcVar4 = s_gpu_dfd_init_ffff0000907d819d;
LAB_ffff00009075e940:
  FUN_ffff00009077e11c(pcVar3,pcVar4);
  return;
}



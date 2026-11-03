// ffff00009075e478  size=624  FUN_ffff00009075e478

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075e478(void)

{
  undefined *puVar1;
  int iVar2;
  char *pcVar3;
  char *pcVar4;
  uint uVar5;
  undefined4 uVar6;
  
  iVar2 = FUN_ffff0000907849e0();
  puVar1 = &DAT_ffff0000907dc730;
  if (iVar2 != 0) {
    puVar1 = &DAT_ffff0000907e48d9;
  }
  FUN_ffff00009077de54
            (s__s__GPU_DFD_s_is_enabled_ffff00009080118a,s__GPU_DFD__ffff0000907e503a,puVar1);
  if ((_DAT_ffff000010001600 >> 0x13 & 1) == 0) {
    pcVar3 = s__s__GPU_DFD_was_not_triggered__0_ffff0000907f6ca6;
    uVar5 = _DAT_ffff000010001600;
LAB_ffff00009075e5a4:
    FUN_ffff00009077de54(pcVar3,s__GPU_DFD__ffff0000907e503a,uVar5);
    iVar2 = FUN_ffff000090739ce8(1);
    if (iVar2 < 0) {
      pcVar3 = s__s__set_gpu_latch_en_fail_ffff0000907f6ccc;
    }
    else {
      iVar2 = FUN_ffff000090739c84(1);
      if (iVar2 < 0) {
        pcVar3 = s__s__set_rg_drm_mfg_req_mode_fail_ffff0000907f88fe;
      }
      else {
        iVar2 = FUN_ffff0000907849e0();
        uVar6 = 10000;
        if (iVar2 != 1) {
          uVar6 = 2000;
        }
        iVar2 = FUN_ffff000090739b7c(uVar6,0);
        if (iVar2 < 0) {
          pcVar3 = s__s__set_dfd_timeout_fail_ffff0000907eb444;
        }
        else {
          iVar2 = FUN_ffff000090739a08(1);
          if (iVar2 < 0) {
            pcVar3 = s__s__start_dfd_count_fail_ffff0000907f8920;
          }
          else {
            if ((_DAT_ffff00001000d060 & 1) != 0) {
              return;
            }
            iVar2 = FUN_ffff000090739d48(0);
            if (iVar2 < 0) goto LAB_ffff00009075e668;
            FUN_ffff00009077de54
                      (s__s_after_MFG_PWR_ON_DISABLE_SETT_ffff0000907dff2c,
                       s__GPU_DFD__ffff0000907e503a);
            FUN_ffff00009077cbd8(10);
            iVar2 = FUN_ffff000090739d48(1);
            if (iVar2 < 0) {
              pcVar3 = s__s__set_mfg_pwr_on_fail_ffff0000907ecd0d;
            }
            else {
              iVar2 = FUN_ffff000090739da0(0);
              if (-1 < iVar2) {
                pcVar3 = s__s_after_MFG_PWR_ON_ENABLE_SETTI_ffff0000907e1b62;
                pcVar4 = s__GPU_DFD__ffff0000907e503a;
                goto LAB_ffff00009075e6a8;
              }
              pcVar3 = s__s__clear_mfg_pwr_en_fail_ffff0000907e351d;
            }
          }
        }
      }
    }
  }
  else {
    FUN_ffff00009077de54
              (s__s__GPU_DFD_was_triggered__start_ffff0000907e5044,s__GPU_DFD__ffff0000907e503a);
    _DAT_ffff00004b800500 = _DAT_ffff00004b800500 & 0xfffffffe;
    FUN_ffff00009077de54
              (s__s__after_release_RST_B_MFG_RPC__ffff0000907dff51,s__GPU_DFD__ffff0000907e503a,
               _DAT_ffff00004b800500);
    _DAT_ffff00004b800500 = _DAT_ffff00004b800500 | 1;
    FUN_ffff00009077de54
              (s__s__after_set_RST_B_MFG_RPC_MFG1_ffff0000907d4ce5,s__GPU_DFD__ffff0000907e503a,
               _DAT_ffff00004b800500);
    FUN_ffff00009077de54(s__s__disable_MFG_DFD_CON_0_ffff0000907e50a1,s__GPU_DFD__ffff0000907e503a);
    _DAT_ffff000048500a00 = 0;
    FUN_ffff00009077de54
              (s__s__before_clear_wdt_mfg_pwr_on__ffff0000907ee84f,s__GPU_DFD__ffff0000907e503a,
               _DAT_ffff00001000d060);
    iVar2 = FUN_ffff000090739d48(0);
    if (iVar2 < 0) {
LAB_ffff00009075e668:
      pcVar3 = s__s__clear_mfg_pwr_on_fail_ffff000090802a7f;
    }
    else {
      iVar2 = FUN_ffff000090739da0(1);
      if (-1 < iVar2) {
        pcVar3 = s__s__after_clear_wdt_mfg_pwr_on__D_ffff0000907e506d;
        uVar5 = _DAT_ffff00001000d060;
        goto LAB_ffff00009075e5a4;
      }
      pcVar3 = s__s__set_mfg_pwr_en_fail_ffff0000907fa408;
    }
  }
  pcVar4 = s_gpu_dfd_init_ffff0000907d8107;
LAB_ffff00009075e6a8:
  FUN_ffff00009077de54(pcVar3,pcVar4);
  return;
}



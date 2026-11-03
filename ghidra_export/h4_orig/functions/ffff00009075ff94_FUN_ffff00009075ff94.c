// ffff00009075ff94  size=268  FUN_ffff00009075ff94

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075ff94(long param_1)

{
  int iVar1;
  char local_30 [8];
  undefined1 local_28;
  
  local_28 = 0;
  local_30 = (char  [8])s_disabled_ffff0000907f0f7e._0_8_;
  if (param_1 == 0) {
    FUN_ffff00009077e11c(s_kernel_fdt_is_NULL__ffff0000907fcf08);
    return;
  }
  iVar1 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_mt8183_ufshci_ffff0000907f02da);
  if (-1 < iVar1) {
    FUN_ffff0000907600a0(param_1,iVar1);
    if ((_DAT_ffff00001002d6f0 >> 0x14 & 1) != 0) {
      FUN_ffff00009077e11c
                (s__s_disable_ufs_when_emmc_boot_ffff0000907d69b1,s_update_ufs_dts_ffff0000907f1af7)
      ;
      iVar1 = FUN_ffff00009076e6d4(param_1,iVar1,s_status_ffff0000907edcdb,local_30,9);
      if (iVar1 != 0) {
        FUN_ffff00009077e11c
                  (s__s_fail_to_set_status_to_disable_ffff0000907fded1,
                   s_update_ufs_dts_ffff0000907f1af7,iVar1);
      }
    }
    return;
  }
  FUN_ffff00009077e11c
            (s__s_fail_to_find_node__d__ffff0000907d199b,s_update_ufs_dts_ffff0000907f1af7,iVar1);
  return;
}



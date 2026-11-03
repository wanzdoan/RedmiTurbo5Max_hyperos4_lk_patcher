// ffff00009075fcfc  size=268  FUN_ffff00009075fcfc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075fcfc(long param_1)

{
  int iVar1;
  char local_30 [8];
  undefined1 local_28;
  
  local_28 = 0;
  local_30 = (char  [8])s_disabled_ffff0000907f0df6._0_8_;
  if (param_1 == 0) {
    FUN_ffff00009077de54(s_kernel_fdt_is_NULL__ffff0000907fcd60);
    return;
  }
  iVar1 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_mt8183_ufshci_ffff0000907f0152);
  if (-1 < iVar1) {
    FUN_ffff00009075fe08(param_1,iVar1);
    if ((_DAT_ffff00001002d6f0 >> 0x14 & 1) != 0) {
      FUN_ffff00009077de54
                (s__s_disable_ufs_when_emmc_boot_ffff0000907d691b,s_update_ufs_dts_ffff0000907f196f)
      ;
      iVar1 = FUN_ffff00009076e40c(param_1,iVar1,s_status_ffff0000907edb53,local_30,9);
      if (iVar1 != 0) {
        FUN_ffff00009077de54
                  (s__s_fail_to_set_status_to_disable_ffff0000907fdd29,
                   s_update_ufs_dts_ffff0000907f196f,iVar1);
      }
    }
    return;
  }
  FUN_ffff00009077de54
            (s__s_fail_to_find_node__d__ffff0000907d195e,s_update_ufs_dts_ffff0000907f196f,iVar1);
  return;
}



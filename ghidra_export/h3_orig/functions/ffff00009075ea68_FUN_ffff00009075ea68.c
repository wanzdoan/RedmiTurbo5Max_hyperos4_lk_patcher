// ffff00009075ea68  size=192  FUN_ffff00009075ea68

void FUN_ffff00009075ea68(long param_1)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  
  if (param_1 == 0) {
    pcVar3 = s_kernel_fdt_is_NULL__ffff0000907fcd60;
  }
  else {
    iVar1 = FUN_ffff00009076d75c(param_1,s__soc_mmc_ffff0000907cfec3);
    if ((-1 < iVar1) || (iVar1 = FUN_ffff00009076d75c(param_1,&DAT_ffff0000907e80c5), -1 < iVar1)) {
      lVar2 = FUN_ffff00009076dcd4(param_1,iVar1,s_no_sd_uhs_sdr104_ffff0000907f893a,0);
      if (lVar2 != 0) {
        FUN_ffff00009077de54(s_mmc_no_sd_uhs_sdr104_get_success_ffff0000907db3de);
        FUN_ffff00009076e5dc(param_1,iVar1,s_sd_uhs_sdr104_ffff0000907eb484);
        return;
      }
      return;
    }
    pcVar3 = s_mmc_node_get_fail__ffff0000907cfecc;
  }
  FUN_ffff00009077de54(pcVar3);
  return;
}



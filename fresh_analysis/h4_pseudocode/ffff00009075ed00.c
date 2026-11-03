
void FUN_ffff00009075ed00(long param_1)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  
  if (param_1 == 0) {
    pcVar3 = s_kernel_fdt_is_NULL__ffff0000907fcf08;
  }
  else {
    iVar1 = FUN_ffff00009076da24(param_1,s__soc_mmc_ffff0000907cfee2);
    if ((-1 < iVar1) || (iVar1 = FUN_ffff00009076da24(param_1,&DAT_ffff0000907e8209), -1 < iVar1)) {
      lVar2 = FUN_ffff00009076df9c(param_1,iVar1,s_no_sd_uhs_sdr104_ffff0000907f8ad6,0);
      if (lVar2 != 0) {
        FUN_ffff00009077e11c(s_mmc_no_sd_uhs_sdr104_get_success_ffff0000907db4e9);
        FUN_ffff00009076e8a4(param_1,iVar1,s_sd_uhs_sdr104_ffff0000907eb607);
        return;
      }
      return;
    }
    pcVar3 = s_mmc_node_get_fail__ffff0000907cfeeb;
  }
  FUN_ffff00009077e11c(pcVar3);
  return;
}



// ffff00009072098c  size=240  FUN_ffff00009072098c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009072098c(undefined4 param_1,int param_2)

{
  int iVar1;
  uint local_24;
  
  if (_DAT_ffff000090860650 != 0) {
    return 0xffffffff;
  }
  if (param_2 != 1) {
    if (param_2 != 0) {
      return 0;
    }
    FUN_ffff00009077e11c
              (s__s__not_support_on_mt6379_2p_ffff0000907edde1,s_mt6379_2p_set_cv_ffff0000907d28ea);
    return 0;
  }
  FUN_ffff00009077e11c
            (s__s____dmA_ffff0000907ef60a,s_mt6379_2p_set_ibat_limit_ffff0000908007a5,param_1);
  local_24 = 0;
  iVar1 = FUN_ffff000090737554(_DAT_ffff000090860648,1,&local_24);
  if (iVar1 < 0) {
    iVar1 = -1;
  }
  else {
    local_24 = local_24 & 0xff;
    iVar1 = FUN_ffff00009073751c(_DAT_ffff000090860648,1);
    if (-1 < iVar1) goto LAB_ffff000090720a48;
  }
  FUN_ffff00009077e11c(s_mt6379_2p_write_field__d_fail____ffff0000907dc4c4,2,iVar1);
LAB_ffff000090720a48:
  FUN_ffff00009077e11c
            (s__s___d__ret__d_ffff0000907f107d,s_mt6379_2p_set_cc_ffff0000907e2863,param_1,iVar1);
  return 0;
}



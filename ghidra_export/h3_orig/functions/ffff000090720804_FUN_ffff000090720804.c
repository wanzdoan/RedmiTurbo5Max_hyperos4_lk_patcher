// ffff000090720804  size=240  FUN_ffff000090720804

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090720804(undefined4 param_1,int param_2)

{
  int iVar1;
  uint local_24;
  
  if (_DAT_ffff0000908605b8 != 0) {
    return 0xffffffff;
  }
  if (param_2 != 1) {
    if (param_2 != 0) {
      return 0;
    }
    FUN_ffff00009077de54
              (s__s__not_support_on_mt6379_2p_ffff0000907edc70,s_mt6379_2p_set_cv_ffff0000907d28ad);
    return 0;
  }
  FUN_ffff00009077de54
            (s__s____dmA_ffff0000907ef499,s_mt6379_2p_set_ibat_limit_ffff0000908005c9,param_1);
  local_24 = 0;
  iVar1 = FUN_ffff0000907372bc(_DAT_ffff0000908605b0,1,&local_24);
  if (iVar1 < 0) {
    iVar1 = -1;
  }
  else {
    local_24 = local_24 & 0xff;
    iVar1 = FUN_ffff000090737284(_DAT_ffff0000908605b0,1);
    if (-1 < iVar1) goto LAB_ffff0000907208c0;
  }
  FUN_ffff00009077de54(s_mt6379_2p_write_field__d_fail____ffff0000907dc3ee,2,iVar1);
LAB_ffff0000907208c0:
  FUN_ffff00009077de54
            (s__s___d__ret__d_ffff0000907f0f0c,s_mt6379_2p_set_cc_ffff0000907e2792,param_1,iVar1);
  return 0;
}



// ffff000090746188  size=348  FUN_ffff000090746188

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090746188(undefined8 param_1)

{
  int iVar1;
  int *piVar2;
  int local_24;
  
  iVar1 = FUN_ffff00009076d75c(param_1,s__watchdog_ffff0000907f4b21);
  if (iVar1 < 0) {
    FUN_ffff00009077de54(s_Failed_to_find__s_in_dtb_ffff0000907e9623,s__watchdog_ffff0000907f4b21);
    iVar1 = FUN_ffff00009076d75c(param_1,s__soc_watchdog_ffff0000907ee35f);
    if (iVar1 < 0) {
      FUN_ffff00009077de54
                (s_Failed_to_find__s_in_dtb_ffff0000907e9623,s__soc_watchdog_ffff0000907ee35f);
      return;
    }
  }
  local_24 = 0;
  piVar2 = (int *)FUN_ffff00009076dcd4(param_1,iVar1,s_apwdt_en_ffff0000907daf59,&local_24);
  if ((local_24 < 1) || (piVar2 == (int *)0x0)) {
    FUN_ffff00009077de54(s_Fail_to_found_property__s_ffff0000907f65e5,s_apwdt_en_ffff0000907daf59);
    piVar2 = (int *)FUN_ffff00009076dcd4(param_1,iVar1,s_apwdten_ffff0000907f6600,&local_24);
    if ((local_24 < 1) || (piVar2 == (int *)0x0)) {
      FUN_ffff00009077de54(s_Fail_to_found_property__s_ffff0000907f65e5,s_apwdten_ffff0000907f6600);
      return;
    }
  }
  if (*piVar2 == 0) {
    _DAT_ffff00001c010000 = _DAT_ffff00001c010000 & 0xddffffb0 | 0x22000004;
    _DAT_ffff00001c010030 = 0x33000000;
    _DAT_ffff00001c010034 = 0x44000000;
    FUN_ffff00009077de54(s_apwdt_is_disabled_by_doe_ffff0000907dfbe8);
  }
  return;
}



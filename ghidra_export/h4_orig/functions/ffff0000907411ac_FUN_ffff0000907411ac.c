// ffff0000907411ac  size=124  FUN_ffff0000907411ac

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907411ac(void)

{
  int iVar1;
  
  _DAT_ffff00009086f450 = FUN_ffff000090740610(1,5);
  if (_DAT_ffff00009086f450 == 0) {
    iVar1 = -8;
  }
  else {
    iVar1 = FUN_ffff000090722730
                      (s_mt6373_shutdown_callback_ffff0000907d2ef4,FUN_ffff000090741228,
                       _DAT_ffff00009086f450);
    if (iVar1 != 0) {
      FUN_ffff00009077e11c
                (s__s_register_shutdown_callback_fa_ffff0000907dfa33,
                 s_mt6373_device_register_ffff0000907e4cd2,iVar1);
    }
  }
  return iVar1;
}



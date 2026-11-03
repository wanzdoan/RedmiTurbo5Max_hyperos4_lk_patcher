
int FUN_ffff0000907411ac(void)

{
  int iVar1;
  
  DAT_ffff00009086f450 = FUN_ffff000090740610(1,5);
  if (DAT_ffff00009086f450 == 0) {
    iVar1 = -8;
  }
  else {
    iVar1 = FUN_ffff000090722730
                      (s_mt6373_shutdown_callback_ffff0000907d2ef4,FUN_ffff000090741228,
                       DAT_ffff00009086f450);
    if (iVar1 != 0) {
      FUN_ffff00009077e11c
                (s__s_register_shutdown_callback_fa_ffff0000907dfa33,
                 s_mt6373_device_register_ffff0000907e4cd2,iVar1);
    }
  }
  return iVar1;
}



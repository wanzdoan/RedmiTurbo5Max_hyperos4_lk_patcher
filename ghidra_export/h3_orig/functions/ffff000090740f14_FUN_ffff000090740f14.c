// ffff000090740f14  size=124  FUN_ffff000090740f14

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090740f14(void)

{
  int iVar1;
  
  _DAT_ffff00009086f3b8 = FUN_ffff000090740378(1,5);
  if (_DAT_ffff00009086f3b8 == 0) {
    iVar1 = -8;
  }
  else {
    iVar1 = FUN_ffff0000907224ec
                      (s_mt6373_shutdown_callback_ffff0000907d2eb7,FUN_ffff000090740f90,
                       _DAT_ffff00009086f3b8);
    if (iVar1 != 0) {
      FUN_ffff00009077de54
                (s__s_register_shutdown_callback_fa_ffff0000907df990,
                 s_mt6373_device_register_ffff0000907e4bce,iVar1);
    }
  }
  return iVar1;
}



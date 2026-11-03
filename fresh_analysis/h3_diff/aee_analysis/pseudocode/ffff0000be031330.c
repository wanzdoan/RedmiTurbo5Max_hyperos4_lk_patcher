
int FUN_ffff0000be031330(void)

{
  int iVar1;
  
  DAT_ffff0000be119708 = FUN_ffff0000be0303e8(1,5);
  if (DAT_ffff0000be119708 == 0) {
    iVar1 = -8;
  }
  else {
    iVar1 = FUN_ffff0000be0203a4
                      (s_mt6373_shutdown_callback_ffff0000be0a5ca3,FUN_ffff0000be0313ac,
                       DAT_ffff0000be119708);
    if (iVar1 != 0) {
      FUN_ffff0000be05fd98
                (s__s_register_shutdown_callback_fa_ffff0000be0ae7d8,
                 s_mt6373_device_register_ffff0000be0b1fd4,iVar1);
    }
  }
  return iVar1;
}



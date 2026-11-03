
int FUN_ffff0000b8027338(void)

{
  int iVar1;
  
  DAT_ffff0000b811cf18 = FUN_ffff0000b80263f0(1,5);
  if (DAT_ffff0000b811cf18 == 0) {
    iVar1 = -8;
  }
  else {
    iVar1 = FUN_ffff0000b8014d0c
                      (s_mt6373_shutdown_callback_ffff0000b8087319,FUN_ffff0000b80273b4,
                       DAT_ffff0000b811cf18);
    if (iVar1 != 0) {
      FUN_ffff0000b804c33c
                (s__s_register_shutdown_callback_fa_ffff0000b808e04d,
                 s_mt6373_device_register_ffff0000b8090cd9,iVar1);
    }
  }
  return iVar1;
}



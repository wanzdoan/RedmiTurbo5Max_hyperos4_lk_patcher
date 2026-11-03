
void FUN_ffff0000b802795c(void)

{
  int iVar1;
  
  DAT_ffff0000b811cf30 = FUN_ffff0000b80263f0(1,9);
  if (DAT_ffff0000b811cf30 == 0) {
    FUN_ffff0000b804c33c(s__s__get_spmi_device_fail_ffff0000b80897f3,s_mt6685_init_ffff0000b809aa95)
    ;
    return;
  }
  iVar1 = FUN_ffff0000b8014d0c(s_mt6685_shutdown_callback_ffff0000b808d201,FUN_ffff0000b80279e0,0);
  if (iVar1 != 0) {
    FUN_ffff0000b804c33c
              (s__s__register_shutdown_callback_f_ffff0000b8098e32,s_mt6685_init_ffff0000b809aa95,
               iVar1);
    return;
  }
  return;
}



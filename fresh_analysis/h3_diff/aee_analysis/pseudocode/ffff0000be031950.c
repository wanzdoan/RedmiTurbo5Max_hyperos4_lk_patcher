
void FUN_ffff0000be031950(void)

{
  int iVar1;
  
  DAT_ffff0000be119720 = FUN_ffff0000be0303e8(1,9);
  if (DAT_ffff0000be119720 == 0) {
    FUN_ffff0000be05fd98(s__s__get_spmi_device_fail_ffff0000be0a8b7b,s_mt6685_init_ffff0000be0be4ae)
    ;
    return;
  }
  iVar1 = FUN_ffff0000be0203a4(s_mt6685_shutdown_callback_ffff0000be0ad8af,FUN_ffff0000be0319d8,0);
  if (iVar1 != 0) {
    FUN_ffff0000be05fd98
              (s__s__register_shutdown_callback_f_ffff0000be0bc007,s_mt6685_init_ffff0000be0be4ae,
               iVar1);
    return;
  }
  return;
}



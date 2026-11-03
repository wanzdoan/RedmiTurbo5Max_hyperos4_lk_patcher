// ffff0000907418ec  size=4  FUN_ffff0000907418ec

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907418ec(void)

{
  int iVar1;
  
  _DAT_ffff00009086f468 = FUN_ffff000090740610(1,9);
  if (_DAT_ffff00009086f468 == 0) {
    FUN_ffff00009077e11c(s__s__get_spmi_device_fail_ffff0000907d71f6,s_mt6685_init_ffff0000907f669b)
    ;
    return;
  }
  iVar1 = FUN_ffff000090722730(s_mt6685_shutdown_callback_ffff0000907de362,FUN_ffff000090741974,0);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c
              (s__s__register_shutdown_callback_f_ffff0000907f2eb9,s_mt6685_init_ffff0000907f669b,
               iVar1);
    return;
  }
  return;
}



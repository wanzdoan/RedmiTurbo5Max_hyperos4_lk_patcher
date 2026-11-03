// ffff000090741658  size=132  FUN_ffff000090741658

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090741658(void)

{
  int iVar1;
  
  _DAT_ffff00009086f3d0 = FUN_ffff000090740378(1,9);
  if (_DAT_ffff00009086f3d0 == 0) {
    FUN_ffff00009077de54(s__s__get_spmi_device_fail_ffff0000907d7160,s_mt6685_init_ffff0000907f64fa)
    ;
    return;
  }
  iVar1 = FUN_ffff0000907224ec(s_mt6685_shutdown_callback_ffff0000907de2a8,FUN_ffff0000907416dc,0);
  if (iVar1 != 0) {
    FUN_ffff00009077de54
              (s__s__register_shutdown_callback_f_ffff0000907f2d16,s_mt6685_init_ffff0000907f64fa,
               iVar1);
    return;
  }
  return;
}



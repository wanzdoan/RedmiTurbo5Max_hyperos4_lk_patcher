// ffff000090711a68  size=152  FUN_ffff000090711a68

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090711a68(undefined8 param_1,int param_2)

{
  if (param_2 == 0) {
    _DAT_ffff00009083f750 = 0;
    return;
  }
  _DAT_ffff00009083f750 = FUN_ffff00009077d24c(param_2 + 1);
  if (_DAT_ffff00009083f750 != 0) {
    FUN_ffff00009077eb88(_DAT_ffff00009083f750,0,param_2 + 1);
    FUN_ffff00009077efe4(_DAT_ffff00009083f750,param_1,param_2);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_bootconfig_c_ffff0000907e7401,0x30,
             s_vendor_bootconfig_buf_ffff000090800308);
}



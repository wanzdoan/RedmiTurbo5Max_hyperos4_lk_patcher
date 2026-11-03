// ffff000090711b4c  size=152  FUN_ffff000090711b4c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090711b4c(undefined8 param_1,int param_2)

{
  if (param_2 == 0) {
    _DAT_ffff00009083f788 = 0;
    return;
  }
  _DAT_ffff00009083f788 = FUN_ffff00009077d514(param_2 + 1);
  if (_DAT_ffff00009083f788 != 0) {
    FUN_ffff00009077ee50(_DAT_ffff00009083f788,0,param_2 + 1);
    FUN_ffff00009077f2ac(_DAT_ffff00009083f788,param_1,param_2);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_bootconfig_c_ffff0000907e755c,0x30,
             s_vendor_bootconfig_buf_ffff0000908004e4);
}



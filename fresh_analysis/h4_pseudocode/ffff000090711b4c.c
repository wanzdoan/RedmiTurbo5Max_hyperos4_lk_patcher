
void FUN_ffff000090711b4c(undefined8 param_1,int param_2)

{
  if (param_2 == 0) {
    DAT_ffff00009083f788 = 0;
    return;
  }
  DAT_ffff00009083f788 = FUN_ffff00009077d514(param_2 + 1);
  if (DAT_ffff00009083f788 != 0) {
    FUN_ffff00009077ee50(DAT_ffff00009083f788,0,param_2 + 1);
    FUN_ffff00009077f2ac(DAT_ffff00009083f788,param_1,param_2);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_bootconfig_c_ffff0000907e755c,0x30,
             s_vendor_bootconfig_buf_ffff0000908004e4);
}



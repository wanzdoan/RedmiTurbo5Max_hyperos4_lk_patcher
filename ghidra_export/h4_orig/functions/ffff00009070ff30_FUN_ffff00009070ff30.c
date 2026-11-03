// ffff00009070ff30  size=52  FUN_ffff00009070ff30

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009070ff30(void)

{
  uint uVar1;
  
  if ((DAT_ffff00009083f638 & 1) != 0) {
    uVar1 = 0;
    if (_DAT_ffff00009083e78c != 0) {
      uVar1 = ((_DAT_ffff00009083e78c + _DAT_ffff00009083edc8) - 1) / _DAT_ffff00009083e78c;
    }
    return uVar1 * _DAT_ffff00009083e78c;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x7a,
             s_g_boot_info_hdr_loaded_ffff0000907d25df);
}



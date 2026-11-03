// ffff00009070feb8  size=52  FUN_ffff00009070feb8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009070feb8(void)

{
  uint uVar1;
  
  if ((DAT_ffff00009083f600 & 1) != 0) {
    uVar1 = 0;
    if (_DAT_ffff00009083e754 != 0) {
      uVar1 = ((_DAT_ffff00009083e754 + _DAT_ffff00009083ed90) - 1) / _DAT_ffff00009083e754;
    }
    return uVar1 * _DAT_ffff00009083e754;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0x7a,
             s_g_boot_info_hdr_loaded_ffff0000907d25a2);
}



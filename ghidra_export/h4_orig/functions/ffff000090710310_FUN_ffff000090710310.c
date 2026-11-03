// ffff000090710310  size=140  FUN_ffff000090710310

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff000090710310(void)

{
  uint *puVar1;
  
  if ((DAT_ffff00009083f638 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x10d,
               s_g_boot_info_hdr_loaded_ffff0000907d25df);
  }
  if ((DAT_ffff00009083f640 & 1) != 0) {
    puVar1 = (uint *)&DAT_ffff00009083edf4;
    if (_DAT_ffff00009083e790 < 3) {
      puVar1 = (uint *)&DAT_ffff00009083e78c;
    }
    return _DAT_ffff00009083f650 + (ulong)*puVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x10e,
             s_g_boot_info_img_loaded_ffff0000907f0d95);
}



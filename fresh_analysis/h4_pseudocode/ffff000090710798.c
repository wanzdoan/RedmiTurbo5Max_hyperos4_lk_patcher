
undefined4 FUN_ffff000090710798(void)

{
  undefined4 uVar1;
  
  if ((DAT_ffff00009083f638 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x1aa,
               s_g_boot_info_hdr_loaded_ffff0000907d25df);
  }
  if ((DAT_ffff00009083f640 & 1) != 0) {
    uVar1 = DAT_ffff00009083f634;
    if (DAT_ffff00009083e790 < 4) {
      uVar1 = 0;
    }
    return uVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x1ab,
             s_g_boot_info_img_loaded_ffff0000907f0d95);
}



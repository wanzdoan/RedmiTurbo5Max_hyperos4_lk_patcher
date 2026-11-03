
undefined4 FUN_ffff000090710818(void)

{
  undefined4 *puVar1;
  
  if ((DAT_ffff00009083f638 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x1b5,
               s_g_boot_info_hdr_loaded_ffff0000907d25df);
  }
  if ((DAT_ffff00009083f640 & 1) != 0) {
    puVar1 = &DAT_ffff00009083e144;
    if (DAT_ffff00009083e790 < 4) {
      puVar1 = &DAT_ffff00009083e778;
    }
    return *puVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x1b6,
             s_g_boot_info_img_loaded_ffff0000907f0d95);
}



// ffff000090710024  size=140  FUN_ffff000090710024

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff000090710024(void)

{
  uint *puVar1;
  
  if ((DAT_ffff00009083f600 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,199,
               s_g_boot_info_hdr_loaded_ffff0000907d25a2);
  }
  if ((DAT_ffff00009083f608 & 1) != 0) {
    puVar1 = (uint *)&DAT_ffff00009083edbc;
    if (_DAT_ffff00009083e758 < 3) {
      puVar1 = (uint *)&DAT_ffff00009083e754;
    }
    return _DAT_ffff00009083f610 + (ulong)*puVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,200,
             s_g_boot_info_img_loaded_ffff0000907f0c0d);
}



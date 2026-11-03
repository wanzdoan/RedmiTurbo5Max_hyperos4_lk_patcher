// ffff0000907107a0  size=128  FUN_ffff0000907107a0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907107a0(void)

{
  undefined4 *puVar1;
  
  if ((DAT_ffff00009083f600 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0x1b5,
               s_g_boot_info_hdr_loaded_ffff0000907d25a2);
  }
  if ((DAT_ffff00009083f608 & 1) != 0) {
    puVar1 = (undefined4 *)&DAT_ffff00009083e10c;
    if (_DAT_ffff00009083e758 < 4) {
      puVar1 = (undefined4 *)&DAT_ffff00009083e740;
    }
    return *puVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0x1b6,
             s_g_boot_info_img_loaded_ffff0000907f0c24);
}



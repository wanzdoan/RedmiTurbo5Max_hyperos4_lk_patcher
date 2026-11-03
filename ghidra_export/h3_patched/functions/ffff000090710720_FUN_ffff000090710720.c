// ffff000090710720  size=128  FUN_ffff000090710720

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff000090710720(void)

{
  undefined4 uVar1;
  
  if ((DAT_ffff00009083f600 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x1aa,
               s_g_boot_info_hdr_loaded_ffff0000907d25a2);
  }
  if ((DAT_ffff00009083f608 & 1) != 0) {
    uVar1 = _DAT_ffff00009083f5fc;
    if (_DAT_ffff00009083e758 < 4) {
      uVar1 = 0;
    }
    return uVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x1ab,
             s_g_boot_info_img_loaded_ffff0000907f0c0d);
}



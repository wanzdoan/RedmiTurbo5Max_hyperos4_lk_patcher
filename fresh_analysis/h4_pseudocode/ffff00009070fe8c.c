
undefined4 FUN_ffff00009070fe8c(void)

{
  undefined4 *puVar1;
  
  if ((DAT_ffff00009083f638 & 1) != 0) {
    puVar1 = &DAT_ffff00009083f61c;
    if (DAT_ffff00009083e790 < 3) {
      puVar1 = &DAT_ffff00009083edd8;
    }
    return *puVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x62,
             s_g_boot_info_hdr_loaded_ffff0000907d25df);
}



// ffff00009071096c  size=92  FUN_ffff00009071096c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009071096c(void)

{
  undefined4 *puVar1;
  
  if ((DAT_ffff00009083f600 & 1) != 0) {
    puVar1 = (undefined4 *)&DAT_ffff00009083f5cc;
    if (_DAT_ffff00009083e758 < 3) {
      puVar1 = (undefined4 *)&DAT_ffff00009083e750;
    }
    return *puVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0x1ff,
             s_g_boot_info_hdr_loaded_ffff0000907d25a2);
}




undefined * FUN_ffff000090710040(void)

{
  undefined *puVar1;
  long lVar2;
  
  if ((DAT_ffff00009083f638 & 1) != 0) {
    lVar2 = FUN_ffff00009077f250(&DAT_ffff00009083ee04);
    puVar1 = (undefined *)0x0;
    if (lVar2 != 0) {
      puVar1 = &DAT_ffff00009083ee04;
    }
    return puVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0xbd,
             s_g_boot_info_hdr_loaded_ffff0000907d25df);
}



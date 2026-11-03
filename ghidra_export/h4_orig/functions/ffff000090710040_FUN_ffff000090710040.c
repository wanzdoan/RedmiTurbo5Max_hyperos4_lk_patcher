// ffff000090710040  size=92  FUN_ffff000090710040

undefined8 FUN_ffff000090710040(void)

{
  undefined8 uVar1;
  long lVar2;
  
  if ((DAT_ffff00009083f638 & 1) != 0) {
    lVar2 = FUN_ffff00009077f250(0xffff00009083ee04);
    uVar1 = 0;
    if (lVar2 != 0) {
      uVar1 = 0xffff00009083ee04;
    }
    return uVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0xbd,
             s_g_boot_info_hdr_loaded_ffff0000907d25df);
}



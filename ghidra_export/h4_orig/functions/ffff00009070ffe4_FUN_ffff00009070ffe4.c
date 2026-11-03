// ffff00009070ffe4  size=92  FUN_ffff00009070ffe4

undefined8 FUN_ffff00009070ffe4(void)

{
  undefined8 uVar1;
  long lVar2;
  
  if ((DAT_ffff00009083f638 & 1) != 0) {
    lVar2 = FUN_ffff00009077f250(0xffff00009083e7a8);
    uVar1 = 0;
    if (lVar2 != 0) {
      uVar1 = 0xffff00009083e7a8;
    }
    return uVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0xb7,
             s_g_boot_info_hdr_loaded_ffff0000907d25df);
}



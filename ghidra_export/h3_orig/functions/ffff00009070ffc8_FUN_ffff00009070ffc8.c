// ffff00009070ffc8  size=92  FUN_ffff00009070ffc8

undefined8 FUN_ffff00009070ffc8(void)

{
  undefined8 uVar1;
  long lVar2;
  
  if ((DAT_ffff00009083f600 & 1) != 0) {
    lVar2 = FUN_ffff00009077ef88(0xffff00009083edcc);
    uVar1 = 0;
    if (lVar2 != 0) {
      uVar1 = 0xffff00009083edcc;
    }
    return uVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0xbd,
             s_g_boot_info_hdr_loaded_ffff0000907d25a2);
}



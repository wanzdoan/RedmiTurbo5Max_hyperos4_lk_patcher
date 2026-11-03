
long FUN_ffff00009071039c(undefined8 param_1)

{
  uint *puVar1;
  uint local_18;
  undefined1 auStack_14 [4];
  
  local_18 = 0;
  FUN_ffff000090710448(param_1,&local_18,auStack_14);
  if ((DAT_ffff00009083f638 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x10d,
               s_g_boot_info_hdr_loaded_ffff0000907d25df);
  }
  if ((DAT_ffff00009083f640 & 1) != 0) {
    puVar1 = &DAT_ffff00009083edf4;
    if (DAT_ffff00009083e790 < 3) {
      puVar1 = &DAT_ffff00009083e78c;
    }
    return DAT_ffff00009083f650 + (ulong)local_18 + (ulong)*puVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x10e,
             s_g_boot_info_img_loaded_ffff0000907f0d95);
}



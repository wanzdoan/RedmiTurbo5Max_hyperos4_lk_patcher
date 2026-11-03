// ffff00009070ff88  size=92  FUN_ffff00009070ff88

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009070ff88(void)

{
  undefined4 *puVar1;
  
  if ((DAT_ffff00009083f638 & 1) != 0) {
    puVar1 = (undefined4 *)&DAT_ffff00009083edf8;
    if (_DAT_ffff00009083e790 < 3) {
      puVar1 = (undefined4 *)&DAT_ffff00009083e774;
    }
    return *puVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x9f,
             s_g_boot_info_hdr_loaded_ffff0000907d25df);
}



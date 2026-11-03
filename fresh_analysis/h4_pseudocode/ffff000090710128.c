
int FUN_ffff000090710128(void)

{
  uint *puVar1;
  uint uVar2;
  uint uVar3;
  
  if ((DAT_ffff00009083f638 & 1) != 0) {
    puVar1 = &DAT_ffff00009083edf4;
    if (DAT_ffff00009083e790 < 3) {
      puVar1 = &DAT_ffff00009083e78c;
    }
    uVar2 = *puVar1;
    uVar3 = 0;
    if (uVar2 != 0) {
      uVar3 = ((uVar2 + DAT_ffff00009083e770) - 1) / uVar2;
    }
    return uVar3 * uVar2;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0xd5,
             s_g_boot_info_hdr_loaded_ffff0000907d25df);
}




long FUN_ffff000090710650(void)

{
  uint *puVar1;
  int *piVar2;
  uint uVar3;
  uint uVar4;
  
  if ((DAT_ffff00009083f638 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x19d,
               s_g_boot_info_hdr_loaded_ffff0000907d25df);
  }
  if ((DAT_ffff00009083f640 & 1) != 0) {
    puVar1 = &DAT_ffff00009083edf4;
    if (DAT_ffff00009083e790 < 3) {
      puVar1 = &DAT_ffff00009083e78c;
    }
    piVar2 = &DAT_ffff00009083f61c;
    if (DAT_ffff00009083e790 < 3) {
      piVar2 = &DAT_ffff00009083edd8;
    }
    uVar3 = *puVar1;
    uVar4 = 0;
    if (uVar3 != 0) {
      uVar4 = ((DAT_ffff00009083ee00 + uVar3) - 1) / uVar3;
    }
    return DAT_ffff00009083f650 + (ulong)uVar3 + (long)(int)(uVar4 * uVar3) +
           (ulong)(*piVar2 + (uVar3 - 1) & -uVar3) +
           (ulong)(DAT_ffff00009083f628 + (uVar3 - 1) & -uVar3);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x19e,
             s_g_boot_info_img_loaded_ffff0000907f0d95);
}



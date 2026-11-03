// ffff0000907105d8  size=216  FUN_ffff0000907105d8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff0000907105d8(void)

{
  uint *puVar1;
  int *piVar2;
  uint uVar3;
  uint uVar4;
  
  if ((DAT_ffff00009083f600 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0x19d,
               s_g_boot_info_hdr_loaded_ffff0000907d25a2);
  }
  if ((DAT_ffff00009083f608 & 1) != 0) {
    puVar1 = (uint *)&DAT_ffff00009083edbc;
    if (_DAT_ffff00009083e758 < 3) {
      puVar1 = (uint *)&DAT_ffff00009083e754;
    }
    piVar2 = (int *)&DAT_ffff00009083f5e4;
    if (_DAT_ffff00009083e758 < 3) {
      piVar2 = (int *)&DAT_ffff00009083eda0;
    }
    uVar3 = *puVar1;
    uVar4 = 0;
    if (uVar3 != 0) {
      uVar4 = ((_DAT_ffff00009083edc8 + uVar3) - 1) / uVar3;
    }
    return _DAT_ffff00009083f618 + (ulong)uVar3 + (long)(int)(uVar4 * uVar3) +
           (ulong)(*piVar2 + (uVar3 - 1) & -uVar3) +
           (ulong)(_DAT_ffff00009083f5f0 + (uVar3 - 1) & -uVar3);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0x19e,
             s_g_boot_info_img_loaded_ffff0000907f0c24);
}



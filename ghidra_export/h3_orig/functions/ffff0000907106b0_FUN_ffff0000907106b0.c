// ffff0000907106b0  size=112  FUN_ffff0000907106b0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907106b0(void)

{
  uint *puVar1;
  uint uVar2;
  uint uVar3;
  
  if ((DAT_ffff00009083f600 & 1) != 0) {
    puVar1 = (uint *)&DAT_ffff00009083edbc;
    if (_DAT_ffff00009083e758 < 3) {
      puVar1 = (uint *)&DAT_ffff00009083e754;
    }
    uVar2 = *puVar1;
    uVar3 = 0;
    if (uVar2 != 0) {
      uVar3 = ((_DAT_ffff00009083edc8 + uVar2) - 1) / uVar2;
    }
    return uVar3 * uVar2;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0x94,
             s_g_boot_info_hdr_loaded_ffff0000907d25a2);
}



// ffff0000907101c4  size=212  FUN_ffff0000907101c4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff0000907101c4(void)

{
  uint *puVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  long lVar5;
  
  if ((DAT_ffff00009083f600 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0xf6,
               s_g_boot_info_hdr_loaded_ffff0000907d25a2);
  }
  if ((DAT_ffff00009083f608 & 1) != 0) {
    puVar1 = (uint *)&DAT_ffff00009083edbc;
    if (_DAT_ffff00009083e758 < 3) {
      puVar1 = (uint *)&DAT_ffff00009083e754;
    }
    uVar2 = *puVar1;
    uVar3 = (uVar2 + _DAT_ffff00009083e738) - 1;
    uVar4 = 0;
    if (uVar2 != 0) {
      uVar4 = uVar3 / uVar2;
    }
    if (uVar3 == uVar3 - uVar4 * uVar2) {
      lVar5 = 0;
    }
    else if (_DAT_ffff00009083e758 < 4) {
      lVar5 = (ulong)uVar2 + (ulong)(uVar4 * uVar2) + _DAT_ffff00009083f610;
    }
    else {
      lVar5 = _DAT_ffff00009083f620 + (ulong)uVar2;
    }
    return lVar5;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0xf7,
             s_g_boot_info_img_loaded_ffff0000907f0c24);
}




long FUN_ffff00009071023c(void)

{
  uint *puVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  long lVar5;
  
  if ((DAT_ffff00009083f638 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0xf6,
               s_g_boot_info_hdr_loaded_ffff0000907d25df);
  }
  if ((DAT_ffff00009083f640 & 1) != 0) {
    puVar1 = &DAT_ffff00009083edf4;
    if (DAT_ffff00009083e790 < 3) {
      puVar1 = &DAT_ffff00009083e78c;
    }
    uVar2 = *puVar1;
    uVar3 = (uVar2 + DAT_ffff00009083e770) - 1;
    uVar4 = 0;
    if (uVar2 != 0) {
      uVar4 = uVar3 / uVar2;
    }
    if (uVar3 == uVar3 - uVar4 * uVar2) {
      lVar5 = 0;
    }
    else if (DAT_ffff00009083e790 < 4) {
      lVar5 = (ulong)uVar2 + (ulong)(uVar4 * uVar2) + DAT_ffff00009083f648;
    }
    else {
      lVar5 = DAT_ffff00009083f658 + (ulong)uVar2;
    }
    return lVar5;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0xf7,
             s_g_boot_info_img_loaded_ffff0000907f0d95);
}



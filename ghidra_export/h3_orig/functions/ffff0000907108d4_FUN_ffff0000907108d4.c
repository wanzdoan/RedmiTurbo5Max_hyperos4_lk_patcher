// ffff0000907108d4  size=152  FUN_ffff0000907108d4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907108d4(void)

{
  int iVar1;
  int *piVar2;
  
  if ((DAT_ffff00009083f600 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0x1f2,
               s_g_boot_info_hdr_loaded_ffff0000907d25a2);
  }
  if ((DAT_ffff00009083f608 & 1) != 0) {
    piVar2 = (int *)&DAT_ffff00009083e10c;
    if (_DAT_ffff00009083e758 < 4) {
      piVar2 = (int *)&DAT_ffff00009083e740;
    }
    iVar1 = _DAT_ffff00009083edc8;
    if (_DAT_ffff00009083e758 < 3) {
      iVar1 = 0;
    }
    return iVar1 + *piVar2;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ac8,0x1b6,
             s_g_boot_info_img_loaded_ffff0000907f0c24);
}



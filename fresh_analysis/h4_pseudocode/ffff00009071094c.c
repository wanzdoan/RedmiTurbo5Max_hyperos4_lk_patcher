
int FUN_ffff00009071094c(void)

{
  int iVar1;
  int *piVar2;
  
  if ((DAT_ffff00009083f638 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x1f2,
               s_g_boot_info_hdr_loaded_ffff0000907d25df);
  }
  if ((DAT_ffff00009083f640 & 1) != 0) {
    piVar2 = &DAT_ffff00009083e144;
    if (DAT_ffff00009083e790 < 4) {
      piVar2 = &DAT_ffff00009083e778;
    }
    iVar1 = DAT_ffff00009083ee00;
    if (DAT_ffff00009083e790 < 3) {
      iVar1 = 0;
    }
    return iVar1 + *piVar2;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x1b6,
             s_g_boot_info_img_loaded_ffff0000907f0d95);
}



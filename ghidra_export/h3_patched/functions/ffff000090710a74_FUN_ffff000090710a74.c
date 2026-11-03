// ffff000090710a74  size=212  FUN_ffff000090710a74

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090710a74(void)

{
  int iVar1;
  
  if (3 < _DAT_ffff00009083e758) {
    iVar1 = FUN_ffff00009077f080(0xffff00009083edb0,s_VNDRBOOT_ffff0000907df37b,8);
    if (iVar1 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x240,
                 s__strncmp__const_char___g_vendor__ffff0000907ed98c);
    }
    iVar1 = FUN_ffff00009077f080(0xffff00009083e100,s_ANDROID__ffff0000907ebde1,8);
    if (iVar1 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x242,
                 s__strncmp__const_char___g_init_bo_ffff0000907e2453);
    }
  }
  iVar1 = FUN_ffff00009077f080(0xffff00009083e730,s_ANDROID__ffff0000907ebde1,8);
  if (iVar1 == 0) {
    DAT_ffff00009083f600 = 1;
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x246,
             s__strncmp__const_char___g_bootimg_ffff0000907e7390);
}



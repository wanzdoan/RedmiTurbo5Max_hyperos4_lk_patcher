
void FUN_ffff000090710634(void)

{
  int iVar1;

  if (3 < DAT_ffff000090836758) {
    iVar1 = FUN_ffff00009077fc2c(&DAT_ffff000090836db0,s_VNDRBOOT_ffff0000907d798b,8);
    if (iVar1 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d934
                (s_app_mt_boot_boot_info_c_ffff0000907d90c4,0x240,
                 s__strncmp__const_char___g_vendor__ffff0000907e66b3);
    }
    iVar1 = FUN_ffff00009077fc2c(&DAT_ffff000090836100,s_ANDROID__ffff0000907e4ab6,8);
    if (iVar1 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d934
                (s_app_mt_boot_boot_info_c_ffff0000907d90c4,0x242,
                 s__strncmp__const_char___g_init_bo_ffff0000907dac35);
    }
  }
  iVar1 = FUN_ffff00009077fc2c(&DAT_ffff000090836730,s_ANDROID__ffff0000907e4ab6,8);
  if (iVar1 == 0) {
    DAT_ffff000090837600 = 1;
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077d934
            (s_app_mt_boot_boot_info_c_ffff0000907d90c4,0x246,
             s__strncmp__const_char___g_bootimg_ffff0000907dfd0b);
}

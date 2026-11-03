
void FUN_ffff000090710aec(void)

{
  int iVar1;
  
  if (3 < DAT_ffff00009083e790) {
    iVar1 = FUN_ffff00009077f348(&DAT_ffff00009083ede8,s_VNDRBOOT_ffff0000907df435,8);
    if (iVar1 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x240,
                 s__strncmp__const_char___g_vendor__ffff0000907edb14);
    }
    iVar1 = FUN_ffff00009077f348(&DAT_ffff00009083e138,s_ANDROID__ffff0000907ebf7b,8);
    if (iVar1 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x242,
                 s__strncmp__const_char___g_init_bo_ffff0000907e253b);
    }
  }
  iVar1 = FUN_ffff00009077f348(&DAT_ffff00009083e768,s_ANDROID__ffff0000907ebf7b,8);
  if (iVar1 == 0) {
    DAT_ffff00009083f638 = 1;
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x246,
             s__strncmp__const_char___g_bootimg_ffff0000907e74eb);
}



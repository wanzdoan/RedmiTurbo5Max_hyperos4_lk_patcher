
undefined8 FUN_ffff0000be087c64(ulong param_1)

{
  int iVar1;
  
  iVar1 = *(int *)PTR_DAT_ffff0000be0f5738;
  if (iVar1 == 0x22) {
    iVar1 = FUN_ffff0000be0852f8();
    if (iVar1 == 1) {
      if ((param_1 & 1) == 0) {
        return 1;
      }
      FUN_ffff0000be05fd98(s___s__S_CHIP_ffff0000be0bc9a1,&DAT_ffff0000be0bb811);
      return 1;
    }
    if ((param_1 & 1) != 0) {
      FUN_ffff0000be05fd98(s___s__NS_CHIP_ffff0000be0b176a,&DAT_ffff0000be0bb811);
    }
  }
  else {
    if (iVar1 == 0x11) {
      return 1;
    }
    if (iVar1 != 0) {
      FUN_ffff0000be05fd98
                (s___s__invalid_sboot_config__0x_x__ffff0000be0bdcd2,&DAT_ffff0000be0bb811,
                 *(undefined4 *)(PTR_DAT_ffff0000be0f5730 + 0xe8));
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_mt6991_sboot_s_ffff0000be0af235,0x118,&DAT_ffff0000be0a553d);
    }
    if (DAT_ffff0000be1b6ad8 == '\0') {
      DAT_ffff0000be1b6ad8 = '\x01';
      FUN_ffff0000be067a28(3);
    }
  }
  return 0;
}



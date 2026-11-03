
undefined8 FUN_ffff0000b806a268(ulong param_1)

{
  int iVar1;
  
  iVar1 = *(int *)PTR_DAT_ffff0000b8108808;
  if (iVar1 == 0x22) {
    iVar1 = FUN_ffff0000b8067938();
    if (iVar1 == 1) {
      if ((param_1 & 1) == 0) {
        return 1;
      }
      FUN_ffff0000b804c33c(s___s__S_CHIP_ffff0000b8099629,&DAT_ffff0000b80987e6);
      return 1;
    }
    if ((param_1 & 1) != 0) {
      FUN_ffff0000b804c33c(s___s__NS_CHIP_ffff0000b8090585,&DAT_ffff0000b80987e6);
    }
  }
  else {
    if (iVar1 == 0x11) {
      return 1;
    }
    if (iVar1 != 0) {
      FUN_ffff0000b804c33c
                (s___s__invalid_sboot_config__0x_x__ffff0000b809a449,&DAT_ffff0000b80987e6,
                 *(undefined4 *)(PTR_DAT_ffff0000b8108800 + 0xe8));
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b830
                (s_platform_mediatek_mt6991_sboot_s_ffff0000b808e718,0x118,&DAT_ffff0000b8086efb);
    }
    if (DAT_ffff0000b81b8818 == '\0') {
      DAT_ffff0000b81b8818 = '\x01';
      FUN_ffff0000b8052eb4(3);
    }
  }
  return 0;
}



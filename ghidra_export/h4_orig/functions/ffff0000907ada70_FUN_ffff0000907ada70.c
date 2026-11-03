// ffff0000907ada70  size=228  FUN_ffff0000907ada70

undefined8 FUN_ffff0000907ada70(ulong param_1)

{
  int iVar1;
  
  iVar1 = *(int *)PTR_DAT_ffff000090839ac8;
  if (iVar1 == 0x22) {
    iVar1 = FUN_ffff0000907aa304();
    if (iVar1 == 1) {
      if ((param_1 & 1) == 0) {
        return 1;
      }
      FUN_ffff00009077e11c(s___s__S_CHIP_ffff0000907f3b6f,&DAT_ffff0000907f228a);
      return 1;
    }
    if ((param_1 & 1) != 0) {
      FUN_ffff00009077e11c(s___s__NS_CHIP_ffff0000907e3df2,&DAT_ffff0000907f228a);
    }
  }
  else {
    if (iVar1 == 0x11) {
      return 1;
    }
    if (iVar1 != 0) {
      FUN_ffff00009077e11c
                (s___s__invalid_sboot_config__0x_x__ffff0000907f5971,&DAT_ffff0000907f228a,
                 *(undefined4 *)(DAT_ffff000090839ab8 + 0xe8));
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_mt6991_sboot_s_ffff0000907e0726,0x118,&DAT_ffff0000907d2506);
    }
    if (DAT_ffff00009090c5e0 == '\0') {
      DAT_ffff00009090c5e0 = '\x01';
      FUN_ffff000090788a54(3);
    }
  }
  return 0;
}



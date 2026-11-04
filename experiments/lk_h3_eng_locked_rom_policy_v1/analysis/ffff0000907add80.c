
undefined8 FUN_ffff0000907add80(ulong param_1)

{
  int iVar1;

  iVar1 = *(int *)PTR_DAT_ffff000090831818;
  if (iVar1 == 0x22) {
    iVar1 = FUN_ffff0000907aa614();
    if (iVar1 == 1) {
      if ((param_1 & 1) == 0) {
        return 1;
      }
      FUN_ffff00009077ea84(s___s__S_CHIP_ffff0000907ec84e,&DAT_ffff0000907eae49);
      return 1;
    }
    if ((param_1 & 1) != 0) {
      FUN_ffff00009077ea84(s___s__NS_CHIP_ffff0000907dc4fe,&DAT_ffff0000907eae49);
    }
  }
  else {
    if (iVar1 == 0x11) {
      return 1;
    }
    if (iVar1 != 0) {
      FUN_ffff00009077ea84
                (s___s__invalid_sboot_config__0x_x__ffff0000907ee5f0,&DAT_ffff0000907eae49,
                 *(undefined4 *)(PTR_DAT_ffff000090831808 + 0xe8));
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d934
                (s_platform_mediatek_mt6991_sboot_s_ffff0000907d8d36,0x117,&DAT_ffff0000907ca5f8);
    }
    if (DAT_ffff00009090c520 == '\0') {
      DAT_ffff00009090c520 = '\x01';
      FUN_ffff000090788640(3);
    }
  }
  return 0;
}

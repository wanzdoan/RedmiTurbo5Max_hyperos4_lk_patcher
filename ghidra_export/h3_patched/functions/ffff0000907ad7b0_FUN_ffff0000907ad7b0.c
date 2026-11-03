// ffff0000907ad7b0  size=228  FUN_ffff0000907ad7b0

undefined8 FUN_ffff0000907ad7b0(ulong param_1)

{
  int iVar1;
  
  iVar1 = *(int *)PTR_DAT_ffff000090839aa8;
  if (iVar1 == 0x22) {
    iVar1 = FUN_ffff0000907aa044();
    if (iVar1 == 1) {
      if ((param_1 & 1) == 0) {
        return 1;
      }
      FUN_ffff00009077de54(s___s__S_CHIP_ffff0000907f39cc,&DAT_ffff0000907f20e7);
      return 1;
    }
    if ((param_1 & 1) != 0) {
      FUN_ffff00009077de54(s___s__NS_CHIP_ffff0000907e3d0a,&DAT_ffff0000907f20e7);
    }
  }
  else {
    if (iVar1 == 0x11) {
      return 1;
    }
    if (iVar1 != 0) {
      FUN_ffff00009077de54
                (s___s__invalid_sboot_config__0x_x__ffff0000907f57ed,&DAT_ffff0000907f20e7,
                 *(undefined4 *)(DAT_ffff000090839a98 + 0xe8));
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_mt6991_sboot_s_ffff0000907e066c,0x117,&DAT_ffff0000907d24c9);
    }
    if (DAT_ffff00009090c520 == '\0') {
      DAT_ffff00009090c520 = '\x01';
      FUN_ffff000090788794(3);
    }
  }
  return 0;
}



// ffff000090762ff8  size=236  FUN_ffff000090762ff8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090762ff8(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_fastboot_m_ffff0000907fdd50,
               s_CheckBlackboxFastbootModelogHead_ffff0000907ecd62,0x563);
  }
  else {
    uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c04,&DAT_ffff00009087c3f8,0xac60c00,0x22c);
    if ((uVar1 & 1) != 0) {
      if (_DAT_ffff00009087c3f8 != 0x5e9f1d3b7a8c2e0f) {
        FUN_ffff00009077de54
                  (s__s__d____s___init_fastboot_mode__ffff0000907e0084,
                   s_CheckBlackboxFastbootModelogHead_ffff0000907ecd62,0x56e,
                   s_BLACKBOX_ffff0000907d9988);
        FUN_ffff000090760fb8(&DAT_ffff00009087c3f8);
      }
      FUN_ffff000090761c44(&DAT_ffff00009087c3f8);
      return 1;
    }
    FUN_ffff00009077de54
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
               s_CheckBlackboxFastbootModelogHead_ffff0000907ecd62,0x56a,s_BLACKBOX_ffff0000907d9988
              );
  }
  return 0;
}



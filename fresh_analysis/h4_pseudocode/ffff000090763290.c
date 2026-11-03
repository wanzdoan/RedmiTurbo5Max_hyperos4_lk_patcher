
undefined8 FUN_ffff000090763290(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_fastboot_m_ffff0000907fdef8,
               s_CheckBlackboxFastbootModelogHead_ffff0000907ecefc,0x563);
  }
  else {
    uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c490,0xac60c00,0x22c);
    if ((uVar1 & 1) != 0) {
      if (DAT_ffff00009087c490 != 0x5e9f1d3b7a8c2e0f) {
        FUN_ffff00009077e11c
                  (s__s__d____s___init_fastboot_mode__ffff0000907e013e,
                   s_CheckBlackboxFastbootModelogHead_ffff0000907ecefc,0x56e,
                   s_BLACKBOX_ffff0000907d9a1f);
        FUN_ffff000090761250(&DAT_ffff00009087c490);
      }
      FUN_ffff000090761edc(&DAT_ffff00009087c490);
      return 1;
    }
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxFastbootModelogHead_ffff0000907ecefc,0x56a,s_BLACKBOX_ffff0000907d9a1f
              );
  }
  return 0;
}




undefined8 FUN_ffff0000be04f370(void)

{
  ulong uVar1;
  
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_fastboot_m_ffff0000be0c3422,
               s_CheckBlackboxFastbootModelogHead_ffff0000be0b7dd2,0x563);
  }
  else {
    uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be1266c8,0xac60c00,0x22c);
    if ((uVar1 & 1) != 0) {
      if (DAT_ffff0000be1266c8 != 0x5e9f1d3b7a8c2e0f) {
        FUN_ffff0000be05fd98
                  (s__s__d____s___init_fastboot_mode__ffff0000be0aed91,
                   s_CheckBlackboxFastbootModelogHead_ffff0000be0b7dd2,0x56e,
                   s_BLACKBOX_ffff0000be0aa9ee);
        FUN_ffff0000be04d330(&DAT_ffff0000be1266c8);
      }
      FUN_ffff0000be04dfbc(&DAT_ffff0000be1266c8);
      return 1;
    }
    FUN_ffff0000be05fd98
              (s__s__d____s___GetPartitionInfo_fa_ffff0000be0c10b3,
               s_CheckBlackboxFastbootModelogHead_ffff0000be0b7dd2,0x56a,s_BLACKBOX_ffff0000be0aa9ee
              );
  }
  return 0;
}



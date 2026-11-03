
undefined8 FUN_ffff0000be04f45c(void)

{
  ulong uVar1;
  
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_recovery_m_ffff0000be0a6259,
               s_CheckBlackboxRecoveryModelogHead_ffff0000be0beb23,0x57a);
  }
  else {
    uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be1268f8,0xb00a800,0x22c);
    if ((uVar1 & 1) != 0) {
      if (DAT_ffff0000be1268f8 != 0xc4a8e6d2f3b5d9a) {
        FUN_ffff0000be05fd98
                  (s__s__d____s___init_recovery_mode__ffff0000be0c21c3,
                   s_CheckBlackboxRecoveryModelogHead_ffff0000be0beb23,0x585,
                   s_BLACKBOX_ffff0000be0aa9ee);
        FUN_ffff0000be04d454(&DAT_ffff0000be1268f8);
      }
      FUN_ffff0000be05fd98
                (s__s__d____s___recovery_mode_log_o_ffff0000be0b90e8,
                 s_CheckBlackboxRecoveryModelogHead_ffff0000be0beb23,0x588,
                 s_BLACKBOX_ffff0000be0aa9ee,DAT_ffff0000be12622c._4_4_);
      FUN_ffff0000be04e218(&DAT_ffff0000be1268f8);
      return 1;
    }
    FUN_ffff0000be05fd98
              (s__s__d____s___GetPartitionInfo_fa_ffff0000be0c10b3,
               s_CheckBlackboxRecoveryModelogHead_ffff0000be0beb23,0x581,s_BLACKBOX_ffff0000be0aa9ee
              );
  }
  return 0;
}



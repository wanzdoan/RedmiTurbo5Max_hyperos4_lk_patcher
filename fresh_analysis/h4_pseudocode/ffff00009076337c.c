
undefined8 FUN_ffff00009076337c(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_recovery_m_ffff0000907d359a,
               s_CheckBlackboxRecoveryModelogHead_ffff0000907f6f9e,0x57a);
  }
  else {
    uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c6c0,0xb00a800,0x22c);
    if ((uVar1 & 1) != 0) {
      if (DAT_ffff00009087c6c0 != 0xc4a8e6d2f3b5d9a) {
        FUN_ffff00009077e11c
                  (s__s__d____s___init_recovery_mode__ffff0000907fc121,
                   s_CheckBlackboxRecoveryModelogHead_ffff0000907f6f9e,0x585,
                   s_BLACKBOX_ffff0000907d9a1f);
        FUN_ffff000090761374(&DAT_ffff00009087c6c0);
      }
      FUN_ffff00009077e11c
                (s__s__d____s___recovery_mode_log_o_ffff0000907eeaab,
                 s_CheckBlackboxRecoveryModelogHead_ffff0000907f6f9e,0x588,
                 s_BLACKBOX_ffff0000907d9a1f,DAT_ffff00009087bff4._4_4_);
      FUN_ffff000090762138(&DAT_ffff00009087c6c0);
      return 1;
    }
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxRecoveryModelogHead_ffff0000907f6f9e,0x581,s_BLACKBOX_ffff0000907d9a1f
              );
  }
  return 0;
}



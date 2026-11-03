// ffff0000907630e4  size=276  FUN_ffff0000907630e4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907630e4(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_recovery_m_ffff0000907d355d,
               s_CheckBlackboxRecoveryModelogHead_ffff0000907f6dfd,0x57a);
  }
  else {
    uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c04,&DAT_ffff00009087c628,0xb00a800,0x22c);
    if ((uVar1 & 1) != 0) {
      if (_DAT_ffff00009087c628 != 0xc4a8e6d2f3b5d9a) {
        FUN_ffff00009077de54
                  (s__s__d____s___init_recovery_mode__ffff0000907fbf85,
                   s_CheckBlackboxRecoveryModelogHead_ffff0000907f6dfd,0x585,
                   s_BLACKBOX_ffff0000907d9988);
        FUN_ffff0000907610dc(&DAT_ffff00009087c628);
      }
      FUN_ffff00009077de54
                (s__s__d____s___recovery_mode_log_o_ffff0000907ee923,
                 s_CheckBlackboxRecoveryModelogHead_ffff0000907f6dfd,0x588,
                 s_BLACKBOX_ffff0000907d9988,_DAT_ffff00009087bf60);
      FUN_ffff000090761ea0(&DAT_ffff00009087c628);
      return 1;
    }
    FUN_ffff00009077de54
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
               s_CheckBlackboxRecoveryModelogHead_ffff0000907f6dfd,0x581,s_BLACKBOX_ffff0000907d9988
              );
  }
  return 0;
}



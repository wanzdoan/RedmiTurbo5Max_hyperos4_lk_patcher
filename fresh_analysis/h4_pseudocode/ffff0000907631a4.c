
undefined8 FUN_ffff0000907631a4(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_exception_ffff0000907f6ee6,
               s_CheckBlackboxExceptionShutdownlo_ffff0000907e1d28,0x54c);
  }
  else {
    uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c300,0xac10800,0x18c);
    if ((uVar1 & 1) != 0) {
      if (DAT_ffff00009087c300 != -0x581d3b49270f5e2d) {
        FUN_ffff00009077e11c
                  (s__s__d____s___init_exception_shut_ffff0000907d1a24,
                   s_CheckBlackboxExceptionShutdownlo_ffff0000907e1d28,0x557,
                   s_BLACKBOX_ffff0000907d9a1f);
        FUN_ffff000090761170(&DAT_ffff00009087c300);
      }
      FUN_ffff000090761cf4(&DAT_ffff00009087c300);
      return 1;
    }
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxExceptionShutdownlo_ffff0000907e1d28,0x553,s_BLACKBOX_ffff0000907d9a1f
              );
  }
  return 0;
}



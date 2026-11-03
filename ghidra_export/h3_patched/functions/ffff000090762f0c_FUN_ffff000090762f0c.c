// ffff000090762f0c  size=236  FUN_ffff000090762f0c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090762f0c(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_exception_ffff0000907f6d45,
               s_CheckBlackboxExceptionShutdownlo_ffff0000907e1c40,0x54c);
  }
  else {
    uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c04,&DAT_ffff00009087c268,0xac10800,0x18c);
    if ((uVar1 & 1) != 0) {
      if (_DAT_ffff00009087c268 != -0x581d3b49270f5e2d) {
        FUN_ffff00009077de54
                  (s__s__d____s___init_exception_shut_ffff0000907d19e7,
                   s_CheckBlackboxExceptionShutdownlo_ffff0000907e1c40,0x557,
                   s_BLACKBOX_ffff0000907d9988);
        FUN_ffff000090760ed8(&DAT_ffff00009087c268);
      }
      FUN_ffff000090761a5c(&DAT_ffff00009087c268);
      return 1;
    }
    FUN_ffff00009077de54
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
               s_CheckBlackboxExceptionShutdownlo_ffff0000907e1c40,0x553,s_BLACKBOX_ffff0000907d9988
              );
  }
  return 0;
}



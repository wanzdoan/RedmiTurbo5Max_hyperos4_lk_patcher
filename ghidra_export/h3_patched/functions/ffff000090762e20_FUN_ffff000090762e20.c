// ffff000090762e20  size=236  FUN_ffff000090762e20

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090762e20(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_exception_ffff0000907f3331,
               s_CheckBlackboxExceptionRebootlogH_ffff0000907e5190,0x535);
  }
  else {
    uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c04,&DAT_ffff00009087bf70,0xa440400,0x2f4);
    if ((uVar1 & 1) != 0) {
      if (_DAT_ffff00009087bf70 != 0x3b9d5f1e7c8a2e4f) {
        FUN_ffff00009077de54
                  (s__s__d____s___init_exception_rebo_ffff0000907fddc7,
                   s_CheckBlackboxExceptionRebootlogH_ffff0000907e5190,0x540,
                   s_BLACKBOX_ffff0000907d9988);
        FUN_ffff000090760df4(&DAT_ffff00009087bf70);
      }
      FUN_ffff000090761874(&DAT_ffff00009087bf70);
      return 1;
    }
    FUN_ffff00009077de54
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
               s_CheckBlackboxExceptionRebootlogH_ffff0000907e5190,0x53c,s_BLACKBOX_ffff0000907d9988
              );
  }
  return 0;
}



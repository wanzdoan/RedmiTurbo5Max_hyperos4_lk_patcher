// ffff0000907630b8  size=236  FUN_ffff0000907630b8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907630b8(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_exception_ffff0000907f34d4,
               s_CheckBlackboxExceptionRebootlogH_ffff0000907e52ab,0x535);
  }
  else {
    uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c008,0xa440400,0x2f4);
    if ((uVar1 & 1) != 0) {
      if (_DAT_ffff00009087c008 != 0x3b9d5f1e7c8a2e4f) {
        FUN_ffff00009077e11c
                  (s__s__d____s___init_exception_rebo_ffff0000907fdf6f,
                   s_CheckBlackboxExceptionRebootlogH_ffff0000907e52ab,0x540,
                   s_BLACKBOX_ffff0000907d9a1f);
        FUN_ffff00009076108c(&DAT_ffff00009087c008);
      }
      FUN_ffff000090761b0c(&DAT_ffff00009087c008);
      return 1;
    }
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxExceptionRebootlogH_ffff0000907e52ab,0x53c,s_BLACKBOX_ffff0000907d9a1f
              );
  }
  return 0;
}



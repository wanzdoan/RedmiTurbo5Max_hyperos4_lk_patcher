
undefined8 FUN_ffff0000be04f198(void)

{
  ulong uVar1;
  
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_exception_ffff0000be0bc52f,
               s_CheckBlackboxExceptionRebootlogH_ffff0000be0b24ed,0x535);
  }
  else {
    uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126240,0xa440400,0x2f4);
    if ((uVar1 & 1) != 0) {
      if (DAT_ffff0000be126240 != 0x3b9d5f1e7c8a2e4f) {
        FUN_ffff0000be05fd98
                  (s__s__d____s___init_exception_rebo_ffff0000be0c3499,
                   s_CheckBlackboxExceptionRebootlogH_ffff0000be0b24ed,0x540,
                   s_BLACKBOX_ffff0000be0aa9ee);
        FUN_ffff0000be04d16c(&DAT_ffff0000be126240);
      }
      FUN_ffff0000be04dbec(&DAT_ffff0000be126240);
      return 1;
    }
    FUN_ffff0000be05fd98
              (s__s__d____s___GetPartitionInfo_fa_ffff0000be0c10b3,
               s_CheckBlackboxExceptionRebootlogH_ffff0000be0b24ed,0x53c,s_BLACKBOX_ffff0000be0aa9ee
              );
  }
  return 0;
}



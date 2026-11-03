
undefined8 FUN_ffff0000be04f284(void)

{
  ulong uVar1;
  
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_exception_ffff0000be0bea6b,
               s_CheckBlackboxExceptionShutdownlo_ffff0000be0b02e5,0x54c);
  }
  else {
    uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126538,0xac10800,0x18c);
    if ((uVar1 & 1) != 0) {
      if (DAT_ffff0000be126538 != -0x581d3b49270f5e2d) {
        FUN_ffff0000be05fd98
                  (s__s__d____s___init_exception_shut_ffff0000be0a4eee,
                   s_CheckBlackboxExceptionShutdownlo_ffff0000be0b02e5,0x557,
                   s_BLACKBOX_ffff0000be0aa9ee);
        FUN_ffff0000be04d250(&DAT_ffff0000be126538);
      }
      FUN_ffff0000be04ddd4(&DAT_ffff0000be126538);
      return 1;
    }
    FUN_ffff0000be05fd98
              (s__s__d____s___GetPartitionInfo_fa_ffff0000be0c10b3,
               s_CheckBlackboxExceptionShutdownlo_ffff0000be0b02e5,0x553,s_BLACKBOX_ffff0000be0aa9ee
              );
  }
  return 0;
}



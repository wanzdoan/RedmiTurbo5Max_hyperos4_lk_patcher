// ffff0000907c314c  size=12  FUN_ffff0000907c314c

void FUN_ffff0000907c314c(long param_1)

{
  long lVar1;
  
  if (param_1 != 0) {
    lVar1 = *(long *)(param_1 + -0x10);
    *(undefined8 *)(lVar1 + 0x10) = *(undefined8 *)(param_1 + -8);
    FUN_ffff0000907c3170(lVar1);
    return;
  }
  return;
}



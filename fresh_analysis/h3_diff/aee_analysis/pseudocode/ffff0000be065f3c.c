
undefined8 FUN_ffff0000be065f3c(uint param_1,int param_2)

{
  long lVar1;
  
  if (0x10e < param_1) {
    return 0xfffffffe;
  }
  if ((long)(char)(&DAT_ffff0000be0efbf8)[param_1] != 0xffffffffffffffff) {
    lVar1 = 8;
    if (param_2 != 0) {
      lVar1 = 4;
    }
    *(int *)(*(long *)(&DAT_ffff0000be0ef380 + (ulong)param_1 * 8) + lVar1) =
         (int)(1L << ((long)(char)(&DAT_ffff0000be0efbf8)[param_1] & 0x3fU));
    return 0;
  }
  return 0xffffffff;
}



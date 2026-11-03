// ffff000090785794  size=52  FUN_ffff000090785794

undefined8 FUN_ffff000090785794(uint param_1,int param_2)

{
  long lVar1;
  
  if (0x10e < param_1) {
    return 0xfffffffe;
  }
  if ((long)(char)(&DAT_ffff000090833208)[param_1] != 0xffffffffffffffff) {
    lVar1 = 8;
    if (param_2 != 0) {
      lVar1 = 4;
    }
    *(int *)(*(long *)(&DAT_ffff000090832990 + (ulong)param_1 * 8) + lVar1) =
         (int)(1L << ((long)(char)(&DAT_ffff000090833208)[param_1] & 0x3fU));
    return 0;
  }
  return 0xffffffff;
}



// ffff0000907854cc  size=52  FUN_ffff0000907854cc

undefined8 FUN_ffff0000907854cc(uint param_1,int param_2)

{
  long lVar1;
  
  if (0x10e < param_1) {
    return 0xfffffffe;
  }
  if ((long)(char)(&DAT_ffff0000908331e8)[param_1] != 0xffffffffffffffff) {
    lVar1 = 8;
    if (param_2 != 0) {
      lVar1 = 4;
    }
    *(int *)(*(long *)(&DAT_ffff000090832970 + (ulong)param_1 * 8) + lVar1) =
         (int)(1L << ((long)(char)(&DAT_ffff0000908331e8)[param_1] & 0x3fU));
    return 0;
  }
  return 0xffffffff;
}



// ffff0000907857c0  size=56  FUN_ffff0000907857c0

undefined8 FUN_ffff0000907857c0(long param_1,undefined8 param_2,int param_3)

{
  long lVar1;
  ulong in_x9;
  
  lVar1 = 8;
  if (param_3 != 0) {
    lVar1 = 4;
  }
  *(int *)(*(long *)(&DAT_ffff000090832990 + param_1 * 8) + lVar1) = (int)(1L << (in_x9 & 0x3f));
  return 0;
}



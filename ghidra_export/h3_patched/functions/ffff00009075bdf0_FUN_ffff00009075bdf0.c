// ffff00009075bdf0  size=48  FUN_ffff00009075bdf0

long FUN_ffff00009075bdf0(long *param_1)

{
  long lVar1;
  
  lVar1 = 0;
  if (param_1 != (long *)0x0) {
    if (*param_1 == 0) {
      return 0;
    }
    lVar1 = 0;
    if (param_1[1] != 0) {
      lVar1 = *param_1;
    }
  }
  return lVar1;
}



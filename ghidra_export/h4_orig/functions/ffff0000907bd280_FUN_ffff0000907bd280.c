// ffff0000907bd280  size=36  FUN_ffff0000907bd280

undefined8 FUN_ffff0000907bd280(undefined8 param_1,long *param_2)

{
  long lVar1;
  
  lVar1 = *param_2;
  *(undefined8 *)(param_2[1] + lVar1 * 8) = param_1;
  *param_2 = lVar1 + 1;
  return 1;
}



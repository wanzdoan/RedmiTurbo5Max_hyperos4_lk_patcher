
void FUN_ffff0000be0973e8(undefined8 *param_1,undefined8 param_2)

{
  long lVar1;
  long lVar2;
  
  if (param_1 == (undefined8 *)0x0) {
    return;
  }
  if (*(int *)*param_1 == 0x1000000) {
    lVar1 = param_1[2];
    while (lVar1 != 0) {
      lVar2 = *(long *)(lVar1 + 8);
      FUN_ffff0000be0973e8(lVar1,param_2);
      lVar1 = lVar2;
    }
  }
  FUN_ffff0000be097de0(param_2,param_1);
  return;
}



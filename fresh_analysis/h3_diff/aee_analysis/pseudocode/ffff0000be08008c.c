
void FUN_ffff0000be08008c(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000be01d35c();
  if (iVar1 == 2) {
    FUN_ffff0000be080d14(param_2,param_3);
    return;
  }
  if (iVar1 == 1) {
    FUN_ffff0000be080a70(param_1,param_2,param_3);
    return;
  }
  FUN_ffff0000be0802d8(param_1,param_2,param_3);
  return;
}



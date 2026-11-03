
void FUN_ffff0000be07f2d4(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000be01d35c();
  if (iVar1 == 2) {
    FUN_ffff0000be07fd8c(param_2,param_3);
    return;
  }
  if (iVar1 == 1) {
    FUN_ffff0000be07ff44(param_1,param_2,param_3);
    return;
  }
  FUN_ffff0000be07f4cc(param_1,param_2,param_3);
  return;
}



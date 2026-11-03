
void FUN_ffff0000be08011c(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000be01d35c();
  if (iVar1 == 2) {
    FUN_ffff0000be080d70(param_2);
    return;
  }
  if (iVar1 == 1) {
    FUN_ffff0000be080ae4(param_1,param_2);
    return;
  }
  FUN_ffff0000be0805f0(param_1,param_2);
  return;
}



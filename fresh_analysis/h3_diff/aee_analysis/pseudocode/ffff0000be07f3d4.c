
void FUN_ffff0000be07f3d4(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000be01d35c();
  if (iVar1 == 2) {
    FUN_ffff0000be07fe60(param_1,param_2,param_3);
  }
  else if (iVar1 == 1) {
    thunk_FUN_ffff0000be085bd4(param_1,param_2,param_3);
  }
  else {
    FUN_ffff0000be07f8cc(param_1,param_2,param_3);
  }
  return;
}



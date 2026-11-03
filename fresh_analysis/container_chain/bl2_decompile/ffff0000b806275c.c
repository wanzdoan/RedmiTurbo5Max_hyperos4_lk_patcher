
void FUN_ffff0000b806275c(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b800e378();
  if (iVar1 == 2) {
    FUN_ffff0000b80633b0(param_2);
    return;
  }
  if (iVar1 == 1) {
    FUN_ffff0000b8063124(param_1,param_2);
    return;
  }
  FUN_ffff0000b8062c30(param_1,param_2);
  return;
}



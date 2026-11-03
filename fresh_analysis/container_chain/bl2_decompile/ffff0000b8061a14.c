
void FUN_ffff0000b8061a14(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b800e378();
  if (iVar1 == 2) {
    FUN_ffff0000b80624a0(param_1,param_2,param_3);
  }
  else if (iVar1 == 1) {
    thunk_FUN_ffff0000b8068214(param_1,param_2,param_3);
  }
  else {
    FUN_ffff0000b8061f0c(param_1,param_2,param_3);
  }
  return;
}



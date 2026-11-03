
void FUN_ffff0000b8061914(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b800e378();
  if (iVar1 == 2) {
    FUN_ffff0000b80623cc(param_2,param_3);
    return;
  }
  if (iVar1 == 1) {
    FUN_ffff0000b8062584(param_1,param_2,param_3);
    return;
  }
  FUN_ffff0000b8061b0c(param_1,param_2,param_3);
  return;
}



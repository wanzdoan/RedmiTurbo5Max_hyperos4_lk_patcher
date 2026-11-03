
void FUN_ffff0000b80626cc(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b800e378();
  if (iVar1 == 2) {
    FUN_ffff0000b8063354(param_2,param_3);
    return;
  }
  if (iVar1 == 1) {
    FUN_ffff0000b80630b0(param_1,param_2,param_3);
    return;
  }
  FUN_ffff0000b8062918(param_1,param_2,param_3);
  return;
}



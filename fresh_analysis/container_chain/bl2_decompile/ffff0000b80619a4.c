
void FUN_ffff0000b80619a4(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b800e378();
  if (iVar1 == 2) {
    FUN_ffff0000b8062428(param_2);
    return;
  }
  if (iVar1 == 1) {
    FUN_ffff0000b8062600(param_1,param_2);
    return;
  }
  FUN_ffff0000b8061c18(param_1,param_2);
  return;
}



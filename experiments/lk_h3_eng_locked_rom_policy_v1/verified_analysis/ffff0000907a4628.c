
void FUN_ffff0000907a4628(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;

  iVar1 = FUN_ffff00009071c0e0();
  if (iVar1 == 2) {
    FUN_ffff0000907a517c(param_1,param_2,param_3);
  }
  else if (iVar1 == 1) {
    thunk_FUN_ffff0000907aaef0(param_1,param_2,param_3);
  }
  else {
    FUN_ffff0000907a4b20(param_1,param_2,param_3);
  }
  return;
}

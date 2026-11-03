
void FUN_ffff0000907a5198(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff00009071c6e8();
  if (iVar1 == 2) {
    iVar1 = FUN_ffff0000907a5cdc();
    if ((iVar1 == 0) && (iVar1 = FUN_ffff0000907a5d20(param_1,param_2), iVar1 == 0)) {
      FUN_ffff0000907a5d7c(param_3);
    }
  }
  else if (iVar1 == 1) {
    thunk_FUN_ffff0000907aadf0(param_1,param_2,param_3);
  }
  else {
    FUN_ffff0000907a5900(param_1,param_2,param_3);
  }
  return;
}



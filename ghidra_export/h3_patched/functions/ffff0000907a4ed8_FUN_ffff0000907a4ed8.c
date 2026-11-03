// ffff0000907a4ed8  size=144  FUN_ffff0000907a4ed8

void FUN_ffff0000907a4ed8(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff00009071c55c();
  if (iVar1 == 2) {
    iVar1 = FUN_ffff0000907a5a1c();
    if ((iVar1 == 0) && (iVar1 = FUN_ffff0000907a5a60(param_1,param_2), iVar1 == 0)) {
      FUN_ffff0000907a5abc(param_3);
    }
  }
  else if (iVar1 == 1) {
    thunk_FUN_ffff0000907aab30(param_1,param_2,param_3);
  }
  else {
    FUN_ffff0000907a5640(param_1,param_2,param_3);
  }
  return;
}



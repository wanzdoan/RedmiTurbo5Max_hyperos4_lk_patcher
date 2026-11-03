
void FUN_ffff0000b80627cc(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b800e378();
  if (iVar1 == 2) {
    iVar1 = FUN_ffff0000b8063310();
    if ((iVar1 == 0) && (iVar1 = FUN_ffff0000b8063354(param_1,param_2), iVar1 == 0)) {
      FUN_ffff0000b80633b0(param_3);
    }
  }
  else if (iVar1 == 1) {
    thunk_FUN_ffff0000b8068424(param_1,param_2,param_3);
  }
  else {
    FUN_ffff0000b8062f34(param_1,param_2,param_3);
  }
  return;
}



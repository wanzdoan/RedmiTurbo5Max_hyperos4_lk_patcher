
void FUN_ffff0000be08018c(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000be01d35c();
  if (iVar1 == 2) {
    iVar1 = FUN_ffff0000be080cd0();
    if ((iVar1 == 0) && (iVar1 = FUN_ffff0000be080d14(param_1,param_2), iVar1 == 0)) {
      FUN_ffff0000be080d70(param_3);
    }
  }
  else if (iVar1 == 1) {
    thunk_FUN_ffff0000be085de4(param_1,param_2,param_3);
  }
  else {
    FUN_ffff0000be0808f4(param_1,param_2,param_3);
  }
  return;
}



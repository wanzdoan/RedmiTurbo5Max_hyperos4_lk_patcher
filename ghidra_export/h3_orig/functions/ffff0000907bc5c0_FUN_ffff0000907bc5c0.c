// ffff0000907bc5c0  size=100  FUN_ffff0000907bc5c0

undefined4
FUN_ffff0000907bc5c0(undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4)

{
  undefined4 uVar1;
  long lVar2;
  
  lVar2 = FUN_ffff0000907c182c(param_3,param_4);
  uVar1 = 0;
  if (lVar2 != 0) {
    uVar1 = FUN_ffff0000907bc4c0(param_1,param_2,lVar2);
    FUN_ffff0000907c09a0(lVar2);
  }
  return uVar1;
}



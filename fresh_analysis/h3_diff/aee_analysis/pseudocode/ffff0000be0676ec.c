
undefined8 FUN_ffff0000be0676ec(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff0000be01d35c();
  if (iVar1 == 2) {
    uVar2 = 1;
    param_1 = 0;
  }
  else {
    if (iVar1 != 1) {
      return 0;
    }
    uVar2 = 0;
  }
  uVar2 = FUN_ffff0000be0871c0(uVar2,param_1);
  return uVar2;
}



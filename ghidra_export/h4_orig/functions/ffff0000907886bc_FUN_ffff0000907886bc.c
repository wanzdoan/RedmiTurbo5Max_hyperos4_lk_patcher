// ffff0000907886bc  size=96  FUN_ffff0000907886bc

undefined8 FUN_ffff0000907886bc(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff00009071c6e8();
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
  uVar2 = FUN_ffff0000907ac1cc(uVar2,param_1);
  return uVar2;
}



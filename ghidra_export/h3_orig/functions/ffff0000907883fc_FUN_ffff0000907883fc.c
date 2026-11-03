// ffff0000907883fc  size=96  FUN_ffff0000907883fc

undefined8 FUN_ffff0000907883fc(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff00009071c55c();
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
  uVar2 = FUN_ffff0000907abf0c(uVar2,param_1);
  return uVar2;
}



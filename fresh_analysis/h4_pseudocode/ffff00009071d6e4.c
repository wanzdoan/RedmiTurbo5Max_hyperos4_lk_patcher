
undefined8 FUN_ffff00009071d6e4(long param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  if (param_1 == 0) {
    uVar2 = 0xfffffff9;
  }
  else {
    iVar1 = FUN_ffff00009071d438();
    if (-1 < iVar1) {
      uVar2 = FUN_ffff00009071d4b4(param_1);
      return uVar2;
    }
    uVar2 = 0;
  }
  return uVar2;
}



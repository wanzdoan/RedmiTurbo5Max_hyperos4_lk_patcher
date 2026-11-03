
int FUN_ffff0000be07fd8c(long param_1,undefined8 param_2)

{
  int iVar1;
  undefined8 uVar2;
  
  if (param_1 == 0) {
    iVar1 = 0x100006;
  }
  else {
    FUN_ffff0000be007328();
    uVar2 = FUN_ffff0000be05d228(param_1);
    iVar1 = FUN_ffff0000be07fac4(uVar2,param_2);
    if (iVar1 == 0) {
      return 0;
    }
  }
  FUN_ffff0000be091d88();
  return iVar1;
}



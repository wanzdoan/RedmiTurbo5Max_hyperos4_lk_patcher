// ffff0000907a29f0  size=28  FUN_ffff0000907a29f0

undefined4 FUN_ffff0000907a29f0(undefined8 param_1,int param_2,undefined4 param_3)

{
  undefined4 uVar1;
  
  uVar1 = 0;
  if (param_2 != 0) {
    if (param_2 != 1) {
      return 0x101023;
    }
    uVar1 = FUN_ffff000090798d10(param_1,param_3);
  }
  return uVar1;
}



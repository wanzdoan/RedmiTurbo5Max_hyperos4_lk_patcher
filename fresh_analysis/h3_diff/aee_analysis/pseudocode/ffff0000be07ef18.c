
undefined8 FUN_ffff0000be07ef18(undefined8 param_1,int param_2)

{
  undefined8 uVar1;
  
  if (param_2 - 1U < 2) {
    uVar1 = FUN_ffff0000be08002c();
    return uVar1;
  }
  if (param_2 == 0) {
    uVar1 = FUN_ffff0000be07f274();
    return uVar1;
  }
  return 0x101023;
}



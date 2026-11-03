
undefined8 FUN_ffff0000be07f050(undefined8 param_1,undefined8 param_2,int param_3)

{
  undefined8 uVar1;
  
  if (param_3 - 1U < 2) {
    uVar1 = FUN_ffff0000be0805f0();
    return uVar1;
  }
  if (param_3 == 0) {
    uVar1 = FUN_ffff0000be07f5d8();
    return uVar1;
  }
  return 0x101023;
}



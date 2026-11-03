// ffff0000907a2920  size=52  FUN_ffff0000907a2920

undefined8 FUN_ffff0000907a2920(undefined8 param_1,int param_2)

{
  undefined8 uVar1;
  
  if (param_2 - 1U < 2) {
    uVar1 = FUN_ffff0000907a5228();
    return uVar1;
  }
  if (param_2 == 0) {
    uVar1 = FUN_ffff0000907a4398();
    return uVar1;
  }
  return 0x101023;
}



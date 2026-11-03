
undefined4 FUN_ffff0000be085bd4(long param_1,long param_2,undefined8 param_3)

{
  undefined4 uVar1;
  
  if (param_1 == 0) {
    uVar1 = 2;
  }
  else if (param_2 == 0) {
    uVar1 = 1;
  }
  else {
    FUN_ffff0000be093c50(0xffff000010000000,3);
    uVar1 = FUN_ffff0000be0939ac(0xffff000018005000,param_1,param_2,param_3);
    FUN_ffff0000be093c50(0xffff000010000000,0);
  }
  return uVar1;
}



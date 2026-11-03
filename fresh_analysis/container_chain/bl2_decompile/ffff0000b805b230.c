
undefined8 FUN_ffff0000b805b230(undefined8 param_1,undefined4 param_2)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff0000b800e378();
  if (iVar1 == 2) {
    uVar2 = FUN_ffff0000b805b370(param_1,param_2);
    return uVar2;
  }
  if (iVar1 == 1) {
    uVar2 = FUN_ffff0000b806342c(param_1,param_2);
    return uVar2;
  }
  return 0x10c000;
}



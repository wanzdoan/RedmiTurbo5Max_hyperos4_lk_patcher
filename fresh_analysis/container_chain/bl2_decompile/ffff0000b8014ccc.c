
undefined8 FUN_ffff0000b8014ccc(undefined1 *param_1,undefined4 param_2)

{
  undefined8 uVar1;
  
  uVar1 = FUN_ffff0000b8003b0c();
  uVar1 = FUN_ffff0000b80295f8(uVar1,param_2);
  if (-1 < (int)uVar1) {
    *param_1 = (char)uVar1;
    uVar1 = 0;
  }
  return uVar1;
}



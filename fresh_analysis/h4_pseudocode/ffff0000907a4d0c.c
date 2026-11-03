
undefined8 FUN_ffff0000907a4d0c(void)

{
  undefined8 uVar1;
  
  if (DAT_ffff00009090ae0c == 1) {
    uVar1 = 0;
  }
  else {
    uVar1 = FUN_ffff0000907a4988();
    if ((int)uVar1 == 0) {
      DAT_ffff00009090ae0c = 1;
    }
  }
  return uVar1;
}



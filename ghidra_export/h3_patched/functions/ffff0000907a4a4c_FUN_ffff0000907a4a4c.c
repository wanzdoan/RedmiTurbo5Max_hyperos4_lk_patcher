// ffff0000907a4a4c  size=72  FUN_ffff0000907a4a4c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907a4a4c(void)

{
  undefined8 uVar1;
  
  if (_DAT_ffff00009090ad4c == 1) {
    uVar1 = 0;
  }
  else {
    uVar1 = FUN_ffff0000907a46c8();
    if ((int)uVar1 == 0) {
      _DAT_ffff00009090ad4c = 1;
    }
  }
  return uVar1;
}



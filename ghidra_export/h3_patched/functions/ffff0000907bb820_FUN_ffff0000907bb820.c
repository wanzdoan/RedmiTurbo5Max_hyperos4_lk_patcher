// ffff0000907bb820  size=40  FUN_ffff0000907bb820

undefined4 FUN_ffff0000907bb820(uint param_1)

{
  undefined4 uVar1;
  
  uVar1 = 0x50;
  if ((param_1 & 0x20010a) != 0x10a) {
    uVar1 = 0x44;
  }
  return uVar1;
}



// ffff00009072b74c  size=144  FUN_ffff00009072b74c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009072b74c(void)

{
  undefined8 uVar1;
  uint uVar2;
  
  uVar2 = _DAT_ffff00000c000b48;
  if (_DAT_ffff00000c000b48 == 0xdeadbeef) {
    uVar2 = _DAT_ffff00000c1b0b48;
  }
  uVar1 = 0xfffffffe;
  switch(uVar2 >> 3 & 0x3f) {
  case 0:
  case 0x13:
  case 0x1d:
    uVar1 = FUN_ffff00009072b7dc();
    return uVar1;
  case 0xf:
    FUN_ffff00009072b620(0xfffffffe);
    uVar1 = 0;
  }
  return uVar1;
}



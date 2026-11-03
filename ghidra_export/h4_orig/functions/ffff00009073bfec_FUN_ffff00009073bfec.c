// ffff00009073bfec  size=76  FUN_ffff00009073bfec

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

byte FUN_ffff00009073bfec(void)

{
  byte bVar1;
  
  if (_DAT_ffff00009086e080 < 0x1e) {
    return 0;
  }
  bVar1 = DAT_ffff00009086e140;
  if (_DAT_ffff00009086e090 != 1) {
    bVar1 = DAT_ffff00009086e140 & DAT_ffff00009086e141;
  }
  return bVar1;
}



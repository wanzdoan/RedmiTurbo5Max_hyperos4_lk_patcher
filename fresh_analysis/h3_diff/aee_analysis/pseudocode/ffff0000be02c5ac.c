
byte FUN_ffff0000be02c5ac(void)

{
  byte bVar1;
  
  if (DAT_ffff0000be118348 < 0x1e) {
    return 0;
  }
  bVar1 = DAT_ffff0000be118408;
  if (DAT_ffff0000be118358 != 1) {
    bVar1 = DAT_ffff0000be118408 & DAT_ffff0000be118409;
  }
  return bVar1;
}



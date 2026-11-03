
byte FUN_ffff0000b8022a1c(void)

{
  byte bVar1;
  
  if (DAT_ffff0000b811bb10 < 0x1e) {
    return 0;
  }
  bVar1 = DAT_ffff0000b811bbd0;
  if (DAT_ffff0000b811bb20 != 1) {
    bVar1 = DAT_ffff0000b811bbd0 & DAT_ffff0000b811bbd1;
  }
  return bVar1;
}



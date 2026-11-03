
undefined8 FUN_ffff0000b8016ce0(void)

{
  ulong uVar1;
  byte bVar2;
  
  uVar1 = FUN_ffff0000b8017ddc();
  bVar2 = 2;
  if ((uVar1 & 1) == 0) {
    bVar2 = 0;
  }
  DAT_ffff0000b80e8a10 = DAT_ffff0000b80e8a10 & 0xfd | bVar2;
  return 0;
}



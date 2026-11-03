// File offset entry: 0xac23c

undefined8 FUN_020ac13c(void)

{
  ulong uVar1;
  
  uVar1 = 0x100001;
  do {
    if ((uRam000000001326f000 & 1) != 0) {
      uRam000000001326f000 = uRam000000001326f000 | 4;
      return 0;
    }
    uVar1 = uVar1 - 1;
  } while (1 < uVar1);
  uRam000000001326f000 = uRam000000001326f000 | 4;
  FUN_0208ada0(0x20ba4bf,1,1);
  FUN_0208ada0(0x20b934b);
  return 0x2000000;
}



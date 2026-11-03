// File offset entry: 0x99d6c

void FUN_02099c6c(void)

{
  uRam000000001c010000 = 0x230001ff;
  thunk_FUN_02091c94(0x28);
  if ((uRam000000001c010000 >> 5 & 1) == 0) {
    uRam000000001c010000 = uRam000000001c010000 | 0x22000020;
    thunk_FUN_02091c94(0x46);
    uRam000000001c010000 = uRam000000001c010000 & 0xddffffdf | 0x22000000;
  }
  else {
    uRam000000001c010000 = uRam000000001c010000 & 0xddffffdf | 0x22000000;
    thunk_FUN_02091c94(0x46);
    uRam000000001c010000 = uRam000000001c010000 | 0x22000020;
  }
  return;
}



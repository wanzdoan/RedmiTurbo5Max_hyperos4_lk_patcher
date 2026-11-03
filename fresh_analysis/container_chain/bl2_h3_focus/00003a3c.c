// File offset entry: 0x3a3c

void FUN_ffff0000b8003a3c(void)

{
  int iVar1;
  ulong uVar2;
  
  FUN_ffff0000b802ce04();
  FUN_ffff0000b800f94c();
  uVar2 = FUN_ffff0000b8043260();
  if ((uVar2 & 1) != 0) {
    FUN_ffff0000b8058370();
  }
  FUN_ffff0000b800d264(0xb8000000);
  iVar1 = FUN_ffff0000b800a01c();
  DAT_ffff0000b810b018._4_4_ = iVar1 - DAT_ffff0000b810b030;
  FUN_ffff0000b8009f9c();
  FUN_ffff0000b802bd9c(0x1e);
  FUN_ffff0000b802bd7c();
  FUN_ffff0000b800e0bc(&DAT_ffff000088600000);
  return;
}



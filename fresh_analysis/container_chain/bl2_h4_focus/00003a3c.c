// File offset entry: 0x3a3c

void FUN_ffff0000b8003a3c(void)

{
  int iVar1;
  ulong uVar2;
  
  FUN_ffff0000b802d290();
  FUN_ffff0000b800f96c();
  uVar2 = FUN_ffff0000b80436ec();
  if ((uVar2 & 1) != 0) {
    FUN_ffff0000b80587fc();
  }
  FUN_ffff0000b800d264(0xb8000000);
  iVar1 = FUN_ffff0000b800a01c();
  DAT_ffff0000b810c018._4_4_ = iVar1 - DAT_ffff0000b810c030;
  FUN_ffff0000b8009f9c();
  FUN_ffff0000b802c228(0x1e);
  FUN_ffff0000b802c208();
  FUN_ffff0000b800e0bc(&DAT_ffff000088600000);
  return;
}



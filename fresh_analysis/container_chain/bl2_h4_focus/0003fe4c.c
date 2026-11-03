// File offset entry: 0x3fe4c

void FUN_ffff0000b803fe4c(void)

{
  int iVar1;
  
  if (DAT_ffff0000b8129898 == 0) {
    iVar1 = FUN_ffff0000b803fd20();
    DAT_ffff0000b8129898 = iVar1 + 0xffffU & 0xffff0000;
  }
  return;
}



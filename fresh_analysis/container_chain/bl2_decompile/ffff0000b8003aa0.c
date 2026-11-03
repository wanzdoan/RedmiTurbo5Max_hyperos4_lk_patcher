
void FUN_ffff0000b8003aa0(void)

{
  ulong uVar1;
  
  uVar1 = currentel;
  if ((uVar1 & 0x3fffffffc) == 0xc) {
    FUN_ffff0000b8009970();
    return;
  }
  return;
}



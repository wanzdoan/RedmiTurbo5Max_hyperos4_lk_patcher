// File offset entry: 0x3f9c0

void FUN_ffff0000b803f9c0(void)

{
  int iVar1;
  
  if (DAT_ffff0000b8128838 == 0) {
    iVar1 = FUN_ffff0000b803f894();
    DAT_ffff0000b8128838 = iVar1 + 0xffffU & 0xffff0000;
  }
  return;
}



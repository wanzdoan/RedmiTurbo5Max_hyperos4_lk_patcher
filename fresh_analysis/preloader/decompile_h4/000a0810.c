// File offset entry: 0xa0810

undefined4 FUN_020a0710(void)

{
  int iVar1;
  
  iVar1 = func_0x020a0788();
  if (iVar1 == 0) {
    if ((uRam000000001800d688 >> 3 & 1) == 0) {
      return 4;
    }
  }
  else if (iVar1 != 1) {
    return 4;
  }
  if (((uRam0000000018010000 >> 0x11 & 1) != 0) && ((uRam00000000180100a0 & 1) != 0)) {
    if ((uRam00000000180100a0 & 4) == 0) {
      return 5;
    }
    return 4;
  }
  return 4;
}



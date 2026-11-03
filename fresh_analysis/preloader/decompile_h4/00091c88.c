// File offset entry: 0x91c88

int FUN_02091b88(void)

{
  uint uVar1;
  
  uVar1 = (iRam000000001c400008 + 12999U) / 13000;
  if (uVar1 < uRam0000000002120b48) {
    iRam0000000002120b4c = (iRam0000000002120b4c - uRam0000000002120b48) + uVar1 + 0x50a8e;
  }
  else {
    iRam0000000002120b4c = (uVar1 + iRam0000000002120b4c) - uRam0000000002120b48;
  }
  uRam0000000002120b48 = uVar1;
  return iRam0000000002120b4c;
}



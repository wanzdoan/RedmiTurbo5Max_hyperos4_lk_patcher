// File offset entry: 0xa5c68

undefined4 FUN_020a5b68(void)

{
  int iVar1;
  undefined4 uVar2;
  
  uRam0000000002123ae0 = uRam0000000002123840;
  iRam0000000002123ae8 = iRam0000000002123850;
  uVar2 = 0x101012;
  if (iRam0000000002123850 != 0) {
    do {
      iVar1 = func_0x020a6fa8(0x21240ec);
      if (iVar1 == 0) break;
    } while (iRam0000000002123ae8 != 0);
    uVar2 = 0;
    if (iVar1 != 0) {
      uVar2 = 0x101012;
    }
  }
  uRam0000000002123ae0 = 0;
  iRam0000000002123ae8 = 0;
  return uVar2;
}



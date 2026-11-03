// File offset entry: 0xa6468

undefined8 FUN_020a6368(undefined8 param_1)

{
  bool bVar1;
  undefined8 uVar2;
  
  uRam0000000002123ae0 = uRam0000000002123840;
  iRam0000000002123ae8 = iRam0000000002123850;
  if (iRam0000000002123850 != 0) {
    uVar2 = func_0x020a6a40(3);
    if ((int)uVar2 == 0) {
      do {
        uVar2 = func_0x020a6fa8(param_1);
        bVar1 = (int)uVar2 == 0;
        if (((int)uVar2 == 0) || (iRam0000000002123ae8 == 0)) break;
        uVar2 = func_0x020a6a40(3);
      } while ((int)uVar2 == 0);
    }
    else {
      bVar1 = false;
    }
    if (bVar1) {
      uRam0000000002123ae0 = 0;
      iRam0000000002123ae8 = 0;
      return uVar2;
    }
  }
  uRam0000000002123ae0 = 0;
  iRam0000000002123ae8 = 0;
  return 0x101012;
}



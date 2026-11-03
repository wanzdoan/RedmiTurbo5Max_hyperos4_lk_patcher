// File offset entry: 0xa4b40

undefined8 FUN_020a4a40(long param_1,int param_2)

{
  undefined8 uVar1;
  
  uVar1 = 0x101023;
  if ((param_1 != 0) && (cRam000000000212376c != '\0')) {
    if (param_2 == 0x20) {
      uVar1 = 0x101022;
      if (iRam00000000021237ac == 0x40) {
        func_0x020915ac(param_1,0x212376c,0x20);
        uVar1 = 0;
      }
    }
    else {
      uVar1 = 0x101022;
    }
  }
  return uVar1;
}



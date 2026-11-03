// File offset entry: 0xa4a5c

undefined8 FUN_020a495c(ulong param_1)

{
  int iVar1;
  
  iVar1 = *(int *)(lRam0000000002122010 + 4);
  if (iVar1 == 0x22) {
    iVar1 = func_0x020a7cec();
    if (iVar1 == 1) {
      if ((param_1 & 1) == 0) {
        return 1;
      }
      func_0x0208ada0(0x20b7ff1,0x20c9773);
      return 1;
    }
    if ((param_1 & 1) != 0) {
      func_0x0208ada0(0x20bf25b,0x20c9773);
    }
  }
  else {
    if (iVar1 == 0x11) {
      return 1;
    }
    if (iVar1 != 0) {
      func_0x0208ada0(0x20b3079,0x20c9773);
    }
  }
  return 0;
}



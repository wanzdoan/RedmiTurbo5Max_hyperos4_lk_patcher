// ffff00009078b30c  size=128  FUN_ffff00009078b30c

undefined1 * FUN_ffff00009078b30c(uint param_1,char param_2)

{
  long lVar1;
  
  if ((DAT_ffff00009088a168 == param_1) && ((bool)DAT_ffff00009088a169 == (param_2 == -0x80))) {
    lVar1 = 1;
  }
  else {
    if (DAT_ffff00009088a1b0 != param_1) {
      return (undefined1 *)0x0;
    }
    if ((bool)DAT_ffff00009088a1b1 != (param_2 == -0x80)) {
      return (undefined1 *)0x0;
    }
    lVar1 = 2;
  }
  return &DAT_ffff00009088a0e0 + lVar1 * 0x48;
}



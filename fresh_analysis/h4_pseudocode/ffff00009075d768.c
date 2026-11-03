
undefined4 FUN_ffff00009075d768(void)

{
  long *plVar1;
  
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  if (DAT_ffff00009087bf70 == (long *)0x0) {
    plVar1 = (long *)FUN_ffff00009075bc40(0,0);
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    DAT_ffff00009087bf70 = plVar1;
    if (plVar1 == (long *)0x0) {
      return 0;
    }
  }
  if (*DAT_ffff00009087bf70 == 0) {
    return 0;
  }
  return *(undefined4 *)(*DAT_ffff00009087bf70 + 0x18);
}



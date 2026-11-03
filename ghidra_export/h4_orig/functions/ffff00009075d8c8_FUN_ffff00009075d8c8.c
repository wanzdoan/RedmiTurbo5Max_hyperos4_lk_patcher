// ffff00009075d8c8  size=172  FUN_ffff00009075d8c8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009075d8c8(void)

{
  long *plVar1;
  
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  if (_DAT_ffff00009087bf70 == (long *)0x0) {
    plVar1 = (long *)FUN_ffff00009075bc40(0,0);
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    _DAT_ffff00009087bf70 = plVar1;
    if (plVar1 == (long *)0x0) {
      return 0;
    }
  }
  if (*_DAT_ffff00009087bf70 == 0) {
    return 0;
  }
  return *(undefined4 *)(*_DAT_ffff00009087bf70 + 0x1c);
}



// ffff00009075d6dc  size=180  FUN_ffff00009075d6dc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff00009075d6dc(void)

{
  long *plVar1;
  
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  if (_DAT_ffff00009087bed8 == (long *)0x0) {
    plVar1 = (long *)FUN_ffff00009075b9a8(0,0);
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    _DAT_ffff00009087bed8 = plVar1;
    if (plVar1 == (long *)0x0) {
      return 0;
    }
  }
  if (*_DAT_ffff00009087bed8 == 0) {
    return 0;
  }
  return *(int *)(*_DAT_ffff00009087bed8 + 0x1c) + 0x1fU & 0xffffffe0;
}



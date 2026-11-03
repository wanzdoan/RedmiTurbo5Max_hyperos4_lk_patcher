// ffff00009075dfcc  size=300  FUN_ffff00009075dfcc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075dfcc(void)

{
  long *plVar1;
  uint uVar2;
  uint uVar3;
  
  if (_DAT_ffff00009087bebc != 0) {
    return;
  }
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
    if (plVar1 != (long *)0x0) goto LAB_ffff00009075e05c;
    plVar1 = (long *)FUN_ffff00009075b9a8(0,0);
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    uVar2 = 0;
    uVar3 = 0;
    _DAT_ffff00009087bed8 = plVar1;
    if (plVar1 == (long *)0x0) goto LAB_ffff00009075e090;
  }
  else {
LAB_ffff00009075e05c:
    if (*_DAT_ffff00009087bed8 == 0) {
      uVar3 = 0;
      uVar2 = 0;
      goto LAB_ffff00009075e090;
    }
    uVar2 = *(int *)(*_DAT_ffff00009087bed8 + 0x18) * 0x10 + 0x1f0U & 0xfffffe00;
  }
  uVar3 = 0;
  if (*_DAT_ffff00009087bed8 != 0) {
    uVar3 = *(int *)(*_DAT_ffff00009087bed8 + 0x1c) + 0x1fU & 0xffffffe0;
  }
LAB_ffff00009075e090:
  _DAT_ffff00009087bebc = uVar2 * uVar3 & 0x7fffc000;
  return;
}



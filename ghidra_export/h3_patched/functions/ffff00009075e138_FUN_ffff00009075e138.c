// ffff00009075e138  size=300  FUN_ffff00009075e138

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075e138(void)

{
  long *plVar1;
  uint uVar2;
  uint uVar3;
  
  if (_DAT_ffff00009087bec4 != 0) {
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
    if (plVar1 != (long *)0x0) goto LAB_ffff00009075e1c8;
    plVar1 = (long *)FUN_ffff00009075b9a8(0,0);
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    uVar2 = 0;
    uVar3 = 0;
    _DAT_ffff00009087bed8 = plVar1;
    if (plVar1 == (long *)0x0) goto LAB_ffff00009075e200;
  }
  else {
LAB_ffff00009075e1c8:
    if (*_DAT_ffff00009087bed8 == 0) {
      uVar3 = 0;
      uVar2 = 0;
      goto LAB_ffff00009075e200;
    }
    uVar2 = *(int *)(*_DAT_ffff00009087bed8 + 0x18) * 4 + 0x7cU & 0xffffff80;
  }
  uVar3 = 0;
  if (*_DAT_ffff00009087bed8 != 0) {
    uVar3 = *(int *)(*_DAT_ffff00009087bed8 + 0x1c) + 0x1fU >> 3 & 0x1ffffffc;
  }
LAB_ffff00009075e200:
  _DAT_ffff00009087bec4 = uVar2 * uVar3;
  return;
}



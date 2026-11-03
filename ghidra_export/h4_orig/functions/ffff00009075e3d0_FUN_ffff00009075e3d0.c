// ffff00009075e3d0  size=300  FUN_ffff00009075e3d0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075e3d0(void)

{
  long *plVar1;
  uint uVar2;
  uint uVar3;
  
  if (_DAT_ffff00009087bf5c != 0) {
    return;
  }
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
    if (plVar1 != (long *)0x0) goto LAB_ffff00009075e460;
    plVar1 = (long *)FUN_ffff00009075bc40(0,0);
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    uVar2 = 0;
    uVar3 = 0;
    _DAT_ffff00009087bf70 = plVar1;
    if (plVar1 == (long *)0x0) goto LAB_ffff00009075e498;
  }
  else {
LAB_ffff00009075e460:
    if (*_DAT_ffff00009087bf70 == 0) {
      uVar3 = 0;
      uVar2 = 0;
      goto LAB_ffff00009075e498;
    }
    uVar2 = *(int *)(*_DAT_ffff00009087bf70 + 0x18) * 4 + 0x7cU & 0xffffff80;
  }
  uVar3 = 0;
  if (*_DAT_ffff00009087bf70 != 0) {
    uVar3 = *(int *)(*_DAT_ffff00009087bf70 + 0x1c) + 0x1fU >> 3 & 0x1ffffffc;
  }
LAB_ffff00009075e498:
  _DAT_ffff00009087bf5c = uVar2 * uVar3;
  return;
}



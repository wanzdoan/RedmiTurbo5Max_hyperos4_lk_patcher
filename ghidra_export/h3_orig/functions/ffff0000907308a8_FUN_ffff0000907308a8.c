// ffff0000907308a8  size=144  FUN_ffff0000907308a8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907308a8(void)

{
  uint uVar1;
  int iVar2;
  
  if ((((_DAT_ffff00009086d998 != 0) && (0 < _DAT_ffff00009086d9b4)) &&
      (-1 < (int)_DAT_ffff00009086d9b0)) && ((int)_DAT_ffff00009086d9b0 < _DAT_ffff00009086d9b4)) {
    iVar2 = 1;
    uVar1 = _DAT_ffff00009086d9b0;
    while( true ) {
      FUN_ffff0000907310f4(_DAT_ffff00009086d998 + (ulong)uVar1 * 0x20);
      if (_DAT_ffff00009086d9b4 <= iVar2) break;
      uVar1 = *(uint *)(_DAT_ffff00009086d998 + (ulong)uVar1 * 0x20 + 0x18);
      if ((int)uVar1 < 0) {
        return;
      }
      iVar2 = iVar2 + 1;
      if (_DAT_ffff00009086d9b4 <= (int)uVar1) {
        return;
      }
    }
  }
  return;
}



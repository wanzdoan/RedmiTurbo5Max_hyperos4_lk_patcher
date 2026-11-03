// ffff000090730b40  size=144  FUN_ffff000090730b40

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090730b40(void)

{
  uint uVar1;
  int iVar2;
  
  if ((((_DAT_ffff00009086da30 != 0) && (0 < _DAT_ffff00009086da4c)) &&
      (-1 < (int)_DAT_ffff00009086da48)) && ((int)_DAT_ffff00009086da48 < _DAT_ffff00009086da4c)) {
    iVar2 = 1;
    uVar1 = _DAT_ffff00009086da48;
    while( true ) {
      FUN_ffff00009073138c(_DAT_ffff00009086da30 + (ulong)uVar1 * 0x20);
      if (_DAT_ffff00009086da4c <= iVar2) break;
      uVar1 = *(uint *)(_DAT_ffff00009086da30 + (ulong)uVar1 * 0x20 + 0x18);
      if ((int)uVar1 < 0) {
        return;
      }
      iVar2 = iVar2 + 1;
      if (_DAT_ffff00009086da4c <= (int)uVar1) {
        return;
      }
    }
  }
  return;
}



// ffff00009075e0f8  size=64  FUN_ffff00009075e0f8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075e0f8(void)

{
  int iVar1;
  
  if (_DAT_ffff00009087bec0 == 0) {
    iVar1 = FUN_ffff00009075dfcc();
    _DAT_ffff00009087bec0 = iVar1 + 0xffffU & 0xffff0000;
  }
  return;
}



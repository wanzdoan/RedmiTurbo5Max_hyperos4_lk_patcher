// ffff00009075e390  size=64  FUN_ffff00009075e390

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075e390(void)

{
  int iVar1;
  
  if (_DAT_ffff00009087bf58 == 0) {
    iVar1 = FUN_ffff00009075e264();
    _DAT_ffff00009087bf58 = iVar1 + 0xffffU & 0xffff0000;
  }
  return;
}



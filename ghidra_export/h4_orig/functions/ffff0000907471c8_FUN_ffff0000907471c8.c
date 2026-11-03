// ffff0000907471c8  size=44  FUN_ffff0000907471c8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff0000907471c8(uint param_1)

{
  long lVar1;
  
  lVar1 = _DAT_ffff0000908776d0 + (ulong)param_1 * 0x18;
  if (_DAT_ffff0000908776cc <= param_1) {
    lVar1 = 0;
  }
  return lVar1;
}



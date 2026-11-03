// ffff000090746f30  size=44  FUN_ffff000090746f30

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff000090746f30(uint param_1)

{
  long lVar1;
  
  lVar1 = _DAT_ffff000090877638 + (ulong)param_1 * 0x18;
  if (_DAT_ffff000090877634 <= param_1) {
    lVar1 = 0;
  }
  return lVar1;
}



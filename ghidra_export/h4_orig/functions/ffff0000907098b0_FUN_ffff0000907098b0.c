// ffff0000907098b0  size=64  FUN_ffff0000907098b0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907098b0(void)

{
  while (_DAT_ffff00009083d100 != (long *)0x0) {
    _DAT_ffff00009083d100 = (long *)*_DAT_ffff00009083d100;
    thunk_FUN_ffff0000907c314c();
  }
  return;
}



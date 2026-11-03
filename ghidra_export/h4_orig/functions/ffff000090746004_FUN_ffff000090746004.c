// ffff000090746004  size=20  FUN_ffff000090746004

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

byte FUN_ffff000090746004(void)

{
  if (DAT_ffff00009081a6bc == 0xf) {
    DAT_ffff00009081a6bc = (byte)_DAT_ffff00001c010024 & 0xf;
    _DAT_ffff00001c010024 = _DAT_ffff00001c010024 & 0xfffffff0;
  }
  return DAT_ffff00009081a6bc;
}



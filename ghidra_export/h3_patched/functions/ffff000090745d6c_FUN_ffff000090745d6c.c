// ffff000090745d6c  size=20  FUN_ffff000090745d6c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

byte FUN_ffff000090745d6c(void)

{
  if (DAT_ffff00009081a69c == 0xf) {
    DAT_ffff00009081a69c = (byte)_DAT_ffff00001c010024 & 0xf;
    _DAT_ffff00001c010024 = _DAT_ffff00001c010024 & 0xfffffff0;
  }
  return DAT_ffff00009081a69c;
}



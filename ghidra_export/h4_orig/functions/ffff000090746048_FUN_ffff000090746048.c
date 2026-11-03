// ffff000090746048  size=44  FUN_ffff000090746048

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090746048(uint param_1)

{
  _DAT_ffff00001c010024 = _DAT_ffff00001c010024 & 0xfffff80f | (param_1 & 0xff) << 4;
  return;
}



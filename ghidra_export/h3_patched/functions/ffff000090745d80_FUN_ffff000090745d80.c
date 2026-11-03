// ffff000090745d80  size=48  FUN_ffff000090745d80

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined1 FUN_ffff000090745d80(long param_1)

{
  *(byte *)(param_1 + 0x69c) = (byte)_DAT_ffff00001c010024 & 0xf;
  _DAT_ffff00001c010024 = _DAT_ffff00001c010024 & 0xfffffff0;
  return *(undefined1 *)(param_1 + 0x69c);
}



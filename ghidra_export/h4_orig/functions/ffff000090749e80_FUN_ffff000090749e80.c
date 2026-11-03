// ffff000090749e80  size=80  FUN_ffff000090749e80

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749e80(int param_1)

{
  if (param_1 == 0) {
    _DAT_ffff00000c1b0288 = _DAT_ffff00000c1b0288 & 0xeeeeeeee;
    _DAT_ffff00000c1b028c = _DAT_ffff00000c1b028c & 0xffffeeee;
  }
  else {
    _DAT_ffff00000c1b0288 = _DAT_ffff00000c1b0288 | 0x11111111;
    _DAT_ffff00000c1b028c = _DAT_ffff00000c1b028c | 0x1111;
  }
  return;
}



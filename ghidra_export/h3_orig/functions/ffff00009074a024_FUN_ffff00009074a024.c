// ffff00009074a024  size=136  FUN_ffff00009074a024

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009074a024(int param_1)

{
  if (param_1 == 0) {
    _DAT_ffff000016610020 = _DAT_ffff000016610020 & 0xfffeb6df;
    _DAT_ffff000016610024 = _DAT_ffff000016610024 & 0xffffdfff;
    _DAT_ffff000016610028 = _DAT_ffff000016610028 & 0xffffdfff;
    _DAT_ffff00001661002c = _DAT_ffff00001661002c & 0xffffdfff;
  }
  else {
    _DAT_ffff000016610020 = _DAT_ffff000016610020 | 0x14920;
    _DAT_ffff000016610024 = _DAT_ffff000016610024 | 0x2000;
    _DAT_ffff000016610028 = _DAT_ffff000016610028 | 0x2000;
    _DAT_ffff00001661002c = _DAT_ffff00001661002c | 0x2000;
  }
  return;
}



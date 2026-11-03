// ffff000090749f9c  size=136  FUN_ffff000090749f9c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749f9c(int param_1)

{
  if (param_1 == 0) {
    _DAT_ffff00001661002c = _DAT_ffff00001661002c & 0xfffeb6df;
    _DAT_ffff000016610030 = _DAT_ffff000016610030 & 0xffffdfff;
    _DAT_ffff000016610034 = _DAT_ffff000016610034 & 0xffffdfff;
    _DAT_ffff000016610038 = _DAT_ffff000016610038 & 0xffffdfff;
  }
  else {
    _DAT_ffff00001661002c = _DAT_ffff00001661002c | 0x14920;
    _DAT_ffff000016610030 = _DAT_ffff000016610030 | 0x2000;
    _DAT_ffff000016610034 = _DAT_ffff000016610034 | 0x2000;
    _DAT_ffff000016610038 = _DAT_ffff000016610038 | 0x2000;
  }
  return;
}



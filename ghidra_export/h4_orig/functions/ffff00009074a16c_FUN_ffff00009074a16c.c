// ffff00009074a16c  size=200  FUN_ffff00009074a16c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009074a16c(int param_1)

{
  if (param_1 == 0) {
    _DAT_ffff00001412400c = _DAT_ffff00001412400c & 0xffffffe3;
    _DAT_ffff000014124014 = _DAT_ffff000014124014 & 0xffffff6f;
    _DAT_ffff000014124018 = _DAT_ffff000014124018 & 0xffdfffff;
    _DAT_ffff00001412401c = _DAT_ffff00001412401c & 0xffbfffff;
    _DAT_ffff000014124020 = _DAT_ffff000014124020 & 0xffefffff;
    _DAT_ffff000014124024 = _DAT_ffff000014124024 & 0xfeffffff;
    _DAT_ffff000014124028 = _DAT_ffff000014124028 & 0xff7fffff;
  }
  else {
    _DAT_ffff00001412400c = _DAT_ffff00001412400c | 0x1c;
    _DAT_ffff000014124014 = _DAT_ffff000014124014 | 0x90;
    _DAT_ffff000014124018 = _DAT_ffff000014124018 | 0x200000;
    _DAT_ffff00001412401c = _DAT_ffff00001412401c | 0x400000;
    _DAT_ffff000014124020 = _DAT_ffff000014124020 | 0x100000;
    _DAT_ffff000014124024 = _DAT_ffff000014124024 | 0x1000000;
    _DAT_ffff000014124028 = _DAT_ffff000014124028 | 0x800000;
  }
  return;
}



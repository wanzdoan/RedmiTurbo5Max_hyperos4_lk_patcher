// ffff00009075eddc  size=240  FUN_ffff00009075eddc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075eddc(void)

{
  _DAT_ffff00009087bf00 = 0;
  DAT_ffff00009087bf04 = 0;
  _DAT_ffff00009087bf08 = 0;
  _DAT_ffff00001002c004 = 0x20000000;
  while ((_DAT_ffff00001002c00c >> 0x1d & 1) == 0) {
    FUN_ffff00009075ebd0();
  }
  _DAT_ffff00009087bf08 = _DAT_ffff00009087bf08 + 1;
  _DAT_ffff00009087bf0c = 0;
  _DAT_ffff00001002c024 = 1;
  while ((_DAT_ffff00001002c02c & 1) == 0) {
    FUN_ffff00009075ebd0();
  }
  _DAT_ffff00009087bf0c = 0;
  _DAT_ffff00009087bf08 = _DAT_ffff00009087bf08 + 1;
  _DAT_ffff00001c004e00 = _DAT_ffff00001c004e00 & 0xfffffffb;
  while ((_DAT_ffff00001c004f14 & 1) != 0) {
    FUN_ffff00009075ebd0();
  }
  _DAT_ffff00009087bf0c = 0;
  _DAT_ffff00009087bf08 = _DAT_ffff00009087bf08 + 1;
  _DAT_ffff00001c004efc = _DAT_ffff00001c004efc | 3;
  return;
}



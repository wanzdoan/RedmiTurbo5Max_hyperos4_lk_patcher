
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b803c814(uint param_1)

{
  uint *puVar1;
  uint uVar2;
  
  if ((int)param_1 < 1) {
    uVar2 = 0;
  }
  else if (param_1 < 0xff) {
    uVar2 = param_1 << 2 | 2;
  }
  else {
    uVar2 = 0x3ff;
  }
  if ((_DAT_ffff0000b8129034 & 1) == 0) {
    FUN_ffff0000b80367dc(0x2e);
    puVar1 = (uint *)FUN_ffff0000b801c1cc(s_lcd_backlight_ffff0000b80909f0);
    if (puVar1 == (uint *)0x0) {
      _DAT_ffff0000160b0018 = 0;
    }
    else {
      if (*puVar1 < 5) {
        _DAT_ffff0000100000b8 = 7;
        _DAT_ffff0000100000b4 = *puVar1 & 7;
      }
      _DAT_ffff000010000008 = _DAT_ffff000010000008 | 0x200;
      _DAT_ffff0000160b0018 = 0;
      if (puVar1[1] != 0) {
        _DAT_ffff0000160b0018 = (puVar1[1] & 0x3ff) << 0x10;
      }
    }
    _DAT_ffff0000b8129034 = _DAT_ffff0000b8129034 | 1;
  }
  _DAT_ffff0000160b0080 = 3;
  _DAT_ffff0000160b001c = uVar2 << 0x10 | 0x3ff;
  if (uVar2 == 0) {
    _DAT_ffff0000160b0000 = 0;
  }
  else if ((_DAT_ffff0000160b0000 & 1) == 0) {
    _DAT_ffff0000160b0000 = 1;
  }
  _DAT_ffff0000160b000c = 0;
  return 0;
}



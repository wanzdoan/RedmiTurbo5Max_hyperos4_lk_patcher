// ffff000090749ca8  size=216  FUN_ffff000090749ca8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090749ca8(int param_1)

{
  if (param_1 == 0) {
    _DAT_ffff00000c1b02a0 = _DAT_ffff00000c1b02a0 | 1;
    _DAT_ffff00000c1b02a4 = _DAT_ffff00000c1b02a4 | 3;
    _DAT_ffff00000c1b02a8 = _DAT_ffff00000c1b02a8 | 1;
    _DAT_ffff00000c1b02ac = _DAT_ffff00000c1b02ac | 1;
    _DAT_ffff00000c1b02b0 = _DAT_ffff00000c1b02b0 | 1;
    _DAT_ffff00000c1b02b4 = _DAT_ffff00000c1b02b4 & 0xfffffff8;
    _DAT_ffff00000c1b02bc = _DAT_ffff00000c1b02bc | 1;
    _DAT_ffff00000c1b02c4 = _DAT_ffff00000c1b02c4 | 1;
  }
  else {
    _DAT_ffff00000c1b02a0 = _DAT_ffff00000c1b02a0 & 0xfffffffe;
    _DAT_ffff00000c1b02a4 = _DAT_ffff00000c1b02a4 & 0xfffffffc;
    _DAT_ffff00000c1b02a8 = _DAT_ffff00000c1b02a8 & 0xfffffffe;
    _DAT_ffff00000c1b02ac = _DAT_ffff00000c1b02ac & 0xfffffffe;
    _DAT_ffff00000c1b02b0 = _DAT_ffff00000c1b02b0 & 0xfffffffe;
    _DAT_ffff00000c1b02b4 = _DAT_ffff00000c1b02b4 | 7;
    _DAT_ffff00000c1b02bc = _DAT_ffff00000c1b02bc & 0xfffffffe;
    _DAT_ffff00000c1b02c4 = _DAT_ffff00000c1b02c4 & 0xfffffffe;
  }
  return;
}



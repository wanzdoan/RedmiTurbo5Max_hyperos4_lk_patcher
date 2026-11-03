// ffff0000907578c0  size=276  FUN_ffff0000907578c0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907578c0(int param_1,long param_2)

{
  if ((*(char *)(param_2 + 0x990) != '\0') && (*(char *)(param_2 + 0x2d8) != '\0')) {
    _DAT_ffff000032500198 = *(undefined4 *)(param_2 + 0x92c);
    _DAT_ffff000032500194 = _DAT_ffff00009087b6c4 - _DAT_ffff00009087b6cc;
    _DAT_ffff00003250019c =
         _DAT_ffff00003250019c & 0xffff8000 |
         _DAT_ffff00003250019c & 0xfe | (uint)(param_1 == 0xb7) |
         (_DAT_ffff00009087b6cc & 0x7f) << 8;
    _DAT_ffff0000325001d8 = (uint)(_DAT_ffff00009087b6cc == 0);
  }
  _DAT_ffff000032500004 = 0x20;
  _DAT_ffff000032500008 = 1;
  _DAT_ffff00003250000c = _DAT_ffff00003250000c & 0xffff7ffe | 0x20;
  _DAT_ffff000032510140 = 0;
  _DAT_ffff000032500158 = _DAT_ffff000032500158 | 0x11;
  _DAT_ffff000032500098 = *(undefined4 *)(param_2 + 0x928);
  _DAT_ffff00003250009c = *(undefined4 *)(param_2 + 0x92c);
  _DAT_ffff0000325000a0 = *(undefined4 *)(param_2 + 0x928);
  _DAT_ffff0000325000a4 = *(undefined4 *)(param_2 + 0x92c);
  _DAT_ffff0000325000a8 = *(undefined4 *)(param_2 + 0x928);
  _DAT_ffff0000325000ac = *(undefined4 *)(param_2 + 0x92c);
  return 0;
}



// ffff0000907c2c08  size=136  FUN_ffff0000907c2c08

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907c2c08(ulong param_1,long param_2)

{
  bool bVar1;
  ulong uVar2;
  
  FUN_ffff0000907175bc(0xffff00009090d858);
  bVar1 = (param_1 & 7) != 0;
  uVar2 = param_1 + 7 & 0xfffffffffffffff8;
  _DAT_ffff00009090d838 = param_1;
  if (bVar1) {
    _DAT_ffff00009090d838 = uVar2;
  }
  _DAT_ffff00009090d840 = 0;
  if (bVar1) {
    _DAT_ffff00009090d840 = param_1 - uVar2;
  }
  _DAT_ffff00009090d848 = 0;
  _DAT_ffff00009090d850 = 0;
  _DAT_ffff00009090d840 = _DAT_ffff00009090d840 + param_2;
  _DAT_ffff00009090d890 = &DAT_ffff00009090d890;
  _DAT_ffff00009090d898 = &DAT_ffff00009090d890;
  if (_DAT_ffff00009090d840 != 0) {
    *(long *)(_DAT_ffff00009090d838 + 0x10) = _DAT_ffff00009090d840;
    FUN_ffff0000907c2afc();
    return;
  }
  return;
}



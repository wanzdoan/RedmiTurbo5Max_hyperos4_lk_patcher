// ffff0000907c327c  size=136  FUN_ffff0000907c327c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907c327c(ulong param_1,long param_2)

{
  bool bVar1;
  ulong uVar2;
  
  FUN_ffff000090717700(0xffff00009090d918);
  bVar1 = (param_1 & 7) != 0;
  uVar2 = param_1 + 7 & 0xfffffffffffffff8;
  _DAT_ffff00009090d8f8 = param_1;
  if (bVar1) {
    _DAT_ffff00009090d8f8 = uVar2;
  }
  _DAT_ffff00009090d900 = 0;
  if (bVar1) {
    _DAT_ffff00009090d900 = param_1 - uVar2;
  }
  _DAT_ffff00009090d908 = 0;
  _DAT_ffff00009090d910 = 0;
  _DAT_ffff00009090d900 = _DAT_ffff00009090d900 + param_2;
  _DAT_ffff00009090d950 = &DAT_ffff00009090d950;
  _DAT_ffff00009090d958 = &DAT_ffff00009090d950;
  if (_DAT_ffff00009090d900 != 0) {
    *(long *)(_DAT_ffff00009090d8f8 + 0x10) = _DAT_ffff00009090d900;
    FUN_ffff0000907c3170();
    return;
  }
  return;
}



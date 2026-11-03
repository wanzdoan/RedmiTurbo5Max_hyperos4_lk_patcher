
void FUN_ffff0000907c327c(ulong param_1,long param_2)

{
  long lVar1;
  bool bVar2;
  ulong uVar3;
  
  FUN_ffff000090717700(&DAT_ffff00009090d918);
  bVar2 = (param_1 & 7) != 0;
  uVar3 = param_1 + 7 & 0xfffffffffffffff8;
  DAT_ffff00009090d8f8 = param_1;
  if (bVar2) {
    DAT_ffff00009090d8f8 = uVar3;
  }
  lVar1 = 0;
  if (bVar2) {
    lVar1 = param_1 - uVar3;
  }
  DAT_ffff00009090d908 = 0;
  DAT_ffff00009090d910 = 0;
  DAT_ffff00009090d900 = lVar1 + param_2;
  DAT_ffff00009090d950 = &DAT_ffff00009090d950;
  DAT_ffff00009090d958 = &DAT_ffff00009090d950;
  if (DAT_ffff00009090d900 != 0) {
    *(long *)(DAT_ffff00009090d8f8 + 0x10) = DAT_ffff00009090d900;
    FUN_ffff0000907c3170();
    return;
  }
  return;
}



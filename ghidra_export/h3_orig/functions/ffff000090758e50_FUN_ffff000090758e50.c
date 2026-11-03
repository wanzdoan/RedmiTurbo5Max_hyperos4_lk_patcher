// ffff000090758e50  size=56  FUN_ffff000090758e50

void FUN_ffff000090758e50(ulong param_1)

{
  long lVar1;
  
  lVar1 = 0;
  do {
    if ((&DAT_ffff00009081f3dc)[(param_1 & 0xffffffff) * 0x4b + lVar1] == -1) {
      return;
    }
    lVar1 = lVar1 + 1;
  } while (lVar1 != 0x4b);
  return;
}




void FUN_ffff0000907590e8(ulong param_1)

{
  long lVar1;
  
  lVar1 = 0;
  do {
    if ((&DAT_ffff00009081f3fc)[(param_1 & 0xffffffff) * 0x4b + lVar1] == -1) {
      return;
    }
    lVar1 = lVar1 + 1;
  } while (lVar1 != 0x4b);
  return;
}



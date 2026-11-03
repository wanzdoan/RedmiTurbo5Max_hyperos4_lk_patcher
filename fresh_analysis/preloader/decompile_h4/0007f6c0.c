// File offset entry: 0x7f6c0

int FUN_0207f5c0(uint param_1)

{
  uint *puVar1;
  long lVar2;
  
  if ((bRam000000000211cf08 & 1) == 0) {
    FUN_0207f514();
  }
  lVar2 = 0;
  do {
    if (lVar2 == 0x10) {
      return 0;
    }
    puVar1 = (uint *)(lRam000000000211cf10 + lVar2);
    lVar2 = lVar2 + 4;
  } while (param_1 != *puVar1 >> 0x10);
  return (int)lRam000000000211cf10 + (*puVar1 & 0xffff);
}



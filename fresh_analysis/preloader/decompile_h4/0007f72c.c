// File offset entry: 0x7f72c

int FUN_0207f62c(uint param_1)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  
  if ((bRam000000000211cf08 & 1) == 0) {
    FUN_0207f514();
  }
  uVar1 = 0;
  uVar2 = 0;
  while ((uVar1 != 4 &&
         (uVar2 = *(uint *)(lRam000000000211cf10 + uVar1 * 4), param_1 != uVar2 >> 0x10))) {
    uVar1 = uVar1 + 1;
  }
  do {
    uVar3 = iRam000000000011d978 - 4;
    if (2 < uVar1) break;
    uVar3 = *(uint *)(lRam000000000211cf10 + 4 + uVar1 * 4);
    uVar1 = uVar1 + 1;
  } while (uVar3 < 0x10000);
  return (uVar3 & 0xffff) - (uVar2 & 0xffff);
}



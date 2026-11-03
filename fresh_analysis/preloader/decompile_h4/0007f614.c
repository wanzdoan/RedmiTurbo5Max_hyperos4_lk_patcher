// File offset entry: 0x7f614

void FUN_0207f514(void)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  uint uVar4;
  
  uRam000000000211cf10 = 0x11d800;
  for (lVar3 = 0; lVar3 != 0x10; lVar3 = lVar3 + 4) {
    *(undefined4 *)(lVar3 + 0x11d800) = 0;
  }
  uRam000000000011d800 = 0xe31c002cd8a300bc;
  uRam000000000011d808 = 0x95270060db450020;
  uVar4 = 0x10;
  for (lVar3 = 0; lVar3 != 0x10; lVar3 = lVar3 + 4) {
    uVar1 = *(uint *)(lVar3 + 0x11d800);
    if (0xffff < uVar1) {
      uVar2 = uVar1 & 0xffff0000 | uVar4;
      uVar4 = uVar4 + (uVar1 & 0xffff);
      *(uint *)(lVar3 + 0x11d800) = uVar2;
    }
  }
  uRam000000000011d978 = 0x72590180;
  uRam000000000211cf08 = 1;
  return;
}



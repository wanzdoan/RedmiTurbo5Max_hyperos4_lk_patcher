// File offset entry: 0x5cb98

void FUN_0205ca98(long param_1)

{
  uint uVar1;
  ulong uVar2;
  int iVar3;
  ulong uVar4;
  
  for (uVar2 = 0; uVar2 != 0x100; uVar2 = uVar2 + 1) {
    iVar3 = 8;
    uVar4 = uVar2 & 0xffffffff;
    do {
      iVar3 = iVar3 + -1;
      uVar1 = (uint)(uVar4 >> 1);
      if ((uVar4 & 1) != 0) {
        uVar1 = (uint)uVar4 >> 1 ^ 0xedb88320;
      }
      uVar4 = (ulong)uVar1;
    } while (iVar3 != 0);
    *(uint *)(param_1 + uVar2 * 4) = uVar1;
  }
  return;
}



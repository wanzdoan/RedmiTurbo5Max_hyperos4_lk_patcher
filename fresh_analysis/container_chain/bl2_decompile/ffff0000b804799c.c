
int FUN_ffff0000b804799c(long param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  long *plVar5;
  
  iVar4 = 0;
  uVar2 = (uint)*(byte *)(param_1 + 0x10) << 0x18 | (uint)*(byte *)(param_1 + 0x11) << 0x10 |
          (uint)*(byte *)(param_1 + 0x12) << 8 | (uint)*(byte *)(param_1 + 0x13);
  uVar3 = (*(uint *)(param_1 + 4) & 0xff00ff00) >> 8 | (*(uint *)(param_1 + 4) & 0xff00ff) << 8;
  plVar5 = (long *)(((ulong)*(byte *)(param_1 + 0x11) << 0x10 |
                     (ulong)*(byte *)(param_1 + 0x10) << 0x18 |
                     (ulong)*(byte *)(param_1 + 0x12) << 8 | (ulong)*(byte *)(param_1 + 0x13)) +
                    param_1 + 8);
  uVar1 = uVar2;
  while( true ) {
    if ((ulong)(uVar3 >> 0x10 | uVar3 << 0x10) - 0x10 < (ulong)uVar1) {
      return -8;
    }
    if (*plVar5 == 0) break;
    iVar4 = iVar4 + 1;
    plVar5 = plVar5 + 2;
    uVar1 = uVar1 + 0x10;
    if (uVar1 < uVar2) {
      return -8;
    }
  }
  return iVar4;
}



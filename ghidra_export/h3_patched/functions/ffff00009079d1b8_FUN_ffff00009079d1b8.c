// ffff00009079d1b8  size=128  FUN_ffff00009079d1b8

undefined8 FUN_ffff00009079d1b8(long param_1,long param_2)

{
  uint uVar1;
  long lVar2;
  uint uVar3;
  ushort *puVar4;
  ushort *puVar5;
  
  uVar3 = *(uint *)(param_1 + 8);
  uVar1 = *(uint *)(param_2 + 8);
  if (uVar3 >= uVar1 && uVar3 != uVar1) {
    return 1;
  }
  if (uVar3 < uVar1) {
    return 0xffffffff;
  }
  if (-1 < (int)(uVar3 - 1)) {
    lVar2 = (ulong)(uVar3 - 1) * 2;
    puVar4 = (ushort *)(*(long *)(param_2 + 0x10) + lVar2);
    puVar5 = (ushort *)(*(long *)(param_1 + 0x10) + lVar2);
    do {
      if (*puVar4 < *puVar5) {
        return 1;
      }
      if (*puVar5 < *puVar4) {
        return 0xffffffff;
      }
      puVar5 = puVar5 + -1;
      puVar4 = puVar4 + -1;
      uVar3 = uVar3 - 1;
    } while (0 < (int)uVar3);
  }
  return 0;
}



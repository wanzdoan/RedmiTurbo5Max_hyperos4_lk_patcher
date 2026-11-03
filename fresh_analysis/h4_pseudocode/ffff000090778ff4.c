
void FUN_ffff000090778ff4(long param_1)

{
  uint uVar1;
  uint uVar2;
  undefined1 uVar3;
  int iVar4;
  long lVar5;
  
  uVar1 = *(uint *)(param_1 + 0x1734);
  uVar2 = (uint)*(ushort *)(param_1 + 0x1730) | 2 << (ulong)(uVar1 & 0x1f);
  *(short *)(param_1 + 0x1730) = (short)uVar2;
  if ((int)uVar1 < 0xe) {
    iVar4 = uVar1 + 3;
  }
  else {
    lVar5 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar5 + 1;
    *(char *)(*(long *)(param_1 + 0x10) + lVar5) = (char)uVar2;
    lVar5 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar5 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar5) = *(undefined1 *)(param_1 + 0x1731);
    uVar2 = 2 >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f);
    iVar4 = *(int *)(param_1 + 0x1734) + -0xd;
    *(short *)(param_1 + 0x1730) = (short)uVar2;
  }
  uVar3 = (undefined1)uVar2;
  *(int *)(param_1 + 0x1734) = iVar4;
  if (iVar4 < 10) {
    iVar4 = iVar4 + 7;
  }
  else {
    lVar5 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar5 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar5) = uVar3;
    lVar5 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar5 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar5) = *(undefined1 *)(param_1 + 0x1731);
    *(undefined2 *)(param_1 + 0x1730) = 0;
    iVar4 = *(int *)(param_1 + 0x1734) + -9;
    uVar3 = 0;
  }
  *(int *)(param_1 + 0x1734) = iVar4;
  if (iVar4 == 0x10) {
    lVar5 = *(long *)(param_1 + 0x28);
    iVar4 = 0;
    *(long *)(param_1 + 0x28) = lVar5 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar5) = uVar3;
    lVar5 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar5 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar5) = *(undefined1 *)(param_1 + 0x1731);
    *(undefined2 *)(param_1 + 0x1730) = 0;
  }
  else {
    if (iVar4 < 8) {
      return;
    }
    lVar5 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar5 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar5) = uVar3;
    iVar4 = *(int *)(param_1 + 0x1734) + -8;
    *(ushort *)(param_1 + 0x1730) = (ushort)*(byte *)(param_1 + 0x1731);
  }
  *(int *)(param_1 + 0x1734) = iVar4;
  return;
}



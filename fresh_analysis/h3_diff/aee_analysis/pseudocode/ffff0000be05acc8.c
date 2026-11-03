
void FUN_ffff0000be05acc8(long param_1,undefined8 param_2,long param_3,uint param_4)

{
  uint uVar1;
  ushort uVar2;
  int iVar3;
  long lVar4;
  
  uVar1 = *(uint *)(param_1 + 0x1734);
  if ((int)uVar1 < 0xe) {
    iVar3 = uVar1 + 3;
    uVar2 = *(ushort *)(param_1 + 0x1730) | (ushort)(param_4 << (ulong)(uVar1 & 0x1f));
  }
  else {
    lVar4 = *(long *)(param_1 + 0x28);
    uVar1 = (uint)*(ushort *)(param_1 + 0x1730) | param_4 << (ulong)(uVar1 & 0x1f);
    *(short *)(param_1 + 0x1730) = (short)uVar1;
    *(long *)(param_1 + 0x28) = lVar4 + 1;
    *(char *)(*(long *)(param_1 + 0x10) + lVar4) = (char)uVar1;
    lVar4 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar4 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar4) = *(undefined1 *)(param_1 + 0x1731);
    iVar3 = *(int *)(param_1 + 0x1734) + -0xd;
    uVar2 = (ushort)((param_4 & 0xffff) >> (ulong)(0x10U - *(int *)(param_1 + 0x1734) & 0x1f));
  }
  *(ushort *)(param_1 + 0x1730) = uVar2;
  if (iVar3 < 9) {
    if (0 < iVar3) {
      lVar4 = *(long *)(param_1 + 0x28);
      *(long *)(param_1 + 0x28) = lVar4 + 1;
      *(char *)(*(long *)(param_1 + 0x10) + lVar4) = (char)uVar2;
    }
  }
  else {
    lVar4 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar4 + 1;
    *(char *)(*(long *)(param_1 + 0x10) + lVar4) = (char)uVar2;
    lVar4 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar4 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar4) = *(undefined1 *)(param_1 + 0x1731);
  }
  lVar4 = *(long *)(param_1 + 0x28);
  *(undefined2 *)(param_1 + 0x1730) = 0;
  *(undefined4 *)(param_1 + 0x1734) = 0;
  *(long *)(param_1 + 0x28) = lVar4 + 1;
  *(char *)(*(long *)(param_1 + 0x10) + lVar4) = (char)param_3;
  lVar4 = *(long *)(param_1 + 0x28);
  *(long *)(param_1 + 0x28) = lVar4 + 1;
  *(char *)(*(long *)(param_1 + 0x10) + lVar4) = (char)((ulong)param_3 >> 8);
  lVar4 = *(long *)(param_1 + 0x28);
  *(long *)(param_1 + 0x28) = lVar4 + 1;
  *(char *)(*(long *)(param_1 + 0x10) + lVar4) = (char)~(uint)param_3;
  lVar4 = *(long *)(param_1 + 0x28);
  *(long *)(param_1 + 0x28) = lVar4 + 1;
  *(char *)(*(long *)(param_1 + 0x10) + lVar4) = (char)(~(uint)param_3 >> 8);
  if (param_3 != 0) {
    FUN_ffff0000be0607b0(*(long *)(param_1 + 0x10) + *(long *)(param_1 + 0x28),param_2,param_3);
  }
  *(long *)(param_1 + 0x28) = *(long *)(param_1 + 0x28) + param_3;
  return;
}



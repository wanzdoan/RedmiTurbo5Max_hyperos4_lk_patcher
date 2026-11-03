
int FUN_ffff0000be0579ec(long param_1)

{
  ushort uVar1;
  ushort uVar2;
  ushort uVar3;
  ushort uVar4;
  int iVar5;
  long lVar6;
  long lVar7;
  
  iVar5 = FUN_ffff0000be057e10();
  if (iVar5 == 0) {
    lVar7 = *(long *)(param_1 + 0x38);
    lVar6 = *(long *)(lVar7 + 0x78);
    *(ulong *)(lVar7 + 0x68) = (ulong)*(uint *)(lVar7 + 0x50) << 1;
    *(undefined2 *)(lVar6 + (ulong)(*(int *)(lVar7 + 0x84) - 1) * 2) = 0;
    FUN_ffff0000be060ae0(lVar6,0);
    *(undefined8 *)(lVar7 + 0x98) = 0;
    lVar6 = (long)*(int *)(lVar7 + 0xc4) * 0x10;
    *(undefined4 *)(lVar7 + 0x172c) = 0;
    *(undefined4 *)(lVar7 + 0x80) = 0;
    uVar1 = *(ushort *)(&DAT_ffff0000be0ef0ea + lVar6);
    *(undefined4 *)(lVar7 + 0xa0) = 2;
    uVar2 = *(ushort *)(&DAT_ffff0000be0ef0e8 + lVar6);
    uVar3 = *(ushort *)(&DAT_ffff0000be0ef0ec + lVar6);
    uVar4 = *(ushort *)(&DAT_ffff0000be0ef0ee + lVar6);
    *(undefined8 *)(lVar7 + 0xb4) = 0x200000000;
    *(undefined8 *)(lVar7 + 0xa8) = 0;
    *(uint *)(lVar7 + 0xcc) = (uint)uVar2;
    *(uint *)(lVar7 + 0xd0) = (uint)uVar3;
    *(uint *)(lVar7 + 0xbc) = (uint)uVar4;
    *(uint *)(lVar7 + 0xc0) = (uint)uVar1;
  }
  return iVar5;
}



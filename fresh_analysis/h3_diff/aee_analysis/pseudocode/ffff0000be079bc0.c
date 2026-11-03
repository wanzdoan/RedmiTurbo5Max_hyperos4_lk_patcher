
undefined4 FUN_ffff0000be079bc0(long param_1,long param_2)

{
  uint uVar1;
  uint uVar2;
  undefined4 uVar3;
  ushort *puVar4;
  uint uVar5;
  ushort *puVar6;
  ulong uVar7;
  ushort *puVar8;
  ushort *puVar9;
  int iVar10;
  
  uVar2 = *(uint *)(param_2 + 8);
  puVar4 = *(ushort **)(param_1 + 0x10);
  if (uVar2 == 0) {
    uVar5 = 0;
    puVar9 = puVar4;
  }
  else {
    iVar10 = 0;
    puVar6 = *(ushort **)(param_2 + 0x10);
    puVar8 = puVar4;
    uVar5 = uVar2;
    do {
      uVar1 = (iVar10 - (uint)*puVar6) + (uint)*puVar8 + 0x10000;
      iVar10 = -(uint)(uVar1 < 0x10000);
      uVar5 = uVar5 - 1;
      puVar9 = puVar8 + 1;
      *puVar8 = (ushort)uVar1;
      puVar6 = puVar6 + 1;
      puVar8 = puVar9;
    } while (uVar5 != 0);
    uVar5 = (uint)(uVar1 < 0x10000);
  }
  uVar1 = *(uint *)(param_1 + 8);
  uVar7 = (ulong)uVar1;
  if (uVar2 < uVar1) {
    iVar10 = uVar1 - uVar2;
    do {
      uVar2 = (*puVar9 | 0x10000) - uVar5;
      uVar5 = (uint)(uVar2 < 0x10000);
      iVar10 = iVar10 + -1;
      *puVar9 = (ushort)uVar2;
      puVar9 = puVar9 + 1;
    } while (iVar10 != 0);
  }
  if (1 < uVar1) {
    do {
      if (puVar4[uVar7 - 1] != 0) goto LAB_ffff0000be079c74;
      uVar7 = uVar7 - 1;
    } while (1 < (uint)uVar7);
    uVar7 = 1;
  }
LAB_ffff0000be079c74:
  uVar3 = 0;
  if (uVar5 != 0) {
    uVar3 = 0xfffffffd;
  }
  *(int *)(param_1 + 8) = (int)uVar7;
  return uVar3;
}



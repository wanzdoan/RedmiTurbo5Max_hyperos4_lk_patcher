// ffff00009071e1bc  size=344  FUN_ffff00009071e1bc

void FUN_ffff00009071e1bc(long param_1,long param_2,long param_3,undefined4 param_4)

{
  int iVar1;
  undefined4 *puVar2;
  uint uVar3;
  uint uVar4;
  undefined4 uVar5;
  undefined4 uVar6;
  int iVar7;
  int iVar8;
  int iVar9;
  
  uVar3 = *(uint *)(param_1 + 4);
  uVar4 = *(uint *)(param_1 + 0xc);
  uVar5 = *(undefined4 *)(param_1 + 0x10);
  puVar2 = (undefined4 *)(param_3 + (ulong)uVar3 + (ulong)*(uint *)(param_1 + 8));
  *puVar2 = param_4;
  puVar2[1] = uVar3;
  puVar2[2] = uVar5;
  FUN_ffff00009077e858(param_3 + (ulong)uVar3,param_2 + (ulong)uVar4);
  uVar3 = uVar3 + *(int *)(param_1 + 8) + 0x80;
  puVar2[7] = uVar3;
  uVar4 = *(uint *)(param_1 + 0x1c);
  puVar2[8] = *(undefined4 *)(param_1 + 0x20);
  FUN_ffff00009077e858(param_3 + (ulong)uVar3,param_2 + (ulong)uVar4);
  uVar3 = *(int *)(param_1 + 0x20) + uVar3;
  puVar2[9] = uVar3;
  uVar4 = *(uint *)(param_1 + 0x24);
  puVar2[10] = *(undefined4 *)(param_1 + 0x28);
  FUN_ffff00009077e858(param_3 + (ulong)uVar3,param_2 + (ulong)uVar4);
  uVar3 = *(int *)(param_1 + 0x28) + uVar3;
  puVar2[0xd] = uVar3;
  uVar4 = *(uint *)(param_1 + 0x34);
  puVar2[0xe] = *(undefined4 *)(param_1 + 0x38);
  FUN_ffff00009077e858
            (param_3 + (ulong)uVar3,param_2 + (ulong)uVar4,*(undefined4 *)(param_1 + 0x38));
  uVar3 = *(int *)(param_1 + 0x38) + uVar3;
  puVar2[0x11] = uVar3;
  uVar4 = *(uint *)(param_1 + 0x44);
  puVar2[0x12] = *(undefined4 *)(param_1 + 0x48);
  FUN_ffff00009077e858
            (param_3 + (ulong)uVar3,param_2 + (ulong)uVar4,*(undefined4 *)(param_1 + 0x48));
  uVar3 = *(int *)(param_1 + 0x48) + uVar3;
  puVar2[0x1d] = uVar3;
  uVar4 = *(uint *)(param_1 + 0x74);
  puVar2[0x1e] = *(undefined4 *)(param_1 + 0x78);
  FUN_ffff00009077e858
            (param_3 + (ulong)uVar3,param_2 + (ulong)uVar4,*(undefined4 *)(param_1 + 0x78));
  uVar5 = *(undefined4 *)(param_1 + 0x60);
  uVar6 = *(undefined4 *)(param_1 + 0x68);
  puVar2[0x16] = *(undefined4 *)(param_1 + 0x58);
  iVar7 = *(int *)(param_1 + 0x78);
  puVar2[0x18] = uVar5;
  iVar8 = *(int *)(param_1 + 0x58);
  puVar2[0x1a] = uVar6;
  iVar7 = iVar7 + uVar3;
  iVar9 = *(int *)(param_1 + 0x68);
  iVar8 = iVar8 + iVar7;
  iVar1 = *(int *)(param_1 + 0x60) + iVar8;
  puVar2[0x15] = iVar7;
  puVar2[0x17] = iVar8;
  uVar5 = *(undefined4 *)(param_1 + 0x70);
  puVar2[0x19] = iVar1;
  puVar2[0x1b] = iVar9 + iVar1;
  puVar2[0x1c] = uVar5;
  return;
}



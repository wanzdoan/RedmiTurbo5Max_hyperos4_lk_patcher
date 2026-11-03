// ffff00009071e4a0  size=272  FUN_ffff00009071e4a0

void FUN_ffff00009071e4a0
               (long param_1,undefined4 *param_2,long param_3,int param_4,undefined4 param_5)

{
  int iVar1;
  int iVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  int iVar7;
  int iVar8;
  int iVar9;
  uint uVar10;
  
  iVar4 = *(int *)(param_1 + 0x58);
  iVar5 = *(int *)(param_1 + 0x60);
  iVar6 = *(int *)(param_1 + 0x68);
  iVar1 = iVar4 + 0x58;
  *param_2 = 0x58;
  param_2[1] = iVar4;
  iVar7 = *(int *)(param_1 + 0x70);
  iVar8 = *(int *)(param_1 + 0x18);
  param_2[3] = param_4;
  param_2[2] = iVar1;
  iVar1 = iVar1 + param_4;
  iVar4 = iVar5 + iVar1;
  iVar9 = *(int *)(param_1 + 0x40);
  iVar2 = iVar6 + iVar4;
  param_2[4] = iVar1;
  param_2[5] = iVar5;
  uVar3 = iVar7 + iVar2;
  param_2[6] = iVar4;
  param_2[7] = iVar6;
  iVar5 = *(int *)(param_1 + 0x30);
  iVar1 = iVar8 + uVar3;
  param_2[8] = iVar2;
  param_2[9] = iVar7;
  iVar2 = *(int *)(param_1 + 0x50);
  param_2[10] = uVar3;
  param_2[0xb] = iVar8;
  iVar4 = iVar5 + iVar1;
  uVar10 = *(uint *)(param_1 + 0x14);
  param_2[0xc] = iVar1;
  param_2[0xd] = iVar5;
  iVar1 = iVar9 + iVar4;
  iVar5 = *(int *)(param_1 + 0x78);
  param_2[0xe] = iVar4;
  param_2[0xf] = iVar9;
  iVar4 = iVar2 + iVar1;
  param_2[0x10] = iVar1;
  param_2[0x11] = iVar2;
  param_2[0x12] = iVar4;
  param_2[0x13] = iVar5;
  param_2[0x14] = iVar5 + iVar4;
  param_2[0x15] = param_5;
  FUN_ffff00009077eb20((long)param_2 + (ulong)uVar3,param_3 + (ulong)uVar10);
  FUN_ffff00009077eb20
            ((long)param_2 + (ulong)(uint)param_2[0xc],param_3 + (ulong)*(uint *)(param_1 + 0x2c),
             *(undefined4 *)(param_1 + 0x30));
  FUN_ffff00009077eb20
            ((long)param_2 + (ulong)(uint)param_2[0xe],param_3 + (ulong)*(uint *)(param_1 + 0x3c),
             *(undefined4 *)(param_1 + 0x40));
  FUN_ffff00009077eb20
            ((long)param_2 + (ulong)(uint)param_2[0x10],param_3 + (ulong)*(uint *)(param_1 + 0x4c),
             *(undefined4 *)(param_1 + 0x50));
  FUN_ffff00009077eb20
            ((long)param_2 + (ulong)(uint)param_2[0x12],param_3 + (ulong)*(uint *)(param_1 + 0x74),
             *(undefined4 *)(param_1 + 0x78));
  return;
}



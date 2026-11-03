
void FUN_ffff0000be029ab4(undefined2 *param_1)

{
  undefined2 *puVar1;
  undefined2 *puVar2;
  int iVar3;
  undefined2 uVar4;
  uint uVar5;
  undefined2 uVar6;
  undefined2 uVar7;
  undefined2 uVar8;
  undefined2 uVar9;
  undefined2 uVar10;
  ushort local_34 [2];
  
  puVar1 = DAT_ffff0000be118088;
  local_34[0] = 0;
  iVar3 = (*(code *)*DAT_ffff0000be118158)(*DAT_ffff0000be118088,local_34);
  puVar2 = DAT_ffff0000be118090;
  uVar6 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar1[2];
    uVar6 = (undefined2)
            (((uint)(ushort)puVar1[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*DAT_ffff0000be118158)(*DAT_ffff0000be118090,local_34);
  puVar1 = DAT_ffff0000be118098;
  uVar7 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar2[2];
    uVar7 = (undefined2)
            (((uint)(ushort)puVar2[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*DAT_ffff0000be118158)(*DAT_ffff0000be118098,local_34);
  puVar2 = DAT_ffff0000be1180a0;
  uVar8 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar1[2];
    uVar8 = (undefined2)
            (((uint)(ushort)puVar1[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*DAT_ffff0000be118158)(*DAT_ffff0000be1180a0,local_34);
  puVar1 = DAT_ffff0000be1180a8;
  uVar9 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar2[2];
    uVar9 = (undefined2)
            (((uint)(ushort)puVar2[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*DAT_ffff0000be118158)(*DAT_ffff0000be1180a8,local_34);
  puVar2 = DAT_ffff0000be1180b0;
  uVar10 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar1[2];
    uVar10 = (undefined2)
             (((uint)(ushort)puVar1[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
             (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*DAT_ffff0000be118158)(*DAT_ffff0000be1180b0,local_34);
  uVar4 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar2[2];
    uVar4 = (undefined2)
            (((uint)(ushort)puVar2[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  *param_1 = uVar6;
  param_1[1] = uVar7;
  param_1[2] = uVar8;
  param_1[3] = uVar9;
  param_1[4] = uVar10;
  param_1[5] = uVar4;
  return;
}



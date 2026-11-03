// ffff0000907376b0  size=492  FUN_ffff0000907376b0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907376b0(undefined2 *param_1)

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
  
  puVar1 = _DAT_ffff00009086ddc8;
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de98)(*_DAT_ffff00009086ddc8,local_34);
  puVar2 = _DAT_ffff00009086ddd0;
  uVar6 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar1[2];
    uVar6 = (undefined2)
            (((uint)(ushort)puVar1[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de98)(*_DAT_ffff00009086ddd0,local_34);
  puVar1 = _DAT_ffff00009086ddd8;
  uVar7 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar2[2];
    uVar7 = (undefined2)
            (((uint)(ushort)puVar2[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de98)(*_DAT_ffff00009086ddd8,local_34);
  puVar2 = _DAT_ffff00009086dde0;
  uVar8 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar1[2];
    uVar8 = (undefined2)
            (((uint)(ushort)puVar1[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de98)(*_DAT_ffff00009086dde0,local_34);
  puVar1 = _DAT_ffff00009086dde8;
  uVar9 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar2[2];
    uVar9 = (undefined2)
            (((uint)(ushort)puVar2[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de98)(*_DAT_ffff00009086dde8,local_34);
  puVar2 = _DAT_ffff00009086ddf0;
  uVar10 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar1[2];
    uVar10 = (undefined2)
             (((uint)(ushort)puVar1[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
             (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de98)(*_DAT_ffff00009086ddf0,local_34);
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



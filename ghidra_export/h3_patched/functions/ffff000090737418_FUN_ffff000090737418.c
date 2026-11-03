// ffff000090737418  size=492  FUN_ffff000090737418

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090737418(undefined2 *param_1)

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
  
  puVar1 = _DAT_ffff00009086dd30;
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de00)(*_DAT_ffff00009086dd30,local_34);
  puVar2 = _DAT_ffff00009086dd38;
  uVar6 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar1[2];
    uVar6 = (undefined2)
            (((uint)(ushort)puVar1[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de00)(*_DAT_ffff00009086dd38,local_34);
  puVar1 = _DAT_ffff00009086dd40;
  uVar7 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar2[2];
    uVar7 = (undefined2)
            (((uint)(ushort)puVar2[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de00)(*_DAT_ffff00009086dd40,local_34);
  puVar2 = _DAT_ffff00009086dd48;
  uVar8 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar1[2];
    uVar8 = (undefined2)
            (((uint)(ushort)puVar1[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de00)(*_DAT_ffff00009086dd48,local_34);
  puVar1 = _DAT_ffff00009086dd50;
  uVar9 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar2[2];
    uVar9 = (undefined2)
            (((uint)(ushort)puVar2[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
            (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de00)(*_DAT_ffff00009086dd50,local_34);
  puVar2 = _DAT_ffff00009086dd58;
  uVar10 = 0;
  if (iVar3 == 0) {
    uVar5 = (uint)(ushort)puVar1[2];
    uVar10 = (undefined2)
             (((uint)(ushort)puVar1[1] << (ulong)(uVar5 & 0x1f) & (uint)local_34[0]) >>
             (ulong)(uVar5 & 0x1f));
  }
  local_34[0] = 0;
  iVar3 = (*(code *)*_DAT_ffff00009086de00)(*_DAT_ffff00009086dd58,local_34);
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



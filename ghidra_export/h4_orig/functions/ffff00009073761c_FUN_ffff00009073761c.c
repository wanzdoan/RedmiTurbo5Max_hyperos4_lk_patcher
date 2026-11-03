// ffff00009073761c  size=148  FUN_ffff00009073761c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073761c(ushort *param_1)

{
  undefined2 *puVar1;
  int iVar2;
  uint uVar3;
  ushort local_24 [2];
  
  FUN_ffff0000907376b0();
  puVar1 = _DAT_ffff00009086ddc8;
  local_24[0] = 0;
  iVar2 = (*(code *)*_DAT_ffff00009086de98)(*_DAT_ffff00009086ddc8,local_24);
  uVar3 = 0;
  if (iVar2 == 0) {
    uVar3 = (uint)(ushort)puVar1[2];
    uVar3 = ((uint)(ushort)puVar1[1] << (ulong)(uVar3 & 0x1f) & (uint)local_24[0]) >>
            (ulong)(uVar3 & 0x1f);
  }
  if (uVar3 < *param_1) {
    FUN_ffff0000907376b0(param_1);
  }
  param_1[5] = param_1[5] + 0x7b0;
  return;
}



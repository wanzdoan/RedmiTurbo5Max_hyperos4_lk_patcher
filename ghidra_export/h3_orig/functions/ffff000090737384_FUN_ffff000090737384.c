// ffff000090737384  size=148  FUN_ffff000090737384

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090737384(ushort *param_1)

{
  undefined2 *puVar1;
  int iVar2;
  uint uVar3;
  ushort local_24 [2];
  
  FUN_ffff000090737418();
  puVar1 = _DAT_ffff00009086dd30;
  local_24[0] = 0;
  iVar2 = (*(code *)*_DAT_ffff00009086de00)(*_DAT_ffff00009086dd30,local_24);
  uVar3 = 0;
  if (iVar2 == 0) {
    uVar3 = (uint)(ushort)puVar1[2];
    uVar3 = ((uint)(ushort)puVar1[1] << (ulong)(uVar3 & 0x1f) & (uint)local_24[0]) >>
            (ulong)(uVar3 & 0x1f);
  }
  if (uVar3 < *param_1) {
    FUN_ffff000090737418(param_1);
  }
  param_1[5] = param_1[5] + 0x7b0;
  return;
}



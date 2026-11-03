// ffff000090705670  size=116  FUN_ffff000090705670

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090705670(ulong param_1)

{
  undefined8 uVar1;
  int iVar2;
  uint uVar3;
  uint local_24;
  
  uVar1 = _DAT_ffff00009083d0e0;
  local_24 = 0;
  iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x114,&local_24);
  if (-1 < iVar2) {
    uVar3 = 8;
    if ((param_1 & 1) == 0) {
      uVar3 = 0;
    }
    local_24 = local_24 & 0xfffffff7 | uVar3;
    FUN_ffff000090737284(uVar1,0x114);
  }
  return;
}



// ffff000090705080  size=116  FUN_ffff000090705080

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090705080(ulong param_1)

{
  undefined8 uVar1;
  int iVar2;
  uint uVar3;
  uint local_24;
  
  uVar1 = _DAT_ffff00009083d080;
  local_24 = 0;
  iVar2 = FUN_ffff000090737554(_DAT_ffff00009083d080,0x114,&local_24);
  if (-1 < iVar2) {
    uVar3 = 0x10;
    if ((param_1 & 1) == 0) {
      uVar3 = 0;
    }
    local_24 = local_24 & 0xffffffef | uVar3;
    FUN_ffff00009073751c(uVar1,0x114);
  }
  return;
}



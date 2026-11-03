// ffff000090705448  size=156  FUN_ffff000090705448

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090705448(uint param_1)

{
  undefined8 uVar1;
  int iVar2;
  uint uVar3;
  uint local_24;
  
  uVar1 = _DAT_ffff00009083d0e0;
  if (param_1 < 0xf3d) {
    uVar3 = 0;
  }
  else if (param_1 >> 4 < 0x55f) {
    uVar3 = (param_1 - 0xed9 >> 2 & 0x3fff) / 0x19;
  }
  else {
    uVar3 = 0xb5;
  }
  local_24 = 0;
  iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x10b,&local_24);
  if (-1 < iVar2) {
    local_24 = local_24 & 0xffffff00 | uVar3;
    FUN_ffff000090737284(uVar1,0x10b);
  }
  return;
}



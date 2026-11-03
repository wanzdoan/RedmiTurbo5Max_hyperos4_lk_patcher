// ffff000090705744  size=144  FUN_ffff000090705744

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff000090705744(byte *param_1)

{
  byte bVar1;
  undefined4 uVar2;
  uint uVar3;
  uint uVar4;
  undefined4 local_28;
  uint local_24;
  
  local_28 = 0;
  uVar3 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,6,&local_28);
  uVar2 = local_28;
  local_24 = 0;
  uVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x107,&local_24);
  bVar1 = 0;
  if (uVar4 == 0) {
    bVar1 = uVar3 == 0 & (byte)uVar2;
  }
  *param_1 = bVar1 & (byte)((local_24 & 0x40) >> 6);
  return uVar4 | uVar3;
}



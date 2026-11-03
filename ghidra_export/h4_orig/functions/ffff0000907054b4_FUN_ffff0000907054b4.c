// ffff0000907054b4  size=192  FUN_ffff0000907054b4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907054b4(ulong param_1)

{
  uint uVar1;
  undefined8 uVar2;
  int iVar3;
  uint uVar4;
  uint local_24;
  
  uVar2 = _DAT_ffff00009083d080;
  uVar4 = 100;
  if ((param_1 & 1) == 0) {
    uVar4 = DAT_ffff000090816618;
  }
  if (uVar4 < 0x65) {
    uVar4 = 0;
  }
  else if (uVar4 >> 3 < 0x20d) {
    uVar1 = (uVar4 - 100 & 0xffff) * 0x47af >> 0x10;
    uVar4 = uVar1 + (((uVar4 - 100) - uVar1 & 0xfffe) >> 1) >> 4;
  }
  else {
    uVar4 = 0xa4;
  }
  local_24 = 0;
  iVar3 = FUN_ffff000090737554(_DAT_ffff00009083d080,0x109,&local_24);
  if (-1 < iVar3) {
    local_24 = local_24 & 0xffffff00 | uVar4;
    FUN_ffff00009073751c(uVar2,0x109);
  }
  return;
}




/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b5a14(void)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  undefined8 uVar4;
  
  iVar3 = FUN_ffff0000907aa45c();
  if (iVar3 == 0) {
    uVar1 = _DAT_ffff00001002d8f0 & 0x40000;
    uVar2 = _DAT_ffff00001002d8f0 & 0x80000;
    if ((_DAT_ffff000013260058 & 3) != 0) {
      uVar1 = _DAT_ffff000013260058 & 2;
      uVar2 = _DAT_ffff000013260058 & 1;
    }
    if (uVar2 == 0 || uVar1 != 0) {
      uVar1 = _DAT_ffff00001002d8f0 & 0x40000;
      uVar2 = _DAT_ffff00001002d8f0 & 0x80000;
      if ((_DAT_ffff000013260058 & 3) != 0) {
        uVar1 = _DAT_ffff000013260058 & 2;
        uVar2 = _DAT_ffff000013260058 & 1;
      }
      if ((uVar2 == 0) || (uVar1 == 0)) goto LAB_ffff0000907b5a28;
    }
    uVar4 = 1;
  }
  else {
LAB_ffff0000907b5a28:
    uVar4 = 0;
  }
  return uVar4;
}




/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907c3544(void)

{
  bool bVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  uVar2 = daif;
  if (((uint)uVar2 >> 7 & 1) != 0) {
    uVar2 = daif;
    bVar1 = ((uint)uVar2 >> 7 & 1) == 0;
    if (bVar1) {
      uVar3 = daif;
      daif = uVar3 | 0x80;
    }
    _DAT_ffff00009090d960 = (uint)bVar1;
    return;
  }
  FUN_ffff000090717738(&LAB_ffff0000908384a0,0xffffffff);
  return;
}



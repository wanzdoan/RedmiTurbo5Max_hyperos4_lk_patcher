// ffff0000907c2ed0  size=36  FUN_ffff0000907c2ed0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907c2ed0(void)

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
    _DAT_ffff00009090d8a0 = (uint)bVar1;
    return;
  }
  FUN_ffff0000907175f4(&LAB_ffff000090838480,0xffffffff);
  return;
}



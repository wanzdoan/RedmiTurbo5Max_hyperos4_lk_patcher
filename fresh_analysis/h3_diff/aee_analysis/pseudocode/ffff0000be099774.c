
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be099774(void)

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
    _DAT_ffff0000be1b7e30 = (uint)bVar1;
    return;
  }
  FUN_ffff0000be01a730(&LAB_ffff0000be0f45d0,0xffffffff);
  return;
}



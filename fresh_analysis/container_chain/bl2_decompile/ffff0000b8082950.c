
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b8082950(void)

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
    _DAT_ffff0000b81b9b50 = (uint)bVar1;
    return;
  }
  FUN_ffff0000b800ad90(&LAB_ffff0000b8107d68,0xffffffff);
  return;
}



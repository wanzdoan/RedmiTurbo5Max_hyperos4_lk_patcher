
ulong FUN_ffff0000be047d3c(void)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  
  uVar3 = FUN_ffff0000be063c7c();
  if (((uVar3 & 1) != 0) && (iVar1 = FUN_ffff0000be065774(), iVar1 == 0xfc11)) {
    iVar2 = FUN_ffff0000be048724();
    iVar1 = iVar2 + 7;
    if (-1 < iVar2) {
      iVar1 = iVar2;
    }
    return (ulong)(uint)(iVar1 >> 3);
  }
  uVar3 = FUN_ffff0000be048724();
  return uVar3;
}



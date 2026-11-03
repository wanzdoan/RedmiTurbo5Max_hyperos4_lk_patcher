
ulong FUN_ffff0000b803e984(void)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  
  uVar3 = FUN_ffff0000b804f554();
  if (((uVar3 & 1) != 0) && (iVar1 = FUN_ffff0000b8050384(), iVar1 == 0xfc11)) {
    iVar2 = FUN_ffff0000b803f36c();
    iVar1 = iVar2 + 7;
    if (-1 < iVar2) {
      iVar1 = iVar2;
    }
    return (ulong)(uint)(iVar1 >> 3);
  }
  uVar3 = FUN_ffff0000b803f36c();
  return uVar3;
}



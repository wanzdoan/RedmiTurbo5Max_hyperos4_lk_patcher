// ffff00009075c664  size=80  FUN_ffff00009075c664

ulong FUN_ffff00009075c664(void)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  
  uVar3 = FUN_ffff000090781944();
  if (((uVar3 & 1) != 0) && (iVar1 = FUN_ffff000090783694(), iVar1 == 0xfc11)) {
    iVar2 = FUN_ffff00009075d630();
    iVar1 = iVar2 + 7;
    if (-1 < iVar2) {
      iVar1 = iVar2;
    }
    return (ulong)(uint)(iVar1 >> 3);
  }
  uVar3 = FUN_ffff00009075d630();
  return uVar3;
}



// ffff00009075c8fc  size=80  FUN_ffff00009075c8fc

ulong FUN_ffff00009075c8fc(void)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  
  uVar3 = FUN_ffff000090781c0c();
  if (((uVar3 & 1) != 0) && (iVar1 = FUN_ffff00009078395c(), iVar1 == 0xfc11)) {
    iVar2 = FUN_ffff00009075d8c8();
    iVar1 = iVar2 + 7;
    if (-1 < iVar2) {
      iVar1 = iVar2;
    }
    return (ulong)(uint)(iVar1 >> 3);
  }
  uVar3 = FUN_ffff00009075d8c8();
  return uVar3;
}



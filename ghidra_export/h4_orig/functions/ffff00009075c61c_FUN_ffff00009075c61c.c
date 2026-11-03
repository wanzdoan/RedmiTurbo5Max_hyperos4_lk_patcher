// ffff00009075c61c  size=64  FUN_ffff00009075c61c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009075c61c(void)

{
  int iVar1;
  undefined4 uVar2;
  ulong uVar3;
  
  uVar3 = FUN_ffff000090781c0c();
  uVar2 = _DAT_ffff00009087bf30;
  if (((uVar3 & 1) != 0) &&
     (iVar1 = FUN_ffff00009078395c(), uVar2 = _DAT_ffff00009087bf30, iVar1 == 0xfc11)) {
    uVar2 = 0;
  }
  return uVar2;
}



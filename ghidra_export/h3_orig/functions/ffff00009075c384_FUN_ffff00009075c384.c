// ffff00009075c384  size=64  FUN_ffff00009075c384

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009075c384(void)

{
  int iVar1;
  undefined4 uVar2;
  ulong uVar3;
  
  uVar3 = FUN_ffff000090781944();
  uVar2 = _DAT_ffff00009087be98;
  if (((uVar3 & 1) != 0) &&
     (iVar1 = FUN_ffff000090783694(), uVar2 = _DAT_ffff00009087be98, iVar1 == 0xfc11)) {
    uVar2 = 0;
  }
  return uVar2;
}



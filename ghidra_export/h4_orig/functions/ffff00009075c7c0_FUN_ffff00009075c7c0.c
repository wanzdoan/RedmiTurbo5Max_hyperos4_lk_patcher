// ffff00009075c7c0  size=100  FUN_ffff00009075c7c0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075c7c0(void)

{
  int iVar1;
  undefined4 uVar2;
  ulong uVar3;
  undefined8 uVar4;
  
  uVar3 = FUN_ffff000090781c0c();
  if (((uVar3 & 1) == 0) ||
     (iVar1 = FUN_ffff00009078395c(), uVar4 = _DAT_ffff00009087bf28, iVar1 != 0xfc11)) {
    uVar4 = _DAT_ffff00009087bf28;
    uVar2 = FUN_ffff00009075e264();
  }
  else {
    uVar2 = FUN_ffff00009075e3d0();
  }
  FUN_ffff000090707c50(uVar4,uVar2);
  FUN_ffff00009075da38(1);
  return;
}



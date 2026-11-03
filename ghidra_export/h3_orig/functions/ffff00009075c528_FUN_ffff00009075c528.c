// ffff00009075c528  size=100  FUN_ffff00009075c528

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075c528(void)

{
  int iVar1;
  undefined4 uVar2;
  ulong uVar3;
  undefined8 uVar4;
  
  uVar3 = FUN_ffff000090781944();
  if (((uVar3 & 1) == 0) ||
     (iVar1 = FUN_ffff000090783694(), uVar4 = _DAT_ffff00009087be90, iVar1 != 0xfc11)) {
    uVar4 = _DAT_ffff00009087be90;
    uVar2 = FUN_ffff00009075dfcc();
  }
  else {
    uVar2 = FUN_ffff00009075e138();
  }
  FUN_ffff000090707cc4(uVar4,uVar2);
  FUN_ffff00009075d7a0(1);
  return;
}



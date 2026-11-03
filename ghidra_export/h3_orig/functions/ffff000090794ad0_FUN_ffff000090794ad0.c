// ffff000090794ad0  size=168  FUN_ffff000090794ad0

long FUN_ffff000090794ad0(long param_1,undefined8 param_2,undefined4 param_3,ulong param_4)

{
  int iVar1;
  long lVar2;
  
  lVar2 = *(long *)(param_1 + 0xb0);
  iVar1 = FUN_ffff00009078fa98(*(undefined4 *)(param_1 + 0xa8));
  if (iVar1 < 0) {
    lVar2 = -8;
  }
  else {
    lVar2 = lVar2 + 0x270;
    FUN_ffff0000907175f4(lVar2,0xffffffff);
    iVar1 = FUN_ffff00009078f374(0,iVar1,param_3,param_4 & 0xffffffff,param_2);
    FUN_ffff00009071768c(lVar2);
    if (iVar1 == 0) {
      lVar2 = *(long *)(param_1 + 0x28) * (param_4 & 0xffffffff);
    }
    else {
      lVar2 = -0x14;
    }
  }
  return lVar2;
}



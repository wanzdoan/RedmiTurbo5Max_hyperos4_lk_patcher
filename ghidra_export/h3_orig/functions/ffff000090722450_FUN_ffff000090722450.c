// ffff000090722450  size=92  FUN_ffff000090722450

void FUN_ffff000090722450(uint param_1)

{
  int iVar1;
  undefined4 uVar2;
  
  iVar1 = FUN_ffff000090703c8c();
  if (iVar1 != 0) {
    if ((param_1 & 0xff) == 10) {
      uVar2 = FUN_ffff000090703ca0();
      FUN_ffff0000907427d0(uVar2,0xd);
    }
    uVar2 = FUN_ffff000090703ca0();
    FUN_ffff0000907427d0(uVar2,param_1);
    return;
  }
  return;
}



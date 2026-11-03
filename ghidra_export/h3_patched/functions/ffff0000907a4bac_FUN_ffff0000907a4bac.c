// ffff0000907a4bac  size=164  FUN_ffff0000907a4bac

int FUN_ffff0000907a4bac(long param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = 0x100006;
  if ((param_1 != 0) && (param_3 != 0)) {
    iVar1 = FUN_ffff0000907a4948();
    if (iVar1 == 0) {
      FUN_ffff0000907b78d8();
      FUN_ffff000090707ce4(0xffff000088410000,0x10000);
      FUN_ffff000090707ce4(param_1,param_2);
      uVar2 = FUN_ffff00009077b06c(param_1);
      iVar1 = FUN_ffff0000907a48c8(uVar2,param_2);
      if (iVar1 == 0) {
        FUN_ffff00009077e858(param_3,DAT_ffff000090837c60,0x20);
      }
      FUN_ffff0000907b78f0();
    }
  }
  return iVar1;
}



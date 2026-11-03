
int FUN_ffff0000907a4e6c(long param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = 0x100006;
  if ((param_1 != 0) && (param_3 != 0)) {
    iVar1 = FUN_ffff0000907a4c08();
    if (iVar1 == 0) {
      FUN_ffff0000907b7f4c();
      FUN_ffff000090707c70(0xffff000088410000,0x10000);
      FUN_ffff000090707c70(param_1,param_2);
      uVar2 = FUN_ffff00009077b334(param_1);
      iVar1 = FUN_ffff0000907a4b88(uVar2,param_2);
      if (iVar1 == 0) {
        FUN_ffff00009077eb20(param_3,DAT_ffff000090837c80,0x20);
      }
      FUN_ffff0000907b7f64();
    }
  }
  return iVar1;
}



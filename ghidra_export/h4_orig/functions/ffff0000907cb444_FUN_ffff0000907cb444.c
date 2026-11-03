// ffff0000907cb444  size=292  FUN_ffff0000907cb444

void FUN_ffff0000907cb444(long param_1,long param_2,undefined8 param_3,long param_4)

{
  long lVar1;
  int iVar2;
  
  iVar2 = FUN_ffff0000907c7d34(param_3,1);
  if (iVar2 == 0) {
    iVar2 = FUN_ffff0000907c7260(param_2,param_4);
    if ((iVar2 == 0) && (iVar2 = FUN_ffff0000907c7260(param_2 + 0x18,param_4 + 0x18), iVar2 == 0)) {
      FUN_ffff0000907c7260(param_2 + 0x30,param_4 + 0x30);
      return;
    }
  }
  else {
    iVar2 = FUN_ffff0000907c7d34(param_3,0xffffffff);
    if (iVar2 != 0) {
      FUN_ffff0000907ca7dc(param_1,param_2,param_3,param_4,0,0);
      return;
    }
    iVar2 = FUN_ffff0000907c7260(param_2,param_4);
    if (iVar2 == 0) {
      lVar1 = param_2 + 0x18;
      iVar2 = FUN_ffff0000907c7260(lVar1,param_4 + 0x18);
      if (((iVar2 == 0) && (iVar2 = FUN_ffff0000907c7260(param_2 + 0x30,param_4 + 0x30), iVar2 == 0)
          ) && (iVar2 = FUN_ffff0000907c7d34(lVar1,0), iVar2 != 0)) {
        FUN_ffff0000907c8358(lVar1,param_1 + 8,lVar1);
        return;
      }
    }
  }
  return;
}



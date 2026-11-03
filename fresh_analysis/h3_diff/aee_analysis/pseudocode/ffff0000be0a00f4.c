
void FUN_ffff0000be0a00f4(long param_1,long param_2,undefined8 param_3,long param_4)

{
  long lVar1;
  int iVar2;
  
  iVar2 = FUN_ffff0000be09c9e4(param_3,1);
  if (iVar2 == 0) {
    iVar2 = FUN_ffff0000be09bf10(param_2,param_4);
    if ((iVar2 == 0) && (iVar2 = FUN_ffff0000be09bf10(param_2 + 0x18,param_4 + 0x18), iVar2 == 0)) {
      FUN_ffff0000be09bf10(param_2 + 0x30,param_4 + 0x30);
      return;
    }
  }
  else {
    iVar2 = FUN_ffff0000be09c9e4(param_3,0xffffffff);
    if (iVar2 != 0) {
      FUN_ffff0000be09f48c(param_1,param_2,param_3,param_4,0,0);
      return;
    }
    iVar2 = FUN_ffff0000be09bf10(param_2,param_4);
    if (iVar2 == 0) {
      lVar1 = param_2 + 0x18;
      iVar2 = FUN_ffff0000be09bf10(lVar1,param_4 + 0x18);
      if (((iVar2 == 0) && (iVar2 = FUN_ffff0000be09bf10(param_2 + 0x30,param_4 + 0x30), iVar2 == 0)
          ) && (iVar2 = FUN_ffff0000be09c9e4(lVar1,0), iVar2 != 0)) {
        FUN_ffff0000be09d008(lVar1,param_1 + 8,lVar1);
        return;
      }
    }
  }
  return;
}



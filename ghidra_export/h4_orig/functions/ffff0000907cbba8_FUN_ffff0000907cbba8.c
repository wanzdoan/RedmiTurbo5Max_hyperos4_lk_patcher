// ffff0000907cbba8  size=352  FUN_ffff0000907cbba8

int FUN_ffff0000907cbba8(long param_1,long param_2)

{
  long lVar1;
  int iVar2;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  lVar1 = param_2 + 0x30;
  iVar2 = FUN_ffff0000907c7d34(lVar1,0);
  if (iVar2 == 0) {
    iVar2 = 0;
  }
  else {
    local_40 = 0;
    uStack_38 = 0;
    local_50 = 0;
    uStack_48 = 0;
    local_60 = 0;
    uStack_58 = 0;
    FUN_ffff0000907c7040(&uStack_48);
    FUN_ffff0000907c7040(&local_60);
    iVar2 = FUN_ffff0000907c9fa4(&uStack_48,lVar1,param_1 + 8);
    if (iVar2 == 0) {
      iVar2 = FUN_ffff0000907c846c(&local_60,&uStack_48,&uStack_48);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff0000907cc6c4(&local_60,param_1);
        if (iVar2 == 0) {
          iVar2 = FUN_ffff0000907c846c(param_2,param_2,&local_60);
          if (iVar2 == 0) {
            iVar2 = FUN_ffff0000907cc6c4(param_2,param_1);
            if (iVar2 == 0) {
              param_2 = param_2 + 0x18;
              iVar2 = FUN_ffff0000907c846c(param_2,param_2,&local_60);
              if (iVar2 == 0) {
                iVar2 = FUN_ffff0000907cc6c4(param_2,param_1);
                if (iVar2 == 0) {
                  iVar2 = FUN_ffff0000907c846c(param_2,param_2,&uStack_48);
                  if (iVar2 == 0) {
                    iVar2 = FUN_ffff0000907cc6c4(param_2,param_1);
                    if (iVar2 == 0) {
                      iVar2 = FUN_ffff0000907c7528(lVar1,1);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    FUN_ffff0000907c7058(&uStack_48);
    FUN_ffff0000907c7058(&local_60);
  }
  return iVar2;
}



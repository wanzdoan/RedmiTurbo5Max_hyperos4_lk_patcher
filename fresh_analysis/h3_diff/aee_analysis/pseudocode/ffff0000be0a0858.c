
int FUN_ffff0000be0a0858(long param_1,long param_2)

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
  iVar2 = FUN_ffff0000be09c9e4(lVar1,0);
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
    FUN_ffff0000be09bcf0(&uStack_48);
    FUN_ffff0000be09bcf0(&local_60);
    iVar2 = FUN_ffff0000be09ec54(&uStack_48,lVar1,param_1 + 8);
    if (iVar2 == 0) {
      iVar2 = FUN_ffff0000be09d11c(&local_60,&uStack_48,&uStack_48);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff0000be0a1374(&local_60,param_1);
        if (iVar2 == 0) {
          iVar2 = FUN_ffff0000be09d11c(param_2,param_2,&local_60);
          if (iVar2 == 0) {
            iVar2 = FUN_ffff0000be0a1374(param_2,param_1);
            if (iVar2 == 0) {
              param_2 = param_2 + 0x18;
              iVar2 = FUN_ffff0000be09d11c(param_2,param_2,&local_60);
              if (iVar2 == 0) {
                iVar2 = FUN_ffff0000be0a1374(param_2,param_1);
                if (iVar2 == 0) {
                  iVar2 = FUN_ffff0000be09d11c(param_2,param_2,&uStack_48);
                  if (iVar2 == 0) {
                    iVar2 = FUN_ffff0000be0a1374(param_2,param_1);
                    if (iVar2 == 0) {
                      iVar2 = FUN_ffff0000be09c1d8(lVar1,1);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    FUN_ffff0000be09bd08(&uStack_48);
    FUN_ffff0000be09bd08(&local_60);
  }
  return iVar2;
}



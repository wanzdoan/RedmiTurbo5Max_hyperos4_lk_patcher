
int FUN_ffff0000907cb0b0(long param_1,long param_2)

{
  long lVar1;
  long lVar2;
  int iVar3;
  int iVar4;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  iVar3 = FUN_ffff0000907c7d34(param_2 + 0x30,1);
  if (iVar3 == 0) {
    if ((*(long *)(param_1 + 0x60) == 0) || (*(long *)(param_1 + 0x78) == 0)) {
      return -0x4f80;
    }
    iVar3 = FUN_ffff0000907c7d34(param_2,0);
    if (-1 < iVar3) {
      lVar1 = param_2 + 0x18;
      iVar3 = FUN_ffff0000907c7d34(lVar1,0);
      if (-1 < iVar3) {
        lVar2 = param_1 + 8;
        iVar3 = FUN_ffff0000907c7c44(param_2,lVar2);
        if ((iVar3 < 0) && (iVar3 = FUN_ffff0000907c7c44(lVar1,lVar2), iVar3 < 0)) {
          local_40 = 0;
          uStack_38 = 0;
          local_50 = 0;
          uStack_48 = 0;
          local_60 = 0;
          uStack_58 = 0;
          FUN_ffff0000907c7040(&uStack_48);
          FUN_ffff0000907c7040(&local_60);
          iVar3 = FUN_ffff0000907c846c(&uStack_48,lVar1,lVar1);
          if ((iVar3 == 0) &&
             (((iVar3 = FUN_ffff0000907cc6c4(&uStack_48,param_1), iVar3 == 0 &&
               (iVar3 = FUN_ffff0000907c846c(&local_60,param_2,param_2), iVar3 == 0)) &&
              (iVar3 = FUN_ffff0000907cc6c4(&local_60,param_1), iVar3 == 0)))) {
            if (*(long *)(param_1 + 0x30) == 0) {
              iVar3 = FUN_ffff0000907c76f4(&local_60,&local_60,3);
              while (iVar3 == 0) {
                if ((-1 < (short)local_60) ||
                   (iVar3 = FUN_ffff0000907c7d34(&local_60,0), iVar3 == 0))
                goto LAB_ffff0000907cb26c;
                iVar3 = FUN_ffff0000907c8248(&local_60,&local_60,lVar2);
              }
            }
            else {
              iVar3 = FUN_ffff0000907c8248(&local_60,&local_60,param_1 + 0x20);
              while (iVar3 == 0) {
                iVar3 = FUN_ffff0000907c7c44(&local_60,lVar2);
                if (iVar3 < 0) goto LAB_ffff0000907cb26c;
                iVar3 = FUN_ffff0000907c8058(&local_60,&local_60,lVar2);
              }
            }
          }
          goto LAB_ffff0000907cb2f4;
        }
      }
    }
  }
  return -0x4c80;
LAB_ffff0000907cb26c:
  iVar3 = FUN_ffff0000907c846c(&local_60,&local_60,param_2);
  if ((iVar3 == 0) && (iVar3 = FUN_ffff0000907cc6c4(&local_60,param_1), iVar3 == 0)) {
    iVar3 = FUN_ffff0000907c8248(&local_60,&local_60,param_1 + 0x38);
    while (iVar3 == 0) {
      iVar3 = FUN_ffff0000907c7c44(&local_60,lVar2);
      if (iVar3 < 0) {
        iVar4 = FUN_ffff0000907c7c44(&uStack_48,&local_60);
        iVar3 = 0;
        if (iVar4 != 0) {
          iVar3 = -0x4c80;
        }
        break;
      }
      iVar3 = FUN_ffff0000907c8058(&local_60,&local_60,lVar2);
    }
  }
LAB_ffff0000907cb2f4:
  FUN_ffff0000907c7058(&uStack_48);
  FUN_ffff0000907c7058(&local_60);
  return iVar3;
}



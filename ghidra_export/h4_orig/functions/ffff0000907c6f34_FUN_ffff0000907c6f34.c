// ffff0000907c6f34  size=268  FUN_ffff0000907c6f34

int FUN_ffff0000907c6f34
              (long param_1,undefined8 param_2,undefined8 param_3,long param_4,long param_5)

{
  int iVar1;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  long local_40;
  long local_38;
  
  local_40 = 0;
  param_5 = param_4 + param_5;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_38 = param_4;
  FUN_ffff0000907c7040(&uStack_58);
  FUN_ffff0000907c7040(&local_70);
  iVar1 = FUN_ffff0000907ccbc4(&local_38,param_5,&local_40,0x30);
  if (iVar1 == 0) {
    if (local_38 + local_40 != param_5) {
      iVar1 = -0x4fe6;
      goto LAB_ffff0000907c6fb4;
    }
    iVar1 = FUN_ffff0000907ccc08(&local_38,param_5,&uStack_58);
    if ((iVar1 == 0) && (iVar1 = FUN_ffff0000907ccc08(&local_38,param_5,&local_70), iVar1 == 0)) {
      iVar1 = FUN_ffff0000907c6ca4(param_1,param_2,param_3,param_1 + 0x110,&uStack_58,&local_70);
      if ((iVar1 == 0) && (iVar1 = 0, local_38 != param_5)) {
        iVar1 = -0x4c00;
      }
      goto LAB_ffff0000907c6fb4;
    }
  }
  iVar1 = iVar1 + -0x4f80;
LAB_ffff0000907c6fb4:
  FUN_ffff0000907c7058(&uStack_58);
  FUN_ffff0000907c7058(&local_70);
  return iVar1;
}



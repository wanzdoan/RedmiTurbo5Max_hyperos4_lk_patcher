
int FUN_ffff0000be09bbd8
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
  FUN_ffff0000be09bcf0(&uStack_58);
  FUN_ffff0000be09bcf0(&local_70);
  iVar1 = FUN_ffff0000be0a1874(&local_38,param_5,&local_40,0x30);
  if (iVar1 == 0) {
    if (local_38 + local_40 != param_5) {
      iVar1 = -0x4fe6;
      goto LAB_ffff0000be09bc64;
    }
    iVar1 = FUN_ffff0000be0a18b8(&local_38,param_5,&uStack_58);
    if ((iVar1 == 0) && (iVar1 = FUN_ffff0000be0a18b8(&local_38,param_5,&local_70), iVar1 == 0)) {
      iVar1 = FUN_ffff0000be09b954(param_1,param_2,param_3,param_1 + 0x110,&uStack_58,&local_70);
      if ((iVar1 == 0) && (iVar1 = 0, local_38 != param_5)) {
        iVar1 = -0x4c00;
      }
      goto LAB_ffff0000be09bc64;
    }
  }
  iVar1 = iVar1 + -0x4f80;
LAB_ffff0000be09bc64:
  FUN_ffff0000be09bd08(&uStack_58);
  FUN_ffff0000be09bd08(&local_70);
  return iVar1;
}



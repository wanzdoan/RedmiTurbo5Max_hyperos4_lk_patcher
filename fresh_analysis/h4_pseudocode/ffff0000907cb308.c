
int FUN_ffff0000907cb308
              (long param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
              undefined8 param_5,undefined8 param_6,undefined8 param_7)

{
  int iVar1;
  undefined8 local_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  if ((*(long *)(param_1 + 0x60) == 0) || (*(long *)(param_1 + 0x78) == 0)) {
    iVar1 = -0x4e80;
  }
  else {
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    local_90 = 0;
    uStack_88 = 0;
    local_a0 = 0;
    uStack_98 = 0;
    local_a8 = 0;
    FUN_ffff0000907c7040(&local_a8);
    FUN_ffff0000907c7040(&local_90);
    FUN_ffff0000907c7040(&uStack_78);
    iVar1 = FUN_ffff0000907cb444(param_1,&local_a8,param_3,param_4,param_7);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000907cb444(param_1,param_2,param_5,param_6,param_7);
      if (iVar1 == 0) {
        iVar1 = FUN_ffff0000907cb568(param_1,param_2,&local_a8,param_2);
        if (iVar1 == 0) {
          iVar1 = FUN_ffff0000907cbba8(param_1,param_2);
        }
      }
    }
    FUN_ffff0000907c7058(&local_a8);
    FUN_ffff0000907c7058(&local_90);
    FUN_ffff0000907c7058(&uStack_78);
  }
  return iVar1;
}



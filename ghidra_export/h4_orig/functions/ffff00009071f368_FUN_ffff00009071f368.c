// ffff00009071f368  size=412  FUN_ffff00009071f368

undefined4 FUN_ffff00009071f368(long param_1,long param_2,undefined8 param_3)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  uint *puVar5;
  undefined4 uVar6;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  int local_24;
  
  local_24 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  if (param_1 == 0) {
    iVar4 = 2;
    goto LAB_ffff00009071f4a0;
  }
  iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_battery_manager_ffff0000907f1007);
  if (-1 < iVar2) {
    FUN_ffff00009077f6fc(&local_80,0x40,s_gauge_d_ffff0000907e0dea,1);
    puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,&local_80,&local_24);
    iVar4 = 2;
    if (((0 < local_24) && (puVar5 != (uint *)0x0)) &&
       (uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8,
       iVar3 = FUN_ffff00009076e044(param_1,uVar1 >> 0x10 | uVar1 << 0x10), -1 < iVar3)) {
      iVar4 = FUN_ffff00009076e210(param_1,iVar3,param_3);
      if (iVar4 == 0) goto LAB_ffff00009071f4c4;
    }
    FUN_ffff00009077f6fc(&local_80,0x40,s_gauge_d_ffff0000907e0dea,2);
    puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,&local_80,&local_24);
    if (((0 < local_24) && (puVar5 != (uint *)0x0)) &&
       (uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8,
       iVar2 = FUN_ffff00009076e044(param_1,uVar1 >> 0x10 | uVar1 << 0x10), -1 < iVar2)) {
      iVar4 = FUN_ffff00009076e210(param_1,iVar2,param_3);
      if (iVar4 == 0) {
        uVar6 = 1;
        goto LAB_ffff00009071f4c8;
      }
    }
LAB_ffff00009071f4a0:
    FUN_ffff00009077e11c
              (s__s__can_not_find__s_index__d_ffff0000907ea9a0,s_check_gauge_number_ffff0000907fcf1d
               ,param_3,iVar4);
    return 2;
  }
LAB_ffff00009071f4c4:
  uVar6 = 0;
LAB_ffff00009071f4c8:
  FUN_ffff00009077e11c
            (s__s___s_check_gauge_number__d_ffff0000907da7ef,s_check_gauge_number_ffff0000907fcf1d,
             *(undefined8 *)(param_2 + 0x10),uVar6);
  return uVar6;
}



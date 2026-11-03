
undefined4 FUN_ffff0000be01dda0(long param_1,long param_2,undefined8 param_3)

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
    goto LAB_ffff0000be01ded8;
  }
  iVar2 = FUN_ffff0000be056824(param_1,0xffffffff,s_mediatek_battery_manager_ffff0000be0bac78);
  if (-1 < iVar2) {
    FUN_ffff0000be0611dc(&local_80,0x40,s_gauge_d_ffff0000be0af681,1);
    puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar2,&local_80,&local_24);
    iVar4 = 2;
    if (((0 < local_24) && (puVar5 != (uint *)0x0)) &&
       (uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8,
       iVar3 = FUN_ffff0000be05653c(param_1,uVar1 >> 0x10 | uVar1 << 0x10), -1 < iVar3)) {
      iVar4 = FUN_ffff0000be056708(param_1,iVar3,param_3);
      if (iVar4 == 0) goto LAB_ffff0000be01defc;
    }
    FUN_ffff0000be0611dc(&local_80,0x40,s_gauge_d_ffff0000be0af681,2);
    puVar5 = (uint *)FUN_ffff0000be056494(param_1,iVar2,&local_80,&local_24);
    if (((0 < local_24) && (puVar5 != (uint *)0x0)) &&
       (uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8,
       iVar2 = FUN_ffff0000be05653c(param_1,uVar1 >> 0x10 | uVar1 << 0x10), -1 < iVar2)) {
      iVar4 = FUN_ffff0000be056708(param_1,iVar2,param_3);
      if (iVar4 == 0) {
        uVar6 = 1;
        goto LAB_ffff0000be01df00;
      }
    }
LAB_ffff0000be01ded8:
    FUN_ffff0000be05fd98
              (s__s__can_not_find__s_index__d_ffff0000be0b62eb,s_check_gauge_number_ffff0000be0c2aab
               ,param_3,iVar4);
    return 2;
  }
LAB_ffff0000be01defc:
  uVar6 = 0;
LAB_ffff0000be01df00:
  FUN_ffff0000be05fd98
            (s__s___s_check_gauge_number__d_ffff0000be0ab40f,s_check_gauge_number_ffff0000be0c2aab,
             *(undefined8 *)(param_2 + 0x10),uVar6);
  return uVar6;
}



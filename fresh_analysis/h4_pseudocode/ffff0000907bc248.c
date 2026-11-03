
long FUN_ffff0000907bc248
               (long param_1,undefined8 param_2,undefined8 param_3,ulong param_4,ulong *param_5)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  ulong uVar5;
  long lVar6;
  undefined *puVar7;
  char *pcVar8;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined5 uStack_80;
  undefined3 local_7b;
  undefined5 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  
  local_60 = 0;
  uStack_58 = 0;
  pcVar8 = &DAT_ffff0000907d0a72;
  if ((param_4 & 1) == 0) {
    pcVar8 = s_vbmeta_ffff0000907da540;
  }
  local_70 = 0;
  uStack_68 = 0;
  uStack_78 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_7b = 0;
  local_98 = 0;
  uStack_90 = 0;
  lVar2 = FUN_ffff0000907c175c(param_2,s___ANDROID_SYSTEM_PARTUUID__ffff0000907e5941);
  if (lVar2 != 0) {
    uVar3 = thunk_FUN_ffff00009077f250(s_system_ffff0000907edad2);
    uVar4 = thunk_FUN_ffff00009077f250(param_3);
    uVar5 = FUN_ffff0000907c1384(&local_70,0x20,s_system_ffff0000907edad2,uVar3,param_3,uVar4);
    if ((uVar5 & 1) == 0) {
      lVar2 = 0;
LAB_ffff0000907bc3e0:
      uVar3 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_cmdline__ffff0000907d231f);
      puVar7 = &DAT_ffff0000907da2d9;
      pcVar8 = s_Partition_name_and_suffix_does_n_ffff0000908035e2;
    }
    else {
      iVar1 = (**(code **)(param_1 + 0x50))(param_1,&local_70,&local_98,0x25);
      if (iVar1 == 0) {
        lVar2 = FUN_ffff0000907c183c
                          (param_2,s___ANDROID_SYSTEM_PARTUUID__ffff0000907e5941,&local_98);
        if (lVar2 == 0) {
          return 0;
        }
        goto LAB_ffff0000907bc324;
      }
      if (iVar1 == 1) {
        return 0;
      }
      lVar2 = 0;
LAB_ffff0000907bc4f0:
      uVar3 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_cmdline__ffff0000907d231f);
      puVar7 = &DAT_ffff0000907da2dc;
      pcVar8 = s_Error_getting_unique_GUID_for_pa_ffff0000907fe6a3;
    }
    FUN_ffff0000907c0ee8(uVar3,&DAT_ffff0000907ebfa6,puVar7,s___ERROR__ffff0000907e07dc,pcVar8,0);
LAB_ffff0000907bc524:
    if (lVar2 != 0) {
      FUN_ffff0000907c1014(lVar2);
    }
    return 0;
  }
  lVar2 = 0;
LAB_ffff0000907bc324:
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  uStack_78 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_7b = 0;
  local_98 = 0;
  uStack_90 = 0;
  lVar6 = FUN_ffff0000907c175c(param_2,s___ANDROID_BOOT_PARTUUID__ffff0000907f917d);
  if (lVar6 != 0) {
    uVar3 = thunk_FUN_ffff00009077f250(&DAT_ffff0000907d0a72);
    uVar4 = thunk_FUN_ffff00009077f250(param_3);
    uVar5 = FUN_ffff0000907c1384(&local_70,0x20,&DAT_ffff0000907d0a72,uVar3,param_3,uVar4);
    if ((uVar5 & 1) == 0) goto LAB_ffff0000907bc3e0;
    iVar1 = (**(code **)(param_1 + 0x50))(param_1,&local_70,&local_98,0x25);
    if (iVar1 == 1) goto LAB_ffff0000907bc524;
    if (iVar1 != 0) goto LAB_ffff0000907bc4f0;
    if (lVar2 == 0) {
      lVar2 = FUN_ffff0000907c183c(param_2,s___ANDROID_BOOT_PARTUUID__ffff0000907f917d,&local_98);
    }
    else {
      lVar6 = FUN_ffff0000907c183c(lVar2,s___ANDROID_BOOT_PARTUUID__ffff0000907f917d,&local_98);
      FUN_ffff0000907c1014(lVar2);
      lVar2 = lVar6;
    }
    if (lVar2 == 0) {
      return 0;
    }
  }
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  uStack_78 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_7b = 0;
  local_98 = 0;
  uStack_90 = 0;
  lVar6 = FUN_ffff0000907c175c(param_2,s___ANDROID_VBMETA_PARTUUID__ffff0000907da2be);
  if (lVar6 == 0) {
    if (lVar2 != 0) goto LAB_ffff0000907bc580;
    lVar2 = FUN_ffff0000907c1690(param_2);
  }
  else {
    uVar3 = thunk_FUN_ffff00009077f250(pcVar8);
    uVar4 = thunk_FUN_ffff00009077f250(param_3);
    uVar5 = FUN_ffff0000907c1384(&local_70,0x20,pcVar8,uVar3,param_3,uVar4);
    if ((uVar5 & 1) == 0) goto LAB_ffff0000907bc3e0;
    iVar1 = (**(code **)(param_1 + 0x50))(param_1,&local_70,&local_98,0x25);
    if (iVar1 == 1) goto LAB_ffff0000907bc524;
    if (iVar1 != 0) goto LAB_ffff0000907bc4f0;
    if (lVar2 == 0) {
      lVar2 = FUN_ffff0000907c183c(param_2,s___ANDROID_VBMETA_PARTUUID__ffff0000907da2be,&local_98);
    }
    else {
      lVar6 = FUN_ffff0000907c183c(lVar2,s___ANDROID_VBMETA_PARTUUID__ffff0000907da2be,&local_98);
      FUN_ffff0000907c1014(lVar2);
      lVar2 = lVar6;
    }
  }
  if (lVar2 == 0) {
    return 0;
  }
LAB_ffff0000907bc580:
  if (param_5 == (ulong *)0x0) {
    return lVar2;
  }
  if (*param_5 != 0) {
    uVar5 = 0;
    do {
      lVar6 = FUN_ffff0000907c183c(lVar2,param_5[uVar5 + 1],param_5[uVar5 + 0xb]);
      FUN_ffff0000907c1014(lVar2);
      if (lVar6 == 0) {
        return 0;
      }
      uVar5 = uVar5 + 1;
      lVar2 = lVar6;
    } while (uVar5 < *param_5);
    return lVar6;
  }
  return lVar2;
}



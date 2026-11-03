// ffff0000907bbbd4  size=908  FUN_ffff0000907bbbd4

long FUN_ffff0000907bbbd4
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
  pcVar8 = &DAT_ffff0000907d0a5b;
  if ((param_4 & 1) == 0) {
    pcVar8 = s_vbmeta_ffff0000907da492;
  }
  local_70 = 0;
  uStack_68 = 0;
  uStack_78 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_7b = 0;
  local_98 = 0;
  uStack_90 = 0;
  lVar2 = FUN_ffff0000907c10e8(param_2,s___ANDROID_SYSTEM_PARTUUID__ffff0000907e5826);
  if (lVar2 != 0) {
    uVar3 = thunk_FUN_ffff00009077ef88(s_system_ffff0000907ed94a);
    uVar4 = thunk_FUN_ffff00009077ef88(param_3);
    uVar5 = FUN_ffff0000907c0d10(&local_70,0x20,s_system_ffff0000907ed94a,uVar3,param_3,uVar4);
    if ((uVar5 & 1) == 0) {
      lVar2 = 0;
LAB_ffff0000907bbd6c:
      uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_cmdline__ffff0000907d22e2);
      puVar7 = &DAT_ffff0000907da22b;
      pcVar8 = s_Partition_name_and_suffix_does_n_ffff0000908033ba;
    }
    else {
      iVar1 = (**(code **)(param_1 + 0x50))(param_1,&local_70,&local_98,0x25);
      if (iVar1 == 0) {
        lVar2 = FUN_ffff0000907c11c8
                          (param_2,s___ANDROID_SYSTEM_PARTUUID__ffff0000907e5826,&local_98);
        if (lVar2 == 0) {
          return 0;
        }
        goto LAB_ffff0000907bbcb0;
      }
      if (iVar1 == 1) {
        return 0;
      }
      lVar2 = 0;
LAB_ffff0000907bbe7c:
      uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_cmdline__ffff0000907d22e2);
      puVar7 = &DAT_ffff0000907da22e;
      pcVar8 = s_Error_getting_unique_GUID_for_pa_ffff0000907fe4fb;
    }
    FUN_ffff0000907c0874(uVar3,&DAT_ffff0000907ebe0c,puVar7,s___ERROR__ffff0000907e0722,pcVar8,0);
LAB_ffff0000907bbeb0:
    if (lVar2 != 0) {
      FUN_ffff0000907c09a0(lVar2);
    }
    return 0;
  }
  lVar2 = 0;
LAB_ffff0000907bbcb0:
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
  lVar6 = FUN_ffff0000907c10e8(param_2,s___ANDROID_BOOT_PARTUUID__ffff0000907f8fe1);
  if (lVar6 != 0) {
    uVar3 = thunk_FUN_ffff00009077ef88(&DAT_ffff0000907d0a5b);
    uVar4 = thunk_FUN_ffff00009077ef88(param_3);
    uVar5 = FUN_ffff0000907c0d10(&local_70,0x20,&DAT_ffff0000907d0a5b,uVar3,param_3,uVar4);
    if ((uVar5 & 1) == 0) goto LAB_ffff0000907bbd6c;
    iVar1 = (**(code **)(param_1 + 0x50))(param_1,&local_70,&local_98,0x25);
    if (iVar1 == 1) goto LAB_ffff0000907bbeb0;
    if (iVar1 != 0) goto LAB_ffff0000907bbe7c;
    if (lVar2 == 0) {
      lVar2 = FUN_ffff0000907c11c8(param_2,s___ANDROID_BOOT_PARTUUID__ffff0000907f8fe1,&local_98);
    }
    else {
      lVar6 = FUN_ffff0000907c11c8(lVar2,s___ANDROID_BOOT_PARTUUID__ffff0000907f8fe1,&local_98);
      FUN_ffff0000907c09a0(lVar2);
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
  lVar6 = FUN_ffff0000907c10e8(param_2,s___ANDROID_VBMETA_PARTUUID__ffff0000907da210);
  if (lVar6 == 0) {
    if (lVar2 != 0) goto LAB_ffff0000907bbf0c;
    lVar2 = FUN_ffff0000907c101c(param_2);
  }
  else {
    uVar3 = thunk_FUN_ffff00009077ef88(pcVar8);
    uVar4 = thunk_FUN_ffff00009077ef88(param_3);
    uVar5 = FUN_ffff0000907c0d10(&local_70,0x20,pcVar8,uVar3,param_3,uVar4);
    if ((uVar5 & 1) == 0) goto LAB_ffff0000907bbd6c;
    iVar1 = (**(code **)(param_1 + 0x50))(param_1,&local_70,&local_98,0x25);
    if (iVar1 == 1) goto LAB_ffff0000907bbeb0;
    if (iVar1 != 0) goto LAB_ffff0000907bbe7c;
    if (lVar2 == 0) {
      lVar2 = FUN_ffff0000907c11c8(param_2,s___ANDROID_VBMETA_PARTUUID__ffff0000907da210,&local_98);
    }
    else {
      lVar6 = FUN_ffff0000907c11c8(lVar2,s___ANDROID_VBMETA_PARTUUID__ffff0000907da210,&local_98);
      FUN_ffff0000907c09a0(lVar2);
      lVar2 = lVar6;
    }
  }
  if (lVar2 == 0) {
    return 0;
  }
LAB_ffff0000907bbf0c:
  if (param_5 == (ulong *)0x0) {
    return lVar2;
  }
  if (*param_5 != 0) {
    uVar5 = 0;
    do {
      lVar6 = FUN_ffff0000907c11c8(lVar2,param_5[uVar5 + 1],param_5[uVar5 + 0xb]);
      FUN_ffff0000907c09a0(lVar2);
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



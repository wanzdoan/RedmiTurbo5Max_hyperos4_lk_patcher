// ffff0000907bc5d4  size=1376  FUN_ffff0000907bc5d4

undefined4
FUN_ffff0000907bc5d4
          (long param_1,uint param_2,long param_3,long param_4,long param_5,long param_6,
          uint param_7,int param_8,undefined4 param_9)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  long lVar4;
  char *pcVar5;
  ulong uVar6;
  long *plVar7;
  ulong uVar8;
  ulong uVar9;
  char *pcVar10;
  undefined8 local_178;
  undefined8 uStack_170;
  undefined8 local_168;
  undefined8 uStack_160;
  undefined8 local_158;
  undefined8 uStack_150;
  undefined8 local_148;
  undefined8 uStack_140;
  ulong local_138;
  undefined8 uStack_130;
  undefined8 local_128;
  undefined8 uStack_120;
  undefined8 local_118;
  undefined8 uStack_110;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  char local_a4 [4];
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  local_a4[0] = '\0';
  if ((param_2 >> 2 & 1) == 0) {
    iVar1 = FUN_ffff0000907bcb34
                      (param_3,s_androidboot_vbmeta_device_ffff0000907e7161,
                       s_PARTUUID___ANDROID_VBMETA_PARTUU_ffff0000907e595c);
    if (iVar1 == 0) {
      return 1;
    }
    if ((param_5 != 0) && (param_6 != 0)) {
      local_b8 = 0;
      uStack_b0 = 0;
      local_c8 = 0;
      uStack_c0 = 0;
      local_d8 = 0;
      uStack_d0 = 0;
      local_e8 = 0;
      uStack_e0 = 0;
      local_f8 = 0;
      uStack_f0 = 0;
      local_108 = 0;
      uStack_100 = 0;
      local_118 = 0;
      uStack_110 = 0;
      local_128 = 0;
      uStack_120 = 0;
      local_138 = 0;
      uStack_130 = 0;
      local_148 = 0;
      uStack_140 = 0;
      local_158 = 0;
      uStack_150 = 0;
      local_168 = 0;
      uStack_160 = 0;
      local_178 = 0;
      uStack_170 = 0;
      FUN_ffff0000907c6bd8(&local_178);
      FUN_ffff0000907c6c10(&local_178,param_5,param_6);
      uVar2 = FUN_ffff0000907c6c54(&local_178);
      lVar3 = FUN_ffff0000907c1ea0(uVar2,0x20);
      if (lVar3 == 0) {
        return 1;
      }
      iVar1 = FUN_ffff0000907bcb34
                        (param_3,s_androidboot_vbmeta_public_key_di_ffff0000907f3cdf,lVar3);
      FUN_ffff0000907c1014(lVar3);
      if (iVar1 == 0) {
        return 1;
      }
    }
  }
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_138 = local_138 & 0xffffffffffffff00;
  local_148 = 0;
  uStack_140 = 0;
  local_158 = 0;
  uStack_150 = 0;
  local_168 = 0;
  uStack_160 = 0;
  local_178 = 0;
  uStack_170 = 0;
  lVar3 = FUN_ffff0000907c1f98(1,&local_80);
  lVar4 = FUN_ffff0000907c1f98(2,&local_a0);
  thunk_FUN_ffff00009077eb20(&local_178,&local_80,lVar3);
  *(undefined1 *)((long)&local_178 + lVar3) = 0x2e;
  thunk_FUN_ffff00009077eb20((long)&local_178 + lVar3 + 1,&local_a0,lVar4);
  *(undefined1 *)((long)&local_178 + lVar3 + lVar4 + 1) = 0;
  iVar1 = FUN_ffff0000907bcb34(param_3,s_androidboot_vbmeta_avb_version_ffff0000907dd6d2,&local_178)
  ;
  if (iVar1 == 0) {
    return 1;
  }
  iVar1 = (**(code **)(param_1 + 0x48))(param_1,local_a4);
  if (iVar1 != 0) {
    if (iVar1 == 1) {
      return 1;
    }
    uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_cmdline__ffff0000907d231f);
    FUN_ffff0000907c0ee8
              (uVar2,&DAT_ffff0000907ebfa6,&DAT_ffff0000907d2341,s___ERROR__ffff0000907e07dc,
               s_Error_getting_device_state__ffff0000907f76ab,0);
    return 2;
  }
  pcVar5 = s_locked_ffff0000907e8aa1;
  if (local_a4[0] != '\0') {
    pcVar5 = s_unlocked_ffff0000907f5cb2;
  }
  iVar1 = FUN_ffff0000907bcb34(param_3,s_androidboot_vbmeta_device_state_ffff0000907f76c8,pcVar5);
  if (iVar1 == 0) {
    return 1;
  }
  if (param_7 < 4) {
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    FUN_ffff0000907c0070(param_3,0,&local_80);
    uVar6 = *(ulong *)(param_3 + 0x10);
    if (uVar6 == 0) {
      lVar3 = 0;
    }
    else {
      if (uVar6 == 1) {
        lVar3 = 0;
        uVar8 = 0;
      }
      else {
        uVar8 = uVar6 & 0xfffffffffffffffe;
        lVar4 = 0;
        lVar3 = 0;
        plVar7 = (long *)(*(long *)(param_3 + 8) + 0x30);
        uVar9 = uVar8;
        do {
          uVar9 = uVar9 - 2;
          lVar4 = plVar7[-4] + lVar4;
          lVar3 = *plVar7 + lVar3;
          plVar7 = plVar7 + 8;
        } while (uVar9 != 0);
        lVar3 = lVar3 + lVar4;
        if (uVar6 == uVar8) goto LAB_ffff0000907bc8d0;
      }
      lVar4 = uVar6 - uVar8;
      plVar7 = (long *)(*(long *)(param_3 + 8) + uVar8 * 0x20 + 0x10);
      do {
        lVar4 = lVar4 + -1;
        lVar3 = *plVar7 + lVar3;
        plVar7 = plVar7 + 4;
      } while (lVar4 != 0);
    }
LAB_ffff0000907bc8d0:
    iVar1 = FUN_ffff0000907bcb34
                      (param_3,s_androidboot_vbmeta_hash_alg_ffff0000907e9e75,
                       s_sha256_ffff0000908019f2);
    if (iVar1 == 0) {
      return 1;
    }
    local_168 = 0;
    uStack_160 = 0;
    local_178 = 0;
    uStack_170 = 0;
    FUN_ffff0000907c1f98(lVar3,&local_178);
    iVar1 = FUN_ffff0000907bcb34(param_3,s_androidboot_vbmeta_size_ffff0000907fe15c,&local_178);
    if (iVar1 == 0) {
      return 1;
    }
    iVar1 = FUN_ffff0000907bcc34
                      (param_3,s_androidboot_vbmeta_digest_ffff0000907e9e91,&local_80,0x20);
  }
  else {
    if (2 < param_7 - 4) goto LAB_ffff0000907bc9f8;
    local_148 = 0;
    uStack_140 = 0;
    local_158 = 0;
    uStack_150 = 0;
    local_168 = 0;
    uStack_160 = 0;
    local_178 = 0;
    uStack_170 = 0;
    FUN_ffff0000907c0070(param_3,1,&local_178);
    uVar6 = *(ulong *)(param_3 + 0x10);
    if (uVar6 == 0) {
      lVar3 = 0;
    }
    else {
      if (uVar6 == 1) {
        lVar3 = 0;
        uVar8 = 0;
      }
      else {
        uVar8 = uVar6 & 0xfffffffffffffffe;
        lVar4 = 0;
        lVar3 = 0;
        plVar7 = (long *)(*(long *)(param_3 + 8) + 0x30);
        uVar9 = uVar8;
        do {
          uVar9 = uVar9 - 2;
          lVar4 = plVar7[-4] + lVar4;
          lVar3 = *plVar7 + lVar3;
          plVar7 = plVar7 + 8;
        } while (uVar9 != 0);
        lVar3 = lVar3 + lVar4;
        if (uVar6 == uVar8) goto LAB_ffff0000907bc994;
      }
      lVar4 = uVar6 - uVar8;
      plVar7 = (long *)(*(long *)(param_3 + 8) + uVar8 * 0x20 + 0x10);
      do {
        lVar4 = lVar4 + -1;
        lVar3 = *plVar7 + lVar3;
        plVar7 = plVar7 + 4;
      } while (lVar4 != 0);
    }
LAB_ffff0000907bc994:
    iVar1 = FUN_ffff0000907bcb34
                      (param_3,s_androidboot_vbmeta_hash_alg_ffff0000907e9e75,
                       s_sha512_ffff0000907f3d04);
    if (iVar1 == 0) {
      return 1;
    }
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    FUN_ffff0000907c1f98(lVar3,&local_80);
    iVar1 = FUN_ffff0000907bcb34(param_3,s_androidboot_vbmeta_size_ffff0000907fe15c,&local_80);
    if (iVar1 == 0) {
      return 1;
    }
    iVar1 = FUN_ffff0000907bcc34
                      (param_3,s_androidboot_vbmeta_digest_ffff0000907e9e91,&local_178,0x40);
  }
  if (iVar1 == 0) {
    return 1;
  }
LAB_ffff0000907bc9f8:
  if ((*(byte *)(param_4 + 0x78) & 1) == 0) {
    switch(param_9) {
    case 0:
      iVar1 = FUN_ffff0000907bcb34
                        (param_3,s_androidboot_vbmeta_invalidate_on_ffff0000907f2394,
                         &DAT_ffff0000907df35d);
      if (iVar1 == 0) {
        return 1;
      }
    case 1:
      pcVar5 = s_restart_on_corruption_ffff0000907fc9b0;
      pcVar10 = s_enforcing_ffff0000907e717b;
      break;
    case 2:
      pcVar5 = s_ignore_zero_blocks_ffff0000907e07e6;
      pcVar10 = (char *)0xffff0000907fe6cd;
      break;
    case 3:
      pcVar5 = s_ignore_corruption_ffff0000907ed854;
      pcVar10 = s_logging_ffff0000907df10e;
      break;
    default:
      pcVar10 = (char *)0x0;
      pcVar5 = (char *)0x0;
    }
    lVar3 = FUN_ffff0000907c183c
                      (*(undefined8 *)(param_3 + 0x28),s___ANDROID_VERITY_MODE__ffff0000907f5abd,
                       pcVar5);
    FUN_ffff0000907c1014(*(undefined8 *)(param_3 + 0x28));
    *(long *)(param_3 + 0x28) = lVar3;
    if (lVar3 == 0) {
      return 1;
    }
  }
  else {
    pcVar10 = s_disabled_ffff0000907f0f7e;
  }
  iVar1 = FUN_ffff0000907bcb34(param_3,s_androidboot_veritymode_ffff0000907dd6f1,pcVar10);
  if ((iVar1 != 0) &&
     ((param_8 != 4 ||
      (iVar1 = FUN_ffff0000907bcb34
                         (param_3,s_androidboot_veritymode_managed_ffff0000907da2df,
                          &DAT_ffff0000907df35d), iVar1 != 0)))) {
    return 0;
  }
  return 1;
}



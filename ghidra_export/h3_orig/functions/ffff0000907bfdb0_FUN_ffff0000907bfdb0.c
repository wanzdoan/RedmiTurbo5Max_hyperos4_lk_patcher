// ffff0000907bfdb0  size=1440  FUN_ffff0000907bfdb0

int FUN_ffff0000907bfdb0
              (long param_1,undefined8 param_2,undefined8 param_3,ulong param_4,long param_5,
              long param_6)

{
  ulong uVar1;
  int iVar2;
  int iVar3;
  ulong uVar4;
  ulong uVar5;
  long lVar6;
  undefined8 uVar7;
  undefined *puVar8;
  char *pcVar9;
  long lVar10;
  undefined8 *puVar11;
  undefined1 auStack_3c0 [400];
  undefined8 local_230;
  undefined8 local_228;
  undefined8 uStack_220;
  undefined8 local_218;
  undefined8 uStack_210;
  undefined8 local_208;
  undefined8 uStack_200;
  undefined8 local_1f8;
  undefined8 uStack_1f0;
  undefined8 local_1e8;
  undefined8 uStack_1e0;
  undefined8 local_1d8;
  undefined8 uStack_1d0;
  undefined8 local_1c8;
  undefined8 uStack_1c0;
  undefined8 local_1b8;
  undefined8 local_1b0;
  undefined8 local_1a8;
  undefined8 local_1a0;
  undefined8 local_198;
  undefined8 local_190;
  undefined8 local_188;
  undefined8 local_180;
  undefined8 local_178;
  undefined8 local_170;
  undefined8 local_168;
  undefined8 local_160;
  undefined8 local_158;
  undefined8 uStack_150;
  undefined8 local_148;
  undefined8 uStack_140;
  undefined8 local_138;
  undefined8 uStack_130;
  undefined8 local_128;
  ulong local_120;
  char local_114 [4];
  long local_110;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  ulong local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  ulong local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined4 local_68;
  
  local_68 = 0;
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  local_b8 = 0;
  local_b0 = 0;
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
  local_110 = 0;
  local_114[0] = '\0';
  local_128 = 0;
  local_120 = 0;
  local_138 = 0;
  uStack_130 = 0;
  local_148 = 0;
  uStack_140 = 0;
  local_158 = 0;
  uStack_150 = 0;
  local_160 = 0;
  uVar4 = FUN_ffff0000907bcd44(param_5,&local_e8);
  if ((uVar4 & 1) == 0) {
    iVar3 = 6;
    goto LAB_ffff0000907bff28;
  }
  uVar4 = local_b0 & 0xffffffff;
  uVar1 = local_b0 >> 0x20;
  param_5 = param_5 + 0x84;
  uVar5 = FUN_ffff0000907c0c7c(param_5,uVar4);
  if ((uVar5 & 1) == 0) {
    uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar8 = &DAT_ffff0000907d3c41;
    pcVar9 = s_Partition_name_is_not_valid_UTF__ffff0000907fe560;
    goto LAB_ffff0000907bff14;
  }
  lVar6 = FUN_ffff0000907c115c(param_2,param_5,local_b0 & 0xffffffff);
  if (lVar6 == 0) {
    iVar3 = 0;
    goto LAB_ffff0000907bff28;
  }
  if ((local_a8 & 0x100000000) == 0) {
    if (((int)local_a8 == 0) && (lVar10 = thunk_FUN_ffff00009077ef88(param_3), lVar10 != 0)) {
      uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      puVar8 = &DAT_ffff0000907fffef;
      pcVar9 = s_Cannot_use_A_B_with_a_persistent_ffff0000907f2250;
    }
    else {
      uVar5 = local_b0 & 0xffffffff;
      uVar7 = thunk_FUN_ffff00009077ef88(param_3);
      uVar5 = FUN_ffff0000907c0d10(&local_108,0x20,param_5,uVar5,param_3,uVar7);
      if ((uVar5 & 1) != 0) goto LAB_ffff0000907bffb8;
      uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      puVar8 = &DAT_ffff0000907d8a07;
      pcVar9 = s_Partition_name_and_suffix_does_n_ffff0000908033ba;
    }
  }
  else {
    if ((uint)local_b0 < 0x20) {
      thunk_FUN_ffff00009077e858(&local_108,param_5);
      *(undefined1 *)((long)&local_108 + (local_b0 & 0xffffffff)) = 0;
LAB_ffff0000907bffb8:
      local_120 = local_d8;
      if ((param_4 & 1) != 0) {
        iVar2 = (**(code **)(param_1 + 0x58))(param_1,&local_108,&local_120);
        iVar3 = 1;
        if (iVar2 == 1) goto LAB_ffff0000907bff28;
        if (iVar2 != 0) {
          uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
          FUN_ffff0000907c0874
                    (uVar7,&DAT_ffff0000907ebe23,&DAT_ffff0000907e596b,s___ERROR__ffff0000907e0739,
                     &local_108,s___Error_determining_partition_si_ffff0000907facab,0);
          iVar3 = 2;
          goto LAB_ffff0000907bff28;
        }
      }
      iVar3 = FUN_ffff0000907c068c(param_1,&local_108,local_120,&local_110,local_114);
      if (iVar3 == 0) {
        local_168 = 0;
        local_170 = 0;
        param_5 = param_5 + uVar4;
        local_178 = 0;
        local_180 = 0;
        local_188 = 0;
        local_190 = 0;
        local_198 = 0;
        local_1a0 = 0;
        local_1a8 = 0;
        local_1b0 = 0;
        local_1b8 = 0;
        local_1c8 = 0;
        uStack_1c0 = 0;
        local_1d8 = 0;
        uStack_1d0 = 0;
        local_1e8 = 0;
        uStack_1e0 = 0;
        local_1f8 = 0;
        uStack_1f0 = 0;
        local_208 = 0;
        uStack_200 = 0;
        local_218 = 0;
        uStack_210 = 0;
        local_228 = 0;
        uStack_220 = 0;
        local_230 = 0;
        FUN_ffff00009077eb88(auStack_3c0,0,400);
        uVar4 = local_d8;
        if (local_120 <= local_d8) {
          uVar4 = local_120;
        }
        iVar3 = thunk_FUN_ffff00009077ede4(&uStack_d0,s_sha256_ffff000090801816);
        if (iVar3 != 0) {
          iVar3 = thunk_FUN_ffff00009077ede4(&uStack_d0,s_sha512_ffff0000907f3b78);
          if (iVar3 == 0) {
            FUN_ffff0000907bdc70(auStack_3c0);
            FUN_ffff0000907bdd10(auStack_3c0,param_5,local_b0._4_4_);
            FUN_ffff0000907bdd10(auStack_3c0,local_110,uVar4);
            uVar7 = FUN_ffff0000907bdfc4(auStack_3c0);
            uVar4 = 0x40;
            goto LAB_ffff0000907c0268;
          }
          uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
          puVar8 = &DAT_ffff0000907ebb50;
          pcVar9 = s___Unsupported_hash_algorithm__ffff0000907d06f3;
LAB_ffff0000907c032c:
          FUN_ffff0000907c0874
                    (uVar7,&DAT_ffff0000907ebe23,puVar8,s___ERROR__ffff0000907e0739,&local_108,
                     pcVar9,0);
          iVar3 = 6;
          goto LAB_ffff0000907bff28;
        }
        FUN_ffff0000907c6564(&local_230);
        FUN_ffff0000907c659c(&local_230,param_5,local_b0._4_4_);
        FUN_ffff0000907c659c(&local_230,local_110,uVar4);
        uVar7 = FUN_ffff0000907c65e0(&local_230);
        uVar4 = 0x20;
LAB_ffff0000907c0268:
        if ((int)local_a8 == 0) {
          puVar11 = &local_160;
          iVar3 = FUN_ffff0000907c0350(param_1,&local_108,uVar4,uVar7,&local_160);
          if (iVar3 != 0) goto LAB_ffff0000907c0008;
        }
        else {
          puVar11 = (undefined8 *)(param_5 + uVar1);
          if (uVar4 != (local_a8 & 0xffffffff)) {
            uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
            puVar8 = &DAT_ffff0000907ea360;
            pcVar9 = s___Digest_in_descriptor_not_of_ex_ffff0000907e22d7;
            goto LAB_ffff0000907c032c;
          }
        }
        iVar3 = FUN_ffff0000907c0a0c(uVar7,puVar11,uVar4);
        if (iVar3 == 0) {
          iVar3 = 0;
        }
        else {
          uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
          FUN_ffff0000907c0874
                    (uVar7,&DAT_ffff0000907ebe23,&DAT_ffff0000907f76bb,s___ERROR__ffff0000907e0739,
                     &local_108,s___Hash_of_data_does_not_match_di_ffff0000907da284,0);
          iVar3 = 3;
        }
      }
      else {
LAB_ffff0000907c0008:
        if (2 < iVar3 - 3U) goto LAB_ffff0000907bff28;
      }
      if (local_110 != 0) {
        lVar10 = *(long *)(param_6 + 0x20);
        if (lVar10 == 0x20) {
          uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
          FUN_ffff0000907c0874
                    (uVar7,&DAT_ffff0000907ebe23,&DAT_ffff0000907f227a,s___ERROR__ffff0000907e0739,
                     &local_108,s___Too_many_loaded_partitions__ffff0000907d0736,0);
          iVar3 = 1;
        }
        else {
          puVar11 = (undefined8 *)(*(long *)(param_6 + 0x18) + lVar10 * 0x20);
          *(long *)(param_6 + 0x20) = lVar10 + 1;
          uVar7 = FUN_ffff0000907c101c(lVar6);
          *puVar11 = uVar7;
          puVar11[2] = local_120;
          *(char *)(puVar11 + 3) = local_114[0];
          *(int *)((long)puVar11 + 0x1c) = iVar3;
          puVar11[1] = local_110;
          local_110 = 0;
        }
      }
      goto LAB_ffff0000907bff28;
    }
    uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar8 = &DAT_ffff0000907d551f;
    pcVar9 = s_Partition_name_does_not_fit__ffff0000907d54fc;
  }
LAB_ffff0000907bff14:
  FUN_ffff0000907c0874(uVar7,&DAT_ffff0000907ebe23,puVar8,s___ERROR__ffff0000907e0739,pcVar9,0);
  iVar3 = 6;
LAB_ffff0000907bff28:
  if ((local_110 != 0) && (local_114[0] == '\0')) {
    FUN_ffff0000907c09a0();
  }
  return iVar3;
}



// ffff0000907be56c  size=3916  FUN_ffff0000907be56c

/* WARNING: Type propagation algorithm not settling */

int FUN_ffff0000907be56c
              (long param_1,undefined8 param_2,undefined8 param_3,uint param_4,uint param_5,
              ulong param_6,uint param_7,undefined8 param_8,undefined8 param_9,long param_10,
              long param_11,long param_12,undefined4 *param_13,long *param_14,long *param_15,
              long param_16)

{
  int iVar1;
  int iVar2;
  int iVar3;
  undefined4 uVar4;
  undefined8 uVar5;
  ulong uVar6;
  long lVar7;
  undefined8 uVar8;
  ulong uVar9;
  long lVar10;
  long lVar11;
  undefined *puVar12;
  undefined8 *puVar13;
  char *pcVar14;
  long lVar15;
  uint uVar16;
  undefined8 local_2e0;
  undefined8 uStack_2d8;
  undefined8 local_2d0;
  undefined8 uStack_2c8;
  ulong local_2c0;
  undefined8 local_2b8;
  undefined8 local_2b0;
  ulong local_2a8;
  undefined8 local_2a0;
  undefined8 local_298;
  undefined8 local_290;
  undefined8 local_288;
  undefined8 local_280;
  undefined8 local_278;
  undefined8 local_270;
  ulong local_268;
  undefined8 local_260;
  ulong local_258;
  ulong local_250;
  undefined8 uStack_248;
  undefined8 local_240;
  undefined8 uStack_238;
  undefined8 local_230;
  undefined8 uStack_228;
  undefined8 local_220;
  undefined8 uStack_218;
  undefined4 local_210;
  undefined8 local_208;
  undefined8 uStack_200;
  uint local_1f4;
  undefined8 local_1f0;
  undefined8 uStack_1e8;
  undefined4 local_1e0;
  undefined4 uStack_1dc;
  undefined4 uStack_1d8;
  undefined4 uStack_1d4;
  undefined4 local_1d0;
  undefined4 uStack_1cc;
  undefined8 uStack_1c8;
  undefined8 local_1c0;
  undefined8 uStack_1b8;
  ulong local_1b0;
  ulong local_1a8;
  undefined8 local_1a0;
  undefined4 uStack_198;
  undefined4 uStack_194;
  undefined4 local_190;
  undefined4 uStack_18c;
  undefined4 uStack_188;
  undefined4 local_184;
  undefined8 local_180;
  undefined8 uStack_178;
  undefined8 local_170;
  undefined8 local_168;
  undefined8 local_160;
  undefined8 local_158;
  long local_150;
  long local_148;
  undefined8 local_140;
  undefined8 local_138;
  ulong local_130;
  undefined8 local_128;
  undefined8 local_120;
  undefined8 local_118;
  undefined8 local_110;
  undefined8 local_108;
  undefined8 local_100;
  undefined8 local_f8;
  undefined8 local_f0;
  undefined8 local_e8;
  undefined8 local_e0;
  undefined8 local_d8;
  undefined8 local_d0;
  undefined8 local_c8;
  undefined8 local_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  undefined8 local_a8;
  long local_a0;
  long local_98;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  
  uVar5 = thunk_FUN_ffff00009077ef88(s_vbmeta_ffff0000907da492);
  iVar1 = thunk_FUN_ffff00009077f080(param_8,s_vbmeta_ffff0000907da492,uVar5);
  uVar6 = FUN_ffff0000907c0c7c(param_8,param_9);
  if ((uVar6 & 1) == 0) {
    uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar12 = &DAT_ffff0000907e7051;
    pcVar14 = s_Partition_name_is_not_valid_UTF__ffff0000907fe560;
LAB_ffff0000907be78c:
    FUN_ffff0000907c0874(uVar5,&DAT_ffff0000907ebe0c,puVar12,s___ERROR__ffff0000907e0722,pcVar14,0);
    return 6;
  }
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  local_90 = 0;
  local_a0 = 0;
  local_1a0 = 0;
  uStack_198 = 0;
  uStack_194 = 0;
  local_190 = 0;
  uStack_18c = 0;
  uStack_188 = 0;
  local_184 = 0;
  local_180 = 0;
  uStack_178 = 0;
  local_170 = 0;
  local_168 = 0;
  local_160 = 0;
  local_158 = 0;
  local_150 = 0;
  local_148 = 0;
  local_140 = 0;
  local_138 = 0;
  local_130 = 0;
  local_128 = 0;
  local_120 = 0;
  local_118 = 0;
  local_110 = 0;
  local_108 = 0;
  local_100 = 0;
  local_f8 = 0;
  local_f0 = 0;
  local_e8 = 0;
  local_e0 = 0;
  local_d8 = 0;
  local_d0 = 0;
  local_c8 = 0;
  local_c0 = 0;
  local_b8 = 0;
  local_b0 = 0;
  local_a8 = 0;
  local_1b0 = 0;
  local_1a8 = 0;
  uVar5 = thunk_FUN_ffff00009077ef88(param_3);
  uVar6 = FUN_ffff0000907c0d10(&local_88,0x20,param_8,param_9,param_3,uVar5);
  if ((uVar6 & 1) == 0) {
    uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar12 = &DAT_ffff0000907e895a;
    pcVar14 = s_Partition_name_and_suffix_does_n_ffff0000908033ba;
    goto LAB_ffff0000907be78c;
  }
  if (iVar1 == 0) {
LAB_ffff0000907be818:
    uVar5 = 0;
    uVar6 = 0x10000;
  }
  else {
    local_290 = 0;
    local_288 = 0;
    local_2a0 = 0;
    local_298 = 0;
    local_2b0 = 0;
    local_2a8 = 0;
    local_2c0 = 0;
    local_2b8 = 0;
    local_2e0 = 0;
    local_1c0 = 0;
    uStack_1b8 = 0;
    local_1d0 = 0;
    uStack_1cc = 0;
    uStack_1c8 = 0;
    local_1e0 = 0;
    uStack_1dc = 0;
    uStack_1d8 = 0;
    uStack_1d4 = 0;
    local_1f0 = 0;
    uStack_1e8 = 0;
    iVar2 = (**(code **)(param_1 + 0x18))
                      (param_1,&local_88,0xffffffffffffffc0,0x40,&local_2c0,&local_2e0);
    if (iVar2 != 0) {
      if (iVar2 == 1) {
        return 1;
      }
      uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      FUN_ffff0000907c0874
                (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907fe584,s___ERROR__ffff0000907e0722,
                 &local_88,s___Error_loading_footer__ffff0000907e58e8,0);
      return 2;
    }
    uVar6 = FUN_ffff0000907bcc30(&local_2c0,&local_1f0);
    if ((uVar6 & 1) == 0) goto LAB_ffff0000907be818;
    uVar6 = CONCAT44(local_1d0,uStack_1d4);
    if (0x10000 < uVar6) {
      uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      FUN_ffff0000907c0874
                (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907d06dd,s___ERROR__ffff0000907e0722,
                 &local_88,s___Invalid_vbmeta_size_in_footer__ffff0000907e7055,0);
      goto LAB_ffff0000907be818;
    }
    uVar5 = CONCAT44(uStack_1d8,uStack_1dc);
  }
  lVar7 = FUN_ffff0000907c0ed0(uVar6);
  if (lVar7 == 0) {
    return 1;
  }
  iVar2 = (**(code **)(param_1 + 0x18))(param_1,&local_88,uVar5,uVar6,lVar7,&local_90);
  if (iVar2 == 0) {
    uVar4 = FUN_ffff0000907c19c0(lVar7,local_90,&local_98,&local_a0);
    switch(uVar4) {
    case 0:
      if ((param_4 & 4) != 0 || param_7 != 0) {
        FUN_ffff0000907c2058(lVar7,&local_1a0);
        iVar1 = 0;
        goto LAB_ffff0000907bea54;
      }
      if (param_14 == (long *)0x0) {
LAB_ffff0000907beb3c:
        if (param_15 != (long *)0x0) {
          *param_15 = local_a0;
        }
        FUN_ffff0000907c2058(lVar7,&local_1a0);
        iVar1 = 0;
        goto LAB_ffff0000907beb5c;
      }
      lVar15 = FUN_ffff0000907c0ed0(local_a0);
      *param_14 = lVar15;
      if (lVar15 != 0) {
        thunk_FUN_ffff00009077e858(lVar15,local_98,local_a0);
        goto LAB_ffff0000907beb3c;
      }
      goto LAB_ffff0000907beda8;
    case 1:
    case 4:
    case 5:
      uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      uVar8 = FUN_ffff0000907c21a8(uVar4);
      FUN_ffff0000907c0874
                (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907d06e1,s___ERROR__ffff0000907e0722,
                 &local_88,s___Error_verifying_vbmeta_image__ffff0000907e3dbd,uVar8,
                 &DAT_ffff0000907f81ce,0);
      iVar1 = 3;
      iVar3 = iVar1;
      if ((param_5 & 1) == 0) goto LAB_ffff0000907bedb0;
      break;
    case 2:
      uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      puVar12 = &DAT_ffff0000907faca3;
      pcVar14 = s___Error_verifying_vbmeta_image__i_ffff0000907d89d0;
      goto LAB_ffff0000907beabc;
    case 3:
      uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      FUN_ffff0000907c0874
                (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907ef064,s___ERROR__ffff0000907e0722,
                 &local_88,s___Error_verifying_vbmeta_image__u_ffff0000907e2283,0);
      iVar3 = 7;
      goto LAB_ffff0000907bedb0;
    default:
      iVar1 = 0;
    }
    FUN_ffff0000907c2058(lVar7,&local_1a0);
    if ((param_4 & 4) == 0 && param_7 == 0) {
LAB_ffff0000907beb5c:
      uVar16 = local_128._4_4_;
      param_6 = local_128;
    }
    else {
LAB_ffff0000907bea54:
      uVar16 = param_7;
      if ((int)local_128 != 0) {
        uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
        puVar12 = &DAT_ffff0000907d3c3d;
        pcVar14 = s___chained_vbmeta_image_has_non_z_ffff0000907e5901;
LAB_ffff0000907beabc:
        FUN_ffff0000907c0874
                  (uVar5,&DAT_ffff0000907ebe0c,puVar12,s___ERROR__ffff0000907e0722,&local_88,pcVar14
                   ,0);
        iVar3 = 6;
        goto LAB_ffff0000907bedb0;
      }
    }
    lVar15 = local_148;
    local_1f4 = uVar16;
    if (local_98 != 0) {
      if (param_10 == 0) {
        local_2c0 = local_2c0 & 0xffffffffffffff00;
        lVar10 = 0;
        if (local_148 != 0) {
          lVar10 = lVar7 + local_150 + CONCAT44(local_190,uStack_194) + 0x100;
        }
        if ((param_4 & 4) == 0) {
          FUN_ffff0000907c49cc();
          iVar2 = (**(code **)(param_1 + 0x30))(param_1,local_98,local_a0,lVar10,lVar15,&local_2c0);
        }
        else {
          iVar2 = (**(code **)(param_1 + 0x70))
                            (param_1,&local_88,local_98,local_a0,lVar10,local_148,&local_2c0,
                             &local_1f4);
        }
        if (iVar2 != 0) {
          iVar3 = 1;
          if (iVar2 == 1) goto LAB_ffff0000907bedb0;
          uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
          puVar12 = &DAT_ffff0000907f7681;
          pcVar14 = s___Error_while_checking_public_ke_ffff0000907f7685;
          goto LAB_ffff0000907be9f8;
        }
        if ((char)local_2c0 == '\0') {
          uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
          FUN_ffff0000907c0874
                    (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907f900e,s___ERROR__ffff0000907e0722,
                     &local_88,s___Public_key_used_to_sign_data_r_ffff0000907f3b7a,0);
          goto joined_r0xffff0000907becb4;
        }
      }
      else if ((local_a0 != param_11) ||
              (iVar2 = FUN_ffff0000907c0a0c(param_10,local_98), iVar2 != 0)) {
        uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
        FUN_ffff0000907c0874
                  (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907e079e,s___ERROR__ffff0000907e0722,
                   &local_88,s___Public_key_used_to_sign_data_d_ffff0000907f762f,0);
joined_r0xffff0000907becb4:
        iVar1 = 5;
        iVar3 = 5;
        if ((param_5 & 1) == 0) goto LAB_ffff0000907bedb0;
      }
    }
    iVar2 = (**(code **)(param_1 + 0x38))(param_1,local_1f4,&local_1a8);
    if (iVar2 == 0) {
      if (local_130 < local_1a8) {
        uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
        FUN_ffff0000907c0874
                  (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907e7077,s___ERROR__ffff0000907e0722,
                   &local_88,s___Image_rollback_index_is_less_t_ffff0000907fe588,0);
        iVar1 = 4;
        iVar3 = iVar1;
        if ((param_5 & 1) == 0) goto LAB_ffff0000907bedb0;
      }
      lVar15 = *(long *)(param_12 + 0x10);
      if (lVar15 == 0x20) {
        uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
        FUN_ffff0000907c0874
                  (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907faca7,s___ERROR__ffff0000907e0722,
                   &local_88,s___Too_many_vbmeta_images__ffff0000907ea2ff,0);
LAB_ffff0000907beda8:
        iVar3 = 1;
      }
      else {
        puVar13 = (undefined8 *)(*(long *)(param_12 + 8) + lVar15 * 0x20);
        *(long *)(param_12 + 0x10) = lVar15 + 1;
        uVar5 = FUN_ffff0000907c101c(param_8);
        *puVar13 = uVar5;
        puVar13[1] = lVar7;
        puVar13[2] = CONCAT44(local_190,uStack_194) + CONCAT44(uStack_188,uStack_18c) + 0x100;
        *(undefined4 *)(puVar13 + 3) = uVar4;
        if (((byte)local_128 >> 1 & 1) != 0) {
          iVar2 = FUN_ffff0000907bfb74(param_1,param_2,param_3,param_12);
          if (iVar2 == 0) {
            return iVar1;
          }
          return iVar2;
        }
        lVar7 = FUN_ffff0000907bcb5c(lVar7,local_90,&local_1b0);
        if (local_1b0 != 0) {
          uVar6 = 0;
          iVar2 = iVar1;
LAB_ffff0000907beea4:
          local_208 = 0;
          uStack_200 = 0;
          uVar9 = FUN_ffff0000907bc774(*(undefined8 *)(lVar7 + uVar6 * 8),&local_208);
          if ((uVar9 & 1) != 0) {
            iVar1 = iVar2;
            switch(local_208) {
            case 1:
              local_210 = 0;
              local_220 = 0;
              uStack_218 = 0;
              local_230 = 0;
              uStack_228 = 0;
              local_240 = 0;
              uStack_238 = 0;
              local_250 = 0;
              uStack_248 = 0;
              local_260 = 0;
              local_258 = 0;
              local_270 = 0;
              local_268 = 0;
              local_280 = 0;
              local_278 = 0;
              local_290 = 0;
              local_288 = 0;
              local_2a0 = 0;
              local_298 = 0;
              local_2b0 = 0;
              local_2a8 = 0;
              local_2c0 = 0;
              local_2b8 = 0;
              uVar9 = FUN_ffff0000907bced4(*(undefined8 *)(lVar7 + uVar6 * 8),&local_2c0);
              if ((uVar9 & 1) == 0) {
                uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
                puVar12 = &DAT_ffff0000907d06e9;
                pcVar14 = s___Hashtree_descriptor_is_invalid_ffff0000907e592c;
                goto LAB_ffff0000907bf364;
              }
              if ((int)local_250 != 0) goto switchD_ffff0000907beed4_default;
              local_2d0 = 0;
              uStack_2c8 = 0;
              local_2e0 = 0;
              uStack_2d8 = 0;
              local_1c0 = 0;
              uStack_1b8 = 0;
              local_1d0 = 0;
              uStack_1cc = 0;
              uStack_1c8 = 0;
              local_1e0 = 0;
              uStack_1dc = 0;
              uStack_1d8 = 0;
              uStack_1d4 = 0;
              local_1f0 = 0;
              uStack_1e8 = 0;
              lVar15 = *(long *)(lVar7 + uVar6 * 8) + 0xb4;
              uVar9 = FUN_ffff0000907c0c7c(lVar15,local_258 & 0xffffffff);
              if ((uVar9 & 1) != 0) {
                if (((local_250 & 0x100000000) == 0) &&
                   (lVar10 = thunk_FUN_ffff00009077ef88(param_3), lVar10 != 0)) {
                  uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
                  puVar12 = &DAT_ffff0000907ed6f2;
                  pcVar14 = s_Cannot_use_A_B_with_a_persistent_ffff0000907ed6f7;
                  goto LAB_ffff0000907bf468;
                }
                if (0x1f < (uint)local_258) {
                  uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
                  puVar12 = &DAT_ffff0000907e594f;
                  pcVar14 = s_Partition_name_does_not_fit__ffff0000907d54fc;
                  goto LAB_ffff0000907bf468;
                }
                thunk_FUN_ffff00009077e858(&local_2e0,lVar15);
                *(undefined1 *)((long)&local_2e0 + (local_258 & 0xffffffff)) = 0;
                iVar1 = thunk_FUN_ffff00009077ede4(&local_278,&DAT_ffff0000907e7095);
                if (iVar1 != 0) {
                  iVar1 = thunk_FUN_ffff00009077ede4(&local_278,s_sha256_ffff000090801816);
                  if (iVar1 == 0) {
                    uVar5 = 0x20;
                    goto LAB_ffff0000907bf1a8;
                  }
                  iVar1 = thunk_FUN_ffff00009077ede4(&local_278,s_sha512_ffff0000907f3b61);
                  if (iVar1 == 0) {
                    uVar5 = 0x40;
                    goto LAB_ffff0000907bf1a8;
                  }
                  uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
                  puVar12 = &DAT_ffff0000907d06ee;
                  pcVar14 = s___Unsupported_hash_algorithm__ffff0000907d06f3;
                  puVar13 = &local_2e0;
                  goto LAB_ffff0000907bf368;
                }
                uVar5 = 0x14;
LAB_ffff0000907bf1a8:
                iVar3 = FUN_ffff0000907c0350(param_1,&local_2e0,uVar5,0,&local_1f0);
                if ((iVar3 == 0) &&
                   ((param_16 == 0 ||
                    (iVar3 = FUN_ffff0000907bc698(&local_2e0,&local_1f0,uVar5,param_16), iVar3 == 0)
                    ))) {
                  iVar1 = 0;
                  goto switchD_ffff0000907beed4_default;
                }
                goto LAB_ffff0000907bedb0;
              }
              uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
              puVar12 = &DAT_ffff0000907ea31a;
              pcVar14 = s_Partition_name_is_not_valid_UTF__ffff0000907fe560;
LAB_ffff0000907bf468:
              FUN_ffff0000907c0874
                        (uVar5,&DAT_ffff0000907ebe0c,puVar12,s___ERROR__ffff0000907e0722,pcVar14,0);
              break;
            case 2:
              iVar3 = FUN_ffff0000907bfdb0
                                (param_1,param_2,param_3,param_5 & 1,
                                 *(undefined8 *)(lVar7 + uVar6 * 8),param_12);
              if ((iVar3 == 0) || (((param_5 & 1) != 0 && (iVar1 = iVar3, iVar3 - 3U < 3))))
              goto switchD_ffff0000907beed4_default;
              goto LAB_ffff0000907bedb0;
            case 3:
              local_2b8 = 0;
              local_2b0 = 0;
              local_2c0 = 0;
              uVar9 = FUN_ffff0000907bd0e4(*(undefined8 *)(lVar7 + uVar6 * 8),&local_2c0);
              if ((uVar9 & 1) == 0) {
                uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
                puVar12 = &DAT_ffff000090803419;
                pcVar14 = s___Kernel_cmdline_descriptor_is_i_ffff0000907f3ba4;
              }
              else {
                lVar15 = *(long *)(lVar7 + uVar6 * 8) + 0x18;
                uVar9 = FUN_ffff0000907c0c7c(lVar15,local_2b0._4_4_);
                if ((uVar9 & 1) != 0) {
                  if ((param_6 & 1) == 0) {
                    if (((uint)local_2b0 >> 1 & 1) != 0) goto switchD_ffff0000907beed4_default;
LAB_ffff0000907bf0f8:
                    if (*(long *)(param_12 + 0x28) == 0) {
                      lVar10 = FUN_ffff0000907c0f68(local_2b0._4_4_ + 1);
                      *(long *)(param_12 + 0x28) = lVar10;
                      if (lVar10 == 0) {
LAB_ffff0000907bf3d4:
                        iVar3 = 1;
                        goto LAB_ffff0000907bedb0;
                      }
                      thunk_FUN_ffff00009077e858(lVar10,lVar15,local_2b0._4_4_);
                    }
                    else {
                      lVar10 = thunk_FUN_ffff00009077ef88();
                      lVar11 = FUN_ffff0000907c0f68(lVar10 + (local_2b0 >> 0x20) + 2);
                      if (lVar11 == 0) goto LAB_ffff0000907bf3d4;
                      thunk_FUN_ffff00009077e858(lVar11,*(undefined8 *)(param_12 + 0x28),lVar10);
                      *(undefined1 *)(lVar11 + lVar10) = 0x20;
                      thunk_FUN_ffff00009077e858
                                ((undefined1 *)(lVar11 + lVar10) + 1,lVar15,local_2b0._4_4_);
                      FUN_ffff0000907c09a0(*(undefined8 *)(param_12 + 0x28));
                      *(long *)(param_12 + 0x28) = lVar11;
                    }
                  }
                  else if ((local_2b0 & 1) == 0) goto LAB_ffff0000907bf0f8;
switchD_ffff0000907beed4_default:
                  uVar6 = uVar6 + 1;
                  iVar2 = iVar1;
                  if (local_1b0 <= uVar6) goto LAB_ffff0000907bf1f4;
                  goto LAB_ffff0000907beea4;
                }
                uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
                puVar12 = &DAT_ffff0000907f2234;
                pcVar14 = s___Kernel_cmdline_is_not_valid_UT_ffff0000907fe5cd;
              }
LAB_ffff0000907bf364:
              puVar13 = &local_88;
LAB_ffff0000907bf368:
              FUN_ffff0000907c0874
                        (uVar5,&DAT_ffff0000907ebe0c,puVar12,s___ERROR__ffff0000907e0722,puVar13,
                         pcVar14,0);
              break;
            case 4:
              local_268 = local_268 & 0xffffffff00000000;
              local_278 = 0;
              local_270 = 0;
              local_288 = 0;
              local_280 = 0;
              local_298 = 0;
              local_290 = 0;
              local_2a8 = 0;
              local_2a0 = 0;
              local_2b8 = 0;
              local_2b0 = 0;
              local_2c0 = 0;
              if ((param_4 & 4) == 0 && param_7 == 0) {
                uVar9 = FUN_ffff0000907bb9c0(*(undefined8 *)(lVar7 + uVar6 * 8),&local_2c0);
                if ((uVar9 & 1) != 0) {
                  if ((uint)local_2b0 == 0) {
                    uVar5 = FUN_ffff0000907c0b68
                                      (s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
                    puVar12 = &DAT_ffff0000907ebb34;
                    pcVar14 = s___Chain_partition_has_invalid_ro_ffff0000907e3dde;
                    goto LAB_ffff0000907bf294;
                  }
                  lVar15 = *(long *)(lVar7 + uVar6 * 8) + 0x5c;
                  iVar3 = FUN_ffff0000907be56c
                                    (param_1,param_2,param_3,param_4,param_5 & 1,
                                     param_6 & 0xffffffff,local_2b0 & 0xffffffff,lVar15,
                                     local_2b0 >> 0x20,lVar15 + (local_2b0 >> 0x20),
                                     local_2a8 & 0xffffffff,param_12,0,param_14,param_15,0);
                  iVar1 = iVar3;
                  if ((iVar3 - 3U < 3) || (iVar1 = iVar2, iVar3 == 0))
                  goto switchD_ffff0000907beed4_default;
                  goto LAB_ffff0000907bedb0;
                }
                uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
                puVar12 = &DAT_ffff0000907fffe6;
                pcVar14 = s___Chain_partition_descriptor_is_i_ffff0000907ef068;
              }
              else {
                uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
                puVar12 = &DAT_ffff0000907fe5c8;
                pcVar14 = s___Encountered_chain_descriptor_n_ffff0000908033e6;
              }
              goto LAB_ffff0000907bf294;
            default:
              goto switchD_ffff0000907beed4_default;
            }
            iVar3 = 6;
            goto LAB_ffff0000907bedb0;
          }
          uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
          puVar12 = &DAT_ffff0000907dd676;
          pcVar14 = s___Descriptor_is_invalid__ffff0000907e707b;
LAB_ffff0000907bf294:
          FUN_ffff0000907c0874
                    (uVar5,&DAT_ffff0000907ebe0c,puVar12,s___ERROR__ffff0000907e0722,&local_88,
                     pcVar14,0);
          iVar3 = 6;
          goto LAB_ffff0000907bedb0;
        }
LAB_ffff0000907bf1f4:
        if (local_1f4 < 0x20) {
          *(ulong *)(param_12 + (ulong)local_1f4 * 8 + 0x30) = local_130;
          if (param_13 != (undefined4 *)0x0) {
            *param_13 = local_184;
          }
        }
        else {
          uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
          FUN_ffff0000907c0874
                    (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907d551a,s___ERROR__ffff0000907e0722,
                     &local_88,s___Invalid_rollback_index_locatio_ffff0000907d0712,0);
          iVar1 = 6;
        }
        iVar3 = iVar1;
        if (lVar7 == 0) {
          return iVar1;
        }
      }
      goto LAB_ffff0000907bedb0;
    }
    iVar3 = 1;
    if (iVar2 == 1) goto LAB_ffff0000907bedb0;
    uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar12 = &DAT_ffff0000907d06e5;
    pcVar14 = s___Error_getting_rollback_index_f_ffff0000907fffb8;
  }
  else {
    iVar3 = 1;
    if (iVar2 == 1) goto LAB_ffff0000907bedb0;
    if ((((param_4 & 4) == 0 && param_7 == 0) && (iVar2 == 3)) && (iVar1 == 0)) {
      uVar5 = thunk_FUN_ffff00009077ef88(&DAT_ffff0000907d0a5b);
      iVar3 = FUN_ffff0000907be56c
                        (param_1,param_2,param_3,param_4,param_5 & 1,0,0,&DAT_ffff0000907d0a5b,uVar5
                         ,0,0,param_12,param_13,param_14,param_15,param_16);
      goto LAB_ffff0000907bedb0;
    }
    uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar12 = &DAT_ffff0000907fac9f;
    pcVar14 = s___Error_loading_vbmeta_data__ffff0000907d54de;
  }
LAB_ffff0000907be9f8:
  FUN_ffff0000907c0874
            (uVar5,&DAT_ffff0000907ebe0c,puVar12,s___ERROR__ffff0000907e0722,&local_88,pcVar14,0);
  iVar3 = 2;
LAB_ffff0000907bedb0:
  FUN_ffff0000907c09a0(lVar7);
  return iVar3;
}



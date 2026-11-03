// ffff0000907bebe0  size=3916  FUN_ffff0000907bebe0

/* WARNING: Type propagation algorithm not settling */

int FUN_ffff0000907bebe0
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
  
  uVar5 = thunk_FUN_ffff00009077f250(s_vbmeta_ffff0000907da540);
  iVar1 = thunk_FUN_ffff00009077f348(param_8,s_vbmeta_ffff0000907da540,uVar5);
  uVar6 = FUN_ffff0000907c12f0(param_8,param_9);
  if ((uVar6 & 1) == 0) {
    uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
    puVar12 = &DAT_ffff0000907e71ac;
    pcVar14 = s_Partition_name_is_not_valid_UTF__ffff0000907fe708;
LAB_ffff0000907bee00:
    FUN_ffff0000907c0ee8(uVar5,&DAT_ffff0000907ebfa6,puVar12,s___ERROR__ffff0000907e07dc,pcVar14,0);
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
  uVar5 = thunk_FUN_ffff00009077f250(param_3);
  uVar6 = FUN_ffff0000907c1384(&local_88,0x20,param_8,param_9,param_3,uVar5);
  if ((uVar6 & 1) == 0) {
    uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
    puVar12 = &DAT_ffff0000907e8ab5;
    pcVar14 = s_Partition_name_and_suffix_does_n_ffff0000908035e2;
    goto LAB_ffff0000907bee00;
  }
  if (iVar1 == 0) {
LAB_ffff0000907bee8c:
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
      uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
      FUN_ffff0000907c0ee8
                (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907fe72c,s___ERROR__ffff0000907e07dc,
                 &local_88,s___Error_loading_footer__ffff0000907e5a03,0);
      return 2;
    }
    uVar6 = FUN_ffff0000907bd2a4(&local_2c0,&local_1f0);
    if ((uVar6 & 1) == 0) goto LAB_ffff0000907bee8c;
    uVar6 = CONCAT44(local_1d0,uStack_1d4);
    if (0x10000 < uVar6) {
      uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
      FUN_ffff0000907c0ee8
                (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907d06fc,s___ERROR__ffff0000907e07dc,
                 &local_88,s___Invalid_vbmeta_size_in_footer__ffff0000907e71b0,0);
      goto LAB_ffff0000907bee8c;
    }
    uVar5 = CONCAT44(uStack_1d8,uStack_1dc);
  }
  lVar7 = FUN_ffff0000907c1544(uVar6);
  if (lVar7 == 0) {
    return 1;
  }
  iVar2 = (**(code **)(param_1 + 0x18))(param_1,&local_88,uVar5,uVar6,lVar7,&local_90);
  if (iVar2 == 0) {
    uVar4 = FUN_ffff0000907c2034(lVar7,local_90,&local_98,&local_a0);
    switch(uVar4) {
    case 0:
      if ((param_4 & 4) != 0 || param_7 != 0) {
        FUN_ffff0000907c26cc(lVar7,&local_1a0);
        iVar1 = 0;
        goto LAB_ffff0000907bf0c8;
      }
      if (param_14 == (long *)0x0) {
LAB_ffff0000907bf1b0:
        if (param_15 != (long *)0x0) {
          *param_15 = local_a0;
        }
        FUN_ffff0000907c26cc(lVar7,&local_1a0);
        iVar1 = 0;
        goto LAB_ffff0000907bf1d0;
      }
      lVar15 = FUN_ffff0000907c1544(local_a0);
      *param_14 = lVar15;
      if (lVar15 != 0) {
        thunk_FUN_ffff00009077eb20(lVar15,local_98,local_a0);
        goto LAB_ffff0000907bf1b0;
      }
      goto LAB_ffff0000907bf41c;
    case 1:
    case 4:
    case 5:
      uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
      uVar8 = FUN_ffff0000907c281c(uVar4);
      FUN_ffff0000907c0ee8
                (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907d0700,s___ERROR__ffff0000907e07dc,
                 &local_88,s___Error_verifying_vbmeta_image__ffff0000907e3ea5,uVar8,
                 &DAT_ffff0000907f836a,0);
      iVar1 = 3;
      iVar3 = iVar1;
      if ((param_5 & 1) == 0) goto LAB_ffff0000907bf424;
      break;
    case 2:
      uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
      puVar12 = &DAT_ffff0000907fae3f;
      pcVar14 = s___Error_verifying_vbmeta_image__i_ffff0000907d8a51;
      goto LAB_ffff0000907bf130;
    case 3:
      uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
      FUN_ffff0000907c0ee8
                (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907ef1ec,s___ERROR__ffff0000907e07dc,
                 &local_88,s___Error_verifying_vbmeta_image__u_ffff0000907e236b,0);
      iVar3 = 7;
      goto LAB_ffff0000907bf424;
    default:
      iVar1 = 0;
    }
    FUN_ffff0000907c26cc(lVar7,&local_1a0);
    if ((param_4 & 4) == 0 && param_7 == 0) {
LAB_ffff0000907bf1d0:
      uVar16 = local_128._4_4_;
      param_6 = local_128;
    }
    else {
LAB_ffff0000907bf0c8:
      uVar16 = param_7;
      if ((int)local_128 != 0) {
        uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
        puVar12 = &DAT_ffff0000907d3c7a;
        pcVar14 = s___chained_vbmeta_image_has_non_z_ffff0000907e5a1c;
LAB_ffff0000907bf130:
        FUN_ffff0000907c0ee8
                  (uVar5,&DAT_ffff0000907ebfa6,puVar12,s___ERROR__ffff0000907e07dc,&local_88,pcVar14
                   ,0);
        iVar3 = 6;
        goto LAB_ffff0000907bf424;
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
          FUN_ffff0000907c5040();
          iVar2 = (**(code **)(param_1 + 0x30))(param_1,local_98,local_a0,lVar10,lVar15,&local_2c0);
        }
        else {
          iVar2 = (**(code **)(param_1 + 0x70))
                            (param_1,&local_88,local_98,local_a0,lVar10,local_148,&local_2c0,
                             &local_1f4);
        }
        if (iVar2 != 0) {
          iVar3 = 1;
          if (iVar2 == 1) goto LAB_ffff0000907bf424;
          uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
          puVar12 = &DAT_ffff0000907f780b;
          pcVar14 = s___Error_while_checking_public_ke_ffff0000907f780f;
          goto LAB_ffff0000907bf06c;
        }
        if ((char)local_2c0 == '\0') {
          uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
          FUN_ffff0000907c0ee8
                    (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907f91aa,s___ERROR__ffff0000907e07dc,
                     &local_88,s___Public_key_used_to_sign_data_r_ffff0000907f3d1d,0);
          goto joined_r0xffff0000907bf328;
        }
      }
      else if ((local_a0 != param_11) ||
              (iVar2 = FUN_ffff0000907c1080(param_10,local_98), iVar2 != 0)) {
        uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
        FUN_ffff0000907c0ee8
                  (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907e0858,s___ERROR__ffff0000907e07dc,
                   &local_88,s___Public_key_used_to_sign_data_d_ffff0000907f77b9,0);
joined_r0xffff0000907bf328:
        iVar1 = 5;
        iVar3 = 5;
        if ((param_5 & 1) == 0) goto LAB_ffff0000907bf424;
      }
    }
    iVar2 = (**(code **)(param_1 + 0x38))(param_1,local_1f4,&local_1a8);
    if (iVar2 == 0) {
      if (local_130 < local_1a8) {
        uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
        FUN_ffff0000907c0ee8
                  (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907e71d2,s___ERROR__ffff0000907e07dc,
                   &local_88,s___Image_rollback_index_is_less_t_ffff0000907fe730,0);
        iVar1 = 4;
        iVar3 = iVar1;
        if ((param_5 & 1) == 0) goto LAB_ffff0000907bf424;
      }
      lVar15 = *(long *)(param_12 + 0x10);
      if (lVar15 == 0x20) {
        uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
        FUN_ffff0000907c0ee8
                  (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907fae43,s___ERROR__ffff0000907e07dc,
                   &local_88,s___Too_many_vbmeta_images__ffff0000907ea45a,0);
LAB_ffff0000907bf41c:
        iVar3 = 1;
      }
      else {
        puVar13 = (undefined8 *)(*(long *)(param_12 + 8) + lVar15 * 0x20);
        *(long *)(param_12 + 0x10) = lVar15 + 1;
        uVar5 = FUN_ffff0000907c1690(param_8);
        *puVar13 = uVar5;
        puVar13[1] = lVar7;
        puVar13[2] = CONCAT44(local_190,uStack_194) + CONCAT44(uStack_188,uStack_18c) + 0x100;
        *(undefined4 *)(puVar13 + 3) = uVar4;
        if (((byte)local_128 >> 1 & 1) != 0) {
          iVar2 = FUN_ffff0000907c01e8(param_1,param_2,param_3,param_12);
          if (iVar2 == 0) {
            return iVar1;
          }
          return iVar2;
        }
        lVar7 = FUN_ffff0000907bd1d0(lVar7,local_90,&local_1b0);
        if (local_1b0 != 0) {
          uVar6 = 0;
          iVar2 = iVar1;
LAB_ffff0000907bf518:
          local_208 = 0;
          uStack_200 = 0;
          uVar9 = FUN_ffff0000907bcde8(*(undefined8 *)(lVar7 + uVar6 * 8),&local_208);
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
              uVar9 = FUN_ffff0000907bd548(*(undefined8 *)(lVar7 + uVar6 * 8),&local_2c0);
              if ((uVar9 & 1) == 0) {
                uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
                puVar12 = &DAT_ffff0000907d0708;
                pcVar14 = s___Hashtree_descriptor_is_invalid_ffff0000907e5a47;
                goto LAB_ffff0000907bf9d8;
              }
              if ((int)local_250 != 0) goto switchD_ffff0000907bf548_default;
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
              uVar9 = FUN_ffff0000907c12f0(lVar15,local_258 & 0xffffffff);
              if ((uVar9 & 1) != 0) {
                if (((local_250 & 0x100000000) == 0) &&
                   (lVar10 = thunk_FUN_ffff00009077f250(param_3), lVar10 != 0)) {
                  uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
                  puVar12 = &DAT_ffff0000907ed88c;
                  pcVar14 = s_Cannot_use_A_B_with_a_persistent_ffff0000907ed891;
                  goto LAB_ffff0000907bfadc;
                }
                if (0x1f < (uint)local_258) {
                  uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
                  puVar12 = &DAT_ffff0000907e5a6a;
                  pcVar14 = s_Partition_name_does_not_fit__ffff0000907d5539;
                  goto LAB_ffff0000907bfadc;
                }
                thunk_FUN_ffff00009077eb20(&local_2e0,lVar15);
                *(undefined1 *)((long)&local_2e0 + (local_258 & 0xffffffff)) = 0;
                iVar1 = thunk_FUN_ffff00009077f0ac(&local_278,&DAT_ffff0000907e71f0);
                if (iVar1 != 0) {
                  iVar1 = thunk_FUN_ffff00009077f0ac(&local_278,s_sha256_ffff0000908019f2);
                  if (iVar1 == 0) {
                    uVar5 = 0x20;
                    goto LAB_ffff0000907bf81c;
                  }
                  iVar1 = thunk_FUN_ffff00009077f0ac(&local_278,s_sha512_ffff0000907f3d04);
                  if (iVar1 == 0) {
                    uVar5 = 0x40;
                    goto LAB_ffff0000907bf81c;
                  }
                  uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
                  puVar12 = &DAT_ffff0000907d070d;
                  pcVar14 = s___Unsupported_hash_algorithm__ffff0000907d0712;
                  puVar13 = &local_2e0;
                  goto LAB_ffff0000907bf9dc;
                }
                uVar5 = 0x14;
LAB_ffff0000907bf81c:
                iVar3 = FUN_ffff0000907c09c4(param_1,&local_2e0,uVar5,0,&local_1f0);
                if ((iVar3 == 0) &&
                   ((param_16 == 0 ||
                    (iVar3 = FUN_ffff0000907bcd0c(&local_2e0,&local_1f0,uVar5,param_16), iVar3 == 0)
                    ))) {
                  iVar1 = 0;
                  goto switchD_ffff0000907bf548_default;
                }
                goto LAB_ffff0000907bf424;
              }
              uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
              puVar12 = &DAT_ffff0000907ea475;
              pcVar14 = s_Partition_name_is_not_valid_UTF__ffff0000907fe708;
LAB_ffff0000907bfadc:
              FUN_ffff0000907c0ee8
                        (uVar5,&DAT_ffff0000907ebfa6,puVar12,s___ERROR__ffff0000907e07dc,pcVar14,0);
              break;
            case 2:
              iVar3 = FUN_ffff0000907c0424
                                (param_1,param_2,param_3,param_5 & 1,
                                 *(undefined8 *)(lVar7 + uVar6 * 8),param_12);
              if ((iVar3 == 0) || (((param_5 & 1) != 0 && (iVar1 = iVar3, iVar3 - 3U < 3))))
              goto switchD_ffff0000907bf548_default;
              goto LAB_ffff0000907bf424;
            case 3:
              local_2b8 = 0;
              local_2b0 = 0;
              local_2c0 = 0;
              uVar9 = FUN_ffff0000907bd758(*(undefined8 *)(lVar7 + uVar6 * 8),&local_2c0);
              if ((uVar9 & 1) == 0) {
                uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
                puVar12 = &DAT_ffff000090803641;
                pcVar14 = s___Kernel_cmdline_descriptor_is_i_ffff0000907f3d47;
              }
              else {
                lVar15 = *(long *)(lVar7 + uVar6 * 8) + 0x18;
                uVar9 = FUN_ffff0000907c12f0(lVar15,local_2b0._4_4_);
                if ((uVar9 & 1) != 0) {
                  if ((param_6 & 1) == 0) {
                    if (((uint)local_2b0 >> 1 & 1) != 0) goto switchD_ffff0000907bf548_default;
LAB_ffff0000907bf76c:
                    if (*(long *)(param_12 + 0x28) == 0) {
                      lVar10 = FUN_ffff0000907c15dc(local_2b0._4_4_ + 1);
                      *(long *)(param_12 + 0x28) = lVar10;
                      if (lVar10 == 0) {
LAB_ffff0000907bfa48:
                        iVar3 = 1;
                        goto LAB_ffff0000907bf424;
                      }
                      thunk_FUN_ffff00009077eb20(lVar10,lVar15,local_2b0._4_4_);
                    }
                    else {
                      lVar10 = thunk_FUN_ffff00009077f250();
                      lVar11 = FUN_ffff0000907c15dc(lVar10 + (local_2b0 >> 0x20) + 2);
                      if (lVar11 == 0) goto LAB_ffff0000907bfa48;
                      thunk_FUN_ffff00009077eb20(lVar11,*(undefined8 *)(param_12 + 0x28),lVar10);
                      *(undefined1 *)(lVar11 + lVar10) = 0x20;
                      thunk_FUN_ffff00009077eb20
                                ((undefined1 *)(lVar11 + lVar10) + 1,lVar15,local_2b0._4_4_);
                      FUN_ffff0000907c1014(*(undefined8 *)(param_12 + 0x28));
                      *(long *)(param_12 + 0x28) = lVar11;
                    }
                  }
                  else if ((local_2b0 & 1) == 0) goto LAB_ffff0000907bf76c;
switchD_ffff0000907bf548_default:
                  uVar6 = uVar6 + 1;
                  iVar2 = iVar1;
                  if (local_1b0 <= uVar6) goto LAB_ffff0000907bf868;
                  goto LAB_ffff0000907bf518;
                }
                uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
                puVar12 = &DAT_ffff0000907f23d7;
                pcVar14 = s___Kernel_cmdline_is_not_valid_UT_ffff0000907fe775;
              }
LAB_ffff0000907bf9d8:
              puVar13 = &local_88;
LAB_ffff0000907bf9dc:
              FUN_ffff0000907c0ee8
                        (uVar5,&DAT_ffff0000907ebfa6,puVar12,s___ERROR__ffff0000907e07dc,puVar13,
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
                uVar9 = FUN_ffff0000907bc034(*(undefined8 *)(lVar7 + uVar6 * 8),&local_2c0);
                if ((uVar9 & 1) != 0) {
                  if ((uint)local_2b0 == 0) {
                    uVar5 = FUN_ffff0000907c11dc
                                      (s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
                    puVar12 = &DAT_ffff0000907ebcce;
                    pcVar14 = s___Chain_partition_has_invalid_ro_ffff0000907e3ec6;
                    goto LAB_ffff0000907bf908;
                  }
                  lVar15 = *(long *)(lVar7 + uVar6 * 8) + 0x5c;
                  iVar3 = FUN_ffff0000907bebe0
                                    (param_1,param_2,param_3,param_4,param_5 & 1,
                                     param_6 & 0xffffffff,local_2b0 & 0xffffffff,lVar15,
                                     local_2b0 >> 0x20,lVar15 + (local_2b0 >> 0x20),
                                     local_2a8 & 0xffffffff,param_12,0,param_14,param_15,0);
                  iVar1 = iVar3;
                  if ((iVar3 - 3U < 3) || (iVar1 = iVar2, iVar3 == 0))
                  goto switchD_ffff0000907bf548_default;
                  goto LAB_ffff0000907bf424;
                }
                uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
                puVar12 = &DAT_ffff00009080018e;
                pcVar14 = s___Chain_partition_descriptor_is_i_ffff0000907ef1f0;
              }
              else {
                uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
                puVar12 = &DAT_ffff0000907fe770;
                pcVar14 = s___Encountered_chain_descriptor_n_ffff00009080360e;
              }
              goto LAB_ffff0000907bf908;
            default:
              goto switchD_ffff0000907bf548_default;
            }
            iVar3 = 6;
            goto LAB_ffff0000907bf424;
          }
          uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
          puVar12 = &DAT_ffff0000907dd730;
          pcVar14 = s___Descriptor_is_invalid__ffff0000907e71d6;
LAB_ffff0000907bf908:
          FUN_ffff0000907c0ee8
                    (uVar5,&DAT_ffff0000907ebfa6,puVar12,s___ERROR__ffff0000907e07dc,&local_88,
                     pcVar14,0);
          iVar3 = 6;
          goto LAB_ffff0000907bf424;
        }
LAB_ffff0000907bf868:
        if (local_1f4 < 0x20) {
          *(ulong *)(param_12 + (ulong)local_1f4 * 8 + 0x30) = local_130;
          if (param_13 != (undefined4 *)0x0) {
            *param_13 = local_184;
          }
        }
        else {
          uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
          FUN_ffff0000907c0ee8
                    (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907d5557,s___ERROR__ffff0000907e07dc,
                     &local_88,s___Invalid_rollback_index_locatio_ffff0000907d0731,0);
          iVar1 = 6;
        }
        iVar3 = iVar1;
        if (lVar7 == 0) {
          return iVar1;
        }
      }
      goto LAB_ffff0000907bf424;
    }
    iVar3 = 1;
    if (iVar2 == 1) goto LAB_ffff0000907bf424;
    uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
    puVar12 = &DAT_ffff0000907d0704;
    pcVar14 = s___Error_getting_rollback_index_f_ffff000090800160;
  }
  else {
    iVar3 = 1;
    if (iVar2 == 1) goto LAB_ffff0000907bf424;
    if ((((param_4 & 4) == 0 && param_7 == 0) && (iVar2 == 3)) && (iVar1 == 0)) {
      uVar5 = thunk_FUN_ffff00009077f250(&DAT_ffff0000907d0a72);
      iVar3 = FUN_ffff0000907bebe0
                        (param_1,param_2,param_3,param_4,param_5 & 1,0,0,&DAT_ffff0000907d0a72,uVar5
                         ,0,0,param_12,param_13,param_14,param_15,param_16);
      goto LAB_ffff0000907bf424;
    }
    uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
    puVar12 = &DAT_ffff0000907fae3b;
    pcVar14 = s___Error_loading_vbmeta_data__ffff0000907d551b;
  }
LAB_ffff0000907bf06c:
  FUN_ffff0000907c0ee8
            (uVar5,&DAT_ffff0000907ebfa6,puVar12,s___ERROR__ffff0000907e07dc,&local_88,pcVar14,0);
  iVar3 = 2;
LAB_ffff0000907bf424:
  FUN_ffff0000907c1014(lVar7);
  return iVar3;
}



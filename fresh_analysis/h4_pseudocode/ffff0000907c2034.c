
undefined8 FUN_ffff0000907c2034(long param_1,ulong param_2,long *param_3,long *param_4)

{
  long lVar1;
  long lVar2;
  int iVar3;
  undefined8 uVar4;
  ulong uVar5;
  undefined8 *puVar6;
  undefined *puVar7;
  char *pcVar8;
  ulong local_3d8;
  ulong local_3d0;
  ulong local_3c8;
  ulong local_3c0;
  ulong local_3b8;
  undefined1 auStack_3b0 [400];
  undefined8 local_220;
  undefined8 local_218;
  undefined8 uStack_210;
  undefined8 local_208;
  undefined8 uStack_200;
  undefined8 local_1f8;
  undefined8 uStack_1f0;
  undefined8 local_1e8;
  undefined8 uStack_1e0;
  undefined8 local_1d8;
  undefined8 local_1d0;
  undefined8 local_1c8;
  undefined8 local_1c0;
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
  undefined8 local_150;
  uint local_148;
  uint uStack_144;
  undefined4 local_140;
  uint uStack_13c;
  undefined4 uStack_138;
  int local_134;
  long local_130;
  long local_128;
  long local_120;
  undefined8 uStack_118;
  long local_110;
  long local_108;
  undefined8 local_100;
  long local_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  
  if (param_3 != (long *)0x0) {
    *param_3 = 0;
  }
  if (param_4 != (long *)0x0) {
    *param_4 = 0;
  }
  if (param_2 < 0x100) {
    uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
    puVar7 = &DAT_ffff0000907e5a73;
    pcVar8 = s_Length_is_smaller_than_header__ffff0000907d70e4;
  }
  else {
    iVar3 = FUN_ffff0000907c1080(param_1,&DAT_ffff0000907e3ea0,4);
    if (iVar3 == 0) {
      local_60 = 0;
      uStack_58 = 0;
      local_70 = 0;
      uStack_68 = 0;
      local_80 = 0;
      uStack_78 = 0;
      local_90 = 0;
      uStack_88 = 0;
      local_a0 = 0;
      uStack_98 = 0;
      local_b0 = 0;
      uStack_a8 = 0;
      local_c0 = 0;
      uStack_b8 = 0;
      local_d0 = 0;
      uStack_c8 = 0;
      local_e0 = 0;
      uStack_d8 = 0;
      local_f0 = 0;
      uStack_e8 = 0;
      local_100 = 0;
      local_f8 = 0;
      local_110 = 0;
      local_108 = 0;
      local_120 = 0;
      uStack_118 = 0;
      local_130 = 0;
      local_128 = 0;
      local_140 = 0;
      uStack_13c = 0;
      uStack_138 = 0;
      local_134 = 0;
      local_150 = 0;
      local_148 = 0;
      uStack_144 = 0;
      local_158 = 0;
      local_160 = 0;
      local_168 = 0;
      local_170 = 0;
      local_178 = 0;
      local_180 = 0;
      local_188 = 0;
      local_190 = 0;
      local_198 = 0;
      local_1a0 = 0;
      local_1a8 = 0;
      local_1b0 = 0;
      local_1b8 = 0;
      local_1c0 = 0;
      local_1c8 = 0;
      local_1d0 = 0;
      local_1d8 = 0;
      local_1e8 = 0;
      uStack_1e0 = 0;
      local_1f8 = 0;
      uStack_1f0 = 0;
      local_208 = 0;
      uStack_200 = 0;
      local_218 = 0;
      uStack_210 = 0;
      local_220 = 0;
      FUN_ffff00009077ee50(auStack_3b0,0,400);
      FUN_ffff0000907c26cc(param_1,&local_150);
      if ((local_150._4_4_ != 1) || (2 < local_148)) {
        uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
        FUN_ffff0000907c0ee8
                  (uVar4,&DAT_ffff0000907ebfa6,&DAT_ffff0000907fc9c9,s___ERROR__ffff0000907e07dc,
                   s_Mismatch_between_image_version_a_ffff0000907e08b3,0);
        return 3;
      }
      if (uStack_a8._7_1_ == '\0') {
        if (((uStack_144 & 0x3f) == 0) && ((uStack_13c & 0x3f) == 0)) {
          local_3b8 = 0x100;
          uVar5 = FUN_ffff0000907c112c(&local_3b8);
          if (((uVar5 & 1) == 0) ||
             (uVar5 = FUN_ffff0000907c112c(&local_3b8,CONCAT44(uStack_138,uStack_13c)),
             (uVar5 & 1) == 0)) {
            uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
            puVar7 = &DAT_ffff0000907da386;
            pcVar8 = s_Overflow_while_computing_size_of_ffff0000907dbda1;
          }
          else if (param_2 < local_3b8) {
            uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
            puVar7 = &DAT_ffff0000907da389;
            pcVar8 = s_Block_sizes_add_up_to_more_than_g_ffff0000907ed8ca;
          }
          else {
            uVar5 = FUN_ffff0000907c123c(0,param_1,param_2);
            if ((uVar5 & 1) == 0) {
              uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
              puVar7 = &DAT_ffff0000907df11c;
              pcVar8 = s_Boot_image_location_and_length_m_ffff0000907e71f5;
            }
            else {
              local_3c0 = 0;
              uVar5 = FUN_ffff0000907c123c(&local_3c0,local_130,local_128);
              if (((uVar5 & 1) == 0) || (CONCAT44(local_140,uStack_144) < local_3c0)) {
                uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
                puVar7 = &DAT_ffff0000907e3f09;
                pcVar8 = s_Hash_is_not_entirely_in_its_bloc_ffff0000907e3f0d;
              }
              else {
                local_3c8 = 0;
                uVar5 = FUN_ffff0000907c123c(&local_3c8,local_120,uStack_118);
                if (((uVar5 & 1) == 0) || (CONCAT44(local_140,uStack_144) < local_3c8)) {
                  uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
                  puVar7 = &DAT_ffff000090801c75;
                  pcVar8 = s_Signature_is_not_entirely_in_its_ffff0000907d23bd;
                }
                else {
                  local_3d0 = 0;
                  uVar5 = FUN_ffff0000907c123c(&local_3d0,local_110,local_108);
                  if (((uVar5 & 1) == 0) || (CONCAT44(uStack_138,uStack_13c) < local_3d0)) {
                    uVar4 = FUN_ffff0000907c11dc
                                      (s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
                    puVar7 = &DAT_ffff0000907e23df;
                    pcVar8 = s_Public_key_is_not_entirely_in_it_ffff0000907f5b23;
                  }
                  else {
                    if (local_f8 != 0) {
                      local_3d8 = 0;
                      uVar5 = FUN_ffff0000907c123c(&local_3d8,local_100);
                      if (((uVar5 & 1) == 0) || (CONCAT44(uStack_138,uStack_13c) < local_3d8)) {
                        uVar4 = FUN_ffff0000907c11dc
                                          (s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
                        puVar7 = &DAT_ffff000090801c79;
                        pcVar8 = s_Public_key_metadata_is_not_entir_ffff0000907fae8f;
                        goto LAB_ffff0000907c20f0;
                      }
                    }
                    if (local_134 == 0) {
                      return 1;
                    }
                    puVar6 = (undefined8 *)FUN_ffff0000907bc1cc();
                    if (puVar6 == (undefined8 *)0x0) {
                      uVar4 = FUN_ffff0000907c11dc
                                        (s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
                      puVar7 = &DAT_ffff0000907dbdcf;
                      pcVar8 = s_Invalid_or_unknown_algorithm__ffff0000907f246e;
                    }
                    else {
                      if (local_128 == puVar6[2]) {
                        lVar1 = param_1 + 0x100;
                        lVar2 = lVar1 + CONCAT44(local_140,uStack_144);
                        if (local_134 - 4U < 3) {
                          FUN_ffff0000907be2e4(auStack_3b0);
                          FUN_ffff0000907be384(auStack_3b0,param_1,0x100);
                          FUN_ffff0000907be384(auStack_3b0,lVar2,CONCAT44(uStack_138,uStack_13c));
                          uVar4 = FUN_ffff0000907be638(auStack_3b0);
                        }
                        else {
                          if (2 < local_134 - 1U) {
                            uVar4 = FUN_ffff0000907c11dc
                                              (s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
                            puVar7 = &DAT_ffff0000907e2402;
                            pcVar8 = s_Unknown_algorithm__ffff0000907fe7d1;
                            goto LAB_ffff0000907c20f0;
                          }
                          FUN_ffff0000907c6bd8(&local_220);
                          FUN_ffff0000907c6c10(&local_220,param_1,0x100);
                          FUN_ffff0000907c6c10(&local_220,lVar2,CONCAT44(uStack_138,uStack_13c));
                          uVar4 = FUN_ffff0000907c6c54(&local_220);
                        }
                        iVar3 = FUN_ffff0000907c1080(lVar1 + local_130,uVar4,local_128);
                        if (iVar3 != 0) {
                          uVar4 = FUN_ffff0000907c11dc
                                            (s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
                          FUN_ffff0000907c0ee8
                                    (uVar4,&DAT_ffff0000907ebfa6,&DAT_ffff0000907f7849,
                                     s___ERROR__ffff0000907e07dc,
                                     s_Hash_does_not_match__ffff0000907ed8f9,0);
                          return 4;
                        }
                        uVar5 = FUN_ffff0000907bdb54
                                          (lVar2 + local_110,local_108,lVar1 + local_120,uStack_118,
                                           lVar1 + local_130,local_128,*puVar6,puVar6[1]);
                        if ((uVar5 & 1) == 0) {
                          return 5;
                        }
                        if (local_108 == 0) {
                          return 0;
                        }
                        if (param_3 != (long *)0x0) {
                          *param_3 = lVar2 + local_110;
                        }
                        if (param_4 == (long *)0x0) {
                          return 0;
                        }
                        *param_4 = local_108;
                        return 0;
                      }
                      uVar4 = FUN_ffff0000907c11dc
                                        (s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
                      puVar7 = &DAT_ffff0000907ea4b1;
                      pcVar8 = s_Embedded_hash_has_wrong_size__ffff0000907e23e3;
                    }
                  }
                }
              }
            }
          }
        }
        else {
          uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
          puVar7 = &DAT_ffff000090801c72;
          pcVar8 = s_Block_size_is_not_a_multiple_of_6_ffff0000907f2449;
        }
      }
      else {
        uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
        puVar7 = &DAT_ffff0000907da2dc;
        pcVar8 = s_Release_string_does_not_end_with_ffff0000907df1c8;
      }
    }
    else {
      uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_vbmeta_i_ffff0000907e8abd);
      puVar7 = &DAT_ffff0000907df1c5;
      pcVar8 = s_Magic_is_incorrect__ffff0000907f76e8;
    }
  }
LAB_ffff0000907c20f0:
  FUN_ffff0000907c0ee8(uVar4,&DAT_ffff0000907ebfa6,puVar7,s___ERROR__ffff0000907e07dc,pcVar8,0);
  return 2;
}



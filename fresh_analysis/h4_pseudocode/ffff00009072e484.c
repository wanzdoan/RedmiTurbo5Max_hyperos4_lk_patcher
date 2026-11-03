
undefined8 FUN_ffff00009072e484(undefined8 param_1,undefined8 param_2,long *param_3)

{
  int iVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  char *pcVar6;
  char *pcVar7;
  undefined8 uVar8;
  ulong uVar9;
  ulong uVar10;
  uint uVar11;
  int *piVar12;
  uint local_28;
  undefined4 local_24;
  
  local_28 = 0;
  lVar4 = FUN_ffff00009072eb14(param_1,param_2,&local_28);
  if (lVar4 == 0) {
    return 0xffffffff;
  }
  if (param_3 != (long *)0x0) {
    *param_3 = lVar4;
  }
  uVar9 = (ulong)local_28;
  if (local_28 == 6) {
    iVar1 = FUN_ffff000090730738(0,*(undefined4 *)(lVar4 + 0xac),0,1);
    if (iVar1 < 0) {
      pcVar6 = s__CCCI___s__d__update_whole_info_f_ffff0000907ff009;
      pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
      uVar8 = 0x129;
    }
    else {
      uVar10 = (ulong)*(uint *)(lVar4 + 0xc0);
      if (*(uint *)(lVar4 + 0xc0) != 0) {
        uVar9 = 0;
        piVar12 = (int *)(lVar4 + 200);
        do {
          iVar1 = *piVar12;
          if (iVar1 != 0) {
            iVar3 = piVar12[-1];
            iVar2 = FUN_ffff000090731698(uVar9 & 0xffffffff);
            if (iVar2 == 0) {
              pcVar6 = s__CCCI___s__d__region_number_abno_ffff0000907fb649;
              pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
              uVar8 = 0x135;
              uVar9 = uVar9 & 0xffffffff;
              goto LAB_ffff00009072e568;
            }
            iVar1 = FUN_ffff000090730738(iVar3,iVar1,iVar2,0);
            if (iVar1 < 0) {
              pcVar6 = s__CCCI___s__d__update_md_region___ffff0000907ee0ea;
              pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
              uVar8 = 0x139;
              uVar9 = uVar9 & 0xffffffff;
              goto LAB_ffff00009072e568;
            }
            uVar10 = (ulong)*(uint *)(lVar4 + 0xc0);
          }
          piVar12 = piVar12 + 2;
          uVar9 = uVar9 + 1;
        } while (uVar9 < uVar10);
      }
      iVar1 = *(int *)(lVar4 + 0xb8);
      iVar3 = *(int *)(lVar4 + 0xbc);
      if (iVar1 == 0 && iVar3 == 0) {
        FUN_ffff000090730228
                  (s__CCCI___s__d__DSP_size___offset_a_ffff0000907d2b51,
                   s_ass_func_for_v6_normal_img_ffff0000907e79ba,0x142);
LAB_ffff00009072e5f8:
        if ((*(int *)(lVar4 + 0x120) == 0) ||
           (iVar1 = FUN_ffff000090730738(*(undefined4 *)(lVar4 + 0x11c),*(int *)(lVar4 + 0x120),0,4)
           , -1 < iVar1)) {
          if ((*(int *)(lVar4 + 0x128) == 0) ||
             (iVar1 = FUN_ffff000090730738
                                (*(undefined4 *)(lVar4 + 0x124),*(int *)(lVar4 + 0x128),0,4),
             -1 < iVar1)) {
            if ((*(int *)(lVar4 + 0x130) == 0) ||
               (iVar1 = FUN_ffff000090730738
                                  (*(undefined4 *)(lVar4 + 300),*(int *)(lVar4 + 0x130),0,4),
               -1 < iVar1)) {
              if ((*(int *)(lVar4 + 0x138) == 0) ||
                 (iVar1 = FUN_ffff000090730738
                                    (*(undefined4 *)(lVar4 + 0x134),*(int *)(lVar4 + 0x138),0,4),
                 -1 < iVar1)) {
                if (*(int *)(lVar4 + 0x140) != 0) {
                  uVar11 = 4;
                  iVar1 = FUN_ffff000090730738
                                    (*(undefined4 *)(lVar4 + 0x13c),*(int *)(lVar4 + 0x140),0,4);
                  if (iVar1 < 0) goto LAB_ffff00009072e93c;
                }
                if ((*(int *)(lVar4 + 0x148) == 0) ||
                   (iVar1 = FUN_ffff000090730738
                                      (*(undefined4 *)(lVar4 + 0x144),*(int *)(lVar4 + 0x148),0,4),
                   -1 < iVar1)) {
                  if ((*(int *)(lVar4 + 0x150) == 0) ||
                     (iVar1 = FUN_ffff000090730738
                                        (*(undefined4 *)(lVar4 + 0x14c),*(int *)(lVar4 + 0x150),0,4)
                     , -1 < iVar1)) {
                    if ((*(int *)(lVar4 + 0x158) == 0) ||
                       (iVar1 = FUN_ffff000090730738
                                          (*(undefined4 *)(lVar4 + 0x154),*(int *)(lVar4 + 0x158),0,
                                           4), -1 < iVar1)) {
                      if ((*(int *)(lVar4 + 0x170) == 0) ||
                         (iVar1 = FUN_ffff000090730738
                                            (*(undefined4 *)(lVar4 + 0x16c),*(int *)(lVar4 + 0x170),
                                             0,0x20), -1 < iVar1)) {
                        if ((*(int *)(lVar4 + 0x178) == 0) ||
                           (iVar1 = FUN_ffff000090730738
                                              (*(undefined4 *)(lVar4 + 0x174),
                                               *(int *)(lVar4 + 0x178),0,0x40), -1 < iVar1)) {
                          if ((*(int *)(lVar4 + 0x168) == 0) ||
                             (iVar1 = FUN_ffff000090730738
                                                (*(undefined4 *)(lVar4 + 0x164),
                                                 *(int *)(lVar4 + 0x168),0,0x80), -1 < iVar1)) {
                            local_24 = *(undefined4 *)(lVar4 + 0x184);
                            iVar1 = FUN_ffff000090732394(s_udc_en_ffff0000907f9a55,&local_24,4);
                            if (iVar1 < 0) {
                              pcVar6 = s__CCCI___s__d__add_udc_en_tag_fai_ffff0000907e1186;
                              pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
                              uVar8 = 0x17b;
                            }
                            else {
                              local_24 = *(undefined4 *)(lVar4 + 0x180);
                              iVar1 = FUN_ffff000090732394
                                                (s_consys_size_ffff0000907f7ffa,&local_24,4);
                              if (iVar1 < 0) {
                                pcVar6 = s__CCCI___s__d__add_consys_size_ta_ffff0000907eac16;
                                pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
                                uVar8 = 0x182;
                              }
                              else {
                                local_24 = *(undefined4 *)(lVar4 + 0x18c);
                                iVar1 = FUN_ffff000090732394
                                                  (s_nv_cache_shm_size_ffff0000907f11a1,&local_24,4)
                                ;
                                if (iVar1 < 0) {
                                  pcVar6 = s__CCCI___s__d__add_nv_cache_shm_s_ffff0000907f80d2;
                                  pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
                                  uVar8 = 0x189;
                                }
                                else {
                                  local_24 = *(undefined4 *)(lVar4 + 400);
                                  iVar1 = FUN_ffff000090732394
                                                    (s_drdi_version_ffff0000907d77ec,&local_24,4);
                                  if (iVar1 < 0) {
                                    pcVar6 = s__CCCI___s__d__add_drdi_version_t_ffff0000907e11a9;
                                    pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
                                    uVar8 = 400;
                                  }
                                  else {
                                    lVar5 = FUN_ffff00009077d514(0x50);
                                    if (lVar5 != 0) {
                                      FUN_ffff00009077ee50(lVar5,0,0x50);
                                      FUN_ffff00009077eb20(lVar5,lVar4 + 0x18,0x10);
                                      iVar1 = FUN_ffff000090732394
                                                        (s_md_platform_ffff0000907e11e4,lVar5,0x10);
                                      if (iVar1 < 0) {
                                        pcVar6 = s__CCCI___s__d__add_tag_MD_PLATFOR_ffff0000907ec475
                                        ;
                                        uVar8 = 0xad;
LAB_ffff00009072ea5c:
                                        FUN_ffff000090730228
                                                  (pcVar6,s_add_md_inf_to_tag_ffff0000907e11d2,uVar8
                                                  );
                                        thunk_FUN_ffff0000907c314c(lVar5);
                                        return 0xffffffff;
                                      }
                                      FUN_ffff00009077ee50(lVar5,0,0x50);
                                      FUN_ffff00009077eb20(lVar5,lVar4 + 0x28,0x40);
                                      iVar1 = FUN_ffff000090732394
                                                        (s_md_build_time_ffff0000907d1024,lVar5,0x40
                                                        );
                                      if (iVar1 < 0) {
                                        pcVar6 = s__CCCI___s__d__add_tag_MD_BUILD_T_ffff0000907eac3e
                                        ;
                                        uVar8 = 0xb7;
                                        goto LAB_ffff00009072ea5c;
                                      }
                                      FUN_ffff00009077ee50(lVar5,0,0x50);
                                      FUN_ffff00009077eb20(lVar5,lVar4 + 0x68,0x40);
                                      iVar1 = FUN_ffff000090732394
                                                        (s_md_build_ver_ffff0000907f47d5,lVar5,0x40)
                                      ;
                                      if (iVar1 < 0) {
                                        pcVar6 = s__CCCI___s__d__add_tag_MD_BUILD_V_ffff0000907f8100
                                        ;
                                        uVar8 = 0xc1;
                                        goto LAB_ffff00009072ea5c;
                                      }
                                      iVar1 = FUN_ffff000090732394
                                                        (s_md_capability_ffff0000907e9282,
                                                         lVar4 + 0x14,4);
                                      if (iVar1 < 0) {
                                        FUN_ffff000090730228
                                                  (
                                                  s__CCCI___s__d__add_tag_MD_BUILD_V_ffff0000907f8100
                                                  ,s_add_md_inf_to_tag_ffff0000907e11d2,0xc9);
                                      }
                                      iVar3 = *(int *)(lVar4 + 0xc);
                                      if (iVar3 == 1) {
                                        uVar8 = 0xbc;
                                      }
                                      else if (iVar3 == 6) {
                                        uVar8 = 0x200;
                                      }
                                      else {
                                        if (iVar3 != 3) {
                                          FUN_ffff000090730228
                                                    (
                                                  s__CCCI___s__d__Ivalid_MD_check_he_ffff0000907ddfd9
                                                  ,s_compatible_for_legacy_ffff0000907f9b43,0x8c);
                                          goto LAB_ffff00009072eaf4;
                                        }
                                        uVar8 = 0x11c;
                                      }
                                      iVar3 = FUN_ffff000090732394
                                                        (s_md1_chk_ffff0000907e79d5,lVar4,uVar8);
                                      if (iVar3 == 0) {
                                        iVar3 = FUN_ffff000090732394
                                                          (s_md1img_ffff0000907f3f58,lVar4 + 0xb0,4)
                                        ;
                                        if (iVar3 != 0) {
                                          FUN_ffff00009077e11c
                                                    (
                                                  s__s__d___CCCI_add_tag_md1img_fail_ffff0000907f9b59
                                                  ,s_compatible_for_legacy_ffff0000907f9b43,0x96);
                                        }
                                      }
                                      else {
                                        FUN_ffff00009077e11c
                                                  (
                                                  s__s__d___CCCI_add_tag_md_chk_hdr_f_ffff0000907e9290
                                                  ,s_compatible_for_legacy_ffff0000907f9b43,0x91);
                                      }
LAB_ffff00009072eaf4:
                                      thunk_FUN_ffff0000907c314c(lVar5);
                                      if (iVar1 < 0) {
                                        return 0xffffffff;
                                      }
                                      FUN_ffff00009072f664
                                                (*(undefined4 *)(lVar4 + 0x14),
                                                 *(undefined4 *)(lVar4 + 0xb0),
                                                 *(undefined4 *)(lVar4 + 0xac));
                                      return 0;
                                    }
                                    pcVar6 = s__CCCI___s__d__Alloc_hdr_inf_buf_m_ffff0000907dc6c8;
                                    pcVar7 = s_add_md_inf_to_tag_ffff0000907e11d2;
                                    uVar8 = 0xa4;
                                  }
                                }
                              }
                            }
                          }
                          else {
                            pcVar6 = s__CCCI___s__d__update_Ramdisk_fai_ffff0000907f12d0;
                            pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
                            uVar8 = 0x173;
                          }
                        }
                        else {
                          pcVar6 = s__CCCI___s__d__update_DRDI_RT_fai_ffff0000907f2b01;
                          pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
                          uVar8 = 0x169;
                        }
                      }
                      else {
                        pcVar6 = s__CCCI___s__d__update_DRDI_SRC_fa_ffff0000907e4867;
                        pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
                        uVar8 = 0x15f;
                      }
                      goto LAB_ffff00009072e5c0;
                    }
                    uVar11 = 7;
                  }
                  else {
                    uVar11 = 6;
                  }
                }
                else {
                  uVar11 = 5;
                }
              }
              else {
                uVar11 = 3;
              }
            }
            else {
              uVar11 = 2;
            }
          }
          else {
            uVar11 = 1;
          }
        }
        else {
          uVar11 = 0;
        }
LAB_ffff00009072e93c:
        pcVar6 = s__CCCI___s__d__update_md_padding_r_ffff0000907e60e0;
        pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
        uVar8 = 0x155;
        uVar9 = (ulong)uVar11;
        goto LAB_ffff00009072e568;
      }
      if (iVar3 == 0) {
        pcVar6 = s__CCCI___s__d__DSP_size_info_abno_ffff00009080260a;
        pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
        uVar8 = 0x144;
      }
      else if (iVar1 == 0) {
        pcVar6 = s__CCCI___s__d__DSP_offset_info_ab_ffff0000907f62e4;
        pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
        uVar8 = 0x147;
      }
      else {
        iVar1 = FUN_ffff000090730738(iVar1,iVar3,0,2);
        if (-1 < iVar1) goto LAB_ffff00009072e5f8;
        pcVar6 = s__CCCI___s__d__update_DSP_reserve_ffff0000907ef97f;
        pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e79ba;
        uVar8 = 0x14a;
      }
    }
LAB_ffff00009072e5c0:
    FUN_ffff000090730228(pcVar6,pcVar7,uVar8);
  }
  else {
    pcVar6 = s__CCCI___s__d__not_support_header_ffff0000907fefad;
    pcVar7 = s_ccci_main_img_parsing_ffff0000907f808a;
    uVar8 = 0x1cb;
LAB_ffff00009072e568:
    FUN_ffff000090730228(pcVar6,pcVar7,uVar8,uVar9);
  }
  return 0xffffffff;
}



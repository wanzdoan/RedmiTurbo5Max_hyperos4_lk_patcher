// ffff00009072e1ec  size=1680  FUN_ffff00009072e1ec

undefined8 FUN_ffff00009072e1ec(undefined8 param_1,undefined8 param_2,long *param_3)

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
  lVar4 = FUN_ffff00009072e87c(param_1,param_2,&local_28);
  if (lVar4 == 0) {
    return 0xffffffff;
  }
  if (param_3 != (long *)0x0) {
    *param_3 = lVar4;
  }
  uVar9 = (ulong)local_28;
  if (local_28 == 6) {
    iVar1 = FUN_ffff0000907304a0(0,*(undefined4 *)(lVar4 + 0xac),0,1);
    if (iVar1 < 0) {
      pcVar6 = s__CCCI___s__d__update_whole_info_f_ffff0000907fee61;
      pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
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
            iVar2 = FUN_ffff000090731400(uVar9 & 0xffffffff);
            if (iVar2 == 0) {
              pcVar6 = s__CCCI___s__d__region_number_abno_ffff0000907fb4ad;
              pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
              uVar8 = 0x135;
              uVar9 = uVar9 & 0xffffffff;
              goto LAB_ffff00009072e2d0;
            }
            iVar1 = FUN_ffff0000907304a0(iVar3,iVar1,iVar2,0);
            if (iVar1 < 0) {
              pcVar6 = s__CCCI___s__d__update_md_region___ffff0000907edf79;
              pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
              uVar8 = 0x139;
              uVar9 = uVar9 & 0xffffffff;
              goto LAB_ffff00009072e2d0;
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
        FUN_ffff00009072ff90
                  (s__CCCI___s__d__DSP_size___offset_a_ffff0000907d2b14,
                   s_ass_func_for_v6_normal_img_ffff0000907e7876,0x142);
LAB_ffff00009072e360:
        if ((*(int *)(lVar4 + 0x120) == 0) ||
           (iVar1 = FUN_ffff0000907304a0(*(undefined4 *)(lVar4 + 0x11c),*(int *)(lVar4 + 0x120),0,4)
           , -1 < iVar1)) {
          if ((*(int *)(lVar4 + 0x128) == 0) ||
             (iVar1 = FUN_ffff0000907304a0
                                (*(undefined4 *)(lVar4 + 0x124),*(int *)(lVar4 + 0x128),0,4),
             -1 < iVar1)) {
            if ((*(int *)(lVar4 + 0x130) == 0) ||
               (iVar1 = FUN_ffff0000907304a0
                                  (*(undefined4 *)(lVar4 + 300),*(int *)(lVar4 + 0x130),0,4),
               -1 < iVar1)) {
              if ((*(int *)(lVar4 + 0x138) == 0) ||
                 (iVar1 = FUN_ffff0000907304a0
                                    (*(undefined4 *)(lVar4 + 0x134),*(int *)(lVar4 + 0x138),0,4),
                 -1 < iVar1)) {
                if (*(int *)(lVar4 + 0x140) != 0) {
                  uVar11 = 4;
                  iVar1 = FUN_ffff0000907304a0
                                    (*(undefined4 *)(lVar4 + 0x13c),*(int *)(lVar4 + 0x140),0,4);
                  if (iVar1 < 0) goto LAB_ffff00009072e6a4;
                }
                if ((*(int *)(lVar4 + 0x148) == 0) ||
                   (iVar1 = FUN_ffff0000907304a0
                                      (*(undefined4 *)(lVar4 + 0x144),*(int *)(lVar4 + 0x148),0,4),
                   -1 < iVar1)) {
                  if ((*(int *)(lVar4 + 0x150) == 0) ||
                     (iVar1 = FUN_ffff0000907304a0
                                        (*(undefined4 *)(lVar4 + 0x14c),*(int *)(lVar4 + 0x150),0,4)
                     , -1 < iVar1)) {
                    if ((*(int *)(lVar4 + 0x158) == 0) ||
                       (iVar1 = FUN_ffff0000907304a0
                                          (*(undefined4 *)(lVar4 + 0x154),*(int *)(lVar4 + 0x158),0,
                                           4), -1 < iVar1)) {
                      if ((*(int *)(lVar4 + 0x170) == 0) ||
                         (iVar1 = FUN_ffff0000907304a0
                                            (*(undefined4 *)(lVar4 + 0x16c),*(int *)(lVar4 + 0x170),
                                             0,0x20), -1 < iVar1)) {
                        if ((*(int *)(lVar4 + 0x178) == 0) ||
                           (iVar1 = FUN_ffff0000907304a0
                                              (*(undefined4 *)(lVar4 + 0x174),
                                               *(int *)(lVar4 + 0x178),0,0x40), -1 < iVar1)) {
                          if ((*(int *)(lVar4 + 0x168) == 0) ||
                             (iVar1 = FUN_ffff0000907304a0
                                                (*(undefined4 *)(lVar4 + 0x164),
                                                 *(int *)(lVar4 + 0x168),0,0x80), -1 < iVar1)) {
                            local_24 = *(undefined4 *)(lVar4 + 0x184);
                            iVar1 = FUN_ffff0000907320fc(s_udc_en_ffff0000907f98b9,&local_24,4);
                            if (iVar1 < 0) {
                              pcVar6 = s__CCCI___s__d__add_udc_en_tag_fai_ffff0000907e10b5;
                              pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
                              uVar8 = 0x17b;
                            }
                            else {
                              local_24 = *(undefined4 *)(lVar4 + 0x180);
                              iVar1 = FUN_ffff0000907320fc
                                                (s_consys_size_ffff0000907f7e5e,&local_24,4);
                              if (iVar1 < 0) {
                                pcVar6 = s__CCCI___s__d__add_consys_size_ta_ffff0000907eaa93;
                                pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
                                uVar8 = 0x182;
                              }
                              else {
                                local_24 = *(undefined4 *)(lVar4 + 0x18c);
                                iVar1 = FUN_ffff0000907320fc
                                                  (s_nv_cache_shm_size_ffff0000907f1030,&local_24,4)
                                ;
                                if (iVar1 < 0) {
                                  pcVar6 = s__CCCI___s__d__add_nv_cache_shm_s_ffff0000907f7f36;
                                  pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
                                  uVar8 = 0x189;
                                }
                                else {
                                  local_24 = *(undefined4 *)(lVar4 + 400);
                                  iVar1 = FUN_ffff0000907320fc
                                                    (s_drdi_version_ffff0000907d7756,&local_24,4);
                                  if (iVar1 < 0) {
                                    pcVar6 = s__CCCI___s__d__add_drdi_version_t_ffff0000907e10d8;
                                    pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
                                    uVar8 = 400;
                                  }
                                  else {
                                    lVar5 = FUN_ffff00009077d24c(0x50);
                                    if (lVar5 != 0) {
                                      FUN_ffff00009077eb88(lVar5,0,0x50);
                                      FUN_ffff00009077e858(lVar5,lVar4 + 0x18,0x10);
                                      iVar1 = FUN_ffff0000907320fc
                                                        (s_md_platform_ffff0000907e1113,lVar5,0x10);
                                      if (iVar1 < 0) {
                                        pcVar6 = s__CCCI___s__d__add_tag_MD_PLATFOR_ffff0000907ec2f2
                                        ;
                                        uVar8 = 0xad;
LAB_ffff00009072e7c4:
                                        FUN_ffff00009072ff90
                                                  (pcVar6,s_add_md_inf_to_tag_ffff0000907e1101,uVar8
                                                  );
                                        thunk_FUN_ffff0000907c2ad8(lVar5);
                                        return 0xffffffff;
                                      }
                                      FUN_ffff00009077eb88(lVar5,0,0x50);
                                      FUN_ffff00009077e858(lVar5,lVar4 + 0x28,0x40);
                                      iVar1 = FUN_ffff0000907320fc
                                                        (s_md_build_time_ffff0000907d0fe7,lVar5,0x40
                                                        );
                                      if (iVar1 < 0) {
                                        pcVar6 = s__CCCI___s__d__add_tag_MD_BUILD_T_ffff0000907eaabb
                                        ;
                                        uVar8 = 0xb7;
                                        goto LAB_ffff00009072e7c4;
                                      }
                                      FUN_ffff00009077eb88(lVar5,0,0x50);
                                      FUN_ffff00009077e858(lVar5,lVar4 + 0x68,0x40);
                                      iVar1 = FUN_ffff0000907320fc
                                                        (s_md_build_ver_ffff0000907f4668,lVar5,0x40)
                                      ;
                                      if (iVar1 < 0) {
                                        pcVar6 = s__CCCI___s__d__add_tag_MD_BUILD_V_ffff0000907f7f64
                                        ;
                                        uVar8 = 0xc1;
                                        goto LAB_ffff00009072e7c4;
                                      }
                                      iVar1 = FUN_ffff0000907320fc
                                                        (s_md_capability_ffff0000907e913e,
                                                         lVar4 + 0x14,4);
                                      if (iVar1 < 0) {
                                        FUN_ffff00009072ff90
                                                  (
                                                  s__CCCI___s__d__add_tag_MD_BUILD_V_ffff0000907f7f64
                                                  ,s_add_md_inf_to_tag_ffff0000907e1101,0xc9);
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
                                          FUN_ffff00009072ff90
                                                    (
                                                  s__CCCI___s__d__Ivalid_MD_check_he_ffff0000907ddf36
                                                  ,s_compatible_for_legacy_ffff0000907f99a7,0x8c);
                                          goto LAB_ffff00009072e85c;
                                        }
                                        uVar8 = 0x11c;
                                      }
                                      iVar3 = FUN_ffff0000907320fc
                                                        (s_md1_chk_ffff0000907e7891,lVar4,uVar8);
                                      if (iVar3 == 0) {
                                        iVar3 = FUN_ffff0000907320fc
                                                          (s_md1img_ffff0000907f3dcc,lVar4 + 0xb0,4)
                                        ;
                                        if (iVar3 != 0) {
                                          FUN_ffff00009077de54
                                                    (
                                                  s__s__d___CCCI_add_tag_md1img_fail_ffff0000907f99bd
                                                  ,s_compatible_for_legacy_ffff0000907f99a7,0x96);
                                        }
                                      }
                                      else {
                                        FUN_ffff00009077de54
                                                  (
                                                  s__s__d___CCCI_add_tag_md_chk_hdr_f_ffff0000907e914c
                                                  ,s_compatible_for_legacy_ffff0000907f99a7,0x91);
                                      }
LAB_ffff00009072e85c:
                                      thunk_FUN_ffff0000907c2ad8(lVar5);
                                      if (iVar1 < 0) {
                                        return 0xffffffff;
                                      }
                                      FUN_ffff00009072f3cc
                                                (*(undefined4 *)(lVar4 + 0x14),
                                                 *(undefined4 *)(lVar4 + 0xb0),
                                                 *(undefined4 *)(lVar4 + 0xac));
                                      return 0;
                                    }
                                    pcVar6 = s__CCCI___s__d__Alloc_hdr_inf_buf_m_ffff0000907dc5f2;
                                    pcVar7 = s_add_md_inf_to_tag_ffff0000907e1101;
                                    uVar8 = 0xa4;
                                  }
                                }
                              }
                            }
                          }
                          else {
                            pcVar6 = s__CCCI___s__d__update_Ramdisk_fai_ffff0000907f115f;
                            pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
                            uVar8 = 0x173;
                          }
                        }
                        else {
                          pcVar6 = s__CCCI___s__d__update_DRDI_RT_fai_ffff0000907f2975;
                          pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
                          uVar8 = 0x169;
                        }
                      }
                      else {
                        pcVar6 = s__CCCI___s__d__update_DRDI_SRC_fa_ffff0000907e4763;
                        pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
                        uVar8 = 0x15f;
                      }
                      goto LAB_ffff00009072e328;
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
LAB_ffff00009072e6a4:
        pcVar6 = s__CCCI___s__d__update_md_padding_r_ffff0000907e5fcc;
        pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
        uVar8 = 0x155;
        uVar9 = (ulong)uVar11;
        goto LAB_ffff00009072e2d0;
      }
      if (iVar3 == 0) {
        pcVar6 = s__CCCI___s__d__DSP_size_info_abno_ffff000090802406;
        pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
        uVar8 = 0x144;
      }
      else if (iVar1 == 0) {
        pcVar6 = s__CCCI___s__d__DSP_offset_info_ab_ffff0000907f615a;
        pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
        uVar8 = 0x147;
      }
      else {
        iVar1 = FUN_ffff0000907304a0(iVar1,iVar3,0,2);
        if (-1 < iVar1) goto LAB_ffff00009072e360;
        pcVar6 = s__CCCI___s__d__update_DSP_reserve_ffff0000907ef80e;
        pcVar7 = s_ass_func_for_v6_normal_img_ffff0000907e7876;
        uVar8 = 0x14a;
      }
    }
LAB_ffff00009072e328:
    FUN_ffff00009072ff90(pcVar6,pcVar7,uVar8);
  }
  else {
    pcVar6 = s__CCCI___s__d__not_support_header_ffff0000907fee05;
    pcVar7 = s_ccci_main_img_parsing_ffff0000907f7eee;
    uVar8 = 0x1cb;
LAB_ffff00009072e2d0:
    FUN_ffff00009072ff90(pcVar6,pcVar7,uVar8,uVar9);
  }
  return 0xffffffff;
}



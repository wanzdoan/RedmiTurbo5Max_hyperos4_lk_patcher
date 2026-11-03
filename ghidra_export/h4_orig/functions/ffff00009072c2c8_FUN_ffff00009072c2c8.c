// ffff00009072c2c8  size=5020  FUN_ffff00009072c2c8

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009072c2c8(undefined8 param_1,undefined4 param_2)

{
  int *piVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  long lVar5;
  ulong *puVar6;
  char *pcVar7;
  uint uVar8;
  char *pcVar9;
  undefined8 uVar10;
  undefined8 uVar11;
  ulong *puVar12;
  ulong uVar13;
  ulong uVar14;
  long lVar15;
  uint uVar16;
  undefined4 *puVar17;
  ulong *puVar18;
  long local_78;
  ulong local_70;
  uint local_64;
  
  local_78 = 0;
  local_70 = 0;
  FUN_ffff00009077ee50(&local_78,0,0x10);
  lVar5 = FUN_ffff000090741bdc(s_md1_phy_cap_gear_ffff0000907fb610);
  if (lVar5 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI__sib_size__0_for_en_ffff0000907ef87a,s_smem_sib_init_ffff0000907e4736,
               0x324);
    local_70 = local_70 & 0xffffffff00000000;
LAB_ffff00009072c408:
    FUN_ffff00009077e11c
              (s__s__d___CCCI_MD_phy_capture_memo_ffff0000907d430e,s_smem_sib_init_ffff0000907e4736,
               0x338);
  }
  else {
    iVar4 = FUN_ffff00009077d72c();
    FUN_ffff00009077e11c
              (s__s__d___CCCI_env_sib_size__x__ffff0000907d2a1f,s_smem_sib_init_ffff0000907e4736,
               0x327,iVar4);
    uVar8 = iVar4 << 0x14;
    if (uVar8 < 0x60000001) {
      local_70 = CONCAT44(local_70._4_4_,uVar8);
      if (uVar8 == 0) goto LAB_ffff00009072c408;
    }
    else {
      uVar8 = 0x60000000;
      local_70 = CONCAT44(local_70._4_4_,0x60000000);
    }
    lVar5 = FUN_ffff00009072dc88(s_md1_sib_mem_ffff0000907ec394,uVar8,0);
    if (lVar5 == 0) {
      FUN_ffff000090730228
                (s__CCCI___s__d__allocate_MD_phy_ca_ffff0000907d0ef7,
                 s_smem_sib_init_ffff0000907e4736,0x331);
    }
    else {
      local_78 = lVar5;
      FUN_ffff000090732070();
      FUN_ffff00009077e11c
                (s__s__d___CCCI_md1_sib_addr___0x_l_ffff0000907df863,
                 s_smem_sib_init_ffff0000907e4736,0x336,local_78);
      iVar4 = FUN_ffff000090732394(s_md1_sib_info_ffff0000907ef8ac,&local_78,0x10);
      if (iVar4 < 0) {
        FUN_ffff000090730228
                  (s__CCCI___s__d__insert_md1_sib_add_ffff0000907e2a70,
                   s_smem_sib_init_ffff0000907e4736,0x33e);
      }
      FUN_ffff00009079861c(7,lVar5,local_70 & 0xffffffff,0xffffffff);
    }
  }
  local_64 = 0;
  lVar5 = FUN_ffff000090798490(&local_64);
  uVar8 = local_64;
  uVar13 = (ulong)local_64;
  if ((int)local_64 < 1) {
    pcVar7 = s__CCCI___s__d__ccci_get_nc_smem_r_ffff0000907fd1fc;
    pcVar9 = s_smem_noncacheable_init_ffff0000907dde5f;
    uVar10 = 0x28e;
  }
  else {
    uVar14 = 0;
    iVar4 = 0;
    uVar16 = 0;
    lVar15 = lVar5;
    do {
      if (*(code **)(lVar15 + 0x18) != (code *)0x0) {
        (**(code **)(lVar15 + 0x18))(param_1,param_2,lVar15);
      }
      iVar3 = *(int *)(lVar15 + 0xc);
      uVar2 = uVar16;
      if (iVar3 != 0) {
        uVar2 = (uVar16 + iVar3) - 1 & -iVar3;
      }
      FUN_ffff00009077e11c
                (s__s__d___CCCI_curr_offset_0x_x<_d_ffff0000907e7925,
                 s_smem_calc_and_update_ffff0000907cf586,0x215,uVar16,uVar14 & 0xffffffff,uVar2);
      if (uVar2 - uVar16 != 0) {
        iVar4 = iVar4 + 1;
        FUN_ffff00009077e11c
                  (s__s__d___CCCI_align_adjust_for__d_ffff0000907d5f88,
                   s_smem_calc_and_update_ffff0000907cf586,0x218,uVar14 & 0xffffffff,uVar2 - uVar16)
        ;
        uVar16 = uVar2;
      }
      piVar1 = (int *)(lVar15 + 8);
      *(uint *)(lVar15 + 4) = uVar16;
      *(uint *)(lVar15 + 0x14) = uVar16;
      uVar14 = uVar14 + 1;
      lVar15 = lVar15 + 0x20;
      uVar16 = *piVar1 + uVar16;
    } while (uVar13 != uVar14);
    uVar2 = iVar4 + uVar8;
    uVar13 = (ulong)uVar2;
    if (uVar2 != 0 && (int)uVar2 < 0 == SCARRY4(iVar4,uVar8)) {
      uVar8 = uVar16 + 0xffff & 0xffff0000;
      if (0x2000000 < uVar8) {
        pcVar7 = s__CCCI___s__d__MD_non_cache_memor_ffff0000907f9a5c;
        pcVar9 = s_smem_noncacheable_init_ffff0000907dde5f;
        uVar10 = 0x298;
        uVar11 = 0x2000000;
LAB_ffff00009072c55c:
        FUN_ffff000090730228(pcVar7,pcVar9,uVar10,uVar8,uVar11);
        return 0xffffffff;
      }
      local_64 = uVar2;
      puVar6 = (ulong *)FUN_ffff00009072da50(s_ap_md_nc_smem_ffff0000907e2a98,lVar5,uVar2,0,uVar8);
      if (puVar6 == (ulong *)0x0) {
        return 0xffffffff;
      }
      iVar4 = FUN_ffff000090732394(s_nc_smem_layout_ffff0000907ec3a0,puVar6,local_64 * 0x28);
      if (iVar4 < 0) {
        pcVar7 = s__CCCI___s__d__insert_nc_smem_lay_ffff0000907e6086;
        pcVar9 = s_smem_noncacheable_init_ffff0000907dde5f;
        uVar10 = 0x2a4;
      }
      else {
        iVar4 = FUN_ffff000090732394(s_nc_smem_layout_num_ffff0000907df887,&local_64,4);
        if (-1 < iVar4) {
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_NC_share_memory_bas_ffff0000907daa4b,
                     s_smem_noncacheable_init_ffff0000907dde5f,0x2af,*puVar6,uVar8);
          _DAT_ffff00009086d710 = local_64;
          _DAT_ffff00009086d700 = puVar6;
          FUN_ffff00009072dea4(*puVar6,uVar8);
          uVar13 = *puVar6;
          if ((uVar13 != 0) && ((uVar13 + 0x1ffffff & 0xfffffffffe000000) != uVar13)) {
LAB_ffff00009072ca90:
            FUN_ffff000090730228
                      (s__CCCI___s__d__addr_0x_llx__is_no_ffff0000907dde76,
                       s_md_smem_remapping_helper_ffff0000907dc65d,0x42,uVar13);
            return 0xffffffff;
          }
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_Map_0x_x_to_0x_llx_f_ffff0000907fb621,
                     s_md_smem_remapping_helper_ffff0000907dc65d,0x4d,0x40000000,uVar13);
          FUN_ffff000090731f90(uVar13,0);
          FUN_ffff000090731f90(uVar13 + 0x2000000,1);
          FUN_ffff000090731f90(uVar13 + 0x4000000,2);
          FUN_ffff000090731f90(uVar13 + 0x6000000,3);
          FUN_ffff000090731f90(uVar13 + 0x8000000,4);
          FUN_ffff000090731f90(uVar13 + 0xa000000,5);
          FUN_ffff000090731f90(uVar13 + 0xc000000,6);
          FUN_ffff000090731f90(uVar13 + 0xe000000,7);
          uVar16 = local_64;
          uVar13 = (ulong)local_64;
          _DAT_ffff00009086d76c = uVar8;
          _DAT_ffff00009086d780 = uVar8;
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_smem_info_ap_md1_sm_ffff0000907d2a3e,
                     s_nc_smem_legacy_compatible_ffff0000907d433c,0x9a,_DAT_ffff00009086d768);
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_smem_info_ap_md1_sm_ffff0000907e795d,
                     s_nc_smem_legacy_compatible_ffff0000907d433c,0x9b,_DAT_ffff00009086d76c);
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_smem_info_ap_md3_sm_ffff000090802464,
                     s_nc_smem_legacy_compatible_ffff0000907d433c,0x9c,_DAT_ffff00009086d770);
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_smem_info_ap_md3_sm_ffff0000908009a5,
                     s_nc_smem_legacy_compatible_ffff0000907d433c,0x9d,_DAT_ffff00009086d774);
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_smem_info_md1_md3_s_ffff0000907e2ad2,
                     s_nc_smem_legacy_compatible_ffff0000907d433c,0x9e,_DAT_ffff00009086d778);
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_smem_info_md1_md3_s_ffff0000907f9a9d,
                     s_nc_smem_legacy_compatible_ffff0000907d433c,0x9f,_DAT_ffff00009086d77c);
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_smem_info_total_sme_ffff0000907d2a6d,
                     s_nc_smem_legacy_compatible_ffff0000907d433c,0xa0,_DAT_ffff00009086d780);
          _DAT_ffff00009086d760 = *puVar6;
          if (uVar16 != 0) {
            puVar17 = (undefined4 *)((long)puVar6 + 0x14);
            do {
              uVar16 = puVar17[-1];
              if (uVar16 < 0x20 && (1 << (ulong)(uVar16 & 0x1f) & 0x88000500U) != 0) {
                if (uVar16 == DAT_ffff000090818f20) {
                  lVar5 = 0;
                }
                else if (uVar16 == DAT_ffff000090818f30) {
                  lVar5 = 1;
                }
                else if (uVar16 == DAT_ffff000090818f40) {
                  lVar5 = 2;
                }
                else if (uVar16 == DAT_ffff000090818f50) {
                  lVar5 = 3;
                }
                else if (uVar16 == DAT_ffff000090818f60) {
                  lVar5 = 4;
                }
                else if (uVar16 == DAT_ffff000090818f70) {
                  lVar5 = 5;
                }
                else {
                  if (uVar16 != DAT_ffff000090818f80) {
                    FUN_ffff000090730228
                              (s__CCCI___s__d__NC_smem_ID__d__not_ffff0000907d0f5a,
                               s_update_nc_smem_info_ext_ffff0000907ef8b9,0x83);
                    goto LAB_ffff00009072c844;
                  }
                  lVar5 = 6;
                }
                (&DAT_ffff000090818f14)[lVar5 * 4] = *puVar17;
                (&DAT_ffff000090818f18)[lVar5 * 4] = puVar17[4];
                (&DAT_ffff000090818f1c)[lVar5 * 4] = puVar17[1];
                FUN_ffff00009077e11c
                          (s__s__d___CCCI_NC_smem_ID__d__done_ffff0000907f2a24,
                           s_update_nc_smem_info_ext_ffff0000907ef8b9,0x81);
              }
LAB_ffff00009072c844:
              puVar17 = puVar17 + 10;
              uVar13 = uVar13 - 1;
            } while (uVar13 != 0);
          }
          iVar4 = FUN_ffff000090732394
                            (s_nc_smem_info_ext_ffff0000907e2b02,&DAT_ffff000090818f14,0x70);
          if (iVar4 < 0) {
            pcVar7 = s__CCCI___s__d__insert_nc_smem_inf_ffff0000907f46e6;
            pcVar9 = s_nc_smem_legacy_compatible_ffff0000907d433c;
            uVar10 = 0xad;
          }
          else {
            local_78 = CONCAT44(local_78._4_4_,7);
            iVar4 = FUN_ffff000090732394(s_nc_smem_info_ext_num_ffff0000907df89a,&local_78,4);
            if (iVar4 < 0) {
              FUN_ffff000090730228
                        (s__CCCI___s__d__insert_nc_smem_inf_ffff0000907d4356,
                         s_nc_smem_legacy_compatible_ffff0000907d433c,0xb3);
            }
            FUN_ffff000090730228
                      (s__CCCI___s__d__smem_info_base_add_ffff0000907df8af,
                       s_nc_smem_legacy_compatible_ffff0000907d433c,0xb5,
                       _DAT_ffff00009086d760 & 0xffffffff);
            FUN_ffff000090730228
                      (s__CCCI___s__d__smem_info_total_sm_ffff0000907e1129,
                       s_nc_smem_legacy_compatible_ffff0000907d433c,0xb6,_DAT_ffff00009086d780);
            iVar4 = FUN_ffff000090732394(s_smem_layout_ffff0000907ee04c,&DAT_ffff00009086d760,0x28);
            if (iVar4 < 0) {
              pcVar7 = s__CCCI___s__d__insert_smem_layout_ffff0000907f4712;
              pcVar9 = s_nc_smem_legacy_compatible_ffff0000907d433c;
              uVar10 = 0xb9;
            }
            else {
              if (local_64 != 0) {
                iVar4 = FUN_ffff00009079861c(10,*puVar6,uVar8,0xffffffff);
                if (iVar4 < 0) {
                  return 0xffffffff;
                }
                uVar13 = (ulong)local_64;
                if (local_64 != 0) {
                  puVar6 = puVar6 + 4;
                  do {
                    if (((((byte)*puVar6 >> 6 & 1) != 0) && ((int)puVar6[-2] == 0x16)) &&
                       (iVar4 = FUN_ffff00009079861c(8,puVar6[-4],(int)puVar6[-1],0xffffffff),
                       iVar4 < 0)) {
                      return 0xffffffff;
                    }
                    puVar6 = puVar6 + 5;
                    uVar13 = uVar13 - 1;
                  } while (uVar13 != 0);
                }
                local_64 = 0;
                lVar5 = FUN_ffff0000907984b0(&local_64);
                uVar8 = local_64;
                uVar13 = (ulong)local_64;
                if ((int)local_64 < 1) {
                  pcVar7 = s__CCCI___s__d__ccci_get_c_smem_re_ffff0000907d7823;
                  pcVar9 = s_smem_cacheable_init_ffff0000907d2a99;
                  uVar10 = 0x2d3;
                  goto LAB_ffff00009072c594;
                }
                uVar14 = 0;
                iVar4 = 0;
                uVar16 = 0;
                lVar15 = lVar5;
                do {
                  if (*(code **)(lVar15 + 0x18) != (code *)0x0) {
                    (**(code **)(lVar15 + 0x18))(param_1,param_2,lVar15);
                  }
                  iVar3 = *(int *)(lVar15 + 0xc);
                  uVar2 = uVar16;
                  if (iVar3 != 0) {
                    uVar2 = (uVar16 + iVar3) - 1 & -iVar3;
                  }
                  FUN_ffff00009077e11c
                            (s__s__d___CCCI_curr_offset_0x_x<_d_ffff0000907e7925,
                             s_smem_calc_and_update_ffff0000907cf586,0x215,uVar16,
                             uVar14 & 0xffffffff,uVar2);
                  if (uVar2 - uVar16 != 0) {
                    iVar4 = iVar4 + 1;
                    FUN_ffff00009077e11c
                              (s__s__d___CCCI_align_adjust_for__d_ffff0000907d5f88,
                               s_smem_calc_and_update_ffff0000907cf586,0x218,uVar14 & 0xffffffff,
                               uVar2 - uVar16);
                    uVar16 = uVar2;
                  }
                  piVar1 = (int *)(lVar15 + 8);
                  *(uint *)(lVar15 + 4) = uVar16;
                  *(uint *)(lVar15 + 0x14) = uVar16;
                  uVar14 = uVar14 + 1;
                  lVar15 = lVar15 + 0x20;
                  uVar16 = *piVar1 + uVar16;
                } while (uVar13 != uVar14);
                uVar2 = iVar4 + uVar8;
                uVar13 = (ulong)uVar2;
                if (uVar2 == 0 || (int)uVar2 < 0 != SCARRY4(iVar4,uVar8)) {
                  pcVar7 = s__CCCI___s__d__MD_cache_memory_ca_ffff0000907f4739;
                  pcVar9 = s_smem_cacheable_init_ffff0000907d2a99;
                  uVar10 = 0x2d9;
                  goto LAB_ffff00009072c594;
                }
                uVar8 = uVar16 + 0xffff & 0xffff0000;
                if (0xe000000 < uVar8) {
                  pcVar7 = s__CCCI___s__d__MD_cache_memory_si_ffff0000907fd238;
                  pcVar9 = s_smem_cacheable_init_ffff0000907d2a99;
                  uVar10 = 0x2dd;
                  uVar11 = 0xe000000;
                  goto LAB_ffff00009072c55c;
                }
                local_64 = uVar2;
                puVar6 = (ulong *)FUN_ffff00009072da50
                                            (s_ap_md_c_smem_ffff0000907d9165,lVar5,uVar2,0x2000000,
                                             uVar8);
                if (puVar6 == (ulong *)0x0) {
                  return 0xffffffff;
                }
                iVar4 = FUN_ffff000090732394
                                  (s_c_smem_layout_ffff0000907d5fb4,puVar6,local_64 * 0x28);
                if (iVar4 < 0) {
                  pcVar7 = s__CCCI___s__d__insert_c_smem_layo_ffff0000908009d2;
                  pcVar9 = s_smem_cacheable_init_ffff0000907d2a99;
                  uVar10 = 0x2e9;
                  goto LAB_ffff00009072ca68;
                }
                iVar4 = FUN_ffff000090732394(s_c_smem_layout_num_ffff0000907ddea3,&local_64,4);
                if (iVar4 < 0) {
                  pcVar7 = s__CCCI___s__d__insert_c_smem_layo_ffff0000907f2a61;
                  pcVar9 = s_smem_cacheable_init_ffff0000907d2a99;
                  uVar10 = 0x2ee;
                  goto LAB_ffff00009072ca68;
                }
                iVar4 = FUN_ffff000090732394(s_ccb_gear_id_ffff0000907f629b,&DAT_ffff00009086d6f8,4)
                ;
                if (iVar4 < 0) {
                  FUN_ffff000090730228
                            (s__CCCI___s__d__insert_ccb_gear_id_ffff0000907d4386,
                             s_smem_cacheable_init_ffff0000907d2a99,0x2f3);
                }
                FUN_ffff00009077e11c
                          (s__s__d___CCCI_C_share_memory_base_ffff0000907fef5b,
                           s_smem_cacheable_init_ffff0000907d2a99,0x2f6,*puVar6,uVar8);
                _DAT_ffff00009086d714 = local_64;
                _DAT_ffff00009086d708 = puVar6;
                FUN_ffff00009072deb0(*puVar6,uVar8);
                uVar13 = *puVar6;
                if ((uVar13 != 0) && ((uVar13 + 0x1ffffff & 0xfffffffffe000000) != uVar13))
                goto LAB_ffff00009072ca90;
                FUN_ffff00009077e11c
                          (s__s__d___CCCI_Map_0x_x_to_0x_llx_f_ffff0000907fb621,
                           s_md_smem_remapping_helper_ffff0000907dc65d,0x4d,0x42000000,uVar13);
                FUN_ffff000090731f90(uVar13,1);
                FUN_ffff000090731f90(uVar13 + 0x2000000,2);
                FUN_ffff000090731f90(uVar13 + 0x4000000,3);
                FUN_ffff000090731f90(uVar13 + 0x6000000,4);
                FUN_ffff000090731f90(uVar13 + 0x8000000,5);
                FUN_ffff000090731f90(uVar13 + 0xa000000,6);
                FUN_ffff000090731f90(uVar13 + 0xc000000,7);
                uVar13 = (ulong)local_64;
                if (local_64 != 0) {
                  puVar18 = puVar6 + 3;
                  do {
                    switch((int)puVar18[-1]) {
                    case 1:
                      if (DAT_ffff000090818f98 == 1) {
                        lVar5 = 0;
LAB_ffff00009072d130:
                        (&DAT_ffff000090818f88)[lVar5 * 3] = puVar18[-3];
                        (&DAT_ffff000090818f90)[lVar5 * 6] =
                             *(int *)((long)puVar18 + 0xc) + -0x2000000;
                        (&DAT_ffff000090818f94)[lVar5 * 6] = (int)*puVar18;
                        FUN_ffff00009077e11c
                                  (s__s__d___CCCI_C_smem_ID__d__done__ffff0000907ef8e8,
                                   s_update_csmem_layout_ffff0000907d2aad,0xe2,1);
                      }
                      else {
                        if (DAT_ffff000090818fb0 == 1) {
                          lVar5 = 1;
                          goto LAB_ffff00009072d130;
                        }
                        if (DAT_ffff000090818fc8 == 1) {
                          lVar5 = 2;
                          goto LAB_ffff00009072d130;
                        }
                        if (DAT_ffff000090818fe0 == 1) {
                          lVar5 = 3;
                          goto LAB_ffff00009072d130;
                        }
                        if (DAT_ffff000090818ff8 == 1) {
                          lVar5 = 4;
                          goto LAB_ffff00009072d130;
                        }
                        if (DAT_ffff000090819010 == 1) {
                          lVar5 = 5;
                          goto LAB_ffff00009072d130;
                        }
                        FUN_ffff000090730228
                                  (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec3af,
                                   s_update_csmem_layout_ffff0000907d2aad,0xe4,1);
                      }
                      _DAT_ffff00009086d788 = puVar18[-3];
                      _DAT_ffff00009086d790 = (undefined4)*puVar18;
                    default:
                      goto code_r0xffff00009072ce10;
                    case 0x16:
                      if (DAT_ffff000090818f98 == 0x16) {
                        lVar5 = 0;
                      }
                      else if (DAT_ffff000090818fb0 == 0x16) {
                        lVar5 = 1;
                      }
                      else if (DAT_ffff000090818fc8 == 0x16) {
                        lVar5 = 2;
                      }
                      else if (DAT_ffff000090818fe0 == 0x16) {
                        lVar5 = 3;
                      }
                      else if (DAT_ffff000090818ff8 == 0x16) {
                        lVar5 = 4;
                      }
                      else {
                        if (DAT_ffff000090819010 != 0x16) {
                          FUN_ffff000090730228
                                    (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec3af,
                                     s_update_csmem_layout_ffff0000907d2aad,0xe4,0x16);
                          goto code_r0xffff00009072ce10;
                        }
                        lVar5 = 5;
                      }
                      uVar14 = puVar18[-3];
                      puVar12 = &DAT_ffff000090818f88 + lVar5 * 3;
                      uVar10 = 0x16;
                      break;
                    case 0x18:
                      if (DAT_ffff000090818f98 == 0x18) {
                        lVar5 = 0;
                      }
                      else if (DAT_ffff000090818fb0 == 0x18) {
                        lVar5 = 1;
                      }
                      else if (DAT_ffff000090818fc8 == 0x18) {
                        lVar5 = 2;
                      }
                      else if (DAT_ffff000090818fe0 == 0x18) {
                        lVar5 = 3;
                      }
                      else if (DAT_ffff000090818ff8 == 0x18) {
                        lVar5 = 4;
                      }
                      else {
                        if (DAT_ffff000090819010 != 0x18) {
                          FUN_ffff000090730228
                                    (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec3af,
                                     s_update_csmem_layout_ffff0000907d2aad,0xe4,0x18);
                          goto code_r0xffff00009072ce10;
                        }
                        lVar5 = 5;
                      }
                      uVar14 = puVar18[-3];
                      puVar12 = &DAT_ffff000090818f88 + lVar5 * 3;
                      uVar10 = 0x18;
                      break;
                    case 0x1c:
                      if (DAT_ffff000090818f98 == 0x1c) {
                        lVar5 = 0;
                      }
                      else if (DAT_ffff000090818fb0 == 0x1c) {
                        lVar5 = 1;
                      }
                      else if (DAT_ffff000090818fc8 == 0x1c) {
                        lVar5 = 2;
                      }
                      else if (DAT_ffff000090818fe0 == 0x1c) {
                        lVar5 = 3;
                      }
                      else if (DAT_ffff000090818ff8 == 0x1c) {
                        lVar5 = 4;
                      }
                      else {
                        if (DAT_ffff000090819010 != 0x1c) {
                          FUN_ffff000090730228
                                    (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec3af,
                                     s_update_csmem_layout_ffff0000907d2aad,0xe4,0x1c);
                          goto code_r0xffff00009072ce10;
                        }
                        lVar5 = 5;
                      }
                      uVar14 = puVar18[-3];
                      puVar12 = &DAT_ffff000090818f88 + lVar5 * 3;
                      uVar10 = 0x1c;
                      break;
                    case 0x20:
                      if (DAT_ffff000090818f98 == 0x20) {
                        lVar5 = 0;
                      }
                      else if (DAT_ffff000090818fb0 == 0x20) {
                        lVar5 = 1;
                      }
                      else if (DAT_ffff000090818fc8 == 0x20) {
                        lVar5 = 2;
                      }
                      else if (DAT_ffff000090818fe0 == 0x20) {
                        lVar5 = 3;
                      }
                      else if (DAT_ffff000090818ff8 == 0x20) {
                        lVar5 = 4;
                      }
                      else {
                        if (DAT_ffff000090819010 != 0x20) {
                          FUN_ffff000090730228
                                    (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec3af,
                                     s_update_csmem_layout_ffff0000907d2aad,0xe4,0x20);
                          goto code_r0xffff00009072ce10;
                        }
                        lVar5 = 5;
                      }
                      uVar14 = puVar18[-3];
                      puVar12 = &DAT_ffff000090818f88 + lVar5 * 3;
                      uVar10 = 0x20;
                      break;
                    case 0x27:
                      if (DAT_ffff000090818f98 == 0x27) {
                        lVar5 = 0;
                      }
                      else if (DAT_ffff000090818fb0 == 0x27) {
                        lVar5 = 1;
                      }
                      else if (DAT_ffff000090818fc8 == 0x27) {
                        lVar5 = 2;
                      }
                      else if (DAT_ffff000090818fe0 == 0x27) {
                        lVar5 = 3;
                      }
                      else if (DAT_ffff000090818ff8 == 0x27) {
                        lVar5 = 4;
                      }
                      else {
                        if (DAT_ffff000090819010 != 0x27) {
                          FUN_ffff000090730228
                                    (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec3af,
                                     s_update_csmem_layout_ffff0000907d2aad,0xe4,0x27);
                          goto code_r0xffff00009072ce10;
                        }
                        lVar5 = 5;
                      }
                      uVar14 = puVar18[-3];
                      puVar12 = &DAT_ffff000090818f88 + lVar5 * 3;
                      uVar10 = 0x27;
                    }
                    *puVar12 = uVar14;
                    *(int *)(puVar12 + 1) = *(int *)((long)puVar18 + 0xc) + -0x2000000;
                    *(int *)((long)puVar12 + 0xc) = (int)*puVar18;
                    FUN_ffff00009077e11c
                              (s__s__d___CCCI_C_smem_ID__d__done__ffff0000907ef8e8,
                               s_update_csmem_layout_ffff0000907d2aad,0xe2,uVar10);
code_r0xffff00009072ce10:
                    puVar18 = puVar18 + 5;
                    uVar13 = uVar13 - 1;
                  } while (uVar13 != 0);
                }
                _DAT_ffff00009086d798 = *puVar6;
                _DAT_ffff00009086d7a0 = 0x2000000;
                _DAT_ffff00009086d7a8 = 6;
                _DAT_ffff00009086d7a4 = uVar8;
                FUN_ffff000090730228
                          (s__CCCI___s__d__cache_buffer_addr__ffff0000907cf5ac,
                           s_c_smem_legacy_compatible_ffff0000907cf5d1,0x128);
                FUN_ffff000090730228
                          (s__CCCI___s__d__cache_buffer_size__ffff0000907df8d6,
                           s_c_smem_legacy_compatible_ffff0000907cf5d1,0x129,_DAT_ffff00009086d7a4);
                FUN_ffff000090730228
                          (s__CCCI___s__d__cache_buffer_itm_c_ffff0000908009fb,
                           s_c_smem_legacy_compatible_ffff0000907cf5d1,0x12a,_DAT_ffff00009086d7a8);
                iVar4 = FUN_ffff000090732394
                                  (s_md1_bank4_cache_info_ffff0000907d5fc2,&DAT_ffff00009086d798,
                                   0x18);
                if (iVar4 < 0) {
                  FUN_ffff000090730228
                            (s__CCCI___s__d__insert_md1_smem_ca_ffff0000907ee058,
                             s_add_c_smem_to_tag_inf_ffff0000907f4769,0xf3);
                }
                iVar4 = FUN_ffff000090732394
                                  (s_md1_bank4_cache_layout_ffff0000907e4772,&DAT_ffff000090818f88,
                                   0x90);
                if (iVar4 < 0) {
                  FUN_ffff000090730228
                            (s__CCCI___s__d__insert_md1_smem_ca_ffff0000907e2b13,
                             s_add_c_smem_to_tag_inf_ffff0000907f4769,0xf6);
                }
                local_78 = CONCAT44(local_78._4_4_,0x2000000);
                iVar4 = FUN_ffff000090732394(s_md1_smem_cahce_offset_ffff0000907d5fd7,&local_78,4);
                if (iVar4 < 0) {
                  FUN_ffff000090730228
                            (s__CCCI___s__d__insert_md1_smem_ca_ffff0000907e2b44,
                             s_add_c_smem_to_tag_inf_ffff0000907f4769,0xfc);
                }
                iVar4 = FUN_ffff000090732394(s_ccb_info_ffff0000907e4789,&DAT_ffff00009086d788,0x10)
                ;
                if (iVar4 < 0) {
                  FUN_ffff000090730228
                            (s__CCCI___s__d__insert_ccb_info_fa_ffff0000907daa83,
                             s_add_c_smem_to_tag_inf_ffff0000907f4769,0xff);
                }
                if (local_64 != 0) {
                  iVar4 = FUN_ffff00009079861c(9,*puVar6,uVar8,0xffffffff);
                  if (iVar4 < 0) {
                    return 0xffffffff;
                  }
                  uVar13 = (ulong)local_64;
                  if (local_64 != 0) {
                    puVar6 = puVar6 + 4;
                    do {
                      if (((((byte)*puVar6 >> 6 & 1) != 0) && ((int)puVar6[-2] == 0x16)) &&
                         (iVar4 = FUN_ffff00009079861c(8,puVar6[-4],(int)puVar6[-1],0xffffffff),
                         iVar4 < 0)) {
                        return 0xffffffff;
                      }
                      puVar6 = puVar6 + 5;
                      uVar13 = uVar13 - 1;
                    } while (uVar13 != 0);
                  }
                  local_78 = CONCAT44(local_78._4_4_,1);
                  if ((_DAT_ffff00009086d728 != 0) && (_DAT_ffff00009086d730 != 0)) {
                    FUN_ffff00009077e11c
                              (s__s__d___CCCI_mtee_base_addr__0x__ffff0000907dde36,
                               s_smem_others_init_ffff0000907fef92,0x38c);
                    iVar4 = FUN_ffff000090732394(s_mtee_support_ffff0000907d5fed,&local_78,4);
                    uVar8 = _DAT_ffff00009086d730;
                    uVar13 = _DAT_ffff00009086d728;
                    if (iVar4 < 0) {
                      FUN_ffff000090730228
                                (s__CCCI___s__d__insert_mtee_suppor_ffff000090800a23,
                                 s_smem_others_init_ffff0000907fef92,0x38f);
                    }
                    else if (_DAT_ffff00009086d730 != 0) {
                      if ((_DAT_ffff00009086d728 == 0) ||
                         ((_DAT_ffff00009086d728 + 0x1ffffff & 0xfffffffffe000000) ==
                          _DAT_ffff00009086d728)) {
                        FUN_ffff00009077e11c
                                  (s__s__d___CCCI_Map_0x_x_to_0x_llx_f_ffff0000907fb621,
                                   s_md_smem_remapping_helper_ffff0000907dc65d,0x4d,0x4a000000,
                                   _DAT_ffff00009086d728);
                        uVar16 = uVar8 >> 0x19;
                        FUN_ffff000090731f90(uVar13,5);
                        if ((uVar16 != 0) &&
                           (((FUN_ffff000090731f90(uVar13 + 0x2000000,6), uVar16 != 1 &&
                             (FUN_ffff000090731f90(uVar13 + 0x4000000,7), 0x6000000 < uVar8)) &&
                            (uVar16 != 2)))) {
                          FUN_ffff000090730228
                                    (s__CCCI___s__d__Size_abnormal_0x_x_ffff0000907d9110,
                                     s_md_smem_remapping_helper_ffff0000907dc65d,0x55,uVar8,5);
                        }
                      }
                      else {
                        FUN_ffff000090730228
                                  (s__CCCI___s__d__addr_0x_llx__is_no_ffff0000907dde76,
                                   s_md_smem_remapping_helper_ffff0000907dc65d,0x42,
                                   _DAT_ffff00009086d728);
                      }
                    }
                  }
                  DAT_ffff00009086d6fc = 1;
                  FUN_ffff00009072c0a4();
                  return 0;
                }
              }
              pcVar7 = s__CCCI___s__d__rt_smem_table_num_i_ffff0000907d913f;
              pcVar9 = s_config_smem_common_mpu_ffff0000907ef8d1;
              uVar10 = 0x277;
            }
          }
          FUN_ffff000090730228(pcVar7,pcVar9,uVar10);
          return 0xffffffff;
        }
        pcVar7 = s__CCCI___s__d__insert_nc_smem_lay_ffff0000907e4744;
        pcVar9 = s_smem_noncacheable_init_ffff0000907dde5f;
        uVar10 = 0x2a9;
      }
LAB_ffff00009072ca68:
      FUN_ffff000090730228(pcVar7,pcVar9,uVar10);
      thunk_FUN_ffff0000907c314c(puVar6);
      return 0xffffffff;
    }
    pcVar7 = s__CCCI___s__d__MD_non_cache_memor_ffff000090800971;
    pcVar9 = s_smem_noncacheable_init_ffff0000907dde5f;
    uVar10 = 0x294;
  }
LAB_ffff00009072c594:
  FUN_ffff000090730228(pcVar7,pcVar9,uVar10,uVar13);
  return 0xffffffff;
}



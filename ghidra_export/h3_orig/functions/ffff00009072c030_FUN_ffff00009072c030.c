// ffff00009072c030  size=5020  FUN_ffff00009072c030

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009072c030(undefined8 param_1,undefined4 param_2)

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
  FUN_ffff00009077eb88(&local_78,0,0x10);
  lVar5 = FUN_ffff000090741944(s_md1_phy_cap_gear_ffff0000907fb474);
  if (lVar5 == 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI__sib_size__0_for_en_ffff0000907ef709,s_smem_sib_init_ffff0000907e4632,
               0x324);
    local_70 = local_70 & 0xffffffff00000000;
LAB_ffff00009072c170:
    FUN_ffff00009077de54
              (s__s__d___CCCI_MD_phy_capture_memo_ffff0000907d42d1,s_smem_sib_init_ffff0000907e4632,
               0x338);
  }
  else {
    iVar4 = FUN_ffff00009077d464();
    FUN_ffff00009077de54
              (s__s__d___CCCI_env_sib_size__x__ffff0000907d29e2,s_smem_sib_init_ffff0000907e4632,
               0x327,iVar4);
    uVar8 = iVar4 << 0x14;
    if (uVar8 < 0x60000001) {
      local_70 = CONCAT44(local_70._4_4_,uVar8);
      if (uVar8 == 0) goto LAB_ffff00009072c170;
    }
    else {
      uVar8 = 0x60000000;
      local_70 = CONCAT44(local_70._4_4_,0x60000000);
    }
    lVar5 = FUN_ffff00009072d9f0(s_md1_sib_mem_ffff0000907ec211,uVar8,0);
    if (lVar5 == 0) {
      FUN_ffff00009072ff90
                (s__CCCI___s__d__allocate_MD_phy_ca_ffff0000907d0eba,
                 s_smem_sib_init_ffff0000907e4632,0x331);
    }
    else {
      local_78 = lVar5;
      FUN_ffff000090731dd8();
      FUN_ffff00009077de54
                (s__s__d___CCCI_md1_sib_addr___0x_l_ffff0000907df7c0,
                 s_smem_sib_init_ffff0000907e4632,0x336,local_78);
      iVar4 = FUN_ffff0000907320fc(s_md1_sib_info_ffff0000907ef73b,&local_78,0x10);
      if (iVar4 < 0) {
        FUN_ffff00009072ff90
                  (s__CCCI___s__d__insert_md1_sib_add_ffff0000907e299f,
                   s_smem_sib_init_ffff0000907e4632,0x33e);
      }
      FUN_ffff00009079835c(7,lVar5,local_70 & 0xffffffff,0xffffffff);
    }
  }
  local_64 = 0;
  lVar5 = FUN_ffff0000907981d0(&local_64);
  uVar8 = local_64;
  uVar13 = (ulong)local_64;
  if ((int)local_64 < 1) {
    pcVar7 = s__CCCI___s__d__ccci_get_nc_smem_r_ffff0000907fd054;
    pcVar9 = s_smem_noncacheable_init_ffff0000907dddbc;
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
      FUN_ffff00009077de54
                (s__s__d___CCCI_curr_offset_0x_x<_d_ffff0000907e77e1,
                 s_smem_calc_and_update_ffff0000907cf567,0x215,uVar16,uVar14 & 0xffffffff,uVar2);
      if (uVar2 - uVar16 != 0) {
        iVar4 = iVar4 + 1;
        FUN_ffff00009077de54
                  (s__s__d___CCCI_align_adjust_for__d_ffff0000907d5ef2,
                   s_smem_calc_and_update_ffff0000907cf567,0x218,uVar14 & 0xffffffff,uVar2 - uVar16)
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
        pcVar7 = s__CCCI___s__d__MD_non_cache_memor_ffff0000907f98c0;
        pcVar9 = s_smem_noncacheable_init_ffff0000907dddbc;
        uVar10 = 0x298;
        uVar11 = 0x2000000;
LAB_ffff00009072c2c4:
        FUN_ffff00009072ff90(pcVar7,pcVar9,uVar10,uVar8,uVar11);
        return 0xffffffff;
      }
      local_64 = uVar2;
      puVar6 = (ulong *)FUN_ffff00009072d7b8(s_ap_md_nc_smem_ffff0000907e29c7,lVar5,uVar2,0,uVar8);
      if (puVar6 == (ulong *)0x0) {
        return 0xffffffff;
      }
      iVar4 = FUN_ffff0000907320fc(s_nc_smem_layout_ffff0000907ec21d,puVar6,local_64 * 0x28);
      if (iVar4 < 0) {
        pcVar7 = s__CCCI___s__d__insert_nc_smem_lay_ffff0000907e5f72;
        pcVar9 = s_smem_noncacheable_init_ffff0000907dddbc;
        uVar10 = 0x2a4;
      }
      else {
        iVar4 = FUN_ffff0000907320fc(s_nc_smem_layout_num_ffff0000907df7e4,&local_64,4);
        if (-1 < iVar4) {
          FUN_ffff00009077de54
                    (s__s__d___CCCI_NC_share_memory_bas_ffff0000907da940,
                     s_smem_noncacheable_init_ffff0000907dddbc,0x2af,*puVar6,uVar8);
          _DAT_ffff00009086d678 = local_64;
          _DAT_ffff00009086d668 = puVar6;
          FUN_ffff00009072dc0c(*puVar6,uVar8);
          uVar13 = *puVar6;
          if ((uVar13 != 0) && ((uVar13 + 0x1ffffff & 0xfffffffffe000000) != uVar13)) {
LAB_ffff00009072c7f8:
            FUN_ffff00009072ff90
                      (s__CCCI___s__d__addr_0x_llx__is_no_ffff0000907dddd3,
                       s_md_smem_remapping_helper_ffff0000907dc587,0x42,uVar13);
            return 0xffffffff;
          }
          FUN_ffff00009077de54
                    (s__s__d___CCCI_Map_0x_x_to_0x_llx_f_ffff0000907fb485,
                     s_md_smem_remapping_helper_ffff0000907dc587,0x4d,0x40000000,uVar13);
          FUN_ffff000090731cf8(uVar13,0);
          FUN_ffff000090731cf8(uVar13 + 0x2000000,1);
          FUN_ffff000090731cf8(uVar13 + 0x4000000,2);
          FUN_ffff000090731cf8(uVar13 + 0x6000000,3);
          FUN_ffff000090731cf8(uVar13 + 0x8000000,4);
          FUN_ffff000090731cf8(uVar13 + 0xa000000,5);
          FUN_ffff000090731cf8(uVar13 + 0xc000000,6);
          FUN_ffff000090731cf8(uVar13 + 0xe000000,7);
          uVar16 = local_64;
          uVar13 = (ulong)local_64;
          _DAT_ffff00009086d6d4 = uVar8;
          _DAT_ffff00009086d6e8 = uVar8;
          FUN_ffff00009077de54
                    (s__s__d___CCCI_smem_info_ap_md1_sm_ffff0000907d2a01,
                     s_nc_smem_legacy_compatible_ffff0000907d42ff,0x9a,_DAT_ffff00009086d6d0);
          FUN_ffff00009077de54
                    (s__s__d___CCCI_smem_info_ap_md1_sm_ffff0000907e7819,
                     s_nc_smem_legacy_compatible_ffff0000907d42ff,0x9b,_DAT_ffff00009086d6d4);
          FUN_ffff00009077de54
                    (s__s__d___CCCI_smem_info_ap_md3_sm_ffff000090802260,
                     s_nc_smem_legacy_compatible_ffff0000907d42ff,0x9c,_DAT_ffff00009086d6d8);
          FUN_ffff00009077de54
                    (s__s__d___CCCI_smem_info_ap_md3_sm_ffff0000908007c9,
                     s_nc_smem_legacy_compatible_ffff0000907d42ff,0x9d,_DAT_ffff00009086d6dc);
          FUN_ffff00009077de54
                    (s__s__d___CCCI_smem_info_md1_md3_s_ffff0000907e2a01,
                     s_nc_smem_legacy_compatible_ffff0000907d42ff,0x9e,_DAT_ffff00009086d6e0);
          FUN_ffff00009077de54
                    (s__s__d___CCCI_smem_info_md1_md3_s_ffff0000907f9901,
                     s_nc_smem_legacy_compatible_ffff0000907d42ff,0x9f,_DAT_ffff00009086d6e4);
          FUN_ffff00009077de54
                    (s__s__d___CCCI_smem_info_total_sme_ffff0000907d2a30,
                     s_nc_smem_legacy_compatible_ffff0000907d42ff,0xa0,_DAT_ffff00009086d6e8);
          _DAT_ffff00009086d6c8 = *puVar6;
          if (uVar16 != 0) {
            puVar17 = (undefined4 *)((long)puVar6 + 0x14);
            do {
              uVar16 = puVar17[-1];
              if (uVar16 < 0x20 && (1 << (ulong)(uVar16 & 0x1f) & 0x88000500U) != 0) {
                if (uVar16 == DAT_ffff000090818f00) {
                  lVar5 = 0;
                }
                else if (uVar16 == DAT_ffff000090818f10) {
                  lVar5 = 1;
                }
                else if (uVar16 == DAT_ffff000090818f20) {
                  lVar5 = 2;
                }
                else if (uVar16 == DAT_ffff000090818f30) {
                  lVar5 = 3;
                }
                else if (uVar16 == DAT_ffff000090818f40) {
                  lVar5 = 4;
                }
                else if (uVar16 == DAT_ffff000090818f50) {
                  lVar5 = 5;
                }
                else {
                  if (uVar16 != DAT_ffff000090818f60) {
                    FUN_ffff00009072ff90
                              (s__CCCI___s__d__NC_smem_ID__d__not_ffff0000907d0f1d,
                               s_update_nc_smem_info_ext_ffff0000907ef748,0x83);
                    goto LAB_ffff00009072c5ac;
                  }
                  lVar5 = 6;
                }
                (&DAT_ffff000090818ef4)[lVar5 * 4] = *puVar17;
                (&DAT_ffff000090818ef8)[lVar5 * 4] = puVar17[4];
                (&DAT_ffff000090818efc)[lVar5 * 4] = puVar17[1];
                FUN_ffff00009077de54
                          (s__s__d___CCCI_NC_smem_ID__d__done_ffff0000907f2898,
                           s_update_nc_smem_info_ext_ffff0000907ef748,0x81);
              }
LAB_ffff00009072c5ac:
              puVar17 = puVar17 + 10;
              uVar13 = uVar13 - 1;
            } while (uVar13 != 0);
          }
          iVar4 = FUN_ffff0000907320fc
                            (s_nc_smem_info_ext_ffff0000907e2a31,&DAT_ffff000090818ef4,0x70);
          if (iVar4 < 0) {
            pcVar7 = s__CCCI___s__d__insert_nc_smem_inf_ffff0000907f4579;
            pcVar9 = s_nc_smem_legacy_compatible_ffff0000907d42ff;
            uVar10 = 0xad;
          }
          else {
            local_78 = CONCAT44(local_78._4_4_,7);
            iVar4 = FUN_ffff0000907320fc(s_nc_smem_info_ext_num_ffff0000907df7f7,&local_78,4);
            if (iVar4 < 0) {
              FUN_ffff00009072ff90
                        (s__CCCI___s__d__insert_nc_smem_inf_ffff0000907d4319,
                         s_nc_smem_legacy_compatible_ffff0000907d42ff,0xb3);
            }
            FUN_ffff00009072ff90
                      (s__CCCI___s__d__smem_info_base_add_ffff0000907df80c,
                       s_nc_smem_legacy_compatible_ffff0000907d42ff,0xb5,
                       _DAT_ffff00009086d6c8 & 0xffffffff);
            FUN_ffff00009072ff90
                      (s__CCCI___s__d__smem_info_total_sm_ffff0000907e1058,
                       s_nc_smem_legacy_compatible_ffff0000907d42ff,0xb6,_DAT_ffff00009086d6e8);
            iVar4 = FUN_ffff0000907320fc(s_smem_layout_ffff0000907ededb,&DAT_ffff00009086d6c8,0x28);
            if (iVar4 < 0) {
              pcVar7 = s__CCCI___s__d__insert_smem_layout_ffff0000907f45a5;
              pcVar9 = s_nc_smem_legacy_compatible_ffff0000907d42ff;
              uVar10 = 0xb9;
            }
            else {
              if (local_64 != 0) {
                iVar4 = FUN_ffff00009079835c(10,*puVar6,uVar8,0xffffffff);
                if (iVar4 < 0) {
                  return 0xffffffff;
                }
                uVar13 = (ulong)local_64;
                if (local_64 != 0) {
                  puVar6 = puVar6 + 4;
                  do {
                    if (((((byte)*puVar6 >> 6 & 1) != 0) && ((int)puVar6[-2] == 0x16)) &&
                       (iVar4 = FUN_ffff00009079835c(8,puVar6[-4],(int)puVar6[-1],0xffffffff),
                       iVar4 < 0)) {
                      return 0xffffffff;
                    }
                    puVar6 = puVar6 + 5;
                    uVar13 = uVar13 - 1;
                  } while (uVar13 != 0);
                }
                local_64 = 0;
                lVar5 = FUN_ffff0000907981f0(&local_64);
                uVar8 = local_64;
                uVar13 = (ulong)local_64;
                if ((int)local_64 < 1) {
                  pcVar7 = s__CCCI___s__d__ccci_get_c_smem_re_ffff0000907d778d;
                  pcVar9 = s_smem_cacheable_init_ffff0000907d2a5c;
                  uVar10 = 0x2d3;
                  goto LAB_ffff00009072c2fc;
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
                  FUN_ffff00009077de54
                            (s__s__d___CCCI_curr_offset_0x_x<_d_ffff0000907e77e1,
                             s_smem_calc_and_update_ffff0000907cf567,0x215,uVar16,
                             uVar14 & 0xffffffff,uVar2);
                  if (uVar2 - uVar16 != 0) {
                    iVar4 = iVar4 + 1;
                    FUN_ffff00009077de54
                              (s__s__d___CCCI_align_adjust_for__d_ffff0000907d5ef2,
                               s_smem_calc_and_update_ffff0000907cf567,0x218,uVar14 & 0xffffffff,
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
                  pcVar7 = s__CCCI___s__d__MD_cache_memory_ca_ffff0000907f45cc;
                  pcVar9 = s_smem_cacheable_init_ffff0000907d2a5c;
                  uVar10 = 0x2d9;
                  goto LAB_ffff00009072c2fc;
                }
                uVar8 = uVar16 + 0xffff & 0xffff0000;
                if (0xe000000 < uVar8) {
                  pcVar7 = s__CCCI___s__d__MD_cache_memory_si_ffff0000907fd090;
                  pcVar9 = s_smem_cacheable_init_ffff0000907d2a5c;
                  uVar10 = 0x2dd;
                  uVar11 = 0xe000000;
                  goto LAB_ffff00009072c2c4;
                }
                local_64 = uVar2;
                puVar6 = (ulong *)FUN_ffff00009072d7b8
                                            (s_ap_md_c_smem_ffff0000907d90ce,lVar5,uVar2,0x2000000,
                                             uVar8);
                if (puVar6 == (ulong *)0x0) {
                  return 0xffffffff;
                }
                iVar4 = FUN_ffff0000907320fc
                                  (s_c_smem_layout_ffff0000907d5f1e,puVar6,local_64 * 0x28);
                if (iVar4 < 0) {
                  pcVar7 = s__CCCI___s__d__insert_c_smem_layo_ffff0000908007f6;
                  pcVar9 = s_smem_cacheable_init_ffff0000907d2a5c;
                  uVar10 = 0x2e9;
                  goto LAB_ffff00009072c7d0;
                }
                iVar4 = FUN_ffff0000907320fc(s_c_smem_layout_num_ffff0000907dde00,&local_64,4);
                if (iVar4 < 0) {
                  pcVar7 = s__CCCI___s__d__insert_c_smem_layo_ffff0000907f28d5;
                  pcVar9 = s_smem_cacheable_init_ffff0000907d2a5c;
                  uVar10 = 0x2ee;
                  goto LAB_ffff00009072c7d0;
                }
                iVar4 = FUN_ffff0000907320fc(s_ccb_gear_id_ffff0000907f6111,&DAT_ffff00009086d660,4)
                ;
                if (iVar4 < 0) {
                  FUN_ffff00009072ff90
                            (s__CCCI___s__d__insert_ccb_gear_id_ffff0000907d4349,
                             s_smem_cacheable_init_ffff0000907d2a5c,0x2f3);
                }
                FUN_ffff00009077de54
                          (s__s__d___CCCI_C_share_memory_base_ffff0000907fedb3,
                           s_smem_cacheable_init_ffff0000907d2a5c,0x2f6,*puVar6,uVar8);
                _DAT_ffff00009086d67c = local_64;
                _DAT_ffff00009086d670 = puVar6;
                FUN_ffff00009072dc18(*puVar6,uVar8);
                uVar13 = *puVar6;
                if ((uVar13 != 0) && ((uVar13 + 0x1ffffff & 0xfffffffffe000000) != uVar13))
                goto LAB_ffff00009072c7f8;
                FUN_ffff00009077de54
                          (s__s__d___CCCI_Map_0x_x_to_0x_llx_f_ffff0000907fb485,
                           s_md_smem_remapping_helper_ffff0000907dc587,0x4d,0x42000000,uVar13);
                FUN_ffff000090731cf8(uVar13,1);
                FUN_ffff000090731cf8(uVar13 + 0x2000000,2);
                FUN_ffff000090731cf8(uVar13 + 0x4000000,3);
                FUN_ffff000090731cf8(uVar13 + 0x6000000,4);
                FUN_ffff000090731cf8(uVar13 + 0x8000000,5);
                FUN_ffff000090731cf8(uVar13 + 0xa000000,6);
                FUN_ffff000090731cf8(uVar13 + 0xc000000,7);
                uVar13 = (ulong)local_64;
                if (local_64 != 0) {
                  puVar18 = puVar6 + 3;
                  do {
                    switch((int)puVar18[-1]) {
                    case 1:
                      if (DAT_ffff000090818f78 == 1) {
                        lVar5 = 0;
LAB_ffff00009072ce98:
                        (&DAT_ffff000090818f68)[lVar5 * 3] = puVar18[-3];
                        (&DAT_ffff000090818f70)[lVar5 * 6] =
                             *(int *)((long)puVar18 + 0xc) + -0x2000000;
                        (&DAT_ffff000090818f74)[lVar5 * 6] = (int)*puVar18;
                        FUN_ffff00009077de54
                                  (s__s__d___CCCI_C_smem_ID__d__done__ffff0000907ef777,
                                   s_update_csmem_layout_ffff0000907d2a70,0xe2,1);
                      }
                      else {
                        if (DAT_ffff000090818f90 == 1) {
                          lVar5 = 1;
                          goto LAB_ffff00009072ce98;
                        }
                        if (DAT_ffff000090818fa8 == 1) {
                          lVar5 = 2;
                          goto LAB_ffff00009072ce98;
                        }
                        if (DAT_ffff000090818fc0 == 1) {
                          lVar5 = 3;
                          goto LAB_ffff00009072ce98;
                        }
                        if (DAT_ffff000090818fd8 == 1) {
                          lVar5 = 4;
                          goto LAB_ffff00009072ce98;
                        }
                        if (DAT_ffff000090818ff0 == 1) {
                          lVar5 = 5;
                          goto LAB_ffff00009072ce98;
                        }
                        FUN_ffff00009072ff90
                                  (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec22c,
                                   s_update_csmem_layout_ffff0000907d2a70,0xe4,1);
                      }
                      _DAT_ffff00009086d6f0 = puVar18[-3];
                      _DAT_ffff00009086d6f8 = (undefined4)*puVar18;
                    default:
                      goto code_r0xffff00009072cb78;
                    case 0x16:
                      if (DAT_ffff000090818f78 == 0x16) {
                        lVar5 = 0;
                      }
                      else if (DAT_ffff000090818f90 == 0x16) {
                        lVar5 = 1;
                      }
                      else if (DAT_ffff000090818fa8 == 0x16) {
                        lVar5 = 2;
                      }
                      else if (DAT_ffff000090818fc0 == 0x16) {
                        lVar5 = 3;
                      }
                      else if (DAT_ffff000090818fd8 == 0x16) {
                        lVar5 = 4;
                      }
                      else {
                        if (DAT_ffff000090818ff0 != 0x16) {
                          FUN_ffff00009072ff90
                                    (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec22c,
                                     s_update_csmem_layout_ffff0000907d2a70,0xe4,0x16);
                          goto code_r0xffff00009072cb78;
                        }
                        lVar5 = 5;
                      }
                      uVar14 = puVar18[-3];
                      puVar12 = &DAT_ffff000090818f68 + lVar5 * 3;
                      uVar10 = 0x16;
                      break;
                    case 0x18:
                      if (DAT_ffff000090818f78 == 0x18) {
                        lVar5 = 0;
                      }
                      else if (DAT_ffff000090818f90 == 0x18) {
                        lVar5 = 1;
                      }
                      else if (DAT_ffff000090818fa8 == 0x18) {
                        lVar5 = 2;
                      }
                      else if (DAT_ffff000090818fc0 == 0x18) {
                        lVar5 = 3;
                      }
                      else if (DAT_ffff000090818fd8 == 0x18) {
                        lVar5 = 4;
                      }
                      else {
                        if (DAT_ffff000090818ff0 != 0x18) {
                          FUN_ffff00009072ff90
                                    (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec22c,
                                     s_update_csmem_layout_ffff0000907d2a70,0xe4,0x18);
                          goto code_r0xffff00009072cb78;
                        }
                        lVar5 = 5;
                      }
                      uVar14 = puVar18[-3];
                      puVar12 = &DAT_ffff000090818f68 + lVar5 * 3;
                      uVar10 = 0x18;
                      break;
                    case 0x1c:
                      if (DAT_ffff000090818f78 == 0x1c) {
                        lVar5 = 0;
                      }
                      else if (DAT_ffff000090818f90 == 0x1c) {
                        lVar5 = 1;
                      }
                      else if (DAT_ffff000090818fa8 == 0x1c) {
                        lVar5 = 2;
                      }
                      else if (DAT_ffff000090818fc0 == 0x1c) {
                        lVar5 = 3;
                      }
                      else if (DAT_ffff000090818fd8 == 0x1c) {
                        lVar5 = 4;
                      }
                      else {
                        if (DAT_ffff000090818ff0 != 0x1c) {
                          FUN_ffff00009072ff90
                                    (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec22c,
                                     s_update_csmem_layout_ffff0000907d2a70,0xe4,0x1c);
                          goto code_r0xffff00009072cb78;
                        }
                        lVar5 = 5;
                      }
                      uVar14 = puVar18[-3];
                      puVar12 = &DAT_ffff000090818f68 + lVar5 * 3;
                      uVar10 = 0x1c;
                      break;
                    case 0x20:
                      if (DAT_ffff000090818f78 == 0x20) {
                        lVar5 = 0;
                      }
                      else if (DAT_ffff000090818f90 == 0x20) {
                        lVar5 = 1;
                      }
                      else if (DAT_ffff000090818fa8 == 0x20) {
                        lVar5 = 2;
                      }
                      else if (DAT_ffff000090818fc0 == 0x20) {
                        lVar5 = 3;
                      }
                      else if (DAT_ffff000090818fd8 == 0x20) {
                        lVar5 = 4;
                      }
                      else {
                        if (DAT_ffff000090818ff0 != 0x20) {
                          FUN_ffff00009072ff90
                                    (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec22c,
                                     s_update_csmem_layout_ffff0000907d2a70,0xe4,0x20);
                          goto code_r0xffff00009072cb78;
                        }
                        lVar5 = 5;
                      }
                      uVar14 = puVar18[-3];
                      puVar12 = &DAT_ffff000090818f68 + lVar5 * 3;
                      uVar10 = 0x20;
                      break;
                    case 0x27:
                      if (DAT_ffff000090818f78 == 0x27) {
                        lVar5 = 0;
                      }
                      else if (DAT_ffff000090818f90 == 0x27) {
                        lVar5 = 1;
                      }
                      else if (DAT_ffff000090818fa8 == 0x27) {
                        lVar5 = 2;
                      }
                      else if (DAT_ffff000090818fc0 == 0x27) {
                        lVar5 = 3;
                      }
                      else if (DAT_ffff000090818fd8 == 0x27) {
                        lVar5 = 4;
                      }
                      else {
                        if (DAT_ffff000090818ff0 != 0x27) {
                          FUN_ffff00009072ff90
                                    (s__CCCI___s__d__C_smem_ID__d__not_f_ffff0000907ec22c,
                                     s_update_csmem_layout_ffff0000907d2a70,0xe4,0x27);
                          goto code_r0xffff00009072cb78;
                        }
                        lVar5 = 5;
                      }
                      uVar14 = puVar18[-3];
                      puVar12 = &DAT_ffff000090818f68 + lVar5 * 3;
                      uVar10 = 0x27;
                    }
                    *puVar12 = uVar14;
                    *(int *)(puVar12 + 1) = *(int *)((long)puVar18 + 0xc) + -0x2000000;
                    *(int *)((long)puVar12 + 0xc) = (int)*puVar18;
                    FUN_ffff00009077de54
                              (s__s__d___CCCI_C_smem_ID__d__done__ffff0000907ef777,
                               s_update_csmem_layout_ffff0000907d2a70,0xe2,uVar10);
code_r0xffff00009072cb78:
                    puVar18 = puVar18 + 5;
                    uVar13 = uVar13 - 1;
                  } while (uVar13 != 0);
                }
                _DAT_ffff00009086d700 = *puVar6;
                _DAT_ffff00009086d708 = 0x2000000;
                _DAT_ffff00009086d710 = 6;
                _DAT_ffff00009086d70c = uVar8;
                FUN_ffff00009072ff90
                          (s__CCCI___s__d__cache_buffer_addr__ffff0000907cf58d,
                           s_c_smem_legacy_compatible_ffff0000907cf5b2,0x128);
                FUN_ffff00009072ff90
                          (s__CCCI___s__d__cache_buffer_size__ffff0000907df833,
                           s_c_smem_legacy_compatible_ffff0000907cf5b2,0x129,_DAT_ffff00009086d70c);
                FUN_ffff00009072ff90
                          (s__CCCI___s__d__cache_buffer_itm_c_ffff00009080081f,
                           s_c_smem_legacy_compatible_ffff0000907cf5b2,0x12a,_DAT_ffff00009086d710);
                iVar4 = FUN_ffff0000907320fc
                                  (s_md1_bank4_cache_info_ffff0000907d5f2c,&DAT_ffff00009086d700,
                                   0x18);
                if (iVar4 < 0) {
                  FUN_ffff00009072ff90
                            (s__CCCI___s__d__insert_md1_smem_ca_ffff0000907edee7,
                             s_add_c_smem_to_tag_inf_ffff0000907f45fc,0xf3);
                }
                iVar4 = FUN_ffff0000907320fc
                                  (s_md1_bank4_cache_layout_ffff0000907e466e,&DAT_ffff000090818f68,
                                   0x90);
                if (iVar4 < 0) {
                  FUN_ffff00009072ff90
                            (s__CCCI___s__d__insert_md1_smem_ca_ffff0000907e2a42,
                             s_add_c_smem_to_tag_inf_ffff0000907f45fc,0xf6);
                }
                local_78 = CONCAT44(local_78._4_4_,0x2000000);
                iVar4 = FUN_ffff0000907320fc(s_md1_smem_cahce_offset_ffff0000907d5f41,&local_78,4);
                if (iVar4 < 0) {
                  FUN_ffff00009072ff90
                            (s__CCCI___s__d__insert_md1_smem_ca_ffff0000907e2a73,
                             s_add_c_smem_to_tag_inf_ffff0000907f45fc,0xfc);
                }
                iVar4 = FUN_ffff0000907320fc(s_ccb_info_ffff0000907e4685,&DAT_ffff00009086d6f0,0x10)
                ;
                if (iVar4 < 0) {
                  FUN_ffff00009072ff90
                            (s__CCCI___s__d__insert_ccb_info_fa_ffff0000907da978,
                             s_add_c_smem_to_tag_inf_ffff0000907f45fc,0xff);
                }
                if (local_64 != 0) {
                  iVar4 = FUN_ffff00009079835c(9,*puVar6,uVar8,0xffffffff);
                  if (iVar4 < 0) {
                    return 0xffffffff;
                  }
                  uVar13 = (ulong)local_64;
                  if (local_64 != 0) {
                    puVar6 = puVar6 + 4;
                    do {
                      if (((((byte)*puVar6 >> 6 & 1) != 0) && ((int)puVar6[-2] == 0x16)) &&
                         (iVar4 = FUN_ffff00009079835c(8,puVar6[-4],(int)puVar6[-1],0xffffffff),
                         iVar4 < 0)) {
                        return 0xffffffff;
                      }
                      puVar6 = puVar6 + 5;
                      uVar13 = uVar13 - 1;
                    } while (uVar13 != 0);
                  }
                  local_78 = CONCAT44(local_78._4_4_,1);
                  if ((_DAT_ffff00009086d690 != 0) && (_DAT_ffff00009086d698 != 0)) {
                    FUN_ffff00009077de54
                              (s__s__d___CCCI_mtee_base_addr__0x__ffff0000907ddd93,
                               s_smem_others_init_ffff0000907fedea,0x38c);
                    iVar4 = FUN_ffff0000907320fc(s_mtee_support_ffff0000907d5f57,&local_78,4);
                    uVar8 = _DAT_ffff00009086d698;
                    uVar13 = _DAT_ffff00009086d690;
                    if (iVar4 < 0) {
                      FUN_ffff00009072ff90
                                (s__CCCI___s__d__insert_mtee_suppor_ffff000090800847,
                                 s_smem_others_init_ffff0000907fedea,0x38f);
                    }
                    else if (_DAT_ffff00009086d698 != 0) {
                      if ((_DAT_ffff00009086d690 == 0) ||
                         ((_DAT_ffff00009086d690 + 0x1ffffff & 0xfffffffffe000000) ==
                          _DAT_ffff00009086d690)) {
                        FUN_ffff00009077de54
                                  (s__s__d___CCCI_Map_0x_x_to_0x_llx_f_ffff0000907fb485,
                                   s_md_smem_remapping_helper_ffff0000907dc587,0x4d,0x4a000000,
                                   _DAT_ffff00009086d690);
                        uVar16 = uVar8 >> 0x19;
                        FUN_ffff000090731cf8(uVar13,5);
                        if ((uVar16 != 0) &&
                           (((FUN_ffff000090731cf8(uVar13 + 0x2000000,6), uVar16 != 1 &&
                             (FUN_ffff000090731cf8(uVar13 + 0x4000000,7), 0x6000000 < uVar8)) &&
                            (uVar16 != 2)))) {
                          FUN_ffff00009072ff90
                                    (s__CCCI___s__d__Size_abnormal_0x_x_ffff0000907d9079,
                                     s_md_smem_remapping_helper_ffff0000907dc587,0x55,uVar8,5);
                        }
                      }
                      else {
                        FUN_ffff00009072ff90
                                  (s__CCCI___s__d__addr_0x_llx__is_no_ffff0000907dddd3,
                                   s_md_smem_remapping_helper_ffff0000907dc587,0x42,
                                   _DAT_ffff00009086d690);
                      }
                    }
                  }
                  DAT_ffff00009086d664 = 1;
                  FUN_ffff00009072be0c();
                  return 0;
                }
              }
              pcVar7 = s__CCCI___s__d__rt_smem_table_num_i_ffff0000907d90a8;
              pcVar9 = s_config_smem_common_mpu_ffff0000907ef760;
              uVar10 = 0x277;
            }
          }
          FUN_ffff00009072ff90(pcVar7,pcVar9,uVar10);
          return 0xffffffff;
        }
        pcVar7 = s__CCCI___s__d__insert_nc_smem_lay_ffff0000907e4640;
        pcVar9 = s_smem_noncacheable_init_ffff0000907dddbc;
        uVar10 = 0x2a9;
      }
LAB_ffff00009072c7d0:
      FUN_ffff00009072ff90(pcVar7,pcVar9,uVar10);
      thunk_FUN_ffff0000907c2ad8(puVar6);
      return 0xffffffff;
    }
    pcVar7 = s__CCCI___s__d__MD_non_cache_memor_ffff000090800795;
    pcVar9 = s_smem_noncacheable_init_ffff0000907dddbc;
    uVar10 = 0x294;
  }
LAB_ffff00009072c2fc:
  FUN_ffff00009072ff90(pcVar7,pcVar9,uVar10,uVar13);
  return 0xffffffff;
}




undefined8 FUN_ffff0000907471f4(undefined8 param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  int iVar5;
  int iVar6;
  undefined4 uVar7;
  long lVar8;
  uint *puVar9;
  char *pcVar10;
  undefined8 uVar11;
  long lVar12;
  ulong uVar13;
  int *piVar14;
  uint uVar15;
  uint uVar16;
  ulong uVar17;
  ulong uVar18;
  long lVar19;
  ulong uVar20;
  int iVar21;
  long lVar22;
  uint uVar23;
  long lVar24;
  long local_a8;
  long lStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  uint local_84;
  ulong local_80 [4];
  
  local_98 = 0;
  uStack_90 = 0;
  local_a8 = 0;
  lStack_a0 = 0;
  local_84 = 0;
  iVar5 = FUN_ffff00009076da24(param_1,s__soc_connsys_emi_config_ffff0000907d9540);
  if ((iVar5 < 0) &&
     (iVar5 = FUN_ffff00009076da24(param_1,s__connsys_emi_config_ffff0000907dcae9), iVar5 < 0)) {
    FUN_ffff00009077e11c
              (s___s___s_offset_is_not_found_ffff0000907d6439,
               s_connsys_get_fdt_node_ffff0000907de446,s__connsys_emi_config_ffff0000907dcae9);
    FUN_ffff00009077e11c(s__soc_connsys_emi_config_not_foun_ffff0000907f9fcf);
  }
  else {
    iVar6 = FUN_ffff00009071c708();
    if (iVar6 == 0) {
      local_80[0] = local_80[0] & 0xffffffff00000000;
      lVar8 = FUN_ffff00009076ddb4(param_1,iVar5,s_emi_max_addr_ffff0000907d7d05,local_80);
      if (lVar8 == 0) {
        FUN_ffff00009077e11c
                  (s___s___s_is_not_found_ffff0000907e6545,s_connsys_get_fdt_prop_ffff0000907e314c,
                   s_emi_max_addr_ffff0000907d7d05);
        DAT_ffff0000908776b8 = 0;
      }
      else {
        uVar15 = (*(uint *)(lVar8 + 0xc) & 0xff00ff00) >> 8 |
                 (*(uint *)(lVar8 + 0xc) & 0xff00ff) << 8;
        DAT_ffff0000908776b8 = (ulong)(uVar15 >> 0x10 | uVar15 << 0x10);
      }
      local_80[0] = local_80[0] & 0xffffffff00000000;
      lVar8 = FUN_ffff00009076ddb4(param_1,iVar5,s_emi_min_addr_ffff0000907ee51e,local_80);
      if (lVar8 == 0) {
        FUN_ffff00009077e11c
                  (s___s___s_is_not_found_ffff0000907e6545,s_connsys_get_fdt_prop_ffff0000907e314c,
                   s_emi_min_addr_ffff0000907ee51e);
        DAT_ffff0000908776c0 = 0;
      }
      else {
        uVar15 = (*(uint *)(lVar8 + 0xc) & 0xff00ff00) >> 8 |
                 (*(uint *)(lVar8 + 0xc) & 0xff00ff) << 8;
        DAT_ffff0000908776c0 = (ulong)(uVar15 >> 0x10 | uVar15 << 0x10);
      }
    }
    else {
      DAT_ffff0000908776c0 = 0x250000000;
      DAT_ffff0000908776b8 = 0x29fffffff;
    }
    local_80[0] = local_80[0] & 0xffffffff00000000;
    lVar8 = FUN_ffff00009076ddb4(param_1,iVar5,s_emi_alignment_ffff0000907fd860,local_80);
    if (lVar8 == 0) {
      FUN_ffff00009077e11c
                (s___s___s_is_not_found_ffff0000907e6545,s_connsys_get_fdt_prop_ffff0000907e314c,
                 s_emi_alignment_ffff0000907fd860);
LAB_ffff0000907473a4:
      DAT_ffff0000908776e8 = 0x1000000;
    }
    else {
      uVar15 = *(uint *)(lVar8 + 0xc);
      if (uVar15 == 0) goto LAB_ffff0000907473a4;
      uVar15 = (uVar15 & 0xff00ff00) >> 8 | (uVar15 & 0xff00ff) << 8;
      DAT_ffff0000908776e8 = uVar15 >> 0x10 | uVar15 << 0x10;
    }
    FUN_ffff00009077e11c
              (s___s__alloc_max_min_addr___llX____ffff0000907f9ff2,
               s_connsys_load_emi_settings_ffff0000907d48d3,DAT_ffff0000908776b8,
               DAT_ffff0000908776c0);
    puVar9 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar5,s_emi_layout_region_ffff0000907e3161,&local_84);
    FUN_ffff00009077e11c
              (s___s__emi_layout_len___d__ffff0000907e9798,
               s_connsys_load_emi_settings_ffff0000907d48d3,local_84);
    uVar15 = local_84;
    if ((puVar9 != (uint *)0x0) && (0 < (int)local_84)) {
      uVar4 = local_84 >> 2;
      FUN_ffff00009077e11c
                (s___s__items___d____d__ffff0000907e97b2,
                 s_connsys_load_emi_settings_ffff0000907d48d3,uVar4,local_84 >> 2 & 1);
      if ((uVar4 & 1) == 0) {
        DAT_ffff0000908776cc = uVar15 >> 3;
        DAT_ffff0000908776d0 = FUN_ffff00009077d514((ulong)DAT_ffff0000908776cc * 0x18);
        if (DAT_ffff0000908776d0 != 0) {
          if (DAT_ffff0000908776cc != 0) {
            uVar20 = 0;
            lVar8 = 0xc;
            do {
              uVar15 = (puVar9[1] & 0xff00ff00) >> 8 | (puVar9[1] & 0xff00ff) << 8;
              uVar15 = uVar15 >> 0x10 | uVar15 << 0x10;
              uVar4 = (*puVar9 & 0xff00ff00) >> 8 | (*puVar9 & 0xff00ff) << 8;
              FUN_ffff00009077e11c
                        (s___s__idx___d__vals___x___x__ffff0000907f1764,
                         s_connsys_load_emi_settings_ffff0000907d48d3,uVar20 & 0xffffffff,
                         uVar4 >> 0x10 | uVar4 << 0x10,uVar15);
              uVar20 = uVar20 + 1;
              *(uint *)(DAT_ffff0000908776d0 + lVar8) = uVar15;
              lVar8 = lVar8 + 0x18;
              DAT_ffff0000908776c8 = DAT_ffff0000908776c8 + uVar15;
              puVar9 = puVar9 + 2;
            } while (uVar20 < DAT_ffff0000908776cc);
          }
          goto LAB_ffff0000907474c4;
        }
        pcVar10 = s___s__malloc_fail_for_g_layout_re_ffff000090800eb3;
LAB_ffff000090747e00:
        FUN_ffff00009077e11c(pcVar10,s_connsys_load_emi_settings_ffff0000907d48d3);
        uVar11 = 0xfffffffb;
      }
      else {
LAB_ffff00009074755c:
        uVar11 = 0xfffffff9;
      }
LAB_ffff000090747e10:
      FUN_ffff00009077e11c
                (s___s__load_emi_seting_fail_ret____ffff0000907ec91e,
                 s_platform_load_connsys_ffff0000907f9fb9,uVar11);
      goto LAB_ffff000090747e28;
    }
LAB_ffff0000907474c4:
    FUN_ffff00009077e11c
              (s___s__total_size___d__ffff0000907d1550,s_connsys_load_emi_settings_ffff0000907d48d3,
               DAT_ffff0000908776c8);
    puVar9 = (uint *)FUN_ffff00009076df9c
                               (param_1,iVar5,s_emi_alloc_block_ffff0000907ee52b,&local_84);
    FUN_ffff00009077e11c
              (s___s__emi_alloc_region_len___d__ffff0000907e655b,
               s_connsys_load_emi_settings_ffff0000907d48d3,local_84);
    uVar15 = local_84;
    if ((puVar9 != (uint *)0x0) && (0 < (int)local_84)) {
      uVar4 = (local_84 >> 2) % 3;
      FUN_ffff00009077e11c
                (s___s__items___d____d__ffff0000907e97b2,
                 s_connsys_load_emi_settings_ffff0000907d48d3,local_84 >> 2,uVar4);
      if (uVar4 != 0) goto LAB_ffff00009074755c;
      DAT_ffff0000908776d8 = uVar15 / 0xc;
      DAT_ffff0000908776e0 = FUN_ffff00009077d514(((ulong)uVar15 / 0xc) * 0xa0);
      if (DAT_ffff0000908776e0 == 0) {
        pcVar10 = s___s__malloc_fail_for_g_alloc_blo_ffff0000907d6456;
        goto LAB_ffff000090747e00;
      }
      FUN_ffff00009077ee50(DAT_ffff0000908776e0,0,(ulong)DAT_ffff0000908776d8 * 0xa0);
      if (DAT_ffff0000908776d8 != 0) {
        lVar8 = 0;
        uVar20 = 0;
        do {
          uVar15 = (*puVar9 & 0xff00ff00) >> 8 | (*puVar9 & 0xff00ff) << 8;
          uVar15 = uVar15 >> 0x10 | uVar15 << 0x10;
          uVar4 = (puVar9[1] & 0xff00ff00) >> 8 | (puVar9[1] & 0xff00ff) << 8;
          uVar4 = uVar4 >> 0x10 | uVar4 << 0x10;
          uVar23 = (puVar9[2] & 0xff00ff00) >> 8 | (puVar9[2] & 0xff00ff) << 8;
          uVar23 = uVar23 >> 0x10 | uVar23 << 0x10;
          FUN_ffff00009077e11c
                    (s___s__idx___d__vals___x___x___x__ffff0000907f85e1,
                     s_connsys_load_emi_settings_ffff0000907d48d3,uVar20 & 0xffffffff,uVar15,uVar4,
                     uVar23);
          uVar11 = 0xfffffff9;
          if ((DAT_ffff0000908776cc <= uVar15) || (DAT_ffff0000908776cc <= uVar4))
          goto LAB_ffff000090747e10;
          uVar20 = uVar20 + 1;
          puVar9 = puVar9 + 3;
          lVar19 = DAT_ffff0000908776e0 + lVar8;
          lVar8 = lVar8 + 0xa0;
          *(uint *)(lVar19 + 0x80) = uVar15;
          *(uint *)(lVar19 + 0x84) = uVar4;
          *(uint *)(lVar19 + 0x90) = uVar23;
        } while (uVar20 < DAT_ffff0000908776d8);
      }
    }
    lVar8 = FUN_ffff00009076df9c(param_1,iVar5,s_emi_alloc_block_name_ffff0000907e97c8,&local_84);
    if ((0 < (int)local_84) && (lVar8 != 0)) {
      lVar19 = 0;
      uVar20 = 0xffffffffffffffff;
      do {
        FUN_ffff00009077e11c
                  (s___s__emi_alloc_name_len___d____s_ffff0000907e184e,
                   s_connsys_load_emi_settings_ffff0000907d48d3,local_84,lVar8);
        uVar20 = uVar20 + 1;
        if (DAT_ffff0000908776d8 <= uVar20) goto LAB_ffff00009074755c;
        FUN_ffff00009077f2ac(DAT_ffff0000908776e0 + lVar19,lVar8,0x7f);
        iVar6 = FUN_ffff00009077f250(lVar8);
        local_84 = local_84 - (iVar6 + 1);
        if (lVar8 == 0) break;
        lVar19 = lVar19 + 0xa0;
        lVar8 = lVar8 + (iVar6 + 1);
      } while (0 < (int)local_84);
    }
    lVar8 = FUN_ffff00009076df9c(param_1,iVar5,s_emi_mpu_region_ffff0000907fd86e,&local_84);
    FUN_ffff00009077e11c
              (s___s__emi_mpu_len___d__ffff0000907f1781,s_connsys_load_emi_settings_ffff0000907d48d3
               ,local_84);
    uVar15 = local_84;
    if ((lVar8 != 0) && (0 < (int)local_84)) {
      FUN_ffff00009077e11c
                (s___s__MPU_items___d____d__ffff0000907d48ed,
                 s_connsys_load_emi_settings_ffff0000907d48d3,local_84 >> 2,(local_84 >> 2) % 3);
      DAT_ffff0000908776ec = uVar15 / 0xc;
      DAT_ffff0000908776f0 = FUN_ffff00009077d514(((ulong)uVar15 / 0xc) * 0xc);
      if (DAT_ffff0000908776f0 == 0) {
        pcVar10 = s___s__malloc_fail_for_g_mpu_regio_ffff0000907db09b;
        goto LAB_ffff000090747e00;
      }
      FUN_ffff00009077ee50(DAT_ffff0000908776f0,0,(ulong)DAT_ffff0000908776ec * 0xc);
      if (DAT_ffff0000908776ec != 0) {
        lVar19 = 0;
        uVar20 = 0;
        do {
          puVar9 = (uint *)(lVar8 + lVar19);
          uVar15 = (*puVar9 & 0xff00ff00) >> 8 | (*puVar9 & 0xff00ff) << 8;
          uVar15 = uVar15 >> 0x10 | uVar15 << 0x10;
          uVar4 = (puVar9[1] & 0xff00ff00) >> 8 | (puVar9[1] & 0xff00ff) << 8;
          uVar4 = uVar4 >> 0x10 | uVar4 << 0x10;
          uVar23 = (puVar9[2] & 0xff00ff00) >> 8 | (puVar9[2] & 0xff00ff) << 8;
          uVar23 = uVar23 >> 0x10 | uVar23 << 0x10;
          FUN_ffff00009077e11c
                    (s___s__idx___d__vals___x___x___x__ffff0000907f85e1,
                     s_connsys_load_emi_settings_ffff0000907d48d3,uVar20 & 0xffffffff,uVar15,uVar4,
                     uVar23);
          uVar11 = 0xfffffff9;
          if ((DAT_ffff0000908776cc <= uVar15) || (DAT_ffff0000908776cc <= uVar4))
          goto LAB_ffff000090747e10;
          uVar20 = uVar20 + 1;
          puVar9 = (uint *)(DAT_ffff0000908776f0 + lVar19);
          lVar19 = lVar19 + 0xc;
          puVar9[1] = uVar15;
          puVar9[2] = uVar4;
          *puVar9 = uVar23;
        } while (uVar20 < DAT_ffff0000908776ec);
      }
    }
  }
  if (PTR_FUN_ffff00009081a6f8 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a6f8)(param_1);
  }
  if (PTR_FUN_ffff00009081a708 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a708)(param_1);
  }
  if (PTR_FUN_ffff00009081a718 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a718)(param_1);
  }
  if (PTR_FUN_ffff00009081a728 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a728)(param_1);
  }
  if (PTR_FUN_ffff00009081a738 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a738)(param_1);
  }
  if (DAT_ffff0000908776d8 == 0) {
    lVar8 = 0;
  }
  else {
    uVar20 = 0;
    lVar19 = 0;
    lVar8 = 0;
    do {
      lVar22 = DAT_ffff0000908776e0;
      lVar24 = DAT_ffff0000908776e0 + uVar20 * 0xa0;
      uVar15 = *(uint *)(lVar24 + 0x80);
      uVar4 = *(uint *)(lVar24 + 0x84);
      if (uVar4 < uVar15) {
        uVar23 = 0;
      }
      else {
        uVar23 = uVar15 + 1;
        uVar3 = uVar23;
        if (uVar23 <= uVar4 + 1) {
          uVar3 = uVar4 + 1;
        }
        uVar3 = uVar3 - uVar15;
        if (uVar3 < 2) {
LAB_ffff000090747948:
          uVar23 = 0;
          uVar13 = (ulong)uVar15;
        }
        else {
          if (uVar23 <= uVar4 + 1) {
            uVar23 = uVar4 + 1;
          }
          if (uVar23 - 1 < uVar15) goto LAB_ffff000090747948;
          uVar16 = uVar3 & 0xfffffffe;
          iVar5 = 0;
          iVar6 = 0;
          uVar13 = (ulong)(uVar15 + uVar16);
          uVar23 = uVar16;
          do {
            uVar2 = uVar15 + 1;
            uVar18 = (ulong)uVar15;
            uVar23 = uVar23 - 2;
            uVar15 = uVar15 + 2;
            iVar5 = *(int *)(DAT_ffff0000908776d0 + uVar18 * 0x18 + 0xc) + iVar5;
            iVar6 = *(int *)(DAT_ffff0000908776d0 + (ulong)uVar2 * 0x18 + 0xc) + iVar6;
          } while (uVar23 != 0);
          uVar23 = iVar6 + iVar5;
          if (uVar3 == uVar16) goto LAB_ffff000090747968;
        }
        do {
          lVar12 = uVar13 * 0x18;
          uVar15 = (int)uVar13 + 1;
          uVar13 = (ulong)uVar15;
          uVar23 = *(int *)(DAT_ffff0000908776d0 + lVar12 + 0xc) + uVar23;
        } while (uVar15 <= uVar4);
      }
LAB_ffff000090747968:
      FUN_ffff00009077e11c
                (s___s__region__d__name___s__region_ffff0000907f8580,
                 s_platform_load_connsys_ffff0000907f9fb9,uVar20 & 0xffffffff,lVar24,uVar23);
      puVar9 = (uint *)(lVar22 + uVar20 * 0xa0 + 0x88);
      *puVar9 = uVar23;
      if (uVar23 != 0) {
        lVar12 = DAT_ffff0000908776e0 + uVar20 * 0xa0;
        FUN_ffff00009077e11c
                  (s___s__alloc_idx___d____s____d__>__ffff0000907efe49,
                   s_platform_load_connsys_ffff0000907f9fb9,uVar20 & 0xffffffff,lVar12,
                   *(undefined4 *)(lVar12 + 0x80),*(undefined4 *)(lVar12 + 0x84),
                   *(undefined4 *)(lVar12 + 0x90));
        if (uVar20 == 0) {
          lVar8 = FUN_ffff00009071a09c
                            (DAT_ffff0000908776c8,DAT_ffff0000908776e8,DAT_ffff0000908776c0,
                             DAT_ffff0000908776b8,0,*(undefined4 *)(lVar22 + 0x90),lVar24);
          if (lVar8 == 0) {
            FUN_ffff00009077e11c
                      (s___s__allocation_failed_ffff0000907e4da4,
                       s_platform_load_connsys_ffff0000907f9fb9);
            goto LAB_ffff000090747e28;
          }
          uVar7 = FUN_ffff00009071a424(lVar8,DAT_ffff0000908776c8,*puVar9);
          FUN_ffff00009077e11c
                    (s___s__mblock_resize__0x_x__>_0x_x_ffff0000907f85ac,
                     s_platform_load_connsys_ffff0000907f9fb9,DAT_ffff0000908776c8,*puVar9,lVar8,
                     uVar7);
          *(long *)(lVar22 + 0x98) = lVar8;
          lVar19 = lVar8;
        }
        else {
          lVar12 = FUN_ffff0000907198dc
                             (*puVar9,DAT_ffff0000908776e8,DAT_ffff0000908776b8,lVar19,
                              *(undefined4 *)(lVar22 + uVar20 * 0xa0 + 0x90),lVar24);
          if (lVar12 == 0) {
            FUN_ffff00009077e11c
                      (s___s__allocation___d___s__failed_ffff0000907e7d06,
                       s_platform_load_connsys_ffff0000907f9fb9,uVar20 & 0xffffffff,lVar24);
            goto LAB_ffff000090747e28;
          }
          uVar15 = *puVar9;
          *(long *)(lVar22 + uVar20 * 0xa0 + 0x98) = lVar19;
          FUN_ffff00009077e11c
                    (s___s__mblock_allocate___d___s__pa_ffff0000907efe77,
                     s_platform_load_connsys_ffff0000907f9fb9,uVar20 & 0xffffffff,lVar24,lVar19,
                     uVar15);
        }
        lVar19 = lVar19 + (ulong)*puVar9;
      }
      uVar20 = uVar20 + 1;
    } while (uVar20 < DAT_ffff0000908776d8);
  }
  if (DAT_ffff0000908776cc != 0) {
    lVar19 = 0;
    uVar20 = 0;
    lVar22 = 0;
    do {
      lVar24 = DAT_ffff0000908776d0 + lVar19;
      *(long *)(lVar24 + 0x10) = lVar22 + lVar8;
      lVar22 = lVar22 + (ulong)*(uint *)(lVar24 + 0xc);
      FUN_ffff00009077e11c
                (s___s__fill___d__addr___08lX__ffff0000907fd843,
                 s_platform_load_connsys_ffff0000907f9fb9,uVar20 & 0xffffffff);
      uVar20 = uVar20 + 1;
      lVar19 = lVar19 + 0x18;
    } while (uVar20 < DAT_ffff0000908776cc);
  }
  if (PTR_FUN_ffff00009081a700 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a700)(param_1);
  }
  if (PTR_FUN_ffff00009081a710 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a710)(param_1);
  }
  if (PTR_FUN_ffff00009081a720 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a720)(param_1);
  }
  if (PTR_FUN_ffff00009081a730 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a730)(param_1);
  }
  if (PTR_FUN_ffff00009081a740 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a740)(param_1);
  }
  if (DAT_ffff0000908776ec != 0) {
    uVar20 = 0;
    do {
      lVar8 = DAT_ffff0000908776f0 + uVar20 * 0xc;
      uVar15 = *(uint *)(lVar8 + 4);
      uVar13 = (ulong)uVar15;
      uVar4 = *(uint *)(lVar8 + 8);
      if (uVar4 < uVar15) {
        uVar23 = 0;
      }
      else {
        uVar23 = uVar15 + 1;
        uVar3 = uVar23;
        if (uVar23 <= uVar4 + 1) {
          uVar3 = uVar4 + 1;
        }
        uVar3 = uVar3 - uVar15;
        if (uVar3 < 2) {
LAB_ffff000090747c6c:
          uVar23 = 0;
          uVar16 = uVar15;
        }
        else {
          if (uVar23 <= uVar4 + 1) {
            uVar23 = uVar4 + 1;
          }
          if (uVar23 - 1 < uVar15) goto LAB_ffff000090747c6c;
          uVar2 = uVar3 & 0xfffffffe;
          iVar5 = 0;
          iVar6 = 0;
          uVar16 = uVar15 + uVar2;
          uVar23 = uVar2;
          do {
            uVar1 = uVar15 + 1;
            uVar18 = (ulong)uVar15;
            uVar23 = uVar23 - 2;
            uVar15 = uVar15 + 2;
            iVar5 = *(int *)(DAT_ffff0000908776d0 + uVar18 * 0x18 + 0xc) + iVar5;
            iVar6 = *(int *)(DAT_ffff0000908776d0 + (ulong)uVar1 * 0x18 + 0xc) + iVar6;
          } while (uVar23 != 0);
          uVar23 = iVar6 + iVar5;
          if (uVar3 == uVar2) goto LAB_ffff000090747bd4;
        }
        do {
          uVar18 = (ulong)uVar16;
          uVar16 = uVar16 + 1;
          uVar23 = *(int *)(DAT_ffff0000908776d0 + uVar18 * 0x18 + 0xc) + uVar23;
        } while (uVar16 <= uVar4);
      }
LAB_ffff000090747bd4:
      lVar8 = *(long *)(DAT_ffff0000908776d0 + uVar13 * 0x18 + 0x10);
      local_98 = CONCAT44(local_98._4_4_,*(undefined4 *)(DAT_ffff0000908776f0 + uVar20 * 0xc));
      local_a8 = lVar8;
      lStack_a0 = lVar8 + (ulong)uVar23;
      FUN_ffff00009078b0fc(&local_a8);
      FUN_ffff00009077e11c
                (s___s__MPU__d____lX___lX__ffff0000907eb0f6,s_platform_load_connsys_ffff0000907f9fb9
                 ,uVar20 & 0xffffffff,lVar8,lVar8 + (ulong)uVar23);
      uVar20 = uVar20 + 1;
    } while (uVar20 < DAT_ffff0000908776ec);
  }
  iVar5 = FUN_ffff00009076da24(param_1,s__soc_connsys_emi_config_ffff0000907d9540);
  if ((iVar5 < 0) &&
     (iVar5 = FUN_ffff00009076da24(param_1,s__connsys_emi_config_ffff0000907dcae9), iVar5 < 0)) {
    FUN_ffff00009077e11c
              (s___s___s_offset_is_not_found_ffff0000907d6439,
               s_connsys_get_fdt_node_ffff0000907de446,s__connsys_emi_config_ffff0000907dcae9);
    FUN_ffff00009077e11c(s__soc_connsys_emi_config_not_foun_ffff0000907f9fcf);
    goto LAB_ffff000090747e28;
  }
  local_84 = 0;
  puVar9 = (uint *)FUN_ffff00009076df9c
                             (param_1,iVar5,s_emi_region_to_atf_ffff0000907f1798,&local_84);
  if ((puVar9 == (uint *)0x0) || (local_84 == 0)) {
LAB_ffff00009074806c:
    FUN_ffff00009077e11c(s_not_found_emi_region_to_atf_node_ffff0000907f8602);
    goto LAB_ffff000090747e28;
  }
  FUN_ffff00009077e11c
            (s___s__data_len___d__ffff0000907e3173,s_conn_pass_info_to_atf_ffff0000907f8624);
  if ((int)local_84 >> 2 != 2) {
LAB_ffff00009074807c:
    FUN_ffff00009077e11c
              (s___s__idx_not_match_items___d__ffff0000907dfcd5,
               s_conn_pass_info_to_atf_ffff0000907f8624);
    goto LAB_ffff000090747e28;
  }
  uVar15 = *puVar9;
  uVar4 = puVar9[1];
  uVar23 = (uVar15 & 0xff00ff00) >> 8 | (uVar15 & 0xff00ff) << 8;
  uVar23 = uVar23 >> 0x10 | uVar23 << 0x10;
  uVar20 = (ulong)uVar23;
  uVar3 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
  uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
  FUN_ffff00009077e11c
            (s___s__items___d____d__>__d__ffff0000907dcafd,s_conn_pass_info_to_atf_ffff0000907f8624,
             2,uVar20,uVar3);
  if ((uVar23 < DAT_ffff0000908776cc) && (uVar3 < DAT_ffff0000908776cc)) {
    if (uVar23 <= uVar3) {
      if (uVar4 == uVar15) {
        iVar21 = 0;
LAB_ffff000090747f3c:
        iVar6 = (uVar3 - (int)uVar20) + 1;
        piVar14 = (int *)(DAT_ffff0000908776d0 + uVar20 * 0x18 + 0xc);
        do {
          iVar6 = iVar6 + -1;
          iVar21 = *piVar14 + iVar21;
          piVar14 = piVar14 + 6;
        } while (iVar6 != 0);
      }
      else {
        uVar13 = (ulong)(uVar3 - uVar23) + 1;
        uVar17 = uVar13 & 0x1fffffffe;
        iVar6 = 0;
        iVar21 = 0;
        uVar20 = uVar17 + uVar20;
        piVar14 = (int *)(DAT_ffff0000908776d0 + (ulong)uVar23 * 0x18 + 0x24);
        uVar18 = uVar17;
        do {
          uVar18 = uVar18 - 2;
          iVar6 = piVar14[-6] + iVar6;
          iVar21 = *piVar14 + iVar21;
          piVar14 = piVar14 + 0xc;
        } while (uVar18 != 0);
        iVar21 = iVar21 + iVar6;
        if (uVar13 != uVar17) goto LAB_ffff000090747f3c;
      }
      if (iVar21 != 0) {
        uVar11 = *(undefined8 *)(DAT_ffff0000908776d0 + (ulong)uVar23 * 0x18 + 0x10);
        FUN_ffff00009077e11c
                  (s___s__connsys_atf_size__0x_x__ffff0000907f17aa,
                   s_conn_pass_info_to_atf_ffff0000907f8624,iVar21);
        local_80[2] = 0;
        local_80[3] = 0;
        local_80[0] = 0;
        local_80[1] = 0;
        FUN_ffff00009071ebf4(0xc200041a,0,uVar11,iVar21,0,0,0,0,local_80);
        if (local_80[0] != 0x100) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cee0
                    (s___s__Fail_to_set_connsys_emi_add_ffff0000907f4cbc,
                     s_conninfra_smc_set_emi_addr_ffff0000907d4907);
        }
        FUN_ffff00009077e11c
                  (s___s__Set_connsys_emi_addr_ok_ffff0000907e7d27,
                   s_conninfra_smc_set_emi_addr_ffff0000907d4907);
        puVar9 = (uint *)FUN_ffff00009076df9c
                                   (param_1,iVar5,s_gps_emi_to_atf_ffff0000907ec942,&local_84);
        if ((puVar9 == (uint *)0x0) || (local_84 == 0)) goto LAB_ffff00009074806c;
        FUN_ffff00009077e11c
                  (s___s__data_len___d__ffff0000907e3173,s_conn_pass_info_to_atf_ffff0000907f8624);
        if ((int)local_84 >> 2 != 2) goto LAB_ffff00009074807c;
        uVar15 = *puVar9;
        uVar4 = puVar9[1];
        uVar23 = (uVar15 & 0xff00ff00) >> 8 | (uVar15 & 0xff00ff) << 8;
        uVar23 = uVar23 >> 0x10 | uVar23 << 0x10;
        uVar20 = (ulong)uVar23;
        uVar3 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
        uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
        if ((uVar23 < DAT_ffff0000908776cc) && (uVar3 < DAT_ffff0000908776cc)) {
          if (uVar23 <= uVar3) {
            if (uVar4 == uVar15) {
              iVar6 = 0;
LAB_ffff000090748110:
              iVar5 = (uVar3 - (int)uVar20) + 1;
              piVar14 = (int *)(DAT_ffff0000908776d0 + uVar20 * 0x18 + 0xc);
              do {
                iVar5 = iVar5 + -1;
                iVar6 = *piVar14 + iVar6;
                piVar14 = piVar14 + 6;
              } while (iVar5 != 0);
            }
            else {
              uVar13 = (ulong)(uVar3 - uVar23) + 1;
              uVar17 = uVar13 & 0x1fffffffe;
              iVar5 = 0;
              iVar6 = 0;
              uVar20 = uVar17 + uVar20;
              piVar14 = (int *)(DAT_ffff0000908776d0 + (ulong)uVar23 * 0x18 + 0x24);
              uVar18 = uVar17;
              do {
                uVar18 = uVar18 - 2;
                iVar5 = piVar14[-6] + iVar5;
                iVar6 = *piVar14 + iVar6;
                piVar14 = piVar14 + 0xc;
              } while (uVar18 != 0);
              iVar6 = iVar6 + iVar5;
              if (uVar13 != uVar17) goto LAB_ffff000090748110;
            }
            if (iVar6 != 0) {
              uVar11 = *(undefined8 *)(DAT_ffff0000908776d0 + (ulong)uVar23 * 0x18 + 0x10);
              FUN_ffff00009077e11c
                        (s___s__gps_emi_pa___lx__size___d__ffff0000907f67a9,
                         s_conn_pass_info_to_atf_ffff0000907f8624,uVar11,iVar6);
              FUN_ffff000090748184(uVar11,iVar6);
              goto LAB_ffff000090747e28;
            }
          }
        }
        else {
          FUN_ffff00009077e11c
                    (s___s__incorrect___d__>__d__ffff0000907d647b,
                     s_conn_calculate_region_size_ffff0000907db0be,uVar23,uVar3);
        }
        FUN_ffff00009077e11c(s_incorrect_region_size___d__>__d__ffff0000907ec951,uVar23,uVar3);
        goto LAB_ffff000090747e28;
      }
    }
  }
  else {
    FUN_ffff00009077e11c
              (s___s__incorrect___d__>__d__ffff0000907d647b,
               s_conn_calculate_region_size_ffff0000907db0be,uVar23,uVar3);
  }
  FUN_ffff00009077e11c(s_incorrect_emi_region_size___d__>_ffff0000907fa027,uVar23,uVar3);
LAB_ffff000090747e28:
  if (DAT_ffff0000908776d0 != 0) {
    thunk_FUN_ffff0000907c314c();
  }
  if (DAT_ffff0000908776e0 != 0) {
    thunk_FUN_ffff0000907c314c();
  }
  if (DAT_ffff0000908776f0 != 0) {
    thunk_FUN_ffff0000907c314c();
  }
  return 0;
}



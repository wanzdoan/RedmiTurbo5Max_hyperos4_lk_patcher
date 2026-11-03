// ffff000090746f5c  size=3984  FUN_ffff000090746f5c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090746f5c(undefined8 param_1)

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
  iVar5 = FUN_ffff00009076d75c(param_1,s__soc_connsys_emi_config_ffff0000907d94a9);
  if ((iVar5 < 0) &&
     (iVar5 = FUN_ffff00009076d75c(param_1,s__connsys_emi_config_ffff0000907dc9fc), iVar5 < 0)) {
    FUN_ffff00009077de54
              (s___s___s_offset_is_not_found_ffff0000907d63a3,
               s_connsys_get_fdt_node_ffff0000907de38c,s__connsys_emi_config_ffff0000907dc9fc);
    FUN_ffff00009077de54(s__soc_connsys_emi_config_not_foun_ffff0000907f9e33);
  }
  else {
    iVar6 = FUN_ffff00009071c57c();
    if (iVar6 == 0) {
      local_80[0] = local_80[0] & 0xffffffff00000000;
      lVar8 = FUN_ffff00009076daec(param_1,iVar5,s_emi_max_addr_ffff0000907d7c6f,local_80);
      if (lVar8 == 0) {
        FUN_ffff00009077de54
                  (s___s___s_is_not_found_ffff0000907e641a,s_connsys_get_fdt_prop_ffff0000907e3064,
                   s_emi_max_addr_ffff0000907d7c6f);
        _DAT_ffff000090877620 = 0;
      }
      else {
        uVar15 = (*(uint *)(lVar8 + 0xc) & 0xff00ff00) >> 8 |
                 (*(uint *)(lVar8 + 0xc) & 0xff00ff) << 8;
        _DAT_ffff000090877620 = (ulong)(uVar15 >> 0x10 | uVar15 << 0x10);
      }
      local_80[0] = local_80[0] & 0xffffffff00000000;
      lVar8 = FUN_ffff00009076daec(param_1,iVar5,s_emi_min_addr_ffff0000907ee396,local_80);
      if (lVar8 == 0) {
        FUN_ffff00009077de54
                  (s___s___s_is_not_found_ffff0000907e641a,s_connsys_get_fdt_prop_ffff0000907e3064,
                   s_emi_min_addr_ffff0000907ee396);
        _DAT_ffff000090877628 = 0;
      }
      else {
        uVar15 = (*(uint *)(lVar8 + 0xc) & 0xff00ff00) >> 8 |
                 (*(uint *)(lVar8 + 0xc) & 0xff00ff) << 8;
        _DAT_ffff000090877628 = (ulong)(uVar15 >> 0x10 | uVar15 << 0x10);
      }
    }
    else {
      _DAT_ffff000090877628 = 0x250000000;
      _DAT_ffff000090877620 = 0x29fffffff;
    }
    local_80[0] = local_80[0] & 0xffffffff00000000;
    lVar8 = FUN_ffff00009076daec(param_1,iVar5,s_emi_alignment_ffff0000907fd6b8,local_80);
    if (lVar8 == 0) {
      FUN_ffff00009077de54
                (s___s___s_is_not_found_ffff0000907e641a,s_connsys_get_fdt_prop_ffff0000907e3064,
                 s_emi_alignment_ffff0000907fd6b8);
LAB_ffff00009074710c:
      _DAT_ffff000090877650 = 0x1000000;
    }
    else {
      uVar15 = *(uint *)(lVar8 + 0xc);
      if (uVar15 == 0) goto LAB_ffff00009074710c;
      uVar15 = (uVar15 & 0xff00ff00) >> 8 | (uVar15 & 0xff00ff) << 8;
      _DAT_ffff000090877650 = uVar15 >> 0x10 | uVar15 << 0x10;
    }
    FUN_ffff00009077de54
              (s___s__alloc_max_min_addr___llX____ffff0000907f9e56,
               s_connsys_load_emi_settings_ffff0000907d4896,_DAT_ffff000090877620,
               _DAT_ffff000090877628);
    puVar9 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar5,s_emi_layout_region_ffff0000907e3079,&local_84);
    FUN_ffff00009077de54
              (s___s__emi_layout_len___d__ffff0000907e963d,
               s_connsys_load_emi_settings_ffff0000907d4896,local_84);
    uVar15 = local_84;
    if ((puVar9 != (uint *)0x0) && (0 < (int)local_84)) {
      uVar4 = local_84 >> 2;
      FUN_ffff00009077de54
                (s___s__items___d____d__ffff0000907e9657,
                 s_connsys_load_emi_settings_ffff0000907d4896,uVar4,local_84 >> 2 & 1);
      if ((uVar4 & 1) == 0) {
        _DAT_ffff000090877634 = uVar15 >> 3;
        _DAT_ffff000090877638 = FUN_ffff00009077d24c((ulong)_DAT_ffff000090877634 * 0x18);
        if (_DAT_ffff000090877638 != 0) {
          if (_DAT_ffff000090877634 != 0) {
            uVar20 = 0;
            lVar8 = 0xc;
            do {
              uVar15 = (puVar9[1] & 0xff00ff00) >> 8 | (puVar9[1] & 0xff00ff) << 8;
              uVar15 = uVar15 >> 0x10 | uVar15 << 0x10;
              uVar4 = (*puVar9 & 0xff00ff00) >> 8 | (*puVar9 & 0xff00ff) << 8;
              FUN_ffff00009077de54
                        (s___s__idx___d__vals___x___x__ffff0000907f15dc,
                         s_connsys_load_emi_settings_ffff0000907d4896,uVar20 & 0xffffffff,
                         uVar4 >> 0x10 | uVar4 << 0x10,uVar15);
              uVar20 = uVar20 + 1;
              *(uint *)(_DAT_ffff000090877638 + lVar8) = uVar15;
              lVar8 = lVar8 + 0x18;
              _DAT_ffff000090877630 = _DAT_ffff000090877630 + uVar15;
              puVar9 = puVar9 + 2;
            } while (uVar20 < _DAT_ffff000090877634);
          }
          goto LAB_ffff00009074722c;
        }
        pcVar10 = s___s__malloc_fail_for_g_layout_re_ffff000090800cd7;
LAB_ffff000090747b68:
        FUN_ffff00009077de54(pcVar10,s_connsys_load_emi_settings_ffff0000907d4896);
        uVar11 = 0xfffffffb;
      }
      else {
LAB_ffff0000907472c4:
        uVar11 = 0xfffffff9;
      }
LAB_ffff000090747b78:
      FUN_ffff00009077de54
                (s___s__load_emi_seting_fail_ret____ffff0000907ec784,
                 s_platform_load_connsys_ffff0000907f9e1d,uVar11);
      goto LAB_ffff000090747b90;
    }
LAB_ffff00009074722c:
    FUN_ffff00009077de54
              (s___s__total_size___d__ffff0000907d1513,s_connsys_load_emi_settings_ffff0000907d4896,
               _DAT_ffff000090877630);
    puVar9 = (uint *)FUN_ffff00009076dcd4
                               (param_1,iVar5,s_emi_alloc_block_ffff0000907ee3a3,&local_84);
    FUN_ffff00009077de54
              (s___s__emi_alloc_region_len___d__ffff0000907e6430,
               s_connsys_load_emi_settings_ffff0000907d4896,local_84);
    uVar15 = local_84;
    if ((puVar9 != (uint *)0x0) && (0 < (int)local_84)) {
      uVar4 = (local_84 >> 2) % 3;
      FUN_ffff00009077de54
                (s___s__items___d____d__ffff0000907e9657,
                 s_connsys_load_emi_settings_ffff0000907d4896,local_84 >> 2,uVar4);
      if (uVar4 != 0) goto LAB_ffff0000907472c4;
      _DAT_ffff000090877640 = uVar15 / 0xc;
      _DAT_ffff000090877648 = FUN_ffff00009077d24c(((ulong)uVar15 / 0xc) * 0xa0);
      if (_DAT_ffff000090877648 == 0) {
        pcVar10 = s___s__malloc_fail_for_g_alloc_blo_ffff0000907d63c0;
        goto LAB_ffff000090747b68;
      }
      FUN_ffff00009077eb88(_DAT_ffff000090877648,0,(ulong)_DAT_ffff000090877640 * 0xa0);
      if (_DAT_ffff000090877640 != 0) {
        lVar8 = 0;
        uVar20 = 0;
        do {
          uVar15 = (*puVar9 & 0xff00ff00) >> 8 | (*puVar9 & 0xff00ff) << 8;
          uVar15 = uVar15 >> 0x10 | uVar15 << 0x10;
          uVar4 = (puVar9[1] & 0xff00ff00) >> 8 | (puVar9[1] & 0xff00ff) << 8;
          uVar4 = uVar4 >> 0x10 | uVar4 << 0x10;
          uVar23 = (puVar9[2] & 0xff00ff00) >> 8 | (puVar9[2] & 0xff00ff) << 8;
          uVar23 = uVar23 >> 0x10 | uVar23 << 0x10;
          FUN_ffff00009077de54
                    (s___s__idx___d__vals___x___x___x__ffff0000907f8445,
                     s_connsys_load_emi_settings_ffff0000907d4896,uVar20 & 0xffffffff,uVar15,uVar4,
                     uVar23);
          uVar11 = 0xfffffff9;
          if ((_DAT_ffff000090877634 <= uVar15) || (_DAT_ffff000090877634 <= uVar4))
          goto LAB_ffff000090747b78;
          uVar20 = uVar20 + 1;
          puVar9 = puVar9 + 3;
          lVar19 = _DAT_ffff000090877648 + lVar8;
          lVar8 = lVar8 + 0xa0;
          *(uint *)(lVar19 + 0x80) = uVar15;
          *(uint *)(lVar19 + 0x84) = uVar4;
          *(uint *)(lVar19 + 0x90) = uVar23;
        } while (uVar20 < _DAT_ffff000090877640);
      }
    }
    lVar8 = FUN_ffff00009076dcd4(param_1,iVar5,s_emi_alloc_block_name_ffff0000907e966d,&local_84);
    if ((0 < (int)local_84) && (lVar8 != 0)) {
      lVar19 = 0;
      uVar20 = 0xffffffffffffffff;
      do {
        FUN_ffff00009077de54
                  (s___s__emi_alloc_name_len___d____s_ffff0000907e1766,
                   s_connsys_load_emi_settings_ffff0000907d4896,local_84,lVar8);
        uVar20 = uVar20 + 1;
        if (_DAT_ffff000090877640 <= uVar20) goto LAB_ffff0000907472c4;
        FUN_ffff00009077efe4(_DAT_ffff000090877648 + lVar19,lVar8,0x7f);
        iVar6 = FUN_ffff00009077ef88(lVar8);
        local_84 = local_84 - (iVar6 + 1);
        if (lVar8 == 0) break;
        lVar19 = lVar19 + 0xa0;
        lVar8 = lVar8 + (iVar6 + 1);
      } while (0 < (int)local_84);
    }
    lVar8 = FUN_ffff00009076dcd4(param_1,iVar5,s_emi_mpu_region_ffff0000907fd6c6,&local_84);
    FUN_ffff00009077de54
              (s___s__emi_mpu_len___d__ffff0000907f15f9,s_connsys_load_emi_settings_ffff0000907d4896
               ,local_84);
    uVar15 = local_84;
    if ((lVar8 != 0) && (0 < (int)local_84)) {
      FUN_ffff00009077de54
                (s___s__MPU_items___d____d__ffff0000907d48b0,
                 s_connsys_load_emi_settings_ffff0000907d4896,local_84 >> 2,(local_84 >> 2) % 3);
      _DAT_ffff000090877654 = uVar15 / 0xc;
      _DAT_ffff000090877658 = FUN_ffff00009077d24c(((ulong)uVar15 / 0xc) * 0xc);
      if (_DAT_ffff000090877658 == 0) {
        pcVar10 = s___s__malloc_fail_for_g_mpu_regio_ffff0000907daf79;
        goto LAB_ffff000090747b68;
      }
      FUN_ffff00009077eb88(_DAT_ffff000090877658,0,(ulong)_DAT_ffff000090877654 * 0xc);
      if (_DAT_ffff000090877654 != 0) {
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
          FUN_ffff00009077de54
                    (s___s__idx___d__vals___x___x___x__ffff0000907f8445,
                     s_connsys_load_emi_settings_ffff0000907d4896,uVar20 & 0xffffffff,uVar15,uVar4,
                     uVar23);
          uVar11 = 0xfffffff9;
          if ((_DAT_ffff000090877634 <= uVar15) || (_DAT_ffff000090877634 <= uVar4))
          goto LAB_ffff000090747b78;
          uVar20 = uVar20 + 1;
          puVar9 = (uint *)(_DAT_ffff000090877658 + lVar19);
          lVar19 = lVar19 + 0xc;
          puVar9[1] = uVar15;
          puVar9[2] = uVar4;
          *puVar9 = uVar23;
        } while (uVar20 < _DAT_ffff000090877654);
      }
    }
  }
  if (PTR_FUN_ffff00009081a6d8 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a6d8)(param_1);
  }
  if (PTR_FUN_ffff00009081a6e8 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a6e8)(param_1);
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
  if (_DAT_ffff000090877640 == 0) {
    lVar8 = 0;
  }
  else {
    uVar20 = 0;
    lVar19 = 0;
    lVar8 = 0;
    do {
      lVar22 = _DAT_ffff000090877648;
      lVar24 = _DAT_ffff000090877648 + uVar20 * 0xa0;
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
LAB_ffff0000907476b0:
          uVar23 = 0;
          uVar13 = (ulong)uVar15;
        }
        else {
          if (uVar23 <= uVar4 + 1) {
            uVar23 = uVar4 + 1;
          }
          if (uVar23 - 1 < uVar15) goto LAB_ffff0000907476b0;
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
            iVar5 = *(int *)(_DAT_ffff000090877638 + uVar18 * 0x18 + 0xc) + iVar5;
            iVar6 = *(int *)(_DAT_ffff000090877638 + (ulong)uVar2 * 0x18 + 0xc) + iVar6;
          } while (uVar23 != 0);
          uVar23 = iVar6 + iVar5;
          if (uVar3 == uVar16) goto LAB_ffff0000907476d0;
        }
        do {
          lVar12 = uVar13 * 0x18;
          uVar15 = (int)uVar13 + 1;
          uVar13 = (ulong)uVar15;
          uVar23 = *(int *)(_DAT_ffff000090877638 + lVar12 + 0xc) + uVar23;
        } while (uVar15 <= uVar4);
      }
LAB_ffff0000907476d0:
      FUN_ffff00009077de54
                (s___s__region__d__name___s__region_ffff0000907f83e4,
                 s_platform_load_connsys_ffff0000907f9e1d,uVar20 & 0xffffffff,lVar24,uVar23);
      puVar9 = (uint *)(lVar22 + uVar20 * 0xa0 + 0x88);
      *puVar9 = uVar23;
      if (uVar23 != 0) {
        lVar12 = _DAT_ffff000090877648 + uVar20 * 0xa0;
        FUN_ffff00009077de54
                  (s___s__alloc_idx___d____s____d__>__ffff0000907efcc1,
                   s_platform_load_connsys_ffff0000907f9e1d,uVar20 & 0xffffffff,lVar12,
                   *(undefined4 *)(lVar12 + 0x80),*(undefined4 *)(lVar12 + 0x84),
                   *(undefined4 *)(lVar12 + 0x90));
        if (uVar20 == 0) {
          lVar8 = FUN_ffff000090719f58
                            (_DAT_ffff000090877630,_DAT_ffff000090877650,_DAT_ffff000090877628,
                             _DAT_ffff000090877620,0,*(undefined4 *)(lVar22 + 0x90),lVar24);
          if (lVar8 == 0) {
            FUN_ffff00009077de54
                      (s___s__allocation_failed_ffff0000907e4c89,
                       s_platform_load_connsys_ffff0000907f9e1d);
            goto LAB_ffff000090747b90;
          }
          uVar7 = FUN_ffff00009071a2e0(lVar8,_DAT_ffff000090877630,*puVar9);
          FUN_ffff00009077de54
                    (s___s__mblock_resize__0x_x__>_0x_x_ffff0000907f8410,
                     s_platform_load_connsys_ffff0000907f9e1d,_DAT_ffff000090877630,*puVar9,lVar8,
                     uVar7);
          *(long *)(lVar22 + 0x98) = lVar8;
          lVar19 = lVar8;
        }
        else {
          lVar12 = FUN_ffff000090719798
                             (*puVar9,_DAT_ffff000090877650,_DAT_ffff000090877620,lVar19,
                              *(undefined4 *)(lVar22 + uVar20 * 0xa0 + 0x90),lVar24);
          if (lVar12 == 0) {
            FUN_ffff00009077de54
                      (s___s__allocation___d___s__failed_ffff0000907e7bab,
                       s_platform_load_connsys_ffff0000907f9e1d,uVar20 & 0xffffffff,lVar24);
            goto LAB_ffff000090747b90;
          }
          uVar15 = *puVar9;
          *(long *)(lVar22 + uVar20 * 0xa0 + 0x98) = lVar19;
          FUN_ffff00009077de54
                    (s___s__mblock_allocate___d___s__pa_ffff0000907efcef,
                     s_platform_load_connsys_ffff0000907f9e1d,uVar20 & 0xffffffff,lVar24,lVar19,
                     uVar15);
        }
        lVar19 = lVar19 + (ulong)*puVar9;
      }
      uVar20 = uVar20 + 1;
    } while (uVar20 < _DAT_ffff000090877640);
  }
  if (_DAT_ffff000090877634 != 0) {
    lVar19 = 0;
    uVar20 = 0;
    lVar22 = 0;
    do {
      lVar24 = _DAT_ffff000090877638 + lVar19;
      *(long *)(lVar24 + 0x10) = lVar22 + lVar8;
      lVar22 = lVar22 + (ulong)*(uint *)(lVar24 + 0xc);
      FUN_ffff00009077de54
                (s___s__fill___d__addr___08lX__ffff0000907fd69b,
                 s_platform_load_connsys_ffff0000907f9e1d,uVar20 & 0xffffffff);
      uVar20 = uVar20 + 1;
      lVar19 = lVar19 + 0x18;
    } while (uVar20 < _DAT_ffff000090877634);
  }
  if (PTR_FUN_ffff00009081a6e0 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a6e0)(param_1);
  }
  if (PTR_FUN_ffff00009081a6f0 != (undefined *)0x0) {
    (*(code *)PTR_FUN_ffff00009081a6f0)(param_1);
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
  if (_DAT_ffff000090877654 != 0) {
    uVar20 = 0;
    do {
      lVar8 = _DAT_ffff000090877658 + uVar20 * 0xc;
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
LAB_ffff0000907479d4:
          uVar23 = 0;
          uVar16 = uVar15;
        }
        else {
          if (uVar23 <= uVar4 + 1) {
            uVar23 = uVar4 + 1;
          }
          if (uVar23 - 1 < uVar15) goto LAB_ffff0000907479d4;
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
            iVar5 = *(int *)(_DAT_ffff000090877638 + uVar18 * 0x18 + 0xc) + iVar5;
            iVar6 = *(int *)(_DAT_ffff000090877638 + (ulong)uVar1 * 0x18 + 0xc) + iVar6;
          } while (uVar23 != 0);
          uVar23 = iVar6 + iVar5;
          if (uVar3 == uVar2) goto LAB_ffff00009074793c;
        }
        do {
          uVar18 = (ulong)uVar16;
          uVar16 = uVar16 + 1;
          uVar23 = *(int *)(_DAT_ffff000090877638 + uVar18 * 0x18 + 0xc) + uVar23;
        } while (uVar16 <= uVar4);
      }
LAB_ffff00009074793c:
      lVar8 = *(long *)(_DAT_ffff000090877638 + uVar13 * 0x18 + 0x10);
      local_98 = CONCAT44(local_98._4_4_,*(undefined4 *)(_DAT_ffff000090877658 + uVar20 * 0xc));
      local_a8 = lVar8;
      lStack_a0 = lVar8 + (ulong)uVar23;
      FUN_ffff00009078ae3c(&local_a8);
      FUN_ffff00009077de54
                (s___s__MPU__d____lX___lX__ffff0000907eaf5c,s_platform_load_connsys_ffff0000907f9e1d
                 ,uVar20 & 0xffffffff,lVar8,lVar8 + (ulong)uVar23);
      uVar20 = uVar20 + 1;
    } while (uVar20 < _DAT_ffff000090877654);
  }
  iVar5 = FUN_ffff00009076d75c(param_1,s__soc_connsys_emi_config_ffff0000907d94a9);
  if ((iVar5 < 0) &&
     (iVar5 = FUN_ffff00009076d75c(param_1,s__connsys_emi_config_ffff0000907dc9fc), iVar5 < 0)) {
    FUN_ffff00009077de54
              (s___s___s_offset_is_not_found_ffff0000907d63a3,
               s_connsys_get_fdt_node_ffff0000907de38c,s__connsys_emi_config_ffff0000907dc9fc);
    FUN_ffff00009077de54(s__soc_connsys_emi_config_not_foun_ffff0000907f9e33);
    goto LAB_ffff000090747b90;
  }
  local_84 = 0;
  puVar9 = (uint *)FUN_ffff00009076dcd4
                             (param_1,iVar5,s_emi_region_to_atf_ffff0000907f1610,&local_84);
  if ((puVar9 == (uint *)0x0) || (local_84 == 0)) {
LAB_ffff000090747dd4:
    FUN_ffff00009077de54(s_not_found_emi_region_to_atf_node_ffff0000907f8466);
    goto LAB_ffff000090747b90;
  }
  FUN_ffff00009077de54
            (s___s__data_len___d__ffff0000907e308b,s_conn_pass_info_to_atf_ffff0000907f8488);
  if ((int)local_84 >> 2 != 2) {
LAB_ffff000090747de4:
    FUN_ffff00009077de54
              (s___s__idx_not_match_items___d__ffff0000907dfc1b,
               s_conn_pass_info_to_atf_ffff0000907f8488);
    goto LAB_ffff000090747b90;
  }
  uVar15 = *puVar9;
  uVar4 = puVar9[1];
  uVar23 = (uVar15 & 0xff00ff00) >> 8 | (uVar15 & 0xff00ff) << 8;
  uVar23 = uVar23 >> 0x10 | uVar23 << 0x10;
  uVar20 = (ulong)uVar23;
  uVar3 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
  uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
  FUN_ffff00009077de54
            (s___s__items___d____d__>__d__ffff0000907dca10,s_conn_pass_info_to_atf_ffff0000907f8488,
             2,uVar20,uVar3);
  if ((uVar23 < _DAT_ffff000090877634) && (uVar3 < _DAT_ffff000090877634)) {
    if (uVar23 <= uVar3) {
      if (uVar4 == uVar15) {
        iVar21 = 0;
LAB_ffff000090747ca4:
        iVar6 = (uVar3 - (int)uVar20) + 1;
        piVar14 = (int *)(_DAT_ffff000090877638 + uVar20 * 0x18 + 0xc);
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
        piVar14 = (int *)(_DAT_ffff000090877638 + (ulong)uVar23 * 0x18 + 0x24);
        uVar18 = uVar17;
        do {
          uVar18 = uVar18 - 2;
          iVar6 = piVar14[-6] + iVar6;
          iVar21 = *piVar14 + iVar21;
          piVar14 = piVar14 + 0xc;
        } while (uVar18 != 0);
        iVar21 = iVar21 + iVar6;
        if (uVar13 != uVar17) goto LAB_ffff000090747ca4;
      }
      if (iVar21 != 0) {
        uVar11 = *(undefined8 *)(_DAT_ffff000090877638 + (ulong)uVar23 * 0x18 + 0x10);
        FUN_ffff00009077de54
                  (s___s__connsys_atf_size__0x_x__ffff0000907f1622,
                   s_conn_pass_info_to_atf_ffff0000907f8488,iVar21);
        local_80[2] = 0;
        local_80[3] = 0;
        local_80[0] = 0;
        local_80[1] = 0;
        FUN_ffff00009071ea6c(0xc200041a,0,uVar11,iVar21,0,0,0,0,local_80);
        if (local_80[0] != 0x100) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cc18
                    (s___s__Fail_to_set_connsys_emi_add_ffff0000907f4b38,
                     s_conninfra_smc_set_emi_addr_ffff0000907d48ca);
        }
        FUN_ffff00009077de54
                  (s___s__Set_connsys_emi_addr_ok_ffff0000907e7bcc,
                   s_conninfra_smc_set_emi_addr_ffff0000907d48ca);
        puVar9 = (uint *)FUN_ffff00009076dcd4
                                   (param_1,iVar5,s_gps_emi_to_atf_ffff0000907ec7a8,&local_84);
        if ((puVar9 == (uint *)0x0) || (local_84 == 0)) goto LAB_ffff000090747dd4;
        FUN_ffff00009077de54
                  (s___s__data_len___d__ffff0000907e308b,s_conn_pass_info_to_atf_ffff0000907f8488);
        if ((int)local_84 >> 2 != 2) goto LAB_ffff000090747de4;
        uVar15 = *puVar9;
        uVar4 = puVar9[1];
        uVar23 = (uVar15 & 0xff00ff00) >> 8 | (uVar15 & 0xff00ff) << 8;
        uVar23 = uVar23 >> 0x10 | uVar23 << 0x10;
        uVar20 = (ulong)uVar23;
        uVar3 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
        uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
        if ((uVar23 < _DAT_ffff000090877634) && (uVar3 < _DAT_ffff000090877634)) {
          if (uVar23 <= uVar3) {
            if (uVar4 == uVar15) {
              iVar6 = 0;
LAB_ffff000090747e78:
              iVar5 = (uVar3 - (int)uVar20) + 1;
              piVar14 = (int *)(_DAT_ffff000090877638 + uVar20 * 0x18 + 0xc);
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
              piVar14 = (int *)(_DAT_ffff000090877638 + (ulong)uVar23 * 0x18 + 0x24);
              uVar18 = uVar17;
              do {
                uVar18 = uVar18 - 2;
                iVar5 = piVar14[-6] + iVar5;
                iVar6 = *piVar14 + iVar6;
                piVar14 = piVar14 + 0xc;
              } while (uVar18 != 0);
              iVar6 = iVar6 + iVar5;
              if (uVar13 != uVar17) goto LAB_ffff000090747e78;
            }
            if (iVar6 != 0) {
              uVar11 = *(undefined8 *)(_DAT_ffff000090877638 + (ulong)uVar23 * 0x18 + 0x10);
              FUN_ffff00009077de54
                        (s___s__gps_emi_pa___lx__size___d__ffff0000907f6608,
                         s_conn_pass_info_to_atf_ffff0000907f8488,uVar11,iVar6);
              FUN_ffff000090747eec(uVar11,iVar6);
              goto LAB_ffff000090747b90;
            }
          }
        }
        else {
          FUN_ffff00009077de54
                    (s___s__incorrect___d__>__d__ffff0000907d63e5,
                     s_conn_calculate_region_size_ffff0000907daf9c,uVar23,uVar3);
        }
        FUN_ffff00009077de54(s_incorrect_region_size___d__>__d__ffff0000907ec7b7,uVar23,uVar3);
        goto LAB_ffff000090747b90;
      }
    }
  }
  else {
    FUN_ffff00009077de54
              (s___s__incorrect___d__>__d__ffff0000907d63e5,
               s_conn_calculate_region_size_ffff0000907daf9c,uVar23,uVar3);
  }
  FUN_ffff00009077de54(s_incorrect_emi_region_size___d__>_ffff0000907f9e8b,uVar23,uVar3);
LAB_ffff000090747b90:
  if (_DAT_ffff000090877638 != 0) {
    thunk_FUN_ffff0000907c2ad8();
  }
  if (_DAT_ffff000090877648 != 0) {
    thunk_FUN_ffff0000907c2ad8();
  }
  if (_DAT_ffff000090877658 != 0) {
    thunk_FUN_ffff0000907c2ad8();
  }
  return 0;
}



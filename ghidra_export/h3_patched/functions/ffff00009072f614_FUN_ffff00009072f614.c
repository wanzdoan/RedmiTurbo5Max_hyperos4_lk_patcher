// ffff00009072f614  size=2168  FUN_ffff00009072f614

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009072f614(void)

{
  uint *puVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  uint uVar5;
  uint *puVar6;
  long lVar7;
  long lVar8;
  char *pcVar9;
  undefined8 uVar10;
  undefined8 uVar11;
  uint *puVar12;
  ulong uVar13;
  undefined8 *puVar14;
  ulong uVar15;
  long lVar16;
  long local_b8;
  uint *local_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  int local_64;
  
  local_b8 = 0;
  puVar6 = (uint *)FUN_ffff00009072f3f4();
  if (((puVar6 == (uint *)0x0) && (puVar6 = (uint *)FUN_ffff00009072dc30(), puVar6 == (uint *)0x0))
     && (puVar6 = (uint *)FUN_ffff00009072dc40(), puVar6 == (uint *)0x0)) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_use_old_partion_nam_ffff0000907f616d,s_get_dl_tbl_ffff0000907e2bca,
               0xd4);
    local_78 = 0;
    uStack_70 = 0;
    local_88 = 0;
    uStack_80 = 0;
    local_98 = 0;
    uStack_90 = 0;
    local_a8 = 0;
    uStack_a0 = 0;
    local_b0 = (uint *)0x0;
    FUN_ffff00009073505c(s_md1img_ffff0000907f3db5,&local_b0,0x48);
    lVar7 = FUN_ffff00009077bd60(&local_b0);
    if (lVar7 == 0) {
LAB_ffff00009072f6fc:
      FUN_ffff00009077de54
                (s__s__d___CCCI_replace_the_partiti_ffff0000907e4770,
                 s_check_md1rom_image_name_ffff0000907eaace,0xb7);
      puVar6 = (uint *)0xffff000090819208;
    }
    else {
      FUN_ffff00009077bce4();
      puVar6 = (uint *)0xffff000090819188;
    }
  }
  else if (puVar6[6] != 1) {
    local_78 = 0;
    uStack_70 = 0;
    local_88 = 0;
    uStack_80 = 0;
    local_98 = 0;
    uStack_90 = 0;
    local_a8 = 0;
    uStack_a0 = 0;
    local_b0 = (uint *)0x0;
    FUN_ffff00009073505c(s_md1img_ffff0000907f3db5,&local_b0,0x48);
    lVar7 = FUN_ffff00009077bd60(&local_b0);
    if (lVar7 == 0) goto LAB_ffff00009072f6fc;
    FUN_ffff00009077bce4();
  }
  iVar2 = FUN_ffff00009072dc60();
  iVar4 = 0x20;
  if (-1 < iVar2) {
    iVar4 = iVar2;
  }
  iVar3 = FUN_ffff00009072dc50();
  iVar2 = 0x20000000;
  if (iVar3 != 0) {
    iVar2 = iVar3;
  }
  iVar4 = FUN_ffff000090730164(iVar4);
  if (iVar4 < 0) {
    return 0xffffffff;
  }
  lVar7 = FUN_ffff00009072d9f0(s_md_mem_usage_ffff0000907d0f58,iVar2,&local_b8);
  if (lVar7 != 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_Memory_base___p_va__ffff0000907edf8a,
               s_load_soc_modem_image_only_ffff0000907f7f8d,0x264,local_b8,lVar7,iVar2);
    iVar4 = FUN_ffff0000907303a4(iVar2);
    lVar8 = local_b8;
    puVar1 = puVar6;
    if (-1 < iVar4) {
      do {
        puVar12 = puVar1;
        puVar1 = puVar12 + 8;
      } while (1 < *puVar12);
      if (*puVar12 == 1) {
        local_b0 = (uint *)0x0;
        uVar10 = *(undefined8 *)(puVar12 + 2);
        uVar11 = *(undefined8 *)(puVar12 + 4);
        FUN_ffff00009077de54
                  (s__s__d___CCCI_Load_image____s__fr_ffff0000907e1108,
                   s_load_image_by_name_ffff0000907d912f,0xe0,uVar11,uVar10,local_b8,iVar2);
        FUN_ffff000090707d04(lVar8,iVar2);
        iVar4 = FUN_ffff000090785a50(uVar10,uVar11,lVar8,iVar2);
        if (iVar4 < 0) {
          FUN_ffff00009077de54
                    (s__s__d___CCCI_Load_image____s__fa_ffff0000907d1009,
                     s_load_image_by_name_ffff0000907d912f,0xe9,uVar11,uVar10,lVar8,iVar2,iVar4);
        }
        else {
          FUN_ffff000090707cc4(lVar8,iVar4);
          iVar4 = FUN_ffff00009072e1ec(lVar8,iVar4,&local_b0);
          if (-1 < iVar4) {
            FUN_ffff00009072dbf4(local_b0);
            puVar6 = puVar6 + 4;
LAB_ffff00009072f898:
            switch(puVar6[-4]) {
            case 0:
              goto code_r0xffff00009072fc84;
            case 1:
              break;
            case 2:
              local_b0 = (uint *)0x0;
              iVar4 = FUN_ffff00009072fe8c(2,&local_b0,iVar2);
              puVar1 = local_b0;
              if (iVar4 < 0) goto LAB_ffff00009072fc48;
              if (iVar4 != 0) {
                uVar10 = *(undefined8 *)(puVar6 + -2);
                uVar11 = *(undefined8 *)puVar6;
                uVar5 = local_b0[1];
                lVar16 = lVar8 + (ulong)*local_b0;
                FUN_ffff00009077de54
                          (s__s__d___CCCI_Load_image____s__fr_ffff0000907e1108,
                           s_load_image_by_name_ffff0000907d912f,0xe0,uVar11,uVar10,lVar16,uVar5);
                FUN_ffff000090707d04(lVar16,uVar5);
                iVar4 = FUN_ffff000090785a50(uVar10,uVar11,lVar16,uVar5);
                if (iVar4 < 0) {
                  FUN_ffff00009077de54
                            (s__s__d___CCCI_Load_image____s__fa_ffff0000907d1009,
                             s_load_image_by_name_ffff0000907d912f,0xe9,uVar11,uVar10,lVar16,uVar5,
                             iVar4);
                  if (((byte)puVar6[2] >> 1 & 1) == 0) {
                    FUN_ffff0000907312dc();
                    goto LAB_ffff00009072fc48;
                  }
                  FUN_ffff00009077de54
                            (s__s__d___CCCI_skip_optional_sub_i_ffff0000907edfc1,
                             s_load_dsp_image_ffff0000907d444c,0x1ac,*(undefined8 *)puVar6);
                  puVar1[3] = puVar1[3] | 0x100;
                  puVar6 = puVar6 + 8;
                  goto LAB_ffff00009072f898;
                }
                FUN_ffff000090707cc4(lVar16,iVar4);
                iVar4 = FUN_ffff00009072ea78(lVar16,iVar4,*puVar1);
                if (iVar4 < 0) goto LAB_ffff00009072fc48;
              }
              break;
            case 3:
              local_b0 = (uint *)0x0;
              iVar4 = FUN_ffff00009072fe8c(0x80,&local_b0,iVar2);
              if (iVar4 < 0) goto LAB_ffff00009072fc48;
              if (iVar4 != 0) {
                uVar10 = *(undefined8 *)(puVar6 + -2);
                uVar11 = *(undefined8 *)puVar6;
                uVar5 = local_b0[1];
                lVar16 = lVar8 + (ulong)*local_b0;
                FUN_ffff00009077de54
                          (s__s__d___CCCI_Load_image____s__fr_ffff0000907e1108,
                           s_load_image_by_name_ffff0000907d912f,0xe0,uVar11,uVar10,lVar16,uVar5);
                FUN_ffff000090707d04(lVar16,uVar5);
                iVar4 = FUN_ffff000090785a50(uVar10,uVar11,lVar16,uVar5);
                if (-1 < iVar4) {
LAB_ffff00009072fa40:
                  FUN_ffff000090707cc4(lVar16,iVar4);
                  puVar6 = puVar6 + 8;
                  goto LAB_ffff00009072f898;
                }
                FUN_ffff00009077de54
                          (s__s__d___CCCI_Load_image____s__fa_ffff0000907d1009,
                           s_load_image_by_name_ffff0000907d912f,0xe9,uVar11,uVar10,lVar16,uVar5);
                if (((byte)puVar6[2] >> 1 & 1) == 0) {
LAB_ffff00009072fd10:
                  FUN_ffff0000907312dc();
                  goto LAB_ffff00009072fc48;
                }
                uVar11 = *(undefined8 *)puVar6;
                pcVar9 = s_load_ramdisk_image_ffff0000907f11d9;
                uVar10 = 0x1ff;
LAB_ffff00009072fc14:
                FUN_ffff00009077de54
                          (s__s__d___CCCI_skip_optional_sub_i_ffff0000907edfc1,pcVar9,uVar10,uVar11)
                ;
              }
              break;
            case 4:
              local_64 = 0;
              iVar4 = FUN_ffff00009073220c(s_drdi_version_ffff0000907d7756,&local_64,4);
              if (iVar4 == 4) {
                FUN_ffff00009077de54
                          (s__s__d___CCCI___s___drdi_version___ffff0000907e5fe5,
                           s_load_drdi_image_ffff0000907d445b,0x1c5,
                           s_load_drdi_image_ffff0000907d445b,local_64);
                if (local_64 == 3) break;
              }
              else {
                FUN_ffff00009072ff90
                          (s__CCCI___s__d____s__error___drdi__ffff0000907d0e6e,
                           s_load_drdi_image_ffff0000907d445b,0x1c3,
                           s_load_drdi_image_ffff0000907d445b);
              }
              local_b0 = (uint *)0x0;
              iVar4 = FUN_ffff00009072fe8c(0x40,&local_b0,iVar2);
              if (iVar4 < 0) goto LAB_ffff00009072fc48;
              if (iVar4 != 0) {
                if ((*local_b0 == 0) || (local_b0[1] == 0)) {
                  FUN_ffff00009077de54
                            (s__s__d___CCCI_DRDI_v1_not_enable_ffff0000907e915b,
                             s_load_drdi_image_ffff0000907d445b,0x1d1);
                  puVar6 = puVar6 + 8;
                  goto LAB_ffff00009072f898;
                }
                iVar4 = FUN_ffff00009072fe8c(0x20,&local_b0,iVar2);
                if (iVar4 < 0) goto LAB_ffff00009072fc48;
                if (iVar4 != 0) {
                  uVar10 = *(undefined8 *)(puVar6 + -2);
                  uVar11 = *(undefined8 *)puVar6;
                  uVar5 = local_b0[1];
                  lVar16 = lVar8 + (ulong)*local_b0;
                  FUN_ffff00009077de54
                            (s__s__d___CCCI_Load_image____s__fr_ffff0000907e1108,
                             s_load_image_by_name_ffff0000907d912f,0xe0,uVar11,uVar10,lVar16,uVar5);
                  FUN_ffff000090707d04(lVar16,uVar5);
                  iVar4 = FUN_ffff000090785a50(uVar10,uVar11,lVar16,uVar5);
                  if (-1 < iVar4) goto LAB_ffff00009072fa40;
                  FUN_ffff00009077de54
                            (s__s__d___CCCI_Load_image____s__fa_ffff0000907d1009,
                             s_load_image_by_name_ffff0000907d912f,0xe9,uVar11,uVar10,lVar16,uVar5);
                  if (((byte)puVar6[2] >> 1 & 1) == 0) goto LAB_ffff00009072fd10;
                  uVar11 = *(undefined8 *)puVar6;
                  pcVar9 = s_load_drdi_image_ffff0000907d445b;
                  uVar10 = 0x1e1;
                  goto LAB_ffff00009072fc14;
                }
              }
              break;
            default:
              goto switchD_ffff00009072f8b0_default;
            }
            puVar6 = puVar6 + 8;
            goto LAB_ffff00009072f898;
          }
        }
      }
      else {
        FUN_ffff00009077de54
                  (s__s__d___CCCI_Main_image_info_not_ffff0000907fee87,
                   s_find_main_img_ffff0000907d443e,0x211);
      }
    }
  }
LAB_ffff00009072fc48:
  FUN_ffff00009072dba4(lVar7,iVar2);
  FUN_ffff0000907302dc();
  return 0xffffffff;
switchD_ffff00009072f8b0_default:
  FUN_ffff00009077de54
            (s__s__d___CCCI_Not_support_image_t_ffff0000908008ec,s_load_sub_img_ffff0000907df86d,
             0x22e);
  puVar6 = puVar6 + 8;
  goto LAB_ffff00009072f898;
code_r0xffff00009072fc84:
  _DAT_ffff00009086d720 = lVar7;
  uVar5 = FUN_ffff000090730f28(lVar7);
  if ((int)uVar5 < 1) {
    return 0xffffffff;
  }
  uVar15 = (ulong)uVar5;
  lVar8 = FUN_ffff000090730354();
  if (((lVar8 != 0) && (iVar4 = FUN_ffff00009073142c(lVar8,uVar15), -1 < iVar4)) &&
     (iVar4 = FUN_ffff000090731b4c(lVar7), -1 < iVar4)) {
    iVar4 = FUN_ffff0000907320fc(s_md_mem_layout_ffff0000907f11ec,lVar8,uVar5 * 0x18);
    if (iVar4 < 0) {
      pcVar9 = s__s__d___CCCI_Add_memory_layout_t_ffff0000907d603e;
      uVar10 = 0x125;
    }
    else {
      iVar4 = FUN_ffff0000907320fc(s_md_bank0_base_ffff0000907eaae6,lVar8 + 0x10,8);
      if (-1 < iVar4) goto LAB_ffff00009072fd3c;
      pcVar9 = s__s__d___CCCI_Add_md_bank0_base_t_ffff0000907f465e;
      uVar10 = 300;
    }
    FUN_ffff00009077de54(pcVar9,s_add_md_memory_layout_to_tag_inf_ffff0000907f7fde,uVar10);
LAB_ffff00009072fd3c:
    uVar10 = DAT_ffff000090839908;
    puVar14 = (undefined8 *)(lVar8 + 0x10);
    local_b0 = (uint *)0x0;
    do {
      if (0x1f < _DAT_ffff00009086d780) {
        FUN_ffff00009077de54
                  (s__s__d___CCCI_Freeable_memory_tab_ffff0000907ddf50,
                   s_update_freeable_memory_table_ffff0000907d606a,0x140);
        break;
      }
      if ((*(uint *)((long)puVar14 + -4) >> 4 & 1) == 0) {
        if ((*(uint *)((long)puVar14 + -4) >> 2 & 1) != 0) {
          FUN_ffff00009077de54
                    (s__s__d___CCCI_memory_init_set_0x__ffff0000907d7805,
                     s_update_freeable_memory_table_ffff0000907d606a,0x14c,*puVar14,
                     *(undefined4 *)((long)puVar14 + -0xc));
          iVar4 = FUN_ffff00009077b79c
                            (uVar10,s_md_padding_ffff0000907f4687,
                             (ulong)*(uint *)((long)puVar14 + -0xc) + 0xfff & 0x1fffff000,&local_b0,
                             0xc,*puVar14,0,0);
          if (iVar4 == 0) {
            FUN_ffff00009077eb88(local_b0,0,*(undefined4 *)((long)puVar14 + -0xc));
            FUN_ffff000090707cc4(local_b0,*(undefined4 *)((long)puVar14 + -0xc));
            FUN_ffff00009077bb68(uVar10,local_b0);
          }
          else {
            FUN_ffff00009077de54
                      (s__s__d___CCCI_ccci__vmm_alloc_phy_ffff0000907fb513,
                       s_update_freeable_memory_table_ffff0000907d606a,0x156,iVar4);
          }
        }
        if ((*(int *)((long)puVar14 + -4) == 0) && (*(int *)(puVar14 + -1) == 0))
        goto LAB_ffff00009072fd80;
      }
      else {
LAB_ffff00009072fd80:
        uVar13 = (ulong)_DAT_ffff00009086d780;
        _DAT_ffff00009086d780 = _DAT_ffff00009086d780 + 1;
        *(undefined8 *)(&DAT_ffff00009086d788 + uVar13 * 0x10) = *puVar14;
        *(undefined4 *)(&DAT_ffff00009086d790 + uVar13 * 0x10) =
             *(undefined4 *)((long)puVar14 + -0xc);
      }
      puVar14 = puVar14 + 3;
      uVar15 = uVar15 - 1;
    } while (uVar15 != 0);
    FUN_ffff0000907302dc();
    FUN_ffff00009072dc00(lVar7);
    return 0;
  }
  goto LAB_ffff00009072fc48;
}



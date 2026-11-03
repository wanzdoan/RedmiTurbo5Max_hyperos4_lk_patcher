
undefined8 FUN_ffff00009072f8ac(void)

{
  undefined *puVar1;
  uint *puVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  uint uVar6;
  uint *puVar7;
  long lVar8;
  long lVar9;
  char *pcVar10;
  undefined8 uVar11;
  undefined8 uVar12;
  uint *puVar13;
  ulong uVar14;
  undefined8 *puVar15;
  ulong uVar16;
  long lVar17;
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
  puVar7 = (uint *)FUN_ffff00009072f68c();
  if (((puVar7 == (uint *)0x0) && (puVar7 = (uint *)FUN_ffff00009072dec8(), puVar7 == (uint *)0x0))
     && (puVar7 = (uint *)FUN_ffff00009072ded8(), puVar7 == (uint *)0x0)) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_use_old_partion_nam_ffff0000907f630e,s_get_dl_tbl_ffff0000907e2cb2,
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
    FUN_ffff0000907352f4(s_md1img_ffff0000907f3f58,&local_b0,0x48);
    lVar8 = FUN_ffff00009077c028(&local_b0);
    if (lVar8 == 0) {
LAB_ffff00009072f994:
      FUN_ffff00009077e11c
                (s__s__d___CCCI_replace_the_partiti_ffff0000907e488b,
                 s_check_md1rom_image_name_ffff0000907eac68,0xb7);
      puVar7 = (uint *)0xffff000090819228;
    }
    else {
      FUN_ffff00009077bfac();
      puVar7 = (uint *)0xffff0000908191a8;
    }
  }
  else if (puVar7[6] != 1) {
    local_78 = 0;
    uStack_70 = 0;
    local_88 = 0;
    uStack_80 = 0;
    local_98 = 0;
    uStack_90 = 0;
    local_a8 = 0;
    uStack_a0 = 0;
    local_b0 = (uint *)0x0;
    FUN_ffff0000907352f4(s_md1img_ffff0000907f3f58,&local_b0,0x48);
    lVar8 = FUN_ffff00009077c028(&local_b0);
    if (lVar8 == 0) goto LAB_ffff00009072f994;
    FUN_ffff00009077bfac();
  }
  iVar3 = FUN_ffff00009072def8();
  iVar5 = 0x20;
  if (-1 < iVar3) {
    iVar5 = iVar3;
  }
  iVar4 = FUN_ffff00009072dee8();
  iVar3 = 0x20000000;
  if (iVar4 != 0) {
    iVar3 = iVar4;
  }
  iVar5 = FUN_ffff0000907303fc(iVar5);
  if (iVar5 < 0) {
    return 0xffffffff;
  }
  lVar8 = FUN_ffff00009072dc88(s_md_mem_usage_ffff0000907d0f95,iVar3,&local_b8);
  if (lVar8 != 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_Memory_base___p_va__ffff0000907ee112,
               s_load_soc_modem_image_only_ffff0000907f8129,0x264,local_b8,lVar8,iVar3);
    iVar5 = FUN_ffff00009073063c(iVar3);
    lVar9 = local_b8;
    puVar2 = puVar7;
    if (-1 < iVar5) {
      do {
        puVar13 = puVar2;
        puVar2 = puVar13 + 8;
      } while (1 < *puVar13);
      if (*puVar13 == 1) {
        local_b0 = (uint *)0x0;
        uVar11 = *(undefined8 *)(puVar13 + 2);
        uVar12 = *(undefined8 *)(puVar13 + 4);
        FUN_ffff00009077e11c
                  (s__s__d___CCCI_Load_image____s__fr_ffff0000907e11f0,
                   s_load_image_by_name_ffff0000907d91c6,0xe0,uVar12,uVar11,local_b8,iVar3);
        FUN_ffff000090707c90(lVar9,iVar3);
        iVar5 = FUN_ffff000090785d18(uVar11,uVar12,lVar9,iVar3);
        if (iVar5 < 0) {
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_Load_image____s__fa_ffff0000907d1046,
                     s_load_image_by_name_ffff0000907d91c6,0xe9,uVar12,uVar11,lVar9,iVar3,iVar5);
        }
        else {
          FUN_ffff000090707c50(lVar9,iVar5);
          iVar5 = FUN_ffff00009072e484(lVar9,iVar5,&local_b0);
          if (-1 < iVar5) {
            FUN_ffff00009072de8c(local_b0);
            puVar7 = puVar7 + 4;
LAB_ffff00009072fb30:
            switch(puVar7[-4]) {
            case 0:
              goto code_r0xffff00009072ff1c;
            case 1:
              break;
            case 2:
              local_b0 = (uint *)0x0;
              iVar5 = FUN_ffff000090730124(2,&local_b0,iVar3);
              puVar2 = local_b0;
              if (iVar5 < 0) goto LAB_ffff00009072fee0;
              if (iVar5 != 0) {
                uVar11 = *(undefined8 *)(puVar7 + -2);
                uVar12 = *(undefined8 *)puVar7;
                uVar6 = local_b0[1];
                lVar17 = lVar9 + (ulong)*local_b0;
                FUN_ffff00009077e11c
                          (s__s__d___CCCI_Load_image____s__fr_ffff0000907e11f0,
                           s_load_image_by_name_ffff0000907d91c6,0xe0,uVar12,uVar11,lVar17,uVar6);
                FUN_ffff000090707c90(lVar17,uVar6);
                iVar5 = FUN_ffff000090785d18(uVar11,uVar12,lVar17,uVar6);
                if (iVar5 < 0) {
                  FUN_ffff00009077e11c
                            (s__s__d___CCCI_Load_image____s__fa_ffff0000907d1046,
                             s_load_image_by_name_ffff0000907d91c6,0xe9,uVar12,uVar11,lVar17,uVar6,
                             iVar5);
                  if (((byte)puVar7[2] >> 1 & 1) == 0) {
                    FUN_ffff000090731574();
                    goto LAB_ffff00009072fee0;
                  }
                  FUN_ffff00009077e11c
                            (s__s__d___CCCI_skip_optional_sub_i_ffff0000907ee149,
                             s_load_dsp_image_ffff0000907d4489,0x1ac,*(undefined8 *)puVar7);
                  puVar2[3] = puVar2[3] | 0x100;
                  puVar7 = puVar7 + 8;
                  goto LAB_ffff00009072fb30;
                }
                FUN_ffff000090707c50(lVar17,iVar5);
                iVar5 = FUN_ffff00009072ed10(lVar17,iVar5,*puVar2);
                if (iVar5 < 0) goto LAB_ffff00009072fee0;
              }
              break;
            case 3:
              local_b0 = (uint *)0x0;
              iVar5 = FUN_ffff000090730124(0x80,&local_b0,iVar3);
              if (iVar5 < 0) goto LAB_ffff00009072fee0;
              if (iVar5 != 0) {
                uVar11 = *(undefined8 *)(puVar7 + -2);
                uVar12 = *(undefined8 *)puVar7;
                uVar6 = local_b0[1];
                lVar17 = lVar9 + (ulong)*local_b0;
                FUN_ffff00009077e11c
                          (s__s__d___CCCI_Load_image____s__fr_ffff0000907e11f0,
                           s_load_image_by_name_ffff0000907d91c6,0xe0,uVar12,uVar11,lVar17,uVar6);
                FUN_ffff000090707c90(lVar17,uVar6);
                iVar5 = FUN_ffff000090785d18(uVar11,uVar12,lVar17,uVar6);
                if (-1 < iVar5) {
LAB_ffff00009072fcd8:
                  FUN_ffff000090707c50(lVar17,iVar5);
                  puVar7 = puVar7 + 8;
                  goto LAB_ffff00009072fb30;
                }
                FUN_ffff00009077e11c
                          (s__s__d___CCCI_Load_image____s__fa_ffff0000907d1046,
                           s_load_image_by_name_ffff0000907d91c6,0xe9,uVar12,uVar11,lVar17,uVar6);
                if (((byte)puVar7[2] >> 1 & 1) == 0) {
LAB_ffff00009072ffa8:
                  FUN_ffff000090731574();
                  goto LAB_ffff00009072fee0;
                }
                uVar12 = *(undefined8 *)puVar7;
                pcVar10 = s_load_ramdisk_image_ffff0000907f1361;
                uVar11 = 0x1ff;
LAB_ffff00009072feac:
                FUN_ffff00009077e11c
                          (s__s__d___CCCI_skip_optional_sub_i_ffff0000907ee149,pcVar10,uVar11,uVar12
                          );
              }
              break;
            case 4:
              local_64 = 0;
              iVar5 = FUN_ffff0000907324a4(s_drdi_version_ffff0000907d77ec,&local_64,4);
              if (iVar5 == 4) {
                FUN_ffff00009077e11c
                          (s__s__d___CCCI___s___drdi_version___ffff0000907e6110,
                           s_load_drdi_image_ffff0000907d4498,0x1c5,
                           s_load_drdi_image_ffff0000907d4498,local_64);
                if (local_64 == 3) break;
              }
              else {
                FUN_ffff000090730228
                          (s__CCCI___s__d____s__error___drdi__ffff0000907d0eab,
                           s_load_drdi_image_ffff0000907d4498,0x1c3,
                           s_load_drdi_image_ffff0000907d4498);
              }
              local_b0 = (uint *)0x0;
              iVar5 = FUN_ffff000090730124(0x40,&local_b0,iVar3);
              if (iVar5 < 0) goto LAB_ffff00009072fee0;
              if (iVar5 != 0) {
                if ((*local_b0 == 0) || (local_b0[1] == 0)) {
                  FUN_ffff00009077e11c
                            (s__s__d___CCCI_DRDI_v1_not_enable_ffff0000907e92b6,
                             s_load_drdi_image_ffff0000907d4498,0x1d1);
                  puVar7 = puVar7 + 8;
                  goto LAB_ffff00009072fb30;
                }
                iVar5 = FUN_ffff000090730124(0x20,&local_b0,iVar3);
                if (iVar5 < 0) goto LAB_ffff00009072fee0;
                if (iVar5 != 0) {
                  uVar11 = *(undefined8 *)(puVar7 + -2);
                  uVar12 = *(undefined8 *)puVar7;
                  uVar6 = local_b0[1];
                  lVar17 = lVar9 + (ulong)*local_b0;
                  FUN_ffff00009077e11c
                            (s__s__d___CCCI_Load_image____s__fr_ffff0000907e11f0,
                             s_load_image_by_name_ffff0000907d91c6,0xe0,uVar12,uVar11,lVar17,uVar6);
                  FUN_ffff000090707c90(lVar17,uVar6);
                  iVar5 = FUN_ffff000090785d18(uVar11,uVar12,lVar17,uVar6);
                  if (-1 < iVar5) goto LAB_ffff00009072fcd8;
                  FUN_ffff00009077e11c
                            (s__s__d___CCCI_Load_image____s__fa_ffff0000907d1046,
                             s_load_image_by_name_ffff0000907d91c6,0xe9,uVar12,uVar11,lVar17,uVar6);
                  if (((byte)puVar7[2] >> 1 & 1) == 0) goto LAB_ffff00009072ffa8;
                  uVar12 = *(undefined8 *)puVar7;
                  pcVar10 = s_load_drdi_image_ffff0000907d4498;
                  uVar11 = 0x1e1;
                  goto LAB_ffff00009072feac;
                }
              }
              break;
            default:
              goto switchD_ffff00009072fb48_default;
            }
            puVar7 = puVar7 + 8;
            goto LAB_ffff00009072fb30;
          }
        }
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d___CCCI_Main_image_info_not_ffff0000907ff02f,
                   s_find_main_img_ffff0000907d447b,0x211);
      }
    }
  }
LAB_ffff00009072fee0:
  FUN_ffff00009072de3c(lVar8,iVar3);
  FUN_ffff000090730574();
  return 0xffffffff;
switchD_ffff00009072fb48_default:
  FUN_ffff00009077e11c
            (s__s__d___CCCI_Not_support_image_t_ffff000090800ac8,s_load_sub_img_ffff0000907df927,
             0x22e);
  puVar7 = puVar7 + 8;
  goto LAB_ffff00009072fb30;
code_r0xffff00009072ff1c:
  DAT_ffff00009086d7b8 = lVar8;
  uVar6 = FUN_ffff0000907311c0(lVar8);
  if ((int)uVar6 < 1) {
    return 0xffffffff;
  }
  uVar16 = (ulong)uVar6;
  lVar9 = FUN_ffff0000907305ec();
  if (((lVar9 != 0) && (iVar5 = FUN_ffff0000907316c4(lVar9,uVar16), -1 < iVar5)) &&
     (iVar5 = FUN_ffff000090731de4(lVar8), -1 < iVar5)) {
    iVar5 = FUN_ffff000090732394(s_md_mem_layout_ffff0000907f1374,lVar9,uVar6 * 0x18);
    if (iVar5 < 0) {
      pcVar10 = s__s__d___CCCI_Add_memory_layout_t_ffff0000907d60d4;
      uVar11 = 0x125;
    }
    else {
      iVar5 = FUN_ffff000090732394(s_md_bank0_base_ffff0000907eac80,lVar9 + 0x10,8);
      if (-1 < iVar5) goto LAB_ffff00009072ffd4;
      pcVar10 = s__s__d___CCCI_Add_md_bank0_base_t_ffff0000907f47e2;
      uVar11 = 300;
    }
    FUN_ffff00009077e11c(pcVar10,s_add_md_memory_layout_to_tag_inf_ffff0000907f817a,uVar11);
LAB_ffff00009072ffd4:
    puVar1 = PTR_DAT_ffff000090839928;
    puVar15 = (undefined8 *)(lVar9 + 0x10);
    local_b0 = (uint *)0x0;
    do {
      if (0x1f < DAT_ffff00009086d818) {
        FUN_ffff00009077e11c
                  (s__s__d___CCCI_Freeable_memory_tab_ffff0000907de00a,
                   s_update_freeable_memory_table_ffff0000907d6100,0x140);
        break;
      }
      if ((*(uint *)((long)puVar15 + -4) >> 4 & 1) == 0) {
        if ((*(uint *)((long)puVar15 + -4) >> 2 & 1) != 0) {
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_memory_init_set_0x__ffff0000907d789b,
                     s_update_freeable_memory_table_ffff0000907d6100,0x14c,*puVar15,
                     *(undefined4 *)((long)puVar15 + -0xc));
          iVar5 = FUN_ffff00009077ba64
                            (puVar1,s_md_padding_ffff0000907f480b,
                             (ulong)*(uint *)((long)puVar15 + -0xc) + 0xfff & 0x1fffff000,&local_b0,
                             0xc,*puVar15,0,0);
          if (iVar5 == 0) {
            FUN_ffff00009077ee50(local_b0,0,*(undefined4 *)((long)puVar15 + -0xc));
            FUN_ffff000090707c50(local_b0,*(undefined4 *)((long)puVar15 + -0xc));
            FUN_ffff00009077be30(puVar1,local_b0);
          }
          else {
            FUN_ffff00009077e11c
                      (s__s__d___CCCI_ccci__vmm_alloc_phy_ffff0000907fb6af,
                       s_update_freeable_memory_table_ffff0000907d6100,0x156,iVar5);
          }
        }
        if ((*(int *)((long)puVar15 + -4) == 0) && (*(int *)(puVar15 + -1) == 0))
        goto LAB_ffff000090730018;
      }
      else {
LAB_ffff000090730018:
        uVar14 = (ulong)DAT_ffff00009086d818;
        DAT_ffff00009086d818 = DAT_ffff00009086d818 + 1;
        (&DAT_ffff00009086d820)[uVar14 * 2] = *puVar15;
        (&DAT_ffff00009086d828)[uVar14 * 4] = *(undefined4 *)((long)puVar15 + -0xc);
      }
      puVar15 = puVar15 + 3;
      uVar16 = uVar16 - 1;
    } while (uVar16 != 0);
    FUN_ffff000090730574();
    FUN_ffff00009072de98(lVar8);
    return 0;
  }
  goto LAB_ffff00009072fee0;
}



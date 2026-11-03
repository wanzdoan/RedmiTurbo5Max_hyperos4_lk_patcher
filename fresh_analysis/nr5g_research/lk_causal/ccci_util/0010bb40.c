
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void collect_lk_boot_arguments(void)

{
  undefined4 *puVar1;
  uint uVar2;
  undefined4 uVar3;
  uint uVar4;
  int iVar5;
  int iVar6;
  uint uVar7;
  long lVar8;
  long *plVar9;
  undefined8 uVar10;
  undefined *puVar11;
  char *pcVar12;
  ulong uVar13;
  uint local_a8;
  int local_a4;
  undefined8 local_a0;
  long lStack_98;
  long local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 local_78;
  undefined8 uStack_70;
  long local_68;
  
  lVar8 = sp_el0;
  local_68 = *(long *)(lVar8 + 0x620);
  lVar8 = of_find_compatible_node(0,0,"mediatek,mddriver");
  if (lVar8 == 0) {
    pcVar12 = "[0/util]device node no mediatek,mddriver node\n";
  }
  else {
    plVar9 = (long *)of_get_property(lVar8,"ccci,modem_info_v2",0);
    if (plVar9 != (long *)0x0) {
      lVar8 = *plVar9;
      uVar7 = *(uint *)(plVar9 + 1);
      iVar5 = *(int *)((long)plVar9 + 0xc);
      uVar2 = *(uint *)(plVar9 + 2);
      uVar3 = *(undefined4 *)((long)plVar9 + 0x14);
      uVar4 = *(uint *)((long)plVar9 + 0x1c);
      ccci_dump_write(0,1,"[0/util]lk info.lk_info_base_addr: 0x%llX\n",lVar8);
      ccci_dump_write(2,1,"[0/util]lk info.lk_info_base_addr: 0x%llX\n",lVar8);
      ccci_dump_write(0,1,"[0/util]lk info.lk_info_size:      0x%x\n",uVar7);
      ccci_dump_write(2,1,"[0/util]lk info.lk_info_size:      0x%x\n",uVar7);
      ccci_dump_write(0,1,"[0/util]lk info.lk_info_tag_num:   0x%x\n",uVar3);
      ccci_dump_write(2,1,"[0/util]lk info.lk_info_tag_num:   0x%x\n",uVar3);
      s_g_lk_ld_md_errno = iVar5;
      lk_load_img_err_no = uVar4;
      s_g_tag_inf_size = uVar7;
      if ((lVar8 == 0) && (iVar5 == 0)) {
        ccci_dump_write(0,1,"[0/util]no image enabled\n");
        _printk(&DAT_0010f62d);
        uVar10 = 0;
        s_g_lk_inf_base = 0;
        s_g_lk_load_img_status = 0;
        goto LAB_0010ca90;
      }
      if (lVar8 == 0) {
        puVar11 = &UNK_0010ee10;
        uVar7 = 4;
        pcVar12 = "[0/util]no image load success\n";
LAB_0010cc1c:
        ccci_dump_write(0,1,pcVar12);
        _printk(puVar11);
        s_g_lk_load_img_status = s_g_lk_load_img_status | uVar7;
      }
      else {
        if (0x10000 < uVar7) {
          puVar11 = &UNK_0011080b;
          uVar7 = 0x20;
          pcVar12 = "[0/util]tag info mem size too large\n";
          goto LAB_0010cc1c;
        }
        s_g_tag_cnt = uVar3;
        s_g_lk_info_tag_version = uVar2;
        s_g_lk_inf_base = ccci_map_phy_addr(lVar8,0x10000);
LAB_0010bdc0:
        if (s_g_lk_inf_base == 0) {
          puVar11 = &DAT_00112897;
          uVar7 = 4;
          pcVar12 = "[0/util]remap lk info buf fail\n";
          goto LAB_0010cc1c;
        }
      }
      s_g_md_usage_case = 1;
      lk_tag_inf_parsing_to_args_table();
      mtk_ccci_dump_md_mem_layout();
      mtk_ccci_md_smem_layout_init();
      local_a0 = (ulong)local_a0._4_4_ << 0x20;
      local_90 = 0;
      local_88 = 0;
      local_80 = 0;
      iVar5 = mtk_ccci_find_args_val("hdr_count",&local_a0,4);
      if (iVar5 == 4) {
        iVar5 = mtk_ccci_find_args_val("hdr_tbl_inf",&local_90,0x18);
        if (iVar5 < 0) {
          ccci_dump_write(0,1,"[0/util][hdr_tbl_inf] get fail, will pasing invalid data\n");
          ccci_dump_write(2,1,"[0/util][hdr_tbl_inf] get fail, will pasing invalid data\n");
        }
        if (md_resv_mem_size == 0) {
          md_resv_mem_size = (int)local_88;
          lk_load_img_err_no = local_88._5_1_ | 0xffffff00;
          md_resv_mem_addr = local_90;
          if (-1 < (char)local_88._5_1_) {
            lk_load_img_err_no = (uint)local_88._5_1_;
          }
          ccci_dump_write(0,1,"[0/util]md lk_load_img_err_no: %d\n");
          ccci_dump_write(2,1,"[0/util]md lk_load_img_err_no: %d\n",lk_load_img_err_no);
          if (lk_load_img_err_no == 0) {
            s_g_md_env_rdy_flag = 1;
          }
          md_type_at_lk = (uint)local_88._6_1_;
          ccci_dump_write(0,1,"[0/util]md MemStart: 0x%016llx, MemSize:0x%08X\n",md_resv_mem_addr,
                          md_resv_mem_size);
          ccci_dump_write(2,1,"[0/util]md MemStart: 0x%016llx, MemSize:0x%08X\n",md_resv_mem_addr,
                          md_resv_mem_size);
        }
        else {
          ccci_dump_write(0,1,"[0/util]Invalid dt para\n");
          _printk(&DAT_001114dd);
          s_g_lk_load_img_status = s_g_lk_load_img_status | 2;
        }
      }
      else {
        ccci_dump_write(0,1,"[0/util]get hdr_count fail\n");
        _printk(&DAT_00110bc3);
        s_g_lk_load_img_status = s_g_lk_load_img_status | 4;
      }
      lVar8 = md_resv_mem_addr;
      local_a0 = 0;
      lStack_98 = 0;
      local_80 = 0;
      local_78 = 0;
      local_90 = 0;
      local_88 = 0;
      local_a8 = 0xffffffff;
      local_a4 = 0;
      iVar5 = mtk_ccci_find_args_val("retrieve_num",&local_a4,4);
      if (iVar5 < 0) {
        ccci_dump_write(0,1,"[0/util]Retrieve_num NA.\n");
        _printk(&DAT_0010fe91);
      }
      else {
        ccci_dump_write(0,1,"[0/util]retrieve number is %d.\n",local_a4);
        ccci_dump_write(2,1,"[0/util]retrieve number is %d.\n",local_a4);
        if (0 < local_a4) {
          iVar5 = 0;
          do {
            scnprintf(&local_90,0x20,"retrieve%d",iVar5);
            iVar6 = mtk_ccci_find_args_val(&local_90,&local_a0,0x10);
            if (iVar6 != 0) {
              ccci_dump_write(0,1,"[0/util]AP view(0x%llx ~ 0x%llx), MD view(0x%llx ~ 0x%llx)\n",
                              local_a0,lStack_98 + local_a0,local_a0 - lVar8,
                              (lStack_98 + local_a0) - lVar8);
              ccci_dump_write(2,1,"[0/util]AP view(0x%llx ~ 0x%llx), MD view(0x%llx ~ 0x%llx)\n",
                              local_a0,lStack_98 + local_a0,local_a0 - lVar8,
                              (lStack_98 + local_a0) - lVar8);
              iVar6 = mtk_ccci_find_args_val("free_in_kernel",&local_a8,4);
              if ((iVar6 == 0) || (local_a8 != 1)) {
                ccci_dump_write(0,1,"[0/util]no free_in_kernel found, free_in_kernel=%d\n");
                pcVar12 = "[0/util]no free_in_kernel found, free_in_kernel=%d\n";
                uVar7 = local_a8;
              }
              else {
                uVar7 = free_reserved_memory_ccci(local_a0,lStack_98 + local_a0);
                ccci_dump_write(0,1,"[0/util]free_reserved_memory result=%d\n",uVar7);
                pcVar12 = "[0/util]free_reserved_memory result=%d\n";
              }
              ccci_dump_write(2,1,pcVar12,uVar7);
            }
            iVar5 = iVar5 + 1;
          } while (iVar5 < local_a4);
        }
      }
      if (s_g_md_usage_case == 1) {
        md1_check_hdr_info = kmalloc_trace(_kobject_put,0xcc0,0x400);
        if (md1_check_hdr_info == 0) {
          ccci_dump_write(0,1,"[0/util]alloc check header memory fail(MD1)\n");
          _printk(&DAT_0010fee5);
          s_g_md_env_rdy_flag = 0;
        }
        else {
          iVar5 = mtk_ccci_find_args_val("md1_chk",md1_check_hdr_info,0x400);
          if (((iVar5 == 0xbc) || (iVar5 == 0x158)) || (iVar5 == 0x200)) {
            md1_check_hdr_info_size = iVar5;
            iVar5 = mtk_ccci_find_args_val("md1img",&md1_raw_img_size,4);
            if (iVar5 < 0) {
              ccci_dump_write(0,1,"[0/util]key [md1img] get fail, parsing is not safe\n");
              _printk(&DAT_00111870);
            }
          }
          else {
            ccci_dump_write(0,1,"[0/util]get md1 chk header info fail\n");
            _printk(&DAT_00111ece);
            s_g_md_env_rdy_flag = 0;
            s_g_lk_load_img_status = s_g_lk_load_img_status | 8;
          }
        }
      }
      local_78 = 0;
      uStack_70 = 0;
      local_88 = 0;
      local_80 = 0;
      local_90 = 0;
      iVar5 = mtk_ccci_find_args_val("smem_layout",&local_90,0x28);
      if (iVar5 == 0x28) {
        ccb_info = 0;
        _DAT_0010cdc0 = 0;
        iVar5 = mtk_ccci_find_args_val("ccb_info",&ccb_info,0x10);
        if (iVar5 != 0x10) {
          ccci_dump_write(0,1,"[0/util]Invalid ccb info dt para\n");
          _printk(&DAT_0010ea7c);
        }
        ccci_dump_write(0,1,"[0/util]ccci_util get ccb: data:%llx data_size:%d\n",ccb_info,
                        _DAT_0010cdc0 & 0xffffffff);
        ccci_dump_write(2,1,"[0/util]ccci_util get ccb: data:%llx data_size:%d\n",ccb_info,
                        _DAT_0010cdc0 & 0xffffffff);
        _udc_size = 0;
        iVar5 = mtk_ccci_find_args_val("udc_layout",&udc_size,8);
        if (iVar5 != 8) {
          ccci_dump_write(0,1,"[0/util]Invalid udc layout info dt para\n");
          _printk(&DAT_001126a0);
        }
        ccci_dump_write(0,1,"[0/util]ccci_util get udc: cache_size:0x%x noncache_size:0x%x\n",
                        DAT_0010cdcc,_udc_size & 0xffffffff);
        ccci_dump_write(2,1,"[0/util]ccci_util get udc: cache_size:0x%x noncache_size:0x%x\n",
                        DAT_0010cdcc,_udc_size & 0xffffffff);
        iVar5 = mtk_ccci_find_args_val("md1_sib_info",&sib_info,0x10);
        if (iVar5 != 0x10) {
          ccci_dump_write(0,1,"[0/util]get sib info fail\n");
          _printk(&DAT_0010eaa4);
        }
        ccci_dump_write(0,1,"[0/util]ccci_util get sib addr: 0x%llx size: %d\n",sib_info,
                        DAT_0010cde0);
        ccci_dump_write(2,1,"[0/util]ccci_util get sib addr: 0x%llx size: %d\n",sib_info,
                        DAT_0010cde0);
        iVar5 = mtk_ccci_find_args_val("md1_phy_cap",&md1_phy_cap_size,4);
        if (iVar5 != 4) {
          ccci_dump_write(0,1,"[0/util]using 0 as phy capture size\n");
          _printk(&DAT_0010ff25);
        }
        ccci_dump_write(0,1,"[0/util]ccci_util get md1_phy_cap_size: 0x%x\n",md1_phy_cap_size);
        ccci_dump_write(2,1,"[0/util]ccci_util get md1_phy_cap_size: 0x%x\n",md1_phy_cap_size);
        iVar5 = mtk_ccci_find_args_val("smem_dfd_size",&md1_smem_dfd_size,4);
        if (iVar5 != 4) {
          ccci_dump_write(0,1,"[0/util]get smem dfd size fail\n");
          _printk(&DAT_0010ff50);
        }
        ccci_dump_write(0,1,"[0/util]ccci_util get md1_smem_dfd_size: %d\n",md1_smem_dfd_size);
        ccci_dump_write(2,1,"[0/util]ccci_util get md1_smem_dfd_size: %d\n",md1_smem_dfd_size);
        iVar5 = mtk_ccci_find_args_val("smem_amms_pos_size",&smem_amms_pos_size,4);
        if (iVar5 != 4) {
          ccci_dump_write(0,1,"[0/util]get smem amms pos size fail\n");
          _printk(&DAT_0010fc8e);
        }
        ccci_dump_write(0,1,"[0/util]ccci_util get smem_amms_pos_size: %d\n",smem_amms_pos_size);
        ccci_dump_write(2,1,"[0/util]ccci_util get smem_amms_pos_size: %d\n",smem_amms_pos_size);
        iVar5 = mtk_ccci_find_args_val("smem_align_padding_size",&smem_align_padding_size,4);
        if (iVar5 != 4) {
          ccci_dump_write(0,1,"[0/util]get smem align padding size fail\n");
          _printk(&DAT_001124a8);
        }
        ccci_dump_write(0,1,"[0/util]ccci_util get smem_align_padding_size: %d\n",
                        smem_align_padding_size);
        ccci_dump_write(2,1,"[0/util]ccci_util get smem_align_padding_size: %d\n",
                        smem_align_padding_size);
        md1_bank4_cache_offset = 0;
        iVar5 = mtk_ccci_find_args_val("md1_smem_cahce_offset",&md1_bank4_cache_offset,4);
        if (iVar5 != 4) {
          md1_bank4_cache_offset = 0x8000000;
        }
        ccci_dump_write(0,1,"[0/util]smem cachable offset 0x%X\n",md1_bank4_cache_offset);
        ccci_dump_write(2,1,"[0/util]smem cachable offset 0x%X\n",md1_bank4_cache_offset);
        md_resv_smem_addr = local_90 + (local_88 & 0xffffffff);
        md_resv_smem_size = local_88._4_4_;
        ccci_dump_write(0,1,"[0/util]AP  <--> MD1 SMEM(0x%08X):%016llx~%016llx\n",
                        (ulong)local_88._4_4_,md_resv_smem_addr,
                        (ulong)local_88._4_4_ + md_resv_smem_addr + -1);
        ccci_dump_write(2,1,"[0/util]AP  <--> MD1 SMEM(0x%08X):%016llx~%016llx\n",
                        (ulong)md_resv_smem_size,md_resv_smem_addr,
                        (ulong)md_resv_smem_size + md_resv_smem_addr + -1);
        iVar5 = mtk_ccci_find_args_val("mtee_support",&md_mtee_support,4);
        if (iVar5 == 4) {
          ccci_dump_write(0,1,"[0/util]MTEE support: 0x%x\n",md_mtee_support);
          ccci_dump_write(2,1,"[0/util]MTEE support: 0x%x\n",md_mtee_support);
        }
        else {
          ccci_dump_write(0,1,"[0/util]using 0 as MTEE support\n");
          _printk(&DAT_001116bf);
        }
        local_a0 = local_a0 & 0xffffffff00000000;
        iVar5 = mtk_ccci_find_args_val("nc_smem_info_ext_num",&local_a0,4);
        if (iVar5 == 4) {
          iVar5 = (int)local_a0 * 0x10;
          s_nc_smem_ext_num = (int)local_a0;
          s_nc_layout = __kmalloc(iVar5,0xdc0);
          if (s_nc_layout == 0) {
            ccci_dump_write(0,1,"[0/util]nc_layout:alloc nc_layout fail\n");
            puVar11 = &DAT_0010ec39;
          }
          else {
            iVar6 = mtk_ccci_find_args_val("nc_smem_info_ext",s_nc_layout,iVar5);
            if (iVar6 == iVar5) {
              if ((int)local_a0 != 0) {
                lVar8 = 0;
                uVar13 = 0;
                do {
                  puVar1 = (undefined4 *)(s_nc_layout + lVar8);
                  ccci_dump_write(0,1,"[0/util]nc_smem<%d>: ap:0x%08x md:0x%08x[0x%08x]\n",puVar1[3]
                                  ,*puVar1,puVar1[1],puVar1[2]);
                  puVar1 = (undefined4 *)(s_nc_layout + lVar8);
                  ccci_dump_write(2,1,"[0/util]nc_smem<%d>: ap:0x%08x md:0x%08x[0x%08x]\n",puVar1[3]
                                  ,*puVar1,puVar1[1],puVar1[2]);
                  uVar13 = uVar13 + 1;
                  lVar8 = lVar8 + 0x10;
                } while (uVar13 < (local_a0 & 0xffffffff));
              }
              if ((s_nc_layout != 0) && (s_nc_smem_ext_num != 0)) {
                iVar5 = 0;
                do {
                  if (*(int *)(s_nc_layout + (long)iVar5 * 0x10 + 0xc) == 10) {
                    md1_smem_dfd_size = *(undefined4 *)(s_nc_layout + (long)iVar5 * 0x10 + 8);
                    ccci_dump_write(0,1,"[0/util]change dfd to: 0x%x\n");
                    ccci_dump_write(2,1,"[0/util]change dfd to: 0x%x\n",md1_smem_dfd_size);
                    if (s_nc_layout == 0) goto LAB_0010c8ec;
                    goto LAB_0010c604;
                  }
                  iVar5 = iVar5 + 1;
                } while (s_nc_smem_ext_num != iVar5);
              }
              if (s_nc_layout != 0) {
LAB_0010c604:
                if (s_nc_smem_ext_num != 0) {
                  iVar5 = 0;
                  do {
                    if (*(int *)(s_nc_layout + (long)iVar5 * 0x10 + 0xc) == 0x1d) {
                      smem_amms_pos_size = *(undefined4 *)(s_nc_layout + (long)iVar5 * 0x10 + 8);
                      ccci_dump_write(0,1,"[0/util]change POS to: 0x%x\n");
                      ccci_dump_write(2,1,"[0/util]change POS to: 0x%x\n",smem_amms_pos_size);
                      break;
                    }
                    iVar5 = iVar5 + 1;
                  } while (s_nc_smem_ext_num != iVar5);
                }
              }
              goto LAB_0010c8ec;
            }
            ccci_dump_write(0,1,"[0/util]Invalid nc_layout from tag\n");
            puVar11 = &DAT_00110dd3;
          }
          _printk(puVar11);
        }
        else {
          ccci_dump_write(0,1,"[0/util]nc_smem_info_ext_num get fail\n");
          _printk(&DAT_00111bc2);
          s_nc_smem_ext_num = 0;
        }
LAB_0010c8ec:
        csmem_info = 0;
        DAT_0010cdf8 = 0;
        DAT_0010ce00 = 0;
        iVar5 = mtk_ccci_find_args_val("md1_bank4_cache_info",&csmem_info,0x18);
        if (iVar5 == 0x18) {
          iVar5 = (uint)DAT_0010ce00 * 0x18;
          plVar9 = (long *)__kmalloc(iVar5,0xdc0);
          if (plVar9 == (long *)0x0) {
LAB_0010cb74:
            csmem_layout = plVar9;
            ccci_dump_write(plVar9,1,"[0/util]csmem_layout:alloc csmem_layout fail\n");
            puVar11 = &DAT_0010facd;
          }
          else {
            csmem_layout = plVar9;
            iVar6 = mtk_ccci_find_args_val("md1_bank4_cache_layout",plVar9,iVar5);
            if (iVar6 == iVar5) goto LAB_0010c95c;
            ccci_dump_write(0,1,"[0/util]Invalid csmem_layout dt para\n");
            puVar11 = &DAT_00111f23;
          }
          _printk(puVar11);
        }
        else {
          ccci_dump_write(0,1,"[0/util]Invalid csmem_info dt para\n");
          _printk(&DAT_001124d8);
          if (ccb_info != 0) {
            csmem_info = ccb_info;
            DAT_0010cdf8 = CONCAT44(DAT_0010cdc0,(undefined4)DAT_0010cdf8);
            DAT_0010ce00 = CONCAT44(DAT_0010ce00._4_4_,1);
            plVar9 = (long *)kmalloc_trace(_strncmp,0xdc0,0x18);
            lVar8 = csmem_info;
            if (plVar9 == (long *)0x0) goto LAB_0010cb74;
            uVar3 = DAT_0010cdf8._4_4_;
            csmem_layout = plVar9;
            *(undefined4 *)(plVar9 + 2) = 1;
            *plVar9 = lVar8;
            *(undefined4 *)(plVar9 + 1) = 0;
            *(undefined4 *)((long)plVar9 + 0xc) = uVar3;
            ccci_dump_write(0,1,"[0/util]ccci_util get csmem: data:%llx data_size:%d\n");
            ccci_dump_write(2,1,"[0/util]ccci_util get csmem: data:%llx data_size:%d\n",csmem_info,
                            DAT_0010cdf8._4_4_);
          }
        }
LAB_0010c95c:
        if ((uint)DAT_0010ce00 != 0) {
          uVar7 = 0;
          do {
            ccci_dump_write(0,1,"[0/util]csmem_region[%d][%d]: data_offset:%x data_size:%d\n",uVar7,
                            (int)csmem_layout[(long)(int)uVar7 * 3 + 2],
                            (int)csmem_layout[(long)(int)uVar7 * 3 + 1],
                            *(undefined4 *)((long)csmem_layout + (long)(int)uVar7 * 0x18 + 0xc));
            ccci_dump_write(2,1,"[0/util]csmem_region[%d][%d]: data_offset:%x data_size:%d\n",uVar7,
                            (int)csmem_layout[(long)(int)uVar7 * 3 + 2],
                            (int)csmem_layout[(long)(int)uVar7 * 3 + 1],
                            *(undefined4 *)((long)csmem_layout + (long)(int)uVar7 * 0x18 + 0xc));
            uVar7 = uVar7 + 1;
          } while (uVar7 < (uint)DAT_0010ce00);
        }
        if ((s_g_md_usage_case & 1) == 0) {
          if (s_g_md_env_rdy_flag == 0) goto LAB_0010ca10;
          ccci_dump_write(0,1,"[0/util]md1: kernel dis, but lk en\n");
          _printk(&DAT_001118bd);
          s_g_md_usage_case = 0;
          s_g_lk_load_img_status = s_g_lk_load_img_status | 0x10;
        }
        else if ((s_g_md_env_rdy_flag & 1) == 0) goto LAB_0010c9e4;
      }
      else {
        ccci_dump_write(0,1,"[0/util]load smem layout fail\n");
        _printk(&DAT_00110430);
        s_g_md_env_rdy_flag = 0;
        s_g_lk_load_img_status = s_g_lk_load_img_status | 8;
        if ((s_g_md_usage_case & 1) == 0) {
LAB_0010ca10:
          ccci_dump_write(0,1,"[0/util]md1: both lk and kernel dis\n");
          ccci_dump_write(2,1,"[0/util]md1: both lk and kernel dis\n");
          s_g_md_usage_case = 0;
          lk_load_img_err_no = 0;
        }
        else {
LAB_0010c9e4:
          ccci_dump_write(0,1,"[0/util]md1 env prepare abnormal, disable this modem\n");
          _printk(&DAT_0011030c);
          s_g_md_usage_case = 0;
        }
      }
      uVar10 = 0;
      s_g_lk_load_img_status = s_g_lk_load_img_status | 1;
      if ((s_g_lk_inf_base != 0) && (s_g_lk_info_tag_version < 3)) {
        __memset_io(s_g_lk_inf_base,0,s_g_tag_inf_size);
        iounmap(s_g_lk_inf_base);
        uVar10 = 0;
      }
      goto LAB_0010ca90;
    }
    ccci_dump_write(0,1,"[0/util]ccci,modem_info_v2 not found, try v1\n");
    ccci_dump_write(2,1,"[0/util]ccci,modem_info_v2 not found, try v1\n");
    plVar9 = (long *)of_get_property(lVar8,"ccci,modem_info",0);
    if (plVar9 != (long *)0x0) {
      lVar8 = *plVar9;
      uVar7 = *(uint *)(plVar9 + 1);
      uVar3 = *(undefined4 *)((long)plVar9 + 0xc);
      ccci_dump_write(0,1,"[0/util]lk info.lk_info_base_addr: 0x%llX\n",lVar8);
      ccci_dump_write(2,1,"[0/util]lk info.lk_info_base_addr: 0x%llX\n",lVar8);
      ccci_dump_write(0,1,"[0/util]lk info.lk_info_size:      0x%x\n",uVar7);
      ccci_dump_write(2,1,"[0/util]lk info.lk_info_size:      0x%x\n",uVar7);
      ccci_dump_write(0,1,"[0/util]lk info.lk_info_tag_num:   0x%x\n",uVar3);
      ccci_dump_write(2,1,"[0/util]lk info.lk_info_tag_num:   0x%x\n",uVar3);
      s_g_tag_inf_size = uVar7;
      if (lVar8 == 0) {
        puVar11 = &UNK_0010ee10;
        uVar7 = 4;
        pcVar12 = "[0/util]no image load success\n";
      }
      else {
        if (uVar7 < 0x10001) {
          s_g_lk_info_tag_version = 1;
          s_g_tag_cnt = uVar3;
          s_g_lk_inf_base = ccci_map_phy_addr(lVar8,0x10000);
          goto LAB_0010bdc0;
        }
        puVar11 = &UNK_0011080b;
        uVar7 = 0x20;
        pcVar12 = "[0/util]tag info mem size too large\n";
      }
      goto LAB_0010cc1c;
    }
    pcVar12 = "[0/util]ccci,modem_info_v1 still not found, using v0!!!\n";
  }
  ccci_dump_write(0,1,pcVar12);
  ccci_dump_write(2,1,pcVar12);
  uVar10 = 0xffffffff;
LAB_0010ca90:
  lVar8 = sp_el0;
  if (*(long *)(lVar8 + 0x620) != local_68) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail(uVar10);
  }
  return;
}


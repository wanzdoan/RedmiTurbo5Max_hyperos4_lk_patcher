
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 mtk_ccci_md_smem_layout_init(void)

{
  uint uVar1;
  undefined8 uVar2;
  code *pcVar3;
  int iVar4;
  int iVar5;
  undefined *puVar6;
  char *pcVar7;
  long lVar8;
  long *plVar9;
  ulong uVar10;
  uint *puVar11;
  long *plVar12;
  int *piVar13;
  undefined4 uVar14;
  ulong uVar15;
  ulong uVar16;
  undefined1 *puVar17;
  uint uVar18;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  long local_68;
  
  lVar8 = sp_el0;
  local_68 = *(long *)(lVar8 + 0x620);
  s_md_gen = 0x1899;
  iVar4 = mtk_ccci_find_args_val("md_generation",&s_md_gen,4);
  if (s_md_gen < 0x1897) {
    _printk(&DAT_0010fbec,"mtk_ccci_md_smem_layout_init");
LAB_0010a070:
    uVar14 = 0;
  }
  else {
    if (iVar4 < 1) {
      _printk(&DAT_0010eb9b,"mtk_ccci_md_smem_layout_init",iVar4);
    }
    iVar4 = mtk_ccci_find_args_val("nc_smem_layout_num",&s_md_nc_smem_item_num,4);
    if (iVar4 == 4) {
      iVar4 = mtk_ccci_find_args_val("c_smem_layout_num",&s_md_c_smem_item_num,4);
      if (iVar4 != 4) {
        puVar6 = &DAT_001105c9;
        goto LAB_0010a138;
      }
      uVar15 = (ulong)s_md_nc_smem_item_num * 0x28;
      s_md_nc_smem_tbl = (undefined8 *)__kmalloc(uVar15,0xcc0);
      if (s_md_nc_smem_tbl == (undefined8 *)0x0) {
        _printk(&DAT_0011281d,"get_md_smem_layout_tbl_from_lk_tag");
        goto LAB_0010a144;
      }
      iVar4 = mtk_ccci_find_args_val("nc_smem_layout",s_md_nc_smem_tbl,uVar15 & 0xffffffff);
      if (iVar4 != (int)uVar15) {
        _printk(&DAT_00110b7f,"get_md_smem_layout_tbl_from_lk_tag",iVar4);
LAB_0010abb0:
        kfree(s_md_nc_smem_tbl);
        s_md_nc_smem_tbl = (undefined8 *)0x0;
        goto LAB_0010a144;
      }
      uVar15 = (ulong)s_md_c_smem_item_num * 0x28;
      s_md_c_smem_tbl = (undefined1 *)__kmalloc(uVar15,0xcc0);
      if ((long *)s_md_c_smem_tbl == (long *)0x0) {
        _printk(&DAT_0010f0d6,"get_md_smem_layout_tbl_from_lk_tag");
        goto LAB_0010abb0;
      }
      iVar4 = mtk_ccci_find_args_val("c_smem_layout",s_md_c_smem_tbl,uVar15 & 0xffffffff);
      if (iVar4 != (int)uVar15) {
        _printk(&DAT_00110180,"get_md_smem_layout_tbl_from_lk_tag",iVar4);
        kfree(s_md_nc_smem_tbl);
        s_md_nc_smem_tbl = (undefined8 *)0x0;
        kfree(s_md_c_smem_tbl);
        s_md_c_smem_tbl = (undefined1 *)0x0;
        goto LAB_0010a144;
      }
LAB_0010a830:
      uVar15 = (ulong)s_md_nc_smem_item_num;
      s_md_nc_smem_size = 0;
      if (s_md_nc_smem_item_num != 0) {
        if (s_md_nc_smem_item_num == 1) {
          uVar10 = 0;
          s_md_nc_smem_size = 0;
        }
        else {
          uVar10 = uVar15 & 0xfffffffe;
          iVar4 = 0;
          iVar5 = 0;
          piVar13 = (int *)(s_md_nc_smem_tbl + 8);
          uVar16 = uVar10;
          do {
            uVar16 = uVar16 - 2;
            iVar4 = piVar13[-10] + iVar4;
            iVar5 = *piVar13 + iVar5;
            piVar13 = piVar13 + 0x14;
          } while (uVar16 != 0);
          s_md_nc_smem_size = iVar5 + iVar4;
          if (uVar10 == uVar15) goto LAB_0010aa78;
        }
        lVar8 = uVar15 - uVar10;
        piVar13 = (int *)(s_md_nc_smem_tbl + uVar10 * 5 + 3);
        do {
          lVar8 = lVar8 + -1;
          s_md_nc_smem_size = *piVar13 + s_md_nc_smem_size;
          piVar13 = piVar13 + 10;
        } while (lVar8 != 0);
      }
LAB_0010aa78:
      uVar15 = (ulong)s_md_c_smem_item_num;
      s_md_c_smem_size = 0;
      if (s_md_c_smem_item_num != 0) {
        if (s_md_c_smem_item_num == 1) {
          uVar10 = 0;
          s_md_c_smem_size = 0;
        }
        else {
          uVar10 = uVar15 & 0xfffffffe;
          iVar4 = 0;
          iVar5 = 0;
          plVar12 = (long *)((long)s_md_c_smem_tbl + 0x40);
          uVar16 = uVar10;
          do {
            uVar16 = uVar16 - 2;
            iVar4 = (int)plVar12[-5] + iVar4;
            iVar5 = (int)*plVar12 + iVar5;
            plVar12 = plVar12 + 10;
          } while (uVar16 != 0);
          s_md_c_smem_size = iVar5 + iVar4;
          if (uVar10 == uVar15) goto LAB_0010ab08;
        }
        lVar8 = uVar15 - uVar10;
        plVar12 = (long *)((long)s_md_c_smem_tbl + (uVar10 * 5 + 3) * 8);
        do {
          lVar8 = lVar8 + -1;
          s_md_c_smem_size = (int)*plVar12 + s_md_c_smem_size;
          plVar12 = plVar12 + 5;
        } while (lVar8 != 0);
      }
LAB_0010ab08:
      s_md_nc_smem_mpu_size = s_md_nc_smem_size;
      s_md_c_smem_mpu_size = s_md_c_smem_size;
      iVar4 = map_phy_to_kernel();
      if (iVar4 < 0) {
        _printk(&DAT_001107dc,"mtk_ccci_md_smem_layout_init",iVar4);
        uVar14 = 0xffffffff;
        goto LAB_0010a618;
      }
      iVar4 = map_phy_to_kernel(s_md_c_smem_tbl,s_md_c_smem_item_num);
      if (-1 < iVar4) {
        iVar4 = mtk_ccci_find_args_val("md1_sib_info",&s_md_sib_inf,0x10);
        if (iVar4 == 0x10) {
          _printk(&DAT_0010f18a,"get_sib_info_from_tag",s_md_sib_inf,DAT_0010ea28);
        }
        else {
          _printk(&DAT_0010f16b,"get_sib_info_from_tag");
        }
        smem_layout_dump("MD non-cache",s_md_nc_smem_tbl,s_md_nc_smem_item_num);
        smem_layout_dump("MD cache",s_md_c_smem_tbl,s_md_c_smem_item_num);
        goto LAB_0010a070;
      }
      puVar6 = &DAT_0010ed56;
    }
    else {
      puVar6 = &DAT_0010f808;
LAB_0010a138:
      _printk(puVar6,"get_md_smem_layout_tbl_from_lk_tag",iVar4);
LAB_0010a144:
      _printk(&DAT_001102bf,"mtk_ccci_md_smem_layout_init",0xffffffff);
      if (s_md_gen < 0x1899) {
        s_md_c_smem_item_num = 4;
        uVar18 = 0x13;
        s_md_c_smem_tbl = gen6295_cacheable_tbl;
        puVar17 = gen6295_noncacheable_tbl;
        uVar15 = 0x2f8;
      }
      else {
        nc_smem_info_parsing_for_lk();
        s_md_c_smem_item_num = 5;
        uVar18 = 0x16;
        s_md_c_smem_tbl = (undefined1 *)&gen6297_cacheable_tbl;
        puVar17 = (undefined1 *)&gen6297_noncacheable_tbl;
        uVar15 = 0x370;
      }
      local_80 = 0;
      uStack_78 = 0;
      local_90 = 0;
      uStack_88 = 0;
      local_98 = 0;
      s_md_nc_smem_tbl = (undefined8 *)puVar17;
      s_md_nc_smem_item_num = uVar18;
      iVar4 = mtk_ccci_find_args_val("smem_layout",&local_98,0x28);
      uVar2 = local_98;
      if (iVar4 == 0x28) {
        uVar16 = 0;
        lVar8 = ((((ulong)((int)uVar15 - 0x18) & 0xffff) + 7) / 0x28) * 0x28;
        do {
          if (uVar16 == 0) {
            uVar10 = 0;
            *(undefined8 *)puVar17 = uVar2;
            *(undefined4 *)((long)puVar17 + 0x14) = 0;
            *(undefined4 *)((long)puVar17 + 0x24) = 0;
            if (lVar8 == -0x28) goto LAB_0010ac50;
          }
          else {
            if (((((uVar15 <= uVar16 - 0x28) || (uVar15 <= uVar16 - 0x10)) ||
                 (uVar15 <= uVar16 - 0x10)) || ((uVar15 <= uVar16 || (uVar15 <= uVar16 - 0x28)))) ||
               (uVar15 <= uVar16)) goto LAB_0010ac50;
            uVar1 = *(uint *)((long)puVar17 + (uVar16 - 0x10));
            *(long *)((long)puVar17 + uVar16) =
                 ((long *)((long)puVar17 + uVar16))[-5] + (ulong)uVar1;
            if (((uVar15 < uVar16 - 0x14) || (uVar15 < uVar16 + 0x14)) ||
               ((uVar15 < uVar16 - 0x14 || (uVar15 < uVar16 + 0x14)))) goto LAB_0010ac50;
            *(uint *)((long)puVar17 + uVar16 + 0x14) =
                 *(int *)((long)puVar17 + (uVar16 - 0x14)) + uVar1;
            if ((((uVar15 < uVar16 - 4) || (uVar15 < uVar16 + 0x24)) || (uVar15 < uVar16 - 4)) ||
               ((uVar15 < uVar16 + 0x24 ||
                (*(uint *)((long)puVar17 + uVar16 + 0x24) =
                      *(int *)((long)puVar17 + (uVar16 - 4)) + uVar1, uVar10 = uVar16,
                lVar8 + 0x28U == uVar16)))) goto LAB_0010ac50;
          }
          if (uVar15 <= uVar16 + 0x10) {
LAB_0010ac50:
                    /* WARNING: Does not return */
            pcVar3 = (code *)SoftwareBreakpoint(1,0x10ac54);
            (*pcVar3)();
          }
          switch(*(undefined4 *)((long)puVar17 + uVar16 + 0x10)) {
          case 10:
            local_70 = CONCAT44(local_70._4_4_,0xffffffff);
            iVar4 = mtk_ccci_find_args_val("smem_dfd_size",&local_70,4);
            if (iVar4 != 4) {
              _printk(&DAT_0011098b,"get_dfd_size");
            }
            if ((uVar15 <= uVar10 + 0x18) || (uVar15 <= uVar16 + 0x18)) goto LAB_0010ac50;
            iVar5 = *(int *)((long)puVar17 + uVar16 + 0x18);
            iVar4 = iVar5;
            if ((-1 < (int)local_70) && (iVar4 = (int)local_70, (int)local_70 != iVar5)) {
              if (uVar15 <= uVar16 + 0x18) goto LAB_0010ac50;
              *(int *)((long)puVar17 + uVar16 + 0x18) = (int)local_70;
            }
            puVar6 = &DAT_00111601;
            break;
          default:
            goto switchD_0010a374_caseD_b;
          case 0x17:
            local_70 = (ulong)local_70._4_4_ << 0x20;
            iVar4 = mtk_ccci_find_args_val("md1_phy_cap",&local_70,4);
            if (iVar4 != 4) {
              _printk(&DAT_0011149e,"get_phy_cap_size");
            }
            if ((uVar15 <= uVar10 + 0x18) || (uVar15 <= uVar16 + 0x18)) goto LAB_0010ac50;
            puVar6 = &DAT_0010ed84;
            *(int *)((long)puVar17 + uVar16 + 0x18) = (int)local_70;
            iVar4 = (int)local_70;
            break;
          case 0x1b:
            local_70 = 0;
            iVar4 = mtk_ccci_find_args_val("udc_layout",&local_70,8);
            if (iVar4 == 8) {
            }
            else {
              _printk(&DAT_00111220,"get_udc_nc_size");
            }
            if ((int)local_70 != 0) {
              if ((uVar15 <= uVar10 + 0x18) || (uVar15 <= uVar16 + 0x18)) goto LAB_0010ac50;
              if ((int)local_70 != *(int *)((long)puVar17 + uVar16 + 0x18)) {
                if (uVar15 <= uVar16 + 0x18) goto LAB_0010ac50;
                *(int *)((long)puVar17 + uVar16 + 0x18) = (int)local_70;
              }
            }
            goto switchD_0010a374_caseD_b;
          case 0x1d:
            local_70 = CONCAT44(local_70._4_4_,0xffffffff);
            iVar4 = mtk_ccci_find_args_val("smem_amms_pos_size",&local_70,4);
            if (iVar4 != 4) {
              _printk(&DAT_001109ac,"get_amms_pose_size");
            }
            if ((uVar15 <= uVar10 + 0x18) || (uVar15 <= uVar16 + 0x18)) goto LAB_0010ac50;
            iVar5 = *(int *)((long)puVar17 + uVar16 + 0x18);
            iVar4 = iVar5;
            if ((-1 < (int)local_70) && (iVar4 = (int)local_70, (int)local_70 != iVar5)) {
              if (uVar15 <= uVar16 + 0x18) goto LAB_0010ac50;
              *(int *)((long)puVar17 + uVar16 + 0x18) = (int)local_70;
            }
            puVar6 = &DAT_0010f36e;
            break;
          case 0x1e:
            local_70 = CONCAT44(local_70._4_4_,0xffffffff);
            iVar4 = mtk_ccci_find_args_val("smem_align_padding_size",&local_70,4);
            if (iVar4 != 4) {
              _printk(&DAT_00112848,"get_raw_align_padding_size");
            }
            if (-1 < (int)local_70) {
              if ((uVar15 <= uVar10 + 0x18) || (uVar15 <= uVar16 + 0x18)) goto LAB_0010ac50;
              if ((int)local_70 != *(int *)((long)puVar17 + uVar16 + 0x18)) {
                if (uVar15 <= uVar16 + 0x18) goto LAB_0010ac50;
                *(int *)((long)puVar17 + uVar16 + 0x18) = (int)local_70;
              }
            }
            puVar6 = &DAT_0010f82d;
            iVar4 = (int)local_70;
          }
          _printk(puVar6,iVar4);
switchD_0010a374_caseD_b:
          uVar1 = s_md_c_smem_item_num;
          plVar12 = (long *)s_md_c_smem_tbl;
          uVar16 = uVar16 + 0x28;
        } while ((ulong)uVar18 * 0x28 - uVar16 != 0);
        if ((long *)s_md_c_smem_tbl != (long *)0x0) {
          uVar15 = (ulong)s_md_c_smem_item_num;
          ccb_info = 0;
          _DAT_0010e920 = 0;
          iVar4 = mtk_ccci_find_args_val("ccb_info",&ccb_info,0x10);
          if (iVar4 == 0x10) {
            _printk(&DAT_001117ba,"c_smem_info_parsing",ccb_info,_DAT_0010e920 & 0xffffffff);
          }
          else {
            _printk(&DAT_0010f851,"c_smem_info_parsing");
          }
          DAT_0010e908 = 0;
          DAT_0010e910 = 0;
          csmem_info = 0;
          iVar4 = mtk_ccci_find_args_val("md1_bank4_cache_info",&csmem_info,0x18);
          if (iVar4 != 0x18) {
            _printk(&DAT_0010f874,"c_smem_info_parsing");
          }
          csmem_layout = 0;
          DAT_0010e938 = 0;
          DAT_0010e940 = 0;
          DAT_0010e948 = 0;
          DAT_0010e950 = 0;
          DAT_0010e958 = 0;
          iVar4 = (uint)DAT_0010e910 * 0x18;
          DAT_0010e960 = 0;
          DAT_0010e968 = 0;
          DAT_0010e970 = 0;
          DAT_0010e978 = 0;
          DAT_0010e980 = 0;
          DAT_0010e988 = 0;
          DAT_0010e990 = 0;
          DAT_0010e998 = 0;
          DAT_0010e9a0 = 0;
          DAT_0010e9a8 = 0;
          DAT_0010e9b0 = 0;
          DAT_0010e9b8 = 0;
          DAT_0010e9c0 = 0;
          DAT_0010e9c8 = 0;
          DAT_0010e9d0 = 0;
          DAT_0010e9d8 = 0;
          DAT_0010e9e0 = 0;
          DAT_0010e9e8 = 0;
          DAT_0010e9f0 = 0;
          DAT_0010e9f8 = 0;
          DAT_0010ea00 = 0;
          DAT_0010ea08 = 0;
          DAT_0010ea10 = 0;
          DAT_0010ea18 = 0;
          iVar5 = mtk_ccci_find_args_val("md1_bank4_cache_layout",&csmem_layout,iVar4);
          if (iVar5 != iVar4) {
            _printk(&DAT_0010f3a1,"c_smem_info_parsing");
          }
          s_md_cache_offset = 0;
          iVar4 = mtk_ccci_find_args_val("md1_smem_cahce_offset",&s_md_cache_offset,4);
          if (iVar4 != 4) {
            s_md_cache_offset = 0x8000000;
          }
          _printk(&DAT_00111616,"c_smem_info_parsing",s_md_cache_offset);
          lVar8 = ccb_info;
          if (csmem_info != 0) {
            lVar8 = csmem_info;
          }
          if (uVar1 != 0) {
            uVar16 = 0;
            do {
              iVar4 = s_md_cache_offset;
              if (uVar16 == 0) {
                *plVar12 = lVar8;
                *(undefined4 *)((long)plVar12 + 0x14) = 0;
                *(int *)((long)plVar12 + 0x24) = iVar4;
              }
              else {
                plVar9 = plVar12 + uVar16 * 5;
                uVar18 = *(uint *)(plVar9 + -2);
                *plVar9 = plVar9[-5] + (ulong)uVar18;
                *(uint *)((long)plVar9 + 0x14) = *(int *)((long)plVar9 + -0x14) + uVar18;
                *(uint *)((long)plVar9 + 0x24) = *(int *)((long)plVar9 + -4) + uVar18;
              }
              uVar18 = (uint)(plVar12 + uVar16 * 5)[2];
              if (uVar18 < 0x21) {
                if ((1L << ((ulong)uVar18 & 0x3f) & 0x111400000U) == 0) {
                  if ((ulong)uVar18 != 1) goto LAB_0010a898;
                  uVar18 = (uint)DAT_0010e910;
                  if (9 < (uint)DAT_0010e910) {
                    uVar18 = 10;
                  }
                  uVar10 = (ulong)uVar18;
                  puVar11 = (uint *)&DAT_0010e940;
                  if (uVar18 != 0) {
                    do {
                      if (*puVar11 == 1) {
                        uVar10 = *(ulong *)(puVar11 + -4);
                        uVar18 = puVar11[-2];
                        uVar1 = puVar11[-1];
                        *(uint *)((long)plVar12 + uVar16 * 0x28 + 0x14) = uVar18;
                        *(uint *)(plVar12 + uVar16 * 5 + 3) = uVar1;
                        iVar4 = s_md_cache_offset;
                        plVar12[uVar16 * 5] = uVar10 & 0xffffffff;
                        *(uint *)((long)plVar12 + uVar16 * 0x28 + 0x24) = iVar4 + uVar18;
                        ccci_dump_write(0,1,"[0/util]ccci:ap_phy_addr 0x%x offset 0x%x\n",
                                        uVar10 & 0xffffffff);
                        ccci_dump_write(2,1,"[0/util]ccci:ap_phy_addr 0x%x offset 0x%x\n",
                                        uVar10 & 0xffffffff,
                                        *(undefined4 *)((long)plVar12 + uVar16 * 0x28 + 0x14));
                        goto LAB_0010a8a4;
                      }
                      uVar10 = uVar10 - 1;
                      puVar11 = puVar11 + 6;
                    } while (uVar10 != 0);
                  }
                  *(undefined4 *)(plVar12 + uVar16 * 5 + 3) = DAT_0010e920;
                }
                else {
                  uVar1 = (uint)DAT_0010e910;
                  if (9 < (uint)DAT_0010e910) {
                    uVar1 = 10;
                  }
                  uVar10 = (ulong)uVar1;
                  puVar11 = (uint *)&DAT_0010e940;
                  if (uVar1 != 0) {
                    do {
                      if (*puVar11 == uVar18) {
                        uVar18 = puVar11[-2];
                        uVar1 = puVar11[-1];
                        *(uint *)(plVar12 + uVar16 * 5 + 3) = uVar1;
                        if (uVar18 != 0 || uVar1 != 0) {
                          *(uint *)((long)plVar12 + uVar16 * 0x28 + 0x14) = uVar18;
                          *(uint *)((long)plVar12 + uVar16 * 0x28 + 0x24) =
                               s_md_cache_offset + uVar18;
                        }
                        break;
                      }
                      uVar10 = uVar10 - 1;
                      puVar11 = puVar11 + 6;
                    } while (uVar10 != 0);
                  }
                }
              }
              else {
LAB_0010a898:
                *(undefined8 *)((long)plVar12 + uVar16 * 0x28 + 0x14) = 0;
                *(undefined4 *)((long)plVar12 + uVar16 * 0x28 + 0x24) = 0;
              }
LAB_0010a8a4:
              uVar16 = uVar16 + 1;
            } while (uVar16 != uVar15);
          }
          goto LAB_0010a830;
        }
        puVar6 = &DAT_00111e61;
        pcVar7 = "cmpt_c_smem_layout_init";
      }
      else {
        puVar6 = &DAT_0010ebc3;
        pcVar7 = "cmpt_nc_smem_layout_init";
      }
      _printk(puVar6,pcVar7);
      puVar6 = &DAT_001111e2;
    }
    uVar14 = 0xffffffff;
    _printk(puVar6,"mtk_ccci_md_smem_layout_init",0xffffffff);
  }
LAB_0010a618:
  lVar8 = sp_el0;
  if (*(long *)(lVar8 + 0x620) == local_68) {
    return uVar14;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


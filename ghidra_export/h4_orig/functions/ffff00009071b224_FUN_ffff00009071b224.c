// ffff00009071b224  size=4032  FUN_ffff00009071b224

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009071b224(long param_1)

{
  uint *puVar1;
  ulong uVar2;
  uint uVar3;
  uint uVar4;
  int iVar5;
  int iVar6;
  int iVar7;
  long lVar8;
  undefined8 uVar9;
  bool bVar10;
  long *plVar11;
  uint uVar12;
  uint uVar13;
  ulong uVar14;
  ulong *puVar15;
  long *plVar16;
  ulong uVar17;
  ulong uVar18;
  int iVar19;
  ulong uVar20;
  long lVar21;
  int iVar22;
  long lVar23;
  ulong uVar24;
  ulong uVar25;
  undefined8 *puVar26;
  ulong *puVar27;
  uint local_1c0;
  uint uStack_1bc;
  undefined8 local_1b8;
  undefined8 local_1b0;
  undefined8 local_1a8;
  undefined8 local_1a0;
  undefined8 local_198;
  undefined8 uStack_190;
  undefined8 local_188;
  undefined8 uStack_180;
  undefined8 local_178;
  undefined8 uStack_170;
  undefined8 local_168;
  undefined8 uStack_160;
  undefined8 local_158;
  undefined8 uStack_150;
  undefined8 local_148;
  undefined8 uStack_140;
  undefined8 local_138;
  undefined8 uStack_130;
  undefined8 local_128;
  undefined8 uStack_120;
  undefined8 local_118;
  undefined8 uStack_110;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  
  DAT_ffff000090848c84 = 0;
  if (param_1 == 0) {
    FUN_ffff00009077e11c
              (s__s__d__fdt_is_NULL_ffff0000907f7d9d,s_mblock_fdt_append_ffff0000907dc33a,0x737);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc(s_lib_mblock_mblock_c_ffff0000907f7d89,0x738,&DAT_ffff0000907d2506);
  }
  FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
  puVar15 = (ulong *)(_DAT_ffff000090850c90 + 8);
  if (puVar15 <= puVar15 + (ulong)(_DAT_ffff00009085acd0 - 1) * 3) {
    bVar10 = false;
    iVar5 = 0;
    lVar8 = _DAT_ffff000090850c90;
    iVar6 = _DAT_ffff00009085acd0;
    iVar7 = _DAT_ffff00009085b8e0;
    iVar19 = _DAT_ffff00009085b8e0;
    do {
      if ((iVar19 != 0) &&
         (puVar27 = (ulong *)(lVar8 + 0xc18), puVar27 <= (ulong *)(lVar8 + 0x5780U))) {
        uVar24 = *puVar15;
        uVar18 = puVar15[1];
        iVar22 = 0;
        uVar25 = uVar18 + uVar24;
        do {
          uVar17 = *puVar27;
          if (((uVar17 != 0) && (uVar18 != 0)) &&
             (((uVar24 <= uVar17 && (uVar17 < uVar25)) ||
              ((uVar17 < uVar24 && (uVar24 < puVar27[1] + uVar17)))))) {
            FUN_ffff00009077e11c
                      (s__s__d__failed_i__d_j__d__d__d__d_ffff0000907dc398,
                       s_mblock_sanity_check_ffff0000907e4476,0x5fc,iVar5,iVar22,uVar24 <= uVar17,
                       uVar17 < uVar25,uVar24 > uVar17,uVar24 < puVar27[1] + uVar17);
            bVar10 = true;
            lVar8 = _DAT_ffff000090850c90;
            iVar7 = _DAT_ffff00009085b8e0;
          }
          iVar6 = _DAT_ffff00009085acd0;
          iVar19 = iVar7;
          if (iVar7 == 0) break;
          puVar27 = puVar27 + 0x13;
          iVar22 = iVar22 + 1;
        } while (puVar27 <= (ulong *)(lVar8 + 0x5780U));
      }
      iVar5 = iVar5 + 1;
      puVar15 = puVar15 + 3;
    } while (puVar15 <= (ulong *)(lVar8 + (ulong)(iVar6 - 1) * 0x18 + 8));
    if (bVar10) {
      FUN_ffff00009077e11c
                (s__s__d__error_ffff0000907da730,s_mblock_sanity_check_ffff0000907e4476,0x605);
      FUN_ffff000090719710(0);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc(s_lib_mblock_mblock_c_ffff0000907f7d89,0x607,&DAT_ffff0000907d2506);
    }
  }
  iVar5 = FUN_ffff00009076da24(param_1,s__reserved_memory_ffff0000907fce9a);
  if (-1 < iVar5) {
    iVar5 = FUN_ffff00009076d02c(param_1,iVar5);
    if (-1 < iVar5) {
      local_1a8 = local_1a8 & 0xffffffff00000000;
      do {
        lVar8 = FUN_ffff00009076ddb4(param_1,iVar5,&DAT_ffff0000907df645,&local_1a8);
        if ((lVar8 != 0) && (_DAT_ffff00009085b8e0 != 0)) {
          iVar6 = 0;
          bVar10 = false;
          uVar25 = (*(ulong *)(lVar8 + 0xc) & 0xff00ff00ff00ff00) >> 8 |
                   (*(ulong *)(lVar8 + 0xc) & 0xff00ff00ff00ff) << 8;
          uVar25 = (uVar25 & 0xffff0000ffff0000) >> 0x10 | (uVar25 & 0xffff0000ffff) << 0x10;
          uVar24 = uVar25 >> 0x20 | uVar25 << 0x20;
          uVar25 = (*(ulong *)(lVar8 + 0x14) & 0xff00ff00ff00ff00) >> 8 |
                   (*(ulong *)(lVar8 + 0x14) & 0xff00ff00ff00ff) << 8;
          uVar25 = (uVar25 & 0xffff0000ffff0000) >> 0x10 | (uVar25 & 0xffff0000ffff) << 0x10;
          uVar25 = (uVar25 >> 0x20 | uVar25 << 0x20) + uVar24;
          puVar15 = (ulong *)(_DAT_ffff000090850c90 + 0xc18);
          lVar8 = _DAT_ffff000090850c90;
          iVar7 = _DAT_ffff00009085b8e0;
          do {
            if ((ulong *)(lVar8 + 0x5780U) < puVar15) break;
            uVar18 = *puVar15;
            if ((uVar18 != 0) &&
               (((uVar24 < uVar18 && (uVar18 < uVar25)) ||
                ((uVar18 <= uVar24 && (uVar24 < puVar15[1] + uVar18)))))) {
              FUN_ffff00009077e11c
                        (s__s__d__failed_i__d__d__d__d__d_ffff0000907d5b98,
                         s_kernel_dts_mblock_conflict_check_ffff0000907e8fc4,0x5c2,iVar6,
                         uVar18 > uVar24,uVar18 < uVar25,uVar18 <= uVar24,
                         uVar24 < puVar15[1] + uVar18);
              bVar10 = true;
              lVar8 = _DAT_ffff000090850c90;
              iVar7 = _DAT_ffff00009085b8e0;
            }
            iVar6 = iVar6 + 1;
            puVar15 = puVar15 + 0x13;
          } while (iVar7 != 0);
          if (bVar10) {
            FUN_ffff00009077e11c
                      (s__s__d__error_ffff0000907da730,
                       s_kernel_dts_mblock_conflict_check_ffff0000907e8fc4,0x5c9);
            FUN_ffff000090719710(0);
                    /* WARNING: Subroutine does not return */
            FUN_ffff00009077cfcc(s_lib_mblock_mblock_c_ffff0000907f7d89,0x5cb,&DAT_ffff0000907d2506)
            ;
          }
        }
        iVar5 = FUN_ffff00009076d0fc(param_1,iVar5);
      } while (-1 < iVar5);
    }
    FUN_ffff00009071c2bc();
    iVar5 = FUN_ffff00009076da24(param_1,s__reserved_memory_ffff0000907fce9a);
    if (-1 < iVar5) {
      iVar5 = FUN_ffff00009076d02c(param_1,iVar5);
      if (-1 < iVar5) {
        local_1a8 = local_1a8 & 0xffffffff00000000;
        do {
          lVar8 = FUN_ffff00009076ddb4(param_1,iVar5,s_alloc_ranges_ffff0000907e448a,&local_1a8);
          if (lVar8 != 0) {
            uVar24 = *(ulong *)(lVar8 + 0xc);
            uVar25 = *(ulong *)(lVar8 + 0x14);
            lVar8 = FUN_ffff00009076ddb4(param_1,iVar5,s_status_ffff0000907edcdb,&local_1a8);
            if (lVar8 != 0) {
              iVar6 = FUN_ffff00009077f348(lVar8 + 0xc,&DAT_ffff0000907d282d,2);
              if ((iVar6 != 0) &&
                 (iVar6 = FUN_ffff00009077f348(lVar8 + 0xc,&DAT_ffff0000907df649,4), iVar6 != 0))
              goto LAB_ffff00009071b548;
            }
            lVar8 = FUN_ffff00009076ddb4(param_1,iVar5,&DAT_ffff0000907d587a,&local_1a8);
            if (lVar8 != 0) {
              uVar24 = (uVar24 & 0xff00ff00ff00ff00) >> 8 | (uVar24 & 0xff00ff00ff00ff) << 8;
              uVar24 = (uVar24 & 0xffff0000ffff0000) >> 0x10 | (uVar24 & 0xffff0000ffff) << 0x10;
              uVar18 = uVar24 >> 0x20 | uVar24 << 0x20;
              uVar24 = (uVar25 & 0xff00ff00ff00ff00) >> 8 | (uVar25 & 0xff00ff00ff00ff) << 8;
              uVar24 = (uVar24 & 0xffff0000ffff0000) >> 0x10 | (uVar24 & 0xffff0000ffff) << 0x10;
              uVar24 = uVar24 >> 0x20 | uVar24 << 0x20;
              if ((uVar25 == 0) || (uVar25 = uVar24 + uVar18, uVar25 < 0x100000001)) {
LAB_ffff00009071b640:
                uVar25 = (*(ulong *)(lVar8 + 0xc) & 0xff00ff00ff00ff00) >> 8 |
                         (*(ulong *)(lVar8 + 0xc) & 0xff00ff00ff00ff) << 8;
                uVar25 = (uVar25 & 0xffff0000ffff0000) >> 0x10 | (uVar25 & 0xffff0000ffff) << 0x10;
                uVar25 = uVar25 >> 0x20 | uVar25 << 0x20;
                uVar24 = uVar24 + uVar18;
                puVar15 = (ulong *)(_DAT_ffff000090850c90 + 8);
                puVar27 = puVar15 + (ulong)(_DAT_ffff00009085acd0 - 1) * 3;
                do {
                  if (puVar27 < puVar15) {
                    FUN_ffff000090719710(0);
                    FUN_ffff00009077e11c
                              (s__s__d__fatal_error_dts_reserved__ffff0000907cf2d6,
                               s_reserved_memory_alloc_ranges_bas_ffff0000907f5f63,0x580);
                    uVar9 = FUN_ffff00009076da60(param_1,iVar5,0);
                    FUN_ffff00009077e11c
                              (s__s__d__DTS_node__s_alloc_ranges__ffff0000907e8fe5,
                               s_reserved_memory_alloc_ranges_bas_ffff0000907f5f63,0x583,uVar9,
                               uVar18,uVar24);
                    uVar9 = FUN_ffff00009076da60(param_1,iVar5,0);
                    FUN_ffff00009077e11c
                              (s__s__d__DTS_node__s_alloc_ranges_s_ffff0000907fec5b,
                               s_reserved_memory_alloc_ranges_bas_ffff0000907f5f63,0x585,uVar9,
                               uVar25);
                    /* WARNING: Subroutine does not return */
                    FUN_ffff00009077cfcc
                              (s_lib_mblock_mblock_c_ffff0000907f7d89,0x587,&DAT_ffff0000907d2506);
                  }
                  uVar14 = *puVar15;
                  uVar17 = puVar15[1] + uVar14;
                  if ((uVar14 <= uVar18 && uVar18 < uVar17) ||
                     ((uVar18 <= uVar14 && (uVar14 < uVar24)))) {
                    uVar20 = uVar24;
                    if (uVar17 <= uVar24) {
                      uVar20 = uVar17;
                    }
                    uVar17 = uVar18;
                    if (uVar18 <= uVar14) {
                      uVar17 = uVar14;
                    }
                    if (uVar25 <= uVar20 - uVar17) goto LAB_ffff00009071b548;
                  }
                  puVar15 = puVar15 + 3;
                } while( true );
              }
              uVar17 = 0x3f;
              do {
                uVar14 = uVar17 - 1;
                if (uVar17 == 0) break;
                uVar20 = uVar17 & 0x3f;
                uVar17 = uVar14;
              } while (uVar18 >> uVar20 == 0);
              uVar17 = 0x3f;
              do {
                uVar20 = uVar17 - 1;
                if (uVar17 == 0) break;
                uVar2 = uVar17 & 0x3f;
                uVar17 = uVar20;
              } while (uVar25 - 1 >> uVar2 == 0);
              if ((int)uVar20 + 1 <= (int)uVar14 + 1) goto LAB_ffff00009071b640;
            }
          }
LAB_ffff00009071b548:
          iVar5 = FUN_ffff00009076d0fc(param_1,iVar5);
        } while (-1 < iVar5);
      }
      FUN_ffff0000907177d0(&LAB_ffff000090818208);
      iVar5 = FUN_ffff00009076da24(param_1,s__memory_ffff0000907d8ec2);
      if (iVar5 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d___s___d__get_path__memory_f_ffff0000907e7682,
                   s_mblock_fdt_append_ffff0000907dc33a,0x73e,s_mblock_fdt_append_ffff0000907dc33a,
                   0x73e);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc(s_lib_mblock_mblock_c_ffff0000907f7d89,0x740,&DAT_ffff0000907d2506);
      }
      iVar5 = FUN_ffff00009076e6d4
                        (param_1,iVar5,s_mblock_info_ffff0000907dc34c,_DAT_ffff000090850c90,0x5818);
      if (iVar5 != 0) {
        FUN_ffff00009077e11c
                  (s__s__d___s___d__fdt_setprop_faile_ffff0000907ec0fa,
                   s_mblock_fdt_append_ffff0000907dc33a,0x746,s_mblock_fdt_append_ffff0000907dc33a,
                   0x746,iVar5);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc(s_lib_mblock_mblock_c_ffff0000907f7d89,0x747,&DAT_ffff0000907d2506);
      }
      FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
      lVar8 = _DAT_ffff000090850c90 + (ulong)(_DAT_ffff00009085acd0 - 1) * 0x18;
      uVar25 = *(long *)(lVar8 + 0x10) + *(long *)(lVar8 + 8);
      if (_DAT_ffff00009085b8e0 == 0) {
        uVar24 = 0;
        uVar18 = 0;
      }
      else {
        puVar15 = (ulong *)(_DAT_ffff000090850c90 + 0xc18);
        uVar17 = 0;
        uVar14 = 0xffffffffffffffff;
        do {
          uVar20 = *puVar15;
          uVar18 = uVar17;
          uVar24 = uVar14;
          if (uVar20 != 0) {
            uVar24 = uVar20;
            if (uVar14 <= uVar20) {
              uVar24 = uVar14;
            }
            uVar18 = puVar15[1] + uVar20;
            if (puVar15[1] + uVar20 <= uVar17) {
              uVar18 = uVar17;
            }
          }
          puVar15 = puVar15 + 0x13;
          uVar17 = uVar18;
          uVar14 = uVar24;
        } while (puVar15 <= (ulong *)(_DAT_ffff000090850c90 + 0x5780U));
      }
      if (_DAT_ffff00009085acd8 <= uVar24) {
        uVar24 = _DAT_ffff00009085acd8;
      }
      uVar17 = _DAT_ffff00009085acd8;
      if (_DAT_ffff00009085b8e0 != 0) {
        uVar17 = uVar24;
      }
      if (uVar18 <= uVar25) {
        uVar18 = uVar25;
      }
      lVar8 = uVar18 - uVar17;
      uVar12 = (uint)(uVar17 >> 0x20);
      uVar12 = (uVar12 & 0xff00ff00) >> 8 | (uVar12 & 0xff00ff) << 8;
      uVar3 = ((uint)uVar17 & 0xff00ff00) >> 8 | ((uint)uVar17 & 0xff00ff) << 8;
      uVar13 = (uint)((ulong)lVar8 >> 0x20);
      uVar13 = (uVar13 & 0xff00ff00) >> 8 | (uVar13 & 0xff00ff) << 8;
      uVar4 = ((uint)lVar8 & 0xff00ff00) >> 8 | ((uint)lVar8 & 0xff00ff) << 8;
      local_1a8 = CONCAT44(uVar3 >> 0x10 | uVar3 << 0x10,uVar12 >> 0x10 | uVar12 << 0x10);
      local_1a0 = CONCAT44(uVar4 >> 0x10 | uVar4 << 0x10,uVar13 >> 0x10 | uVar13 << 0x10);
      iVar5 = FUN_ffff00009076da24(param_1,s__memory_ffff0000907d8ec2);
      if (iVar5 < 0) {
        FUN_ffff00009077e11c
                  (s__s__d__path__memory_failed_ffff0000907fec89,
                   s_setup_mem_property_use_mblock_in_ffff000090800670,0x719);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc(s_lib_mblock_mblock_c_ffff0000907f7d89,0x71a,&DAT_ffff0000907d2506);
      }
      iVar5 = FUN_ffff00009076e6d4(param_1,iVar5,&DAT_ffff0000907df645,&local_1a8,0x10);
      if (iVar5 != 0) {
        FUN_ffff00009077e11c
                  (s__s__d__setprop_for_reg_failed_re_ffff0000907e0d2c,
                   s_setup_mem_property_use_mblock_in_ffff000090800670,0x71f,iVar5);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc(s_lib_mblock_mblock_c_ffff0000907f7d89,0x720,&DAT_ffff0000907d2506);
      }
      FUN_ffff0000907177d0(&LAB_ffff000090818208);
      FUN_ffff00009077e11c
                (s__s__d__mem_start_0x_llx_mem_sz_0_ffff0000907d8eca,
                 s_setup_mem_property_use_mblock_in_ffff000090800670,0x724,uVar17,lVar8);
      FUN_ffff00009077e11c
                (s__s__d__mem_reg_property_start_hi_ffff0000907f43fa,
                 s_setup_mem_property_use_mblock_in_ffff000090800670,0x725,local_1a8 & 0xffffffff);
      FUN_ffff00009077e11c
                (s__s__d__mem_reg_property_start_lo_ffff0000907da73e,
                 s_setup_mem_property_use_mblock_in_ffff000090800670,0x726,local_1a8._4_4_);
      FUN_ffff00009077e11c
                (s__s__d__mem_reg_property_size_hi___ffff0000907feca6,
                 s_setup_mem_property_use_mblock_in_ffff000090800670,0x727,local_1a0 & 0xffffffff);
      FUN_ffff00009077e11c
                (s__s__d__mem_reg_property_size_lo___ffff000090800693,
                 s_setup_mem_property_use_mblock_in_ffff000090800670,0x728,local_1a0._4_4_);
      local_78 = 0;
      uStack_70 = 0;
      local_88 = 0;
      uStack_80 = 0;
      local_98 = 0;
      uStack_90 = 0;
      local_a8 = 0;
      uStack_a0 = 0;
      local_b8 = 0;
      uStack_b0 = 0;
      local_c8 = 0;
      uStack_c0 = 0;
      local_d8 = 0;
      uStack_d0 = 0;
      local_e8 = 0;
      uStack_e0 = 0;
      local_f8 = 0;
      uStack_f0 = 0;
      local_108 = 0;
      uStack_100 = 0;
      local_118 = 0;
      uStack_110 = 0;
      local_128 = 0;
      uStack_120 = 0;
      local_138 = 0;
      uStack_130 = 0;
      local_148 = 0;
      uStack_140 = 0;
      local_158 = 0;
      uStack_150 = 0;
      local_168 = 0;
      uStack_160 = 0;
      local_178 = 0;
      uStack_170 = 0;
      local_188 = 0;
      uStack_180 = 0;
      local_198 = 0;
      uStack_190 = 0;
      local_1a8 = 0;
      local_1a0 = 0;
      local_1b8 = 0;
      local_1b0 = 0;
      local_1c0 = 0;
      uStack_1bc = 0;
      FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
      if (_DAT_ffff00009085b8e0 != 0) {
        iVar5 = 1;
        puVar15 = (ulong *)(_DAT_ffff000090850c90 + 0xc18);
        iVar6 = _DAT_ffff00009085b8e0;
        do {
          if ((ulong *)(_DAT_ffff000090850c90 + 0x5780U) < puVar15) break;
          if (*puVar15 != 0) {
            if (((*puVar15 & 0xffff) != 0) || ((short)puVar15[1] != 0)) {
              FUN_ffff00009077e11c
                        (s__s__d__Error__name___s__start_or_ffff0000907df64e,
                         s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x667,
                         (uint *)((long)puVar15 + 0x14),0x10000);
                    /* WARNING: Subroutine does not return */
              FUN_ffff00009077cfcc
                        (s_lib_mblock_mblock_c_ffff0000907f7d89,0x668,&DAT_ffff0000907d2506);
            }
            iVar6 = FUN_ffff00009076da24(param_1,s__reserved_memory_ffff0000907fce9a);
            if (iVar6 < 0) {
              FUN_ffff00009077e11c
                        (s__s__d__Error__fdt_path_offset_fa_ffff0000907fceab,
                         s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x66d,iVar6);
                    /* WARNING: Subroutine does not return */
              FUN_ffff00009077cfcc
                        (s_lib_mblock_mblock_c_ffff0000907f7d89,0x66e,&DAT_ffff0000907d2506);
            }
            puVar1 = (uint *)((long)puVar15 + 0x14);
            FUN_ffff00009077f6fc(&local_e8,0x80,s_mblock__d__s_ffff0000907ea909,iVar5,puVar1);
            iVar6 = FUN_ffff00009076eb40(param_1,iVar6,&local_e8);
            if (iVar6 < 0) {
              FUN_ffff00009077e11c
                        (s__s__d__Error__fdt_add_subnode_fa_ffff0000907cf32d,
                         s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x673,iVar6);
                    /* WARNING: Subroutine does not return */
              FUN_ffff00009077cfcc
                        (s_lib_mblock_mblock_c_ffff0000907f7d89,0x674,&DAT_ffff0000907d2506);
            }
            uVar25 = (ulong)_DAT_ffff000090850ca4;
            if (_DAT_ffff000090850ca4 != 0) {
              lVar23 = 0;
              lVar8 = -0xffff6f7af298;
              lVar21 = 0xffffffff;
              do {
                iVar7 = FUN_ffff00009077f0ac(puVar1,lVar8);
                if (iVar7 == 0) {
                  if (lVar21 != 0) {
                    puVar26 = (undefined8 *)
                              ((long)(int)((ulong)lVar23 >> 0x20) * 0x140 + -0xffff6f7af358);
                    goto LAB_ffff00009071ba6c;
                  }
                  break;
                }
                lVar23 = lVar23 + 0x100000000;
                lVar21 = lVar21 + -1;
                uVar25 = uVar25 - 1;
                lVar8 = lVar8 + 0x140;
              } while (uVar25 != 0);
            }
            lVar8 = FUN_ffff00009077f544(puVar1,s_shared_dma_pool_ffff0000907ec125);
            if (lVar8 == 0) {
              puVar26 = &local_1a8;
              FUN_ffff00009077f6fc(&local_1a8,0xc0,s_mediatek__s_ffff0000907d2830,puVar1);
            }
            else {
              puVar26 = &local_1a8;
              FUN_ffff00009077f6fc(&local_1a8,0xc0,s_shared_dma_pool_ffff0000907ec125);
            }
LAB_ffff00009071ba6c:
            iVar7 = FUN_ffff00009077f250(puVar26);
            iVar7 = FUN_ffff00009076e6d4
                              (param_1,iVar6,s_compatible_ffff0000907d74bf,puVar26,iVar7 + 1);
            if (iVar7 != 0) {
              FUN_ffff00009077e11c
                        (s__s__d__Error__fdt_setprop_string_ffff0000907e5d56,
                         s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x684,iVar7);
                    /* WARNING: Subroutine does not return */
              FUN_ffff00009077cfcc
                        (s_lib_mblock_mblock_c_ffff0000907f7d89,0x685,&DAT_ffff0000907d2506);
            }
            uVar12 = (uint)puVar15[2];
            if (uVar12 == 0) {
              iVar7 = FUN_ffff00009076e6d4(param_1,iVar6,s_no_map_ffff0000907f9684,0,0);
              if (iVar7 != 0) {
                FUN_ffff00009077e11c
                          (s__s__d__Error__fdt_setprop_failed_ffff0000907fcedd,
                           s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x68b,iVar7);
                    /* WARNING: Subroutine does not return */
                FUN_ffff00009077cfcc
                          (s_lib_mblock_mblock_c_ffff0000907f7d89,0x68c,&DAT_ffff0000907d2506);
              }
              uVar12 = (uint)puVar15[2];
            }
            if ((uVar12 == 2) &&
               (iVar7 = FUN_ffff00009076e6d4(param_1,iVar6,s_reusable_ffff0000907f968b,0,0),
               iVar7 != 0)) {
              FUN_ffff00009077e11c
                        (s__s__d__Error__fdt_setprop_failed_ffff0000907fcedd,
                         s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x693,iVar7);
                    /* WARNING: Subroutine does not return */
              FUN_ffff00009077cfcc
                        (s_lib_mblock_mblock_c_ffff0000907f7d89,0x694,&DAT_ffff0000907d2506);
            }
            uVar12 = (*(uint *)((long)puVar15 + 4) & 0xff00ff00) >> 8 |
                     (*(uint *)((long)puVar15 + 4) & 0xff00ff) << 8;
            uVar3 = ((uint)*puVar15 & 0xff00ff00) >> 8 | ((uint)*puVar15 & 0xff00ff) << 8;
            uVar13 = (*(uint *)((long)puVar15 + 0xc) & 0xff00ff00) >> 8 |
                     (*(uint *)((long)puVar15 + 0xc) & 0xff00ff) << 8;
            uVar4 = ((uint)puVar15[1] & 0xff00ff00) >> 8 | ((uint)puVar15[1] & 0xff00ff) << 8;
            local_1b8 = CONCAT44(uVar3 >> 0x10 | uVar3 << 0x10,uVar12 >> 0x10 | uVar12 << 0x10);
            local_1b0 = CONCAT44(uVar4 >> 0x10 | uVar4 << 0x10,uVar13 >> 0x10 | uVar13 << 0x10);
            iVar7 = FUN_ffff00009076e6d4(param_1,iVar6,&DAT_ffff0000907df645,&local_1b8,0x10);
            if (iVar7 != 0) {
              FUN_ffff00009077e11c
                        (s__s__d__set_reg_prop_failed_ret_0_ffff0000907f4426,
                         s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x69f,iVar7);
                    /* WARNING: Subroutine does not return */
              FUN_ffff00009077cfcc
                        (s_lib_mblock_mblock_c_ffff0000907f7d89,0x6a0,&DAT_ffff0000907d2506);
            }
            uVar25 = (ulong)_DAT_ffff000090848c88;
            if (_DAT_ffff000090848c88 != 0) {
              lVar23 = 0;
              lVar8 = -0xffff6f7b72f4;
              lVar21 = 0xffffffff;
              do {
                iVar7 = FUN_ffff00009077f0ac(puVar1,lVar8);
                if (iVar7 == 0) {
                  if (lVar21 != 0) {
                    uStack_1bc = 0;
                    iVar7 = FUN_ffff00009076d528(param_1,&uStack_1bc);
                    if (iVar7 != 0) {
                      FUN_ffff00009077e11c
                                (s__s__d__Error__fdt_generate_phand_ffff0000907d8ef2,
                                 s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x6a9,iVar7);
                    /* WARNING: Subroutine does not return */
                      FUN_ffff00009077cee0(s_fdt_generate_phandle_fail_ffff0000907fb3bb);
                    }
                    uVar12 = (uStack_1bc & 0xff00ff00) >> 8 | (uStack_1bc & 0xff00ff) << 8;
                    local_1c0 = uVar12 >> 0x10 | uVar12 << 0x10;
                    iVar6 = FUN_ffff00009076e6d4
                                      (param_1,iVar6,s_phandle_ffff0000907f27f4,&local_1c0,4);
                    if (iVar6 != 0) {
                      FUN_ffff00009077e11c
                                (s__s__d__Error__set_phandle_for_re_ffff0000908006bf,
                                 s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x6b2,puVar1);
                      FUN_ffff000090719710(0);
                    /* WARNING: Subroutine does not return */
                      FUN_ffff00009077cee0(s_set_reserved_memory_failed_ffff0000907e0d54);
                    }
                    FUN_ffff00009077e11c
                              (s__s__d__Set_phandle_reserved_memo_ffff0000907f27fc,
                               s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x6b7,puVar1,uStack_1bc
                              );
                    lVar8 = (lVar23 >> 0x18) + -0xffff6f7b7374;
                    iVar6 = FUN_ffff00009076da24(param_1,lVar8);
                    if (iVar6 < 0) {
                      FUN_ffff00009077e11c
                                (s__s__d__Error__fdt_do_NOT_has_dev_ffff0000907d8f24,
                                 s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x6bc,lVar8);
                    /* WARNING: Subroutine does not return */
                      FUN_ffff00009077cee0(s_You_don_t_has_device_node_ffff0000907ddb62);
                    }
                    iVar6 = FUN_ffff00009076e6d4
                                      (param_1,iVar6,s_memory_region_ffff0000907f0f70,&local_1c0,4);
                    if (iVar6 != 0) {
                      FUN_ffff00009077e11c
                                (s__s__d__Error__set_device_node_me_ffff0000907d4057,
                                 s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x6c5,lVar8);
                    /* WARNING: Subroutine does not return */
                      FUN_ffff00009077cee0(s_set_device_memory_region_failed_ffff0000907fb3d6);
                    }
                    FUN_ffff00009077e11c
                              (s__s__d__Set_device___s__memory_re_ffff000090802169,
                               s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x6c9,lVar8,uStack_1bc)
                    ;
                  }
                  break;
                }
                lVar23 = lVar23 + 0x100000000;
                lVar21 = lVar21 + -1;
                uVar25 = uVar25 - 1;
                lVar8 = lVar8 + 0x100;
              } while (uVar25 != 0);
            }
            iVar5 = iVar5 + 1;
            iVar6 = _DAT_ffff00009085b8e0;
          }
          puVar15 = puVar15 + 0x13;
        } while (iVar6 != 0);
      }
      FUN_ffff000090719710(0);
      FUN_ffff00009077e11c
                (s__s__d__mblock_reserved_memory_is_ffff0000908006f8,
                 s_mblock_fdt_reserved_append_ffff0000907e5d3b,0x6d2);
      FUN_ffff0000907177d0(&LAB_ffff000090818208);
      local_138 = 0;
      uStack_130 = 0;
      local_148 = 0;
      uStack_140 = 0;
      local_158 = 0;
      uStack_150 = 0;
      local_168 = 0;
      uStack_160 = 0;
      local_178 = 0;
      uStack_170 = 0;
      local_188 = 0;
      uStack_180 = 0;
      local_198 = 0;
      uStack_190 = 0;
      local_1a8 = 0;
      local_1a0 = 0;
      FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
      lVar8 = 0;
      plVar11 = (long *)(_DAT_ffff000090850c90 + 8);
      plVar16 = plVar11 + (ulong)(_DAT_ffff00009085acd0 - 1) * 3;
      if (plVar11 <= plVar16) {
        lVar8 = 0;
        do {
          if (*plVar11 != 0) {
            lVar8 = plVar11[1] + lVar8;
          }
          plVar11 = plVar11 + 3;
        } while (plVar11 <= plVar16);
      }
      if (_DAT_ffff00009085b8e0 != 0) {
        plVar11 = (long *)(_DAT_ffff000090850c90 + 0xc18);
        do {
          if (*plVar11 != 0) {
            lVar8 = plVar11[1] + lVar8;
          }
          plVar11 = plVar11 + 0x13;
        } while (plVar11 <= (long *)(_DAT_ffff000090850c90 + 0x5780U));
      }
      FUN_ffff0000907177d0(&LAB_ffff000090818208);
      iVar5 = FUN_ffff00009077f6fc
                        (&local_1a8,0x80,s_androidboot_ddr_size__llu_ffff0000907dc3bd,lVar8);
      if (iVar5 < 1) {
        FUN_ffff00009077e11c
                  (s__s__d__snprintf_error__failed_to_ffff0000907da76a,
                   s_mblock_fdt_kcmdline_append_ffff0000907f282f,0x6e2);
      }
      else {
        FUN_ffff000090719078(&local_1a8);
      }
      return;
    }
    FUN_ffff00009077e11c
              (s__s__d__couldn_t_find__reserved_m_ffff0000907fb394,
               s_reserved_memory_alloc_ranges_bas_ffff0000907f5f63,0x530);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(s_couldn_t_find__reserved_memory_ffff0000907f7db1);
}



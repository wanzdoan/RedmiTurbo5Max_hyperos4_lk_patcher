// ffff000090742508  size=1376  FUN_ffff000090742508

void FUN_ffff000090742508(undefined8 param_1)

{
  undefined8 uVar1;
  uint uVar2;
  ulong uVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  long lVar7;
  undefined8 uVar8;
  ulong *puVar9;
  char *pcVar10;
  uint uVar11;
  long lVar12;
  ulong local_88;
  ulong local_80;
  undefined8 local_78;
  undefined8 local_70;
  undefined4 local_64;
  
  local_64 = 0;
  local_70 = 0;
  local_80 = 0;
  local_78 = 0;
  local_88 = 0;
  iVar4 = FUN_ffff00009076da24(param_1,s__memory_ssmr_features_ffff0000907f96b8);
  if (iVar4 < 0) {
    FUN_ffff00009077e11c(s_can_t_find_memory_ssmr_features_ffff0000907e17fa);
  }
  else {
    for (iVar5 = FUN_ffff00009076db08(param_1,iVar4); -1 < iVar5;
        iVar5 = FUN_ffff00009076db8c(param_1,iVar5)) {
      lVar7 = FUN_ffff00009076deb0(param_1,iVar5,&local_70,&local_64);
      uVar1 = local_70;
      if (lVar7 != 0) {
        uVar8 = FUN_ffff00009077f250(s_svp_region_based_size_ffff00009081a1f8);
        iVar6 = FUN_ffff00009077f348(uVar1,s_svp_region_based_size_ffff00009081a1f8,uVar8);
        if (iVar6 == 0) {
          lVar7 = 0;
        }
        else {
          uVar8 = FUN_ffff00009077f250(s_prot_region_based_size_ffff00009081a248);
          iVar6 = FUN_ffff00009077f348(uVar1,s_prot_region_based_size_ffff00009081a248,uVar8);
          if (iVar6 == 0) {
            lVar7 = 1;
          }
          else {
            uVar8 = FUN_ffff00009077f250(s_wfd_region_based_size_ffff00009081a298);
            iVar6 = FUN_ffff00009077f348(uVar1,s_wfd_region_based_size_ffff00009081a298,uVar8);
            if (iVar6 == 0) {
              lVar7 = 2;
            }
            else {
              uVar8 = FUN_ffff00009077f250(s_ta_elf_size_ffff00009081a2e8);
              iVar6 = FUN_ffff00009077f348(uVar1,s_ta_elf_size_ffff00009081a2e8,uVar8);
              if (iVar6 == 0) {
                lVar7 = 3;
              }
              else {
                uVar8 = FUN_ffff00009077f250(s_ta_stack_heap_size_ffff00009081a338);
                iVar6 = FUN_ffff00009077f348(uVar1,s_ta_stack_heap_size_ffff00009081a338,uVar8);
                if (iVar6 == 0) {
                  lVar7 = 4;
                }
                else {
                  uVar8 = FUN_ffff00009077f250(s_sdsp_firmware_size_ffff00009081a388);
                  iVar6 = FUN_ffff00009077f348(uVar1,s_sdsp_firmware_size_ffff00009081a388,uVar8);
                  if (iVar6 == 0) {
                    lVar7 = 5;
                  }
                  else {
                    uVar8 = FUN_ffff00009077f250(s_sdsp_tee_sharedmem_size_ffff00009081a3d8);
                    iVar6 = FUN_ffff00009077f348
                                      (uVar1,s_sdsp_tee_sharedmem_size_ffff00009081a3d8,uVar8);
                    if (iVar6 == 0) {
                      lVar7 = 6;
                    }
                    else {
                      uVar8 = FUN_ffff00009077f250(s_2d_fr_size_ffff00009081a428);
                      iVar6 = FUN_ffff00009077f348(uVar1,s_2d_fr_size_ffff00009081a428,uVar8);
                      if (iVar6 == 0) {
                        lVar7 = 7;
                      }
                      else {
                        uVar8 = FUN_ffff00009077f250(s_tui_size_ffff00009081a478);
                        iVar6 = FUN_ffff00009077f348(uVar1,s_tui_size_ffff00009081a478,uVar8);
                        if (iVar6 == 0) {
                          lVar7 = 8;
                        }
                        else {
                          uVar8 = FUN_ffff00009077f250(s_sapu_data_shm_size_ffff00009081a4c8);
                          iVar6 = FUN_ffff00009077f348
                                            (uVar1,s_sapu_data_shm_size_ffff00009081a4c8,uVar8);
                          if (iVar6 == 0) {
                            lVar7 = 9;
                          }
                          else {
                            uVar8 = FUN_ffff00009077f250(s_sapu_engine_shm_size_ffff00009081a518);
                            iVar6 = FUN_ffff00009077f348
                                              (uVar1,s_sapu_engine_shm_size_ffff00009081a518,uVar8);
                            if (iVar6 != 0) goto LAB_ffff00009074257c;
                            lVar7 = 10;
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        lVar12 = 0;
        do {
          if (((*(uint *)((long)&DAT_ffff00009081a590 + lVar12) &
               *(uint *)(lVar7 * 0x50 + -0xffff6f7e5dc8)) != 0) &&
             (puVar9 = (ulong *)FUN_ffff00009076df9c(param_1,iVar4,local_70,&local_64),
             puVar9 != (ulong *)0x0)) {
            iVar6 = FUN_ffff00009076e32c
                              (param_1,0xffffffff,
                               s_mediatek_mtk_sec_video_path_supp_ffff0000907e96eb);
            uVar1 = local_70;
            if (iVar6 < 0) {
              uVar8 = FUN_ffff00009077f250(local_70);
              iVar6 = FUN_ffff00009077f348(uVar1,s_svp_region_based_size_ffff00009081a1f8,uVar8);
              if ((iVar6 == 0) ||
                 (iVar6 = FUN_ffff00009077f348(uVar1,s_wfd_region_based_size_ffff00009081a298,uVar8)
                 , iVar6 == 0)) goto LAB_ffff0000907427c0;
            }
            uVar3 = (*puVar9 & 0xff00ff00ff00ff00) >> 8 | (*puVar9 & 0xff00ff00ff00ff) << 8;
            uVar3 = (uVar3 & 0xffff0000ffff0000) >> 0x10 | (uVar3 & 0xffff0000ffff) << 0x10;
            *(ulong *)((long)&DAT_ffff00009081a588 + lVar12) =
                 *(long *)((long)&DAT_ffff00009081a588 + lVar12) + (uVar3 >> 0x20 | uVar3 << 0x20);
          }
LAB_ffff0000907427c0:
          lVar12 = lVar12 + 0x30;
        } while (lVar12 != 0xf0);
      }
LAB_ffff00009074257c:
    }
    FUN_ffff00009077e11c
              (s__s__scenario___s__scenario_size_0_ffff0000907f84c0,
               s_memory_ssmr_fixup_ffff0000907efd5e,s_svp_scheme_ffff00009081a568,
               DAT_ffff00009081a588);
    uVar3 = DAT_ffff00009081a588;
    FUN_ffff00009077e11c
              (s__s__scenario___s__scenario_size_0_ffff0000907f84c0,
               s_memory_ssmr_fixup_ffff0000907efd5e,s_face_registration_scheme_ffff00009081a598,
               DAT_ffff00009081a5b8);
    if (uVar3 <= DAT_ffff00009081a5b8) {
      uVar3 = DAT_ffff00009081a5b8;
    }
    FUN_ffff00009077e11c
              (s__s__scenario___s__scenario_size_0_ffff0000907f84c0,
               s_memory_ssmr_fixup_ffff0000907efd5e,s_face_payment_scheme_ffff00009081a5c8,
               DAT_ffff00009081a5e8);
    if (uVar3 <= DAT_ffff00009081a5e8) {
      uVar3 = DAT_ffff00009081a5e8;
    }
    FUN_ffff00009077e11c
              (s__s__scenario___s__scenario_size_0_ffff0000907f84c0,
               s_memory_ssmr_fixup_ffff0000907efd5e,s_face_unlock_scheme_ffff00009081a5f8,
               DAT_ffff00009081a618);
    if (uVar3 <= DAT_ffff00009081a618) {
      uVar3 = DAT_ffff00009081a618;
    }
    FUN_ffff00009077e11c
              (s__s__scenario___s__scenario_size_0_ffff0000907f84c0,
               s_memory_ssmr_fixup_ffff0000907efd5e,s_tui_scheme_ffff00009081a628,
               DAT_ffff00009081a648);
    if (uVar3 <= DAT_ffff00009081a648) {
      uVar3 = DAT_ffff00009081a648;
    }
    iVar4 = FUN_ffff00009076da24(param_1,s__reserved_memory_ffff0000907fce9a);
    if (iVar4 < 0) {
      pcVar10 = s__s__get_reserved_memory_failed_ffff0000907f2f00;
    }
    else {
      iVar5 = FUN_ffff00009076d814(param_1,iVar4,s_ssmr_reserved_cma_memory_ffff0000907e4ce9);
      if ((iVar5 < 0) &&
         (iVar5 = FUN_ffff00009076d814(param_1,iVar4,s_ssmr_reserved_cma_memory_ffff0000907fd781),
         iVar5 < 0)) {
        pcVar10 = s__s__get_ssmr_reserved_cma_memory_ffff000090800dd4;
      }
      else {
        uVar2 = ((uint)uVar3 & 0xff00ff00) >> 8 | ((uint)uVar3 & 0xff00ff) << 8;
        uVar11 = (uint)(uVar3 >> 0x20);
        uVar11 = (uVar11 & 0xff00ff00) >> 8 | (uVar11 & 0xff00ff) << 8;
        local_78 = CONCAT44(uVar2 >> 0x10 | uVar2 << 0x10,uVar11 >> 0x10 | uVar11 << 0x10);
        iVar4 = FUN_ffff00009076e6d4(param_1,iVar5,&DAT_ffff0000907d587a,&local_78,8);
        if (iVar4 == 0) {
          iVar4 = FUN_ffff00009071c708();
          uVar1 = 0x80000000;
          if (iVar4 != 0) {
            uVar1 = 0x240000000;
          }
          uVar8 = 0xc0000000;
          if (iVar4 != 0) {
            uVar8 = 0x100000000;
          }
          local_88 = CONCAT44((int)((ulong)uVar1 >> 0x18),(int)((ulong)uVar1 >> 8)) & 0xff02000000;
          local_80 = CONCAT44((int)((ulong)uVar8 >> 0x18),(int)((ulong)uVar8 >> 8)) & 0xff01000000;
          iVar4 = FUN_ffff00009076e6d4(param_1,iVar5,s_alloc_ranges_ffff0000907e448a,&local_88,0x10)
          ;
          if (iVar4 == 0) {
            return;
          }
          pcVar10 = s__s__set_ssmr_reserved_cma_memory_ffff0000907ee47c;
        }
        else {
          pcVar10 = s__s__set_ssmr_reserved_cma_memory_ffff0000907f66c8;
        }
      }
    }
    FUN_ffff00009077e11c(pcVar10,s_memory_ssmr_fixup_ffff0000907efd5e);
  }
  FUN_ffff00009077e11c(s__s__exit_with_error_ffff0000907de39b,s_memory_ssmr_fixup_ffff0000907efd5e);
  return;
}



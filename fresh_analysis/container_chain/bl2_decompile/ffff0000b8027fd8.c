
int FUN_ffff0000b8027fd8(void)

{
  undefined *puVar1;
  long lVar2;
  uint uVar3;
  uint uVar4;
  undefined *puVar5;
  int iVar6;
  int iVar7;
  undefined4 uVar8;
  long lVar9;
  undefined8 uVar10;
  long *plVar11;
  uint *puVar12;
  char *pcVar13;
  undefined8 uVar14;
  undefined8 uVar15;
  long lVar16;
  code *pcVar17;
  long lVar18;
  undefined8 local_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  long local_60;
  undefined8 local_58;
  long local_38;
  
  local_78 = 0;
  uStack_70 = 0x656574;
  local_68 = 0;
  local_80 = 0;
  lVar9 = FUN_ffff0000b800c9f4(0xc200000,0x200000,0x100000000,0,0,s_tee_reserved_ffff0000b8093692);
  if (lVar9 == 0) {
    FUN_ffff0000b804c33c(s__s_mblock_allocation_failed_ffff0000b8099d43,s__BL32__ffff0000b808d21a);
    return -5;
  }
  iVar6 = FUN_ffff0000b800e3e0();
  if (iVar6 == 0) {
    uVar10 = 0;
  }
  else {
    uVar10 = FUN_ffff0000b800c9f4
                       (0xc200000,0x200000,0xffffffffffffffff,lVar9 + 0x400000000,0,
                        s_tee_reserved_ffff0000b8093692);
  }
  local_60 = 0;
  lVar2 = lVar9 + 0x200000;
  iVar6 = FUN_ffff0000b804a5f8
                    (PTR_DAT_ffff0000b81086d8,s_vm_BL32_reserved_ffff0000b80955b6,0xc200000,
                     &local_60,0xc,lVar9,0,0);
  if (iVar6 != 0) {
    FUN_ffff0000b804c33c
              (s__s_vmm_alloc_physical_failed__er_ffff0000b808f00f,s__BL32__ffff0000b808d21a,iVar6);
    goto LAB_ffff0000b80283c0;
  }
  iVar6 = FUN_ffff0000b8050eb8(&uStack_70,&DAT_ffff0000b8089c76,local_60 + 0x1ffdc0,0xc000000);
  if (iVar6 < 0) {
    pcVar13 = s__s_load_tee_failed__ret___d_ffff0000b8089c7a;
LAB_ffff0000b80283ac:
    FUN_ffff0000b804c33c(pcVar13,s__BL32__ffff0000b808d21a,iVar6);
  }
  else {
    FUN_ffff0000b800d8e4(lVar9,0x200000);
    iVar7 = FUN_ffff0000b800e3e0();
    if (iVar7 != 0) {
      FUN_ffff0000b800d8e4(uVar10,0x200000);
    }
    plVar11 = (long *)FUN_ffff0000b800db20(s_system_bl31_dtb_ffff0000b808c793,0);
    if (plVar11 == (long *)0x0) {
      FUN_ffff0000b804c33c
                (s___s___s_mblock__s_query_failed__ffff0000b80955c7,s__BL32__ffff0000b808d21a,
                 s_platform_load_tee_ffff0000b8089c97,s_system_bl31_dtb_ffff0000b808c793);
    }
    else {
      lVar9 = *plVar11;
      if (lVar9 != 0) {
        local_58 = 0;
        if (plVar11[1] == 0) {
          FUN_ffff0000b804c33c(s__s_Wrong_size_ffff0000b808aad4,s__BL32__ffff0000b808d21a);
LAB_ffff0000b8028204:
          pcVar13 = s__s_vmm_alloc_physical_failed__er_ffff0000b808f00f;
          iVar6 = -1;
LAB_ffff0000b8028218:
          FUN_ffff0000b804c33c(pcVar13,s__BL32__ffff0000b808d21a,iVar6);
        }
        else {
          local_38 = 0;
          iVar6 = FUN_ffff0000b804a5f8
                            (PTR_DAT_ffff0000b81086d8,s_bl31_dts_reserved_ffff0000b80981a9,
                             plVar11[1],&local_38,0xc,lVar9,0,0);
          lVar18 = local_38;
          puVar5 = PTR_s_Mitee_ffff0000b8108730;
          if (iVar6 != 0) {
            FUN_ffff0000b804c33c
                      (s__s_vmm_alloc_physical_failed__er_ffff0000b808f00f,s__BL32__ffff0000b808d21a
                       ,iVar6);
            goto LAB_ffff0000b8028204;
          }
          if (local_38 == 0) goto LAB_ffff0000b8028204;
          puVar1 = PTR_s_Mitee_ffff0000b8108730 + 0x20;
          iVar6 = FUN_ffff0000b8047d28(local_38,puVar1);
          if (iVar6 < 0) {
            FUN_ffff0000b804c33c
                      (s__s_bl31_dts_can_t_find___s__node_ffff0000b809b95b,s__BL32__ffff0000b808d21a
                       ,puVar1);
LAB_ffff0000b8028454:
            iVar6 = FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,lVar18);
            if (iVar6 == 0) goto LAB_ffff0000b802821c;
            pcVar13 = s__s_vmm_free_region_failed__err____ffff0000b808fd68;
            goto LAB_ffff0000b8028218;
          }
          puVar12 = (uint *)FUN_ffff0000b800db20(s_tee_reserved_ffff0000b8093692,0);
          if (puVar12 != (uint *)0x0) {
            uVar3 = (puVar12[1] & 0xff00ff00) >> 8 | (puVar12[1] & 0xff00ff) << 8;
            uVar4 = (*puVar12 & 0xff00ff00) >> 8 | (*puVar12 & 0xff00ff) << 8;
            local_58 = CONCAT44(uVar4 >> 0x10 | uVar4 << 0x10,uVar3 >> 0x10 | uVar3 << 0x10);
            FUN_ffff0000b8048774(lVar18,iVar6,puVar5 + 0x40,&local_58,8);
            uVar3 = (puVar12[2] & 0xff00ff00) >> 8 | (puVar12[2] & 0xff00ff) << 8;
            local_58 = (ulong)(uVar3 >> 0x10 | uVar3 << 0x10) << 0x20;
            FUN_ffff0000b8048774(lVar18,iVar6,puVar5 + 0x60,&local_58,8);
            FUN_ffff0000b804c33c
                      (s__s_update_bl31_dts_pass_ffff0000b80854c8,s__BL32__ffff0000b808d21a);
            goto LAB_ffff0000b8028454;
          }
          FUN_ffff0000b804c33c
                    (s__s_query___s__fail_ffff0000b809d904,s__BL32__ffff0000b808d21a,
                     s_tee_reserved_ffff0000b8093692);
        }
LAB_ffff0000b802821c:
        FUN_ffff0000b8027fc8(&local_78,&local_80,lVar9);
        uVar15 = local_78;
        uVar14 = local_80;
        if (lVar2 == 0) {
          iVar6 = -8;
        }
        else {
          plVar11 = (long *)FUN_ffff0000b800db20(s_tee_secmem_ffff0000b8089ca9,0);
          if (plVar11 != (long *)0x0) {
            if (*(long *)PTR_DAT_ffff0000b81086b0 == 0) {
              FUN_ffff0000b804c33c(s__s_Wrong_address_ffff0000b809b97d,s__BL32__ffff0000b808d21a);
LAB_ffff0000b802836c:
              pcVar13 = s__s_Map_BL_parameter_failed_ffff0000b8089cb4;
LAB_ffff0000b8028374:
              FUN_ffff0000b804c33c(pcVar13,s__BL32__ffff0000b808d21a);
              pcVar13 = s__s_Pass_ATF_argument_failed_ffff0000b8098e63;
            }
            else {
              lVar18 = *plVar11;
              local_38 = 0;
              iVar6 = FUN_ffff0000b804a5f8
                                (PTR_DAT_ffff0000b81086d8,s_vm_bl_reserved_ffff0000b8086150,0x1000,
                                 &local_38,0xc,*(long *)PTR_DAT_ffff0000b81086b0,0,0);
              if (iVar6 != 0) {
                FUN_ffff0000b804c33c
                          (s__s_vmm_alloc_physical_failed__er_ffff0000b808f00f,
                           s__BL32__ffff0000b808d21a,iVar6);
                goto LAB_ffff0000b802836c;
              }
              if (local_38 == 0) goto LAB_ffff0000b802836c;
              *(long *)(local_38 + 0x18) = lVar18;
              iVar6 = FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8);
              if (iVar6 != 0) {
                FUN_ffff0000b804c33c
                          (s__s_vmm_free_region_failed__err____ffff0000b808fd68,
                           s__BL32__ffff0000b808d21a,iVar6);
                pcVar13 = s__s_Umap_BL_parameter_failed_ffff0000b809e72b;
                goto LAB_ffff0000b8028374;
              }
              if (*plVar11 == 0) {
                pcVar13 = s__s_Wrong_address_ffff0000b809b97d;
LAB_ffff0000b802850c:
                FUN_ffff0000b804c33c(pcVar13,s__BL32__ffff0000b808d21a);
LAB_ffff0000b8028518:
                pcVar13 = s__s_Map_ATF_argument_failed_ffff0000b808e091;
              }
              else {
                if (plVar11[1] == 0) {
                  pcVar13 = s__s_Wrong_size_ffff0000b808aad4;
                  goto LAB_ffff0000b802850c;
                }
                local_38 = 0;
                iVar6 = FUN_ffff0000b804a5f8
                                  (PTR_DAT_ffff0000b81086d8,s_vm_tee_reserved_ffff0000b8087332,
                                   plVar11[1],&local_38,0xc,*plVar11,0,0);
                lVar18 = local_38;
                if (iVar6 != 0) {
                  FUN_ffff0000b804c33c
                            (s__s_vmm_alloc_physical_failed__er_ffff0000b808f00f,
                             s__BL32__ffff0000b808d21a,iVar6);
                  goto LAB_ffff0000b8028518;
                }
                if (local_38 == 0) goto LAB_ffff0000b8028518;
                FUN_ffff0000b804d070(local_38,0,0x100);
                *(long *)(lVar18 + 8) = lVar2;
                *(undefined4 *)(lVar18 + 4) = 1;
                lVar16 = *plVar11;
                *(int *)(lVar18 + 0x60) = (int)uVar15;
                *(int *)(lVar18 + 100) = (int)uVar14;
                *(long *)(lVar18 + 0x10) = lVar16 + 0x100;
                *(long *)(lVar18 + 0x18) = lVar9;
                FUN_ffff0000b804cd40(lVar18 + 0x20,&DAT_ffff0000b8124f80,0x10);
                FUN_ffff0000b804d070(&DAT_ffff0000b8124f80,0,0x10);
                uVar8 = thunk_FUN_ffff0000b802ce0c(0xc);
                *(undefined4 *)(lVar18 + 0x30) = uVar8;
                uVar8 = thunk_FUN_ffff0000b802ce0c(0xd);
                *(undefined4 *)(lVar18 + 0x34) = uVar8;
                uVar8 = thunk_FUN_ffff0000b802ce0c(0xe);
                *(undefined4 *)(lVar18 + 0x38) = uVar8;
                uVar8 = thunk_FUN_ffff0000b802ce0c(0xf);
                *(undefined4 *)(lVar18 + 0x3c) = uVar8;
                *(undefined8 *)(lVar18 + 0x40) = 0;
                *(undefined8 *)(lVar18 + 0x48) = 0;
                iVar6 = FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,lVar18);
                if (iVar6 == 0) {
                  plVar11 = (long *)FUN_ffff0000b800db20(s_tee_secmem_ffff0000b8089ca9,0);
                  if (plVar11 != (long *)0x0) {
                    if (*plVar11 == 0) {
                      pcVar13 = s__s_Wrong_address_ffff0000b809b97d;
LAB_ffff0000b8028670:
                      FUN_ffff0000b804c33c(pcVar13,s__BL32__ffff0000b808d21a);
LAB_ffff0000b802867c:
                      pcVar13 = s__s_Map_TEE_argument_failed_ffff0000b809c866;
                    }
                    else {
                      if (plVar11[1] == 0) {
                        pcVar13 = s__s_Wrong_size_ffff0000b808aad4;
                        goto LAB_ffff0000b8028670;
                      }
                      local_38 = 0;
                      iVar6 = FUN_ffff0000b804a5f8
                                        (PTR_DAT_ffff0000b81086d8,s_vm_tee_reserved_ffff0000b8087332
                                         ,plVar11[1],&local_38,0xc,*plVar11,0,0);
                      if (iVar6 != 0) {
                        FUN_ffff0000b804c33c
                                  (s__s_vmm_alloc_physical_failed__er_ffff0000b808f00f,
                                   s__BL32__ffff0000b808d21a,iVar6);
                        goto LAB_ffff0000b802867c;
                      }
                      if (local_38 == 0) goto LAB_ffff0000b802867c;
                      lVar9 = local_38 + 0x100;
                      FUN_ffff0000b804d070(lVar9,0,0x100);
                      pcVar17 = *(code **)(PTR_s_Mitee_ffff0000b8108730 + 0x18);
                      if (pcVar17 != (code *)0x0) {
                        uVar14 = FUN_ffff0000b800dccc();
                        uVar15 = FUN_ffff0000b800dbfc();
                        (*pcVar17)(lVar9,lVar2,0xc000000,uVar14,uVar15);
                      }
                      if (lVar9 == 0) {
                        FUN_ffff0000b804c33c
                                  (s__s_Wrong_address_ffff0000b809b97d,s__BL32__ffff0000b808d21a);
                      }
                      else {
                        iVar6 = FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,lVar9);
                        if (iVar6 == 0) {
                          FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_60);
                          return 0;
                        }
                        FUN_ffff0000b804c33c
                                  (s__s_vmm_free_region_failed__err____ffff0000b808fd68,
                                   s__BL32__ffff0000b808d21a,iVar6);
                      }
                      pcVar13 = s__s_Umap_TEE_argument_failed_ffff0000b808aae3;
                    }
                    FUN_ffff0000b804c33c(pcVar13,s__BL32__ffff0000b808d21a);
                  }
                  pcVar13 = s__s_Set_TEE_argument_failed__ret__ffff0000b809d8df;
                  iVar6 = -1;
                  goto LAB_ffff0000b80283ac;
                }
                FUN_ffff0000b804c33c
                          (s__s_vmm_free_region_failed__err____ffff0000b808fd68,
                           s__BL32__ffff0000b808d21a,iVar6);
                pcVar13 = s__s_Umap_ATF_argument_failed_ffff0000b8087342;
              }
            }
            FUN_ffff0000b804c33c(pcVar13,s__BL32__ffff0000b808d21a);
          }
          iVar6 = -1;
        }
        pcVar13 = s__s_Set_ATF_argument_failed__ret__ffff0000b808fd43;
        goto LAB_ffff0000b80283ac;
      }
      FUN_ffff0000b804c33c
                (s___s__mblock__s_query_pa_0__ffff0000b80a1233,s__BL32__ffff0000b808d21a,
                 s_system_bl31_dtb_ffff0000b808c793);
    }
  }
  FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_60);
LAB_ffff0000b80283c0:
  FUN_ffff0000b800d264(lVar2);
  iVar7 = FUN_ffff0000b800e3e0();
  if (iVar7 != 0) {
    FUN_ffff0000b800d264(uVar10);
  }
  return iVar6;
}



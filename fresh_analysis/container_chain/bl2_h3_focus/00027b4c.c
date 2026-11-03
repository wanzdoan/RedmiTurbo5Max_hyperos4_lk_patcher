// File offset entry: 0x27b4c

int FUN_ffff0000b8027b4c(void)

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
  lVar9 = FUN_ffff0000b800c9f4(0xc200000,0x200000,0x100000000,0,0,s_tee_reserved_ffff0000b80925c3);
  if (lVar9 == 0) {
    FUN_ffff0000b804beb0(s__s_mblock_allocation_failed_ffff0000b8098c6a,s__BL32__ffff0000b808c1d4);
    return -5;
  }
  iVar6 = FUN_ffff0000b800e3c4();
  if (iVar6 == 0) {
    uVar10 = 0;
  }
  else {
    uVar10 = FUN_ffff0000b800c9f4
                       (0xc200000,0x200000,0xffffffffffffffff,lVar9 + 0x400000000,0,
                        s_tee_reserved_ffff0000b80925c3);
  }
  local_60 = 0;
  lVar2 = lVar9 + 0x200000;
  iVar6 = FUN_ffff0000b804a16c
                    (PTR_DAT_ffff0000b8107610,s_vm_BL32_reserved_ffff0000b80944bb,0xc200000,
                     &local_60,0xc,lVar9,0,0);
  if (iVar6 != 0) {
    FUN_ffff0000b804beb0
              (s__s_vmm_alloc_physical_failed__er_ffff0000b808dfc9,s__BL32__ffff0000b808c1d4,iVar6);
    goto LAB_ffff0000b8027f34;
  }
  iVar6 = FUN_ffff0000b8050a2c(&uStack_70,&DAT_ffff0000b8088c01,local_60 + 0x1ffdc0,0xc000000);
  if (iVar6 < 0) {
    pcVar13 = s__s_load_tee_failed__ret___d_ffff0000b8088c05;
LAB_ffff0000b8027f20:
    FUN_ffff0000b804beb0(pcVar13,s__BL32__ffff0000b808c1d4,iVar6);
  }
  else {
    FUN_ffff0000b800d8e4(lVar9,0x200000);
    iVar7 = FUN_ffff0000b800e3c4();
    if (iVar7 != 0) {
      FUN_ffff0000b800d8e4(uVar10,0x200000);
    }
    plVar11 = (long *)FUN_ffff0000b800db20(s_system_bl31_dtb_ffff0000b808b74d,0);
    if (plVar11 == (long *)0x0) {
      FUN_ffff0000b804beb0
                (s___s___s_mblock__s_query_failed__ffff0000b80944cc,s__BL32__ffff0000b808c1d4,
                 s_platform_load_tee_ffff0000b8088c22,s_system_bl31_dtb_ffff0000b808b74d);
    }
    else {
      lVar9 = *plVar11;
      if (lVar9 != 0) {
        local_58 = 0;
        if (plVar11[1] == 0) {
          FUN_ffff0000b804beb0(s__s_Wrong_size_ffff0000b8089a74,s__BL32__ffff0000b808c1d4);
LAB_ffff0000b8027d78:
          pcVar13 = s__s_vmm_alloc_physical_failed__er_ffff0000b808dfc9;
          iVar6 = -1;
LAB_ffff0000b8027d8c:
          FUN_ffff0000b804beb0(pcVar13,s__BL32__ffff0000b808c1d4,iVar6);
        }
        else {
          local_38 = 0;
          iVar6 = FUN_ffff0000b804a16c
                            (PTR_DAT_ffff0000b8107610,s_bl31_dts_reserved_ffff0000b80970d0,
                             plVar11[1],&local_38,0xc,lVar9,0,0);
          lVar18 = local_38;
          puVar5 = PTR_s_Mitee_ffff0000b8107668;
          if (iVar6 != 0) {
            FUN_ffff0000b804beb0
                      (s__s_vmm_alloc_physical_failed__er_ffff0000b808dfc9,s__BL32__ffff0000b808c1d4
                       ,iVar6);
            goto LAB_ffff0000b8027d78;
          }
          if (local_38 == 0) goto LAB_ffff0000b8027d78;
          puVar1 = PTR_s_Mitee_ffff0000b8107668 + 0x20;
          iVar6 = FUN_ffff0000b804789c(local_38,puVar1);
          if (iVar6 < 0) {
            FUN_ffff0000b804beb0
                      (s__s_bl31_dts_can_t_find___s__node_ffff0000b809a878,s__BL32__ffff0000b808c1d4
                       ,puVar1);
LAB_ffff0000b8027fc8:
            iVar6 = FUN_ffff0000b804a318(PTR_DAT_ffff0000b8107610,lVar18);
            if (iVar6 == 0) goto LAB_ffff0000b8027d90;
            pcVar13 = s__s_vmm_free_region_failed__err____ffff0000b808ed08;
            goto LAB_ffff0000b8027d8c;
          }
          puVar12 = (uint *)FUN_ffff0000b800db20(s_tee_reserved_ffff0000b80925c3,0);
          if (puVar12 != (uint *)0x0) {
            uVar3 = (puVar12[1] & 0xff00ff00) >> 8 | (puVar12[1] & 0xff00ff) << 8;
            uVar4 = (*puVar12 & 0xff00ff00) >> 8 | (*puVar12 & 0xff00ff) << 8;
            local_58 = CONCAT44(uVar4 >> 0x10 | uVar4 << 0x10,uVar3 >> 0x10 | uVar3 << 0x10);
            FUN_ffff0000b80482e8(lVar18,iVar6,puVar5 + 0x40,&local_58,8);
            uVar3 = (puVar12[2] & 0xff00ff00) >> 8 | (puVar12[2] & 0xff00ff) << 8;
            local_58 = (ulong)(uVar3 >> 0x10 | uVar3 << 0x10) << 0x20;
            FUN_ffff0000b80482e8(lVar18,iVar6,puVar5 + 0x60,&local_58,8);
            FUN_ffff0000b804beb0
                      (s__s_update_bl31_dts_pass_ffff0000b808449b,s__BL32__ffff0000b808c1d4);
            goto LAB_ffff0000b8027fc8;
          }
          FUN_ffff0000b804beb0
                    (s__s_query___s__fail_ffff0000b809c7f1,s__BL32__ffff0000b808c1d4,
                     s_tee_reserved_ffff0000b80925c3);
        }
LAB_ffff0000b8027d90:
        FUN_ffff0000b8027b3c(&local_78,&local_80,lVar9);
        uVar15 = local_78;
        uVar14 = local_80;
        if (lVar2 == 0) {
          iVar6 = -8;
        }
        else {
          plVar11 = (long *)FUN_ffff0000b800db20(s_tee_secmem_ffff0000b8088c34,0);
          if (plVar11 != (long *)0x0) {
            if (*(long *)PTR_DAT_ffff0000b81075e8 == 0) {
              FUN_ffff0000b804beb0(s__s_Wrong_address_ffff0000b809a89a,s__BL32__ffff0000b808c1d4);
LAB_ffff0000b8027ee0:
              pcVar13 = s__s_Map_BL_parameter_failed_ffff0000b8088c3f;
LAB_ffff0000b8027ee8:
              FUN_ffff0000b804beb0(pcVar13,s__BL32__ffff0000b808c1d4);
              pcVar13 = s__s_Pass_ATF_argument_failed_ffff0000b8097d8a;
            }
            else {
              lVar18 = *plVar11;
              local_38 = 0;
              iVar6 = FUN_ffff0000b804a16c
                                (PTR_DAT_ffff0000b8107610,s_vm_bl_reserved_ffff0000b808511b,0x1000,
                                 &local_38,0xc,*(long *)PTR_DAT_ffff0000b81075e8,0,0);
              if (iVar6 != 0) {
                FUN_ffff0000b804beb0
                          (s__s_vmm_alloc_physical_failed__er_ffff0000b808dfc9,
                           s__BL32__ffff0000b808c1d4,iVar6);
                goto LAB_ffff0000b8027ee0;
              }
              if (local_38 == 0) goto LAB_ffff0000b8027ee0;
              *(long *)(local_38 + 0x18) = lVar18;
              iVar6 = FUN_ffff0000b804a318(PTR_DAT_ffff0000b8107610);
              if (iVar6 != 0) {
                FUN_ffff0000b804beb0
                          (s__s_vmm_free_region_failed__err____ffff0000b808ed08,
                           s__BL32__ffff0000b808c1d4,iVar6);
                pcVar13 = s__s_Umap_BL_parameter_failed_ffff0000b809d618;
                goto LAB_ffff0000b8027ee8;
              }
              if (*plVar11 == 0) {
                pcVar13 = s__s_Wrong_address_ffff0000b809a89a;
LAB_ffff0000b8028080:
                FUN_ffff0000b804beb0(pcVar13,s__BL32__ffff0000b808c1d4);
LAB_ffff0000b802808c:
                pcVar13 = s__s_Map_ATF_argument_failed_ffff0000b808d04b;
              }
              else {
                if (plVar11[1] == 0) {
                  pcVar13 = s__s_Wrong_size_ffff0000b8089a74;
                  goto LAB_ffff0000b8028080;
                }
                local_38 = 0;
                iVar6 = FUN_ffff0000b804a16c
                                  (PTR_DAT_ffff0000b8107610,s_vm_tee_reserved_ffff0000b80862fd,
                                   plVar11[1],&local_38,0xc,*plVar11,0,0);
                lVar18 = local_38;
                if (iVar6 != 0) {
                  FUN_ffff0000b804beb0
                            (s__s_vmm_alloc_physical_failed__er_ffff0000b808dfc9,
                             s__BL32__ffff0000b808c1d4,iVar6);
                  goto LAB_ffff0000b802808c;
                }
                if (local_38 == 0) goto LAB_ffff0000b802808c;
                FUN_ffff0000b804cbe4(local_38,0,0x100);
                *(long *)(lVar18 + 8) = lVar2;
                *(undefined4 *)(lVar18 + 4) = 1;
                lVar16 = *plVar11;
                *(int *)(lVar18 + 0x60) = (int)uVar15;
                *(int *)(lVar18 + 100) = (int)uVar14;
                *(long *)(lVar18 + 0x10) = lVar16 + 0x100;
                *(long *)(lVar18 + 0x18) = lVar9;
                FUN_ffff0000b804c8b4(lVar18 + 0x20,&DAT_ffff0000b8123f20,0x10);
                FUN_ffff0000b804cbe4(&DAT_ffff0000b8123f20,0,0x10);
                uVar8 = thunk_FUN_ffff0000b802c980(0xc);
                *(undefined4 *)(lVar18 + 0x30) = uVar8;
                uVar8 = thunk_FUN_ffff0000b802c980(0xd);
                *(undefined4 *)(lVar18 + 0x34) = uVar8;
                uVar8 = thunk_FUN_ffff0000b802c980(0xe);
                *(undefined4 *)(lVar18 + 0x38) = uVar8;
                uVar8 = thunk_FUN_ffff0000b802c980(0xf);
                *(undefined4 *)(lVar18 + 0x3c) = uVar8;
                *(undefined8 *)(lVar18 + 0x40) = 0;
                *(undefined8 *)(lVar18 + 0x48) = 0;
                iVar6 = FUN_ffff0000b804a318(PTR_DAT_ffff0000b8107610,lVar18);
                if (iVar6 == 0) {
                  plVar11 = (long *)FUN_ffff0000b800db20(s_tee_secmem_ffff0000b8088c34,0);
                  if (plVar11 != (long *)0x0) {
                    if (*plVar11 == 0) {
                      pcVar13 = s__s_Wrong_address_ffff0000b809a89a;
LAB_ffff0000b80281e4:
                      FUN_ffff0000b804beb0(pcVar13,s__BL32__ffff0000b808c1d4);
LAB_ffff0000b80281f0:
                      pcVar13 = s__s_Map_TEE_argument_failed_ffff0000b809b767;
                    }
                    else {
                      if (plVar11[1] == 0) {
                        pcVar13 = s__s_Wrong_size_ffff0000b8089a74;
                        goto LAB_ffff0000b80281e4;
                      }
                      local_38 = 0;
                      iVar6 = FUN_ffff0000b804a16c
                                        (PTR_DAT_ffff0000b8107610,s_vm_tee_reserved_ffff0000b80862fd
                                         ,plVar11[1],&local_38,0xc,*plVar11,0,0);
                      if (iVar6 != 0) {
                        FUN_ffff0000b804beb0
                                  (s__s_vmm_alloc_physical_failed__er_ffff0000b808dfc9,
                                   s__BL32__ffff0000b808c1d4,iVar6);
                        goto LAB_ffff0000b80281f0;
                      }
                      if (local_38 == 0) goto LAB_ffff0000b80281f0;
                      lVar9 = local_38 + 0x100;
                      FUN_ffff0000b804cbe4(lVar9,0,0x100);
                      pcVar17 = *(code **)(PTR_s_Mitee_ffff0000b8107668 + 0x18);
                      if (pcVar17 != (code *)0x0) {
                        uVar14 = FUN_ffff0000b800dccc();
                        uVar15 = FUN_ffff0000b800dbfc();
                        (*pcVar17)(lVar9,lVar2,0xc000000,uVar14,uVar15);
                      }
                      if (lVar9 == 0) {
                        FUN_ffff0000b804beb0
                                  (s__s_Wrong_address_ffff0000b809a89a,s__BL32__ffff0000b808c1d4);
                      }
                      else {
                        iVar6 = FUN_ffff0000b804a318(PTR_DAT_ffff0000b8107610,lVar9);
                        if (iVar6 == 0) {
                          FUN_ffff0000b804a318(PTR_DAT_ffff0000b8107610,local_60);
                          return 0;
                        }
                        FUN_ffff0000b804beb0
                                  (s__s_vmm_free_region_failed__err____ffff0000b808ed08,
                                   s__BL32__ffff0000b808c1d4,iVar6);
                      }
                      pcVar13 = s__s_Umap_TEE_argument_failed_ffff0000b8089a83;
                    }
                    FUN_ffff0000b804beb0(pcVar13,s__BL32__ffff0000b808c1d4);
                  }
                  pcVar13 = s__s_Set_TEE_argument_failed__ret__ffff0000b809c7cc;
                  iVar6 = -1;
                  goto LAB_ffff0000b8027f20;
                }
                FUN_ffff0000b804beb0
                          (s__s_vmm_free_region_failed__err____ffff0000b808ed08,
                           s__BL32__ffff0000b808c1d4,iVar6);
                pcVar13 = s__s_Umap_ATF_argument_failed_ffff0000b808630d;
              }
            }
            FUN_ffff0000b804beb0(pcVar13,s__BL32__ffff0000b808c1d4);
          }
          iVar6 = -1;
        }
        pcVar13 = s__s_Set_ATF_argument_failed__ret__ffff0000b808ece3;
        goto LAB_ffff0000b8027f20;
      }
      FUN_ffff0000b804beb0
                (s___s__mblock__s_query_pa_0__ffff0000b80a0139,s__BL32__ffff0000b808c1d4,
                 s_system_bl31_dtb_ffff0000b808b74d);
    }
  }
  FUN_ffff0000b804a318(PTR_DAT_ffff0000b8107610,local_60);
LAB_ffff0000b8027f34:
  FUN_ffff0000b800d264(lVar2);
  iVar7 = FUN_ffff0000b800e3c4();
  if (iVar7 != 0) {
    FUN_ffff0000b800d264(uVar10);
  }
  return iVar6;
}



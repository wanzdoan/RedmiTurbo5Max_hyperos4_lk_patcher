
void FUN_ffff0000b8018cec(long param_1)

{
  long lVar1;
  long lVar2;
  long lVar3;
  ulong uVar4;
  long *plVar5;
  ulong uVar6;
  byte bVar7;
  long *plVar8;
  ulong uVar9;
  char *pcVar10;
  long lVar11;
  undefined1 *puVar12;
  uint uVar13;
  long *plVar14;
  long lVar15;
  long lVar16;
  ulong uVar17;
  undefined8 uVar18;
  byte bVar19;
  uint uVar20;
  long lVar21;
  ulong uVar22;
  long lVar23;
  long lVar24;
  long lVar25;
  int iVar26;
  long local_68;
  
  if ((param_1 == 0) || (DAT_ffff0000b811b0f8 == 0 || DAT_ffff0000b811b100 == (undefined1 *)0x0)) {
    FUN_ffff0000b804c33c(s_GSORT__should_not_be_null_ffff0000b809c4b1);
    return;
  }
  FUN_ffff0000b800da00();
  if (*(int *)*DAT_ffff0000b81086e0 != 0) {
    uVar20 = 0;
    do {
      plVar14 = DAT_ffff0000b81086e0;
      FUN_ffff0000b800c818();
      lVar1 = *plVar14 + (ulong)uVar20 * 0x10;
      uVar6 = *(ulong *)(lVar1 + 8);
      lVar1 = *(long *)(lVar1 + 0x10);
      *(char *)(DAT_ffff0000b811b0f8 + 0x28) = (char)uVar20;
      uVar4 = 0xc0000000;
      if (uVar20 != 0) {
        uVar4 = uVar6;
      }
      FUN_ffff0000b804c33c
                (s_GSORT__rank__d____0x_llx__0x_llx_ffff0000b80861e9,uVar20,uVar6,lVar1,uVar4);
      while (uVar9 = uVar4, uVar9 < lVar1 + uVar6) {
        FUN_ffff0000b804c33c(s_GSORT__TEST___0x_llx__0x_x__ffff0000b8091755,uVar9,0x40000000);
        uVar4 = uVar9 + 0x40000000;
        if (*(char *)(DAT_ffff0000b811b0f8 + 0x18) != '\0') {
          uVar17 = 0;
          do {
            bVar19 = 0;
            plVar14 = (long *)(param_1 + uVar17 * 0x18);
            do {
              while( true ) {
                plVar5 = plVar14 + 1;
                plVar8 = plVar14;
                pcVar10 = s__GSORT__Swap_pattern_before__ffff0000b808eb49;
                if (bVar19 != 0) {
                  plVar5 = plVar14;
                  plVar8 = plVar14 + 1;
                  pcVar10 = s__GSORT__Swap_pattern_after__ffff0000b8099b4c;
                }
                FUN_ffff0000b804c33c(pcVar10);
                lVar25 = *plVar5;
                lVar24 = *plVar8;
                FUN_ffff0000b804c33c(s_Start_ddr_remap_______________ffff0000b8088012);
                local_68 = -0xffff46000000;
                uVar13 = FUN_ffff0000b804a5f8
                                   (PTR_DAT_ffff0000b81086d8,s_vm_gsort_ffff0000b809a90f,0x40000000,
                                    &local_68,0xc,uVar9,1,0);
                if (uVar13 == 0) {
                  FUN_ffff0000b804c33c(s_Start_write_______________ffff0000b8091772);
                  lVar21 = 0;
                  local_68 = -0xffff46000000;
                  uVar22 = uVar9;
                  do {
                    FUN_ffff0000b804c33c
                              (s_GSORT__write__0x_016llx__0x_016l_ffff0000b80a0206,uVar22,local_68,
                               lVar24,lVar25);
                    lVar15 = 0;
                    do {
                      lVar16 = 0;
                      do {
                        plVar5 = (long *)(local_68 + lVar16);
                        lVar16 = lVar16 + 0x10;
                        *plVar5 = lVar24;
                        plVar5[1] = lVar24;
                      } while (lVar16 != 0x4000);
                      lVar16 = 0;
                      do {
                        lVar23 = local_68 + lVar16;
                        lVar16 = lVar16 + 0x10;
                        *(long *)(lVar23 + 0x4000) = lVar25;
                        *(long *)(lVar23 + 0x4008) = lVar25;
                      } while (lVar16 != 0x4000);
                      local_68 = local_68 + 0x8000;
                      lVar15 = lVar15 + 1;
                    } while (lVar15 != 0x40);
                    uVar22 = uVar22 + 0x200000;
                    lVar21 = lVar21 + 1;
                  } while (lVar21 != 0x200);
                  FUN_ffff0000b804c33c(s_Start_read______________ffff0000b8097ef0);
                  lVar21 = 0;
                  local_68 = -0xffff46000000;
                  uVar22 = uVar9;
                  do {
                    FUN_ffff0000b804c33c
                              (s_GSORT__read__0x_016llx__0x_016ll_ffff0000b808520c,uVar22,local_68,
                               lVar24,lVar25);
                    lVar15 = 0;
                    do {
                      lVar23 = local_68;
                      lVar16 = 0;
                      iVar26 = 0;
                      do {
                        if (*(long *)(lVar23 + lVar16 * 8) != lVar24) {
                          FUN_ffff0000b804c33c
                                    (s_GSORT_ori_paddr_0x_016llx_write_0_ffff0000b80943c2,
                                     uVar22 + lVar16,lVar24);
                          puVar12 = DAT_ffff0000b811b100;
                          lVar2 = DAT_ffff0000b811b0f8;
                          bVar7 = *(byte *)(DAT_ffff0000b811b0f8 + 0x26);
                          if (bVar7 < 100) {
                            uVar18 = *(undefined8 *)(lVar23 + lVar16 * 8);
                            *(ulong *)(DAT_ffff0000b811b100 + (ulong)bVar7 * 0x24 + 0xc) =
                                 uVar22 + lVar16;
                            *(long *)(puVar12 + (ulong)bVar7 * 0x24 + 0x14) = lVar24;
                            *(undefined8 *)(puVar12 + (ulong)bVar7 * 0x24 + 0x1c) = uVar18;
                            *(byte *)(lVar2 + 0x26) = bVar7 + 1;
                          }
                          FUN_ffff0000b804c33c(s_GSORT_gsort_param_>result_count__ffff0000b8085246);
                          iVar26 = iVar26 + 1;
                        }
                        lVar16 = lVar16 + 1;
                      } while (lVar16 != 0x800);
                      lVar16 = uVar22 + 0x4000;
                      if (iVar26 != 0) {
                        FUN_ffff0000b804c33c
                                  (s__GSORT__Page_compare_err_with_pa_ffff0000b808ddab,uVar22,lVar16
                                   ,iVar26);
                      }
                      lVar23 = 0;
                      iVar26 = 0;
                      lVar2 = local_68 + 0x4000;
                      local_68 = lVar2;
                      do {
                        if (*(long *)(lVar2 + lVar23 * 8) != lVar25) {
                          lVar3 = uVar22 + lVar23 + 0x4000;
                          FUN_ffff0000b804c33c
                                    (s_GSORT_ori_paddr_0x_016llx_write_0_ffff0000b80943c2,lVar3,
                                     lVar25);
                          puVar12 = DAT_ffff0000b811b100;
                          lVar11 = DAT_ffff0000b811b0f8;
                          bVar7 = *(byte *)(DAT_ffff0000b811b0f8 + 0x26);
                          if (bVar7 < 100) {
                            uVar18 = *(undefined8 *)(lVar2 + lVar23 * 8);
                            *(long *)(DAT_ffff0000b811b100 + (ulong)bVar7 * 0x24 + 0xc) = lVar3;
                            *(long *)(puVar12 + (ulong)bVar7 * 0x24 + 0x14) = lVar25;
                            *(undefined8 *)(puVar12 + (ulong)bVar7 * 0x24 + 0x1c) = uVar18;
                            *(byte *)(lVar11 + 0x26) = bVar7 + 1;
                          }
                          FUN_ffff0000b804c33c(s_GSORT_gsort_param_>result_count__ffff0000b8085246);
                          iVar26 = iVar26 + 1;
                        }
                        lVar23 = lVar23 + 1;
                      } while (lVar23 != 0x800);
                      uVar22 = uVar22 + 0x8000;
                      if (iVar26 != 0) {
                        FUN_ffff0000b804c33c
                                  (s__GSORT__Page_compare_err_with_pa_ffff0000b809c4e0,lVar16,uVar22
                                   ,iVar26);
                      }
                      lVar15 = lVar15 + 1;
                      local_68 = local_68 + 0x4000;
                    } while (lVar15 != 0x40);
                    lVar21 = lVar21 + 1;
                  } while (lVar21 != 0x200);
                  local_68 = -0xffff46000000;
                  uVar13 = FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,0xffff0000ba000000);
                  if (uVar13 == 0) {
                    FUN_ffff0000b804c33c(s_Test_finish__ffff0000b809c525);
                    uVar13 = (uint)*(byte *)(DAT_ffff0000b811b0f8 + 0x26);
                  }
                  else {
                    FUN_ffff0000b804c33c(s_vmm_free_region_failed__err___d_ffff0000b808ddf0,uVar13);
                  }
                }
                else {
                  FUN_ffff0000b804c33c(s__GSORT__vm_gsort_vmm_alloc_physi_ffff0000b808cf97,uVar13);
                }
                bVar19 = bVar19 + 1;
                if (0 < (int)uVar13) break;
                if ((uVar13 != 0) || (1 < bVar19)) {
                  if ((int)uVar13 < 0) {
                    FUN_ffff0000b804c33c(s_GSORT__Other_Exception_occur__ffff0000b808621a);
                    return;
                  }
                  FUN_ffff0000b804c33c(s_GSORT__check_pass__ffff0000b809c4cc);
                  goto LAB_ffff0000b8018e6c;
                }
              }
              FUN_ffff0000b804c33c
                        (s__0x_016llx___0x_016llx__0x_016ll_ffff0000b80932fc,uVar9,uVar4,lVar24,
                         lVar25,uVar13);
            } while (bVar19 < 2);
            FUN_ffff0000b804c33c(s_GSORT__Dram_Exception_occur__ffff0000b808a8e9);
            *DAT_ffff0000b811b100 = (char)uVar17;
LAB_ffff0000b8018e6c:
            uVar17 = uVar17 + 1;
          } while (uVar17 < *(byte *)(DAT_ffff0000b811b0f8 + 0x18));
        }
      }
      uVar20 = uVar20 + 1 & 0xff;
    } while (uVar20 < *(uint *)*DAT_ffff0000b81086e0);
  }
  FUN_ffff0000b804c33c
            (s_GSORT__total_error__d_ffff0000b808a907,*(undefined1 *)(DAT_ffff0000b811b0f8 + 0x26));
  return;
}



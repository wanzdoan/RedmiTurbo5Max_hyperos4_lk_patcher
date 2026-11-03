
void FUN_ffff0000be0643a0(void)

{
  long lVar1;
  ulong uVar2;
  uint uVar3;
  uint uVar4;
  int iVar5;
  int iVar6;
  int *piVar7;
  long lVar8;
  long *plVar9;
  ulong uVar10;
  uint uVar11;
  char *pcVar12;
  long lVar13;
  long lVar14;
  ulong uVar15;
  long lVar16;
  long local_48;
  
  iVar5 = FUN_ffff0000be01d35c();
  pcVar12 = s_kedump__last_is_not_full_pmic_re_ffff0000be0bffb4;
  if ((*(uint *)(DAT_ffff0000be1321c0 + (ulong)*(uint *)(DAT_ffff0000be1321c0 + 4)) & 0x800) != 0) {
    pcVar12 = s_kedump__last_is_full_pmic_reset__ffff0000be0ade76;
  }
  FUN_ffff0000be063b40(pcVar12);
  FUN_ffff0000be05fd98(pcVar12);
  if (iVar5 != 2) {
    if (iVar5 != 3) {
      FUN_ffff0000be063b40(s_kedump__normal_flow_at_stage__d_ffff0000be0aac35,iVar5);
      FUN_ffff0000be05fd98(s_kedump__normal_flow_at_stage__d_ffff0000be0aac35,iVar5);
      return;
    }
    FUN_ffff0000be063b40(s_kedump__lk_dump_flow_ffff0000be0b262b);
    FUN_ffff0000be05fd98(s_kedump__lk_dump_flow_ffff0000be0b262b);
    return;
  }
  FUN_ffff0000be063b40(s_kedump__normal_flow_ffff0000be0bc6aa);
  FUN_ffff0000be05fd98(s_kedump__normal_flow_ffff0000be0bc6aa);
  local_48 = 0;
  piVar7 = (int *)FUN_ffff0000be05f1bc(0x2030);
  if (piVar7 == (int *)0x0) goto code_r0xffff0000be0647f4;
  FUN_ffff0000be060ae0(piVar7,0,0x2030);
  lVar8 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar8 == 0) {
    FUN_ffff0000be063b40
              (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
    FUN_ffff0000be05fd98
              (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
    thunk_FUN_ffff0000be09937c(piVar7);
    goto code_r0xffff0000be0647f4;
  }
  FUN_ffff0000be05e000(lVar8,piVar7,0,0x2030);
  if ((*piVar7 == -0x511f2153) && (0x11 < (uint)piVar7[1])) {
    FUN_ffff0000be063b40(s_kedump__found_content_in_expdb_ffff0000be0c2388);
    FUN_ffff0000be05fd98(s_kedump__found_content_in_expdb_ffff0000be0c2388);
    lVar16 = 0;
    do {
      lVar1 = (long)piVar7 + lVar16 + 0x50;
      iVar6 = FUN_ffff0000be060f54(lVar1,s_SYS_RAMCONSOLE_RAW_ffff0000be0b08d8,0x12);
      iVar5 = DAT_ffff0000be1321d8;
      lVar14 = DAT_ffff0000be1321c0;
      if (iVar6 == 0) {
        if ((DAT_ffff0000be1321c0 == 0) || (DAT_ffff0000be1321d8 == 0)) {
          pcVar12 = s_kedump__mboot_params_not_init_ffff0000be0b9221;
        }
        else {
          lVar13 = FUN_ffff0000be05f1bc(DAT_ffff0000be1321d8);
          if (lVar13 != 0) {
            FUN_ffff0000be063b40
                      (s_kedump__read__s_from_offset_0x_x_ffff0000be0bd9ac,lVar1,
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            FUN_ffff0000be05fd98
                      (s_kedump__read__s_from_offset_0x_x_ffff0000be0bd9ac,lVar1,
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            FUN_ffff0000be060ae0(lVar13,0,iVar5);
            FUN_ffff0000be05e000
                      (lVar8,lVar13,*(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            goto LAB_ffff0000be064624;
          }
          pcVar12 = s_kedump__temp_mboot_params_alloc_f_ffff0000be0c23a8;
        }
        FUN_ffff0000be063b40(pcVar12);
        FUN_ffff0000be05fd98(pcVar12);
        goto LAB_ffff0000be064620;
      }
      lVar16 = lVar16 + 0x40;
    } while (lVar16 != 0x2000);
    iVar5 = 0;
    lVar14 = 0;
LAB_ffff0000be064620:
    lVar13 = 0;
LAB_ffff0000be064624:
    FUN_ffff0000be064948();
    if ((lVar13 == 0) || (DAT_ffff0000be1321c8 == '\0')) goto LAB_ffff0000be0647d4;
    uVar11 = *(uint *)(lVar13 + 0x2c);
    if ((uVar11 != 0) &&
       ((uVar11 == (*(int *)(lVar13 + 0x18) + 0x3fU & 0xffffffc0) + *(int *)(lVar13 + 0x14) &&
        (*(int *)(lVar13 + 4) == 0x40)))) {
      uVar4 = ((int *)(lVar13 + (ulong)uVar11))[1];
      uVar3 = uVar4 ^ 0xaeedead0;
      if (0xf < uVar3) {
        uVar3 = uVar4;
      }
      if ((((*(int *)(lVar13 + 0x40) != 0) && (*(int *)(lVar13 + 0x40) != DAT_ffff0000be1321cc)) ||
          ((iVar6 = *(int *)(lVar13 + (ulong)uVar11), iVar6 != 0 && (iVar6 != DAT_ffff0000be1321d0))
          )) || ((uVar3 != 0 && (uVar3 != DAT_ffff0000be1321d4)))) {
        FUN_ffff0000be063b40(s_kedump__mboot_params_should_rest_ffff0000be0b3543);
        FUN_ffff0000be05fd98(s_kedump__mboot_params_should_rest_ffff0000be0b3543);
        FUN_ffff0000be0607b0(lVar14,lVar13,iVar5);
        lVar16 = 0;
        do {
          lVar1 = (long)piVar7 + lVar16 + 0x50;
          iVar5 = FUN_ffff0000be060f54(lVar1,s_SYS_PSTORE_RAW_ffff0000be0c186c,0xe);
          if (iVar5 == 0) {
            plVar9 = (long *)FUN_ffff0000be01cb9c(s_pstore_ffff0000be0b0486,0);
            if (plVar9 == (long *)0x0) {
              FUN_ffff0000be063b40
                        (s__s__fail_to_get_dram_addr_of_pst_ffff0000be0c11fc,
                         s__MBOOT_PARAMS__ffff0000be0b7f52);
              FUN_ffff0000be05fd98
                        (s__s__fail_to_get_dram_addr_of_pst_ffff0000be0c11fc,
                         s__MBOOT_PARAMS__ffff0000be0b7f52);
LAB_ffff0000be0647ac:
              pcVar12 = s_kedump__failed_to_get_pstore_mem_ffff0000be0bb55a;
            }
            else {
              if ((*plVar9 == 0) || (uVar15 = plVar9[1], (int)uVar15 == 0))
              goto LAB_ffff0000be0647ac;
              iVar5 = FUN_ffff0000be05d9a8
                                (PTR_DAT_ffff0000be0f5570,s_pstore_raw_ffff0000be0ba4ec,
                                 uVar15 & 0xffffffff,&local_48,0xc,*plVar9,0,0);
              if (iVar5 == 0) {
                FUN_ffff0000be063b40
                          (s_kedump__read__s_from_offset_0x_x_ffff0000be0bd9ac,lVar1,
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
                FUN_ffff0000be05fd98
                          (s_kedump__read__s_from_offset_0x_x_ffff0000be0bd9ac,lVar1,
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
                uVar11 = *(uint *)((long)piVar7 + lVar16 + 0x3c);
                uVar10 = (ulong)uVar11;
                if (uVar11 == 0) goto LAB_ffff0000be06492c;
                uVar11 = 0;
                goto LAB_ffff0000be0648a8;
              }
              pcVar12 = s_kedump__map_error_for_pstore_ffff0000be0acf4d;
            }
            FUN_ffff0000be063b40(pcVar12);
            FUN_ffff0000be05fd98(pcVar12);
            break;
          }
          lVar16 = lVar16 + 0x40;
        } while (lVar16 != 0x2000);
        goto joined_r0xffff0000be064940;
      }
    }
    goto LAB_ffff0000be0647d8;
  }
  goto LAB_ffff0000be0647e0;
  while( true ) {
    FUN_ffff0000be05e000
              (lVar8,local_48 + (ulong)uVar11,uVar11 + *(int *)((long)piVar7 + lVar16 + 0x38),0x1000
              );
    uVar10 = (ulong)*(uint *)((long)piVar7 + lVar16 + 0x3c);
    uVar11 = uVar11 + 0x1000;
    if (uVar10 <= (uVar2 & 0xffffffff)) break;
LAB_ffff0000be0648a8:
    uVar2 = (ulong)uVar11 + 0x1000;
    if ((uVar15 & 0xffffffff) < uVar2) {
      FUN_ffff0000be063b40(s_kedump__pstore_reserved_mem_size_ffff0000be0a996c,uVar15 & 0xffffffff);
      FUN_ffff0000be05fd98(s_kedump__pstore_reserved_mem_size_ffff0000be0a996c,uVar15 & 0xffffffff);
      break;
    }
    if (uVar10 < uVar2) {
      FUN_ffff0000be063b40(s_kedump__pstore_not_page_align_ffff0000be0becf2);
      FUN_ffff0000be05fd98(s_kedump__pstore_not_page_align_ffff0000be0becf2);
      break;
    }
  }
LAB_ffff0000be06492c:
  FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,local_48);
joined_r0xffff0000be064940:
  if (DAT_ffff0000be1321c0 != 0) {
    *(undefined4 *)(DAT_ffff0000be1321c0 + 0x24) = 0;
  }
  FUN_ffff0000be064948();
LAB_ffff0000be0647d4:
  if (lVar13 != 0) {
LAB_ffff0000be0647d8:
    thunk_FUN_ffff0000be09937c(lVar13);
  }
LAB_ffff0000be0647e0:
  thunk_FUN_ffff0000be09937c(piVar7);
  FUN_ffff0000be05def0(lVar8);
code_r0xffff0000be0647f4:
  FUN_ffff0000be060ae0(&DAT_ffff0000be1321e8,0,0x100);
  iVar5 = FUN_ffff0000be0611dc
                    (&DAT_ffff0000be1321e8,0x100,s_ramoops_mem_address_0x_x_ramoops_ffff0000be0abdb9
                     ,*(undefined4 *)(DAT_ffff0000be1321e0 + 0x24),
                     *(undefined4 *)(DAT_ffff0000be1321e0 + 0x28),
                     *(undefined4 *)(DAT_ffff0000be1321e0 + 0x30),
                     *(undefined4 *)(DAT_ffff0000be1321e0 + 0x2c));
  if (iVar5 < 1) {
    FUN_ffff0000be063b40(s_kedump__failed_to_set_pstore_ffff0000be0b9203);
    FUN_ffff0000be05fd98(s_kedump__failed_to_set_pstore_ffff0000be0b9203);
  }
  return;
}



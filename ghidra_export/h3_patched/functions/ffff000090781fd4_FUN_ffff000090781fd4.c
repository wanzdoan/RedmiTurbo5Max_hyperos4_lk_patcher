// ffff000090781fd4  size=1448  FUN_ffff000090781fd4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090781fd4(void)

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
  
  iVar5 = FUN_ffff00009071c55c();
  pcVar12 = s_kedump__last_is_not_full_pmic_re_ffff0000907f8c09;
  if ((*(uint *)(_DAT_ffff000090887058 + (ulong)*(uint *)(_DAT_ffff000090887058 + 4)) & 0x800) != 0)
  {
    pcVar12 = s_kedump__last_is_full_pmic_reset__ffff0000907debb5;
  }
  FUN_ffff000090781820(pcVar12);
  FUN_ffff00009077de54(pcVar12);
  if (iVar5 != 2) {
    if (iVar5 != 3) {
      FUN_ffff000090781820(s_kedump__normal_flow_at_stage__d_ffff0000907d9cf4,iVar5);
      FUN_ffff00009077de54(s_kedump__normal_flow_at_stage__d_ffff0000907d9cf4,iVar5);
      return;
    }
    FUN_ffff000090781820(s_kedump__lk_dump_flow_ffff0000907e53a4);
    FUN_ffff00009077de54(s_kedump__lk_dump_flow_ffff0000907e53a4);
    return;
  }
  FUN_ffff000090781820(s_kedump__normal_flow_ffff0000907f358f);
  FUN_ffff00009077de54(s_kedump__normal_flow_ffff0000907f358f);
  local_48 = 0;
  piVar7 = (int *)FUN_ffff00009077d24c(0x2030);
  if (piVar7 == (int *)0x0) goto LAB_ffff000090782424;
  FUN_ffff00009077eb88(piVar7,0,0x2030);
  lVar8 = FUN_ffff00009077bd60(s_expdb_ffff0000907d5730);
  if (lVar8 == 0) {
    FUN_ffff000090781820
              (s_kedump__Partition___s__is_not_ex_ffff0000907d3e02,s_expdb_ffff0000907d5730);
    FUN_ffff00009077de54
              (s_kedump__Partition___s__is_not_ex_ffff0000907d3e02,s_expdb_ffff0000907d5730);
    thunk_FUN_ffff0000907c2ad8(piVar7);
    goto LAB_ffff000090782424;
  }
  FUN_ffff00009077bdf4(lVar8,piVar7,0,0x2030);
  if ((*piVar7 == -0x511f2153) && (0x11 < (uint)piVar7[1])) {
    FUN_ffff000090781820(s_kedump__found_content_in_expdb_ffff0000907fc2e5);
    FUN_ffff00009077de54(s_kedump__found_content_in_expdb_ffff0000907fc2e5);
    lVar16 = 0;
    do {
      lVar1 = (long)piVar7 + lVar16 + 0x50;
      iVar6 = FUN_ffff00009077f080(lVar1,s_SYS_RAMCONSOLE_RAW_ffff0000907e3916,0x12);
      iVar5 = _DAT_ffff000090887070;
      lVar14 = _DAT_ffff000090887058;
      if (iVar6 == 0) {
        if ((_DAT_ffff000090887058 == 0) || (_DAT_ffff000090887070 == 0)) {
          pcVar12 = s_kedump__mboot_params_not_init_ffff0000907eebc4;
        }
        else {
          lVar13 = FUN_ffff00009077d24c(_DAT_ffff000090887070);
          if (lVar13 != 0) {
            FUN_ffff000090781820
                      (s_kedump__read__s_from_offset_0x_x_ffff0000907f5380,lVar1,
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            FUN_ffff00009077de54
                      (s_kedump__read__s_from_offset_0x_x_ffff0000907f5380,lVar1,
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            FUN_ffff00009077eb88(lVar13,0,iVar5);
            FUN_ffff00009077bdf4
                      (lVar8,lVar13,*(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            goto LAB_ffff000090782258;
          }
          pcVar12 = s_kedump__temp_mboot_params_alloc_f_ffff0000907fc305;
        }
        FUN_ffff000090781820(pcVar12);
        FUN_ffff00009077de54(pcVar12);
        goto LAB_ffff000090782254;
      }
      lVar16 = lVar16 + 0x40;
    } while (lVar16 != 0x2000);
    iVar5 = 0;
    lVar14 = 0;
LAB_ffff000090782254:
    lVar13 = 0;
LAB_ffff000090782258:
    FUN_ffff00009078257c();
    if ((lVar13 == 0) || (DAT_ffff000090887060 == '\0')) goto LAB_ffff000090782408;
    uVar11 = *(uint *)(lVar13 + 0x2c);
    if ((uVar11 != 0) &&
       ((uVar11 == (*(int *)(lVar13 + 0x18) + 0x3fU & 0xffffffc0) + *(int *)(lVar13 + 0x14) &&
        (*(int *)(lVar13 + 4) == 0x40)))) {
      uVar4 = ((int *)(lVar13 + (ulong)uVar11))[1];
      uVar3 = uVar4 ^ 0xaeedead0;
      if (0xf < uVar3) {
        uVar3 = uVar4;
      }
      if ((((*(int *)(lVar13 + 0x40) != 0) && (*(int *)(lVar13 + 0x40) != _DAT_ffff000090887064)) ||
          ((iVar6 = *(int *)(lVar13 + (ulong)uVar11), iVar6 != 0 && (iVar6 != _DAT_ffff000090887068)
           ))) || ((uVar3 != 0 && (uVar3 != _DAT_ffff00009088706c)))) {
        FUN_ffff000090781820(s_kedump__mboot_params_should_rest_ffff0000907e6ada);
        FUN_ffff00009077de54(s_kedump__mboot_params_should_rest_ffff0000907e6ada);
        FUN_ffff00009077e858(lVar14,lVar13,iVar5);
        lVar16 = 0;
        do {
          lVar1 = (long)piVar7 + lVar16 + 0x50;
          iVar5 = FUN_ffff00009077f080(lVar1,s_SYS_PSTORE_RAW_ffff0000907fc32b,0xe);
          if (iVar5 == 0) {
            plVar9 = (long *)FUN_ffff00009071a204(s_pstore_ffff0000907e1e44,0);
            if (plVar9 == (long *)0x0) {
              FUN_ffff000090781820
                        (s__s__fail_to_get_dram_addr_of_pst_ffff0000907fa6e2,
                         s__MBOOT_PARAMS__ffff0000907ed058);
              FUN_ffff00009077de54
                        (s__s__fail_to_get_dram_addr_of_pst_ffff0000907fa6e2,
                         s__MBOOT_PARAMS__ffff0000907ed058);
LAB_ffff0000907823e0:
              pcVar12 = s_kedump__failed_to_get_pstore_mem_ffff0000907f1ce1;
            }
            else {
              if ((*plVar9 == 0) || (uVar15 = plVar9[1], (int)uVar15 == 0))
              goto LAB_ffff0000907823e0;
              iVar5 = FUN_ffff00009077b79c
                                (DAT_ffff000090839908,s_pstore_raw_ffff0000907f044b,
                                 uVar15 & 0xffffffff,&local_48,0xc,*plVar9,0,0);
              if (iVar5 == 0) {
                FUN_ffff000090781820
                          (s_kedump__read__s_from_offset_0x_x_ffff0000907f5380,lVar1,
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
                FUN_ffff00009077de54
                          (s_kedump__read__s_from_offset_0x_x_ffff0000907f5380,lVar1,
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
                uVar11 = *(uint *)((long)piVar7 + lVar16 + 0x3c);
                uVar10 = (ulong)uVar11;
                if (uVar11 == 0) goto LAB_ffff000090782560;
                uVar11 = 0;
                goto LAB_ffff0000907824dc;
              }
              pcVar12 = s_kedump__map_error_for_pstore_ffff0000907dd242;
            }
            FUN_ffff000090781820(pcVar12);
            FUN_ffff00009077de54(pcVar12);
            break;
          }
          lVar16 = lVar16 + 0x40;
        } while (lVar16 != 0x2000);
        goto joined_r0xffff000090782574;
      }
    }
    goto LAB_ffff00009078240c;
  }
  goto LAB_ffff000090782414;
  while( true ) {
    FUN_ffff00009077bdf4
              (lVar8,local_48 + (ulong)uVar11,uVar11 + *(int *)((long)piVar7 + lVar16 + 0x38),0x1000
              );
    uVar10 = (ulong)*(uint *)((long)piVar7 + lVar16 + 0x3c);
    uVar11 = uVar11 + 0x1000;
    if (uVar10 <= (uVar2 & 0xffffffff)) break;
LAB_ffff0000907824dc:
    uVar2 = (ulong)uVar11 + 0x1000;
    if ((uVar15 & 0xffffffff) < uVar2) {
      FUN_ffff000090781820(s_kedump__pstore_reserved_mem_size_ffff0000907d8522,uVar15 & 0xffffffff);
      FUN_ffff00009077de54(s_kedump__pstore_reserved_mem_size_ffff0000907d8522,uVar15 & 0xffffffff);
      break;
    }
    if (uVar10 < uVar2) {
      FUN_ffff000090781820(s_kedump__pstore_not_page_align_ffff0000907f7105);
      FUN_ffff00009077de54(s_kedump__pstore_not_page_align_ffff0000907f7105);
      break;
    }
  }
LAB_ffff000090782560:
  FUN_ffff00009077bb68(DAT_ffff000090839908,local_48);
joined_r0xffff000090782574:
  if (_DAT_ffff000090887058 != 0) {
    *(undefined4 *)(_DAT_ffff000090887058 + 0x24) = 0;
  }
  FUN_ffff00009078257c();
LAB_ffff000090782408:
  if (lVar13 != 0) {
LAB_ffff00009078240c:
    thunk_FUN_ffff0000907c2ad8(lVar13);
  }
LAB_ffff000090782414:
  thunk_FUN_ffff0000907c2ad8(piVar7);
  FUN_ffff00009077bce4(lVar8);
LAB_ffff000090782424:
  FUN_ffff00009077eb88(0xffff000090887080,0,0x100);
  iVar5 = FUN_ffff00009077f434
                    (0xffff000090887080,0x100,s_ramoops_mem_address_0x_x_ramoops_ffff0000907db743,
                     *(undefined4 *)(_DAT_ffff000090887078 + 0x24),
                     *(undefined4 *)(_DAT_ffff000090887078 + 0x28),
                     *(undefined4 *)(_DAT_ffff000090887078 + 0x30),
                     *(undefined4 *)(_DAT_ffff000090887078 + 0x2c));
  if (iVar5 < 1) {
    FUN_ffff000090781820(s_kedump__failed_to_set_pstore_ffff0000907eeba6);
    FUN_ffff00009077de54(s_kedump__failed_to_set_pstore_ffff0000907eeba6);
  }
  return;
}



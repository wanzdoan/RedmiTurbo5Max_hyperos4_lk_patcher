
void FUN_ffff00009078229c(void)

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
  
  iVar5 = FUN_ffff00009071c6e8();
  pcVar12 = s_kedump__last_is_not_full_pmic_re_ffff0000907f8da5;
  if ((*(uint *)(DAT_ffff0000908870f0 + (ulong)*(uint *)(DAT_ffff0000908870f0 + 4)) & 0x800) != 0) {
    pcVar12 = s_kedump__last_is_full_pmic_reset__ffff0000907dec6f;
  }
  FUN_ffff000090781ae8(pcVar12);
  FUN_ffff00009077e11c(pcVar12);
  if (iVar5 != 2) {
    if (iVar5 != 3) {
      FUN_ffff000090781ae8(s_kedump__normal_flow_at_stage__d_ffff0000907d9da2,iVar5);
      FUN_ffff00009077e11c(s_kedump__normal_flow_at_stage__d_ffff0000907d9da2,iVar5);
      return;
    }
    FUN_ffff000090781ae8(s_kedump__lk_dump_flow_ffff0000907e54bf);
    FUN_ffff00009077e11c(s_kedump__lk_dump_flow_ffff0000907e54bf);
    return;
  }
  FUN_ffff000090781ae8(s_kedump__normal_flow_ffff0000907f3732);
  FUN_ffff00009077e11c(s_kedump__normal_flow_ffff0000907f3732);
  local_48 = 0;
  piVar7 = (int *)FUN_ffff00009077d514(0x2030);
  if (piVar7 == (int *)0x0) goto LAB_ffff0000907826ec;
  FUN_ffff00009077ee50(piVar7,0,0x2030);
  lVar8 = FUN_ffff00009077c028(s_expdb_ffff0000907d5780);
  if (lVar8 == 0) {
    FUN_ffff000090781ae8
              (s_kedump__Partition___s__is_not_ex_ffff0000907d3e3f,s_expdb_ffff0000907d5780);
    FUN_ffff00009077e11c
              (s_kedump__Partition___s__is_not_ex_ffff0000907d3e3f,s_expdb_ffff0000907d5780);
    thunk_FUN_ffff0000907c314c(piVar7);
    goto LAB_ffff0000907826ec;
  }
  FUN_ffff00009077c0bc(lVar8,piVar7,0,0x2030);
  if ((*piVar7 == -0x511f2153) && (0x11 < (uint)piVar7[1])) {
    FUN_ffff000090781ae8(s_kedump__found_content_in_expdb_ffff0000907fc481);
    FUN_ffff00009077e11c(s_kedump__found_content_in_expdb_ffff0000907fc481);
    lVar16 = 0;
    do {
      lVar1 = (long)piVar7 + lVar16 + 0x50;
      iVar6 = FUN_ffff00009077f348(lVar1,s_SYS_RAMCONSOLE_RAW_ffff0000907e39fe,0x12);
      iVar5 = DAT_ffff000090887108;
      lVar14 = DAT_ffff0000908870f0;
      if (iVar6 == 0) {
        if ((DAT_ffff0000908870f0 == 0) || (DAT_ffff000090887108 == 0)) {
          pcVar12 = s_kedump__mboot_params_not_init_ffff0000907eed4c;
        }
        else {
          lVar13 = FUN_ffff00009077d514(DAT_ffff000090887108);
          if (lVar13 != 0) {
            FUN_ffff000090781ae8
                      (s_kedump__read__s_from_offset_0x_x_ffff0000907f5504,lVar1,
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            FUN_ffff00009077e11c
                      (s_kedump__read__s_from_offset_0x_x_ffff0000907f5504,lVar1,
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            FUN_ffff00009077ee50(lVar13,0,iVar5);
            FUN_ffff00009077c0bc
                      (lVar8,lVar13,*(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            goto LAB_ffff000090782520;
          }
          pcVar12 = s_kedump__temp_mboot_params_alloc_f_ffff0000907fc4a1;
        }
        FUN_ffff000090781ae8(pcVar12);
        FUN_ffff00009077e11c(pcVar12);
        goto LAB_ffff00009078251c;
      }
      lVar16 = lVar16 + 0x40;
    } while (lVar16 != 0x2000);
    iVar5 = 0;
    lVar14 = 0;
LAB_ffff00009078251c:
    lVar13 = 0;
LAB_ffff000090782520:
    FUN_ffff000090782844();
    if ((lVar13 == 0) || (DAT_ffff0000908870f8 == '\0')) goto LAB_ffff0000907826d0;
    uVar11 = *(uint *)(lVar13 + 0x2c);
    if ((uVar11 != 0) &&
       ((uVar11 == (*(int *)(lVar13 + 0x18) + 0x3fU & 0xffffffc0) + *(int *)(lVar13 + 0x14) &&
        (*(int *)(lVar13 + 4) == 0x40)))) {
      uVar4 = ((int *)(lVar13 + (ulong)uVar11))[1];
      uVar3 = uVar4 ^ 0xaeedead0;
      if (0xf < uVar3) {
        uVar3 = uVar4;
      }
      if ((((*(int *)(lVar13 + 0x40) != 0) && (*(int *)(lVar13 + 0x40) != DAT_ffff0000908870fc)) ||
          ((iVar6 = *(int *)(lVar13 + (ulong)uVar11), iVar6 != 0 && (iVar6 != DAT_ffff000090887100))
          )) || ((uVar3 != 0 && (uVar3 != DAT_ffff000090887104)))) {
        FUN_ffff000090781ae8(s_kedump__mboot_params_should_rest_ffff0000907e6c05);
        FUN_ffff00009077e11c(s_kedump__mboot_params_should_rest_ffff0000907e6c05);
        FUN_ffff00009077eb20(lVar14,lVar13,iVar5);
        lVar16 = 0;
        do {
          lVar1 = (long)piVar7 + lVar16 + 0x50;
          iVar5 = FUN_ffff00009077f348(lVar1,s_SYS_PSTORE_RAW_ffff0000907fc4c7,0xe);
          if (iVar5 == 0) {
            plVar9 = (long *)FUN_ffff00009071a348(s_pstore_ffff0000907e1f2c,0);
            if (plVar9 == (long *)0x0) {
              FUN_ffff000090781ae8
                        (s__s__fail_to_get_dram_addr_of_pst_ffff0000907fa87e,
                         s__MBOOT_PARAMS__ffff0000907ed1f2);
              FUN_ffff00009077e11c
                        (s__s__fail_to_get_dram_addr_of_pst_ffff0000907fa87e,
                         s__MBOOT_PARAMS__ffff0000907ed1f2);
LAB_ffff0000907826a8:
              pcVar12 = s_kedump__failed_to_get_pstore_mem_ffff0000907f1e84;
            }
            else {
              if ((*plVar9 == 0) || (uVar15 = plVar9[1], (int)uVar15 == 0))
              goto LAB_ffff0000907826a8;
              iVar5 = FUN_ffff00009077ba64
                                (PTR_DAT_ffff000090839928,s_pstore_raw_ffff0000907f05d3,
                                 uVar15 & 0xffffffff,&local_48,0xc,*plVar9,0,0);
              if (iVar5 == 0) {
                FUN_ffff000090781ae8
                          (s_kedump__read__s_from_offset_0x_x_ffff0000907f5504,lVar1,
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
                FUN_ffff00009077e11c
                          (s_kedump__read__s_from_offset_0x_x_ffff0000907f5504,lVar1,
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
                uVar11 = *(uint *)((long)piVar7 + lVar16 + 0x3c);
                uVar10 = (ulong)uVar11;
                if (uVar11 == 0) goto LAB_ffff000090782828;
                uVar11 = 0;
                goto LAB_ffff0000907827a4;
              }
              pcVar12 = s_kedump__map_error_for_pstore_ffff0000907dd32f;
            }
            FUN_ffff000090781ae8(pcVar12);
            FUN_ffff00009077e11c(pcVar12);
            break;
          }
          lVar16 = lVar16 + 0x40;
        } while (lVar16 != 0x2000);
        goto joined_r0xffff00009078283c;
      }
    }
    goto LAB_ffff0000907826d4;
  }
  goto LAB_ffff0000907826dc;
  while( true ) {
    FUN_ffff00009077c0bc
              (lVar8,local_48 + (ulong)uVar11,uVar11 + *(int *)((long)piVar7 + lVar16 + 0x38),0x1000
              );
    uVar10 = (ulong)*(uint *)((long)piVar7 + lVar16 + 0x3c);
    uVar11 = uVar11 + 0x1000;
    if (uVar10 <= (uVar2 & 0xffffffff)) break;
LAB_ffff0000907827a4:
    uVar2 = (ulong)uVar11 + 0x1000;
    if ((uVar15 & 0xffffffff) < uVar2) {
      FUN_ffff000090781ae8(s_kedump__pstore_reserved_mem_size_ffff0000907d85b8,uVar15 & 0xffffffff);
      FUN_ffff00009077e11c(s_kedump__pstore_reserved_mem_size_ffff0000907d85b8,uVar15 & 0xffffffff);
      break;
    }
    if (uVar10 < uVar2) {
      FUN_ffff000090781ae8(s_kedump__pstore_not_page_align_ffff0000907f728f);
      FUN_ffff00009077e11c(s_kedump__pstore_not_page_align_ffff0000907f728f);
      break;
    }
  }
LAB_ffff000090782828:
  FUN_ffff00009077be30(PTR_DAT_ffff000090839928,local_48);
joined_r0xffff00009078283c:
  if (DAT_ffff0000908870f0 != 0) {
    *(undefined4 *)(DAT_ffff0000908870f0 + 0x24) = 0;
  }
  FUN_ffff000090782844();
LAB_ffff0000907826d0:
  if (lVar13 != 0) {
LAB_ffff0000907826d4:
    thunk_FUN_ffff0000907c314c(lVar13);
  }
LAB_ffff0000907826dc:
  thunk_FUN_ffff0000907c314c(piVar7);
  FUN_ffff00009077bfac(lVar8);
LAB_ffff0000907826ec:
  FUN_ffff00009077ee50(&DAT_ffff000090887118,0,0x100);
  iVar5 = FUN_ffff00009077f6fc
                    (&DAT_ffff000090887118,0x100,s_ramoops_mem_address_0x_x_ramoops_ffff0000907db865
                     ,*(undefined4 *)(DAT_ffff000090887110 + 0x24),
                     *(undefined4 *)(DAT_ffff000090887110 + 0x28),
                     *(undefined4 *)(DAT_ffff000090887110 + 0x30),
                     *(undefined4 *)(DAT_ffff000090887110 + 0x2c));
  if (iVar5 < 1) {
    FUN_ffff000090781ae8(s_kedump__failed_to_set_pstore_ffff0000907eed2e);
    FUN_ffff00009077e11c(s_kedump__failed_to_set_pstore_ffff0000907eed2e);
  }
  return;
}




void FUN_ffff0000b804f9f0(void)

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
  
  iVar5 = FUN_ffff0000b800e378();
  pcVar12 = s_kedump__last_is_not_full_pmic_re_ffff0000b809be71;
  if ((*(uint *)(DAT_ffff0000b8133c60 + (ulong)*(uint *)(DAT_ffff0000b8133c60 + 4)) & 0x800) != 0) {
    pcVar12 = s_kedump__last_is_full_pmic_reset__ffff0000b808d6d5;
  }
  FUN_ffff0000b804f4a8(pcVar12);
  FUN_ffff0000b804c33c(pcVar12);
  if (iVar5 != 2) {
    if (iVar5 != 3) {
      FUN_ffff0000b804f4a8(s_kedump__normal_flow_at_stage__d_ffff0000b808affc,iVar5);
      FUN_ffff0000b804c33c(s_kedump__normal_flow_at_stage__d_ffff0000b808affc,iVar5);
      return;
    }
    FUN_ffff0000b804f4a8(s_kedump__lk_dump_flow_ffff0000b80911cf);
    FUN_ffff0000b804c33c(s_kedump__lk_dump_flow_ffff0000b80911cf);
    return;
  }
  FUN_ffff0000b804f4a8(s_kedump__normal_flow_ffff0000b80993c9);
  FUN_ffff0000b804c33c(s_kedump__normal_flow_ffff0000b80993c9);
  local_48 = 0;
  piVar7 = (int *)FUN_ffff0000b804bccc(0x2030);
  if (piVar7 == (int *)0x0) goto code_r0xffff0000b804fe44;
  FUN_ffff0000b804d070(piVar7,0,0x2030);
  lVar8 = FUN_ffff0000b804a99c(s_expdb_ffff0000b8088de7);
  if (lVar8 == 0) {
    FUN_ffff0000b804f4a8
              (s_kedump__Partition___s__is_not_ex_ffff0000b80887a3,s_expdb_ffff0000b8088de7);
    FUN_ffff0000b804c33c
              (s_kedump__Partition___s__is_not_ex_ffff0000b80887a3,s_expdb_ffff0000b8088de7);
    thunk_FUN_ffff0000b8082558(piVar7);
    goto code_r0xffff0000b804fe44;
  }
  FUN_ffff0000b804aa30(lVar8,piVar7,0,0x2030);
  if ((*piVar7 == -0x511f2153) && (0x11 < (uint)piVar7[1])) {
    FUN_ffff0000b804f4a8(s_kedump__found_content_in_expdb_ffff0000b809dd9b);
    FUN_ffff0000b804c33c(s_kedump__found_content_in_expdb_ffff0000b809dd9b);
    lVar16 = 0;
    do {
      lVar1 = (long)piVar7 + lVar16 + 0x50;
      iVar6 = FUN_ffff0000b804d4c4(lVar1,s_SYS_RAMCONSOLE_RAW_ffff0000b8090301,0x12);
      iVar5 = DAT_ffff0000b8133c78;
      lVar14 = DAT_ffff0000b8133c60;
      if (iVar6 == 0) {
        if ((DAT_ffff0000b8133c60 == 0) || (DAT_ffff0000b8133c78 == 0)) {
          pcVar12 = s_kedump__mboot_params_not_init_ffff0000b8096a1f;
        }
        else {
          lVar13 = FUN_ffff0000b804bccc(DAT_ffff0000b8133c78);
          if (lVar13 != 0) {
            FUN_ffff0000b804f4a8
                      (s_kedump__read__s_from_offset_0x_x_ffff0000b809a223,lVar1,
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            FUN_ffff0000b804c33c
                      (s_kedump__read__s_from_offset_0x_x_ffff0000b809a223,lVar1,
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            FUN_ffff0000b804d070(lVar13,0,iVar5);
            FUN_ffff0000b804aa30
                      (lVar8,lVar13,*(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                       *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
            goto LAB_ffff0000b804fc74;
          }
          pcVar12 = s_kedump__temp_mboot_params_alloc_f_ffff0000b809ddbb;
        }
        FUN_ffff0000b804f4a8(pcVar12);
        FUN_ffff0000b804c33c(pcVar12);
        goto LAB_ffff0000b804fc70;
      }
      lVar16 = lVar16 + 0x40;
    } while (lVar16 != 0x2000);
    iVar5 = 0;
    lVar14 = 0;
LAB_ffff0000b804fc70:
    lVar13 = 0;
LAB_ffff0000b804fc74:
    FUN_ffff0000b804ff98();
    if ((lVar13 == 0) || (DAT_ffff0000b8133c68 == '\0')) goto LAB_ffff0000b804fe24;
    uVar11 = *(uint *)(lVar13 + 0x2c);
    if ((uVar11 != 0) &&
       ((uVar11 == (*(int *)(lVar13 + 0x18) + 0x3fU & 0xffffffc0) + *(int *)(lVar13 + 0x14) &&
        (*(int *)(lVar13 + 4) == 0x40)))) {
      uVar4 = ((int *)(lVar13 + (ulong)uVar11))[1];
      uVar3 = uVar4 ^ 0xaeedead0;
      if (0xf < uVar3) {
        uVar3 = uVar4;
      }
      if ((((*(int *)(lVar13 + 0x40) != 0) && (*(int *)(lVar13 + 0x40) != DAT_ffff0000b8133c6c)) ||
          ((iVar6 = *(int *)(lVar13 + (ulong)uVar11), iVar6 != 0 && (iVar6 != DAT_ffff0000b8133c70))
          )) || ((uVar3 != 0 && (uVar3 != DAT_ffff0000b8133c74)))) {
        FUN_ffff0000b804f4a8(s_kedump__mboot_params_should_rest_ffff0000b8091daa);
        FUN_ffff0000b804c33c(s_kedump__mboot_params_should_rest_ffff0000b8091daa);
        FUN_ffff0000b804cd40(lVar14,lVar13,iVar5);
        lVar16 = 0;
        do {
          lVar1 = (long)piVar7 + lVar16 + 0x50;
          iVar5 = FUN_ffff0000b804d4c4(lVar1,s_SYS_PSTORE_RAW_ffff0000b809dde1,0xe);
          if (iVar5 == 0) {
            plVar9 = (long *)FUN_ffff0000b800db20(s_pstore_ffff0000b808f553,0);
            if (plVar9 == (long *)0x0) {
              FUN_ffff0000b804f4a8
                        (s__s__fail_to_get_dram_addr_of_pst_ffff0000b809cedb,
                         s__MBOOT_PARAMS__ffff0000b8095af6);
              FUN_ffff0000b804c33c
                        (s__s__fail_to_get_dram_addr_of_pst_ffff0000b809cedb,
                         s__MBOOT_PARAMS__ffff0000b8095af6);
LAB_ffff0000b804fdfc:
              pcVar12 = s_kedump__failed_to_get_pstore_mem_ffff0000b80985c0;
            }
            else {
              if ((*plVar9 == 0) || (uVar15 = plVar9[1], (int)uVar15 == 0))
              goto LAB_ffff0000b804fdfc;
              iVar5 = FUN_ffff0000b804a5f8
                                (PTR_DAT_ffff0000b81086d8,s_pstore_raw_ffff0000b8097827,
                                 uVar15 & 0xffffffff,&local_48,0xc,*plVar9,0,0);
              if (iVar5 == 0) {
                FUN_ffff0000b804f4a8
                          (s_kedump__read__s_from_offset_0x_x_ffff0000b809a223,lVar1,
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
                FUN_ffff0000b804c33c
                          (s_kedump__read__s_from_offset_0x_x_ffff0000b809a223,lVar1,
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x38),
                           *(undefined4 *)((long)piVar7 + lVar16 + 0x3c));
                uVar11 = *(uint *)((long)piVar7 + lVar16 + 0x3c);
                uVar10 = (ulong)uVar11;
                if (uVar11 == 0) goto LAB_ffff0000b804ff7c;
                uVar11 = 0;
                goto LAB_ffff0000b804fef8;
              }
              pcVar12 = s_kedump__map_error_for_pstore_ffff0000b808cb76;
            }
            FUN_ffff0000b804f4a8(pcVar12);
            FUN_ffff0000b804c33c(pcVar12);
            break;
          }
          lVar16 = lVar16 + 0x40;
        } while (lVar16 != 0x2000);
        goto joined_r0xffff0000b804ff90;
      }
    }
    goto LAB_ffff0000b804fe28;
  }
  goto LAB_ffff0000b804fe30;
  while( true ) {
    FUN_ffff0000b804aa30
              (lVar8,local_48 + (ulong)uVar11,uVar11 + *(int *)((long)piVar7 + lVar16 + 0x38),0x1000
              );
    uVar10 = (ulong)*(uint *)((long)piVar7 + lVar16 + 0x3c);
    uVar11 = uVar11 + 0x1000;
    if (uVar10 <= (uVar2 & 0xffffffff)) break;
LAB_ffff0000b804fef8:
    uVar2 = (ulong)uVar11 + 0x1000;
    if ((uVar15 & 0xffffffff) < uVar2) {
      FUN_ffff0000b804f4a8(s_kedump__pstore_reserved_mem_size_ffff0000b808a29d,uVar15 & 0xffffffff);
      FUN_ffff0000b804c33c(s_kedump__pstore_reserved_mem_size_ffff0000b808a29d,uVar15 & 0xffffffff);
      break;
    }
    if (uVar10 < uVar2) {
      FUN_ffff0000b804f4a8(s_kedump__pstore_not_page_align_ffff0000b809b067);
      FUN_ffff0000b804c33c(s_kedump__pstore_not_page_align_ffff0000b809b067);
      break;
    }
  }
LAB_ffff0000b804ff7c:
  FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_48);
joined_r0xffff0000b804ff90:
  if (DAT_ffff0000b8133c60 != 0) {
    *(undefined4 *)(DAT_ffff0000b8133c60 + 0x24) = 0;
  }
  FUN_ffff0000b804ff98();
LAB_ffff0000b804fe24:
  if (lVar13 != 0) {
LAB_ffff0000b804fe28:
    thunk_FUN_ffff0000b8082558(lVar13);
  }
LAB_ffff0000b804fe30:
  thunk_FUN_ffff0000b8082558(piVar7);
  FUN_ffff0000b804a920(lVar8);
code_r0xffff0000b804fe44:
  FUN_ffff0000b804d070(&DAT_ffff0000b8133c88,0,0x100);
  iVar5 = FUN_ffff0000b804d74c
                    (&DAT_ffff0000b8133c88,0x100,s_ramoops_mem_address_0x_x_ramoops_ffff0000b808bdcb
                     ,*(undefined4 *)(DAT_ffff0000b8133c80 + 0x24),
                     *(undefined4 *)(DAT_ffff0000b8133c80 + 0x28),
                     *(undefined4 *)(DAT_ffff0000b8133c80 + 0x30),
                     *(undefined4 *)(DAT_ffff0000b8133c80 + 0x2c));
  if (iVar5 < 1) {
    FUN_ffff0000b804f4a8(s_kedump__failed_to_set_pstore_ffff0000b8096a01);
    FUN_ffff0000b804c33c(s_kedump__failed_to_set_pstore_ffff0000b8096a01);
  }
  return;
}



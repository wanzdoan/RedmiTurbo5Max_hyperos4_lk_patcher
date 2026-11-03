// ffff00009073ec68  size=2336  FUN_ffff00009073ec68

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073ec68(code *param_1)

{
  undefined4 *puVar1;
  uint uVar2;
  undefined **ppuVar3;
  undefined **ppuVar4;
  undefined **ppuVar5;
  uint uVar6;
  int iVar7;
  bool bVar8;
  uint uVar9;
  int iVar10;
  int iVar11;
  int iVar12;
  int iVar13;
  int iVar14;
  int iVar15;
  long lVar16;
  undefined8 uVar17;
  ulong uVar18;
  char *pcVar19;
  ulong uVar20;
  uint uVar21;
  long lVar22;
  long lVar23;
  undefined8 local_90;
  undefined4 *local_88;
  long local_80;
  undefined8 local_78;
  undefined8 local_70;
  undefined8 local_68;
  
  local_70 = 0;
  local_68 = 0;
  local_80 = 0;
  local_78 = 0;
  local_90 = 0;
  local_88 = (undefined4 *)0x0;
  lVar16 = FUN_ffff00009077d24c(0x80000);
  if (lVar16 == 0) {
    FUN_ffff00009077de54(s_slbc__Can_t_alloc_buffer_ffff0000907ead3b);
    return;
  }
  FUN_ffff00009077eb88(lVar16,0,0x80000);
  uVar9 = FUN_ffff00009077f434(lVar16,0x7ffff,s_SLBC_STATUS_DUMP_ffff0000907e1618);
  if (_DAT_ffff00009086f208 != 0) {
    FUN_ffff00009077de54(s_slbc__get_fdt_info_fail__err___d_ffff000090800b24);
    iVar10 = FUN_ffff00009077f434
                       (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                        s_SLBC_GET_FDT_INFO_FAIL__ERR___d_ffff0000907dad93,_DAT_ffff00009086f208);
    uVar9 = iVar10 + uVar9;
    goto LAB_ffff00009073ef54;
  }
  iVar10 = FUN_ffff00009077f434
                     (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                      s______________________________SLB_ffff0000907dfab4);
  uVar9 = iVar10 + uVar9;
  if (DAT_ffff00009086f20c == 1) {
    FUN_ffff00009077de54(s_slbc__get_sram_info_fail__err____ffff000090800b46,0xffffffff);
    iVar10 = FUN_ffff00009077f434
                       (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                        s_SLBC_GET_SRAM_INFO_FAIL__ERR___d_ffff0000907cf925,
                        -(DAT_ffff00009086f20c & 1));
    uVar9 = iVar10 + uVar9;
  }
  else {
    iVar10 = FUN_ffff00009077f434
                       (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                        s_SLBC_SRAM_BASE___0x_x_ffff0000907e162a,_DAT_ffff00009086f210);
    uVar9 = iVar10 + uVar9;
    iVar10 = FUN_ffff00009077f434
                       (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                        s_SLBC_SRAM_SIZE___0x_x_ffff0000907e2eb1,_DAT_ffff00009086f214);
    uVar9 = iVar10 + uVar9;
    if ((_DAT_ffff00009086f210 == 0) || (_DAT_ffff00009086f214 == 0)) {
      FUN_ffff00009077de54(s_slbc__get_sram_info_fail__zero__ffff0000907d9357);
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                          s_SLBC_GET_SRAM_INFO_FAIL___ZERO__ffff000090800b69);
      uVar9 = iVar10 + uVar9;
    }
    else {
      uVar21 = 0;
      do {
        iVar10 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s_0x_x___0x_x_ffff0000907e2ec9,
                            uVar21,*(undefined4 *)
                                    (((ulong)_DAT_ffff00009086f210 & 0xfffff) + (ulong)uVar21 +
                                    -0xfffffff00000));
        uVar9 = iVar10 + uVar9;
        uVar21 = uVar21 + 4;
      } while (uVar21 < _DAT_ffff00009086f214);
    }
  }
  iVar10 = FUN_ffff00009077f434
                     (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                      s______________________________SLB_ffff0000907ead55);
  uVar9 = iVar10 + uVar9;
  iVar10 = FUN_ffff000090783450(s_platform_debug_slbc_shared_ffff0000907e4b52,&local_80,&local_90);
  if (iVar10 < 0) {
    FUN_ffff00009077de54(s_slbc__get_reserved_mblock_info_f_ffff0000907f2cdd,iVar10);
    pcVar19 = s_SLBC_GET_RESERVED_MBLOCK_INFO_FA_ffff0000907f49d2;
LAB_ffff00009073ef20:
    iVar10 = FUN_ffff00009077f434(lVar16 + (ulong)uVar9,0x7ffff - uVar9,pcVar19,iVar10);
  }
  else if (local_80 == 0) {
    FUN_ffff00009077de54(s_slbc__mblock_allocation_failed_ffff0000907cf947);
    iVar10 = FUN_ffff00009077f434
                       (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                        s_SLBC_MBLOCK_ALLOCATION_FAILED__ffff0000907d62a6);
  }
  else {
    iVar10 = FUN_ffff00009077b79c
                       (DAT_ffff000090839908,s_vm_SLBC_shared_ffff0000907d2e50,local_90,&local_88,
                        0xc,local_80,0,0);
    if (iVar10 != 0) {
      FUN_ffff00009077de54(s_slbc__vmm_alloc_physical_failed__ffff0000907d7b16,iVar10);
      pcVar19 = s_SLBC_vmm_alloc_physical_FAILED__E_ffff0000907d62c6;
      goto LAB_ffff00009073ef20;
    }
    if (local_88 != (undefined4 *)0x0) {
      _DAT_ffff00009086f218 = local_88;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                          s_SLBC_DRAM_BASE_VA___0x_llx_ffff0000907e1641);
      uVar9 = iVar10 + uVar9;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                          s_SLBC_DRAM_BASE_PA___0x_llx_ffff0000907e165d,local_80);
      uVar9 = iVar10 + uVar9;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                          s_SLBC_DRAM_SIZE___0x_llx_ffff0000907ec65b,local_90);
      uVar9 = iVar10 + uVar9;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                          s______________Enable_Setting______ffff0000907d474f);
      uVar9 = uVar9 + iVar10;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s_enable____u_ffff0000907f49ff,
                          *_DAT_ffff00009086f218);
      uVar9 = iVar10 + uVar9;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s_log_enable____u_ffff0000907dfb00,
                          _DAT_ffff00009086f218[1]);
      uVar9 = iVar10 + uVar9;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s_rec_enable____u_ffff0000907f64d0,
                          _DAT_ffff00009086f218[2]);
      uVar9 = iVar10 + uVar9;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                          s_______________Mask_Setting_______ffff0000907efb32);
      uVar9 = uVar9 + iVar10;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s_log_mask___0x_x_ffff0000907f2d0b,
                          _DAT_ffff00009086f218[3]);
      uVar9 = iVar10 + uVar9;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s_rec_mask___0x_x_ffff0000907f2d1c,
                          _DAT_ffff00009086f218[4]);
      uVar9 = iVar10 + uVar9;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s_dump_mask___0x_x_ffff0000907e4b6d,
                          _DAT_ffff00009086f218[5]);
      uVar9 = iVar10 + uVar9;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                          s________________Record_Info_______ffff0000907ff15a);
      uVar9 = uVar9 + iVar10;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                          s_record_count____llu_ffff0000907ff185,
                          *(undefined8 *)(_DAT_ffff00009086f218 + 0xc));
      uVar9 = iVar10 + uVar9;
      iVar10 = FUN_ffff00009077f434
                         (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                          s_RECORD_DATA__MAX_RECORD_EVENT__d_ffff0000907d62ef,1000);
      uVar9 = uVar9 + iVar10;
      if (*(long *)(_DAT_ffff00009086f218 + 0xc) == 0) {
        iVar10 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s_NO_RECORD__ffff0000907e4b7f);
        uVar9 = iVar10 + uVar9;
      }
      else {
        iVar10 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                            s___6s___6s___16s___32s___5s___5s___ffff0000907eadca,
                            &DAT_ffff0000907f82b8,s_LEVEL_ffff0000907d7b40,
                            s_TIME_us__ffff0000907dc8eb,s_EVENT_ffff0000907dadb4,
                            &DAT_ffff0000907f14b9,&DAT_ffff0000907d2e5f,s_ID_HEX__ffff0000907fb9cb,
                            &DAT_ffff0000907dfb11,&DAT_ffff0000907d7b46);
        uVar18 = *(ulong *)(_DAT_ffff00009086f218 + 0xc);
        iVar11 = (int)((uVar18 - 1) / 1000);
        uVar6 = (int)(uVar18 - 1) + iVar11 * -1000;
        uVar9 = iVar10 + uVar9;
        uVar21 = uVar6 - 999;
        if (uVar6 < 999) {
          uVar21 = uVar6 + 1;
        }
        if (uVar18 < 0x3e9) {
          uVar21 = 0;
        }
        uVar2 = 999;
        if (uVar18 < 0x3e9) {
          uVar2 = uVar6;
        }
        if (uVar21 <= uVar2) {
          iVar10 = (uVar2 - uVar21) + 1;
          lVar22 = (ulong)uVar21 * 0xc0 + 0x58;
          do {
            puVar1 = (undefined4 *)((long)_DAT_ffff00009086f218 + lVar22);
            uVar6 = puVar1[1];
            uVar20 = (ulong)uVar6;
            if (uVar6 < 6) {
              *(int *)((long)&local_78 + uVar20 * 4) = *(int *)((long)&local_78 + uVar20 * 4) + 1;
            }
            ppuVar3 = &PTR_DAT_ffff000090819fb0 + uVar20;
            if (5 < uVar6) {
              ppuVar3 = &PTR_DAT_ffff000090819fd8;
            }
            ppuVar4 = &PTR_DAT_ffff000090819f80 + uVar20;
            if (5 < uVar6) {
              ppuVar4 = &PTR_DAT_ffff000090819fa8;
            }
            ppuVar5 = &PTR_DAT_ffff000090819fe0 + (uint)puVar1[2];
            if (5 < (uint)puVar1[2]) {
              ppuVar5 = &PTR_DAT_ffff00009081a008;
            }
            iVar15 = FUN_ffff00009077f434
                               (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                                s__6s___6s___16llu___32s___5d___5s_ffff0000907dfb15,*ppuVar3,
                                *ppuVar4,*(undefined8 *)(puVar1 + 6),puVar1 + -8,*puVar1,*ppuVar5,
                                puVar1[3],puVar1[4],puVar1 + 8);
            iVar10 = iVar10 + -1;
            uVar9 = iVar15 + uVar9;
            lVar22 = lVar22 + 0xc0;
          } while (iVar10 != 0);
        }
        if (uVar21 != 0) {
          lVar23 = 0;
          lVar22 = -(ulong)(uint)((int)uVar18 + iVar11 * -1000);
          do {
            uVar21 = *(uint *)((long)_DAT_ffff00009086f218 + lVar23 + 0x5c);
            uVar18 = (ulong)uVar21;
            if (uVar21 < 6) {
              *(int *)((long)&local_78 + uVar18 * 4) = *(int *)((long)&local_78 + uVar18 * 4) + 1;
            }
            ppuVar3 = &PTR_DAT_ffff000090819fb0 + uVar18;
            if (5 < uVar21) {
              ppuVar3 = &PTR_DAT_ffff000090819fd8;
            }
            uVar6 = *(uint *)((long)_DAT_ffff00009086f218 + lVar23 + 0x60);
            ppuVar4 = &PTR_DAT_ffff000090819f80 + uVar18;
            if (5 < uVar21) {
              ppuVar4 = &PTR_DAT_ffff000090819fa8;
            }
            ppuVar5 = &PTR_DAT_ffff000090819fe0 + uVar6;
            if (5 < uVar6) {
              ppuVar5 = &PTR_DAT_ffff00009081a008;
            }
            iVar10 = FUN_ffff00009077f434
                               (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                                s__6s___6s___16llu___32s___5d___5s_ffff0000907dfb15,*ppuVar3,
                                *ppuVar4,*(undefined8 *)
                                          ((long)_DAT_ffff00009086f218 + lVar23 + 0x70),
                                (long)_DAT_ffff00009086f218 + lVar23 + 0x38,
                                *(undefined4 *)((long)_DAT_ffff00009086f218 + lVar23 + 0x58),
                                *ppuVar5,*(undefined4 *)((long)_DAT_ffff00009086f218 + lVar23 + 100)
                                ,*(undefined4 *)((long)_DAT_ffff00009086f218 + lVar23 + 0x68),
                                (long)_DAT_ffff00009086f218 + lVar23 + 0x78);
            bVar8 = lVar22 != -1;
            lVar22 = lVar22 + 1;
            uVar9 = iVar10 + uVar9;
            lVar23 = lVar23 + 0xc0;
          } while (bVar8);
        }
        iVar11 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                            s________________Summary___________ffff0000907f82bd);
        iVar10 = (int)local_78;
        uVar9 = iVar11 + uVar9;
        iVar11 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s__s___u_ffff0000907e955e,
                            PTR_DAT_ffff000090819f80,local_78 & 0xffffffff);
        uVar9 = iVar11 + uVar9;
        iVar11 = local_78._4_4_;
        iVar12 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s__s___u_ffff0000907e955e,
                            PTR_DAT_ffff000090819f88,local_78._4_4_);
        iVar15 = (int)local_70;
        uVar9 = iVar12 + uVar9;
        iVar12 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s__s___u_ffff0000907e955e,
                            PTR_DAT_ffff000090819f90,local_70 & 0xffffffff);
        uVar9 = iVar12 + uVar9;
        iVar12 = local_70._4_4_;
        iVar13 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s__s___u_ffff0000907e955e,
                            PTR_DAT_ffff000090819f98,local_70._4_4_);
        iVar7 = (int)local_68;
        uVar9 = iVar13 + uVar9;
        iVar13 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s__s___u_ffff0000907e955e,
                            PTR_DAT_ffff000090819fa0,local_68 & 0xffffffff);
        uVar9 = iVar13 + uVar9;
        iVar13 = local_68._4_4_;
        iVar14 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s__s___u_ffff0000907e955e,
                            PTR_DAT_ffff000090819fa8,local_68._4_4_);
        uVar9 = iVar14 + uVar9;
        iVar10 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,s_TOTAL___u_ffff0000907d4779,
                            iVar13 + iVar7 + iVar12 + iVar15 + iVar11 + iVar10);
        uVar9 = iVar10 + uVar9;
        iVar10 = FUN_ffff00009077f434
                           (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                            s__________________________________ffff0000907e1679);
        uVar9 = uVar9 + iVar10;
      }
      goto LAB_ffff00009073ef54;
    }
    FUN_ffff00009077de54(s_slbc__slbc_dram_base_va_is_zero__ffff0000907eada1,0);
    iVar10 = FUN_ffff00009077f434
                       (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                        s_SLBC_slbc_dram_base_va_is_zero__0_ffff0000907e9536,local_88);
  }
  uVar9 = iVar10 + uVar9;
LAB_ffff00009073ef54:
  if (local_88 != (undefined4 *)0x0) {
    FUN_ffff00009077bb68(DAT_ffff000090839908);
  }
  iVar10 = FUN_ffff00009077f434
                     (lVar16 + (ulong)uVar9,0x7ffff - uVar9,
                      s______________________________SLB_ffff0000907f14be);
  uVar17 = FUN_ffff00009077b06c(lVar16);
  uVar18 = (*param_1)(uVar17,iVar10 + uVar9);
  if ((uVar18 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__slbc_aee_dump_failed_ffff0000907e2ed6,s_save_slbc_data_ffff0000907dadba);
  }
  thunk_FUN_ffff0000907c2ad8(lVar16);
  return;
}



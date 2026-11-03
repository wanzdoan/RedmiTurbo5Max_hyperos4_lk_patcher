// ffff00009071d734  size=2864  FUN_ffff00009071d734

uint FUN_ffff00009071d734(undefined8 param_1)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  int iVar6;
  uint uVar7;
  uint uVar8;
  uint uVar9;
  long lVar10;
  undefined8 uVar11;
  uint *puVar12;
  uint *puVar13;
  ulong uVar14;
  char *pcVar15;
  ulong uVar16;
  char *pcVar17;
  int iVar18;
  long lVar19;
  ulong uVar20;
  int iVar21;
  uint *puVar22;
  ulong local_6b0 [5];
  undefined2 local_688;
  undefined8 local_680;
  undefined8 local_678;
  ulong local_670 [192];
  uint local_6c [3];
  
  local_680 = 0;
  local_678 = 0;
  local_688 = 0;
  local_6b0[3] = 0;
  local_6b0[4] = 0x737973757061;
  local_6b0[1] = 0;
  local_6b0[2] = 0;
  local_6b0[0] = 0;
  local_670[0] = local_670[0] & 0xffffffff00000000;
  iVar6 = FUN_ffff00009076da24(param_1,s__apusys_rv_4c200000_ffff00009080229b);
  if (iVar6 < 0) {
    iVar6 = FUN_ffff00009076da24(param_1,s__soc_apusys_rv_4c200000_ffff0000907ea988);
    if (iVar6 < 0) {
      iVar6 = FUN_ffff00009076da24(param_1,s__apusys_rv_ffff0000907edd87);
      if (iVar6 < 0) {
        iVar6 = FUN_ffff00009076da24(param_1,s__soc_apusys_rv_ffff0000907f9694);
        if (iVar6 < 0) {
          pcVar17 = s__s__apusys_rv_not_found_ffff0000907da7a1;
          pcVar15 = s_get_apusys_rv_fdt_path_offset_ffff0000907f7e13;
          goto LAB_ffff00009071e220;
        }
      }
    }
  }
  lVar10 = FUN_ffff00009076ddb4(param_1,iVar6,s_status_ffff0000907edcdb,local_670);
  if (lVar10 != 0) {
    uVar7 = (*(uint *)(lVar10 + 8) & 0xff00ff00) >> 8 | (*(uint *)(lVar10 + 8) & 0xff00ff) << 8;
    uVar11 = FUN_ffff00009076d3d4(param_1,uVar7 >> 0x10 | uVar7 << 0x10);
    FUN_ffff00009077e11c
              (s__s___s__s_ffff0000907e76aa,s_apusys_rv_prop_off_ffff0000907ea975,uVar11,
               lVar10 + 0xc);
    uVar11 = FUN_ffff00009077f250(&DAT_ffff0000907df649);
    iVar6 = FUN_ffff00009077f348(lVar10 + 0xc,&DAT_ffff0000907df649,uVar11);
    if (iVar6 == 0) {
      FUN_ffff00009071ebf4(0xc200040c,1,0,0,0,0,0,0,local_6b0);
      uVar7 = (uint)local_6b0[0];
      if ((-1 < (int)(uint)local_6b0[0]) ||
         (FUN_ffff00009077e11c
                    (s__s__smc_call_MTK_APUSYS_SMC_OP_A_ffff0000907f5fe5,
                     s_platform_load_apusys_rv_ffff0000907f5fcd,local_6b0[0] & 0xffffffff),
         uVar7 == 0xffffffd3)) {
        FUN_ffff00009071ebf4(0xc200040c,5,0,0,0,0,0,0,local_6b0);
        uVar7 = (uint)local_6b0[0];
        if ((-1 < (int)(uint)local_6b0[0]) ||
           (FUN_ffff00009077e11c
                      (s__s__smc_call_MTK_APUSYS_SMC_OP_A_ffff0000907e5dca,
                       s_platform_load_apusys_rv_ffff0000907f5fcd,local_6b0[0] & 0xffffffff),
           uVar7 == 0xffffffd3)) {
          puVar12 = (uint *)FUN_ffff00009077d514(0x7c);
          if (puVar12 == (uint *)0x0) {
            uVar7 = 0xfffffff4;
          }
          else {
            FUN_ffff00009077ee50(puVar12,0,0x7c);
            local_6c[0] = 0;
            FUN_ffff00009077ee50(local_670,0,0x600);
            FUN_ffff0000907350ac(local_670,local_6c);
            uVar16 = (ulong)local_6c[0];
            if (0 < (int)local_6c[0]) {
              uVar11 = FUN_ffff00009077f250(local_6b0 + 4);
              lVar10 = 0;
              pcVar17 = s__s__unable_to_locate__s_ffff0000907d2893;
LAB_ffff00009071d9a0:
              lVar19 = *(long *)((long)local_670 + lVar10);
              iVar6 = FUN_ffff00009077f348(lVar19,local_6b0 + 4,uVar11);
              if (iVar6 != 0) goto code_r0xffff00009071d9b8;
              if (lVar19 == 0) goto LAB_ffff00009071dbb8;
              lVar10 = FUN_ffff00009077c028(lVar19);
              if (lVar10 != 0) {
                uVar16 = *(ulong *)(lVar10 + 0x20);
                FUN_ffff00009077bfac();
                if ((long)uVar16 < 0) goto LAB_ffff00009071dbd8;
                puVar12[1] = 0;
                uVar7 = FUN_ffff00009071e264(param_1,s_up_code_buf_sz_ffff0000907ef537);
                if (((int)uVar7 < 0) &&
                   (uVar7 = FUN_ffff00009071e264(param_1,s_up_code_buf_sz_ffff0000907e9020),
                   (int)uVar7 < 0)) goto LAB_ffff00009071dbd8;
                puVar12[2] = uVar7;
                puVar12[0x15] = uVar7 + puVar12[1] + (int)uVar16;
                uVar7 = FUN_ffff00009071e264(param_1,s_up_coredump_buf_sz_ffff0000907d766f);
                if (((int)uVar7 < 0) &&
                   (uVar7 = FUN_ffff00009071e264(param_1,s_up_coredump_buf_sz_ffff0000907edd16),
                   (int)uVar7 < 0)) goto LAB_ffff00009071dbd8;
                puVar12[0x16] = uVar7;
                uVar8 = FUN_ffff00009071e264(param_1,s_ce_coredump_buf_sz_ffff0000907d40c6);
                if (((int)uVar8 < 0) &&
                   (uVar7 = FUN_ffff00009071e264(param_1,s_ce_coredump_buf_sz_ffff0000907d283c),
                   uVar8 = uVar7, (int)uVar7 < 0)) goto LAB_ffff00009071dbd8;
                puVar12[0x17] = puVar12[0x16] + puVar12[0x15];
                uVar7 = FUN_ffff00009071e264(param_1,s_mdla_coredump_buf_sz_ffff0000907edd29);
                if (((int)uVar7 < 0) &&
                   (uVar7 = FUN_ffff00009071e264(param_1,s_mdla_coredump_buf_sz_ffff0000907e902f),
                   (int)uVar7 < 0)) goto LAB_ffff00009071dbd8;
                puVar12[0x18] = uVar7;
                puVar12[0x19] = puVar12[0x17] + uVar7;
                uVar7 = FUN_ffff00009071e264(param_1,s_mvpu_coredump_buf_sz_ffff0000907fb442);
                if (((int)uVar7 < 0) &&
                   (uVar7 = FUN_ffff00009071e264(param_1,s_mvpu_coredump_buf_sz_ffff0000907d284f),
                   (int)uVar7 < 0)) goto LAB_ffff00009071dbd8;
                puVar12[0x1a] = uVar7;
                puVar12[0x1b] = puVar12[0x19] + uVar7;
                uVar7 = FUN_ffff00009071e264(param_1,s_mvpu_sec_coredump_buf_sz_ffff0000907ef546);
                if (((int)uVar7 < 0) &&
                   (uVar7 = FUN_ffff00009071e264
                                      (param_1,s_mvpu_sec_coredump_buf_sz_ffff0000907f7dfa),
                   (int)uVar7 < 0)) goto LAB_ffff00009071dbd8;
                puVar12[0x1c] = uVar7;
                puVar13 = (uint *)FUN_ffff00009077d514(uVar16);
                if (puVar13 == (uint *)0x0) {
                  FUN_ffff00009077e11c
                            (s__s__part_tmp_buf_sz_0x_llx__mall_ffff0000907d40d9,
                             s_platform_load_apusys_rv_ffff0000907f5fcd,uVar16);
                  uVar7 = 0xfffffff4;
                  goto LAB_ffff00009071dbd8;
                }
                FUN_ffff00009077ee50(puVar13,0,uVar16);
                uVar7 = FUN_ffff000090785d18
                                  (local_6b0 + 4,s_tinysys_apusys_RV33_A_ffff0000907f444b,puVar13,
                                   uVar16 & 0xffffffff);
                if ((int)uVar7 < 0) {
                  FUN_ffff00009077e11c(s_apusys_load_from_partition_faile_ffff0000907ec183,uVar7);
                  goto LAB_ffff00009071dd80;
                }
                if (*puVar13 != 0x58901690) {
                  lVar10 = 0;
                  lVar19 = 0;
                  goto LAB_ffff00009071dd68;
                }
                lVar10 = 0;
                lVar19 = 0;
                puVar22 = puVar13;
                do {
                  uVar7 = puVar22[1];
                  lVar1 = (long)puVar22 + (ulong)uVar7;
                  iVar6 = (int)puVar13;
                  iVar18 = (int)lVar1;
                  iVar21 = (int)puVar22;
                  switch(puVar22[4]) {
                  case 0:
                    uVar7 = puVar22[2];
                    puVar12[3] = iVar18 - iVar6;
                    puVar12[4] = uVar7;
                    lVar19 = lVar1;
                    break;
                  case 1:
                    uVar9 = puVar22[2];
                    puVar12[5] = iVar21 - iVar6;
                    puVar12[6] = uVar9 + uVar7;
                    lVar10 = lVar1;
                    break;
                  case 2:
                    uVar7 = puVar22[2];
                    puVar12[7] = iVar18 - iVar6;
                    puVar12[8] = uVar7;
                    break;
                  case 3:
                    uVar7 = puVar22[2];
                    puVar12[9] = iVar18 - iVar6;
                    puVar12[10] = uVar7;
                    break;
                  case 4:
                    uVar9 = puVar22[2];
                    puVar12[0xb] = iVar21 - iVar6;
                    puVar12[0xc] = uVar9 + uVar7;
                    break;
                  case 5:
                    uVar7 = puVar22[2];
                    puVar12[0xd] = iVar18 - iVar6;
                    puVar12[0xe] = uVar7;
                    break;
                  case 6:
                    uVar9 = puVar22[2];
                    puVar12[0xf] = iVar21 - iVar6;
                    puVar12[0x10] = uVar9 + uVar7;
                    break;
                  case 7:
                    uVar7 = puVar22[2];
                    puVar12[0x11] = iVar18 - iVar6;
                    puVar12[0x12] = uVar7;
                    break;
                  case 8:
                    uVar9 = puVar22[2];
                    puVar12[0x13] = iVar21 - iVar6;
                    puVar12[0x14] = uVar9 + uVar7;
                    break;
                  case 9:
                    uVar9 = puVar22[2];
                    puVar12[0x1d] = iVar18 - iVar6;
                    puVar12[0x1e] = uVar9 + uVar7;
                    break;
                  case 10:
                    break;
                  default:
                    FUN_ffff00009077e11c(s_Warning__Ignore_unknown_APUSYS_i_ffff0000907d5c22);
                  }
                  puVar22 = (uint *)(lVar1 + (ulong)((puVar22[2] + puVar22[3]) - 1 & -puVar22[3]));
                } while (*puVar22 == 0x58901690);
                if ((lVar19 == 0) || (lVar10 == 0)) {
LAB_ffff00009071dd68:
                  FUN_ffff00009077e11c
                            (s_APUSYS_partition_missing___PM_0x_ffff0000907f0fad,lVar19,lVar10);
                  uVar7 = 0xfffffffe;
LAB_ffff00009071dd80:
                  pcVar17 = s__s__apusys_rv_load_fw_fail__d__ffff0000907f601d;
LAB_ffff00009071dd88:
                  FUN_ffff00009077e11c(pcVar17,s_platform_load_apusys_rv_ffff0000907f5fcd,uVar7);
                }
                else {
                  FUN_ffff00009077e11c
                            (s__s_APUSYS_part_load_finished__PM_ffff0000907f7e31,
                             s_apusys_rv_load_fw_ffff0000907f2857,lVar19,puVar12[3],lVar10,
                             puVar12[5]);
                  uVar9 = puVar12[0x16] + uVar8 + puVar12[0x18] + puVar12[0x1a] + puVar12[0x1c];
                  uVar7 = puVar12[2] + 0x4fffff + uVar9 & 0xffe00000;
                  uVar16 = (ulong)uVar7;
                  *puVar12 = uVar7;
                  iVar6 = FUN_ffff00009071c708();
                  if (iVar6 == 0) {
                    uVar14 = FUN_ffff0000907198dc
                                       (uVar16,0x200000,0x90000000,0,0,
                                        s_apu_apusys_rv_secure_ffff0000907d8f6c);
                  }
                  else {
                    uVar14 = FUN_ffff00009071a09c
                                       (uVar16,0x200000,0x240000000,0x340000000,0,0,
                                        s_apu_apusys_rv_secure_ffff0000907d8f6c);
                  }
                  if (uVar14 == 0) {
                    FUN_ffff00009077e11c(s_apu_apusys_rv_secure_mblock_allo_ffff0000907d2864);
                    uVar7 = 0xfffffffb;
                  }
                  else {
                    uVar7 = FUN_ffff00009077ba64
                                      (DAT_ffff000090839928,
                                       s_vm_apu_apusys_rv_secure_ffff0000907dc412,uVar16,&local_678,
                                       0xc,uVar14,0,0);
                    if (uVar7 == 0) {
                      FUN_ffff00009077ee50(local_678,0,uVar16);
                      FUN_ffff00009071e348(puVar12,puVar13,local_678,uVar16);
                      FUN_ffff000090707c70(local_678,uVar16);
                      FUN_ffff00009071ebf4(0xc200040c,2,uVar14,uVar16,0,0,0,0,local_6b0);
                      uVar7 = (uint)local_6b0[0];
                      if ((-1 < (int)(uint)local_6b0[0]) ||
                         (FUN_ffff00009077e11c
                                    (s__s__smc_call_MTK_APUSYS_SMC_OP_A_ffff000090802240,
                                     s_platform_load_apusys_rv_ffff0000907f5fcd,
                                     local_6b0[0] & 0xffffffff), uVar7 == 0xffffffd3)) {
                        if (puVar12[0x1e] != 0) {
                          FUN_ffff00009071ebf4(0xc200040c,8,0,0,0,0,0,0,local_6b0);
                          uVar7 = (uint)local_6b0[0];
                          if (((int)(uint)local_6b0[0] < 0) &&
                             (FUN_ffff00009077e11c
                                        (s__s__smc_call_MTK_APUSYS_SMC_OP_A_ffff0000907ea938,
                                         s_platform_load_apusys_rv_ffff0000907f5fcd,
                                         local_6b0[0] & 0xffffffff), uVar7 != 0xffffffd3))
                          goto LAB_ffff00009071e1d8;
                        }
                        uVar2 = puVar12[6];
                        uVar3 = puVar12[0x10];
                        uVar4 = puVar12[0xc];
                        uVar5 = puVar12[0x14];
                        uVar7 = FUN_ffff00009071e264(param_1,s_regdump_buf_sz_ffff0000907f0f9e);
                        if (((int)uVar7 < 0) &&
                           (uVar7 = FUN_ffff00009071e264(param_1,s_regdump_buf_sz_ffff0000907e9044),
                           (int)uVar7 < 0)) {
                          FUN_ffff00009077e11c
                                    (s__s__get_regdump_buf_sz_fail_ffff0000907d4104,
                                     s_platform_load_apusys_rv_ffff0000907f5fcd);
                        }
                        else {
                          uVar20 = (ulong)uVar9 + (ulong)(uVar4 + uVar2 + uVar3 + uVar5) +
                                   (ulong)uVar7 + (ulong)puVar12[0x1e] + 0x10057 & 0xfffff0000;
                          iVar6 = FUN_ffff00009071c708();
                          if (iVar6 == 0) {
                            lVar10 = FUN_ffff0000907198dc
                                               (uVar20,0x10000,0x90000000,0,0,
                                                s_apu_apusys_rv_aee_coredump_ffff0000907dc42a);
                          }
                          else {
                            lVar10 = FUN_ffff00009071a09c
                                               (uVar20,0x10000,0x240000000,0x340000000,0,0,
                                                s_apu_apusys_rv_aee_coredump_ffff0000907dc42a);
                          }
                          if (lVar10 == 0) {
                            FUN_ffff00009077e11c
                                      (s_apu_apusys_rv_aee_coredump_mbloc_ffff0000907e27c6);
                            uVar7 = 0xfffffffb;
                          }
                          else {
                            uVar9 = FUN_ffff00009077ba64
                                              (DAT_ffff000090839928,
                                               s_vm_apu_apusys_rv_aee_coredump_ffff00009080227d,
                                               uVar20,&local_680,0xc,lVar10,0,1);
                            if (uVar9 == 0) {
                              FUN_ffff00009077ee50(local_680,0,uVar20);
                              FUN_ffff00009071e4a0(puVar12,local_680,puVar13,uVar7,uVar8);
                              FUN_ffff00009071ebf4(0xc200040c,3,lVar10,uVar20,0,0,0,0,local_6b0);
                              uVar7 = (uint)local_6b0[0];
                              if ((-1 < (int)(uint)local_6b0[0]) ||
                                 (FUN_ffff00009077e11c
                                            (s__s__smc_call_MTK_APUSYS_SMC_OP_A_ffff0000907d7682,
                                             s_platform_load_apusys_rv_ffff0000907f5fcd,
                                             local_6b0[0] & 0xffffffff), uVar7 == 0xffffffd3)) {
                                local_670[1] = uVar14 + uVar16;
                                local_670[2] = 0xb4db6900000017;
                                local_670[3] = 0xb6db6d;
                                local_670[0] = uVar14;
                                uVar7 = FUN_ffff00009078b0fc(local_670);
                                if (uVar7 == 0) {
                                  uVar7 = FUN_ffff00009071e660(param_1);
                                  if (uVar7 != 0) {
                                    pcVar17 = s__s__prepare_sapu_ctrl_mem_fail___ffff0000907fed31;
                                    goto LAB_ffff00009071dd88;
                                  }
                                  goto LAB_ffff00009071dd98;
                                }
                                FUN_ffff00009077e11c
                                          (s__s__apusys_rv_sec_mem_emi_protec_ffff0000907e5e0c,
                                           s_platform_load_apusys_rv_ffff0000907f5fcd,uVar7);
                              }
                              FUN_ffff00009077be30(DAT_ffff000090839928,local_680);
                            }
                            else {
                              FUN_ffff00009077e11c
                                        (s_APUSYS_vmm_alloc_physical_vm_apu_ffff0000907edd3e,uVar9);
                              uVar7 = uVar9;
                            }
                            FUN_ffff00009071a7f0(lVar10);
                          }
                        }
                      }
LAB_ffff00009071e1d8:
                      FUN_ffff00009077be30(DAT_ffff000090839928,local_678);
                    }
                    else {
                      FUN_ffff00009077e11c
                                (s_APUSYS_vmm_alloc_physical_vm_apu_ffff0000907f603d,uVar7);
                    }
                    FUN_ffff00009071a7f0(uVar14);
                  }
                }
LAB_ffff00009071dd98:
                thunk_FUN_ffff0000907c314c(puVar12);
                puVar12 = puVar13;
                goto LAB_ffff00009071dbd8;
              }
              pcVar17 = s__s__unable_to_get_part_size_of___ffff0000908022af;
              goto LAB_ffff00009071dbc0;
            }
LAB_ffff00009071dbb8:
            pcVar17 = s__s__unable_to_locate__s_ffff0000907d2893;
LAB_ffff00009071dbc0:
            FUN_ffff00009077e11c(pcVar17,s_get_apusys_rv_part_size_ffff0000907ef55f,local_6b0 + 4);
LAB_ffff00009071dbd8:
            thunk_FUN_ffff0000907c314c(puVar12);
          }
        }
      }
    }
    else {
      FUN_ffff00009077e11c
                (s__s__apusys_rv_is_not_ready_yet_ffff0000907cf3ae,
                 s_platform_load_apusys_rv_ffff0000907f5fcd);
      uVar7 = 0;
    }
    return uVar7;
  }
  pcVar17 = s__s__prop_is_NULL_ffff0000907ec171;
  pcVar15 = s_apusys_rv_prop_off_ffff0000907ea975;
LAB_ffff00009071e220:
  FUN_ffff00009077e11c(pcVar17,pcVar15);
  FUN_ffff00009077e11c
            (s__s__failed_to_find_apusys_rv_in_d_ffff0000907edcf1,
             s_platform_load_apusys_rv_ffff0000907f5fcd);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_common_apusys__ffff0000907ec142,0x1cf,&DAT_ffff0000907d2506);
code_r0xffff00009071d9b8:
  lVar10 = lVar10 + 8;
  if (uVar16 * 8 - lVar10 == 0) goto LAB_ffff00009071dbc0;
  goto LAB_ffff00009071d9a0;
}



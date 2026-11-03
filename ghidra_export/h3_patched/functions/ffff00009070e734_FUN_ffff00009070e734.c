// ffff00009070e734  size=1664  FUN_ffff00009070e734

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070e734(undefined8 param_1,undefined4 *param_2,uint param_3)

{
  byte *pbVar1;
  uint uVar2;
  undefined4 uVar3;
  ushort uVar4;
  int iVar5;
  int iVar6;
  long lVar7;
  char *pcVar8;
  ulong uVar9;
  uint uVar10;
  ulong uVar11;
  byte *pbVar12;
  ushort *puVar13;
  long lVar14;
  uint uVar15;
  ulong uVar16;
  ulong uVar17;
  ushort *puVar18;
  ushort *puVar19;
  ulong uVar20;
  uint uVar21;
  undefined8 uVar22;
  uint uVar23;
  uint local_68;
  
  lVar7 = FUN_ffff00009077bd60();
  if ((lVar7 == 0) && (lVar7 = FUN_ffff00009077bc50(param_1), lVar7 == 0)) {
    pcVar8 = s_partition_does_not_exist_ffff0000907fe857;
LAB_ffff00009070e814:
    FUN_ffff000090709d10(pcVar8);
    return;
  }
  uVar17 = *(ulong *)(lVar7 + 0x20);
  if (DAT_ffff00009083dc80 == '\x01') {
    uVar22 = 0xffff00009083d690;
    iVar5 = FUN_ffff00009077ede4(param_1,0xffff00009083d690);
    if (iVar5 == 0) {
      iVar5 = 0;
LAB_ffff00009070e84c:
      FUN_ffff00009077de54(s_partition_name__s_vs_sparsecrc_l_ffff0000907fc9c7,param_1,iVar5,uVar22)
      ;
    }
    else {
      uVar22 = 0xffff00009083d80c;
      iVar5 = FUN_ffff00009077ede4(param_1,0xffff00009083d80c);
      if (iVar5 == 0) {
        iVar5 = 1;
        goto LAB_ffff00009070e84c;
      }
      uVar22 = 0xffff00009083d988;
      iVar5 = FUN_ffff00009077ede4(param_1,0xffff00009083d988);
      if (iVar5 == 0) {
        iVar5 = 2;
        goto LAB_ffff00009070e84c;
      }
      uVar22 = 0xffff00009083db04;
      iVar5 = FUN_ffff00009077ede4(param_1,0xffff00009083db04);
      if (iVar5 == 0) {
        iVar5 = 3;
        goto LAB_ffff00009070e84c;
      }
      iVar5 = -1;
    }
    FUN_ffff00009077de54(s_sparsecrc_index____d_ffff0000907f0be9,iVar5);
  }
  else {
    iVar5 = -1;
  }
  FUN_ffff00009077de54
            (s_Image_size_span_0x_llx__partitio_ffff0000907df316,
             (ulong)(uint)param_2[3] * (ulong)(uint)param_2[4],uVar17);
  if ((ulong)(uint)param_2[3] * (ulong)(uint)param_2[4] < uVar17 ||
      (ulong)(uint)param_2[3] * (ulong)(uint)param_2[4] - uVar17 == 0) {
    uVar4 = *(ushort *)(param_2 + 2);
    if (param_3 < uVar4) {
      pcVar8 = s_sparse_image_header_invalid__ffff0000907da4bd;
    }
    else {
      FUN_ffff00009077de54(s_____Sparse_Image_Header_____ffff0000907dd86b);
      FUN_ffff00009077de54(s_magic__0x_x_ffff0000907f5b40,*param_2);
      FUN_ffff00009077de54(s_major_version__0x_x_ffff0000907e7336,*(undefined2 *)(param_2 + 1));
      FUN_ffff00009077de54
                (s_minor_version__0x_x_ffff0000907df345,*(undefined2 *)((long)param_2 + 6));
      FUN_ffff00009077de54(s_file_hdr_sz___d_ffff0000907d0aa2,*(undefined2 *)(param_2 + 2));
      FUN_ffff00009077de54(s_chunk_hdr_sz___d_ffff0000907e0a55,*(undefined2 *)((long)param_2 + 10));
      FUN_ffff00009077de54(s_blk_sz___d_ffff0000907e734b,param_2[3]);
      FUN_ffff00009077de54(s_total_blks___d_ffff0000907ebdd1,param_2[4]);
      if (param_2[5] == 0) {
        uVar20 = 0;
        local_68 = 0;
      }
      else {
        uVar10 = (uint)uVar4;
        if (uVar10 < 0x1d) {
          uVar10 = 0x1c;
        }
        uVar20 = 0;
        uVar21 = 0;
        puVar19 = (ushort *)((long)param_2 + (ulong)uVar10);
        local_68 = 0;
        do {
          uVar10 = param_2[3];
          uVar11 = (ulong)uVar10;
          uVar4 = *(ushort *)((long)param_2 + 10);
          uVar9 = (ulong)*(uint *)(puVar19 + 2);
          lVar14 = 0;
          if (uVar11 != 0) {
            lVar14 = (long)uVar17 / (long)uVar11;
          }
          uVar15 = (uint)uVar4;
          if (uVar4 < 0xd) {
            uVar15 = 0xc;
          }
          if (lVar14 < (long)uVar9) {
            pcVar8 = s_sparse_chunk_size_is_too_big__ffff0000907f5b4d;
            goto LAB_ffff00009070e8bc;
          }
          uVar11 = (uVar20 + uVar9) * uVar11;
          if (uVar17 <= uVar11 && uVar11 - uVar17 != 0) {
            pcVar8 = s_sparse_chunk_size_overflow__ffff0000907f7a07;
            goto LAB_ffff00009070e814;
          }
          if ((uint)param_2[4] - uVar20 < uVar9) {
            pcVar8 = s_sparse_chunk_blocks_bigger_than_t_ffff0000907e4122;
            goto LAB_ffff00009070e8bc;
          }
          if (3 < *puVar19 - 0xcac1) {
            pcVar8 = s_Unknown_chunk_type_ffff0000907ef2a0;
            goto LAB_ffff00009070e8bc;
          }
          puVar18 = (ushort *)((long)puVar19 + (ulong)uVar15);
          uVar15 = *(uint *)(puVar19 + 2) * uVar10;
          uVar11 = (ulong)uVar15;
          switch((uint)*puVar19) {
          case 0xcac1:
            if (*(int *)(puVar19 + 4) == uVar15 + uVar4) {
              FUN_ffff00009077de54(s_Raw__start_block_addr__0x_llx_ffff0000907ea565,uVar20);
              uVar9 = FUN_ffff00009077be7c(lVar7,puVar18,uVar20 * (uint)param_2[3],uVar11);
              if (uVar9 == uVar11) {
                if ((-1 < iVar5) && (DAT_ffff00009083dc80 != '\0')) {
                  if (uVar15 != 0) {
                    local_68 = ~local_68;
                    uVar9 = uVar11;
                    puVar13 = puVar18;
                    do {
                      uVar10 = (int)uVar9 - 1;
                      uVar9 = (ulong)uVar10;
                      local_68 = *(uint *)(&DAT_ffff000090803e00 +
                                          ((ulong)((byte)*puVar13 ^ local_68) & 0xff) * 4) ^
                                 local_68 >> 8;
                      puVar13 = (ushort *)((long)puVar13 + 1);
                    } while (uVar10 != 0);
                    local_68 = ~local_68;
                  }
                  FUN_ffff00009077de54
                            (s_chunk__d_sparsecrc_0x_x_type__x_ffff0000907fe8ac,uVar21,local_68,
                             0xcac1);
                }
                uVar10 = *(uint *)(puVar19 + 2);
                puVar18 = (ushort *)((long)puVar18 + uVar11);
                goto LAB_ffff00009070e9b0;
              }
              pcVar8 = s_flash_write_failure_ffff0000907d0a8e;
            }
            else {
              pcVar8 = s_Bogus_chunk_size_for_chunk_type_R_ffff0000907fe888;
            }
            goto LAB_ffff00009070e8bc;
          case 0xcac2:
            if (uVar4 + 4 != *(int *)(puVar19 + 4)) {
              pcVar8 = s_Bogus_chunk_size_for_chunk_type_F_ffff0000907d5779;
              goto LAB_ffff00009070e8bc;
            }
            if (_DAT_ffff00009083df98 == (byte *)0x0) {
              uVar23 = 0;
              if (uVar10 != 0) {
                uVar23 = 0x400000 / uVar10;
              }
              _DAT_ffff00009083df98 = (byte *)FUN_ffff00009077d294(0x80,uVar23 * uVar10);
              if (_DAT_ffff00009083df98 == (byte *)0x0) {
                pcVar8 = s_Malloc_failed_for__CHUNK_TYPE_FI_ffff0000907e4150;
                goto LAB_ffff00009070e8bc;
              }
              uVar10 = param_2[3];
            }
            pbVar12 = _DAT_ffff00009083df98;
            uVar23 = 0;
            if (uVar10 != 0) {
              uVar23 = 0x400000 / uVar10;
            }
            uVar23 = 0x400000 - uVar23 * uVar10;
            puVar19 = puVar18 + 2;
            uVar3 = *(undefined4 *)puVar18;
            if (uVar23 < 0x3ffffd) {
              uVar9 = (ulong)(0x400000 - uVar23 >> 2);
              if (uVar23 < 0x3ffff9) {
                uVar11 = 0;
                uVar16 = uVar9 & 0x3ffffffe;
                do {
                  pbVar1 = pbVar12 + uVar11 * 4;
                  uVar11 = uVar11 + 2;
                  *(undefined4 *)pbVar1 = uVar3;
                  *(undefined4 *)(pbVar1 + 4) = uVar3;
                } while (uVar16 != uVar11);
                if (uVar16 == uVar9) goto LAB_ffff00009070eba4;
              }
              else {
                uVar16 = 0;
              }
              lVar14 = uVar9 - uVar16;
              pbVar12 = pbVar12 + uVar16 * 4;
              do {
                lVar14 = lVar14 + -1;
                *(undefined4 *)pbVar12 = uVar3;
                pbVar12 = pbVar12 + 4;
              } while (lVar14 != 0);
            }
LAB_ffff00009070eba4:
            if (uVar10 <= uVar15) {
              uVar23 = 0;
              if (uVar10 != 0) {
                uVar23 = uVar15 / uVar10;
              }
              do {
                uVar10 = param_2[3];
                uVar15 = 0;
                if (uVar10 != 0) {
                  uVar15 = 0x400000 / uVar10;
                }
                uVar2 = uVar23;
                if (uVar15 <= uVar23) {
                  uVar2 = uVar15;
                }
                iVar6 = FUN_ffff00009077be7c
                                  (lVar7,_DAT_ffff00009083df98,uVar20 * uVar10,uVar2 * uVar10);
                if (param_2[3] * uVar2 != iVar6) {
                  pcVar8 = s_CHUNK_TYPE_FILL_flash_write_fail_ffff0000907d257e;
                  goto LAB_ffff00009070e8bc;
                }
                if ((-1 < iVar5) && (DAT_ffff00009083dc80 != '\0')) {
                  if (iVar6 != 0) {
                    local_68 = ~local_68;
                    pbVar12 = _DAT_ffff00009083df98;
                    do {
                      iVar6 = iVar6 + -1;
                      local_68 = *(uint *)(&DAT_ffff000090803e00 +
                                          ((ulong)(*pbVar12 ^ local_68) & 0xff) * 4) ^ local_68 >> 8
                      ;
                      pbVar12 = pbVar12 + 1;
                    } while (iVar6 != 0);
                    local_68 = ~local_68;
                  }
                  FUN_ffff00009077de54
                            (s_chunk__d_sparsecrc_0x_x_type__x_ffff0000907fe8ac,uVar21,local_68,
                             0xcac2);
                }
                uVar20 = uVar20 + uVar2;
                uVar23 = uVar23 - uVar2;
              } while (uVar23 != 0);
              if (_DAT_ffff00009083df98 == (byte *)0x0) break;
            }
            thunk_FUN_ffff0000907c2ad8();
            _DAT_ffff00009083df98 = (byte *)0x0;
            break;
          case 0xcac3:
            FUN_ffff00009077de54(s___Blank__start__0x_llx_offset__0_ffff0000907dd888,uVar20);
            uVar10 = *(uint *)(puVar19 + 2);
LAB_ffff00009070e9b0:
            uVar20 = uVar20 + uVar10;
            puVar19 = puVar18;
            break;
          case 0xcac4:
            if (uVar4 + 4 != *(int *)(puVar19 + 4)) {
              pcVar8 = s_Bogus_chunk_size_for_chunk_type_D_ffff0000907fe8cd;
              goto LAB_ffff00009070e8bc;
            }
            puVar19 = (ushort *)((long)puVar18 + uVar11);
            uVar20 = uVar20 + uVar9;
          }
          uVar21 = uVar21 + 1;
        } while (uVar21 < (uint)param_2[5]);
      }
      FUN_ffff00009077de54(s_Wrote_0x_llx_blocks__expected_to_ffff0000907d0ab3,uVar20,param_2[4]);
      if (uVar20 == (uint)param_2[4]) {
        if (((iVar5 < 0) || (DAT_ffff00009083dc80 == '\0')) ||
           (iVar5 = FUN_ffff00009070dd74(iVar5,local_68), iVar5 == 0)) {
          iVar5 = FUN_ffff00009077ede4(param_1,s_userdata_ffff0000907ea542);
          if (iVar5 == 0) {
            FUN_ffff000090709acc(&DAT_ffff0000907d3de5,&DAT_ffff0000907f0b6f);
          }
          FUN_ffff000090709d50(&DAT_ffff0000907df27e);
          goto LAB_ffff00009070e8c0;
        }
        pcVar8 = s_failed_to_check_sparse_crc_ffff0000907d579e;
      }
      else {
        pcVar8 = s_sparse_image_write_failure_ffff0000907ef2b3;
      }
    }
  }
  else {
    pcVar8 = s_sparse_image_size_span_overflow__ffff0000907e7315;
  }
LAB_ffff00009070e8bc:
  FUN_ffff000090709d10(pcVar8);
LAB_ffff00009070e8c0:
  FUN_ffff00009077bce4(lVar7);
  return;
}



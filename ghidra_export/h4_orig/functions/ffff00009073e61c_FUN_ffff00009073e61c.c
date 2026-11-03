// ffff00009073e61c  size=1520  FUN_ffff00009073e61c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073e61c(long *param_1,uint *param_2)

{
  bool bVar1;
  undefined4 uVar2;
  uint uVar3;
  long lVar4;
  int iVar5;
  long lVar6;
  char *pcVar7;
  undefined8 uVar8;
  uint uVar9;
  uint uVar10;
  uint uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  long lVar15;
  uint *puVar16;
  char cVar17;
  
  *param_2 = 0;
  if (_DAT_ffff00009086f1d0 == 0) {
    uVar8 = 0;
  }
  else {
    lVar6 = FUN_ffff00009077d514(0xa000);
    *param_1 = lVar6;
    uVar8 = 0;
    if (lVar6 != 0) {
      FUN_ffff00009077ee50(lVar6,0,0xa000);
      lVar6 = *param_1;
      if (_DAT_ffff00009086f1d0 != 0) {
        uVar14 = 0;
        do {
          lVar4 = uVar14 * 0x30;
          lVar15 = lVar4 + -0xffff6f790e28;
          puVar16 = *(uint **)(&DAT_ffff00009086f1f8 + lVar4);
          uVar2 = *(undefined4 *)(&DAT_ffff00009086f200 + lVar4);
          uVar3 = *puVar16;
          iVar5 = FUN_ffff00009077f6fc
                            (lVar6 + (int)*param_2,0xa000 - (long)(int)*param_2,
                             s_____________________________s_Tr_ffff0000907dade9,lVar15);
          uVar11 = *param_2;
          uVar9 = (uint)((long)(int)uVar11 + (long)iVar5);
          *param_2 = uVar9;
          iVar5 = FUN_ffff00009077f6fc
                            (lVar6 + (long)(int)uVar11 + (long)iVar5,(long)(int)(0xa000 - uVar9),
                             s_Please_check_error_type_and_time_ffff0000907f4afb);
          uVar12 = (long)(int)*param_2 + (long)iVar5;
          *param_2 = (uint)uVar12;
          if ((uVar3 & 0xa000000) != 0) {
            if (lVar6 != 0) {
              uVar11 = *puVar16;
              pcVar7 = s__s_tracker_detects_AR_slave_erro_ffff0000907d4768;
              if (((uVar11 & 0x3001000) == 0x2001000) ||
                 (pcVar7 = s__s_tracker_detects_AR_decode_err_ffff0000907dc993,
                 ((uVar11 ^ 0xffffffff) & 0x3000000) == 0)) {
                iVar5 = FUN_ffff00009077f6fc
                                  (lVar6 + uVar12,(long)(int)(0xa000 - (uint)uVar12),pcVar7,lVar15);
                uVar12 = (ulong)(*param_2 + iVar5);
                *param_2 = *param_2 + iVar5;
              }
              uVar9 = (uint)uVar12;
              pcVar7 = s__s_tracker_detects_AW_slave_erro_ffff0000907d2e69;
              if (((uVar11 & 0xc002000) == 0x8002000) ||
                 (pcVar7 = s__s_tracker_detects_AW_decode_err_ffff0000907cf91d,
                 ((uVar11 ^ 0xffffffff) & 0xc000000) == 0)) {
                iVar5 = FUN_ffff00009077f6fc
                                  (lVar6 + (int)uVar9,(long)(int)(0xa000 - uVar9),pcVar7,lVar15);
                uVar9 = *param_2 + iVar5;
                *param_2 = uVar9;
              }
              if (_DAT_ffff00009086f298 == 0) {
                iVar5 = FUN_ffff00009073ec1c(lVar6,param_2,puVar16,uVar2,lVar15);
                if (-1 < iVar5) {
                  bVar1 = true;
                  goto LAB_ffff00009073e8f8;
                }
                pcVar7 = s__s_tracker_slverr_entry_dump_fai_ffff0000907f1606;
              }
              else {
                pcVar7 = s__s_tracker_capture_AW_error_from_ffff0000907f15d6;
                if ((uVar11 & 0x10000000) != 0) {
                  pcVar7 = s__s_tracker_capture_AR_error_from_ffff0000907e1631;
                }
                iVar5 = FUN_ffff00009077f6fc
                                  (lVar6 + (int)uVar9,(long)(int)(0xa000 - uVar9),pcVar7,lVar15);
                uVar9 = *param_2;
                uVar10 = (uint)((long)(int)uVar9 + (long)iVar5);
                *param_2 = uVar10;
                uVar12 = *(ulong *)(puVar16 + 0x10);
                uVar13 = uVar12;
                if (uVar12 != 0) {
                  do {
                    uVar12 = uVar13 >> 1 ^ uVar12;
                    bVar1 = 1 < uVar13;
                    uVar13 = uVar13 >> 1;
                  } while (bVar1);
                  if (uVar12 != 0) {
                    iVar5 = FUN_ffff00009077f6fc
                                      (lVar6 + (long)(int)uVar9 + (long)iVar5,
                                       (long)(int)(0xa000 - uVar10),
                                       s__s_tracker_slverr_timestamp__0x__ffff0000908028f8,lVar15);
                    uVar12 = (long)(int)*param_2 + (long)iVar5;
                    *param_2 = (uint)uVar12;
                    if ((uVar11 >> 0xc & 1) != 0) {
                      uVar9 = puVar16[0x23];
                      iVar5 = FUN_ffff00009077f6fc
                                        (lVar6 + uVar12,(long)(int)(0xa000 - (uint)uVar12),
                                         s_read_capture__non_secure___0x_x__ffff0000907ff287,
                                         uVar9 >> 8 & 1,puVar16[0x22],
                                         *(undefined8 *)(puVar16 + 0x20),uVar9 >> 4 & 7,uVar9 & 0xf)
                      ;
                      uVar12 = (ulong)(*param_2 + iVar5);
                      *param_2 = *param_2 + iVar5;
                    }
                    if ((uVar11 >> 0xd & 1) == 0) goto LAB_ffff00009073e8cc;
                    uVar11 = puVar16[0x27];
                    iVar5 = FUN_ffff00009077f6fc
                                      (lVar6 + (int)uVar12,(long)(0xa000 - (int)uVar12),
                                       s_write_capture__non_secure___0x_x_ffff0000907fbac0,
                                       uVar11 >> 8 & 1,puVar16[0x26],*(undefined8 *)(puVar16 + 0x24)
                                       ,uVar11 >> 4 & 7,uVar11 & 0xf);
                    uVar11 = *param_2 + iVar5;
                    *param_2 = uVar11;
                    goto joined_r0xffff00009073e98c;
                  }
                }
                pcVar7 = s_Get__s_tracker_slverr_timestamp_f_ffff0000907fd6a0;
              }
              FUN_ffff00009077e11c(pcVar7,lVar15);
            }
            FUN_ffff00009077e11c(s_SLVERR_capture_msg_get_fail__ffff0000908028da);
            break;
          }
LAB_ffff00009073e8cc:
          uVar11 = (uint)uVar12;
joined_r0xffff00009073e98c:
          if (lVar6 == 0) {
            iVar5 = FUN_ffff00009077f6fc
                              ((long)(int)uVar11,(long)(int)(0xa000 - uVar11),
                               s_Stall_timeout_detected__valid___1_ffff0000907dae2d);
            bVar1 = false;
LAB_ffff00009073eb20:
            *param_2 = *param_2 + iVar5;
            if (bVar1) goto LAB_ffff00009073eb38;
LAB_ffff00009073eb30:
            if ((uVar3 & 0x300) == 0) goto LAB_ffff00009073eb38;
            iVar5 = FUN_ffff00009073ec1c(lVar6,param_2,puVar16,uVar2,lVar15);
            if (iVar5 < 0) {
              FUN_ffff00009077e11c(s__s_tracker_timeout_entry_dump_fa_ffff0000907fba9b,lVar15);
              break;
            }
          }
          else {
            bVar1 = false;
LAB_ffff00009073e8f8:
            uVar11 = puVar16[10];
            if ((_DAT_ffff00009086f29c != 0) && ((uVar11 >> 0x10 & 1) != 0)) {
              iVar5 = FUN_ffff00009077f6fc
                                (lVar6 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_write_timeou_ffff0000907f2e2c,lVar15);
              uVar9 = *param_2;
              pcVar7 = s_Stall_timeout_detected_raw_0x_x__ffff0000907dae5f;
LAB_ffff00009073eb04:
              uVar9 = uVar9 + iVar5;
              *param_2 = uVar9;
LAB_ffff00009073eb0c:
              iVar5 = FUN_ffff00009077f6fc
                                (lVar6 + (int)uVar9,(long)(int)(0xa000 - uVar9),pcVar7,uVar11);
              goto LAB_ffff00009073eb20;
            }
            cVar17 = (uVar11 >> 6 & 1) != 0;
            if ((bool)cVar17) {
              iVar5 = FUN_ffff00009077f6fc
                                (lVar6 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_read_timeout_ffff0000907d6305,lVar15);
              *param_2 = *param_2 + iVar5;
            }
            if ((uVar11 >> 0xe & 1) != 0) {
              iVar5 = FUN_ffff00009077f6fc
                                (lVar6 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_write_timeou_ffff0000907e4c1e,lVar15);
              cVar17 = cVar17 + '\x01';
              *param_2 = *param_2 + iVar5;
            }
            if ((uVar11 >> 0xd & 1) != 0) {
              iVar5 = FUN_ffff00009077f6fc
                                (lVar6 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_write_timeou_ffff0000907dfb20,lVar15);
              cVar17 = cVar17 + '\x01';
              *param_2 = *param_2 + iVar5;
              if ((uVar11 >> 0x10 & 1) != 0) goto LAB_ffff00009073ea68;
LAB_ffff00009073ea18:
              if ((uVar11 >> 7 & 1) == 0) goto LAB_ffff00009073ea1c;
LAB_ffff00009073ea98:
              iVar5 = FUN_ffff00009077f6fc
                                (lVar6 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_read_timeout_ffff0000907e2f2b,lVar15);
              pcVar7 = s_Stall_timeout_detected_raw_0x_x__ffff0000907f65ea;
              uVar9 = *param_2 + iVar5;
              *param_2 = uVar9;
              if ((uVar11 >> 0xf & 1) == 0) goto LAB_ffff00009073eb0c;
LAB_ffff00009073eadc:
              iVar5 = FUN_ffff00009077f6fc
                                (lVar6 + (int)uVar9,(long)(int)(0xa000 - uVar9),
                                 s__s_tracker_occurred_write_timeou_ffff0000907ec7a6,lVar15);
              uVar9 = *param_2;
              pcVar7 = s_Stall_timeout_detected_raw_0x_x__ffff0000907f65ea;
              goto LAB_ffff00009073eb04;
            }
            if ((uVar11 >> 0x10 & 1) == 0) goto LAB_ffff00009073ea18;
LAB_ffff00009073ea68:
            iVar5 = FUN_ffff00009077f6fc
                              (lVar6 + (int)*param_2,0xa000 - (long)(int)*param_2,
                               s__s_tracker_occurred_write_timeou_ffff0000907f2e2c,lVar15);
            cVar17 = '\x01';
            *param_2 = *param_2 + iVar5;
            if ((uVar11 >> 7 & 1) != 0) goto LAB_ffff00009073ea98;
LAB_ffff00009073ea1c:
            if ((uVar11 >> 0xf & 1) != 0) {
              uVar9 = *param_2;
              goto LAB_ffff00009073eadc;
            }
            if (cVar17 != '\0') {
              uVar9 = *param_2;
              pcVar7 = s_Stall_timeout_detected_raw_0x_x__ffff0000907f65ea;
              goto LAB_ffff00009073eb0c;
            }
            if (!bVar1) goto LAB_ffff00009073eb30;
LAB_ffff00009073eb38:
            if (uVar3 == 0) {
              iVar5 = FUN_ffff00009077f6fc
                                (lVar6 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_detect_no_any_problem_ffff0000907f65c7,lVar15);
              *param_2 = *param_2 + iVar5;
            }
          }
          uVar14 = uVar14 + 1;
        } while (uVar14 < _DAT_ffff00009086f1d0);
      }
      uVar8 = FUN_ffff00009077f250(lVar6);
      uVar3 = *param_2;
      if (((int)uVar8 < 0) || (0xa000 < (int)uVar3)) {
        if (0x9fff < (int)uVar3) {
          uVar3 = 0xa000;
        }
        *param_2 = uVar3;
      }
      else {
        uVar8 = 1;
      }
    }
  }
  return uVar8;
}



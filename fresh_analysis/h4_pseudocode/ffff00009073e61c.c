
undefined8 FUN_ffff00009073e61c(long *param_1,uint *param_2)

{
  bool bVar1;
  undefined4 uVar2;
  uint uVar3;
  int iVar4;
  long lVar5;
  char *pcVar6;
  undefined8 uVar7;
  uint uVar8;
  uint uVar9;
  uint uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  undefined *puVar14;
  uint *puVar15;
  char cVar16;
  
  *param_2 = 0;
  if (DAT_ffff00009086f1d0 == 0) {
    uVar7 = 0;
  }
  else {
    lVar5 = FUN_ffff00009077d514(0xa000);
    *param_1 = lVar5;
    uVar7 = 0;
    if (lVar5 != 0) {
      FUN_ffff00009077ee50(lVar5,0,0xa000);
      lVar5 = *param_1;
      if (DAT_ffff00009086f1d0 != 0) {
        uVar13 = 0;
        do {
          puVar14 = &DAT_ffff00009086f1d8 + uVar13 * 0x30;
          puVar15 = (uint *)(&DAT_ffff00009086f1f8)[uVar13 * 6];
          uVar2 = (&DAT_ffff00009086f200)[uVar13 * 0xc];
          uVar3 = *puVar15;
          iVar4 = FUN_ffff00009077f6fc
                            (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                             s_____________________________s_Tr_ffff0000907dade9,puVar14);
          uVar10 = *param_2;
          uVar8 = (uint)((long)(int)uVar10 + (long)iVar4);
          *param_2 = uVar8;
          iVar4 = FUN_ffff00009077f6fc
                            (lVar5 + (long)(int)uVar10 + (long)iVar4,(long)(int)(0xa000 - uVar8),
                             s_Please_check_error_type_and_time_ffff0000907f4afb);
          uVar11 = (long)(int)*param_2 + (long)iVar4;
          *param_2 = (uint)uVar11;
          if ((uVar3 & 0xa000000) != 0) {
            if (lVar5 != 0) {
              uVar10 = *puVar15;
              pcVar6 = s__s_tracker_detects_AR_slave_erro_ffff0000907d4768;
              if (((uVar10 & 0x3001000) == 0x2001000) ||
                 (pcVar6 = s__s_tracker_detects_AR_decode_err_ffff0000907dc993,
                 ((uVar10 ^ 0xffffffff) & 0x3000000) == 0)) {
                iVar4 = FUN_ffff00009077f6fc
                                  (lVar5 + uVar11,(long)(int)(0xa000 - (uint)uVar11),pcVar6,puVar14)
                ;
                uVar11 = (ulong)(*param_2 + iVar4);
                *param_2 = *param_2 + iVar4;
              }
              uVar8 = (uint)uVar11;
              pcVar6 = s__s_tracker_detects_AW_slave_erro_ffff0000907d2e69;
              if (((uVar10 & 0xc002000) == 0x8002000) ||
                 (pcVar6 = s__s_tracker_detects_AW_decode_err_ffff0000907cf91d,
                 ((uVar10 ^ 0xffffffff) & 0xc000000) == 0)) {
                iVar4 = FUN_ffff00009077f6fc
                                  (lVar5 + (int)uVar8,(long)(int)(0xa000 - uVar8),pcVar6,puVar14);
                uVar8 = *param_2 + iVar4;
                *param_2 = uVar8;
              }
              if (DAT_ffff00009086f298 == 0) {
                iVar4 = FUN_ffff00009073ec1c(lVar5,param_2,puVar15,uVar2,puVar14);
                if (-1 < iVar4) {
                  bVar1 = true;
                  goto LAB_ffff00009073e8f8;
                }
                pcVar6 = s__s_tracker_slverr_entry_dump_fai_ffff0000907f1606;
              }
              else {
                pcVar6 = s__s_tracker_capture_AW_error_from_ffff0000907f15d6;
                if ((uVar10 & 0x10000000) != 0) {
                  pcVar6 = s__s_tracker_capture_AR_error_from_ffff0000907e1631;
                }
                iVar4 = FUN_ffff00009077f6fc
                                  (lVar5 + (int)uVar8,(long)(int)(0xa000 - uVar8),pcVar6,puVar14);
                uVar8 = *param_2;
                uVar9 = (uint)((long)(int)uVar8 + (long)iVar4);
                *param_2 = uVar9;
                uVar11 = *(ulong *)(puVar15 + 0x10);
                uVar12 = uVar11;
                if (uVar11 != 0) {
                  do {
                    uVar11 = uVar12 >> 1 ^ uVar11;
                    bVar1 = 1 < uVar12;
                    uVar12 = uVar12 >> 1;
                  } while (bVar1);
                  if (uVar11 != 0) {
                    iVar4 = FUN_ffff00009077f6fc
                                      (lVar5 + (long)(int)uVar8 + (long)iVar4,
                                       (long)(int)(0xa000 - uVar9),
                                       s__s_tracker_slverr_timestamp__0x__ffff0000908028f8,puVar14);
                    uVar11 = (long)(int)*param_2 + (long)iVar4;
                    *param_2 = (uint)uVar11;
                    if ((uVar10 >> 0xc & 1) != 0) {
                      uVar8 = puVar15[0x23];
                      iVar4 = FUN_ffff00009077f6fc
                                        (lVar5 + uVar11,(long)(int)(0xa000 - (uint)uVar11),
                                         s_read_capture__non_secure___0x_x__ffff0000907ff287,
                                         uVar8 >> 8 & 1,puVar15[0x22],
                                         *(undefined8 *)(puVar15 + 0x20),uVar8 >> 4 & 7,uVar8 & 0xf)
                      ;
                      uVar11 = (ulong)(*param_2 + iVar4);
                      *param_2 = *param_2 + iVar4;
                    }
                    if ((uVar10 >> 0xd & 1) == 0) goto LAB_ffff00009073e8cc;
                    uVar10 = puVar15[0x27];
                    iVar4 = FUN_ffff00009077f6fc
                                      (lVar5 + (int)uVar11,(long)(0xa000 - (int)uVar11),
                                       s_write_capture__non_secure___0x_x_ffff0000907fbac0,
                                       uVar10 >> 8 & 1,puVar15[0x26],*(undefined8 *)(puVar15 + 0x24)
                                       ,uVar10 >> 4 & 7,uVar10 & 0xf);
                    uVar10 = *param_2 + iVar4;
                    *param_2 = uVar10;
                    goto joined_r0xffff00009073e98c;
                  }
                }
                pcVar6 = s_Get__s_tracker_slverr_timestamp_f_ffff0000907fd6a0;
              }
              FUN_ffff00009077e11c(pcVar6,puVar14);
            }
            FUN_ffff00009077e11c(s_SLVERR_capture_msg_get_fail__ffff0000908028da);
            break;
          }
LAB_ffff00009073e8cc:
          uVar10 = (uint)uVar11;
joined_r0xffff00009073e98c:
          if (lVar5 == 0) {
            iVar4 = FUN_ffff00009077f6fc
                              ((long)(int)uVar10,(long)(int)(0xa000 - uVar10),
                               s_Stall_timeout_detected__valid___1_ffff0000907dae2d);
            bVar1 = false;
LAB_ffff00009073eb20:
            *param_2 = *param_2 + iVar4;
            if (bVar1) goto LAB_ffff00009073eb38;
LAB_ffff00009073eb30:
            if ((uVar3 & 0x300) == 0) goto LAB_ffff00009073eb38;
            iVar4 = FUN_ffff00009073ec1c(lVar5,param_2,puVar15,uVar2,puVar14);
            if (iVar4 < 0) {
              FUN_ffff00009077e11c(s__s_tracker_timeout_entry_dump_fa_ffff0000907fba9b,puVar14);
              break;
            }
          }
          else {
            bVar1 = false;
LAB_ffff00009073e8f8:
            uVar10 = puVar15[10];
            if ((DAT_ffff00009086f29c != 0) && ((uVar10 >> 0x10 & 1) != 0)) {
              iVar4 = FUN_ffff00009077f6fc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_write_timeou_ffff0000907f2e2c,puVar14);
              uVar8 = *param_2;
              pcVar6 = s_Stall_timeout_detected_raw_0x_x__ffff0000907dae5f;
LAB_ffff00009073eb04:
              uVar8 = uVar8 + iVar4;
              *param_2 = uVar8;
LAB_ffff00009073eb0c:
              iVar4 = FUN_ffff00009077f6fc
                                (lVar5 + (int)uVar8,(long)(int)(0xa000 - uVar8),pcVar6,uVar10);
              goto LAB_ffff00009073eb20;
            }
            cVar16 = (uVar10 >> 6 & 1) != 0;
            if ((bool)cVar16) {
              iVar4 = FUN_ffff00009077f6fc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_read_timeout_ffff0000907d6305,puVar14);
              *param_2 = *param_2 + iVar4;
            }
            if ((uVar10 >> 0xe & 1) != 0) {
              iVar4 = FUN_ffff00009077f6fc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_write_timeou_ffff0000907e4c1e,puVar14);
              cVar16 = cVar16 + '\x01';
              *param_2 = *param_2 + iVar4;
            }
            if ((uVar10 >> 0xd & 1) != 0) {
              iVar4 = FUN_ffff00009077f6fc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_write_timeou_ffff0000907dfb20,puVar14);
              cVar16 = cVar16 + '\x01';
              *param_2 = *param_2 + iVar4;
              if ((uVar10 >> 0x10 & 1) != 0) goto LAB_ffff00009073ea68;
LAB_ffff00009073ea18:
              if ((uVar10 >> 7 & 1) == 0) goto LAB_ffff00009073ea1c;
LAB_ffff00009073ea98:
              iVar4 = FUN_ffff00009077f6fc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_read_timeout_ffff0000907e2f2b,puVar14);
              pcVar6 = s_Stall_timeout_detected_raw_0x_x__ffff0000907f65ea;
              uVar8 = *param_2 + iVar4;
              *param_2 = uVar8;
              if ((uVar10 >> 0xf & 1) == 0) goto LAB_ffff00009073eb0c;
LAB_ffff00009073eadc:
              iVar4 = FUN_ffff00009077f6fc
                                (lVar5 + (int)uVar8,(long)(int)(0xa000 - uVar8),
                                 s__s_tracker_occurred_write_timeou_ffff0000907ec7a6,puVar14);
              uVar8 = *param_2;
              pcVar6 = s_Stall_timeout_detected_raw_0x_x__ffff0000907f65ea;
              goto LAB_ffff00009073eb04;
            }
            if ((uVar10 >> 0x10 & 1) == 0) goto LAB_ffff00009073ea18;
LAB_ffff00009073ea68:
            iVar4 = FUN_ffff00009077f6fc
                              (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                               s__s_tracker_occurred_write_timeou_ffff0000907f2e2c,puVar14);
            cVar16 = '\x01';
            *param_2 = *param_2 + iVar4;
            if ((uVar10 >> 7 & 1) != 0) goto LAB_ffff00009073ea98;
LAB_ffff00009073ea1c:
            if ((uVar10 >> 0xf & 1) != 0) {
              uVar8 = *param_2;
              goto LAB_ffff00009073eadc;
            }
            if (cVar16 != '\0') {
              uVar8 = *param_2;
              pcVar6 = s_Stall_timeout_detected_raw_0x_x__ffff0000907f65ea;
              goto LAB_ffff00009073eb0c;
            }
            if (!bVar1) goto LAB_ffff00009073eb30;
LAB_ffff00009073eb38:
            if (uVar3 == 0) {
              iVar4 = FUN_ffff00009077f6fc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_detect_no_any_problem_ffff0000907f65c7,puVar14);
              *param_2 = *param_2 + iVar4;
            }
          }
          uVar13 = uVar13 + 1;
        } while (uVar13 < DAT_ffff00009086f1d0);
      }
      uVar7 = FUN_ffff00009077f250(lVar5);
      uVar3 = *param_2;
      if (((int)uVar7 < 0) || (0xa000 < (int)uVar3)) {
        if (0x9fff < (int)uVar3) {
          uVar3 = 0xa000;
        }
        *param_2 = uVar3;
      }
      else {
        uVar7 = 1;
      }
    }
  }
  return uVar7;
}



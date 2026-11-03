
undefined8 FUN_ffff0000be02e88c(long *param_1,uint *param_2)

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
  if (DAT_ffff0000be119498 == 0) {
    uVar7 = 0;
  }
  else {
    lVar5 = FUN_ffff0000be05f1bc(0xa000);
    *param_1 = lVar5;
    uVar7 = 0;
    if (lVar5 != 0) {
      FUN_ffff0000be060ae0(lVar5,0,0xa000);
      lVar5 = *param_1;
      if (DAT_ffff0000be119498 != 0) {
        uVar13 = 0;
        do {
          puVar14 = &DAT_ffff0000be1194a0 + uVar13 * 0x30;
          puVar15 = (uint *)(&DAT_ffff0000be1194c0)[uVar13 * 6];
          uVar2 = (&DAT_ffff0000be1194c8)[uVar13 * 0xc];
          uVar3 = *puVar15;
          iVar4 = FUN_ffff0000be0611dc
                            (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                             s_____________________________s_Tr_ffff0000be0ab6e8,puVar14);
          uVar10 = *param_2;
          uVar8 = (uint)((long)(int)uVar10 + (long)iVar4);
          *param_2 = uVar8;
          iVar4 = FUN_ffff0000be0611dc
                            (lVar5 + (long)(int)uVar10 + (long)iVar4,(long)(int)(0xa000 - uVar8),
                             s_Please_check_error_type_and_time_ffff0000be0bd2c8);
          uVar11 = (long)(int)*param_2 + (long)iVar4;
          *param_2 = (uint)uVar11;
          if ((uVar3 & 0xa000000) != 0) {
            if (lVar5 != 0) {
              uVar10 = *puVar15;
              pcVar6 = s__s_tracker_detects_AR_slave_erro_ffff0000be0a6ec7;
              if (((uVar10 & 0x3001000) == 0x2001000) ||
                 (pcVar6 = s__s_tracker_detects_AR_decode_err_ffff0000be0ac91a,
                 ((uVar10 ^ 0xffffffff) & 0x3000000) == 0)) {
                iVar4 = FUN_ffff0000be0611dc
                                  (lVar5 + uVar11,(long)(int)(0xa000 - (uint)uVar11),pcVar6,puVar14)
                ;
                uVar11 = (ulong)(*param_2 + iVar4);
                *param_2 = *param_2 + iVar4;
              }
              uVar8 = (uint)uVar11;
              pcVar6 = s__s_tracker_detects_AW_slave_erro_ffff0000be0a5c51;
              if (((uVar10 & 0xc002000) == 0x8002000) ||
                 (pcVar6 = s__s_tracker_detects_AW_decode_err_ffff0000be0a3539,
                 ((uVar10 ^ 0xffffffff) & 0xc000000) == 0)) {
                iVar4 = FUN_ffff0000be0611dc
                                  (lVar5 + (int)uVar8,(long)(int)(0xa000 - uVar8),pcVar6,puVar14);
                uVar8 = *param_2 + iVar4;
                *param_2 = uVar8;
              }
              if (DAT_ffff0000be119560 == 0) {
                iVar4 = FUN_ffff0000be02ee8c(lVar5,param_2,puVar15,uVar2,puVar14);
                if (-1 < iVar4) {
                  bVar1 = true;
                  goto LAB_ffff0000be02eb68;
                }
                pcVar6 = s__s_tracker_slverr_entry_dump_fai_ffff0000be0baf1c;
              }
              else {
                pcVar6 = s__s_tracker_capture_AW_error_from_ffff0000be0baeec;
                if ((uVar10 & 0x10000000) != 0) {
                  pcVar6 = s__s_tracker_capture_AR_error_from_ffff0000be0afb5a;
                }
                iVar4 = FUN_ffff0000be0611dc
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
                    iVar4 = FUN_ffff0000be0611dc
                                      (lVar5 + (long)(int)uVar8 + (long)iVar4,
                                       (long)(int)(0xa000 - uVar9),
                                       s__s_tracker_slverr_timestamp__0x__ffff0000be0c6435,puVar14);
                    uVar11 = (long)(int)*param_2 + (long)iVar4;
                    *param_2 = (uint)uVar11;
                    if ((uVar10 >> 0xc & 1) != 0) {
                      uVar8 = puVar15[0x23];
                      iVar4 = FUN_ffff0000be0611dc
                                        (lVar5 + uVar11,(long)(int)(0xa000 - (uint)uVar11),
                                         s_read_capture__non_secure___0x_x__ffff0000be0c40b7,
                                         uVar8 >> 8 & 1,puVar15[0x22],
                                         *(undefined8 *)(puVar15 + 0x20),uVar8 >> 4 & 7,uVar8 & 0xf)
                      ;
                      uVar11 = (ulong)(*param_2 + iVar4);
                      *param_2 = *param_2 + iVar4;
                    }
                    if ((uVar10 >> 0xd & 1) == 0) goto LAB_ffff0000be02eb3c;
                    uVar10 = puVar15[0x27];
                    iVar4 = FUN_ffff0000be0611dc
                                      (lVar5 + (int)uVar11,(long)(0xa000 - (int)uVar11),
                                       s_write_capture__non_secure___0x_x_ffff0000be0c1ce6,
                                       uVar10 >> 8 & 1,puVar15[0x26],*(undefined8 *)(puVar15 + 0x24)
                                       ,uVar10 >> 4 & 7,uVar10 & 0xf);
                    uVar10 = *param_2 + iVar4;
                    *param_2 = uVar10;
                    goto joined_r0xffff0000be02ebfc;
                  }
                }
                pcVar6 = s_Get__s_tracker_slverr_timestamp_f_ffff0000be0c2dd6;
              }
              FUN_ffff0000be05fd98(pcVar6,puVar14);
            }
            FUN_ffff0000be05fd98(s_SLVERR_capture_msg_get_fail__ffff0000be0c6417);
            break;
          }
LAB_ffff0000be02eb3c:
          uVar10 = (uint)uVar11;
joined_r0xffff0000be02ebfc:
          if (lVar5 == 0) {
            iVar4 = FUN_ffff0000be0611dc
                              ((long)(int)uVar10,(long)(int)(0xa000 - uVar10),
                               s_Stall_timeout_detected__valid___1_ffff0000be0ab72c);
            bVar1 = false;
LAB_ffff0000be02ed90:
            *param_2 = *param_2 + iVar4;
            if (bVar1) goto LAB_ffff0000be02eda8;
LAB_ffff0000be02eda0:
            if ((uVar3 & 0x300) == 0) goto LAB_ffff0000be02eda8;
            iVar4 = FUN_ffff0000be02ee8c(lVar5,param_2,puVar15,uVar2,puVar14);
            if (iVar4 < 0) {
              FUN_ffff0000be05fd98(s__s_tracker_timeout_entry_dump_fa_ffff0000be0c1cc1,puVar14);
              break;
            }
          }
          else {
            bVar1 = false;
LAB_ffff0000be02eb68:
            uVar10 = puVar15[10];
            if ((DAT_ffff0000be119564 != 0) && ((uVar10 >> 0x10 & 1) != 0)) {
              iVar4 = FUN_ffff0000be0611dc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_write_timeou_ffff0000be0bbf4f,puVar14);
              uVar8 = *param_2;
              pcVar6 = s_Stall_timeout_detected_raw_0x_x__ffff0000be0ab75e;
LAB_ffff0000be02ed74:
              uVar8 = uVar8 + iVar4;
              *param_2 = uVar8;
LAB_ffff0000be02ed7c:
              iVar4 = FUN_ffff0000be0611dc
                                (lVar5 + (int)uVar8,(long)(int)(0xa000 - uVar8),pcVar6,uVar10);
              goto LAB_ffff0000be02ed90;
            }
            cVar16 = (uVar10 >> 6 & 1) != 0;
            if ((bool)cVar16) {
              iVar4 = FUN_ffff0000be0611dc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_read_timeout_ffff0000be0a8038,puVar14);
              *param_2 = *param_2 + iVar4;
            }
            if ((uVar10 >> 0xe & 1) != 0) {
              iVar4 = FUN_ffff0000be0611dc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_write_timeou_ffff0000be0b1f30,puVar14);
              cVar16 = cVar16 + '\x01';
              *param_2 = *param_2 + iVar4;
            }
            if ((uVar10 >> 0xd & 1) != 0) {
              iVar4 = FUN_ffff0000be0611dc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_write_timeou_ffff0000be0ae86f,puVar14);
              cVar16 = cVar16 + '\x01';
              *param_2 = *param_2 + iVar4;
              if ((uVar10 >> 0x10 & 1) != 0) goto LAB_ffff0000be02ecd8;
LAB_ffff0000be02ec88:
              if ((uVar10 >> 7 & 1) == 0) goto LAB_ffff0000be02ec8c;
LAB_ffff0000be02ed08:
              iVar4 = FUN_ffff0000be0611dc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_occurred_read_timeout_ffff0000be0b0ce1,puVar14);
              pcVar6 = s_Stall_timeout_detected_raw_0x_x__ffff0000be0be42d;
              uVar8 = *param_2 + iVar4;
              *param_2 = uVar8;
              if ((uVar10 >> 0xf & 1) == 0) goto LAB_ffff0000be02ed7c;
LAB_ffff0000be02ed4c:
              iVar4 = FUN_ffff0000be0611dc
                                (lVar5 + (int)uVar8,(long)(int)(0xa000 - uVar8),
                                 s__s_tracker_occurred_write_timeou_ffff0000be0b7841,puVar14);
              uVar8 = *param_2;
              pcVar6 = s_Stall_timeout_detected_raw_0x_x__ffff0000be0be42d;
              goto LAB_ffff0000be02ed74;
            }
            if ((uVar10 >> 0x10 & 1) == 0) goto LAB_ffff0000be02ec88;
LAB_ffff0000be02ecd8:
            iVar4 = FUN_ffff0000be0611dc
                              (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                               s__s_tracker_occurred_write_timeou_ffff0000be0bbf4f,puVar14);
            cVar16 = '\x01';
            *param_2 = *param_2 + iVar4;
            if ((uVar10 >> 7 & 1) != 0) goto LAB_ffff0000be02ed08;
LAB_ffff0000be02ec8c:
            if ((uVar10 >> 0xf & 1) != 0) {
              uVar8 = *param_2;
              goto LAB_ffff0000be02ed4c;
            }
            if (cVar16 != '\0') {
              uVar8 = *param_2;
              pcVar6 = s_Stall_timeout_detected_raw_0x_x__ffff0000be0be42d;
              goto LAB_ffff0000be02ed7c;
            }
            if (!bVar1) goto LAB_ffff0000be02eda0;
LAB_ffff0000be02eda8:
            if (uVar3 == 0) {
              iVar4 = FUN_ffff0000be0611dc
                                (lVar5 + (int)*param_2,0xa000 - (long)(int)*param_2,
                                 s__s_tracker_detect_no_any_problem_ffff0000be0be40a,puVar14);
              *param_2 = *param_2 + iVar4;
            }
          }
          uVar13 = uVar13 + 1;
        } while (uVar13 < DAT_ffff0000be119498);
      }
      uVar7 = FUN_ffff0000be060e5c(lVar5);
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



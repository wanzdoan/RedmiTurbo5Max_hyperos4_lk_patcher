
undefined8 FUN_ffff0000be02b37c(long *param_1,uint *param_2)

{
  char cVar1;
  int iVar2;
  undefined8 uVar3;
  long lVar4;
  uint uVar5;
  uint uVar6;
  ulong uVar7;
  ulong uVar8;
  uint uVar9;
  long lVar10;
  
  uVar3 = 0xffffffff;
  if ((param_1 != (long *)0x0) && (param_2 != (uint *)0x0)) {
    *param_2 = 0;
    lVar4 = FUN_ffff0000be05f1bc(0x1000);
    *param_1 = lVar4;
    if (lVar4 == 0) {
      uVar3 = 0xffffffff;
    }
    else {
      iVar2 = FUN_ffff0000be0611dc
                        (lVar4 + (int)*param_2,0x1000 - (long)(int)*param_2,
                         s___________________________Debug_E_ffff0000be0b0c1e);
      uVar6 = *param_2 + iVar2;
      *param_2 = uVar6;
      if (DAT_ffff0000be118194 != 0) {
        uVar9 = 0;
        do {
          if (uVar9 == 0) {
            uVar7 = (ulong)DAT_ffff0000be11819c._4_4_ | 0xffff000000000000;
            if (DAT_ffff0000be118198 < 3) {
              iVar2 = FUN_ffff0000be0611dc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 s_status0___0x_x__status1___0x_x__t_ffff0000be0c09b9,
                                 *(undefined4 *)(uVar7 + DAT_ffff0000be1181ac),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000be1181b0),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000be1181b8),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000be1181b4),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000be1181bc));
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff0000be0611dc
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_err_flag_status0_decode_____ffff0000be0a348f);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff0000be0d67a8)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + DAT_ffff0000be1181ac) &
                    *(uint *)((long)&DAT_ffff0000be0d67ac + lVar10)) != 0)) {
                  iVar2 = FUN_ffff0000be0611dc
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000be0b52b6);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
              iVar2 = FUN_ffff0000be0611dc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000be0c1bcd);
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff0000be0611dc
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_err_flag_status1_decode_____ffff0000be0ab602);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff0000be0d67a8)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + DAT_ffff0000be1181b0) &
                    *(uint *)((long)&DAT_ffff0000be0d67ac + lVar10)) != 0)) {
                  iVar2 = FUN_ffff0000be0611dc
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000be0b52b6);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
            }
            else {
              iVar2 = FUN_ffff0000be0611dc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 s_wdt_status___0x_x__time_h___0x_x_ffff0000be0c5184,
                                 *(undefined4 *)(uVar7 + DAT_ffff0000be118338),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000be1181b8),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000be1181b4),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000be11833c));
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff0000be0611dc
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_soc_err_flag_wdt_status_decode___ffff0000be0b7767);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff0000be0d67a8)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + DAT_ffff0000be118338) &
                    *(uint *)((long)&DAT_ffff0000be0d67ac + lVar10)) != 0)) {
                  iVar2 = FUN_ffff0000be0611dc
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000be0b52b6);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
            }
          }
          else if (uVar9 == 1) {
            uVar8 = (ulong)DAT_ffff0000be118268._4_4_ | 0xffff000000000000;
            iVar2 = FUN_ffff0000be0611dc
                              (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                               s_wdt_status_lower32___0x_x__wdt_s_ffff0000be0b5250,
                               *(undefined4 *)(uVar8 + DAT_ffff0000be1182e8),
                               *(undefined4 *)(uVar8 + DAT_ffff0000be1182ec),
                               *(undefined4 *)(uVar8 + DAT_ffff0000be118330),
                               *(undefined4 *)(uVar8 + DAT_ffff0000be11832c),
                               *(undefined4 *)(uVar8 + DAT_ffff0000be1182f8));
            uVar6 = *param_2;
            uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
            *param_2 = uVar5;
            iVar2 = FUN_ffff0000be0611dc
                              (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                               s_vlp_err_flag_wdt_status_decode___ffff0000be0bd184);
            cVar1 = DAT_ffff0000be0d7180;
            uVar7 = (long)(int)*param_2 + (long)iVar2;
            *param_2 = (uint)uVar7;
            if ((cVar1 != '\0') &&
               ((*(uint *)(uVar8 + DAT_ffff0000be1182e8) & DAT_ffff0000be0d7184) != 0)) {
              iVar2 = FUN_ffff0000be0611dc
                                (lVar4 + uVar7,(long)(int)(0x1000 - (uint)uVar7),
                                 &DAT_ffff0000be0b52b6,
                                 s_soc_dbg_err_flag_wdt_irq_mask_ffff0000be0d7140);
              uVar7 = (ulong)(*param_2 + iVar2);
              *param_2 = *param_2 + iVar2;
            }
            uVar6 = (uint)uVar7;
            if ((DAT_ffff0000be0d71c8 != '\0') &&
               ((*(uint *)(uVar8 + DAT_ffff0000be1182e8) & DAT_ffff0000be0d71cc) != 0)) {
              iVar2 = FUN_ffff0000be0611dc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000be0b52b6,
                                 s_mfg2soc_dfd_smmu_trigger_event_m_ffff0000be0d7188);
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
            if ((DAT_ffff0000be0d7210 != '\0') &&
               ((*(uint *)(uVar8 + DAT_ffff0000be1182e8) & DAT_ffff0000be0d7214) != 0)) {
              iVar2 = FUN_ffff0000be0611dc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000be0b52b6,s_vlp_tracker_timeout_mask_ffff0000be0d71d0);
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
            if ((DAT_ffff0000be0d7258 != '\0') &&
               ((*(uint *)(uVar8 + DAT_ffff0000be1182e8) & DAT_ffff0000be0d725c) != 0)) {
              iVar2 = FUN_ffff0000be0611dc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000be0b52b6,s_dfd_soc_trigger_event_mask_ffff0000be0d7218
                                );
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
          }
          iVar2 = FUN_ffff0000be0611dc
                            (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),&DAT_ffff0000be0c1bcd);
          uVar9 = uVar9 + 1;
          uVar6 = *param_2 + iVar2;
          *param_2 = uVar6;
        } while (uVar9 < DAT_ffff0000be118194);
      }
      if ((int)uVar6 < 0x1001) {
        uVar3 = 1;
      }
      else {
        uVar3 = 1;
        *param_2 = 0x1000;
      }
    }
  }
  return uVar3;
}



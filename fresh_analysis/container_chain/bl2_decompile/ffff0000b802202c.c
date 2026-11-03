
undefined8 FUN_ffff0000b802202c(long *param_1,uint *param_2)

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
    lVar4 = FUN_ffff0000b804bccc(0x1000);
    *param_1 = lVar4;
    if (lVar4 == 0) {
      uVar3 = 0xffffffff;
    }
    else {
      iVar2 = FUN_ffff0000b804d74c
                        (lVar4 + (int)*param_2,0x1000 - (long)(int)*param_2,
                         s___________________________Debug_E_ffff0000b808fb9b);
      uVar6 = *param_2 + iVar2;
      *param_2 = uVar6;
      if (DAT_ffff0000b811b960 != 0) {
        uVar9 = 0;
        do {
          if (uVar9 == 0) {
            uVar7 = (ulong)DAT_ffff0000b811b968._4_4_ | 0xffff000000000000;
            if (DAT_ffff0000b811b964 < 3) {
              iVar2 = FUN_ffff0000b804d74c
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 s_status0___0x_x__status1___0x_x__t_ffff0000b809c75b,
                                 *(undefined4 *)(uVar7 + DAT_ffff0000b811b978),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000b811b97c),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000b811b984),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000b811b980),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000b811b988));
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff0000b804d74c
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_err_flag_status0_decode_____ffff0000b8085392);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff0000b80e91d8)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + DAT_ffff0000b811b978) &
                    *(uint *)((long)&DAT_ffff0000b80e91dc + lVar10)) != 0)) {
                  iVar2 = FUN_ffff0000b804d74c
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000b8093527);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
              iVar2 = FUN_ffff0000b804d74c
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000b809d7a5);
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff0000b804d74c
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_err_flag_status1_decode_____ffff0000b808b863);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff0000b80e91d8)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + DAT_ffff0000b811b97c) &
                    *(uint *)((long)&DAT_ffff0000b80e91dc + lVar10)) != 0)) {
                  iVar2 = FUN_ffff0000b804d74c
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000b8093527);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
            }
            else {
              iVar2 = FUN_ffff0000b804d74c
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 s_wdt_status___0x_x__time_h___0x_x_ffff0000b80a0362,
                                 *(undefined4 *)(uVar7 + DAT_ffff0000b811bb04),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000b811b984),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000b811b980),
                                 *(undefined4 *)(uVar7 + DAT_ffff0000b811bb08));
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff0000b804d74c
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_soc_err_flag_wdt_status_decode___ffff0000b8095479);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff0000b80e91d8)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + DAT_ffff0000b811bb04) &
                    *(uint *)((long)&DAT_ffff0000b80e91dc + lVar10)) != 0)) {
                  iVar2 = FUN_ffff0000b804d74c
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000b8093527);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
            }
          }
          else if (uVar9 == 1) {
            uVar8 = (ulong)DAT_ffff0000b811ba34._4_4_ | 0xffff000000000000;
            iVar2 = FUN_ffff0000b804d74c
                              (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                               s_wdt_status_lower32___0x_x__wdt_s_ffff0000b80934c1,
                               *(undefined4 *)(uVar8 + DAT_ffff0000b811bab4),
                               *(undefined4 *)(uVar8 + DAT_ffff0000b811bab8),
                               *(undefined4 *)(uVar8 + DAT_ffff0000b811bafc),
                               *(undefined4 *)(uVar8 + DAT_ffff0000b811baf8),
                               *(undefined4 *)(uVar8 + DAT_ffff0000b811bac4));
            uVar6 = *param_2;
            uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
            *param_2 = uVar5;
            iVar2 = FUN_ffff0000b804d74c
                              (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                               s_vlp_err_flag_wdt_status_decode___ffff0000b8099c28);
            cVar1 = DAT_ffff0000b80e9bb0;
            uVar7 = (long)(int)*param_2 + (long)iVar2;
            *param_2 = (uint)uVar7;
            if ((cVar1 != '\0') &&
               ((*(uint *)(uVar8 + DAT_ffff0000b811bab4) & DAT_ffff0000b80e9bb4) != 0)) {
              iVar2 = FUN_ffff0000b804d74c
                                (lVar4 + uVar7,(long)(int)(0x1000 - (uint)uVar7),
                                 &DAT_ffff0000b8093527,
                                 s_soc_dbg_err_flag_wdt_irq_mask_ffff0000b80e9b70);
              uVar7 = (ulong)(*param_2 + iVar2);
              *param_2 = *param_2 + iVar2;
            }
            uVar6 = (uint)uVar7;
            if ((DAT_ffff0000b80e9bf8 != '\0') &&
               ((*(uint *)(uVar8 + DAT_ffff0000b811bab4) & DAT_ffff0000b80e9bfc) != 0)) {
              iVar2 = FUN_ffff0000b804d74c
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000b8093527,
                                 s_mfg2soc_dfd_smmu_trigger_event_m_ffff0000b80e9bb8);
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
            if ((DAT_ffff0000b80e9c40 != '\0') &&
               ((*(uint *)(uVar8 + DAT_ffff0000b811bab4) & DAT_ffff0000b80e9c44) != 0)) {
              iVar2 = FUN_ffff0000b804d74c
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000b8093527,s_vlp_tracker_timeout_mask_ffff0000b80e9c00);
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
            if ((DAT_ffff0000b80e9c88 != '\0') &&
               ((*(uint *)(uVar8 + DAT_ffff0000b811bab4) & DAT_ffff0000b80e9c8c) != 0)) {
              iVar2 = FUN_ffff0000b804d74c
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000b8093527,s_dfd_soc_trigger_event_mask_ffff0000b80e9c48
                                );
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
          }
          iVar2 = FUN_ffff0000b804d74c
                            (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),&DAT_ffff0000b809d7a5);
          uVar9 = uVar9 + 1;
          uVar6 = *param_2 + iVar2;
          *param_2 = uVar6;
        } while (uVar9 < DAT_ffff0000b811b960);
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



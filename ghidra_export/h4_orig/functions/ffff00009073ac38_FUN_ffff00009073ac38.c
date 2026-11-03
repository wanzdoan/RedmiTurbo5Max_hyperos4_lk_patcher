// ffff00009073ac38  size=1340  FUN_ffff00009073ac38

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073ac38(long *param_1,uint *param_2)

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
    lVar4 = FUN_ffff00009077d514(0x1000);
    *param_1 = lVar4;
    if (lVar4 == 0) {
      uVar3 = 0xffffffff;
    }
    else {
      iVar2 = FUN_ffff00009077f6fc
                        (lVar4 + (int)*param_2,0x1000 - (long)(int)*param_2,
                         s___________________________Debug_E_ffff0000907e2e3c);
      uVar6 = *param_2 + iVar2;
      *param_2 = uVar6;
      if (_DAT_ffff00009086ded4 != 0) {
        uVar9 = 0;
        do {
          if (uVar9 == 0) {
            uVar7 = (ulong)_DAT_ffff00009086dee0 | 0xffff000000000000;
            if (_DAT_ffff00009086ded8 < 3) {
              iVar2 = FUN_ffff00009077f6fc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 s_status0___0x_x__status1___0x_x__t_ffff0000907f9de7,
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086deec),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086def0),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086def8),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086def4),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086defc));
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff00009077f6fc
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_err_flag_status0_decode_____ffff0000907cf855);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff000090819470)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + _DAT_ffff00009086deec) &
                    *(uint *)((long)&DAT_ffff000090819474 + lVar10)) != 0)) {
                  iVar2 = FUN_ffff00009077f6fc
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000907e94c6);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
              iVar2 = FUN_ffff00009077f6fc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000907fb977);
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff00009077f6fc
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_err_flag_status1_decode_____ffff0000907dacd7);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff000090819470)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + _DAT_ffff00009086def0) &
                    *(uint *)((long)&DAT_ffff000090819474 + lVar10)) != 0)) {
                  iVar2 = FUN_ffff00009077f6fc
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000907e94c6);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
            }
            else {
              iVar2 = FUN_ffff00009077f6fc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 s_wdt_status___0x_x__time_h___0x_x_ffff000090800c24,
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086e078),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086def8),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086def4),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086e07c));
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff00009077f6fc
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_soc_err_flag_wdt_status_decode___ffff0000907ec691);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff000090819470)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + _DAT_ffff00009086e078) &
                    *(uint *)((long)&DAT_ffff000090819474 + lVar10)) != 0)) {
                  iVar2 = FUN_ffff00009077f6fc
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000907e94c6);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
            }
          }
          else if (uVar9 == 1) {
            uVar8 = (ulong)_DAT_ffff00009086dfac | 0xffff000000000000;
            iVar2 = FUN_ffff00009077f6fc
                              (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                               s_wdt_status_lower32___0x_x__wdt_s_ffff0000907e9460,
                               *(undefined4 *)(uVar8 + _DAT_ffff00009086e028),
                               *(undefined4 *)(uVar8 + _DAT_ffff00009086e02c),
                               *(undefined4 *)(uVar8 + _DAT_ffff00009086e070),
                               *(undefined4 *)(uVar8 + _DAT_ffff00009086e06c),
                               *(undefined4 *)(uVar8 + _DAT_ffff00009086e038));
            uVar6 = *param_2;
            uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
            *param_2 = uVar5;
            iVar2 = FUN_ffff00009077f6fc
                              (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                               s_vlp_err_flag_wdt_status_decode___ffff0000907f4970);
            cVar1 = DAT_ffff000090819e48;
            uVar7 = (long)(int)*param_2 + (long)iVar2;
            *param_2 = (uint)uVar7;
            if ((cVar1 != '\0') &&
               ((*(uint *)(uVar8 + _DAT_ffff00009086e028) & DAT_ffff000090819e4c) != 0)) {
              iVar2 = FUN_ffff00009077f6fc
                                (lVar4 + uVar7,(long)(int)(0x1000 - (uint)uVar7),
                                 &DAT_ffff0000907e94c6,
                                 s_soc_dbg_err_flag_wdt_irq_mask_ffff000090819e08);
              uVar7 = (ulong)(*param_2 + iVar2);
              *param_2 = *param_2 + iVar2;
            }
            uVar6 = (uint)uVar7;
            if ((DAT_ffff000090819e90 != '\0') &&
               ((*(uint *)(uVar8 + _DAT_ffff00009086e028) & DAT_ffff000090819e94) != 0)) {
              iVar2 = FUN_ffff00009077f6fc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000907e94c6,
                                 s_mfg2soc_dfd_smmu_trigger_event_m_ffff000090819e50);
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
            if ((DAT_ffff000090819ed8 != '\0') &&
               ((*(uint *)(uVar8 + _DAT_ffff00009086e028) & DAT_ffff000090819edc) != 0)) {
              iVar2 = FUN_ffff00009077f6fc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000907e94c6,s_vlp_tracker_timeout_mask_ffff000090819e98);
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
            if ((DAT_ffff000090819f20 != '\0') &&
               ((*(uint *)(uVar8 + _DAT_ffff00009086e028) & DAT_ffff000090819f24) != 0)) {
              iVar2 = FUN_ffff00009077f6fc
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000907e94c6,s_dfd_soc_trigger_event_mask_ffff000090819ee0
                                );
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
          }
          iVar2 = FUN_ffff00009077f6fc
                            (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),&DAT_ffff0000907fb977);
          uVar9 = uVar9 + 1;
          uVar6 = *param_2 + iVar2;
          *param_2 = uVar6;
        } while (uVar9 < _DAT_ffff00009086ded4);
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



// ffff00009073a9a0  size=1340  FUN_ffff00009073a9a0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073a9a0(long *param_1,uint *param_2)

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
    lVar4 = FUN_ffff00009077d24c(0x1000);
    *param_1 = lVar4;
    if (lVar4 == 0) {
      uVar3 = 0xffffffff;
    }
    else {
      iVar2 = FUN_ffff00009077f434
                        (lVar4 + (int)*param_2,0x1000 - (long)(int)*param_2,
                         s___________________________Debug_E_ffff0000907e2d54);
      uVar6 = *param_2 + iVar2;
      *param_2 = uVar6;
      if (_DAT_ffff00009086de3c != 0) {
        uVar9 = 0;
        do {
          if (uVar9 == 0) {
            uVar7 = (ulong)_DAT_ffff00009086de48 | 0xffff000000000000;
            if (_DAT_ffff00009086de40 < 3) {
              iVar2 = FUN_ffff00009077f434
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 s_status0___0x_x__status1___0x_x__t_ffff0000907f9c4b,
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086de54),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086de58),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086de60),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086de5c),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086de64));
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff00009077f434
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_err_flag_status0_decode_____ffff0000907cf836);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff000090819450)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + _DAT_ffff00009086de54) &
                    *(uint *)((long)&DAT_ffff000090819454 + lVar10)) != 0)) {
                  iVar2 = FUN_ffff00009077f434
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000907e936b);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
              iVar2 = FUN_ffff00009077f434
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000907fb7db);
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff00009077f434
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_err_flag_status1_decode_____ffff0000907dabb5);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff000090819450)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + _DAT_ffff00009086de58) &
                    *(uint *)((long)&DAT_ffff000090819454 + lVar10)) != 0)) {
                  iVar2 = FUN_ffff00009077f434
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000907e936b);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
            }
            else {
              iVar2 = FUN_ffff00009077f434
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 s_wdt_status___0x_x__time_h___0x_x_ffff000090800a48,
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086dfe0),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086de60),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086de5c),
                                 *(undefined4 *)(uVar7 + _DAT_ffff00009086dfe4));
              uVar6 = *param_2;
              uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
              *param_2 = uVar5;
              iVar2 = FUN_ffff00009077f434
                                (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                                 s_soc_err_flag_wdt_status_decode___ffff0000907ec4f7);
              lVar10 = 0;
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
              do {
                if (((&DAT_ffff000090819450)[lVar10] != '\0') &&
                   ((*(uint *)(uVar7 + _DAT_ffff00009086dfe0) &
                    *(uint *)((long)&DAT_ffff000090819454 + lVar10)) != 0)) {
                  iVar2 = FUN_ffff00009077f434
                                    (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                     &DAT_ffff0000907e936b);
                  uVar6 = *param_2 + iVar2;
                  *param_2 = uVar6;
                }
                lVar10 = lVar10 + 0x48;
              } while (lVar10 != 0x9d8);
            }
          }
          else if (uVar9 == 1) {
            uVar8 = (ulong)_DAT_ffff00009086df14 | 0xffff000000000000;
            iVar2 = FUN_ffff00009077f434
                              (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                               s_wdt_status_lower32___0x_x__wdt_s_ffff0000907e9305,
                               *(undefined4 *)(uVar8 + _DAT_ffff00009086df90),
                               *(undefined4 *)(uVar8 + _DAT_ffff00009086df94),
                               *(undefined4 *)(uVar8 + _DAT_ffff00009086dfd8),
                               *(undefined4 *)(uVar8 + _DAT_ffff00009086dfd4),
                               *(undefined4 *)(uVar8 + _DAT_ffff00009086dfa0));
            uVar6 = *param_2;
            uVar5 = (uint)((long)(int)uVar6 + (long)iVar2);
            *param_2 = uVar5;
            iVar2 = FUN_ffff00009077f434
                              (lVar4 + (long)(int)uVar6 + (long)iVar2,(long)(int)(0x1000 - uVar5),
                               s_vlp_err_flag_wdt_status_decode___ffff0000907f47ec);
            cVar1 = DAT_ffff000090819e28;
            uVar7 = (long)(int)*param_2 + (long)iVar2;
            *param_2 = (uint)uVar7;
            if ((cVar1 != '\0') &&
               ((*(uint *)(uVar8 + _DAT_ffff00009086df90) & DAT_ffff000090819e2c) != 0)) {
              iVar2 = FUN_ffff00009077f434
                                (lVar4 + uVar7,(long)(int)(0x1000 - (uint)uVar7),
                                 &DAT_ffff0000907e936b,
                                 s_soc_dbg_err_flag_wdt_irq_mask_ffff000090819de8);
              uVar7 = (ulong)(*param_2 + iVar2);
              *param_2 = *param_2 + iVar2;
            }
            uVar6 = (uint)uVar7;
            if ((DAT_ffff000090819e70 != '\0') &&
               ((*(uint *)(uVar8 + _DAT_ffff00009086df90) & DAT_ffff000090819e74) != 0)) {
              iVar2 = FUN_ffff00009077f434
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000907e936b,
                                 s_mfg2soc_dfd_smmu_trigger_event_m_ffff000090819e30);
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
            if ((DAT_ffff000090819eb8 != '\0') &&
               ((*(uint *)(uVar8 + _DAT_ffff00009086df90) & DAT_ffff000090819ebc) != 0)) {
              iVar2 = FUN_ffff00009077f434
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000907e936b,s_vlp_tracker_timeout_mask_ffff000090819e78);
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
            if ((DAT_ffff000090819f00 != '\0') &&
               ((*(uint *)(uVar8 + _DAT_ffff00009086df90) & DAT_ffff000090819f04) != 0)) {
              iVar2 = FUN_ffff00009077f434
                                (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),
                                 &DAT_ffff0000907e936b,s_dfd_soc_trigger_event_mask_ffff000090819ec0
                                );
              uVar6 = *param_2 + iVar2;
              *param_2 = uVar6;
            }
          }
          iVar2 = FUN_ffff00009077f434
                            (lVar4 + (int)uVar6,(long)(int)(0x1000 - uVar6),&DAT_ffff0000907fb7db);
          uVar9 = uVar9 + 1;
          uVar6 = *param_2 + iVar2;
          *param_2 = uVar6;
        } while (uVar9 < _DAT_ffff00009086de3c);
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



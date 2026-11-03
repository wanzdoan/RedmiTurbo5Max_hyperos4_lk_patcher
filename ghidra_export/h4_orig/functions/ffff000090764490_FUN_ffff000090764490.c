// ffff000090764490  size=644  FUN_ffff000090764490

undefined8 FUN_ffff000090764490(void)

{
  int iVar1;
  long lVar2;
  long lVar3;
  ulong uVar4;
  char *pcVar5;
  
  lVar2 = FUN_ffff00009071935c();
  lVar3 = FUN_ffff000090719370();
  if ((lVar2 == 0) || (lVar3 == 0)) {
    FUN_ffff00009077e11c
              (s__s__d____s___pllk_log_header_is_n_ffff0000907fc0ba,
               s_Read_Pl_Lk_Log_ffff0000907ecea9,0x191,s_BLACKBOX_ffff0000907d9a1f,lVar2,lVar3);
  }
  else {
    iVar1 = *(int *)(lVar2 + 0x4c) + *(int *)(lVar2 + 0x48) + *(int *)(lVar2 + 0x58);
    if (iVar1 != 0) {
      pcVar5 = s_PORSTB_ffff0000907f35b1;
      uVar4 = FUN_ffff0000907641b0(lVar3,s_PORSTB_ffff0000907f35b1,0,iVar1);
      if ((uVar4 & 1) == 0) {
        pcVar5 = s_UVLO_RSTB_ffff0000907fdfcd;
        uVar4 = FUN_ffff0000907641b0(lVar3,s_UVLO_RSTB_ffff0000907fdfcd,0,iVar1);
        if ((uVar4 & 1) == 0) {
          pcVar5 = s_UVLO_VBB_ffff0000907ffa1c;
          uVar4 = FUN_ffff0000907641b0(lVar3,s_UVLO_VBB_ffff0000907ffa1c,0,iVar1);
          if ((uVar4 & 1) == 0) {
            pcVar5 = s_DDLO_RSTB_ffff0000907fa6da;
            uVar4 = FUN_ffff0000907641b0(lVar3,s_DDLO_RSTB_ffff0000907fa6da,0,iVar1);
            if ((uVar4 & 1) == 0) {
              pcVar5 = &DAT_ffff0000907ecf8a;
              uVar4 = FUN_ffff0000907641b0(lVar3,&DAT_ffff0000907ecf8a,0,iVar1);
              if ((uVar4 & 1) == 0) {
                pcVar5 = &DAT_ffff0000907e016a;
                uVar4 = FUN_ffff0000907641b0(lVar3,&DAT_ffff0000907e016a,0,iVar1);
                if ((uVar4 & 1) == 0) {
                  pcVar5 = s__PG_NM_ffff0000907e8397;
                  uVar4 = FUN_ffff0000907641b0(lVar3,s__PG_NM_ffff0000907e8397,0,iVar1);
                  if ((uVar4 & 1) == 0) {
                    pcVar5 = s__PG_LPM__ffff0000907e1d99;
                    uVar4 = FUN_ffff0000907641b0(lVar3,s__PG_LPM__ffff0000907e1d99,0,iVar1);
                    if ((uVar4 & 1) == 0) {
                      pcVar5 = s__OC_NM__ffff0000907dd125;
                      uVar4 = FUN_ffff0000907641b0(lVar3,s__OC_NM__ffff0000907dd125,0,iVar1);
                      if ((uVar4 & 1) == 0) {
                        pcVar5 = s__OC_LPM__ffff000090802d97;
                        uVar4 = FUN_ffff0000907641b0(lVar3,s__OC_LPM__ffff000090802d97,0,iVar1);
                        if ((uVar4 & 1) == 0) {
                          pcVar5 = &DAT_ffff0000907d6ad0;
                          uVar4 = FUN_ffff0000907641b0(lVar3,&DAT_ffff0000907d6ad0,0,iVar1);
                          if ((uVar4 & 1) == 0) {
                            pcVar5 = s_Long_press_shutdown_ffff0000907d3659;
                            uVar4 = FUN_ffff0000907641b0
                                              (lVar3,s_Long_press_shutdown_ffff0000907d3659,0,iVar1)
                            ;
                            if ((uVar4 & 1) == 0) {
                              pcVar5 = s_Power_source_missing_ffff0000907f7030;
                              uVar4 = FUN_ffff0000907641b0
                                                (lVar3,s_Power_source_missing_ffff0000907f7030,0,
                                                 iVar1);
                              if ((uVar4 & 1) == 0) {
                                pcVar5 = s_System_normal_off__SPMICMD__ffff0000907f7045;
                                uVar4 = FUN_ffff0000907641b0
                                                  (lVar3,
                                                  s_System_normal_off__SPMICMD__ffff0000907f7045,0,
                                                  iVar1);
                                if ((uVar4 & 1) == 0) {
                                  pcVar5 = s_PMIC_Self_Protection_Timer_ffff0000907f5266;
                                  uVar4 = FUN_ffff0000907641b0
                                                    (lVar3,
                                                  s_PMIC_Self_Protection_Timer_ffff0000907f5266,0,
                                                  iVar1);
                                  if ((uVar4 & 1) == 0) {
                                    return 0;
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      FUN_ffff00009077e11c
                (s__s__d__found_the_matched_keyword_ffff0000907f35b8,
                 s_is_exception_shutdown_scenario_ffff0000907d4fee,0x773,pcVar5);
      return 1;
    }
  }
  return 0;
}



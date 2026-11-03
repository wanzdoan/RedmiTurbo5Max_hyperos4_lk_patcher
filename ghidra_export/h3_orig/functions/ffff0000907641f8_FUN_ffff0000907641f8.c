// ffff0000907641f8  size=644  FUN_ffff0000907641f8

undefined8 FUN_ffff0000907641f8(void)

{
  int iVar1;
  long lVar2;
  long lVar3;
  ulong uVar4;
  char *pcVar5;
  
  lVar2 = FUN_ffff000090719218();
  lVar3 = FUN_ffff00009071922c();
  if ((lVar2 == 0) || (lVar3 == 0)) {
    FUN_ffff00009077de54
              (s__s__d____s___pllk_log_header_is_n_ffff0000907fbf1e,
               s_Read_Pl_Lk_Log_ffff0000907ecd26,0x191,s_BLACKBOX_ffff0000907d9988,lVar2,lVar3);
  }
  else {
    iVar1 = *(int *)(lVar2 + 0x4c) + *(int *)(lVar2 + 0x48) + *(int *)(lVar2 + 0x58);
    if (iVar1 != 0) {
      pcVar5 = s_PORSTB_ffff0000907f3425;
      uVar4 = FUN_ffff000090763f18(lVar3,s_PORSTB_ffff0000907f3425,0,iVar1);
      if ((uVar4 & 1) == 0) {
        pcVar5 = s_UVLO_RSTB_ffff0000907fde25;
        uVar4 = FUN_ffff000090763f18(lVar3,s_UVLO_RSTB_ffff0000907fde25,0,iVar1);
        if ((uVar4 & 1) == 0) {
          pcVar5 = s_UVLO_VBB_ffff0000907ff874;
          uVar4 = FUN_ffff000090763f18(lVar3,s_UVLO_VBB_ffff0000907ff874,0,iVar1);
          if ((uVar4 & 1) == 0) {
            pcVar5 = s_DDLO_RSTB_ffff0000907fa53e;
            uVar4 = FUN_ffff000090763f18(lVar3,s_DDLO_RSTB_ffff0000907fa53e,0,iVar1);
            if ((uVar4 & 1) == 0) {
              pcVar5 = &DAT_ffff0000907ece07;
              uVar4 = FUN_ffff000090763f18(lVar3,&DAT_ffff0000907ece07,0,iVar1);
              if ((uVar4 & 1) == 0) {
                pcVar5 = &DAT_ffff0000907e00c7;
                uVar4 = FUN_ffff000090763f18(lVar3,&DAT_ffff0000907e00c7,0,iVar1);
                if ((uVar4 & 1) == 0) {
                  pcVar5 = s__PG_NM_ffff0000907e8253;
                  uVar4 = FUN_ffff000090763f18(lVar3,s__PG_NM_ffff0000907e8253,0,iVar1);
                  if ((uVar4 & 1) == 0) {
                    pcVar5 = s__PG_LPM__ffff0000907e1cc8;
                    uVar4 = FUN_ffff000090763f18(lVar3,s__PG_LPM__ffff0000907e1cc8,0,iVar1);
                    if ((uVar4 & 1) == 0) {
                      pcVar5 = s__OC_NM__ffff0000907dd04f;
                      uVar4 = FUN_ffff000090763f18(lVar3,s__OC_NM__ffff0000907dd04f,0,iVar1);
                      if ((uVar4 & 1) == 0) {
                        pcVar5 = s__OC_LPM__ffff000090802b93;
                        uVar4 = FUN_ffff000090763f18(lVar3,s__OC_LPM__ffff000090802b93,0,iVar1);
                        if ((uVar4 & 1) == 0) {
                          pcVar5 = &DAT_ffff0000907d6a3a;
                          uVar4 = FUN_ffff000090763f18(lVar3,&DAT_ffff0000907d6a3a,0,iVar1);
                          if ((uVar4 & 1) == 0) {
                            pcVar5 = s_Long_press_shutdown_ffff0000907d361c;
                            uVar4 = FUN_ffff000090763f18
                                              (lVar3,s_Long_press_shutdown_ffff0000907d361c,0,iVar1)
                            ;
                            if ((uVar4 & 1) == 0) {
                              pcVar5 = s_Power_source_missing_ffff0000907f6ea6;
                              uVar4 = FUN_ffff000090763f18
                                                (lVar3,s_Power_source_missing_ffff0000907f6ea6,0,
                                                 iVar1);
                              if ((uVar4 & 1) == 0) {
                                pcVar5 = s_System_normal_off__SPMICMD__ffff0000907f6ebb;
                                uVar4 = FUN_ffff000090763f18
                                                  (lVar3,
                                                  s_System_normal_off__SPMICMD__ffff0000907f6ebb,0,
                                                  iVar1);
                                if ((uVar4 & 1) == 0) {
                                  pcVar5 = s_PMIC_Self_Protection_Timer_ffff0000907f50f9;
                                  uVar4 = FUN_ffff000090763f18
                                                    (lVar3,
                                                  s_PMIC_Self_Protection_Timer_ffff0000907f50f9,0,
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
      FUN_ffff00009077de54
                (s__s__d__found_the_matched_keyword_ffff0000907f342c,
                 s_is_exception_shutdown_scenario_ffff0000907d4fb1,0x773,pcVar5);
      return 1;
    }
  }
  return 0;
}




void FUN_ffff000090798168(void)

{
  int iVar1;
  undefined4 uVar2;
  uint uVar3;
  long lVar4;
  char *pcVar5;
  undefined8 uVar6;
  byte local_28 [4];
  byte local_24 [4];
  
  local_24[0] = 0;
  local_28[0] = 0;
  lVar4 = FUN_ffff000090740610(1,4);
  if (lVar4 != 0) {
    iVar1 = FUN_ffff00009074046c(lVar4,0x250,local_28,1);
    if (iVar1 < 0) {
      FUN_ffff00009077e11c
                (s__s__d___CCCI__s_fail_to_get_Vsra_ffff0000907e0613,
                 s_md_pmic_lowpower_set_ffff0000907f750a,0x54,
                 s_md_pmic_lowpower_set_ffff0000907f750a,iVar1);
      uVar6 = 0x58;
      local_24[0] = 0x78;
    }
    else {
      FUN_ffff00009077e11c
                (s__s__d___CCCI__s_get_Vsram_voltag_ffff0000907d05be,
                 s_md_pmic_lowpower_set_ffff0000907f750a,0x5a,
                 s_md_pmic_lowpower_set_ffff0000907f750a,local_28[0]);
      uVar6 = 0x5d;
      local_24[0] = local_28[0];
    }
    uVar2 = FUN_ffff0000907404b4(lVar4,0x252,local_24,1);
    FUN_ffff00009077e11c
              (s__s__d___CCCI__s_set_Vmodem_volta_ffff0000907f5840,
               s_md_pmic_lowpower_set_ffff0000907f750a,uVar6,s_md_pmic_lowpower_set_ffff0000907f750a
               ,uVar2);
    uVar3 = FUN_ffff00009074046c(lVar4,0x252,local_28,1);
    if ((int)uVar3 < 0) {
      pcVar5 = s__s__d___CCCI__s_fail_read_Vmodem_ffff0000908032f7;
      uVar6 = 0x62;
    }
    else {
      uVar3 = (uint)local_28[0];
      pcVar5 = s__s__d___CCCI__s_read_Vmodem_valu_ffff0000907d53d2;
      uVar6 = 100;
    }
    FUN_ffff00009077e11c
              (pcVar5,s_md_pmic_lowpower_set_ffff0000907f750a,uVar6,
               s_md_pmic_lowpower_set_ffff0000907f750a,uVar3);
    FUN_ffff00009077cea0(0x26);
    return;
  }
  FUN_ffff00009077e11c
            (s__s__d___CCCI__POWER_ON__s_fail_t_ffff0000907f099f,
             s_md_pmic_lowpower_set_ffff0000907f750a,0x4e,s_md_pmic_lowpower_set_ffff0000907f750a);
  return;
}



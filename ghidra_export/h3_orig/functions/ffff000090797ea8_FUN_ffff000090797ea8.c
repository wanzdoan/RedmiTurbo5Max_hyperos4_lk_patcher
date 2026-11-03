// ffff000090797ea8  size=364  FUN_ffff000090797ea8

void FUN_ffff000090797ea8(void)

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
  lVar4 = FUN_ffff000090740378(1,4);
  if (lVar4 != 0) {
    iVar1 = FUN_ffff0000907401d4(lVar4,0x250,local_28,1);
    if (iVar1 < 0) {
      FUN_ffff00009077de54
                (s__s__d___CCCI__s_fail_to_get_Vsra_ffff0000907e0570,
                 s_md_pmic_lowpower_set_ffff0000907f7380,0x54,
                 s_md_pmic_lowpower_set_ffff0000907f7380,iVar1);
      uVar6 = 0x58;
      local_24[0] = 0x78;
    }
    else {
      FUN_ffff00009077de54
                (s__s__d___CCCI__s_get_Vsram_voltag_ffff0000907d059f,
                 s_md_pmic_lowpower_set_ffff0000907f7380,0x5a,
                 s_md_pmic_lowpower_set_ffff0000907f7380,local_28[0]);
      uVar6 = 0x5d;
      local_24[0] = local_28[0];
    }
    uVar2 = FUN_ffff00009074021c(lVar4,0x252,local_24,1);
    FUN_ffff00009077de54
              (s__s__d___CCCI__s_set_Vmodem_volta_ffff0000907f56d3,
               s_md_pmic_lowpower_set_ffff0000907f7380,uVar6,s_md_pmic_lowpower_set_ffff0000907f7380
               ,uVar2);
    uVar3 = FUN_ffff0000907401d4(lVar4,0x252,local_28,1);
    if ((int)uVar3 < 0) {
      pcVar5 = s__s__d___CCCI__s_fail_read_Vmodem_ffff0000908030f3;
      uVar6 = 0x62;
    }
    else {
      uVar3 = (uint)local_28[0];
      pcVar5 = s__s__d___CCCI__s_read_Vmodem_valu_ffff0000907d5395;
      uVar6 = 100;
    }
    FUN_ffff00009077de54
              (pcVar5,s_md_pmic_lowpower_set_ffff0000907f7380,uVar6,
               s_md_pmic_lowpower_set_ffff0000907f7380,uVar3);
    FUN_ffff00009077cbd8(0x26);
    return;
  }
  FUN_ffff00009077de54
            (s__s__d___CCCI__POWER_ON__s_fail_t_ffff0000907f082e,
             s_md_pmic_lowpower_set_ffff0000907f7380,0x4e,s_md_pmic_lowpower_set_ffff0000907f7380);
  return;
}



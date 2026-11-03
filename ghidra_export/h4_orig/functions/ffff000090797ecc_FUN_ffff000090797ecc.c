// ffff000090797ecc  size=360  FUN_ffff000090797ecc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090797ecc(void)

{
  long local_40 [4];
  
  FUN_ffff00009077e11c
            (s__s__d___CCCI__Power_off_Do_plan_A_ffff0000907e21c1,
             s_internal_md_power_down_ffff0000907f2273,0xee);
  FUN_ffff00009077e11c
            (s__s__d___CCCI__Power_Off__Start__c_ffff0000907dd553,
             s_internal_md_power_down_ffff0000907f2273,0xf1);
  FUN_ffff000090798168();
  local_40[2] = 0;
  local_40[3] = 0;
  local_40[0] = 0;
  local_40[1] = 0;
  FUN_ffff00009077e11c
            (s__s__d___CCCI__POWER_ON__s_set_em_ffff0000907d53a3,
             s_md_set_emi_bus_portect_ffff0000907f0988,0x3c,
             s_md_set_emi_bus_portect_ffff0000907f0988);
  FUN_ffff00009071ebf4(0xc200040b,0x10,0,0,0,0,0,0,local_40);
  if (local_40[0] == 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI__POWER_OFF__md_emi_b_ffff0000907e5752,
               s_md_set_emi_bus_portect_ffff0000907f0988,0x41);
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d___CCCI__POWER_OFF__md_emi_b_ffff0000907def59,
               s_md_set_emi_bus_portect_ffff0000907f0988,0x3f);
  }
  FUN_ffff00009077e11c
            (s__s__d___CCCI__Power_Off__Start__s_ffff0000908032ba,
             s_internal_md_power_down_ffff0000907f2273,0xf8);
  FUN_ffff00009075f074();
  FUN_ffff00009077e11c
            (s__s__d___CCCI__Power_Off__spm_mtc_ffff0000907ed563,
             s_internal_md_power_down_ffff0000907f2273,0xfb);
  FUN_ffff00009077e11c
            (s__s__d___CCCI__POWER_OFF__s_start_ffff0000907ebbad,
             s_md_cd_topclkgen_off_ffff0000907d3a96,0xdc,s_md_cd_topclkgen_off_ffff0000907d3a96);
  _DAT_ffff000010000000 = _DAT_ffff000010000000 | 0x300;
  FUN_ffff00009077e11c
            (s__s__d___CCCI__POWER_OFF__s_end__s_ffff0000907e21e4,
             s_md_cd_topclkgen_off_ffff0000907d3a96,0xe4,s_md_cd_topclkgen_off_ffff0000907d3a96,
             _DAT_ffff000010000000);
  return;
}



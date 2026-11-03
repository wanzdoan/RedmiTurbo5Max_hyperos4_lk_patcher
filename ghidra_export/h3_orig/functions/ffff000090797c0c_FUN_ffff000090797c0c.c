// ffff000090797c0c  size=360  FUN_ffff000090797c0c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090797c0c(void)

{
  long local_40 [4];
  
  FUN_ffff00009077de54
            (s__s__d___CCCI__Power_off_Do_plan_A_ffff0000907e20f0,
             s_internal_md_power_down_ffff0000907f20e7,0xee);
  FUN_ffff00009077de54
            (s__s__d___CCCI__Power_Off__Start__c_ffff0000907dd4b0,
             s_internal_md_power_down_ffff0000907f20e7,0xf1);
  FUN_ffff000090797ea8();
  local_40[2] = 0;
  local_40[3] = 0;
  local_40[0] = 0;
  local_40[1] = 0;
  FUN_ffff00009077de54
            (s__s__d___CCCI__POWER_ON__s_set_em_ffff0000907d5366,
             s_md_set_emi_bus_portect_ffff0000907f0817,0x3c,
             s_md_set_emi_bus_portect_ffff0000907f0817);
  FUN_ffff00009071ea6c(0xc200040b,0x10,0,0,0,0,0,0,local_40);
  if (local_40[0] == 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI__POWER_OFF__md_emi_b_ffff0000907e564e,
               s_md_set_emi_bus_portect_ffff0000907f0817,0x41);
  }
  else {
    FUN_ffff00009077de54
              (s__s__d___CCCI__POWER_OFF__md_emi_b_ffff0000907deeb6,
               s_md_set_emi_bus_portect_ffff0000907f0817,0x3f);
  }
  FUN_ffff00009077de54
            (s__s__d___CCCI__Power_Off__Start__s_ffff0000908030b6,
             s_internal_md_power_down_ffff0000907f20e7,0xf8);
  FUN_ffff00009075eddc();
  FUN_ffff00009077de54
            (s__s__d___CCCI__Power_Off__spm_mtc_ffff0000907ed3e0,
             s_internal_md_power_down_ffff0000907f20e7,0xfb);
  FUN_ffff00009077de54
            (s__s__d___CCCI__POWER_OFF__s_start_ffff0000907eba2a,
             s_md_cd_topclkgen_off_ffff0000907d3a59,0xdc,s_md_cd_topclkgen_off_ffff0000907d3a59);
  _DAT_ffff000010000000 = _DAT_ffff000010000000 | 0x300;
  FUN_ffff00009077de54
            (s__s__d___CCCI__POWER_OFF__s_end__s_ffff0000907e2113,
             s_md_cd_topclkgen_off_ffff0000907d3a59,0xe4,s_md_cd_topclkgen_off_ffff0000907d3a59,
             _DAT_ffff000010000000);
  return;
}



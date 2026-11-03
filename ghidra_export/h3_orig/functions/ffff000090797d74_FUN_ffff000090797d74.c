// ffff000090797d74  size=308  FUN_ffff000090797d74

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090797d74(void)

{
  long local_40 [4];
  
  FUN_ffff00009077de54
            (s__s__d___CCCI__Power_On_Enter_MD_p_ffff0000907d0575,
             s_ccci_md_power_on_ffff0000907d2148,0x117);
  FUN_ffff00009077de54
            (s__s__d___CCCI__Power_On_Do_plan_A_ffff000090803094,s_ccci_md_power_on_ffff0000907d2148
             ,0x121);
  FUN_ffff000090797ea8();
  _DAT_ffff000010000000 = _DAT_ffff000010000000 & 0xfffffcff;
  FUN_ffff00009075eb28();
  FUN_ffff00009077de54
            (s__s__d___CCCI__Power_On_MD_MTCMOS_ffff0000907d9f87,s_ccci_md_power_on_ffff0000907d2148
             ,0x129);
  local_40[2] = 0;
  local_40[3] = 0;
  local_40[0] = 0;
  local_40[1] = 0;
  FUN_ffff00009077de54
            (s__s__d___CCCI__POWER_ON__s_clear_e_ffff0000907d05c8,
             s_md_clear_emi_bus_portect_ffff0000907e214b,0x30,
             s_md_clear_emi_bus_portect_ffff0000907e214b);
  FUN_ffff00009071ea6c(0xc200040b,0x11,0,0,0,0,0,0,local_40);
  if (local_40[0] == 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI__POWER_ON__md_emi_b_ffff0000907ed41b,
               s_md_clear_emi_bus_portect_ffff0000907e214b,0x35);
  }
  else {
    FUN_ffff00009077de54
              (s__s__d___CCCI__POWER_ON__md_emi_b_ffff0000907f8eb5,
               s_md_clear_emi_bus_portect_ffff0000907e214b,0x33);
  }
  FUN_ffff000090798014();
  FUN_ffff00009077de54
            (s__s__d___CCCI__Power_On_MD_DPSW_s_ffff0000907fc663,s_ccci_md_power_on_ffff0000907d2148
             ,0x130);
  return;
}



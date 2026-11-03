
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090798034(void)

{
  long local_40 [4];
  
  FUN_ffff00009077e11c
            (s__s__d___CCCI__Power_On_Enter_MD_p_ffff0000907d0594,
             s_ccci_md_power_on_ffff0000907d2185,0x117);
  FUN_ffff00009077e11c
            (s__s__d___CCCI__Power_On_Do_plan_A_ffff000090803298,s_ccci_md_power_on_ffff0000907d2185
             ,0x121);
  FUN_ffff000090798168();
  _DAT_ffff000010000000 = _DAT_ffff000010000000 & 0xfffffcff;
  FUN_ffff00009075edc0();
  FUN_ffff00009077e11c
            (s__s__d___CCCI__Power_On_MD_MTCMOS_ffff0000907da01e,s_ccci_md_power_on_ffff0000907d2185
             ,0x129);
  local_40[2] = 0;
  local_40[3] = 0;
  local_40[0] = 0;
  local_40[1] = 0;
  FUN_ffff00009077e11c
            (s__s__d___CCCI__POWER_ON__s_clear_e_ffff0000907d05e7,
             s_md_clear_emi_bus_portect_ffff0000907e221c,0x30,
             s_md_clear_emi_bus_portect_ffff0000907e221c);
  FUN_ffff00009071ebf4(0xc200040b,0x11,0,0,0,0,0,0,local_40);
  if (local_40[0] == 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI__POWER_ON__md_emi_b_ffff0000907ed59e,
               s_md_clear_emi_bus_portect_ffff0000907e221c,0x35);
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d___CCCI__POWER_ON__md_emi_b_ffff0000907f9051,
               s_md_clear_emi_bus_portect_ffff0000907e221c,0x33);
  }
  FUN_ffff0000907982d4();
  FUN_ffff00009077e11c
            (s__s__d___CCCI__Power_On_MD_DPSW_s_ffff0000907fc7ff,s_ccci_md_power_on_ffff0000907d2185
             ,0x130);
  return;
}



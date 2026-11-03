// ffff00009075ebd0  size=524  FUN_ffff00009075ebd0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075ebd0(void)

{
  char *pcVar1;
  
  if (1000000 < _DAT_ffff00009087bf0c) {
    if ((DAT_ffff00009087bf10 & 1) != 0) {
      return;
    }
    DAT_ffff00009087bf10 = 1;
    FUN_ffff00009077de54(s_sleep_protect_en_0__08x_ffff0000907f0150,_DAT_ffff00001002c000);
    FUN_ffff00009077de54(s_sleep_protect_en_1__08x_ffff0000907d3510,_DAT_ffff00001002c020);
    FUN_ffff00009077de54(s_sleep_protect_rdy_sta_1__08x_ffff0000907eb492,_DAT_ffff00001002c02c);
    FUN_ffff00009077de54(s_sleep_protect_rdy_sta_0__08x_ffff0000907f6cf9,_DAT_ffff00001002c00c);
    FUN_ffff00009077de54(s_bus_protect_rdy_sta__08x_ffff0000907db400,_DAT_ffff00001c00d208);
    FUN_ffff00009077de54(s_bus_msb_protect_rdy_sta__08x_ffff0000907d68fd,_DAT_ffff00001c00d20c);
    FUN_ffff00009077de54(s_bus_protect_en__08x_ffff0000907e3557,_DAT_ffff00001c00d0d8);
    FUN_ffff00009077de54(s_md1_pwr_con__08x_ffff0000907cfee0,_DAT_ffff00001c004e00);
    FUN_ffff00009077de54(s_bus_msb_protect_en__08x_ffff0000907f194f,_DAT_ffff00001c00d0e4);
    FUN_ffff00009077de54(s_adsp_top_pwr_con__08x_ffff0000907d995b,_DAT_ffff00001c004e54);
    FUN_ffff00009077de54(s_adsp_infra_pwr_con__08x_ffff0000907dce16,_DAT_ffff00001c004e58);
    FUN_ffff00009077de54(s_adsp_ao_pwr_con__08x_ffff0000907d9972,_DAT_ffff00001c004e5c);
    FUN_ffff00009077de54(s_spu_hwrot_pwr_con__08x_ffff0000907f1968,_DAT_ffff00001c004e8c);
    FUN_ffff00009077de54(s_md_buck_iso_con__08x_ffff0000907e356c,_DAT_ffff00001c004efc);
    FUN_ffff00009077de54(s_pwr_status__08x_ffff0000907f3294,_DAT_ffff00001c004f14);
    FUN_ffff00009077de54(s_pwr_status_2nd__08x_ffff0000907cfef2,_DAT_ffff00001c004f18);
    FUN_ffff00009077de54(s_dsi_phy0_pwr_con__08x_ffff0000907cff07,_DAT_ffff000031b500f0);
    FUN_ffff00009077de54(s_dsi_phy1_pwr_con__08x_ffff0000907dff95,_DAT_ffff000031b500f4);
    FUN_ffff00009077de54(s_dsi_phy2_pwr_con__08x_ffff000090802a9a,_DAT_ffff000031b500f8);
    pcVar1 = s_pwron_ffff0000907f1980;
    if (DAT_ffff00009087bf04 == '\0') {
      pcVar1 = &DAT_ffff0000907f4fcc;
    }
    FUN_ffff00009077de54
              (s__s__d_MTCMOS_hang_at__s_flow_ste_ffff0000907cff1e,s__clkmgr__ffff0000907d1955,
               _DAT_ffff00009087bf00,pcVar1,_DAT_ffff00009087bf08);
  }
  if ((DAT_ffff00009087bf10 & 1) == 0) {
    _DAT_ffff00009087bf0c = _DAT_ffff00009087bf0c + 1;
  }
  return;
}



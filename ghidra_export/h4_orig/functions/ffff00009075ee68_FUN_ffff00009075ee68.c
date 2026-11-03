// ffff00009075ee68  size=524  FUN_ffff00009075ee68

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075ee68(void)

{
  char *pcVar1;
  
  if (1000000 < _DAT_ffff00009087bfa4) {
    if ((DAT_ffff00009087bfa8 & 1) != 0) {
      return;
    }
    DAT_ffff00009087bfa8 = 1;
    FUN_ffff00009077e11c(s_sleep_protect_en_0__08x_ffff0000907f02c1,_DAT_ffff00001002c000);
    FUN_ffff00009077e11c(s_sleep_protect_en_1__08x_ffff0000907d354d,_DAT_ffff00001002c020);
    FUN_ffff00009077e11c(s_sleep_protect_rdy_sta_1__08x_ffff0000907eb615,_DAT_ffff00001002c02c);
    FUN_ffff00009077e11c(s_sleep_protect_rdy_sta_0__08x_ffff0000907f6e83,_DAT_ffff00001002c00c);
    FUN_ffff00009077e11c(s_bus_protect_rdy_sta__08x_ffff0000907db50b,_DAT_ffff00001c00d208);
    FUN_ffff00009077e11c(s_bus_msb_protect_rdy_sta__08x_ffff0000907d6993,_DAT_ffff00001c00d20c);
    FUN_ffff00009077e11c(s_bus_protect_en__08x_ffff0000907e3628,_DAT_ffff00001c00d0d8);
    FUN_ffff00009077e11c(s_md1_pwr_con__08x_ffff0000907cfeff,_DAT_ffff00001c004e00);
    FUN_ffff00009077e11c(s_bus_msb_protect_en__08x_ffff0000907f1ac0,_DAT_ffff00001c00d0e4);
    FUN_ffff00009077e11c(s_adsp_top_pwr_con__08x_ffff0000907d99f2,_DAT_ffff00001c004e54);
    FUN_ffff00009077e11c(s_adsp_infra_pwr_con__08x_ffff0000907dceec,_DAT_ffff00001c004e58);
    FUN_ffff00009077e11c(s_adsp_ao_pwr_con__08x_ffff0000907d9a09,_DAT_ffff00001c004e5c);
    FUN_ffff00009077e11c(s_spu_hwrot_pwr_con__08x_ffff0000907f1ad9,_DAT_ffff00001c004e8c);
    FUN_ffff00009077e11c(s_md_buck_iso_con__08x_ffff0000907e363d,_DAT_ffff00001c004efc);
    FUN_ffff00009077e11c(s_pwr_status__08x_ffff0000907f3420,_DAT_ffff00001c004f14);
    FUN_ffff00009077e11c(s_pwr_status_2nd__08x_ffff0000907cff11,_DAT_ffff00001c004f18);
    FUN_ffff00009077e11c(s_dsi_phy0_pwr_con__08x_ffff0000907cff26,_DAT_ffff000031b500f0);
    FUN_ffff00009077e11c(s_dsi_phy1_pwr_con__08x_ffff0000907e0038,_DAT_ffff000031b500f4);
    FUN_ffff00009077e11c(s_dsi_phy2_pwr_con__08x_ffff000090802c9e,_DAT_ffff000031b500f8);
    pcVar1 = s_pwron_ffff0000907f1af1;
    if (DAT_ffff00009087bf9c == '\0') {
      pcVar1 = &DAT_ffff0000907f5139;
    }
    FUN_ffff00009077e11c
              (s__s__d_MTCMOS_hang_at__s_flow_ste_ffff0000907cff3d,s__clkmgr__ffff0000907d1992,
               _DAT_ffff00009087bf98,pcVar1,_DAT_ffff00009087bfa0);
  }
  if ((DAT_ffff00009087bfa8 & 1) == 0) {
    _DAT_ffff00009087bfa4 = _DAT_ffff00009087bfa4 + 1;
  }
  return;
}



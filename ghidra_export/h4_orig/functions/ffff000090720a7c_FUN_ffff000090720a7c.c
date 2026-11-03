// ffff000090720a7c  size=4  FUN_ffff000090720a7c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090720a7c(void)

{
  FUN_ffff00009077e11c
            (s__s__mt6379_2p_start_probe_ffff0000907fed5f,s_mt6379_2p_charger_probe_ffff0000907e5e87
            );
  FUN_ffff000090737324(&PTR_FUN_ffff0000908183b0,0,&PTR_s_mt6379_2p_master_ffff0000908183d0);
  _DAT_ffff000090860648 = FUN_ffff000090737168(s_mt6379_2p_master_ffff0000907fcf51);
  if (_DAT_ffff000090860648 == 0) {
    FUN_ffff00009077e11c(s_Failed_to_initialize_regmap_ffff0000907d4185);
  }
  _DAT_ffff000090860680 = &DAT_ffff0000908183d8;
  _DAT_ffff000090860688 = FUN_ffff000090720b40;
  if (DAT_ffff000090860678 == '\0') {
    _DAT_ffff000090860650 = 1;
  }
  else if (_DAT_ffff000090860650 == 0) goto LAB_ffff000090720b20;
  FUN_ffff00009077e11c(s_cs_disable____d_ffff0000907d0d45);
LAB_ffff000090720b20:
  FUN_ffff000090720720(0xffff000090860658);
  FUN_ffff00009077e11c(s_mt6379_2p_probe_successfully__ffff0000907ea9dc);
  return;
}



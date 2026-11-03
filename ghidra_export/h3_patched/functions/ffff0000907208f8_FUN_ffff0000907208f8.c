// ffff0000907208f8  size=192  FUN_ffff0000907208f8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907208f8(void)

{
  FUN_ffff00009077de54
            (s__s__mt6379_2p_start_probe_ffff0000907febb7,s_mt6379_2p_charger_probe_ffff0000907e5d5c
            );
  FUN_ffff00009073708c(&PTR_FUN_ffff0000908183a0,0,&PTR_s_mt6379_2p_master_ffff0000908183c0);
  _DAT_ffff0000908605b0 = FUN_ffff000090736ed0(s_mt6379_2p_master_ffff0000907fcda9);
  if (_DAT_ffff0000908605b0 == 0) {
    FUN_ffff00009077de54(s_Failed_to_initialize_regmap_ffff0000907d4148);
  }
  _DAT_ffff0000908605e8 = &DAT_ffff0000908183c8;
  _DAT_ffff0000908605f0 = FUN_ffff0000907209b8;
  if (DAT_ffff0000908605e0 == '\0') {
    _DAT_ffff0000908605b8 = 1;
  }
  else if (_DAT_ffff0000908605b8 == 0) goto LAB_ffff000090720998;
  FUN_ffff00009077de54(s_cs_disable____d_ffff0000907d0d08);
LAB_ffff000090720998:
  FUN_ffff000090720598(0xffff0000908605c0);
  FUN_ffff00009077de54(s_mt6379_2p_probe_successfully__ffff0000907ea842);
  return;
}



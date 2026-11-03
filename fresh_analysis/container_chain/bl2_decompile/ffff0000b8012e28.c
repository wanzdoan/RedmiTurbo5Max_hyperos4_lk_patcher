
void FUN_ffff0000b8012e28(void)

{
  FUN_ffff0000b804c33c
            (s__s__mt6379_2p_start_probe_ffff0000b809f446,s_mt6379_2p_charger_probe_ffff0000b8091594
            );
  FUN_ffff0000b8020fdc(&PTR_FUN_ffff0000b80adf30,0,&PTR_FUN_ffff0000b80adf50);
  DAT_ffff0000b811af40 = FUN_ffff0000b8020e20(FUN_ffff0000b809e30d);
  if (DAT_ffff0000b811af40 == 0) {
    FUN_ffff0000b804c33c(s_Failed_to_initialize_regmap_ffff0000b8087eb6);
  }
  DAT_ffff0000b811af78 = &DAT_ffff0000b80adf58;
  DAT_ffff0000b811af80 = &LAB_ffff0000b8012ee8;
  if (DAT_ffff0000b811af70 == '\0') {
    DAT_ffff0000b811af48 = 1;
  }
  else if (DAT_ffff0000b811af48 == 0) goto LAB_ffff0000b8012ec8;
  FUN_ffff0000b804c33c(s_cs_disable____d_ffff0000b8086111);
LAB_ffff0000b8012ec8:
  FUN_ffff0000b80126d4(&DAT_ffff0000b811af50);
  FUN_ffff0000b804c33c(s_mt6379_2p_probe_successfully__ffff0000b8094228);
  return;
}



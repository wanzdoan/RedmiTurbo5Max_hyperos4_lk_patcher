
void FUN_ffff0000be01ed1c(void)

{
  FUN_ffff0000be05fd98
            (s__s__mt6379_2p_start_probe_ffff0000be0c3e78,s_mt6379_2p_charger_probe_ffff0000be0b2cfc
            );
  FUN_ffff0000be029728(&PTR_FUN_ffff0000be0d63c0,0,&PTR_s_mt6379_2p_master_ffff0000be0d63e0);
  DAT_ffff0000be117658 = FUN_ffff0000be02956c(s_mt6379_2p_master_ffff0000be0c2acd);
  if (DAT_ffff0000be117658 == 0) {
    FUN_ffff0000be05fd98(s_Failed_to_initialize_regmap_ffff0000be0a6ba8);
  }
  DAT_ffff0000be117690 = &DAT_ffff0000be0d63e8;
  DAT_ffff0000be117698 = &LAB_ffff0000be01ede0;
  if (DAT_ffff0000be117688 == '\0') {
    DAT_ffff0000be117660 = 1;
  }
  else if (DAT_ffff0000be117660 == 0) goto code_r0xffff0000be01edc4;
  FUN_ffff0000be05fd98(s_cs_disable____d_ffff0000be0a45e4);
code_r0xffff0000be01edc4:
  FUN_ffff0000be01e9c0(&DAT_ffff0000be117668);
  FUN_ffff0000be05fd98(s_mt6379_2p_probe_successfully__ffff0000be0b6323);
  return;
}



// ffff0000907631f8  size=240  FUN_ffff0000907631f8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907631f8(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bf14 - 1 < 2) {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1_v2__no_normal_ffff0000907d4def,
               s_CheckBlackboxNormalBootlogHeader_ffff0000907e81b8,0x592);
  }
  else {
    uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c04,&DAT_ffff00009087c858,0xb3b4400,0x2f4);
    if ((uVar1 & 1) != 0) {
      if (_DAT_ffff00009087c858 != -0x2c4a58061e3d5b2a) {
        FUN_ffff00009077de54
                  (s__s__d____s___init_normal_boot_lo_ffff0000907d1a18,
                   s_CheckBlackboxNormalBootlogHeader_ffff0000907e81b8,0x59d,
                   s_BLACKBOX_ffff0000907d9988);
        FUN_ffff000090761200(&DAT_ffff00009087c858);
      }
      FUN_ffff0000907620fc(&DAT_ffff00009087c858);
      return 1;
    }
    FUN_ffff00009077de54
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
               s_CheckBlackboxNormalBootlogHeader_ffff0000907e81b8,0x599,s_BLACKBOX_ffff0000907d9988
              );
  }
  return 0;
}



// ffff000090763490  size=240  FUN_ffff000090763490

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090763490(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bfac - 1 < 2) {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1_v2__no_normal_ffff0000907d4e2c,
               s_CheckBlackboxNormalBootlogHeader_ffff0000907e8313,0x592);
  }
  else {
    uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c8f0,0xb3b4400,0x2f4);
    if ((uVar1 & 1) != 0) {
      if (_DAT_ffff00009087c8f0 != -0x2c4a58061e3d5b2a) {
        FUN_ffff00009077e11c
                  (s__s__d____s___init_normal_boot_lo_ffff0000907d1a55,
                   s_CheckBlackboxNormalBootlogHeader_ffff0000907e8313,0x59d,
                   s_BLACKBOX_ffff0000907d9a1f);
        FUN_ffff000090761498(&DAT_ffff00009087c8f0);
      }
      FUN_ffff000090762394(&DAT_ffff00009087c8f0);
      return 1;
    }
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxNormalBootlogHeader_ffff0000907e8313,0x599,s_BLACKBOX_ffff0000907d9a1f
              );
  }
  return 0;
}



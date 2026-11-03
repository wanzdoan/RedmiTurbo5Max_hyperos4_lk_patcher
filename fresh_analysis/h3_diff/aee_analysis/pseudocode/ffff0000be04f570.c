
undefined8 FUN_ffff0000be04f570(void)

{
  ulong uVar1;
  
  if (DAT_ffff0000be1261e0 - 1 < 2) {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1_v2__no_normal_ffff0000be0a7491,
               s_CheckBlackboxNormalBootlogHeader_ffff0000be0b4858,0x592);
  }
  else {
    uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126b28,0xb3b4400,0x2f4);
    if ((uVar1 & 1) != 0) {
      if (DAT_ffff0000be126b28 != -0x2c4a58061e3d5b2a) {
        FUN_ffff0000be05fd98
                  (s__s__d____s___init_normal_boot_lo_ffff0000be0a4f1f,
                   s_CheckBlackboxNormalBootlogHeader_ffff0000be0b4858,0x59d,
                   s_BLACKBOX_ffff0000be0aa9ee);
        FUN_ffff0000be04d578(&DAT_ffff0000be126b28);
      }
      FUN_ffff0000be04e474(&DAT_ffff0000be126b28);
      return 1;
    }
    FUN_ffff0000be05fd98
              (s__s__d____s___GetPartitionInfo_fa_ffff0000be0c10b3,
               s_CheckBlackboxNormalBootlogHeader_ffff0000be0b4858,0x599,s_BLACKBOX_ffff0000be0aa9ee
              );
  }
  return 0;
}



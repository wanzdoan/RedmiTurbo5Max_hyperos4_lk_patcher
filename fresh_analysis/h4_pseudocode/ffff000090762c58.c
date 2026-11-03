
undefined8 FUN_ffff000090762c58(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_control_z_ffff0000907d69e7,
               s_CheckBlackboxControlZoneHeader_ffff0000907f351a,0x4f0);
    return 0;
  }
  uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087bfb0,0xa400000,0x14);
  if ((uVar1 & 1) == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxControlZoneHeader_ffff0000907f351a,0x4f7,s_BLACKBOX_ffff0000907d9a1f);
    return 0;
  }
  if (DAT_ffff00009087bfb0 != 0x1a3f5d7e9c2b4a6d) {
    FUN_ffff00009077e11c
              (s__s__d____s___init_control_zone_h_ffff0000907d19fd,
               s_CheckBlackboxControlZoneHeader_ffff0000907f351a,0x4fb,s_BLACKBOX_ffff0000907d9a1f);
    if (DAT_ffff00009087bfac == '\x03') {
      DAT_ffff00009087bfb8 = 0x3000;
    }
    else if (DAT_ffff00009087bfac == '\x02') {
      DAT_ffff00009087bfb8 = 0x2000;
    }
    else if (DAT_ffff00009087bfac == '\x01') {
      FUN_ffff00009077e11c
                (s__s__d__blackbox_v1__no_control_z_ffff0000907d69e7,
                 s_InitControlZoneHeader_ffff0000907eb633,0x215);
      goto LAB_ffff000090762d84;
    }
    DAT_ffff00009087bfb0 = 0x1a3f5d7e9c2b4a6d;
    DAT_ffff00009087bfbc = 0xa40000000000400;
  }
LAB_ffff000090762d84:
  FUN_ffff0000907617e8(&DAT_ffff00009087bfb0);
  return 1;
}



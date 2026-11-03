
undefined8 FUN_ffff0000be04ed38(void)

{
  ulong uVar1;
  
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_control_z_ffff0000be0a8655,
               s_CheckBlackboxControlZoneHeader_ffff0000be0bc575,0x4f0);
    return 0;
  }
  uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be1261e8,0xa400000,0x14);
  if ((uVar1 & 1) == 0) {
    FUN_ffff0000be05fd98
              (s__s__d____s___GetPartitionInfo_fa_ffff0000be0c10b3,
               s_CheckBlackboxControlZoneHeader_ffff0000be0bc575,0x4f7,s_BLACKBOX_ffff0000be0aa9ee);
    return 0;
  }
  if (DAT_ffff0000be1261e8 != 0x1a3f5d7e9c2b4a6d) {
    FUN_ffff0000be05fd98
              (s__s__d____s___init_control_zone_h_ffff0000be0a4ec7,
               s_CheckBlackboxControlZoneHeader_ffff0000be0bc575,0x4fb,s_BLACKBOX_ffff0000be0aa9ee);
    if (DAT_ffff0000be1261e0 == '\x03') {
      DAT_ffff0000be1261f0 = 0x3000;
    }
    else if (DAT_ffff0000be1261e0 == '\x02') {
      DAT_ffff0000be1261f0 = 0x2000;
    }
    else if (DAT_ffff0000be1261e0 == '\x01') {
      FUN_ffff0000be05fd98
                (s__s__d__blackbox_v1__no_control_z_ffff0000be0a8655,
                 s_InitControlZoneHeader_ffff0000be0b6cef,0x215);
      goto code_r0xffff0000be04ee68;
    }
    DAT_ffff0000be1261e8 = 0x1a3f5d7e9c2b4a6d;
    DAT_ffff0000be1261f4 = 0xa40000000000400;
  }
code_r0xffff0000be04ee68:
  FUN_ffff0000be04d8c8(&DAT_ffff0000be1261e8);
  return 1;
}



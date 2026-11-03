
undefined8 FUN_ffff0000be04ee88(void)

{
  ulong uVar1;
  
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_record_zo_ffff0000be0c573c,
               s_CheckBlackboxRecordZoneHeader_ffff0000be0ba369,0x507);
    return 0;
  }
  uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126200,0xa420000,0x14);
  if ((uVar1 & 1) == 0) {
    FUN_ffff0000be05fd98
              (s__s__d____s___GetPartitionInfo_fa_ffff0000be0c10b3,
               s_CheckBlackboxRecordZoneHeader_ffff0000be0ba369,0x50e,s_BLACKBOX_ffff0000be0aa9ee);
    return 0;
  }
  if (DAT_ffff0000be126200 != -0x71b3d560f4c2a184) {
    FUN_ffff0000be05fd98
              (s__s__d____s___init_record_zone_he_ffff0000be0b02bf,
               s_CheckBlackboxRecordZoneHeader_ffff0000be0ba369,0x512,s_BLACKBOX_ffff0000be0aa9ee);
    if (DAT_ffff0000be1261e0 == '\x03') {
      DAT_ffff0000be126208 = 0x3000;
    }
    else if (DAT_ffff0000be1261e0 == '\x02') {
      DAT_ffff0000be126208 = 0x2000;
    }
    else if (DAT_ffff0000be1261e0 == '\x01') {
      FUN_ffff0000be05fd98
                (s__s__d__blackbox_v1__no_record_zo_ffff0000be0c573c,
                 s_InitRecordZoneHeader_ffff0000be0acd47,0x228);
      goto code_r0xffff0000be04efb8;
    }
    DAT_ffff0000be126200 = -0x71b3d560f4c2a184;
    DAT_ffff0000be12620c = 0xa42040000000400;
  }
code_r0xffff0000be04efb8:
  FUN_ffff0000be04d9ac(&DAT_ffff0000be126200);
  return 1;
}



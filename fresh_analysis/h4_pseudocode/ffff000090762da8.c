
undefined8 FUN_ffff000090762da8(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_record_zo_ffff0000908013b3,
               s_CheckBlackboxRecordZoneHeader_ffff0000907f03b8,0x507);
    return 0;
  }
  uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087bfc8,0xa420000,0x14);
  if ((uVar1 & 1) == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxRecordZoneHeader_ffff0000907f03b8,0x50e,s_BLACKBOX_ffff0000907d9a1f);
    return 0;
  }
  if (DAT_ffff00009087bfc8 != -0x71b3d560f4c2a184) {
    FUN_ffff00009077e11c
              (s__s__d____s___init_record_zone_he_ffff0000907e1d02,
               s_CheckBlackboxRecordZoneHeader_ffff0000907f03b8,0x512,s_BLACKBOX_ffff0000907d9a1f);
    if (DAT_ffff00009087bfac == '\x03') {
      DAT_ffff00009087bfd0 = 0x3000;
    }
    else if (DAT_ffff00009087bfac == '\x02') {
      DAT_ffff00009087bfd0 = 0x2000;
    }
    else if (DAT_ffff00009087bfac == '\x01') {
      FUN_ffff00009077e11c
                (s__s__d__blackbox_v1__no_record_zo_ffff0000908013b3,
                 s_InitRecordZoneHeader_ffff0000907dcfab,0x228);
      goto LAB_ffff000090762ed4;
    }
    DAT_ffff00009087bfc8 = -0x71b3d560f4c2a184;
    DAT_ffff00009087bfd4 = 0xa42040000000400;
  }
LAB_ffff000090762ed4:
  FUN_ffff0000907618cc(&DAT_ffff00009087bfc8);
  return 1;
}



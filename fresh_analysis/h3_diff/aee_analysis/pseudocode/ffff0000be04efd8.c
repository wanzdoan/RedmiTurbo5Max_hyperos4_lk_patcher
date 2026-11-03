
undefined8 FUN_ffff0000be04efd8(void)

{
  ulong uVar1;
  
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_journal_z_ffff0000be0b6d05,
               s_CheckBlackboxJournalZoneHeader_ffff0000be0bfe89,0x51e);
    return 0;
  }
  uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126218,0xa440000,0x28);
  if ((uVar1 & 1) == 0) {
    FUN_ffff0000be05fd98
              (s__s__d____s___GetPartitionInfo_fa_ffff0000be0c10b3,
               s_CheckBlackboxJournalZoneHeader_ffff0000be0bfe89,0x525,s_BLACKBOX_ffff0000be0aa9ee);
    return 0;
  }
  if (DAT_ffff0000be126218 != -0x9275f4d3b1e0c5b) {
    FUN_ffff0000be05fd98
              (s__s__d____s___init_journal_zone_h_ffff0000be0c678e,
               s_CheckBlackboxJournalZoneHeader_ffff0000be0bfe89,0x529,s_BLACKBOX_ffff0000be0aa9ee);
    if (DAT_ffff0000be1261e0 == '\x03') {
      FUN_ffff0000be05fd98
                (s__s__d__blackbox_v3_ffff0000be0a969f,s_InitJournalZoneHeader_ffff0000be0b9032,
                 0x244);
      DAT_ffff0000be12623c = 0xbf6c800;
      DAT_ffff0000be126238 = 0xb3b4400;
      DAT_ffff0000be126220 = 0x3000;
    }
    else if (DAT_ffff0000be1261e0 == '\x02') {
      FUN_ffff0000be05fd98
                (s__s__d__blackbox_v2_ffff0000be0aa9f7,s_InitJournalZoneHeader_ffff0000be0b9032,
                 0x23f);
      DAT_ffff0000be126238 = 0;
      DAT_ffff0000be12623c = 0;
      DAT_ffff0000be126220 = 0x2000;
    }
    else if (DAT_ffff0000be1261e0 == '\x01') {
      FUN_ffff0000be05fd98
                (s__s__d__blackbox_v1__no_journal_z_ffff0000be0b6d05,
                 s_InitJournalZoneHeader_ffff0000be0b9032,0x23b);
      goto code_r0xffff0000be04f178;
    }
    DAT_ffff0000be126218 = -0x9275f4d3b1e0c5b;
    DAT_ffff0000be126224 = 0xa44040000000400;
    DAT_ffff0000be12622c = 0xb00a8000ac10800;
    DAT_ffff0000be126234 = 0xac60c00;
  }
code_r0xffff0000be04f178:
  FUN_ffff0000be04da90(&DAT_ffff0000be126218);
  return 1;
}



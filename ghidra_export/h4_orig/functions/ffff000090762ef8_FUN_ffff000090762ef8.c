// ffff000090762ef8  size=448  FUN_ffff000090762ef8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090762ef8(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_journal_z_ffff0000907eb649,
               s_CheckBlackboxJournalZoneHeader_ffff0000907f8c08,0x51e);
    return 0;
  }
  uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087bfe0,0xa440000,0x28);
  if ((uVar1 & 1) == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxJournalZoneHeader_ffff0000907f8c08,0x525,s_BLACKBOX_ffff0000907d9a1f);
    return 0;
  }
  if (_DAT_ffff00009087bfe0 != -0x9275f4d3b1e0c5b) {
    FUN_ffff00009077e11c
              (s__s__d____s___init_journal_zone_h_ffff000090802d70,
               s_CheckBlackboxJournalZoneHeader_ffff0000907f8c08,0x529,s_BLACKBOX_ffff0000907d9a1f);
    if (DAT_ffff00009087bfac == '\x03') {
      FUN_ffff00009077e11c
                (s__s__d__blackbox_v3_ffff0000907d81d1,s_InitJournalZoneHeader_ffff0000907ee9f5,
                 0x244);
      _DAT_ffff00009087c004 = 0xbf6c800;
      _DAT_ffff00009087c000 = 0xb3b4400;
      _DAT_ffff00009087bfe8 = 0x3000;
    }
    else if (DAT_ffff00009087bfac == '\x02') {
      FUN_ffff00009077e11c
                (s__s__d__blackbox_v2_ffff0000907d9a28,s_InitJournalZoneHeader_ffff0000907ee9f5,
                 0x23f);
      _DAT_ffff00009087c000 = 0;
      _DAT_ffff00009087c004 = 0;
      _DAT_ffff00009087bfe8 = 0x2000;
    }
    else if (DAT_ffff00009087bfac == '\x01') {
      FUN_ffff00009077e11c
                (s__s__d__blackbox_v1__no_journal_z_ffff0000907eb649,
                 s_InitJournalZoneHeader_ffff0000907ee9f5,0x23b);
      goto LAB_ffff000090763094;
    }
    _DAT_ffff00009087bfe0 = -0x9275f4d3b1e0c5b;
    _DAT_ffff00009087bfec = 0xa44040000000400;
    _DAT_ffff00009087bff4 = 0xb00a8000ac10800;
    _DAT_ffff00009087bffc = 0xac60c00;
  }
LAB_ffff000090763094:
  FUN_ffff0000907619b0(&DAT_ffff00009087bfe0);
  return 1;
}



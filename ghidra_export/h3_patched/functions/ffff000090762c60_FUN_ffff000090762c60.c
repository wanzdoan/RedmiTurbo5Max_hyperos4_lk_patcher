// ffff000090762c60  size=448  FUN_ffff000090762c60

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090762c60(void)

{
  ulong uVar1;
  
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_journal_z_ffff0000907eb4af,
               s_CheckBlackboxJournalZoneHeader_ffff0000907f8a6c,0x51e);
    return 0;
  }
  uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c04,&DAT_ffff00009087bf48,0xa440000,0x28);
  if ((uVar1 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
               s_CheckBlackboxJournalZoneHeader_ffff0000907f8a6c,0x525,s_BLACKBOX_ffff0000907d9988);
    return 0;
  }
  if (_DAT_ffff00009087bf48 != -0x9275f4d3b1e0c5b) {
    FUN_ffff00009077de54
              (s__s__d____s___init_journal_zone_h_ffff000090802b6c,
               s_CheckBlackboxJournalZoneHeader_ffff0000907f8a6c,0x529,s_BLACKBOX_ffff0000907d9988);
    if (DAT_ffff00009087bf14 == '\x03') {
      FUN_ffff00009077de54
                (s__s__d__blackbox_v3_ffff0000907d813b,s_InitJournalZoneHeader_ffff0000907ee86d,
                 0x244);
      _DAT_ffff00009087bf6c = 0xbf6c800;
      _DAT_ffff00009087bf68 = 0xb3b4400;
      _DAT_ffff00009087bf50 = 0x3000;
    }
    else if (DAT_ffff00009087bf14 == '\x02') {
      FUN_ffff00009077de54
                (s__s__d__blackbox_v2_ffff0000907d9991,s_InitJournalZoneHeader_ffff0000907ee86d,
                 0x23f);
      _DAT_ffff00009087bf68 = 0;
      _DAT_ffff00009087bf6c = 0;
      _DAT_ffff00009087bf50 = 0x2000;
    }
    else if (DAT_ffff00009087bf14 == '\x01') {
      FUN_ffff00009077de54
                (s__s__d__blackbox_v1__no_journal_z_ffff0000907eb4af,
                 s_InitJournalZoneHeader_ffff0000907ee86d,0x23b);
      goto LAB_ffff000090762dfc;
    }
    _DAT_ffff00009087bf48 = -0x9275f4d3b1e0c5b;
    _DAT_ffff00009087bf54 = 0xa44040000000400;
    _DAT_ffff00009087bf5c = 0xb00a8000ac10800;
    _DAT_ffff00009087bf64 = 0xac60c00;
  }
LAB_ffff000090762dfc:
  FUN_ffff000090761718(&DAT_ffff00009087bf48);
  return 1;
}




void FUN_ffff0000be04da90(undefined8 *param_1)

{
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__log_journal_zone_header_i_ffff0000be0c57a7,
               s_PrintJournalZoneHeader_ffff0000be0a7550,0x375);
    return;
  }
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_journal_z_ffff0000be0b6d05,
               s_PrintJournalZoneHeader_ffff0000be0a7550,0x379);
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d__print_black_box_journal_z_ffff0000be0b022d,
             s_PrintJournalZoneHeader_ffff0000be0a7550,0x37c);
  FUN_ffff0000be05fd98
            (s__s__d__magic__0x_llx_ffff0000be0abc7b,s_PrintJournalZoneHeader_ffff0000be0a7550,0x37d
             ,*param_1);
  FUN_ffff0000be05fd98
            (s__s__d__version__0x_x_ffff0000be0c5791,s_PrintJournalZoneHeader_ffff0000be0a7550,0x37e
             ,*(undefined4 *)(param_1 + 1));
  FUN_ffff0000be05fd98
            (s__s__d__header_size__0x_x_ffff0000be0bd78f,s_PrintJournalZoneHeader_ffff0000be0a7550,
             0x37f,*(undefined4 *)((long)param_1 + 0xc));
  FUN_ffff0000be05fd98
            (s__s__d__exception_reboot_log_offs_ffff0000be0ba2fa,
             s_PrintJournalZoneHeader_ffff0000be0a7550,0x380,*(undefined4 *)(param_1 + 2));
  FUN_ffff0000be05fd98
            (s__s__d__exception_shutdown_log_of_ffff0000be0b12bf,
             s_PrintJournalZoneHeader_ffff0000be0a7550,0x381,*(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff0000be05fd98
            (s__s__d__fastboot_mode_log_offset__ffff0000be0acdd0,
             s_PrintJournalZoneHeader_ffff0000be0a7550,0x382,*(undefined4 *)((long)param_1 + 0x1c));
  FUN_ffff0000be05fd98
            (s__s__d__recovery_mode_log_offset__ffff0000be0a96b3,
             s_PrintJournalZoneHeader_ffff0000be0a7550,899,*(undefined4 *)(param_1 + 3));
  FUN_ffff0000be05fd98
            (s__s__d__normal_boot_log_offset__0_ffff0000be0b12eb,
             s_PrintJournalZoneHeader_ffff0000be0a7550,900,*(undefined4 *)(param_1 + 4));
  FUN_ffff0000be05fd98
            (s__s__d__file_system_log_offset__0_ffff0000be0a96da,
             s_PrintJournalZoneHeader_ffff0000be0a7550,0x385,*(undefined4 *)((long)param_1 + 0x24));
  return;
}



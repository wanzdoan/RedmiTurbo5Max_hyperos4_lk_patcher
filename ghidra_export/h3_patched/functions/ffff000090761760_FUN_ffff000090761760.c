// ffff000090761760  size=276  FUN_ffff000090761760

void FUN_ffff000090761760(undefined8 *param_1)

{
  FUN_ffff00009077de54
            (s__s__d__print_black_box_journal_z_ffff0000907e1b88,
             s_PrintJournalZoneHeader_ffff0000907d4eae,0x37c);
  FUN_ffff00009077de54
            (s__s__d__magic__0x_llx_ffff0000907db46d,s_PrintJournalZoneHeader_ffff0000907d4eae,0x37d
             ,*param_1);
  FUN_ffff00009077de54
            (s__s__d__version__0x_x_ffff00009080122c,s_PrintJournalZoneHeader_ffff0000907d4eae,0x37e
             ,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077de54
            (s__s__d__header_size__0x_x_ffff0000907f4fe2,s_PrintJournalZoneHeader_ffff0000907d4eae,
             0x37f,*(undefined4 *)((long)param_1 + 0xc));
  FUN_ffff00009077de54
            (s__s__d__exception_reboot_log_offs_ffff0000907f01c1,
             s_PrintJournalZoneHeader_ffff0000907d4eae,0x380,*(undefined4 *)(param_1 + 2));
  FUN_ffff00009077de54
            (s__s__d__exception_shutdown_log_of_ffff0000907e35c3,
             s_PrintJournalZoneHeader_ffff0000907d4eae,0x381,*(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff00009077de54
            (s__s__d__fastboot_mode_log_offset__ffff0000907dcf47,
             s_PrintJournalZoneHeader_ffff0000907d4eae,0x382,*(undefined4 *)((long)param_1 + 0x1c));
  FUN_ffff00009077de54
            (s__s__d__recovery_mode_log_offset__ffff0000907d8188,
             s_PrintJournalZoneHeader_ffff0000907d4eae,899,*(undefined4 *)(param_1 + 3));
  FUN_ffff00009077de54
            (s__s__d__normal_boot_log_offset__0_ffff0000907e35ef,
             s_PrintJournalZoneHeader_ffff0000907d4eae,900,*(undefined4 *)(param_1 + 4));
  FUN_ffff00009077de54
            (s__s__d__file_system_log_offset__0_ffff0000907d81af,
             s_PrintJournalZoneHeader_ffff0000907d4eae,0x385,*(undefined4 *)((long)param_1 + 0x24));
  return;
}



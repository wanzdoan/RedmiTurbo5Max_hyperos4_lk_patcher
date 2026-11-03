// ffff0000907619f8  size=276  FUN_ffff0000907619f8

void FUN_ffff0000907619f8(undefined8 *param_1)

{
  FUN_ffff00009077e11c
            (s__s__d__print_black_box_journal_z_ffff0000907e1c70,
             s_PrintJournalZoneHeader_ffff0000907d4eeb,0x37c);
  FUN_ffff00009077e11c
            (s__s__d__magic__0x_llx_ffff0000907db58f,s_PrintJournalZoneHeader_ffff0000907d4eeb,0x37d
             ,*param_1);
  FUN_ffff00009077e11c
            (s__s__d__version__0x_x_ffff000090801408,s_PrintJournalZoneHeader_ffff0000907d4eeb,0x37e
             ,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077e11c
            (s__s__d__header_size__0x_x_ffff0000907f5166,s_PrintJournalZoneHeader_ffff0000907d4eeb,
             0x37f,*(undefined4 *)((long)param_1 + 0xc));
  FUN_ffff00009077e11c
            (s__s__d__exception_reboot_log_offs_ffff0000907f0349,
             s_PrintJournalZoneHeader_ffff0000907d4eeb,0x380,*(undefined4 *)(param_1 + 2));
  FUN_ffff00009077e11c
            (s__s__d__exception_shutdown_log_of_ffff0000907e36ab,
             s_PrintJournalZoneHeader_ffff0000907d4eeb,0x381,*(undefined4 *)((long)param_1 + 0x14));
  FUN_ffff00009077e11c
            (s__s__d__fastboot_mode_log_offset__ffff0000907dd034,
             s_PrintJournalZoneHeader_ffff0000907d4eeb,0x382,*(undefined4 *)((long)param_1 + 0x1c));
  FUN_ffff00009077e11c
            (s__s__d__recovery_mode_log_offset__ffff0000907d821e,
             s_PrintJournalZoneHeader_ffff0000907d4eeb,899,*(undefined4 *)(param_1 + 3));
  FUN_ffff00009077e11c
            (s__s__d__normal_boot_log_offset__0_ffff0000907e36d7,
             s_PrintJournalZoneHeader_ffff0000907d4eeb,900,*(undefined4 *)(param_1 + 4));
  FUN_ffff00009077e11c
            (s__s__d__file_system_log_offset__0_ffff0000907d8245,
             s_PrintJournalZoneHeader_ffff0000907d4eeb,0x385,*(undefined4 *)((long)param_1 + 0x24));
  return;
}



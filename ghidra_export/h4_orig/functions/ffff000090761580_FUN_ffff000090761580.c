// ffff000090761580  size=32  FUN_ffff000090761580

void FUN_ffff000090761580(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d__print_black_box_log_table_ffff0000907d9a86,
               s_PrintLogTableHeader_ffff0000907eced7,0x302);
    FUN_ffff00009077e11c
              (s__s__d__magic__0x_llx_ffff0000907db58f,s_PrintLogTableHeader_ffff0000907eced7,0x303,
               *param_1);
    FUN_ffff00009077e11c
              (s__s__d__version__0x_x_ffff000090801408,s_PrintLogTableHeader_ffff0000907eced7,0x304,
               *(undefined4 *)(param_1 + 1));
    FUN_ffff00009077e11c
              (s__s__d__header_size__0x_x_ffff0000907f5166,s_PrintLogTableHeader_ffff0000907eced7,
               0x305,*(undefined4 *)((long)param_1 + 0xc));
    FUN_ffff00009077e11c
              (s__s__d__boot_index__0x_x_ffff0000907f8bb3,s_PrintLogTableHeader_ffff0000907eced7,
               0x306,*(undefined4 *)(param_1 + 2));
    FUN_ffff00009077e11c
              (s__s__d__log_content_offset__0x_x_ffff0000907e368a,
               s_PrintLogTableHeader_ffff0000907eced7,0x307,*(undefined4 *)((long)param_1 + 0x14));
    FUN_ffff00009077e11c
              (s__s__d__minidump_is_valid__0x_x_ffff0000907cffb1,
               s_PrintLogTableHeader_ffff0000907eced7,0x308,*(undefined4 *)((long)param_1 + 0x1c));
    FUN_ffff00009077e11c
              (s__s__d__minidump_offset__0x_x_ffff0000907ff95a,
               s_PrintLogTableHeader_ffff0000907eced7,0x309,*(undefined4 *)(param_1 + 3));
    FUN_ffff00009077e11c
              (s__s__d__expdb_table_offset__0x_x_ffff0000907fdf3b,
               s_PrintLogTableHeader_ffff0000907eced7,0x30a,*(undefined4 *)(param_1 + 4));
    FUN_ffff00009077e11c
              (s__s__d__control_content_offset__0_ffff0000907e6966,
               s_PrintLogTableHeader_ffff0000907eced7,0x30b,*(undefined4 *)((long)param_1 + 0x24));
    FUN_ffff00009077e11c
              (s__s__d__fw_minidump_offset__0x_x_ffff0000907e5223,
               s_PrintLogTableHeader_ffff0000907eced7,0x30c,*(undefined4 *)(param_1 + 5));
    FUN_ffff00009077e11c
              (s__s__d__boot_content_offset__0x_x_ffff0000907eea0b,
               s_PrintLogTableHeader_ffff0000907eced7,0x30d,*(undefined4 *)((long)param_1 + 0x2c));
    FUN_ffff00009077e11c
              (s__s__d__control_zone_offset__0x_x_ffff0000907f34b2,
               s_PrintLogTableHeader_ffff0000907eced7,0x30e,*(undefined4 *)(param_1 + 6));
    FUN_ffff00009077e11c
              (s__s__d__record_zone_offset__0x_x_ffff0000907f5180,
               s_PrintLogTableHeader_ffff0000907eced7,0x30f,*(undefined4 *)((long)param_1 + 0x34));
    FUN_ffff00009077e11c
              (s__s__d__journal_zone_offset__0x_x_ffff0000907db5a5,
               s_PrintLogTableHeader_ffff0000907eced7,0x310,*(undefined4 *)(param_1 + 7));
    return;
  }
  FUN_ffff00009077e11c
            (s__s__d__log_header_is_null_ffff0000907d4e70,s_PrintLogTableHeader_ffff0000907eced7,
             0x2ff);
  return;
}



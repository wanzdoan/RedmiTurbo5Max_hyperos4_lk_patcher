
void FUN_ffff0000be04d660(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__print_black_box_log_table_ffff0000be0aaa55,
               s_PrintLogTableHeader_ffff0000be0b7dad,0x302);
    FUN_ffff0000be05fd98
              (s__s__d__magic__0x_llx_ffff0000be0abc7b,s_PrintLogTableHeader_ffff0000be0b7dad,0x303,
               *param_1);
    FUN_ffff0000be05fd98
              (s__s__d__version__0x_x_ffff0000be0c5791,s_PrintLogTableHeader_ffff0000be0b7dad,0x304,
               *(undefined4 *)(param_1 + 1));
    FUN_ffff0000be05fd98
              (s__s__d__header_size__0x_x_ffff0000be0bd78f,s_PrintLogTableHeader_ffff0000be0b7dad,
               0x305,*(undefined4 *)((long)param_1 + 0xc));
    FUN_ffff0000be05fd98
              (s__s__d__boot_index__0x_x_ffff0000be0bfe34,s_PrintLogTableHeader_ffff0000be0b7dad,
               0x306,*(undefined4 *)(param_1 + 2));
    FUN_ffff0000be05fd98
              (s__s__d__log_content_offset__0x_x_ffff0000be0b129e,
               s_PrintLogTableHeader_ffff0000be0b7dad,0x307,*(undefined4 *)((long)param_1 + 0x14));
    FUN_ffff0000be05fd98
              (s__s__d__minidump_is_valid__0x_x_ffff0000be0a3acb,
               s_PrintLogTableHeader_ffff0000be0b7dad,0x308,*(undefined4 *)((long)param_1 + 0x1c));
    FUN_ffff0000be05fd98
              (s__s__d__minidump_offset__0x_x_ffff0000be0c4633,
               s_PrintLogTableHeader_ffff0000be0b7dad,0x309,*(undefined4 *)(param_1 + 3));
    FUN_ffff0000be05fd98
              (s__s__d__expdb_table_offset__0x_x_ffff0000be0c3465,
               s_PrintLogTableHeader_ffff0000be0b7dad,0x30a,*(undefined4 *)(param_1 + 4));
    FUN_ffff0000be05fd98
              (s__s__d__control_content_offset__0_ffff0000be0b33d7,
               s_PrintLogTableHeader_ffff0000be0b7dad,0x30b,*(undefined4 *)((long)param_1 + 0x24));
    FUN_ffff0000be05fd98
              (s__s__d__fw_minidump_offset__0x_x_ffff0000be0b2465,
               s_PrintLogTableHeader_ffff0000be0b7dad,0x30c,*(undefined4 *)(param_1 + 5));
    FUN_ffff0000be05fd98
              (s__s__d__boot_content_offset__0x_x_ffff0000be0b9048,
               s_PrintLogTableHeader_ffff0000be0b7dad,0x30d,*(undefined4 *)((long)param_1 + 0x2c));
    FUN_ffff0000be05fd98
              (s__s__d__control_zone_offset__0x_x_ffff0000be0bc50d,
               s_PrintLogTableHeader_ffff0000be0b7dad,0x30e,*(undefined4 *)(param_1 + 6));
    FUN_ffff0000be05fd98
              (s__s__d__record_zone_offset__0x_x_ffff0000be0bd7a9,
               s_PrintLogTableHeader_ffff0000be0b7dad,0x30f,*(undefined4 *)((long)param_1 + 0x34));
    FUN_ffff0000be05fd98
              (s__s__d__journal_zone_offset__0x_x_ffff0000be0abc91,
               s_PrintLogTableHeader_ffff0000be0b7dad,0x310,*(undefined4 *)(param_1 + 7));
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d__log_header_is_null_ffff0000be0a74d5,s_PrintLogTableHeader_ffff0000be0b7dad,
             0x2ff);
  return;
}



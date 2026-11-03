
void FUN_ffff0000be04d9ac(undefined8 *param_1)

{
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__log_record_zone_header_is_ffff0000be0b906a,
               s_PrintRecordZoneHeader_ffff0000be0a8693,0x363);
    return;
  }
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_record_zo_ffff0000be0c573c,
               s_PrintRecordZoneHeader_ffff0000be0a8693,0x368);
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d__print_black_box_record_zo_ffff0000be0a7523,
             s_PrintRecordZoneHeader_ffff0000be0a8693,0x36b);
  FUN_ffff0000be05fd98
            (s__s__d__magic__0x_llx_ffff0000be0abc7b,s_PrintRecordZoneHeader_ffff0000be0a8693,0x36c,
             *param_1);
  FUN_ffff0000be05fd98
            (s__s__d__version__0x_x_ffff0000be0c5791,s_PrintRecordZoneHeader_ffff0000be0a8693,0x36d,
             *(undefined4 *)(param_1 + 1));
  FUN_ffff0000be05fd98
            (s__s__d__header_size__0x_x_ffff0000be0bd78f,s_PrintRecordZoneHeader_ffff0000be0a8693,
             0x36e,*(undefined4 *)((long)param_1 + 0xc));
  FUN_ffff0000be05fd98
            (s__s__d__fis_record_zone_offset__0_ffff0000be0aed47,
             s_PrintRecordZoneHeader_ffff0000be0a8693,0x36f,*(undefined4 *)(param_1 + 2));
  return;
}



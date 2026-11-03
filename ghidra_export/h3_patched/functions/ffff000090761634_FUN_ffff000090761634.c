// ffff000090761634  size=72  FUN_ffff000090761634

void FUN_ffff000090761634(undefined8 *param_1)

{
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d__log_record_zone_header_is_ffff0000907ee8a5,
               s_PrintRecordZoneHeader_ffff0000907d698f,0x363);
    return;
  }
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_record_zo_ffff0000908011d7,
               s_PrintRecordZoneHeader_ffff0000907d698f,0x368);
    return;
  }
  FUN_ffff00009077de54
            (s__s__d__print_black_box_record_zo_ffff0000907d4e81,
             s_PrintRecordZoneHeader_ffff0000907d698f,0x36b);
  FUN_ffff00009077de54
            (s__s__d__magic__0x_llx_ffff0000907db46d,s_PrintRecordZoneHeader_ffff0000907d698f,0x36c,
             *param_1);
  FUN_ffff00009077de54
            (s__s__d__version__0x_x_ffff00009080122c,s_PrintRecordZoneHeader_ffff0000907d698f,0x36d,
             *(undefined4 *)(param_1 + 1));
  FUN_ffff00009077de54
            (s__s__d__header_size__0x_x_ffff0000907f4fe2,s_PrintRecordZoneHeader_ffff0000907d698f,
             0x36e,*(undefined4 *)((long)param_1 + 0xc));
  FUN_ffff00009077de54
            (s__s__d__fis_record_zone_offset__0_ffff0000907e003a,
             s_PrintRecordZoneHeader_ffff0000907d698f,0x36f,*(undefined4 *)(param_1 + 2));
  return;
}



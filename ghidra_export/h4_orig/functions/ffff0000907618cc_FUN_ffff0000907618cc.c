// ffff0000907618cc  size=72  FUN_ffff0000907618cc

void FUN_ffff0000907618cc(undefined8 *param_1)

{
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d__log_record_zone_header_is_ffff0000907eea2d,
               s_PrintRecordZoneHeader_ffff0000907d6a25,0x363);
    return;
  }
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_record_zo_ffff0000908013b3,
               s_PrintRecordZoneHeader_ffff0000907d6a25,0x368);
    return;
  }
  FUN_ffff00009077e11c
            (s__s__d__print_black_box_record_zo_ffff0000907d4ebe,
             s_PrintRecordZoneHeader_ffff0000907d6a25,0x36b);
  FUN_ffff00009077e11c
            (s__s__d__magic__0x_llx_ffff0000907db58f,s_PrintRecordZoneHeader_ffff0000907d6a25,0x36c,
             *param_1);
  FUN_ffff00009077e11c
            (s__s__d__version__0x_x_ffff000090801408,s_PrintRecordZoneHeader_ffff0000907d6a25,0x36d,
             *(undefined4 *)(param_1 + 1));
  FUN_ffff00009077e11c
            (s__s__d__header_size__0x_x_ffff0000907f5166,s_PrintRecordZoneHeader_ffff0000907d6a25,
             0x36e,*(undefined4 *)((long)param_1 + 0xc));
  FUN_ffff00009077e11c
            (s__s__d__fis_record_zone_offset__0_ffff0000907e00f4,
             s_PrintRecordZoneHeader_ffff0000907d6a25,0x36f,*(undefined4 *)(param_1 + 2));
  return;
}



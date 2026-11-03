// ffff000090761914  size=156  FUN_ffff000090761914

void FUN_ffff000090761914(undefined8 *param_1)

{
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



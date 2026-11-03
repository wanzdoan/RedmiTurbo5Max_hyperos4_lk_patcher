// ffff00009076167c  size=156  FUN_ffff00009076167c

void FUN_ffff00009076167c(undefined8 *param_1)

{
  FUN_ffff00009077de54
            (s__s__d__print_black_box_record_zo_ffff0000907d4e81,
             s_PrintRecordZoneHeader_ffff0000907d698f,0x36b);
  FUN_ffff00009077de54
            (s__s__d__magic__0x_llx_ffff0000907db484,s_PrintRecordZoneHeader_ffff0000907d698f,0x36c,
             *param_1);
  FUN_ffff00009077de54
            (s__s__d__version__0x_x_ffff00009080122c,s_PrintRecordZoneHeader_ffff0000907d698f,0x36d,
             *(undefined4 *)(param_1 + 1));
  FUN_ffff00009077de54
            (s__s__d__header_size__0x_x_ffff0000907f4ff9,s_PrintRecordZoneHeader_ffff0000907d698f,
             0x36e,*(undefined4 *)((long)param_1 + 0xc));
  FUN_ffff00009077de54
            (s__s__d__fis_record_zone_offset__0_ffff0000907e0051,
             s_PrintRecordZoneHeader_ffff0000907d698f,0x36f,*(undefined4 *)(param_1 + 2));
  return;
}



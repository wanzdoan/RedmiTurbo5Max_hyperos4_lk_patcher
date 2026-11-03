// ffff000090761550  size=72  FUN_ffff000090761550

void FUN_ffff000090761550(undefined8 *param_1)

{
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d__log_control_zone_header_i_ffff0000907d9a46,
               s_PrintControlZoneHeader_ffff0000907e003a,0x352);
    return;
  }
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_control_z_ffff0000907d6951,
               s_PrintControlZoneHeader_ffff0000907e003a,0x356);
    return;
  }
  FUN_ffff00009077de54
            (s__s__d__print_black_box_control_z_ffff0000907f5034,
             s_PrintControlZoneHeader_ffff0000907e003a,0x359);
  FUN_ffff00009077de54
            (s__s__d__magic__0x_llx_ffff0000907db484,s_PrintControlZoneHeader_ffff0000907e003a,0x35a
             ,*param_1);
  FUN_ffff00009077de54
            (s__s__d__version__0x_x_ffff00009080122c,s_PrintControlZoneHeader_ffff0000907e003a,0x35b
             ,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077de54
            (s__s__d__header_size__0x_x_ffff0000907f4ff9,s_PrintControlZoneHeader_ffff0000907e003a,
             0x35c,*(undefined4 *)((long)param_1 + 0xc));
  FUN_ffff00009077de54
            (s__s__d__control_zone_offset__0x_x_ffff0000907f3326,
             s_PrintControlZoneHeader_ffff0000907e003a,0x35d,*(undefined4 *)(param_1 + 2));
  return;
}



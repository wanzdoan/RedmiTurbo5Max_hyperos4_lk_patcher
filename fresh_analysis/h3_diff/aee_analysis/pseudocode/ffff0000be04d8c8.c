
void FUN_ffff0000be04d8c8(undefined8 *param_1)

{
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__log_control_zone_header_i_ffff0000be0aaaac,
               s_PrintControlZoneHeader_ffff0000be0aed30,0x352);
    return;
  }
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_control_z_ffff0000be0a8655,
               s_PrintControlZoneHeader_ffff0000be0aed30,0x356);
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d__print_black_box_control_z_ffff0000be0bd7ca,
             s_PrintControlZoneHeader_ffff0000be0aed30,0x359);
  FUN_ffff0000be05fd98
            (s__s__d__magic__0x_llx_ffff0000be0abc7b,s_PrintControlZoneHeader_ffff0000be0aed30,0x35a
             ,*param_1);
  FUN_ffff0000be05fd98
            (s__s__d__version__0x_x_ffff0000be0c5791,s_PrintControlZoneHeader_ffff0000be0aed30,0x35b
             ,*(undefined4 *)(param_1 + 1));
  FUN_ffff0000be05fd98
            (s__s__d__header_size__0x_x_ffff0000be0bd78f,s_PrintControlZoneHeader_ffff0000be0aed30,
             0x35c,*(undefined4 *)((long)param_1 + 0xc));
  FUN_ffff0000be05fd98
            (s__s__d__control_zone_offset__0x_x_ffff0000be0bc50d,
             s_PrintControlZoneHeader_ffff0000be0aed30,0x35d,*(undefined4 *)(param_1 + 2));
  return;
}



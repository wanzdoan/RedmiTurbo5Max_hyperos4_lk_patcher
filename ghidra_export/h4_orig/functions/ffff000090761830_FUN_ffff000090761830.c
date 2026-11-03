// ffff000090761830  size=156  FUN_ffff000090761830

void FUN_ffff000090761830(undefined8 *param_1)

{
  FUN_ffff00009077e11c
            (s__s__d__print_black_box_control_z_ffff0000907f51a1,
             s_PrintControlZoneHeader_ffff0000907e00dd,0x359);
  FUN_ffff00009077e11c
            (s__s__d__magic__0x_llx_ffff0000907db58f,s_PrintControlZoneHeader_ffff0000907e00dd,0x35a
             ,*param_1);
  FUN_ffff00009077e11c
            (s__s__d__version__0x_x_ffff000090801408,s_PrintControlZoneHeader_ffff0000907e00dd,0x35b
             ,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077e11c
            (s__s__d__header_size__0x_x_ffff0000907f5166,s_PrintControlZoneHeader_ffff0000907e00dd,
             0x35c,*(undefined4 *)((long)param_1 + 0xc));
  FUN_ffff00009077e11c
            (s__s__d__control_zone_offset__0x_x_ffff0000907f34b2,
             s_PrintControlZoneHeader_ffff0000907e00dd,0x35d,*(undefined4 *)(param_1 + 2));
  return;
}



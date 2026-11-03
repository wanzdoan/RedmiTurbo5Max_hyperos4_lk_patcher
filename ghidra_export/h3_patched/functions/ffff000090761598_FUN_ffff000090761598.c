// ffff000090761598  size=156  FUN_ffff000090761598

void FUN_ffff000090761598(undefined8 *param_1)

{
  FUN_ffff00009077de54
            (s__s__d__print_black_box_control_z_ffff0000907f501d,
             s_PrintControlZoneHeader_ffff0000907e0023,0x359);
  FUN_ffff00009077de54
            (s__s__d__magic__0x_llx_ffff0000907db46d,s_PrintControlZoneHeader_ffff0000907e0023,0x35a
             ,*param_1);
  FUN_ffff00009077de54
            (s__s__d__version__0x_x_ffff00009080122c,s_PrintControlZoneHeader_ffff0000907e0023,0x35b
             ,*(undefined4 *)(param_1 + 1));
  FUN_ffff00009077de54
            (s__s__d__header_size__0x_x_ffff0000907f4fe2,s_PrintControlZoneHeader_ffff0000907e0023,
             0x35c,*(undefined4 *)((long)param_1 + 0xc));
  FUN_ffff00009077de54
            (s__s__d__control_zone_offset__0x_x_ffff0000907f330f,
             s_PrintControlZoneHeader_ffff0000907e0023,0x35d,*(undefined4 *)(param_1 + 2));
  return;
}



// ffff000090740378  size=40  FUN_ffff000090740378

undefined8 FUN_ffff000090740378(undefined8 param_1,byte param_2)

{
  if (0xf < param_2) {
    FUN_ffff00009077de54(s__SPMI__failed_to_get_spmi_device_ffff0000907e4b84);
    return 0;
  }
  return *(undefined8 *)((ulong)param_2 * 8 + -0xffff6f790dd0);
}



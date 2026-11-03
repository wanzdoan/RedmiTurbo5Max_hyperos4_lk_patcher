
undefined8 FUN_ffff0000b80263f0(undefined8 param_1,byte param_2)

{
  if (0xf < param_2) {
    FUN_ffff0000b804c33c(s__SPMI__failed_to_get_spmi_device_ffff0000b8090ca6);
    return 0;
  }
  return *(undefined8 *)(&DAT_ffff0000b811cd90 + (ulong)param_2 * 8);
}



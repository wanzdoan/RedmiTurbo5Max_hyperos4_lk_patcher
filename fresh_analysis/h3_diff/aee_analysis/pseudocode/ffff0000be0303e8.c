
undefined8 FUN_ffff0000be0303e8(undefined8 param_1,byte param_2)

{
  if (0xf < param_2) {
    FUN_ffff0000be05fd98(s__SPMI__failed_to_get_spmi_device_ffff0000be0b1fa1);
    return 0;
  }
  return *(undefined8 *)(&DAT_ffff0000be119580 + (ulong)param_2 * 8);
}



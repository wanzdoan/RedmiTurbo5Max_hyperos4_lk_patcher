// ffff000090740610  size=40  FUN_ffff000090740610

undefined8 FUN_ffff000090740610(undefined8 param_1,byte param_2)

{
  if (0xf < param_2) {
    FUN_ffff00009077e11c(s__SPMI__failed_to_get_spmi_device_ffff0000907e4c9f);
    return 0;
  }
  return *(undefined8 *)((ulong)param_2 * 8 + -0xffff6f790d38);
}



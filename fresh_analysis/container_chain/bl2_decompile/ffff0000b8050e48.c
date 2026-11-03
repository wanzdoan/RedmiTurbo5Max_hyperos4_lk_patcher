
undefined8 FUN_ffff0000b8050e48(uint param_1)

{
  if (param_1 < 0x10f) {
    if ((byte)(&DAT_ffff0000b81062d8)[param_1] != 0xff) {
      **(uint **)(&DAT_ffff0000b8105a60 + (ulong)param_1 * 8) =
           **(uint **)(&DAT_ffff0000b8105a60 + (ulong)param_1 * 8) |
           1 << (ulong)((byte)(&DAT_ffff0000b81062d8)[param_1] & 0x1f);
      return 0;
    }
    FUN_ffff0000b804c33c(s__GPIO__there_no_iocfg_pin_to_set_ffff0000b809be97);
  }
  return 0xfffffffe;
}



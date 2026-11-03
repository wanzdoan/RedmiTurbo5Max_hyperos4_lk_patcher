// ffff00009078ba24  size=28  FUN_ffff00009078ba24

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009078ba24(undefined8 param_1)

{
  if (_DAT_ffff00009088a0c0 != 0) {
    FUN_ffff00009077e11c(s__USB__only_one_gadget_supported_ffff0000907f8ef7);
    return 0xfffffff0;
  }
  _DAT_ffff00009088a0c0 = param_1;
  return 0;
}



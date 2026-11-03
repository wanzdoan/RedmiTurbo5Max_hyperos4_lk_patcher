
undefined8 FUN_ffff0000be0695b0(undefined8 param_1)

{
  if (DAT_ffff0000be134748 != 0) {
    FUN_ffff0000be05fd98(s__USB__only_one_gadget_supported_ffff0000be0c0030);
    return 0xfffffff0;
  }
  DAT_ffff0000be134748 = param_1;
  return 0;
}



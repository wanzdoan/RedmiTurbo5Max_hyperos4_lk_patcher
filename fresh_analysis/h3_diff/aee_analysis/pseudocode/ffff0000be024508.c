
void FUN_ffff0000be024508(long param_1)

{
  if (param_1 != 0) {
    FUN_ffff0000be0607b0(&DAT_ffff0000be117bb8,param_1,0x110);
    DAT_ffff0000be117cb0 = param_1 + 0x110;
    DAT_ffff0000be117bb0 = 1;
    FUN_ffff0000be05fd98(s_iframe_stored__ffff0000be0a5974);
    return;
  }
  return;
}



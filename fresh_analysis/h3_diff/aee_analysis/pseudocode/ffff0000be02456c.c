
void FUN_ffff0000be02456c(long param_1)

{
  if (param_1 != 0) {
    FUN_ffff0000be0607b0(&DAT_ffff0000be117bb8,param_1,0x90);
    FUN_ffff0000be0607b0(&DAT_ffff0000be117ca8,param_1 + 0x90,8);
    FUN_ffff0000be0607b0(&DAT_ffff0000be117cb8,param_1 + 0xa0,8);
    FUN_ffff0000be0607b0(&DAT_ffff0000be117cc0,param_1 + 0xa8,8);
    FUN_ffff0000be0607b0(&DAT_ffff0000be117cb0,param_1 + 0x98,8);
    DAT_ffff0000be117bb0 = 1;
    FUN_ffff0000be05fd98(s_iframe_short_stored__ffff0000be0a4664);
    return;
  }
  return;
}



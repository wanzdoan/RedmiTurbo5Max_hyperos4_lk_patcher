
void FUN_ffff0000be04e660(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__print_black_box_selinux_c_ffff0000be0b1310,
               s_PrintSelinuxConfig_ffff0000be0b3413,0x414);
    FUN_ffff0000be05fd98
              (s__s__d__magic__0x_llx_ffff0000be0abc7b,s_PrintSelinuxConfig_ffff0000be0b3413,0x415,
               *param_1);
    FUN_ffff0000be05fd98
              (s__s__d__isSelinuxPermissive___d_ffff0000be0aaad4,
               s_PrintSelinuxConfig_ffff0000be0b3413,0x416,*(undefined4 *)(param_1 + 1));
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d__selinux_config_is_null_ffff0000be0bb421,s_PrintSelinuxConfig_ffff0000be0b3413,
             0x411);
  return;
}



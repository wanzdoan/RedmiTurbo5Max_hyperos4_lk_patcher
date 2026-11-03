// ffff0000907622e8  size=32  FUN_ffff0000907622e8

void FUN_ffff0000907622e8(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d__print_black_box_selinux_c_ffff0000907e3614,
               s_PrintSelinuxConfig_ffff0000907e6877,0x414);
    FUN_ffff00009077de54
              (s__s__d__magic__0x_llx_ffff0000907db46d,s_PrintSelinuxConfig_ffff0000907e6877,0x415,
               *param_1);
    FUN_ffff00009077de54
              (s__s__d__isSelinuxPermissive___d_ffff0000907d9a6e,
               s_PrintSelinuxConfig_ffff0000907e6877,0x416,*(undefined4 *)(param_1 + 1));
    return;
  }
  FUN_ffff00009077de54
            (s__s__d__selinux_config_is_null_ffff0000907f19f4,s_PrintSelinuxConfig_ffff0000907e6877,
             0x411);
  return;
}



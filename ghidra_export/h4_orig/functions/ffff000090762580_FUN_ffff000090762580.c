// ffff000090762580  size=32  FUN_ffff000090762580

void FUN_ffff000090762580(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d__print_black_box_selinux_c_ffff0000907e36fc,
               s_PrintSelinuxConfig_ffff0000907e69a2,0x414);
    FUN_ffff00009077e11c
              (s__s__d__magic__0x_llx_ffff0000907db58f,s_PrintSelinuxConfig_ffff0000907e69a2,0x415,
               *param_1);
    FUN_ffff00009077e11c
              (s__s__d__isSelinuxPermissive___d_ffff0000907d9b05,
               s_PrintSelinuxConfig_ffff0000907e69a2,0x416,*(undefined4 *)(param_1 + 1));
    return;
  }
  FUN_ffff00009077e11c
            (s__s__d__selinux_config_is_null_ffff0000907f1b7c,s_PrintSelinuxConfig_ffff0000907e69a2,
             0x411);
  return;
}



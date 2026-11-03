// ffff000090766e98  size=256  FUN_ffff000090766e98

undefined8 FUN_ffff000090766e98(undefined8 *param_1)

{
  ulong uVar1;
  undefined8 uVar2;
  int local_24;
  
  local_24 = 0;
  uVar1 = FUN_ffff0000907661c0(&local_24,3);
  uVar2 = 0;
  if (((uVar1 & 1) != 0) && (local_24 != 0)) {
    uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,param_1,local_24,0xc);
    if ((uVar1 & 1) == 0) {
      FUN_ffff00009077e11c
                (s__s__d____s___get_selinux_config_e_ffff0000907fc24b,
                 s_GetBlackBoxSelinuxConfig_ffff0000907fc272,0xb27,s_BLACKBOX_ffff0000907d9a1f);
      uVar2 = 0;
    }
    else {
      if (param_1 == (undefined8 *)0x0) {
        FUN_ffff00009077e11c
                  (s__s__d__selinux_config_is_null_ffff0000907f1b7c,
                   s_PrintSelinuxConfig_ffff0000907e69a2,0x411);
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d__print_black_box_selinux_c_ffff0000907e36fc,
                   s_PrintSelinuxConfig_ffff0000907e69a2,0x414);
        FUN_ffff00009077e11c
                  (s__s__d__magic__0x_llx_ffff0000907db58f,s_PrintSelinuxConfig_ffff0000907e69a2,
                   0x415,*param_1);
        FUN_ffff00009077e11c
                  (s__s__d__isSelinuxPermissive___d_ffff0000907d9b05,
                   s_PrintSelinuxConfig_ffff0000907e69a2,0x416,*(undefined4 *)(param_1 + 1));
      }
      uVar2 = 1;
    }
  }
  return uVar2;
}



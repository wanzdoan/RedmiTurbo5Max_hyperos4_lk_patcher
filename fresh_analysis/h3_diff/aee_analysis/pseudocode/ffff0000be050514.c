
undefined8 FUN_ffff0000be050514(undefined8 *param_1)

{
  ulong uVar1;
  undefined8 uVar2;
  int local_24;
  
  local_24 = 0;
  uVar1 = FUN_ffff0000be04fba4(&local_24,3);
  uVar2 = 0;
  if (((uVar1 & 1) != 0) && (local_24 != 0)) {
    uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,param_1,local_24,0xc);
    if ((uVar1 & 1) == 0) {
      FUN_ffff0000be05fd98
                (s__s__d____s___get_selinux_config_e_ffff0000be0c21ef,
                 s_GetBlackBoxSelinuxConfig_ffff0000be0c2216,0xb27,s_BLACKBOX_ffff0000be0aa9ee);
      uVar2 = 0;
    }
    else {
      if (param_1 == (undefined8 *)0x0) {
        FUN_ffff0000be05fd98
                  (s__s__d__selinux_config_is_null_ffff0000be0bb421,
                   s_PrintSelinuxConfig_ffff0000be0b3413,0x411);
      }
      else {
        FUN_ffff0000be05fd98
                  (s__s__d__print_black_box_selinux_c_ffff0000be0b1310,
                   s_PrintSelinuxConfig_ffff0000be0b3413,0x414);
        FUN_ffff0000be05fd98
                  (s__s__d__magic__0x_llx_ffff0000be0abc7b,s_PrintSelinuxConfig_ffff0000be0b3413,
                   0x415,*param_1);
        FUN_ffff0000be05fd98
                  (s__s__d__isSelinuxPermissive___d_ffff0000be0aaad4,
                   s_PrintSelinuxConfig_ffff0000be0b3413,0x416,*(undefined4 *)(param_1 + 1));
      }
      uVar2 = 1;
    }
  }
  return uVar2;
}



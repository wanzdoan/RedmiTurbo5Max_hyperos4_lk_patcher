
void FUN_ffff000090766f98(void)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  long local_30;
  undefined4 local_28;
  int local_4;
  
  local_28 = 0;
  local_30 = 0;
  uVar1 = FUN_ffff000090766e98(&local_30);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_selinux_config_f_ffff0000907fc28b;
    uVar3 = 0xb4a;
  }
  else {
    if (local_30 != 0x6d3fe5a8c9b1e2cf) {
      FUN_ffff00009077e11c
                (s__s__d____s___the_magic_is_incorr_ffff0000907f0447,
                 s_set_blackbox_selinux_permissive_ffff0000907d002b,0xb4e,
                 s_BLACKBOX_ffff0000907d9a1f);
      local_30 = 0x6d3fe5a8c9b1e2cf;
    }
    local_28 = 1;
    FUN_ffff00009077e11c
              (s__s__d__print_black_box_selinux_c_ffff0000907e36fc,
               s_PrintSelinuxConfig_ffff0000907e69a2,0x414);
    FUN_ffff00009077e11c
              (s__s__d__magic__0x_llx_ffff0000907db58f,s_PrintSelinuxConfig_ffff0000907e69a2,0x415,
               local_30);
    FUN_ffff00009077e11c
              (s__s__d__isSelinuxPermissive___d_ffff0000907d9b05,
               s_PrintSelinuxConfig_ffff0000907e69a2,0x416,local_28);
    local_4 = 0;
    uVar1 = FUN_ffff0000907661c0(&local_4,3);
    if (((uVar1 & 1) != 0) && (local_4 != 0)) {
      uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_30,local_4,0xc);
      if ((uVar1 & 1) != 0) {
        return;
      }
      FUN_ffff00009077e11c
                (s__s__d____s___write_selinux_confi_ffff0000907e9db7,
                 s_SetBlackBoxSelinuxConfig_ffff0000907e01e5,0xb3c,s_BLACKBOX_ffff0000907d9a1f);
    }
    pcVar2 = s__s__d____s___set_selinux_config_f_ffff0000907fe050;
    uVar3 = 0xb55;
  }
  FUN_ffff00009077e11c
            (pcVar2,s_set_blackbox_selinux_permissive_ffff0000907d002b,uVar3,
             s_BLACKBOX_ffff0000907d9a1f);
  return;
}



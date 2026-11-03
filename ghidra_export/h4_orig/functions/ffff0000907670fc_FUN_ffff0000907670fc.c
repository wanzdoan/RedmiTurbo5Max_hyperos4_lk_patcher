// ffff0000907670fc  size=416  FUN_ffff0000907670fc

undefined8 FUN_ffff0000907670fc(void)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  long local_30;
  int local_28;
  int local_4;
  
  local_28 = 0;
  local_30 = 0;
  uVar1 = FUN_ffff000090766e98(&local_30);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_selinux_config_f_ffff0000907fc28b;
    uVar3 = 0xb63;
  }
  else if (local_30 == 0x6d3fe5a8c9b1e2cf) {
    if (local_28 == 0) {
      return 0;
    }
    local_28 = 0;
    local_4 = 0;
    uVar1 = FUN_ffff0000907661c0(&local_4,3);
    if (((uVar1 & 1) != 0) && (local_4 != 0)) {
      uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_30,local_4,0xc);
      if ((uVar1 & 1) != 0) {
        return 1;
      }
      FUN_ffff00009077e11c
                (s__s__d____s___write_selinux_confi_ffff0000907e9db7,
                 s_SetBlackBoxSelinuxConfig_ffff0000907e01e5,0xb3c,s_BLACKBOX_ffff0000907d9a1f);
    }
    pcVar2 = s__s__d____s___set_selinux_config_f_ffff0000907d1c24;
    uVar3 = 0xb74;
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d____s___the_magic_is_incorr_ffff0000907fc2b4,
               s_should_set_selinux_permissive_ffff0000907e9de0,0xb68,s_BLACKBOX_ffff0000907d9a1f);
    local_30 = 0x6d3fe5a8c9b1e2cf;
    local_28 = 0;
    local_4 = 0;
    uVar1 = FUN_ffff0000907661c0(&local_4,3);
    if (((uVar1 & 1) != 0) && (local_4 != 0)) {
      uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_30,local_4,0xc);
      if ((uVar1 & 1) != 0) {
        return 0;
      }
      FUN_ffff00009077e11c
                (s__s__d____s___write_selinux_confi_ffff0000907e9db7,
                 s_SetBlackBoxSelinuxConfig_ffff0000907e01e5,0xb3c,s_BLACKBOX_ffff0000907d9a1f);
    }
    pcVar2 = s__s__d____s___set_selinux_config_f_ffff0000907dd1bc;
    uVar3 = 0xb6c;
  }
  FUN_ffff00009077e11c
            (pcVar2,s_should_set_selinux_permissive_ffff0000907e9de0,uVar3,
             s_BLACKBOX_ffff0000907d9a1f);
  return 0;
}



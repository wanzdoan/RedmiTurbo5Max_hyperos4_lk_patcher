// ffff000090766e64  size=416  FUN_ffff000090766e64

undefined8 FUN_ffff000090766e64(void)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  long local_30;
  int local_28;
  int local_4;
  
  local_28 = 0;
  local_30 = 0;
  uVar1 = FUN_ffff000090766c00(&local_30);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_selinux_config_f_ffff0000907fc0ef;
    uVar3 = 0xb63;
  }
  else if (local_30 == 0x6d3fe5a8c9b1e2cf) {
    if (local_28 == 0) {
      return 0;
    }
    local_28 = 0;
    local_4 = 0;
    uVar1 = FUN_ffff000090765f28(&local_4,3);
    if (((uVar1 & 1) != 0) && (local_4 != 0)) {
      uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&local_30,local_4,0xc);
      if ((uVar1 & 1) != 0) {
        return 1;
      }
      FUN_ffff00009077de54
                (s__s__d____s___write_selinux_confi_ffff0000907e9c5c,
                 s_SetBlackBoxSelinuxConfig_ffff0000907e012b,0xb3c,s_BLACKBOX_ffff0000907d9988);
    }
    pcVar2 = s__s__d____s___set_selinux_config_f_ffff0000907d1be7;
    uVar3 = 0xb74;
  }
  else {
    FUN_ffff00009077de54
              (s__s__d____s___the_magic_is_incorr_ffff0000907fc118,
               s_should_set_selinux_permissive_ffff0000907e9c85,0xb68,s_BLACKBOX_ffff0000907d9988);
    local_30 = 0x6d3fe5a8c9b1e2cf;
    local_28 = 0;
    local_4 = 0;
    uVar1 = FUN_ffff000090765f28(&local_4,3);
    if (((uVar1 & 1) != 0) && (local_4 != 0)) {
      uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&local_30,local_4,0xc);
      if ((uVar1 & 1) != 0) {
        return 0;
      }
      FUN_ffff00009077de54
                (s__s__d____s___write_selinux_confi_ffff0000907e9c5c,
                 s_SetBlackBoxSelinuxConfig_ffff0000907e012b,0xb3c,s_BLACKBOX_ffff0000907d9988);
    }
    pcVar2 = s__s__d____s___set_selinux_config_f_ffff0000907dd0cf;
    uVar3 = 0xb6c;
  }
  FUN_ffff00009077de54
            (pcVar2,s_should_set_selinux_permissive_ffff0000907e9c85,uVar3,
             s_BLACKBOX_ffff0000907d9988);
  return 0;
}



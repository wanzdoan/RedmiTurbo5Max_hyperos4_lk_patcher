// ffff000090766d00  size=356  FUN_ffff000090766d00

void FUN_ffff000090766d00(void)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  long local_30;
  undefined4 local_28;
  int local_4;
  
  local_28 = 0;
  local_30 = 0;
  uVar1 = FUN_ffff000090766c00(&local_30);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_selinux_config_f_ffff0000907fc0ef;
    uVar3 = 0xb4a;
  }
  else {
    if (local_30 != 0x6d3fe5a8c9b1e2cf) {
      FUN_ffff00009077de54
                (s__s__d____s___the_magic_is_incorr_ffff0000907f02bf,
                 s_set_blackbox_selinux_permissive_ffff0000907d000c,0xb4e,
                 s_BLACKBOX_ffff0000907d9988);
      local_30 = 0x6d3fe5a8c9b1e2cf;
    }
    local_28 = 1;
    FUN_ffff00009077de54
              (s__s__d__print_black_box_selinux_c_ffff0000907e3614,
               s_PrintSelinuxConfig_ffff0000907e6877,0x414);
    FUN_ffff00009077de54
              (s__s__d__magic__0x_llx_ffff0000907db46d,s_PrintSelinuxConfig_ffff0000907e6877,0x415,
               local_30);
    FUN_ffff00009077de54
              (s__s__d__isSelinuxPermissive___d_ffff0000907d9a6e,
               s_PrintSelinuxConfig_ffff0000907e6877,0x416,local_28);
    local_4 = 0;
    uVar1 = FUN_ffff000090765f28(&local_4,3);
    if (((uVar1 & 1) != 0) && (local_4 != 0)) {
      uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&local_30,local_4,0xc);
      if ((uVar1 & 1) != 0) {
        return;
      }
      FUN_ffff00009077de54
                (s__s__d____s___write_selinux_confi_ffff0000907e9c5c,
                 s_SetBlackBoxSelinuxConfig_ffff0000907e012b,0xb3c,s_BLACKBOX_ffff0000907d9988);
    }
    pcVar2 = s__s__d____s___set_selinux_config_f_ffff0000907fdea8;
    uVar3 = 0xb55;
  }
  FUN_ffff00009077de54
            (pcVar2,s_set_blackbox_selinux_permissive_ffff0000907d000c,uVar3,
             s_BLACKBOX_ffff0000907d9988);
  return;
}



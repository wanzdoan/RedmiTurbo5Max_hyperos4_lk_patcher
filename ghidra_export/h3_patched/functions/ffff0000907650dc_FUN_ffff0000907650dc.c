// ffff0000907650dc  size=1472  FUN_ffff0000907650dc

void FUN_ffff0000907650dc(void)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  long lVar5;
  char *pcVar6;
  undefined8 uVar7;
  undefined8 *puVar8;
  ulong uVar9;
  undefined1 auStack_f58 [64];
  undefined8 auStack_f18 [248];
  long local_758;
  undefined4 local_750;
  undefined8 local_74c;
  undefined1 auStack_744 [320];
  undefined1 auStack_604 [1284];
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined4 local_58;
  
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_100 = 0;
  uStack_f8 = 0;
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_fastboot_r_ffff0000907eb55a,
               s_record_details_of_recovery_to_bl_ffff0000907e36e0,0xc57);
    return;
  }
  FUN_ffff00009077eb88(&local_758,0,0x654);
  FUN_ffff00009077eb88(auStack_f58,0,0x800);
  uVar3 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c04,&local_758,0xa420c00,0x654);
  if ((uVar3 & 1) == 0) {
    pcVar6 = s__s__d____s__get_recovery_record_z_ffff0000907f02f0;
    uVar7 = 0xc5c;
    goto LAB_ffff000090765678;
  }
  if (local_758 != -0x4a37160f5d2c3b19) {
    FUN_ffff00009077de54
              (s__s__d____s__recovery_record_zone_ffff0000907d8324,
               s_record_details_of_recovery_to_bl_ffff0000907e36e0,0xc60,s_BLACKBOX_ffff0000907d9988
              );
    if (DAT_ffff00009087bf14 == '\x03') {
      local_750 = 0x3000;
LAB_ffff00009076524c:
      local_758 = -0x4a37160f5d2c3b19;
      local_74c = 0;
      FUN_ffff00009077eb88(auStack_744,0,0x140);
      FUN_ffff00009077eb88(auStack_604,0,0x500);
    }
    else {
      if (DAT_ffff00009087bf14 == '\x02') {
        local_750 = 0x2000;
        goto LAB_ffff00009076524c;
      }
      if (DAT_ffff00009087bf14 != '\x01') goto LAB_ffff00009076524c;
      FUN_ffff00009077de54
                (s__s__d__blackbox_v1__no_recovery_r_ffff0000907ff957,
                 s_init_blackbox_recovery_record_zo_ffff0000907d9bbe,0xc10);
    }
    uVar3 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&local_758,0xa420c00,0x654);
    if ((uVar3 & 1) == 0) {
      pcVar6 = s__s__d____s__write_recovery_recor_ffff0000907f1b73;
      uVar7 = 0xc64;
      goto LAB_ffff000090765678;
    }
  }
  iVar1 = (int)local_74c;
  uVar9 = local_74c & 0xffffffff;
  local_58 = 0;
  local_60 = 0;
  FUN_ffff000090737384(&local_60);
  FUN_ffff00009077f434
            (&local_80,0x20,s__04d__02d_02d__02d_02d_02d_ffff0000907f3396,local_58._2_2_,
             (undefined2)local_58,local_60._6_2_,local_60._4_2_ + 8,local_60._2_2_,
             (undefined2)local_60);
  FUN_ffff00009077ef1c(auStack_744 + uVar9 * 0x20,&local_80,0x20);
  uVar3 = FUN_ffff000090767298(auStack_f58);
  if ((uVar3 & 1) == 0) {
    pcVar6 = s__s__d____s__read_bcb_from_misc_p_ffff0000907de928;
    uVar7 = 0xc6f;
    goto LAB_ffff000090765678;
  }
  lVar4 = FUN_ffff00009077ef88(auStack_f18);
  if (DAT_ffff00009082192c == '\0') {
    FUN_ffff00009077de54
              (s__s__d____s__enter_recovery_mode_b_ffff0000907dd14c,
               s_record_details_of_recovery_to_bl_ffff0000907e36e0,0xc76,s_BLACKBOX_ffff0000907d9988
              );
    pcVar6 = s_key_press_ffff0000908013ee;
LAB_ffff000090765508:
    FUN_ffff00009077efe4(&local_100,pcVar6,0x7f);
    puVar8 = &local_100;
LAB_ffff000090765518:
    FUN_ffff00009077ef1c(auStack_604 + uVar9 * 0x80,puVar8,0x80);
  }
  else {
    local_60 = local_60 & 0xffffffff00000000;
    lVar5 = FUN_ffff0000907606e0(&local_60);
    FUN_ffff00009077de54
              (s__s__d__the_size_of_last_kmsg_ret_ffff0000907d4f84,
               s_is_triggered_by_adb_reboot_ffff0000907d1aa0,0x739,local_60 & 0xffffffff);
    if ((lVar5 == 0) || ((int)local_60 == 0)) {
      thunk_FUN_ffff0000907c2ad8(lVar5);
    }
    else {
      uVar3 = FUN_ffff000090763f18(lVar5,s__system_bin_reboot_ffff0000907eb4ed,0);
      thunk_FUN_ffff0000907c2ad8(lVar5);
      if ((uVar3 & 1) != 0) {
        FUN_ffff00009077de54
                  (s__s__d____s__enter_recovery_mode_b_ffff0000907d6a69,
                   s_record_details_of_recovery_to_bl_ffff0000907e36e0,0xc7a,
                   s_BLACKBOX_ffff0000907d9988);
        pcVar6 = s_adb_reboot_recovery_ffff0000907f031e;
        goto LAB_ffff000090765508;
      }
    }
    local_60 = local_60 & 0xffffffff00000000;
    lVar5 = FUN_ffff0000907606e0(&local_60);
    FUN_ffff00009077de54
              (s__s__d__the_size_of_last_kmsg_ret_ffff0000907d4f84,
               s_is_triggered_by_sys_powerctl_ffff0000907e1c94,0x74c,local_60 & 0xffffffff);
    if ((lVar5 == 0) || ((int)local_60 == 0)) {
      thunk_FUN_ffff0000907c2ad8(lVar5);
    }
    else {
      uVar3 = FUN_ffff000090763f18(lVar5,s_sys_powerctl_ffff0000907f6e82,0);
      thunk_FUN_ffff0000907c2ad8(lVar5);
      if ((uVar3 & 1) != 0) {
        FUN_ffff00009077de54
                  (s__s__d____s__enter_recovery_mode_b_ffff0000907de95a,
                   s_record_details_of_recovery_to_bl_ffff0000907e36e0,0xc7e,
                   s_BLACKBOX_ffff0000907d9988);
        pcVar6 = s_sys_powerctl_reboot_recovery_ffff0000907e1d3a;
        goto LAB_ffff000090765508;
      }
    }
    lVar5 = FUN_ffff00009077ef88(auStack_f18);
    puVar8 = auStack_f18;
    if (lVar5 != 0) goto LAB_ffff000090765518;
    iVar2 = FUN_ffff00009077ede4(auStack_f58,s_boot_fastboot_ffff0000907d72ae);
    if (iVar2 != 0) {
      FUN_ffff00009077de54
                (s__s__d____s__enter_recovery_mode_b_ffff000090802bcc,
                 s_record_details_of_recovery_to_bl_ffff0000907e36e0,0xc84,
                 s_BLACKBOX_ffff0000907d9988);
      pcVar6 = s_unknown_ffff0000907d64e8;
      goto LAB_ffff000090765508;
    }
  }
  if (lVar4 == 0) {
    iVar2 = FUN_ffff00009077ede4(auStack_f58,s_boot_fastboot_ffff0000907d72ae);
    if (iVar2 != 0) {
      FUN_ffff00009077f434
                (auStack_f18,0x300,s_recovery_reason__s_time__s_ffff0000907d365d,&local_100,
                 &local_80);
    }
  }
  else {
    FUN_ffff00009077f434
              ((long)auStack_f18 + lVar4,0x300 - lVar4,s_time__s_ffff0000907e6a12,&local_80);
  }
  FUN_ffff00009077de54
            (s__s__d____s__misc_msg_command_____ffff0000907e5243,
             s_record_details_of_recovery_to_bl_ffff0000907e36e0,0xc95,s_BLACKBOX_ffff0000907d9988,
             auStack_f58);
  FUN_ffff00009077de54
            (s__s__d____s__current_len____lu_ffff0000907e8262,
             s_record_details_of_recovery_to_bl_ffff0000907e36e0,0xc96,s_BLACKBOX_ffff0000907d9988,
             lVar4);
  FUN_ffff00009077de54
            (s__s__d____s__remaining_space____l_ffff0000907f5197,
             s_record_details_of_recovery_to_bl_ffff0000907e36e0,0xc97,s_BLACKBOX_ffff0000907d9988,
             0x300 - lVar4);
  FUN_ffff00009077de54
            (s__s__d____s__misc_msg_recovery_____ffff0000907ecec1,
             s_record_details_of_recovery_to_bl_ffff0000907e36e0,0xc98,s_BLACKBOX_ffff0000907d9988,
             auStack_f18);
  iVar2 = 10;
  if (local_74c._4_4_ + 1U < 10) {
    iVar2 = local_74c._4_4_ + 1;
  }
  local_74c = CONCAT44(iVar2,(iVar1 + 1U) % 10);
  uVar3 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&local_758,0xa420c00,0x654);
  if ((uVar3 & 1) != 0) {
    return;
  }
  pcVar6 = s__s__d____s__write_recovery_recor_ffff0000907f1b73;
  uVar7 = 0xca1;
LAB_ffff000090765678:
  FUN_ffff00009077de54
            (pcVar6,s_record_details_of_recovery_to_bl_ffff0000907e36e0,uVar7,
             s_BLACKBOX_ffff0000907d9988);
  return;
}



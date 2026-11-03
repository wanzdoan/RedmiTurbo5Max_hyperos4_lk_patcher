
void FUN_ffff000090765374(void)

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
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_fastboot_r_ffff0000907eb6f4,
               s_record_details_of_recovery_to_bl_ffff0000907e37c8,0xc57);
    return;
  }
  FUN_ffff00009077ee50(&local_758,0,0x654);
  FUN_ffff00009077ee50(auStack_f58,0,0x800);
  uVar3 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&local_758,0xa420c00,0x654);
  if ((uVar3 & 1) == 0) {
    pcVar6 = s__s__d____s__get_recovery_record_z_ffff0000907f0478;
    uVar7 = 0xc5c;
    goto LAB_ffff000090765910;
  }
  if (local_758 != -0x4a37160f5d2c3b19) {
    FUN_ffff00009077e11c
              (s__s__d____s__recovery_record_zone_ffff0000907d83ba,
               s_record_details_of_recovery_to_bl_ffff0000907e37c8,0xc60,s_BLACKBOX_ffff0000907d9a1f
              );
    if (DAT_ffff00009087bfac == '\x03') {
      local_750 = 0x3000;
LAB_ffff0000907654e4:
      local_758 = -0x4a37160f5d2c3b19;
      local_74c = 0;
      FUN_ffff00009077ee50(auStack_744,0,0x140);
      FUN_ffff00009077ee50(auStack_604,0,0x500);
    }
    else {
      if (DAT_ffff00009087bfac == '\x02') {
        local_750 = 0x2000;
        goto LAB_ffff0000907654e4;
      }
      if (DAT_ffff00009087bfac != '\x01') goto LAB_ffff0000907654e4;
      FUN_ffff00009077e11c
                (s__s__d__blackbox_v1__no_recovery_r_ffff0000907ffaff,
                 s_init_blackbox_recovery_record_zo_ffff0000907d9c55,0xc10);
    }
    uVar3 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_758,0xa420c00,0x654);
    if ((uVar3 & 1) == 0) {
      pcVar6 = s__s__d____s__write_recovery_recor_ffff0000907f1cfb;
      uVar7 = 0xc64;
      goto LAB_ffff000090765910;
    }
  }
  iVar1 = (int)local_74c;
  uVar9 = local_74c & 0xffffffff;
  local_58 = 0;
  local_60 = 0;
  FUN_ffff00009073761c(&local_60);
  FUN_ffff00009077f6fc
            (&local_80,0x20,s__04d__02d_02d__02d_02d_02d_ffff0000907f3539,local_58._2_2_,
             (undefined2)local_58,local_60._6_2_,local_60._4_2_ + 8,local_60._2_2_,
             (undefined2)local_60);
  FUN_ffff00009077f1e4(auStack_744 + uVar9 * 0x20,&local_80,0x20);
  uVar3 = FUN_ffff000090767530(auStack_f58);
  if ((uVar3 & 1) == 0) {
    pcVar6 = s__s__d____s__read_bcb_from_misc_p_ffff0000907de9e2;
    uVar7 = 0xc6f;
    goto LAB_ffff000090765910;
  }
  lVar4 = FUN_ffff00009077f250(auStack_f18);
  if (DAT_ffff00009082194c == '\0') {
    FUN_ffff00009077e11c
              (s__s__d____s__enter_recovery_mode_b_ffff0000907dd239,
               s_record_details_of_recovery_to_bl_ffff0000907e37c8,0xc76,s_BLACKBOX_ffff0000907d9a1f
              );
    pcVar6 = s_key_press_ffff0000908015ca;
LAB_ffff0000907657a0:
    FUN_ffff00009077f2ac(&local_100,pcVar6,0x7f);
    puVar8 = &local_100;
LAB_ffff0000907657b0:
    FUN_ffff00009077f1e4(auStack_604 + uVar9 * 0x80,puVar8,0x80);
  }
  else {
    local_60 = local_60 & 0xffffffff00000000;
    lVar5 = FUN_ffff000090760978(&local_60);
    FUN_ffff00009077e11c
              (s__s__d__the_size_of_last_kmsg_ret_ffff0000907d4fc1,
               s_is_triggered_by_adb_reboot_ffff0000907d1add,0x739,local_60 & 0xffffffff);
    if ((lVar5 == 0) || ((int)local_60 == 0)) {
      thunk_FUN_ffff0000907c314c(lVar5);
    }
    else {
      uVar3 = FUN_ffff0000907641b0(lVar5,s__system_bin_reboot_ffff0000907eb687,0);
      thunk_FUN_ffff0000907c314c(lVar5);
      if ((uVar3 & 1) != 0) {
        FUN_ffff00009077e11c
                  (s__s__d____s__enter_recovery_mode_b_ffff0000907d6aff,
                   s_record_details_of_recovery_to_bl_ffff0000907e37c8,0xc7a,
                   s_BLACKBOX_ffff0000907d9a1f);
        pcVar6 = s_adb_reboot_recovery_ffff0000907f04a6;
        goto LAB_ffff0000907657a0;
      }
    }
    local_60 = local_60 & 0xffffffff00000000;
    lVar5 = FUN_ffff000090760978(&local_60);
    FUN_ffff00009077e11c
              (s__s__d__the_size_of_last_kmsg_ret_ffff0000907d4fc1,
               s_is_triggered_by_sys_powerctl_ffff0000907e1d7c,0x74c,local_60 & 0xffffffff);
    if ((lVar5 == 0) || ((int)local_60 == 0)) {
      thunk_FUN_ffff0000907c314c(lVar5);
    }
    else {
      uVar3 = FUN_ffff0000907641b0(lVar5,s_sys_powerctl_ffff0000907f7023,0);
      thunk_FUN_ffff0000907c314c(lVar5);
      if ((uVar3 & 1) != 0) {
        FUN_ffff00009077e11c
                  (s__s__d____s__enter_recovery_mode_b_ffff0000907dea14,
                   s_record_details_of_recovery_to_bl_ffff0000907e37c8,0xc7e,
                   s_BLACKBOX_ffff0000907d9a1f);
        pcVar6 = s_sys_powerctl_reboot_recovery_ffff0000907e1e22;
        goto LAB_ffff0000907657a0;
      }
    }
    lVar5 = FUN_ffff00009077f250(auStack_f18);
    puVar8 = auStack_f18;
    if (lVar5 != 0) goto LAB_ffff0000907657b0;
    iVar2 = FUN_ffff00009077f0ac(auStack_f58,s_boot_fastboot_ffff0000907d7344);
    if (iVar2 != 0) {
      FUN_ffff00009077e11c
                (s__s__d____s__enter_recovery_mode_b_ffff000090802dd0,
                 s_record_details_of_recovery_to_bl_ffff0000907e37c8,0xc84,
                 s_BLACKBOX_ffff0000907d9a1f);
      pcVar6 = s_unknown_ffff0000907d657e;
      goto LAB_ffff0000907657a0;
    }
  }
  if (lVar4 == 0) {
    iVar2 = FUN_ffff00009077f0ac(auStack_f58,s_boot_fastboot_ffff0000907d7344);
    if (iVar2 != 0) {
      FUN_ffff00009077f6fc
                (auStack_f18,0x300,s_recovery_reason__s_time__s_ffff0000907d369a,&local_100,
                 &local_80);
    }
  }
  else {
    FUN_ffff00009077f6fc
              ((long)auStack_f18 + lVar4,0x300 - lVar4,s_time__s_ffff0000907e6b3d,&local_80);
  }
  FUN_ffff00009077e11c
            (s__s__d____s__misc_msg_command_____ffff0000907e535e,
             s_record_details_of_recovery_to_bl_ffff0000907e37c8,0xc95,s_BLACKBOX_ffff0000907d9a1f,
             auStack_f58);
  FUN_ffff00009077e11c
            (s__s__d____s__current_len____lu_ffff0000907e83bd,
             s_record_details_of_recovery_to_bl_ffff0000907e37c8,0xc96,s_BLACKBOX_ffff0000907d9a1f,
             lVar4);
  FUN_ffff00009077e11c
            (s__s__d____s__remaining_space____l_ffff0000907f531b,
             s_record_details_of_recovery_to_bl_ffff0000907e37c8,0xc97,s_BLACKBOX_ffff0000907d9a1f,
             0x300 - lVar4);
  FUN_ffff00009077e11c
            (s__s__d____s__misc_msg_recovery_____ffff0000907ed05b,
             s_record_details_of_recovery_to_bl_ffff0000907e37c8,0xc98,s_BLACKBOX_ffff0000907d9a1f,
             auStack_f18);
  iVar2 = 10;
  if (local_74c._4_4_ + 1U < 10) {
    iVar2 = local_74c._4_4_ + 1;
  }
  local_74c = CONCAT44(iVar2,(iVar1 + 1U) % 10);
  uVar3 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_758,0xa420c00,0x654);
  if ((uVar3 & 1) != 0) {
    return;
  }
  pcVar6 = s__s__d____s__write_recovery_recor_ffff0000907f1cfb;
  uVar7 = 0xca1;
LAB_ffff000090765910:
  FUN_ffff00009077e11c
            (pcVar6,s_record_details_of_recovery_to_bl_ffff0000907e37c8,uVar7,
             s_BLACKBOX_ffff0000907d9a1f);
  return;
}




void FUN_ffff000090764ec4(void)

{
  int iVar1;
  char cVar2;
  ulong uVar3;
  long lVar4;
  ulong uVar5;
  char *pcVar6;
  undefined8 uVar7;
  long *plVar8;
  int iVar9;
  long local_438;
  undefined4 local_430;
  undefined8 local_42c;
  undefined1 auStack_424 [320];
  undefined1 auStack_2e4 [644];
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined4 local_38;
  
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_fastboot_r_ffff0000907eb6f4,
               s_record_details_of_fastboot_to_bl_ffff0000907fc2da,0xbd9);
    return;
  }
  FUN_ffff00009077ee50(&local_438,0,0x3d4);
  uVar3 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&local_438,0xa420800,0x3d4);
  if ((uVar3 & 1) == 0) {
    pcVar6 = s__s__d____s__get_fastboot_record_z_ffff0000907db685;
    uVar7 = 0xbde;
    goto LAB_ffff0000907651f0;
  }
  if (local_438 != 0x4a7d3f8c1e6b9a2d) {
    FUN_ffff00009077e11c
              (s__s__d____s__fastboot_record_zone_ffff0000907d5095,
               s_record_details_of_fastboot_to_bl_ffff0000907fc2da,0xbe2,s_BLACKBOX_ffff0000907d9a1f
              );
    if (DAT_ffff00009087bfac == '\x03') {
      local_430 = 0x3000;
LAB_ffff000090764ff4:
      local_438 = 0x4a7d3f8c1e6b9a2d;
      local_42c = 0;
      FUN_ffff00009077ee50(auStack_424,0,0x140);
      FUN_ffff00009077ee50(auStack_2e4,0,0x280);
    }
    else {
      if (DAT_ffff00009087bfac == '\x02') {
        local_430 = 0x2000;
        goto LAB_ffff000090764ff4;
      }
      if (DAT_ffff00009087bfac != '\x01') goto LAB_ffff000090764ff4;
      FUN_ffff00009077e11c
                (s__s__d__blackbox_v1__no_fastboot_r_ffff0000907eb6f4,
                 s_init_blackbox_fastboot_record_zo_ffff0000907f52f8,0xbc3);
    }
    uVar3 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_438,0xa420800,0x3d4);
    if ((uVar3 & 1) == 0) {
      pcVar6 = s__s__d____s__write_fastboot_recor_ffff0000907fc301;
      uVar7 = 0xbe6;
      goto LAB_ffff0000907651f0;
    }
  }
  iVar1 = (int)local_42c;
  uVar3 = local_42c & 0xffffffff;
  local_38 = 0;
  local_40 = 0;
  FUN_ffff00009073761c(&local_40);
  FUN_ffff00009077f6fc
            (&local_60,0x20,s__04d__02d_02d__02d_02d_02d_ffff0000907f3539,local_38._2_2_,
             (undefined2)local_38,local_40._6_2_,local_40._4_2_ + 8,local_40._2_2_,
             (undefined2)local_40);
  FUN_ffff00009077f1e4(auStack_424 + uVar3 * 0x20,&local_60,0x20);
  cVar2 = FUN_ffff000090703bc0();
  if (cVar2 == '\x02') {
    plVar8 = &local_438 + uVar3 * 8;
    pcVar6 = s_boot_menu_ffff0000907d83b0;
LAB_ffff000090765178:
    FUN_ffff00009077f1e4((long)plVar8 + 0x154,pcVar6,0x40);
  }
  else {
    if (cVar2 != '\x01') {
      if (cVar2 == '\0') {
        pcVar6 = s_vol_down_ffff0000907eb73a;
      }
      else {
        pcVar6 = s_unknown_ffff0000907d657e;
      }
LAB_ffff000090765174:
      plVar8 = &local_438 + uVar3 * 8;
      goto LAB_ffff000090765178;
    }
    local_40 = local_40 & 0xffffffff00000000;
    lVar4 = FUN_ffff000090760978(&local_40);
    FUN_ffff00009077e11c
              (s__s__d__the_size_of_last_kmsg_ret_ffff0000907d4fc1,
               s_is_triggered_by_adb_reboot_ffff0000907d1add,0x739,local_40 & 0xffffffff);
    if ((lVar4 == 0) || ((int)local_40 == 0)) {
      thunk_FUN_ffff0000907c314c(lVar4);
    }
    else {
      uVar5 = FUN_ffff0000907641b0(lVar4,s__system_bin_reboot_ffff0000907eb687,0);
      thunk_FUN_ffff0000907c314c(lVar4);
      if ((uVar5 & 1) != 0) {
        pcVar6 = s_reboot_bootloader_ffff0000907eeb62;
        goto LAB_ffff000090765174;
      }
    }
    local_40 = local_40 & 0xffffffff00000000;
    lVar4 = FUN_ffff000090760978(&local_40);
    FUN_ffff00009077e11c
              (s__s__d__the_size_of_last_kmsg_ret_ffff0000907d4fc1,
               s_is_triggered_by_sys_powerctl_ffff0000907e1d7c,0x74c,local_40 & 0xffffffff);
    if ((lVar4 == 0) || ((int)local_40 == 0)) {
      thunk_FUN_ffff0000907c314c(lVar4);
    }
    else {
      uVar5 = FUN_ffff0000907641b0(lVar4,s_sys_powerctl_ffff0000907f7023,0);
      thunk_FUN_ffff0000907c314c(lVar4);
      if ((uVar5 & 1) != 0) {
        pcVar6 = s_sys_powerctl_reboot_bootloader_ffff0000907eeb74;
        goto LAB_ffff000090765174;
      }
    }
  }
  iVar9 = 10;
  if (local_42c._4_4_ + 1U < 10) {
    iVar9 = local_42c._4_4_ + 1;
  }
  local_42c = CONCAT44(iVar9,(iVar1 + 1U) % 10);
  uVar3 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_438,0xa420800,0x3d4);
  if ((uVar3 & 1) != 0) {
    return;
  }
  pcVar6 = s__s__d____s__write_fastboot_recor_ffff0000907fc301;
  uVar7 = 0xc08;
LAB_ffff0000907651f0:
  FUN_ffff00009077e11c
            (pcVar6,s_record_details_of_fastboot_to_bl_ffff0000907fc2da,uVar7,
             s_BLACKBOX_ffff0000907d9a1f);
  return;
}



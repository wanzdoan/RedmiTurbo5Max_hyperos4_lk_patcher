
undefined8 FUN_ffff0000be04fba4(undefined4 *param_1,int param_2)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  undefined4 uVar5;
  long local_68;
  undefined4 local_60;
  long local_58 [3];
  
  local_58[1] = 0;
  local_58[2] = 0;
  local_58[0] = 0;
  local_60 = 0;
  local_68 = 0;
  if (param_1 == (undefined4 *)0x0) {
    pcVar2 = s__s__d____s___pOffset_is_NULL__re_ffff0000be0b2513;
    uVar3 = 0xa0f;
LAB_ffff0000be05001c:
    FUN_ffff0000be05fd98
              (pcVar2,s_GetBlackBoxConfigOffset_ffff0000be0acea8,uVar3,s_BLACKBOX_ffff0000be0aa9ee);
    return 0;
  }
  uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126e20,0x400,0x3c);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_log_table_heade_ffff0000be0bb440;
    uVar3 = 0xa17;
    goto LAB_ffff0000be05001c;
  }
  if (DAT_ffff0000be126e20 != 0x56775af41bcde0f3) {
    FUN_ffff0000be05fd98
              (s__s__d____s___log_table_header_ma_ffff0000be0b7df5,
               s_GetBlackBoxConfigOffset_ffff0000be0acea8,0xa1b,s_BLACKBOX_ffff0000be0aa9ee);
    FUN_ffff0000be04cebc(&DAT_ffff0000be126e20);
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126e20,0x400,0x3c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_log_table_hea_ffff0000be0b030d;
      uVar3 = 0xa1f;
      goto LAB_ffff0000be05001c;
    }
    if (DAT_ffff0000be126e20 != 0x56775af41bcde0f3) {
      FUN_ffff0000be05fd98
                (s__s__d____s___init_log_table_head_ffff0000be0b5a43,
                 s_GetBlackBoxConfigOffset_ffff0000be0acea8,0xa25,s_BLACKBOX_ffff0000be0aa9ee);
      FUN_ffff0000be04cebc(&DAT_ffff0000be126e20);
      uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126e20,0x400,0x3c);
      if ((uVar1 & 1) == 0) {
        pcVar2 = s__s__d____s___write_log_table_hea_ffff0000be0b030d;
        uVar3 = 0xa2a;
        goto LAB_ffff0000be05001c;
      }
    }
  }
  FUN_ffff0000be04d660(&DAT_ffff0000be126e20);
  uVar1 = FUN_ffff0000be04cff4
                    (s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126f00,DAT_ffff0000be126e44,0x20);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_log_control_con_ffff0000be0c46ad;
    uVar3 = 0xa33;
    goto LAB_ffff0000be05001c;
  }
  if (DAT_ffff0000be126f00 != -0x62405cb6abad9411) {
    FUN_ffff0000be05fd98
              (s__s__d____s___init_log_control_co_ffff0000be0c34c8,
               s_GetBlackBoxConfigOffset_ffff0000be0acea8,0xa37,s_BLACKBOX_ffff0000be0aa9ee);
    DAT_ffff0000be126f00 = -0x62405cb6abad9411;
    DAT_ffff0000be126f08 = 0x100000001000;
    DAT_ffff0000be126f10 = 0x9801000;
    DAT_ffff0000be126f14 = 0x9881000;
    DAT_ffff0000be126f18 = 0x9882000;
    DAT_ffff0000be126f1c = 0x9885000;
    uVar1 = FUN_ffff0000be04d0b0
                      (s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126f00,DAT_ffff0000be126e44,0x20);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_log_control_c_ffff0000be0b9115;
      uVar3 = 0xa3c;
      goto LAB_ffff0000be05001c;
    }
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,local_58,DAT_ffff0000be126f14,0x18);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s____d__write_partition_ffff0000be0b7e3a;
      uVar3 = 0xa41;
      uVar4 = 0xa41;
      goto LAB_ffff0000be0501b4;
    }
    local_58[2] = 0x200000000;
    local_58[0] = 0x7f9d3a6b2e1c8f60;
    local_58[1] = 0;
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,local_58,DAT_ffff0000be126f14,0x18);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s____d__write_partition_ffff0000be0b7e3a;
      uVar3 = 0xa47;
      uVar4 = 0xa47;
      goto LAB_ffff0000be0501b4;
    }
    local_60 = 0;
    local_68 = 0x6d3fe5a8c9b1e2cf;
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&local_68,DAT_ffff0000be126f1c,0xc);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s____d__write_selinux_c_ffff0000be0c582e;
      uVar3 = 0xa4d;
      uVar4 = 0xa4d;
      goto LAB_ffff0000be0501b4;
    }
  }
  FUN_ffff0000be05fd98
            (s__s__d__print_black_box_control_c_ffff0000be0acd9f,
             s_PrintLogControlContentHeader_ffff0000be0a7506,0x339);
  FUN_ffff0000be05fd98
            (s__s__d__magic__0x_llx_ffff0000be0abc7b,s_PrintLogControlContentHeader_ffff0000be0a7506
             ,0x33a,DAT_ffff0000be126f00);
  FUN_ffff0000be05fd98
            (s__s__d__version__0x_x_ffff0000be0c5791,s_PrintLogControlContentHeader_ffff0000be0a7506
             ,0x33b,DAT_ffff0000be126f08 & 0xffffffff);
  FUN_ffff0000be05fd98
            (s__s__d__header_size__0x_x_ffff0000be0bd78f,
             s_PrintLogControlContentHeader_ffff0000be0a7506,0x33c,DAT_ffff0000be126f08._4_4_);
  FUN_ffff0000be05fd98
            (s__s__d__crash_history_offset__0x__ffff0000be0b47b6,
             s_PrintLogControlContentHeader_ffff0000be0a7506,0x33d,DAT_ffff0000be126f10);
  FUN_ffff0000be05fd98
            (s__s__d__hwinfo_offset__0x_x_ffff0000be0a3aeb,
             s_PrintLogControlContentHeader_ffff0000be0a7506,0x33e,DAT_ffff0000be126f18);
  if (param_2 == 3) {
    uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&local_68,DAT_ffff0000be126f1c,0xc);
    if ((uVar1 & 1) != 0) {
      FUN_ffff0000be04e660(&local_68);
      uVar5 = DAT_ffff0000be126f1c;
      if (local_68 != 0x6d3fe5a8c9b1e2cf) {
        FUN_ffff0000be05fd98
                  (s__s__d____s___selinux_config_magi_ffff0000be0a4f49,
                   s_GetBlackBoxConfigOffset_ffff0000be0acea8,0xa6d,s_BLACKBOX_ffff0000be0aa9ee);
        local_68 = 0x6d3fe5a8c9b1e2cf;
        local_60 = 0;
        uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&local_68,DAT_ffff0000be126f1c,0xc)
        ;
        uVar5 = DAT_ffff0000be126f1c;
        if ((uVar1 & 1) == 0) {
          pcVar2 = s__s__d____s____d__write_selinux_c_ffff0000be0c582e;
          uVar3 = 0xa71;
          uVar4 = 0xa71;
          goto LAB_ffff0000be0501b4;
        }
      }
LAB_ffff0000be0500cc:
      *param_1 = uVar5;
      return 1;
    }
    pcVar2 = s__s__d____s____d__get_selinux_con_ffff0000be0a62e6;
    uVar3 = 0xa68;
    uVar4 = 0xa68;
  }
  else {
    if (param_2 != 1) {
      return 1;
    }
    uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,local_58,DAT_ffff0000be126f14,0x18);
    if ((uVar1 & 1) != 0) {
      FUN_ffff0000be04d80c(local_58);
      uVar5 = DAT_ffff0000be126f14;
      if (local_58[0] != 0x7f9d3a6b2e1c8f60) {
        FUN_ffff0000be05fd98
                  (s__s__d____s___partition_size_cont_ffff0000be0c46e2,
                   s_GetBlackBoxConfigOffset_ffff0000be0acea8,0xa5b,s_BLACKBOX_ffff0000be0aa9ee);
        local_58[0] = 0x7f9d3a6b2e1c8f60;
        local_58[1] = 0;
        local_58[2] = 0x200000000;
        uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,local_58,DAT_ffff0000be126f14,0x18)
        ;
        uVar5 = DAT_ffff0000be126f14;
        if ((uVar1 & 1) == 0) {
          pcVar2 = s__s__d____s____d__write_partition_ffff0000be0b7e3a;
          uVar3 = 0xa5f;
          uVar4 = 0xa5f;
          goto LAB_ffff0000be0501b4;
        }
      }
      goto LAB_ffff0000be0500cc;
    }
    pcVar2 = s__s__d____s____d__get_partition_c_ffff0000be0aab24;
    uVar3 = 0xa56;
    uVar4 = 0xa56;
  }
LAB_ffff0000be0501b4:
  FUN_ffff0000be05fd98
            (pcVar2,s_GetBlackBoxConfigOffset_ffff0000be0acea8,uVar3,s_BLACKBOX_ffff0000be0aa9ee,
             uVar4);
  return 0;
}



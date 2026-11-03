
undefined8 FUN_ffff0000907661c0(undefined4 *param_1,int param_2)

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
    pcVar2 = s__s__d____s___pOffset_is_NULL__re_ffff0000907e52fb;
    uVar3 = 0xa0f;
LAB_ffff000090766638:
    FUN_ffff00009077e11c
              (pcVar2,s_GetBlackBoxConfigOffset_ffff0000907dd16c,uVar3,s_BLACKBOX_ffff0000907d9a1f);
    return 0;
  }
  uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087cbe8,0x400,0x3c);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_log_table_heade_ffff0000907f1c8b;
    uVar3 = 0xa17;
    goto LAB_ffff000090766638;
  }
  if (DAT_ffff00009087cbe8 != 0x56775af41bcde0f3) {
    FUN_ffff00009077e11c
              (s__s__d____s___log_table_header_ma_ffff0000907ecfd9,
               s_GetBlackBoxConfigOffset_ffff0000907dd16c,0xa1b,s_BLACKBOX_ffff0000907d9a1f);
    FUN_ffff000090760c6c(&DAT_ffff00009087cbe8);
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087cbe8,0x400,0x3c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_log_table_hea_ffff0000907e1da2;
      uVar3 = 0xa1f;
      goto LAB_ffff000090766638;
    }
    if (DAT_ffff00009087cbe8 != 0x56775af41bcde0f3) {
      FUN_ffff00009077e11c
                (s__s__d____s___init_log_table_head_ffff0000907e9d6e,
                 s_GetBlackBoxConfigOffset_ffff0000907dd16c,0xa25,s_BLACKBOX_ffff0000907d9a1f);
      FUN_ffff000090760c6c(&DAT_ffff00009087cbe8);
      uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087cbe8,0x400,0x3c);
      if ((uVar1 & 1) == 0) {
        pcVar2 = s__s__d____s___write_log_table_hea_ffff0000907e1da2;
        uVar3 = 0xa2a;
        goto LAB_ffff000090766638;
      }
    }
  }
  FUN_ffff000090761580(&DAT_ffff00009087cbe8);
  uVar1 = FUN_ffff000090760f14
                    (s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087ccd8,DAT_ffff00009087cc0c,0x20);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_log_control_con_ffff0000907ffa70;
    uVar3 = 0xa33;
    goto LAB_ffff000090766638;
  }
  if (DAT_ffff00009087ccd8 != -0x62405cb6abad9411) {
    FUN_ffff00009077e11c
              (s__s__d____s___init_log_control_co_ffff0000907fe022,
               s_GetBlackBoxConfigOffset_ffff0000907dd16c,0xa37,s_BLACKBOX_ffff0000907d9a1f);
    DAT_ffff00009087ccd8 = -0x62405cb6abad9411;
    DAT_ffff00009087cce0 = 0x100000001000;
    DAT_ffff00009087cce8 = 0x9801000;
    DAT_ffff00009087ccec = 0x9881000;
    DAT_ffff00009087ccf0 = 0x9882000;
    DAT_ffff00009087ccf4 = 0x9885000;
    uVar1 = FUN_ffff000090760fd0
                      (s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087ccd8,DAT_ffff00009087cc0c,0x20);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_log_control_c_ffff0000907eeb2b;
      uVar3 = 0xa3c;
      goto LAB_ffff000090766638;
    }
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,local_58,DAT_ffff00009087ccec,0x18);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s____d__write_partition_ffff0000907ed01e;
      uVar3 = 0xa41;
      uVar4 = 0xa41;
      goto LAB_ffff0000907667d0;
    }
    local_58[2] = 0x200000000;
    local_58[0] = 0x7f9d3a6b2e1c8f60;
    local_58[1] = 0;
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,local_58,DAT_ffff00009087ccec,0x18);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s____d__write_partition_ffff0000907ed01e;
      uVar3 = 0xa47;
      uVar4 = 0xa47;
      goto LAB_ffff0000907667d0;
    }
    local_60 = 0;
    local_68 = 0x6d3fe5a8c9b1e2cf;
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_68,DAT_ffff00009087ccf4,0xc);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s____d__write_selinux_c_ffff00009080159b;
      uVar3 = 0xa4d;
      uVar4 = 0xa4d;
      goto LAB_ffff0000907667d0;
    }
  }
  FUN_ffff00009077e11c
            (s__s__d__print_black_box_control_c_ffff0000907dd003,
             s_PrintLogControlContentHeader_ffff0000907d4ea1,0x339);
  FUN_ffff00009077e11c
            (s__s__d__magic__0x_llx_ffff0000907db58f,s_PrintLogControlContentHeader_ffff0000907d4ea1
             ,0x33a,DAT_ffff00009087ccd8);
  FUN_ffff00009077e11c
            (s__s__d__version__0x_x_ffff000090801408,s_PrintLogControlContentHeader_ffff0000907d4ea1
             ,0x33b,DAT_ffff00009087cce0 & 0xffffffff);
  FUN_ffff00009077e11c
            (s__s__d__header_size__0x_x_ffff0000907f5166,
             s_PrintLogControlContentHeader_ffff0000907d4ea1,0x33c,DAT_ffff00009087cce0._4_4_);
  FUN_ffff00009077e11c
            (s__s__d__crash_history_offset__0x__ffff0000907e8271,
             s_PrintLogControlContentHeader_ffff0000907d4ea1,0x33d,DAT_ffff00009087cce8);
  FUN_ffff00009077e11c
            (s__s__d__hwinfo_offset__0x_x_ffff0000907cffd1,
             s_PrintLogControlContentHeader_ffff0000907d4ea1,0x33e,DAT_ffff00009087ccf0);
  if (param_2 == 3) {
    uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&local_68,DAT_ffff00009087ccf4,0xc);
    if ((uVar1 & 1) != 0) {
      FUN_ffff000090762580(&local_68);
      uVar5 = DAT_ffff00009087ccf4;
      if (local_68 != 0x6d3fe5a8c9b1e2cf) {
        FUN_ffff00009077e11c
                  (s__s__d____s___selinux_config_magi_ffff0000907d1ba0,
                   s_GetBlackBoxConfigOffset_ffff0000907dd16c,0xa6d,s_BLACKBOX_ffff0000907d9a1f);
        local_68 = 0x6d3fe5a8c9b1e2cf;
        local_60 = 0;
        uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_68,DAT_ffff00009087ccf4,0xc)
        ;
        uVar5 = DAT_ffff00009087ccf4;
        if ((uVar1 & 1) == 0) {
          pcVar2 = s__s__d____s____d__write_selinux_c_ffff00009080159b;
          uVar3 = 0xa71;
          uVar4 = 0xa71;
          goto LAB_ffff0000907667d0;
        }
      }
LAB_ffff0000907666e8:
      *param_1 = uVar5;
      return 1;
    }
    pcVar2 = s__s__d____s____d__get_selinux_con_ffff0000907d366d;
    uVar3 = 0xa68;
    uVar4 = 0xa68;
  }
  else {
    if (param_2 != 1) {
      return 1;
    }
    uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,local_58,DAT_ffff00009087ccec,0x18);
    if ((uVar1 & 1) != 0) {
      FUN_ffff00009076172c(local_58);
      uVar5 = DAT_ffff00009087ccec;
      if (local_58[0] != 0x7f9d3a6b2e1c8f60) {
        FUN_ffff00009077e11c
                  (s__s__d____s___partition_size_cont_ffff0000907ffaa5,
                   s_GetBlackBoxConfigOffset_ffff0000907dd16c,0xa5b,s_BLACKBOX_ffff0000907d9a1f);
        local_58[0] = 0x7f9d3a6b2e1c8f60;
        local_58[1] = 0;
        local_58[2] = 0x200000000;
        uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,local_58,DAT_ffff00009087ccec,0x18)
        ;
        uVar5 = DAT_ffff00009087ccec;
        if ((uVar1 & 1) == 0) {
          pcVar2 = s__s__d____s____d__write_partition_ffff0000907ed01e;
          uVar3 = 0xa5f;
          uVar4 = 0xa5f;
          goto LAB_ffff0000907667d0;
        }
      }
      goto LAB_ffff0000907666e8;
    }
    pcVar2 = s__s__d____s____d__get_partition_c_ffff0000907d9ba6;
    uVar3 = 0xa56;
    uVar4 = 0xa56;
  }
LAB_ffff0000907667d0:
  FUN_ffff00009077e11c
            (pcVar2,s_GetBlackBoxConfigOffset_ffff0000907dd16c,uVar3,s_BLACKBOX_ffff0000907d9a1f,
             uVar4);
  return 0;
}



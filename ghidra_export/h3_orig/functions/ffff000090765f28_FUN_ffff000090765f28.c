// ffff000090765f28  size=1560  FUN_ffff000090765f28

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090765f28(undefined4 *param_1,int param_2)

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
    pcVar2 = s__s__d____s___pOffset_is_NULL__re_ffff0000907e51f7;
    uVar3 = 0xa0f;
LAB_ffff0000907663a0:
    FUN_ffff00009077de54
              (pcVar2,s_GetBlackBoxConfigOffset_ffff0000907dd096,uVar3,s_BLACKBOX_ffff0000907d9988);
    return 0;
  }
  uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087cb50,0x400,0x3c);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_log_table_heade_ffff0000907f1b1a;
    uVar3 = 0xa17;
    goto LAB_ffff0000907663a0;
  }
  if (_DAT_ffff00009087cb50 != 0x56775af41bcde0f3) {
    FUN_ffff00009077de54
              (s__s__d____s___log_table_header_ma_ffff0000907ece56,
               s_GetBlackBoxConfigOffset_ffff0000907dd096,0xa1b,s_BLACKBOX_ffff0000907d9988);
    FUN_ffff0000907609d4(&DAT_ffff00009087cb50);
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087cb50,0x400,0x3c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_log_table_hea_ffff0000907e1cd1;
      uVar3 = 0xa1f;
      goto LAB_ffff0000907663a0;
    }
    if (_DAT_ffff00009087cb50 != 0x56775af41bcde0f3) {
      FUN_ffff00009077de54
                (s__s__d____s___init_log_table_head_ffff0000907e9c2a,
                 s_GetBlackBoxConfigOffset_ffff0000907dd096,0xa25,s_BLACKBOX_ffff0000907d9988);
      FUN_ffff0000907609d4(&DAT_ffff00009087cb50);
      uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087cb50,0x400,0x3c);
      if ((uVar1 & 1) == 0) {
        pcVar2 = s__s__d____s___write_log_table_hea_ffff0000907e1cd1;
        uVar3 = 0xa2a;
        goto LAB_ffff0000907663a0;
      }
    }
  }
  FUN_ffff0000907612e8(&DAT_ffff00009087cb50);
  uVar1 = FUN_ffff000090760c7c
                    (s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087cc40,_DAT_ffff00009087cb74,0x20);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s___get_log_control_con_ffff0000907ff8c8;
    uVar3 = 0xa33;
    goto LAB_ffff0000907663a0;
  }
  if (_DAT_ffff00009087cc40 != -0x62405cb6abad9411) {
    FUN_ffff00009077de54
              (s__s__d____s___init_log_control_co_ffff0000907fde7a,
               s_GetBlackBoxConfigOffset_ffff0000907dd096,0xa37,s_BLACKBOX_ffff0000907d9988);
    _DAT_ffff00009087cc40 = -0x62405cb6abad9411;
    _DAT_ffff00009087cc48 = 0x100000001000;
    _DAT_ffff00009087cc50 = 0x9801000;
    _DAT_ffff00009087cc54 = 0x9881000;
    _DAT_ffff00009087cc58 = 0x9882000;
    _DAT_ffff00009087cc5c = 0x9885000;
    uVar1 = FUN_ffff000090760d38
                      (s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087cc40,_DAT_ffff00009087cb74,0x20)
    ;
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_log_control_c_ffff0000907ee9ba;
      uVar3 = 0xa3c;
      goto LAB_ffff0000907663a0;
    }
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,local_58,_DAT_ffff00009087cc54,0x18);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s____d__write_partition_ffff0000907ece9b;
      uVar3 = 0xa41;
      uVar4 = 0xa41;
      goto LAB_ffff000090766538;
    }
    local_58[2] = 0x200000000;
    local_58[0] = 0x7f9d3a6b2e1c8f60;
    local_58[1] = 0;
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,local_58,_DAT_ffff00009087cc54,0x18);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s____d__write_partition_ffff0000907ece9b;
      uVar3 = 0xa47;
      uVar4 = 0xa47;
      goto LAB_ffff000090766538;
    }
    local_60 = 0;
    local_68 = 0x6d3fe5a8c9b1e2cf;
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,&local_68,_DAT_ffff00009087cc5c,0xc);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s____d__write_selinux_c_ffff0000908013bf;
      uVar3 = 0xa4d;
      uVar4 = 0xa4d;
      goto LAB_ffff000090766538;
    }
  }
  FUN_ffff00009077de54
            (s__s__d__print_black_box_control_c_ffff0000907dcf2d,
             s_PrintLogControlContentHeader_ffff0000907d4e64,0x339);
  FUN_ffff00009077de54
            (s__s__d__magic__0x_llx_ffff0000907db484,s_PrintLogControlContentHeader_ffff0000907d4e64
             ,0x33a,_DAT_ffff00009087cc40);
  FUN_ffff00009077de54
            (s__s__d__version__0x_x_ffff00009080122c,s_PrintLogControlContentHeader_ffff0000907d4e64
             ,0x33b,_DAT_ffff00009087cc48 & 0xffffffff);
  FUN_ffff00009077de54
            (s__s__d__header_size__0x_x_ffff0000907f4ff9,
             s_PrintLogControlContentHeader_ffff0000907d4e64,0x33c,_DAT_ffff00009087cc4c);
  FUN_ffff00009077de54
            (s__s__d__crash_history_offset__0x__ffff0000907e812d,
             s_PrintLogControlContentHeader_ffff0000907d4e64,0x33d,_DAT_ffff00009087cc50);
  FUN_ffff00009077de54
            (s__s__d__hwinfo_offset__0x_x_ffff0000907cffb2,
             s_PrintLogControlContentHeader_ffff0000907d4e64,0x33e,_DAT_ffff00009087cc58);
  if (param_2 == 3) {
    uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c1b,&local_68,_DAT_ffff00009087cc5c,0xc);
    if ((uVar1 & 1) != 0) {
      FUN_ffff0000907622e8(&local_68);
      uVar5 = _DAT_ffff00009087cc5c;
      if (local_68 != 0x6d3fe5a8c9b1e2cf) {
        FUN_ffff00009077de54
                  (s__s__d____s___selinux_config_magi_ffff0000907d1b63,
                   s_GetBlackBoxConfigOffset_ffff0000907dd096,0xa6d,s_BLACKBOX_ffff0000907d9988);
        local_68 = 0x6d3fe5a8c9b1e2cf;
        local_60 = 0;
        uVar1 = FUN_ffff000090760d38
                          (s_blackbox_ffff0000907f0c1b,&local_68,_DAT_ffff00009087cc5c,0xc);
        uVar5 = _DAT_ffff00009087cc5c;
        if ((uVar1 & 1) == 0) {
          pcVar2 = s__s__d____s____d__write_selinux_c_ffff0000908013bf;
          uVar3 = 0xa71;
          uVar4 = 0xa71;
          goto LAB_ffff000090766538;
        }
      }
LAB_ffff000090766450:
      *param_1 = uVar5;
      return 1;
    }
    pcVar2 = s__s__d____s____d__get_selinux_con_ffff0000907d3630;
    uVar3 = 0xa68;
    uVar4 = 0xa68;
  }
  else {
    if (param_2 != 1) {
      return 1;
    }
    uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c1b,local_58,_DAT_ffff00009087cc54,0x18);
    if ((uVar1 & 1) != 0) {
      FUN_ffff000090761494(local_58);
      uVar5 = _DAT_ffff00009087cc54;
      if (local_58[0] != 0x7f9d3a6b2e1c8f60) {
        FUN_ffff00009077de54
                  (s__s__d____s___partition_size_cont_ffff0000907ff8fd,
                   s_GetBlackBoxConfigOffset_ffff0000907dd096,0xa5b,s_BLACKBOX_ffff0000907d9988);
        local_58[0] = 0x7f9d3a6b2e1c8f60;
        local_58[1] = 0;
        local_58[2] = 0x200000000;
        uVar1 = FUN_ffff000090760d38
                          (s_blackbox_ffff0000907f0c1b,local_58,_DAT_ffff00009087cc54,0x18);
        uVar5 = _DAT_ffff00009087cc54;
        if ((uVar1 & 1) == 0) {
          pcVar2 = s__s__d____s____d__write_partition_ffff0000907ece9b;
          uVar3 = 0xa5f;
          uVar4 = 0xa5f;
          goto LAB_ffff000090766538;
        }
      }
      goto LAB_ffff000090766450;
    }
    pcVar2 = s__s__d____s____d__get_partition_c_ffff0000907d9b0f;
    uVar3 = 0xa56;
    uVar4 = 0xa56;
  }
LAB_ffff000090766538:
  FUN_ffff00009077de54
            (pcVar2,s_GetBlackBoxConfigOffset_ffff0000907dd096,uVar3,s_BLACKBOX_ffff0000907d9988,
             uVar4);
  return 0;
}



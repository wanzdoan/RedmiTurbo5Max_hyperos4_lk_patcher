
void FUN_ffff000090764028(uint param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  char *pcVar4;
  ulong uVar5;
  undefined8 uVar6;
  undefined8 uVar7;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined4 local_38;
  
  uVar5 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087cbe8,0x400,0x3c);
  if ((uVar5 & 1) == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxLogTableHeader_ffff0000907e528f,0x4a5,s_BLACKBOX_ffff0000907d9a1f);
    uVar6 = 0x6d3;
    param_1 = 0;
    goto LAB_ffff00009076415c;
  }
  if (DAT_ffff00009087cbe8 != 0x56775af41bcde0f3) {
    FUN_ffff00009077e11c
              (s__s__d____s___init_log_table_head_ffff0000907e9d6e,
               s_CheckBlackboxLogTableHeader_ffff0000907e528f,0x4ab,s_BLACKBOX_ffff0000907d9a1f);
    FUN_ffff000090760c6c(&DAT_ffff00009087cbe8);
  }
  FUN_ffff000090761580(&DAT_ffff00009087cbe8);
  if (param_1 == 1) {
    uVar5 = FUN_ffff0000907631a4();
    if ((uVar5 & 1) == 0) {
LAB_ffff00009076413c:
      uVar6 = 0x6e5;
LAB_ffff00009076415c:
      FUN_ffff00009077e11c
                (s__s__d____s___check_blackbox_head_ffff0000907fdf9e,
                 s_save_logs_to_blackbox_ffff0000907ecf74,uVar6,s_BLACKBOX_ffff0000907d9a1f,param_1)
      ;
      return;
    }
    uVar3 = 1;
    uVar6 = 2;
  }
  else {
    if (param_1 == 2) {
      uVar5 = FUN_ffff000090763290();
joined_r0xffff000090764120:
      if ((uVar5 & 1) == 0) goto LAB_ffff00009076413c;
    }
    else {
      if (param_1 == 3) {
        uVar5 = FUN_ffff00009076337c();
        goto joined_r0xffff000090764120;
      }
      if ((param_1 & 0xfffffffe) != 2) goto code_r0xffff000090763c64;
    }
    FUN_ffff000090763580(param_1,0);
    FUN_ffff000090763580(param_1,2);
    uVar6 = 1;
    uVar3 = param_1;
  }
  FUN_ffff000090763580(uVar3,uVar6);
code_r0xffff000090763c64:
  local_38 = 0;
  local_40 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  FUN_ffff00009073761c(&local_40);
  FUN_ffff00009077f6fc
            (&local_60,0x20,s__04d__02d_02d__02d_02d_02d_ffff0000907f3539,local_38._2_2_,
             (undefined2)local_38,local_40._6_2_,local_40._4_2_ + 8,local_40._2_2_,
             (undefined2)local_40);
  uVar2 = DAT_ffff00009087c6c8;
  uVar1 = DAT_ffff00009087c498;
  uVar3 = DAT_ffff00009087c308;
  if (param_1 == 1) {
    uVar5 = (ulong)DAT_ffff00009087c308;
    uVar1 = DAT_ffff00009087c308 + 1;
    if (uVar1 == 5) {
      DAT_ffff00009087c308 = 0;
      DAT_ffff00009087c314 = uVar1;
      FUN_ffff00009077e11c
                (s__s__d____s___exception_shutdown_l_ffff0000907e8334,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6bc,
                 s_BLACKBOX_ffff0000907d9a1f);
    }
    else {
      if (DAT_ffff00009087c314 < 5) {
        DAT_ffff00009087c314 = uVar1;
      }
      DAT_ffff00009087c308 = uVar1;
      FUN_ffff00009077e11c
                (s__s__d____s___exception_shutdown_l_ffff0000907e374b,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6c1,
                 s_BLACKBOX_ffff0000907d9a1f,DAT_ffff00009087c314);
    }
    *(ulong *)(&DAT_ffff00009087c374 + uVar5 * 8) = (ulong)DAT_ffff00009087cbf8;
    FUN_ffff00009077f0d4((ulong)uVar3 * 0x30 + -0xffff6f783c64,&local_60);
    uVar5 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c300,0xac10800,0x18c);
    if ((uVar5 & 1) != 0) {
      return;
    }
    pcVar4 = s__s__d____s____d__write_exception_ffff0000907f03d6;
    uVar6 = 0x6c7;
    uVar7 = 0x6c7;
  }
  else if (param_1 == 3) {
    uVar5 = (ulong)DAT_ffff00009087c6c8;
    DAT_ffff00009087c6c8 = DAT_ffff00009087c6c8 + 1;
    FUN_ffff00009077e11c
              (s__s__d____s___cur_index_is__0x_x_ffff0000908014f9,
               s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6a3,s_BLACKBOX_ffff0000907d9a1f
               ,uVar2);
    FUN_ffff00009077e11c
              (s__s__d____s___next_index_is__0x_x_ffff0000907ff9d4,
               s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6a4,s_BLACKBOX_ffff0000907d9a1f
               ,DAT_ffff00009087c6c8);
    uVar3 = DAT_ffff00009087c6c8;
    if (DAT_ffff00009087c6c8 == 5) {
      DAT_ffff00009087c6c8 = 0;
      DAT_ffff00009087c6d4 = uVar3;
      FUN_ffff00009077e11c
                (s__s__d____s___recovery_mode_log_c_ffff0000907de951,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6a8,
                 s_BLACKBOX_ffff0000907d9a1f);
    }
    else {
      if (DAT_ffff00009087c6d4 < 5) {
        DAT_ffff00009087c6d4 = DAT_ffff00009087c6c8;
      }
      FUN_ffff00009077e11c
                (s__s__d____s___recovery_mode_log_l_ffff0000907f3554,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6ad,
                 s_BLACKBOX_ffff0000907d9a1f,DAT_ffff00009087c6d4);
    }
    *(ulong *)(&DAT_ffff00009087c7d4 + uVar5 * 8) = (ulong)DAT_ffff00009087cbf8;
    FUN_ffff00009077f0d4(uVar5 * 0x30 + -0xffff6f783804,&local_60);
    uVar5 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c6c0,0xb00a800,0x22c);
    if ((uVar5 & 1) != 0) {
      return;
    }
    pcVar4 = s__s__d____s____d__write_recovery_m_ffff0000907f6fea;
    uVar6 = 0x6b3;
    uVar7 = 0x6b3;
  }
  else {
    if (param_1 != 2) {
      return;
    }
    uVar5 = (ulong)DAT_ffff00009087c498;
    uVar3 = DAT_ffff00009087c498 + 1;
    if (uVar3 == 5) {
      DAT_ffff00009087c498 = 0;
      DAT_ffff00009087c4a4 = uVar3;
      FUN_ffff00009077e11c
                (s__s__d____s___fastboot_mode_log_c_ffff0000907e6a41,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x692,
                 s_BLACKBOX_ffff0000907d9a1f);
    }
    else {
      if (DAT_ffff00009087c4a4 < 5) {
        DAT_ffff00009087c4a4 = uVar3;
      }
      DAT_ffff00009087c498 = uVar3;
      FUN_ffff00009077e11c
                (s__s__d____s___fastboot_mode_log_l_ffff0000907ecf44,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x697,
                 s_BLACKBOX_ffff0000907d9a1f,DAT_ffff00009087c4a4);
    }
    *(ulong *)(&DAT_ffff00009087c5a4 + uVar5 * 8) = (ulong)DAT_ffff00009087cbf8;
    FUN_ffff00009077f0d4((ulong)uVar1 * 0x30 + -0xffff6f783a34,&local_60);
    uVar5 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c490,0xac60c00,0x22c);
    if ((uVar5 & 1) != 0) {
      return;
    }
    pcVar4 = s__s__d____s____d__write_fastboot_m_ffff0000907d6a97;
    uVar6 = 0x69d;
    uVar7 = 0x69d;
  }
  FUN_ffff00009077e11c
            (pcVar4,s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,uVar6,
             s_BLACKBOX_ffff0000907d9a1f,uVar7);
  return;
}



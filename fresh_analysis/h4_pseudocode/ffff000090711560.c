
void FUN_ffff000090711560(void)

{
  int iVar1;
  int iVar2;
  long lVar3;
  char *pcVar4;
  undefined8 uVar5;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
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
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  if (((DAT_ffff00009083f768 & 1) != 0) ||
     (lVar3 = FUN_ffff000090721d30(s_enhance_menu_ffff0000907e8d42), lVar3 == 0)) {
    return;
  }
  FUN_ffff00009077e11c
            (s__s__d__BootMenu__Ext_mode___s__ffff0000907f93d8,s_bootmenu_cmdline_ffff0000907f93f8,
             0x76,lVar3);
  iVar1 = FUN_ffff00009077f0ac(lVar3,s_initcall_ffff0000907fb12d);
  if (iVar1 == 0) {
    FUN_ffff000090719078(s_initcall_debug_1_log_buf_len_4M_p_ffff0000907e5bbd);
    return;
  }
  iVar1 = FUN_ffff00009077f0ac(lVar3,s_bootftrace_ffff0000907f0dac);
  if (iVar1 != 0) {
    return;
  }
  lVar3 = FUN_ffff000090721d30(s_perf_event_ffff0000907e0ba1);
  FUN_ffff000090719078(s_androidboot_boot_trace_1_ffff0000907d3efc);
  FUN_ffff000090719078(s_trace_event_sched_switch_cpu_fre_ffff0000907e2590);
  FUN_ffff000090719078(s_initcall_debug_1_log_buf_len_4M_p_ffff0000907e5bbd);
  if (lVar3 == 0) {
    return;
  }
  iVar1 = FUN_ffff00009077f250(lVar3);
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
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
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_130 = 0;
  uStack_128 = 0;
  if (iVar1 == 0) {
    return;
  }
  iVar2 = FUN_ffff00009077f250(s_androidboot_perf_event__ffff0000907fb136);
  if (0x7f < iVar2 + iVar1) {
    FUN_ffff00009077e11c
              (s__s__d__Skip__Perf_event__s__len__ffff0000907df43e,s_ftrace_cb_ffff0000907ef45c,0x50
               ,lVar3,iVar1);
    return;
  }
  iVar1 = FUN_ffff00009077f6fc(&local_b0,0x80,s_androidboot_perf_event__s_ffff0000907e8d4f,lVar3);
  if (iVar1 < 1) {
    pcVar4 = s__s__d__Skip__Perf_event_cmd_erro_ffff0000907edb98;
    uVar5 = 0x55;
    goto LAB_ffff0000907117c4;
  }
  lVar3 = FUN_ffff000090721d30(s_perf_intrl_ffff0000907dc017);
  if (lVar3 == 0) {
LAB_ffff000090711744:
    iVar1 = 4;
  }
  else {
    iVar1 = FUN_ffff00009077d72c();
    if (iVar1 - 0x3e9U < 0xfffffc18) {
      FUN_ffff00009077e11c
                (s__s__d__BootMenu__perf_intvl__d_o_ffff0000907e42b9,s_ftrace_cb_ffff0000907ef45c,
                 0x5e,iVar1);
      goto LAB_ffff000090711744;
    }
  }
  iVar1 = FUN_ffff00009077f6fc(&local_130,0x80,s_androidboot_perf_intvl__d_ffff0000907f5d00,iVar1);
  if (0 < iVar1) {
    FUN_ffff00009077e11c
              (s__s__d__BootMenu__Perf_cmd___s____ffff0000907f40f1,s_ftrace_cb_ffff0000907ef45c,0x67
               ,&local_130,&local_b0);
    FUN_ffff000090719078(&local_b0);
    FUN_ffff000090719078(&local_130);
    return;
  }
  pcVar4 = s__s__d__Skip__Perf_intvl_cmd_erro_ffff0000907d583f;
  uVar5 = 99;
LAB_ffff0000907117c4:
  FUN_ffff00009077e11c(pcVar4,s_ftrace_cb_ffff0000907ef45c,uVar5);
  return;
}



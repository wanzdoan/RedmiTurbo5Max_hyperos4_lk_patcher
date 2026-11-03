// ffff000090711528  size=468  FUN_ffff000090711528

void FUN_ffff000090711528(void)

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
  
  lVar3 = FUN_ffff000090721af0(s_perf_event_ffff0000907e0afe);
  FUN_ffff000090718f34(s_androidboot_boot_trace_1_ffff0000907d3ebf);
  FUN_ffff000090718f34(s_trace_event_sched_switch_cpu_fre_ffff0000907e24bf);
  FUN_ffff000090718f34(s_initcall_debug_1_log_buf_len_4M_p_ffff0000907e5aa9);
  if (lVar3 == 0) {
    return;
  }
  iVar1 = FUN_ffff00009077ef88(lVar3);
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
  iVar2 = FUN_ffff00009077ef88(s_androidboot_perf_event__ffff0000907faf9a);
  if (0x7f < iVar2 + iVar1) {
    FUN_ffff00009077de54
              (s__s__d__Skip__Perf_event__s__len__ffff0000907df39b,s_ftrace_cb_ffff0000907ef2eb,0x50
               ,lVar3,iVar1);
    return;
  }
  iVar1 = FUN_ffff00009077f434(&local_b0,0x80,s_androidboot_perf_event__s_ffff0000907e8c0b,lVar3);
  if (iVar1 < 1) {
    pcVar4 = s__s__d__Skip__Perf_event_cmd_erro_ffff0000907eda27;
    uVar5 = 0x55;
    goto LAB_ffff0000907116e0;
  }
  lVar3 = FUN_ffff000090721af0(s_perf_intrl_ffff0000907dbf41);
  if (lVar3 == 0) {
LAB_ffff000090711660:
    iVar1 = 4;
  }
  else {
    iVar1 = FUN_ffff00009077d464();
    if (iVar1 - 0x3e9U < 0xfffffc18) {
      FUN_ffff00009077de54
                (s__s__d__BootMenu__perf_intvl__d_o_ffff0000907e41b5,s_ftrace_cb_ffff0000907ef2eb,
                 0x5e,iVar1);
      goto LAB_ffff000090711660;
    }
  }
  iVar1 = FUN_ffff00009077f434(&local_130,0x80,s_androidboot_perf_intvl__d_ffff0000907f5b89,iVar1);
  if (0 < iVar1) {
    FUN_ffff00009077de54
              (s__s__d__BootMenu__Perf_cmd___s____ffff0000907f3f65,s_ftrace_cb_ffff0000907ef2eb,0x67
               ,&local_130,&local_b0);
    FUN_ffff000090718f34(&local_b0);
    FUN_ffff000090718f34(&local_130);
    return;
  }
  pcVar4 = s__s__d__Skip__Perf_intvl_cmd_erro_ffff0000907d57ef;
  uVar5 = 99;
LAB_ffff0000907116e0:
  FUN_ffff00009077de54(pcVar4,s_ftrace_cb_ffff0000907ef2eb,uVar5);
  return;
}



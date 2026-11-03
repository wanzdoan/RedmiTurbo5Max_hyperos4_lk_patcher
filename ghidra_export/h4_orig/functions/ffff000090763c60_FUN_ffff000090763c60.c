// ffff000090763c60  size=968  FUN_ffff000090763c60

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090763c60(int param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  char *pcVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  ulong uVar7;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined4 local_38;
  
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
  uVar3 = _DAT_ffff00009087c6c8;
  uVar2 = _DAT_ffff00009087c498;
  uVar1 = _DAT_ffff00009087c308;
  if (param_1 == 1) {
    uVar7 = (ulong)_DAT_ffff00009087c308;
    uVar2 = _DAT_ffff00009087c308 + 1;
    if (uVar2 == 5) {
      _DAT_ffff00009087c308 = 0;
      _DAT_ffff00009087c314 = uVar2;
      FUN_ffff00009077e11c
                (s__s__d____s___exception_shutdown_l_ffff0000907e8334,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6bc,
                 s_BLACKBOX_ffff0000907d9a1f);
    }
    else {
      if (_DAT_ffff00009087c314 < 5) {
        _DAT_ffff00009087c314 = uVar2;
      }
      _DAT_ffff00009087c308 = uVar2;
      FUN_ffff00009077e11c
                (s__s__d____s___exception_shutdown_l_ffff0000907e374b,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6c1,
                 s_BLACKBOX_ffff0000907d9a1f,_DAT_ffff00009087c314);
    }
    *(ulong *)(uVar7 * 8 + -0xffff6f783c8c) = (ulong)_DAT_ffff00009087cbf8;
    FUN_ffff00009077f0d4((ulong)uVar1 * 0x30 + -0xffff6f783c64,&local_60);
    uVar7 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c300,0xac10800,0x18c);
    if ((uVar7 & 1) != 0) {
      return;
    }
    pcVar4 = s__s__d____s____d__write_exception_ffff0000907f03d6;
    uVar5 = 0x6c7;
    uVar6 = 0x6c7;
  }
  else if (param_1 == 3) {
    uVar7 = (ulong)_DAT_ffff00009087c6c8;
    _DAT_ffff00009087c6c8 = _DAT_ffff00009087c6c8 + 1;
    FUN_ffff00009077e11c
              (s__s__d____s___cur_index_is__0x_x_ffff0000908014f9,
               s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6a3,s_BLACKBOX_ffff0000907d9a1f
               ,uVar3);
    FUN_ffff00009077e11c
              (s__s__d____s___next_index_is__0x_x_ffff0000907ff9d4,
               s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6a4,s_BLACKBOX_ffff0000907d9a1f
               ,_DAT_ffff00009087c6c8);
    uVar1 = _DAT_ffff00009087c6c8;
    if (_DAT_ffff00009087c6c8 == 5) {
      _DAT_ffff00009087c6c8 = 0;
      _DAT_ffff00009087c6d4 = uVar1;
      FUN_ffff00009077e11c
                (s__s__d____s___recovery_mode_log_c_ffff0000907de951,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6a8,
                 s_BLACKBOX_ffff0000907d9a1f);
    }
    else {
      if (_DAT_ffff00009087c6d4 < 5) {
        _DAT_ffff00009087c6d4 = _DAT_ffff00009087c6c8;
      }
      FUN_ffff00009077e11c
                (s__s__d____s___recovery_mode_log_l_ffff0000907f3554,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x6ad,
                 s_BLACKBOX_ffff0000907d9a1f,_DAT_ffff00009087c6d4);
    }
    *(ulong *)(uVar7 * 8 + -0xffff6f78382c) = (ulong)_DAT_ffff00009087cbf8;
    FUN_ffff00009077f0d4(uVar7 * 0x30 + -0xffff6f783804,&local_60);
    uVar7 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c6c0,0xb00a800,0x22c);
    if ((uVar7 & 1) != 0) {
      return;
    }
    pcVar4 = s__s__d____s____d__write_recovery_m_ffff0000907f6fea;
    uVar5 = 0x6b3;
    uVar6 = 0x6b3;
  }
  else {
    if (param_1 != 2) {
      return;
    }
    uVar7 = (ulong)_DAT_ffff00009087c498;
    uVar1 = _DAT_ffff00009087c498 + 1;
    if (uVar1 == 5) {
      _DAT_ffff00009087c498 = 0;
      _DAT_ffff00009087c4a4 = uVar1;
      FUN_ffff00009077e11c
                (s__s__d____s___fastboot_mode_log_c_ffff0000907e6a41,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x692,
                 s_BLACKBOX_ffff0000907d9a1f);
    }
    else {
      if (_DAT_ffff00009087c4a4 < 5) {
        _DAT_ffff00009087c4a4 = uVar1;
      }
      _DAT_ffff00009087c498 = uVar1;
      FUN_ffff00009077e11c
                (s__s__d____s___fastboot_mode_log_l_ffff0000907ecf44,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,0x697,
                 s_BLACKBOX_ffff0000907d9a1f,_DAT_ffff00009087c4a4);
    }
    *(ulong *)(uVar7 * 8 + -0xffff6f783a5c) = (ulong)_DAT_ffff00009087cbf8;
    FUN_ffff00009077f0d4((ulong)uVar2 * 0x30 + -0xffff6f783a34,&local_60);
    uVar7 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c490,0xac60c00,0x22c);
    if ((uVar7 & 1) != 0) {
      return;
    }
    pcVar4 = s__s__d____s____d__write_fastboot_m_ffff0000907d6a97;
    uVar5 = 0x69d;
    uVar6 = 0x69d;
  }
  FUN_ffff00009077e11c
            (pcVar4,s_update_blackbox_journal_zone_hea_ffff0000907e6a7c,uVar5,
             s_BLACKBOX_ffff0000907d9a1f,uVar6);
  return;
}



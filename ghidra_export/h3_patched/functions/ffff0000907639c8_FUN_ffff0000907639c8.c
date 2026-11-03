// ffff0000907639c8  size=968  FUN_ffff0000907639c8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907639c8(int param_1)

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
  FUN_ffff000090737384(&local_40);
  FUN_ffff00009077f434
            (&local_60,0x20,s__04d__02d_02d__02d_02d_02d_ffff0000907f3396,local_38._2_2_,
             (undefined2)local_38,local_40._6_2_,local_40._4_2_ + 8,local_40._2_2_,
             (undefined2)local_40);
  uVar3 = _DAT_ffff00009087c630;
  uVar2 = _DAT_ffff00009087c400;
  uVar1 = _DAT_ffff00009087c270;
  if (param_1 == 1) {
    uVar7 = (ulong)_DAT_ffff00009087c270;
    uVar2 = _DAT_ffff00009087c270 + 1;
    if (uVar2 == 5) {
      _DAT_ffff00009087c270 = 0;
      _DAT_ffff00009087c27c = uVar2;
      FUN_ffff00009077de54
                (s__s__d____s___exception_shutdown_l_ffff0000907e81d9,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6951,0x6bc,
                 s_BLACKBOX_ffff0000907d9988);
    }
    else {
      if (_DAT_ffff00009087c27c < 5) {
        _DAT_ffff00009087c27c = uVar2;
      }
      _DAT_ffff00009087c270 = uVar2;
      FUN_ffff00009077de54
                (s__s__d____s___exception_shutdown_l_ffff0000907e3663,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6951,0x6c1,
                 s_BLACKBOX_ffff0000907d9988,_DAT_ffff00009087c27c);
    }
    *(ulong *)(uVar7 * 8 + -0xffff6f783d24) = (ulong)_DAT_ffff00009087cb60;
    FUN_ffff00009077ee0c((ulong)uVar1 * 0x30 + -0xffff6f783cfc,&local_60);
    uVar7 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&DAT_ffff00009087c268,0xac10800,0x18c);
    if ((uVar7 & 1) != 0) {
      return;
    }
    pcVar4 = s__s__d____s____d__write_exception_ffff0000907f024e;
    uVar5 = 0x6c7;
    uVar6 = 0x6c7;
  }
  else if (param_1 == 3) {
    uVar7 = (ulong)_DAT_ffff00009087c630;
    _DAT_ffff00009087c630 = _DAT_ffff00009087c630 + 1;
    FUN_ffff00009077de54
              (s__s__d____s___cur_index_is__0x_x_ffff00009080131d,
               s_update_blackbox_journal_zone_hea_ffff0000907e6951,0x6a3,s_BLACKBOX_ffff0000907d9988
               ,uVar3);
    FUN_ffff00009077de54
              (s__s__d____s___next_index_is__0x_x_ffff0000907ff82c,
               s_update_blackbox_journal_zone_hea_ffff0000907e6951,0x6a4,s_BLACKBOX_ffff0000907d9988
               ,_DAT_ffff00009087c630);
    uVar1 = _DAT_ffff00009087c630;
    if (_DAT_ffff00009087c630 == 5) {
      _DAT_ffff00009087c630 = 0;
      _DAT_ffff00009087c63c = uVar1;
      FUN_ffff00009077de54
                (s__s__d____s___recovery_mode_log_c_ffff0000907de897,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6951,0x6a8,
                 s_BLACKBOX_ffff0000907d9988);
    }
    else {
      if (_DAT_ffff00009087c63c < 5) {
        _DAT_ffff00009087c63c = _DAT_ffff00009087c630;
      }
      FUN_ffff00009077de54
                (s__s__d____s___recovery_mode_log_l_ffff0000907f33b1,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6951,0x6ad,
                 s_BLACKBOX_ffff0000907d9988,_DAT_ffff00009087c63c);
    }
    *(ulong *)(uVar7 * 8 + -0xffff6f7838c4) = (ulong)_DAT_ffff00009087cb60;
    FUN_ffff00009077ee0c(uVar7 * 0x30 + -0xffff6f78389c,&local_60);
    uVar7 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&DAT_ffff00009087c628,0xb00a800,0x22c);
    if ((uVar7 & 1) != 0) {
      return;
    }
    pcVar4 = s__s__d____s____d__write_recovery_m_ffff0000907f6e49;
    uVar5 = 0x6b3;
    uVar6 = 0x6b3;
  }
  else {
    if (param_1 != 2) {
      return;
    }
    uVar7 = (ulong)_DAT_ffff00009087c400;
    uVar1 = _DAT_ffff00009087c400 + 1;
    if (uVar1 == 5) {
      _DAT_ffff00009087c400 = 0;
      _DAT_ffff00009087c40c = uVar1;
      FUN_ffff00009077de54
                (s__s__d____s___fastboot_mode_log_c_ffff0000907e6916,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6951,0x692,
                 s_BLACKBOX_ffff0000907d9988);
    }
    else {
      if (_DAT_ffff00009087c40c < 5) {
        _DAT_ffff00009087c40c = uVar1;
      }
      _DAT_ffff00009087c400 = uVar1;
      FUN_ffff00009077de54
                (s__s__d____s___fastboot_mode_log_l_ffff0000907ecdaa,
                 s_update_blackbox_journal_zone_hea_ffff0000907e6951,0x697,
                 s_BLACKBOX_ffff0000907d9988,_DAT_ffff00009087c40c);
    }
    *(ulong *)(uVar7 * 8 + -0xffff6f783af4) = (ulong)_DAT_ffff00009087cb60;
    FUN_ffff00009077ee0c((ulong)uVar2 * 0x30 + -0xffff6f783acc,&local_60);
    uVar7 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&DAT_ffff00009087c3f8,0xac60c00,0x22c);
    if ((uVar7 & 1) != 0) {
      return;
    }
    pcVar4 = s__s__d____s____d__write_fastboot_m_ffff0000907d6a01;
    uVar5 = 0x69d;
    uVar6 = 0x69d;
  }
  FUN_ffff00009077de54
            (pcVar4,s_update_blackbox_journal_zone_hea_ffff0000907e6951,uVar5,
             s_BLACKBOX_ffff0000907d9988,uVar6);
  return;
}



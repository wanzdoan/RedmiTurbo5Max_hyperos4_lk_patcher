// ffff000090764b50  size=220  FUN_ffff000090764b50

void FUN_ffff000090764b50(void)

{
  char cVar1;
  long lVar2;
  ulong uVar3;
  int local_24;
  
  FUN_ffff000090764c2c();
  cVar1 = FUN_ffff000090703c64();
  if (cVar1 == '\x01') {
    local_24 = 0;
    lVar2 = FUN_ffff0000907606e0(&local_24);
    FUN_ffff00009077de54
              (s__s__d__the_size_of_last_kmsg_ret_ffff0000907d4f84,
               s_is_triggered_by_adb_reboot_ffff0000907d1aa0,0x739,local_24);
    if ((lVar2 == 0) || (local_24 == 0)) {
      thunk_FUN_ffff0000907c2ad8(lVar2);
    }
    else {
      uVar3 = FUN_ffff000090763f18(lVar2,s__system_bin_reboot_ffff0000907eb4ed,0);
      thunk_FUN_ffff0000907c2ad8(lVar2);
      if ((uVar3 & 1) != 0) {
        return;
      }
    }
    FUN_ffff00009077de54
              (s__s__d__exception_fastboot_mode__b_ffff0000907f6ec0,
               s_process_fastboot_mode_scenario_ffff0000907ee950,0x8d2);
    FUN_ffff000090763d90(2);
    FUN_ffff00009077de54
              (s__s__d__exception_fastboot_mode__f_ffff0000907fc003,
               s_process_fastboot_mode_scenario_ffff0000907ee950,0x8d4);
  }
  return;
}



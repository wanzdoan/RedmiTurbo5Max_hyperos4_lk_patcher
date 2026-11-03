// ffff000090764ff4  size=232  FUN_ffff000090764ff4

void FUN_ffff000090764ff4(void)

{
  char cVar1;
  long lVar2;
  ulong uVar3;
  int local_24;
  
  cVar1 = FUN_ffff000090710b48();
  if ((cVar1 == '\x02') && (FUN_ffff0000907650dc(), DAT_ffff00009082192c != '\0')) {
    local_24 = 0;
    lVar2 = FUN_ffff0000907606e0(&local_24);
    FUN_ffff00009077de54
              (s__s__d__the_size_of_last_kmsg_ret_ffff0000907d4f84,
               s_is_triggered_by_adb_reboot_ffff0000907d1aa0,0x739,local_24);
    if ((lVar2 == 0) || (local_24 == 0)) {
      thunk_FUN_ffff0000907c2ad8(lVar2);
    }
    else {
      uVar3 = FUN_ffff000090763f18(lVar2,s__system_bin_reboot_ffff0000907eb504,0);
      thunk_FUN_ffff0000907c2ad8(lVar2);
      if ((uVar3 & 1) != 0) {
        return;
      }
    }
    FUN_ffff00009077de54
              (s__s__d__exception_recovery_mode__b_ffff0000907e69c1,
               s_process_recovery_mode_scenario_ffff0000907e825a,0x8df);
    FUN_ffff000090763d90(3);
    FUN_ffff00009077de54
              (s__s__d__exception_recovery_mode__f_ffff0000907fc044,
               s_process_recovery_mode_scenario_ffff0000907e825a,0x8e1);
  }
  return;
}



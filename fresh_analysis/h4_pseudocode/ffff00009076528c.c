
void FUN_ffff00009076528c(void)

{
  char cVar1;
  long lVar2;
  ulong uVar3;
  int local_24;
  
  cVar1 = FUN_ffff000090710bc0();
  if ((cVar1 == '\x02') && (FUN_ffff000090765374(), DAT_ffff00009082194c != '\0')) {
    local_24 = 0;
    lVar2 = FUN_ffff000090760978(&local_24);
    FUN_ffff00009077e11c
              (s__s__d__the_size_of_last_kmsg_ret_ffff0000907d4fc1,
               s_is_triggered_by_adb_reboot_ffff0000907d1add,0x739,local_24);
    if ((lVar2 == 0) || (local_24 == 0)) {
      thunk_FUN_ffff0000907c314c(lVar2);
    }
    else {
      uVar3 = FUN_ffff0000907641b0(lVar2,s__system_bin_reboot_ffff0000907eb687,0);
      thunk_FUN_ffff0000907c314c(lVar2);
      if ((uVar3 & 1) != 0) {
        return;
      }
    }
    FUN_ffff00009077e11c
              (s__s__d__exception_recovery_mode__b_ffff0000907e6ad5,
               s_process_recovery_mode_scenario_ffff0000907e839e,0x8df);
    FUN_ffff000090764028(3);
    FUN_ffff00009077e11c
              (s__s__d__exception_recovery_mode__f_ffff0000907fc1e0,
               s_process_recovery_mode_scenario_ffff0000907e839e,0x8e1);
  }
  return;
}



// ffff000090764de8  size=220  FUN_ffff000090764de8

void FUN_ffff000090764de8(void)

{
  char cVar1;
  long lVar2;
  ulong uVar3;
  int local_24;
  
  FUN_ffff000090764ec4();
  cVar1 = FUN_ffff000090703bc0();
  if (cVar1 == '\x01') {
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
              (s__s__d__exception_fastboot_mode__b_ffff0000907f7061,
               s_process_fastboot_mode_scenario_ffff0000907eead8,0x8d2);
    FUN_ffff000090764028(2);
    FUN_ffff00009077e11c
              (s__s__d__exception_fastboot_mode__f_ffff0000907fc19f,
               s_process_fastboot_mode_scenario_ffff0000907eead8,0x8d4);
  }
  return;
}



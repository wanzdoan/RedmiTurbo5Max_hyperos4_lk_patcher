
undefined1 FUN_ffff000090798bd8(uint param_1)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_8;
  
  local_8 = 0;
  iVar1 = FUN_ffff0000907adb54((long)&local_8 + 4);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff000090788a10(&local_8);
    if (iVar1 == 0) {
      if (local_8._4_4_ == 1) {
        return (&DAT_ffff000090837630)[(ulong)param_1 * 0x30];
      }
      if (local_8._4_4_ == 0) {
        return (&DAT_ffff00009083762c)[(ulong)param_1 * 0x30];
      }
      return 0;
    }
    pcVar2 = s__SEC__get_lock_state_fail__ret____ffff0000907d8899;
  }
  else {
    pcVar2 = s__SEC__get_sboot_state_fail__ret__ffff0000907ea317;
  }
  FUN_ffff00009077e11c(pcVar2,iVar1);
  local_8 = 0x100000004;
  FUN_ffff00009077e11c(s__SEC__Fail_to_get_security_state_ffff0000907d88bd);
  return 1;
}



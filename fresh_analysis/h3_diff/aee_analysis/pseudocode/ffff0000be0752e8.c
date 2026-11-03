
undefined1 FUN_ffff0000be0752e8(uint param_1)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_8;
  
  local_8 = 0;
  iVar1 = FUN_ffff0000be087d48((long)&local_8 + 4);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000be0679e4(&local_8);
    if (iVar1 == 0) {
      if (local_8._4_4_ == 1) {
        return (&DAT_ffff0000be0f3808)[(ulong)param_1 * 0x30];
      }
      if (local_8._4_4_ == 0) {
        return (&DAT_ffff0000be0f3804)[(ulong)param_1 * 0x30];
      }
      return 0;
    }
    pcVar2 = s__SEC__get_lock_state_fail__ret____ffff0000be0a9bc7;
  }
  else {
    pcVar2 = s__SEC__get_sboot_state_fail__ret__ffff0000be0b5dbc;
  }
  FUN_ffff0000be05fd98(pcVar2,iVar1);
  local_8 = 0x100000004;
  FUN_ffff0000be05fd98(s__SEC__Fail_to_get_security_state_ffff0000be0a9beb);
  return 1;
}



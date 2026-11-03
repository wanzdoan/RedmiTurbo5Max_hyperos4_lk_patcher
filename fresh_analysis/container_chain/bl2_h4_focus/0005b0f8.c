// File offset entry: 0x5b0f8

undefined1 FUN_ffff0000b805b0f8(uint param_1)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_8;
  
  local_8 = 0;
  iVar1 = FUN_ffff0000b806a34c((long)&local_8 + 4);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000b8052e70(&local_8);
    if (iVar1 == 0) {
      if (local_8._4_4_ == 1) {
        return (&DAT_ffff0000b8106e60)[(ulong)param_1 * 0x30];
      }
      if (local_8._4_4_ == 0) {
        return (&DAT_ffff0000b8106e5c)[(ulong)param_1 * 0x30];
      }
      return 0;
    }
    pcVar2 = s__SEC__get_lock_state_fail__ret____ffff0000b808a49e;
  }
  else {
    pcVar2 = s__SEC__get_sboot_state_fail__ret__ffff0000b8093e78;
  }
  FUN_ffff0000b804c33c(pcVar2,iVar1);
  local_8 = 0x100000004;
  FUN_ffff0000b804c33c(s__SEC__Fail_to_get_security_state_ffff0000b808a4c2);
  return 1;
}



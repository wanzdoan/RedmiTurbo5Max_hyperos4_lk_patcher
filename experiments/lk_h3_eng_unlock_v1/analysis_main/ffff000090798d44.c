
undefined1 FUN_ffff000090798d44(uint param_1)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_8;

  local_8 = 0;
  iVar1 = FUN_ffff0000907ade64((long)&local_8 + 4);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000907885fc(&local_8);
    if (iVar1 == 0) {
      if (local_8._4_4_ == 1) {
        return (&DAT_ffff00009082f700)[(ulong)param_1 * 0x30];
      }
      if (local_8._4_4_ == 0) {
        return (&DAT_ffff00009082f6fc)[(ulong)param_1 * 0x30];
      }
      return 0;
    }
    pcVar2 = s__SEC__get_lock_state_fail__ret____ffff0000907d0c78;
  }
  else {
    pcVar2 = s__SEC__get_sboot_state_fail__ret__ffff0000907e2c96;
  }
  FUN_ffff00009077ea84(pcVar2,iVar1);
  local_8 = 0x100000004;
  FUN_ffff00009077ea84(s__SEC__Fail_to_get_security_state_ffff0000907d0c9c);
  return 1;
}

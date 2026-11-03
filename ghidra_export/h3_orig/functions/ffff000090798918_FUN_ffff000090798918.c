// ffff000090798918  size=196  FUN_ffff000090798918

undefined1 FUN_ffff000090798918(uint param_1)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_8;
  
  local_8 = 0;
  iVar1 = FUN_ffff0000907ad894((long)&local_8 + 4);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff000090788750(&local_8);
    if (iVar1 == 0) {
      if (local_8._4_4_ == 1) {
        return (&DAT_ffff000090837610)[(ulong)param_1 * 0x30];
      }
      if (local_8._4_4_ == 0) {
        return (&DAT_ffff00009083760c)[(ulong)param_1 * 0x30];
      }
      return 0;
    }
    pcVar2 = s__SEC__get_lock_state_fail__ret____ffff0000907d8803;
  }
  else {
    pcVar2 = s__SEC__get_sboot_state_fail__ret__ffff0000907ea1d3;
  }
  FUN_ffff00009077de54(pcVar2,iVar1);
  local_8 = 0x100000004;
  FUN_ffff00009077de54(s__SEC__Fail_to_get_security_state_ffff0000907d8827);
  return 1;
}



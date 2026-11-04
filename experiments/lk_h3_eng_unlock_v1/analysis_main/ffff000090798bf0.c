
int FUN_ffff000090798bf0(void)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_8;

  local_8 = 0;
  iVar1 = FUN_ffff0000907ade64((long)&local_8 + 4);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000907885fc(&local_8);
    if (iVar1 == 0) goto LAB_ffff000090798c40;
    pcVar2 = s__SEC__get_lock_state_fail__ret____ffff0000907d0c78;
  }
  else {
    pcVar2 = s__SEC__get_sboot_state_fail__ret__ffff0000907e2c96;
  }
  FUN_ffff00009077ea84(pcVar2,iVar1);
LAB_ffff000090798c40:
  FUN_ffff00009077ea84
            (s__SEC__lock_state___d_sboot_state_ffff0000907e616c,local_8 & 0xffffffff,local_8._4_4_)
  ;
  return iVar1;
}


int FUN_ffff0000be075194(void)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_8;
  
  local_8 = 0;
  iVar1 = FUN_ffff0000be087d48((long)&local_8 + 4);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000be0679e4(&local_8);
    if (iVar1 == 0) goto LAB_ffff0000be0751e4;
    pcVar2 = s__SEC__get_lock_state_fail__ret____ffff0000be0a9bc7;
  }
  else {
    pcVar2 = s__SEC__get_sboot_state_fail__ret__ffff0000be0b5dbc;
  }
  FUN_ffff0000be05fd98(pcVar2,iVar1);
LAB_ffff0000be0751e4:
  FUN_ffff0000be05fd98
            (s__SEC__lock_state___d_sboot_state_ffff0000be0b8182,local_8 & 0xffffffff,local_8._4_4_)
  ;
  return iVar1;
}




int FUN_ffff0000b805afa4(void)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_8;
  
  local_8 = 0;
  iVar1 = FUN_ffff0000b806a34c((long)&local_8 + 4);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000b8052e70(&local_8);
    if (iVar1 == 0) goto LAB_ffff0000b805aff4;
    pcVar2 = s__SEC__get_lock_state_fail__ret____ffff0000b808a49e;
  }
  else {
    pcVar2 = s__SEC__get_sboot_state_fail__ret__ffff0000b8093e78;
  }
  FUN_ffff0000b804c33c(pcVar2,iVar1);
LAB_ffff0000b805aff4:
  FUN_ffff0000b804c33c
            (s__SEC__lock_state___d_sboot_state_ffff0000b8095c0c,local_8 & 0xffffffff,local_8._4_4_)
  ;
  return iVar1;
}



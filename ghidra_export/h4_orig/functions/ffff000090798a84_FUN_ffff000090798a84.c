// ffff000090798a84  size=116  FUN_ffff000090798a84

int FUN_ffff000090798a84(void)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_8;
  
  local_8 = 0;
  iVar1 = FUN_ffff0000907adb54((long)&local_8 + 4);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff000090788a10(&local_8);
    if (iVar1 == 0) goto LAB_ffff000090798ad4;
    pcVar2 = s__SEC__get_lock_state_fail__ret____ffff0000907d8899;
  }
  else {
    pcVar2 = s__SEC__get_sboot_state_fail__ret__ffff0000907ea317;
  }
  FUN_ffff00009077e11c(pcVar2,iVar1);
LAB_ffff000090798ad4:
  FUN_ffff00009077e11c
            (s__SEC__lock_state___d_sboot_state_ffff0000907ed654,local_8 & 0xffffffff,local_8._4_4_)
  ;
  return iVar1;
}



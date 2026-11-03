// ffff0000907987c4  size=116  FUN_ffff0000907987c4

int FUN_ffff0000907987c4(void)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_8;
  
  local_8 = 0;
  iVar1 = FUN_ffff0000907ad894((long)&local_8 + 4);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff000090788750(&local_8);
    if (iVar1 == 0) goto LAB_ffff000090798814;
    pcVar2 = s__SEC__get_lock_state_fail__ret____ffff0000907d8803;
  }
  else {
    pcVar2 = s__SEC__get_sboot_state_fail__ret__ffff0000907ea1bc;
  }
  FUN_ffff00009077de54(pcVar2,iVar1);
LAB_ffff000090798814:
  FUN_ffff00009077de54
            (s__SEC__lock_state___d_sboot_state_ffff0000907ed4ba,local_8 & 0xffffffff,local_8._4_4_)
  ;
  return iVar1;
}




int FUN_ffff0000b80591cc(undefined4 param_1,undefined4 param_2)

{
  int iVar1;
  char *pcVar2;
  undefined4 local_14;
  
  local_14 = 0;
  FUN_ffff0000b8059298();
  FUN_ffff0000b804d070(&DAT_ffff0000b81b6bc0,0,0x100);
  DAT_ffff0000b81b6bc1 = 0x10;
  iVar1 = FUN_ffff0000b8058e5c(0,param_1,&DAT_ffff0000b81b6b80,0x10,&local_14);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000b805936c(param_1,local_14,4,0,0,param_2,0);
    if (iVar1 == 0) {
      return 0;
    }
    pcVar2 = s___s___RPMB_SWPCB_Write_Fail__ret_ffff0000b809df8f;
  }
  else {
    pcVar2 = s___s___RPMB_Get_Write_Counter_fai_ffff0000b808bee4;
  }
  FUN_ffff0000b804c33c(pcVar2,s_rpmb_ufs_clr_wp_ffff0000b80979eb,iVar1);
  return iVar1;
}



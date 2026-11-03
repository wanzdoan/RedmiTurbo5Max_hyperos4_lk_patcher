
int FUN_ffff0000be072e6c(undefined4 param_1,undefined4 param_2)

{
  int iVar1;
  char *pcVar2;
  undefined4 local_14;
  
  local_14 = 0;
  FUN_ffff0000be072f38();
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b4e80,0,0x100);
  DAT_ffff0000be1b4e81 = 0x10;
  iVar1 = FUN_ffff0000be072afc(0,param_1,&DAT_ffff0000be1b4e40,0x10,&local_14);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000be07300c(param_1,local_14,4,0,0,param_2,0);
    if (iVar1 == 0) {
      return 0;
    }
    pcVar2 = s___s___RPMB_SWPCB_Write_Fail__ret_ffff0000be0c25bc;
  }
  else {
    pcVar2 = s___s___RPMB_Get_Write_Counter_fai_ffff0000be0abf12;
  }
  FUN_ffff0000be05fd98(pcVar2,s_rpmb_ufs_clr_wp_ffff0000be0ba712,iVar1);
  return iVar1;
}



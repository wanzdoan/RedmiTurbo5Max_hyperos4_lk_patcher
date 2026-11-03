// ffff0000907956f8  size=204  FUN_ffff0000907956f8

int FUN_ffff0000907956f8(undefined4 param_1,undefined4 param_2)

{
  int iVar1;
  char *pcVar2;
  undefined4 local_14;
  
  local_14 = 0;
  FUN_ffff0000907957c4();
  FUN_ffff00009077ee50(&DAT_ffff00009090a800,0,0x100);
  DAT_ffff00009090a801 = 0x10;
  iVar1 = FUN_ffff000090795388(0,param_1,0xffff00009090a7c0,0x10,&local_14);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff000090795898(param_1,local_14,4,0,0,param_2,0);
    if (iVar1 == 0) {
      return 0;
    }
    pcVar2 = s___s___RPMB_SWPCB_Write_Fail__ret_ffff0000907fc7ad;
  }
  else {
    pcVar2 = s___s___RPMB_Get_Write_Counter_fai_ffff0000907dbac0;
  }
  FUN_ffff00009077e11c(pcVar2,s_rpmb_ufs_clr_wp_ffff0000907f08b1,iVar1);
  return iVar1;
}



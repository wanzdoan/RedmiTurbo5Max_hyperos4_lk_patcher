// ffff000090795438  size=204  FUN_ffff000090795438

int FUN_ffff000090795438(undefined4 param_1,undefined4 param_2)

{
  int iVar1;
  char *pcVar2;
  undefined4 local_14;
  
  local_14 = 0;
  FUN_ffff000090795504();
  FUN_ffff00009077eb88(&DAT_ffff00009090a740,0,0x100);
  DAT_ffff00009090a741 = 0x10;
  iVar1 = FUN_ffff0000907950c8(0,param_1,0xffff00009090a700,0x10,&local_14);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000907955d8(param_1,local_14,4,0,0,param_2,0);
    if (iVar1 == 0) {
      return 0;
    }
    pcVar2 = s___s___RPMB_SWPCB_Write_Fail__ret_ffff0000907fc611;
  }
  else {
    pcVar2 = s___s___RPMB_Get_Write_Counter_fai_ffff0000907db99e;
  }
  FUN_ffff00009077de54(pcVar2,s_rpmb_ufs_clr_wp_ffff0000907f0729,iVar1);
  return iVar1;
}



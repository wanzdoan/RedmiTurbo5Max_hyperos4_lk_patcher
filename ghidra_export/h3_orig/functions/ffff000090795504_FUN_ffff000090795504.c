// ffff000090795504  size=212  FUN_ffff000090795504

int FUN_ffff000090795504(void)

{
  int iVar1;
  undefined4 local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff000090737364(&local_4);
  if (iVar1 == 0) {
    FUN_ffff00009077e858(0xffff00009090a700,&local_4,4);
    iVar1 = FUN_ffff000090737364(&local_4);
    if (iVar1 == 0) {
      FUN_ffff00009077e858(0xffff00009090a704,&local_4,4);
      iVar1 = FUN_ffff000090737364(&local_4);
      if (iVar1 == 0) {
        FUN_ffff00009077e858(0xffff00009090a708,&local_4,4);
        iVar1 = FUN_ffff000090737364(&local_4);
        if (iVar1 == 0) {
          FUN_ffff00009077e858(0xffff00009090a70c,&local_4,4);
          return 0;
        }
      }
    }
  }
  FUN_ffff00009077de54
            (s__s_get_rnd_is_not_ready__ret__d_ffff0000907d0554,
             s_ufs_rpmb_pre_nonce_ffff0000907d8778,iVar1);
  return iVar1;
}



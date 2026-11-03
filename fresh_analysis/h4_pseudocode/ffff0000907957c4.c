
int FUN_ffff0000907957c4(void)

{
  int iVar1;
  undefined4 local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff0000907375fc(&local_4);
  if (iVar1 == 0) {
    FUN_ffff00009077eb20(&DAT_ffff00009090a7c0,&local_4,4);
    iVar1 = FUN_ffff0000907375fc(&local_4);
    if (iVar1 == 0) {
      FUN_ffff00009077eb20(&DAT_ffff00009090a7c4,&local_4,4);
      iVar1 = FUN_ffff0000907375fc(&local_4);
      if (iVar1 == 0) {
        FUN_ffff00009077eb20(&DAT_ffff00009090a7c8,&local_4,4);
        iVar1 = FUN_ffff0000907375fc(&local_4);
        if (iVar1 == 0) {
          FUN_ffff00009077eb20(&DAT_ffff00009090a7cc,&local_4,4);
          return 0;
        }
      }
    }
  }
  FUN_ffff00009077e11c
            (s__s_get_rnd_is_not_ready__ret__d_ffff0000907d0573,
             s_ufs_rpmb_pre_nonce_ffff0000907d880e,iVar1);
  return iVar1;
}



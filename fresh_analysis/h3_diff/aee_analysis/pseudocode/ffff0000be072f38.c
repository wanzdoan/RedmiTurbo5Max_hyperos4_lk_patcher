
int FUN_ffff0000be072f38(void)

{
  int iVar1;
  undefined4 local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff0000be029a00(&local_4);
  if (iVar1 == 0) {
    FUN_ffff0000be0607b0(&DAT_ffff0000be1b4e40,&local_4,4);
    iVar1 = FUN_ffff0000be029a00(&local_4);
    if (iVar1 == 0) {
      FUN_ffff0000be0607b0(&DAT_ffff0000be1b4e44,&local_4,4);
      iVar1 = FUN_ffff0000be029a00(&local_4);
      if (iVar1 == 0) {
        FUN_ffff0000be0607b0(&DAT_ffff0000be1b4e48,&local_4,4);
        iVar1 = FUN_ffff0000be029a00(&local_4);
        if (iVar1 == 0) {
          FUN_ffff0000be0607b0(&DAT_ffff0000be1b4e4c,&local_4,4);
          return 0;
        }
      }
    }
  }
  FUN_ffff0000be05fd98
            (s__s_get_rnd_is_not_ready__ret__d_ffff0000be0a3eb2,
             s_ufs_rpmb_pre_nonce_ffff0000be0a9b58,iVar1);
  return iVar1;
}



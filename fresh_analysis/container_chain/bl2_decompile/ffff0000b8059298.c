
int FUN_ffff0000b8059298(void)

{
  int iVar1;
  undefined4 local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff0000b80212b4(&local_4);
  if (iVar1 == 0) {
    FUN_ffff0000b804cd40(&DAT_ffff0000b81b6b80,&local_4,4);
    iVar1 = FUN_ffff0000b80212b4(&local_4);
    if (iVar1 == 0) {
      FUN_ffff0000b804cd40(&DAT_ffff0000b81b6b84,&local_4,4);
      iVar1 = FUN_ffff0000b80212b4(&local_4);
      if (iVar1 == 0) {
        FUN_ffff0000b804cd40(&DAT_ffff0000b81b6b88,&local_4,4);
        iVar1 = FUN_ffff0000b80212b4(&local_4);
        if (iVar1 == 0) {
          FUN_ffff0000b804cd40(&DAT_ffff0000b81b6b8c,&local_4,4);
          return 0;
        }
      }
    }
  }
  FUN_ffff0000b804c33c
            (s__s_get_rnd_is_not_ready__ret__d_ffff0000b8085c9c,
             s_ufs_rpmb_pre_nonce_ffff0000b808a425,iVar1);
  return iVar1;
}



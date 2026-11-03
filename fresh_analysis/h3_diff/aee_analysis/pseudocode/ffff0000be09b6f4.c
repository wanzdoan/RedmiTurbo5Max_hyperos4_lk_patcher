
int FUN_ffff0000be09b6f4(void)

{
  int iVar1;
  
  FUN_ffff0000be05fd98(s__s_IN_ffff0000be0acfc9,s_CheckIdMgrAuthToken_ffff0000be0a8b12);
  iVar1 = FUN_ffff0000be09b3ac();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000be09b61c();
    if (iVar1 == -0xffffff) {
      FUN_ffff0000be05fd98(s_Auth_Token_already_be_used__inva_ffff0000be0a6734);
      iVar1 = -0xffffff;
    }
    else {
      FUN_ffff0000be05fd98(s__s_OUT_ffff0000be0c1274,s_CheckIdMgrAuthToken_ffff0000be0a8b12);
    }
  }
  else {
    FUN_ffff0000be05fd98(s_do_auth_token_check_failed_ffff0000be0c4bc6);
  }
  return iVar1;
}



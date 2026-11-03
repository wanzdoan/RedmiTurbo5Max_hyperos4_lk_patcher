// ffff0000907ce0e0  size=144  FUN_ffff0000907ce0e0

int FUN_ffff0000907ce0e0(void)

{
  int iVar1;
  
  FUN_ffff00009077de54(s__s_IN_ffff0000907dd338,s_CheckIdMgrAuthToken_ffff0000907d70f7);
  iVar1 = FUN_ffff0000907cdd98();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000907ce008();
    if (iVar1 == -0xffffff) {
      FUN_ffff00009077de54(s_Auth_Token_already_be_used__inva_ffff0000907d3c80);
      iVar1 = -0xffffff;
    }
    else {
      FUN_ffff00009077de54(s__s_OUT_ffff0000907fa7b2,s_CheckIdMgrAuthToken_ffff0000907d70f7);
    }
  }
  else {
    FUN_ffff00009077de54(s_do_auth_token_check_failed_ffff00009080010a);
  }
  return iVar1;
}



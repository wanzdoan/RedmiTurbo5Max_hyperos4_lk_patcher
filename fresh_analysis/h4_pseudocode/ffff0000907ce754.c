
int FUN_ffff0000907ce754(void)

{
  int iVar1;
  
  FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_CheckIdMgrAuthToken_ffff0000907d718d);
  iVar1 = FUN_ffff0000907ce40c();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000907ce67c();
    if (iVar1 == -0xffffff) {
      FUN_ffff00009077e11c(s_Auth_Token_already_be_used__inva_ffff0000907d3cbd);
      iVar1 = -0xffffff;
    }
    else {
      FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_CheckIdMgrAuthToken_ffff0000907d718d);
    }
  }
  else {
    FUN_ffff00009077e11c(s_do_auth_token_check_failed_ffff0000908002b2);
  }
  return iVar1;
}



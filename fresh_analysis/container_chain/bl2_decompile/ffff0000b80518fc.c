
void FUN_ffff0000b80518fc(undefined4 param_1,int param_2)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  
  lVar2 = FUN_ffff0000b804bccc(4);
  if (lVar2 == 0) {
    FUN_ffff0000b804c33c(s_env_buf_malloc_fail_in_mt_lk_set_ffff0000b80985f6);
    return;
  }
  FUN_ffff0000b804d070(lVar2,0,4);
  iVar1 = FUN_ffff0000b804d6a4(lVar2,&DAT_ffff0000b80a0e76,param_1);
  if (iVar1 < 1) {
    pcVar3 = s_env_buf_sprintf_failed_ffff0000b80895ac;
  }
  else {
    pcVar3 = s_logo_offset_ffff0000b8093d54;
    if (param_2 != 0) {
      pcVar3 = s_logo_offset_ext_ffff0000b8085ab3;
    }
    iVar1 = FUN_ffff0000b8027d24(pcVar3,lVar2);
    if (iVar1 == 0) goto LAB_ffff0000b80519a8;
    pcVar3 = s_Logo_offset_read_fail_from_sysen_ffff0000b8098624;
  }
  FUN_ffff0000b804c33c(pcVar3);
LAB_ffff0000b80519a8:
  thunk_FUN_ffff0000b8082558(lVar2);
  return;
}



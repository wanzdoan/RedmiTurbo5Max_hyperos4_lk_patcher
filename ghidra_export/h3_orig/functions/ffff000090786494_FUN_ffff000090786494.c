// ffff000090786494  size=196  FUN_ffff000090786494

void FUN_ffff000090786494(undefined4 param_1,int param_2)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  
  lVar2 = FUN_ffff00009077d24c(4);
  if (lVar2 == 0) {
    FUN_ffff00009077de54(s_env_buf_malloc_fail_in_mt_lk_set_ffff0000907f1da1);
    return;
  }
  FUN_ffff00009077eb88(lVar2,0,4);
  iVar1 = FUN_ffff00009077f38c(lVar2,&DAT_ffff000090801d01,param_1);
  if (iVar1 < 1) {
    pcVar3 = s_env_buf_sprintf_failed_ffff0000907d6c09;
  }
  else {
    pcVar3 = s_logo_offset_ffff0000907e9f8c;
    if (param_2 != 0) {
      pcVar3 = s_logo_offset_ext_ffff0000907d02ac;
    }
    iVar1 = FUN_ffff000090741a20(pcVar3,lVar2);
    if (iVar1 == 0) goto LAB_ffff000090786540;
    pcVar3 = s_Logo_offset_read_fail_from_sysen_ffff0000907f1dcf;
  }
  FUN_ffff00009077de54(pcVar3);
LAB_ffff000090786540:
  thunk_FUN_ffff0000907c2ad8(lVar2);
  return;
}



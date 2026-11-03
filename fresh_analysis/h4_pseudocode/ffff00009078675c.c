
void FUN_ffff00009078675c(undefined4 param_1,int param_2)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  
  lVar2 = FUN_ffff00009077d514(4);
  if (lVar2 == 0) {
    FUN_ffff00009077e11c(s_env_buf_malloc_fail_in_mt_lk_set_ffff0000907f1f2d);
    return;
  }
  FUN_ffff00009077ee50(lVar2,0,4);
  iVar1 = FUN_ffff00009077f654(lVar2,&DAT_ffff000090801edd,param_1);
  if (iVar1 < 1) {
    pcVar3 = s_env_buf_sprintf_failed_ffff0000907d6c9f;
  }
  else {
    pcVar3 = s_logo_offset_ffff0000907ea0d0;
    if (param_2 != 0) {
      pcVar3 = s_logo_offset_ext_ffff0000907d02cb;
    }
    iVar1 = FUN_ffff000090741cb8(pcVar3,lVar2);
    if (iVar1 == 0) goto LAB_ffff000090786808;
    pcVar3 = s_Logo_offset_read_fail_from_sysen_ffff0000907f1f5b;
  }
  FUN_ffff00009077e11c(pcVar3);
LAB_ffff000090786808:
  thunk_FUN_ffff0000907c314c(lVar2);
  return;
}



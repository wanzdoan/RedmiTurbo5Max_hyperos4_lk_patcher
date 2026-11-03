// ffff0000907c659c  size=12  FUN_ffff0000907c659c

void FUN_ffff0000907c659c(long param_1,long param_2,undefined8 param_3)

{
  int iVar1;
  
  if ((param_1 != 0) && (param_2 != 0)) {
    iVar1 = thunk_FUN_ffff0000907a4ad8(param_2,param_3);
    if (iVar1 != 0) {
      FUN_ffff00009077de54(s_sec_sha256_process_adapter___err_ffff0000907f775d,iVar1);
      return;
    }
  }
  return;
}



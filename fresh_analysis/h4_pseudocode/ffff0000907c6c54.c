
long FUN_ffff0000907c6c54(long param_1)

{
  int iVar1;
  
  if (param_1 == 0) {
    param_1 = 0;
  }
  else {
    param_1 = param_1 + 0xb0;
    iVar1 = thunk_FUN_ffff0000907a4df4(param_1);
    if (iVar1 != 0) {
      FUN_ffff00009077e11c(s_sec_sha256_done_adapter___error__ffff00009080021e,iVar1);
    }
  }
  return param_1;
}



// ffff0000907c65e0  size=80  FUN_ffff0000907c65e0

long FUN_ffff0000907c65e0(long param_1)

{
  int iVar1;
  
  if (param_1 == 0) {
    param_1 = 0;
  }
  else {
    param_1 = param_1 + 0xb0;
    iVar1 = thunk_FUN_ffff0000907a4b34(param_1);
    if (iVar1 != 0) {
      FUN_ffff00009077de54(s_sec_sha256_done_adapter___error__ffff000090800076,iVar1);
    }
  }
  return param_1;
}



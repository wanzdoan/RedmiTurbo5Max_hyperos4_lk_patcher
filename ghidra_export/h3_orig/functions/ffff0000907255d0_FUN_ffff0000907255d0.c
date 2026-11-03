// ffff0000907255d0  size=148  FUN_ffff0000907255d0

void FUN_ffff0000907255d0(code *param_1)

{
  int iVar1;
  long lVar2;
  int iVar3;
  undefined8 uVar4;
  int local_34;
  long local_18;
  
  local_18 = 0;
  local_34 = 0;
  iVar3 = FUN_ffff0000907253a8(&local_18,&local_34);
  lVar2 = local_18;
  iVar1 = local_34;
  if (((iVar3 == 0) || (local_18 == 0)) || (local_34 < 1)) {
    if (local_18 == 0) {
      return;
    }
  }
  else {
    uVar4 = FUN_ffff00009077b06c(local_18);
    (*param_1)(uVar4,iVar1);
  }
  thunk_FUN_ffff0000907c2ad8(lVar2);
  return;
}



// ffff00009076ef88  size=60  FUN_ffff00009076ef88

void FUN_ffff00009076ef88(long param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff00009076eed4((int)param_1,0,0);
  if (iVar1 == 0) {
    *(undefined4 *)(param_1 + 0xc) = *(undefined4 *)(param_1 + 4);
  }
  return;
}



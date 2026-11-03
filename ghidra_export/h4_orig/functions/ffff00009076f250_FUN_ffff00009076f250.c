// ffff00009076f250  size=60  FUN_ffff00009076f250

void FUN_ffff00009076f250(long param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff00009076f19c((int)param_1,0,0);
  if (iVar1 == 0) {
    *(undefined4 *)(param_1 + 0xc) = *(undefined4 *)(param_1 + 4);
  }
  return;
}



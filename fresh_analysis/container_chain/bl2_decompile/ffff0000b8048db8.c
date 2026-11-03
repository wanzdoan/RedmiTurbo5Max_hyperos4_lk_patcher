
void FUN_ffff0000b8048db8(long param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b8048d04((int)param_1,0,0);
  if (iVar1 == 0) {
    *(undefined4 *)(param_1 + 0xc) = *(undefined4 *)(param_1 + 4);
  }
  return;
}



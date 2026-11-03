
void FUN_ffff0000be09f408(long param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000be09c1d8(param_1,1);
  if ((iVar1 == 0) && (iVar1 = FUN_ffff0000be09c1d8(param_1 + 0x18,1), iVar1 == 0)) {
    FUN_ffff0000be09c1d8(param_1 + 0x30,0);
    return;
  }
  return;
}



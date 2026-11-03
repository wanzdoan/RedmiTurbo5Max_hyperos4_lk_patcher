
void FUN_ffff0000be0490ac(void)

{
  int iVar1;
  
  if (DAT_ffff0000be126098 == 0) {
    iVar1 = FUN_ffff0000be048f80();
    DAT_ffff0000be126098 = iVar1 + 0xffffU & 0xffff0000;
  }
  return;
}



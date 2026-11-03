
void FUN_ffff0000b8004f78(int *param_1)

{
  int iVar1;
  uint local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x10f,&local_4);
  if (iVar1 == 0) {
    iVar1 = 300;
    if (5 < (local_4 & 0x3f)) {
      iVar1 = (local_4 & 0x3f) * 0x32;
    }
    *param_1 = iVar1;
  }
  return;
}



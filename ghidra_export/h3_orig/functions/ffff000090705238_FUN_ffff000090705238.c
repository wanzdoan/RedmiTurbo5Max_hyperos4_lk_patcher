// ffff000090705238  size=96  FUN_ffff000090705238

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090705238(int *param_1)

{
  int iVar1;
  uint local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x10f,&local_4);
  if (iVar1 == 0) {
    iVar1 = 300;
    if (5 < (local_4 & 0x3f)) {
      iVar1 = (local_4 & 0x3f) * 0x32;
    }
    *param_1 = iVar1;
  }
  return;
}



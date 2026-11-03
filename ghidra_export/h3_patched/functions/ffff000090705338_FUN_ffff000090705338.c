// ffff000090705338  size=84  FUN_ffff000090705338

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090705338(int *param_1)

{
  int iVar1;
  uint local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x109,&local_4);
  if (iVar1 == 0) {
    *param_1 = (local_4 & 0xff) * 0x19 + 100;
  }
  return;
}




void FUN_ffff0000be004df4(int *param_1)

{
  int iVar1;
  uint local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x109,&local_4);
  if (iVar1 == 0) {
    *param_1 = (local_4 & 0xff) * 0x19 + 100;
  }
  return;
}



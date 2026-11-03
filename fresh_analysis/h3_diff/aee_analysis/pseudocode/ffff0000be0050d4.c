
void FUN_ffff0000be0050d4(void)

{
  undefined8 uVar1;
  int iVar2;
  uint local_4;
  
  uVar1 = DAT_ffff0000be0f9050;
  local_4 = 0;
  iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x114,&local_4);
  if (-1 < iVar2) {
    local_4 = local_4 | 4;
    FUN_ffff0000be029920(uVar1,0x114);
  }
  return;
}



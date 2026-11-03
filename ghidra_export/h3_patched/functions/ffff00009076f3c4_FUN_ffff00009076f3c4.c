// ffff00009076f3c4  size=76  FUN_ffff00009076f3c4

ulong FUN_ffff00009076f3c4(undefined8 param_1,ulong param_2)

{
  int iVar1;
  uint uVar2;
  int local_4;
  
  local_4 = 0;
  iVar1 = (int)param_2;
  do {
    if (iVar1 < 0) break;
    uVar2 = FUN_ffff00009076cc68(param_1,param_2,&local_4);
    param_2 = (ulong)uVar2;
    iVar1 = local_4;
  } while (-1 < (int)uVar2);
  return param_2 & 0xffffffff;
}



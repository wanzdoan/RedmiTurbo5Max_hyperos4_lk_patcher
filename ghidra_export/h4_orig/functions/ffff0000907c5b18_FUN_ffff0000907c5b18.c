// ffff0000907c5b18  size=128  FUN_ffff0000907c5b18

undefined4 FUN_ffff0000907c5b18(undefined8 param_1,long param_2,ulong *param_3)

{
  int iVar1;
  undefined4 uVar2;
  uint local_14;
  
  local_14 = 0;
  if (param_3 == (ulong *)0x0) {
    uVar2 = 2;
  }
  else {
    *param_3 = 0;
    uVar2 = 2;
    iVar1 = FUN_ffff000090788998(2,&local_14);
    if ((iVar1 == 0) && ((param_2 != 1 || (iVar1 = FUN_ffff000090788998(3,&local_14), iVar1 == 0))))
    {
      uVar2 = 0;
      *param_3 = (ulong)local_14;
    }
  }
  return uVar2;
}



// ffff00009076d02c  size=208  FUN_ffff00009076d02c

int FUN_ffff00009076d02c(undefined8 param_1,ulong param_2)

{
  bool bVar1;
  int iVar2;
  int iVar3;
  int local_28;
  int local_24;
  
  local_28 = 0;
  if (-1 < (int)param_2) {
    if (((param_2 & 0x80000003) != 0) ||
       (local_24 = (int)param_2, iVar2 = FUN_ffff00009076cd48(param_1,param_2,&local_24), iVar2 != 1
       )) {
      local_28 = -4;
      goto LAB_ffff00009076d0d4;
    }
    local_28 = local_24;
    if (local_24 < 0) goto LAB_ffff00009076d0d4;
  }
  do {
    iVar2 = local_28;
    iVar3 = FUN_ffff00009076cd48(param_1,local_28,&local_28);
    if (iVar3 == 1) {
      bVar1 = false;
      goto LAB_ffff00009076d0d8;
    }
    if (iVar3 == 2) goto LAB_ffff00009076d0d4;
  } while (iVar3 != 9);
  if (-2 < local_28) {
    local_28 = -1;
  }
LAB_ffff00009076d0d4:
  bVar1 = true;
  iVar2 = local_28;
LAB_ffff00009076d0d8:
  if (iVar2 < 0) {
    bVar1 = true;
  }
  if (bVar1) {
    iVar2 = -1;
  }
  return iVar2;
}



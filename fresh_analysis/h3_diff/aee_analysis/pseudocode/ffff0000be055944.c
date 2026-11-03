
int FUN_ffff0000be055944(undefined8 param_1,ulong param_2)

{
  bool bVar1;
  int iVar2;
  int iVar3;
  int local_28;
  int local_24;
  
  local_28 = 0;
  if (-1 < (int)param_2) {
    if (((param_2 & 0x80000003) != 0) ||
       (local_24 = (int)param_2, iVar2 = FUN_ffff0000be055660(param_1,param_2,&local_24), iVar2 != 1
       )) {
      local_28 = -4;
      goto LAB_ffff0000be0559ec;
    }
    local_28 = local_24;
    if (local_24 < 0) goto LAB_ffff0000be0559ec;
  }
  do {
    iVar2 = local_28;
    iVar3 = FUN_ffff0000be055660(param_1,local_28,&local_28);
    if (iVar3 == 1) {
      bVar1 = false;
      goto LAB_ffff0000be0559f0;
    }
    if (iVar3 == 2) goto LAB_ffff0000be0559ec;
  } while (iVar3 != 9);
  if (-2 < local_28) {
    local_28 = -1;
  }
LAB_ffff0000be0559ec:
  bVar1 = true;
  iVar2 = local_28;
LAB_ffff0000be0559f0:
  if (iVar2 < 0) {
    bVar1 = true;
  }
  if (bVar1) {
    iVar2 = -1;
  }
  return iVar2;
}



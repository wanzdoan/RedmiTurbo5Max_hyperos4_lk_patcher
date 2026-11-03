
int FUN_ffff0000be0472e8(long *param_1)

{
  int iVar1;
  long lVar2;
  
  iVar1 = 0;
  if (param_1 != (long *)0x0) {
    lVar2 = *param_1;
    if ((lVar2 == 0) || (param_1[1] == 0)) {
      return 0;
    }
    iVar1 = *(int *)(lVar2 + 0x20);
    if (iVar1 == 0) {
      return *(int *)(lVar2 + 0x18);
    }
  }
  return iVar1;
}



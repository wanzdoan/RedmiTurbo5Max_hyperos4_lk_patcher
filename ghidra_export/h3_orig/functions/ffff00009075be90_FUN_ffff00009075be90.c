// ffff00009075be90  size=64  FUN_ffff00009075be90

int FUN_ffff00009075be90(long *param_1)

{
  int iVar1;
  long lVar2;
  
  iVar1 = 0;
  if (param_1 != (long *)0x0) {
    lVar2 = *param_1;
    if ((lVar2 == 0) || (param_1[1] == 0)) {
      return 0;
    }
    iVar1 = *(int *)(lVar2 + 0x24);
    if (iVar1 == 0) {
      return *(int *)(lVar2 + 0x1c);
    }
  }
  return iVar1;
}



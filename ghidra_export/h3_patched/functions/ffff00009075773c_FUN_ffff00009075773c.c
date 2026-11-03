// ffff00009075773c  size=168  FUN_ffff00009075773c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075773c(int param_1,long param_2)

{
  char cVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  int *piVar5;
  
  if (*(int *)(param_2 + 0x3e0) == 0) {
    iVar2 = (uint)(*(int *)(param_2 + 0x3e4) != 0) << 3;
    cVar1 = *(char *)(param_2 + 0x990);
  }
  else {
    iVar2 = 8;
    cVar1 = *(char *)(param_2 + 0x990);
  }
  if ((cVar1 != '\0') && (*(char *)(param_2 + 0x2d8) != '\0')) {
    if (param_1 == 0xb7) {
      piVar5 = (int *)&DAT_ffff00009087b638;
      iVar3 = *(int *)(param_2 + 0x2d4) + iVar2;
      iVar4 = *(int *)(param_2 + 0x2d0) + iVar2;
      _DAT_ffff00009087b640 = iVar2;
      *(int *)(param_2 + 0x2d4) = iVar3;
      *(int *)(param_2 + 0x2d0) = iVar4;
    }
    else {
      if (param_1 != 0xb6) {
        return 0;
      }
      piVar5 = (int *)&DAT_ffff00009087b62c;
      iVar3 = *(int *)(param_2 + 0x2cc) + iVar2;
      iVar4 = *(int *)(param_2 + 0x2c8) + iVar2;
      _DAT_ffff00009087b634 = iVar2;
      *(int *)(param_2 + 0x2cc) = iVar3;
      *(int *)(param_2 + 0x2c8) = iVar4;
    }
    *piVar5 = iVar4;
    piVar5[1] = iVar3;
  }
  return 0;
}



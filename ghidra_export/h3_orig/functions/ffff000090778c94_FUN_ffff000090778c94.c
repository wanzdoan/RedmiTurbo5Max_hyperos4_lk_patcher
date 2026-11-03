// ffff000090778c94  size=152  FUN_ffff000090778c94

void FUN_ffff000090778c94(long param_1)

{
  int iVar1;
  long lVar2;
  
  if (*(int *)(param_1 + 0x1734) == 0x10) {
    lVar2 = *(long *)(param_1 + 0x28);
    iVar1 = 0;
    *(long *)(param_1 + 0x28) = lVar2 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar2) = *(undefined1 *)(param_1 + 0x1730);
    lVar2 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar2 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar2) = *(undefined1 *)(param_1 + 0x1731);
    *(undefined2 *)(param_1 + 0x1730) = 0;
  }
  else {
    if (*(int *)(param_1 + 0x1734) < 8) {
      return;
    }
    lVar2 = *(long *)(param_1 + 0x28);
    *(long *)(param_1 + 0x28) = lVar2 + 1;
    *(undefined1 *)(*(long *)(param_1 + 0x10) + lVar2) = *(undefined1 *)(param_1 + 0x1730);
    iVar1 = *(int *)(param_1 + 0x1734) + -8;
    *(ushort *)(param_1 + 0x1730) = (ushort)*(byte *)(param_1 + 0x1731);
  }
  *(int *)(param_1 + 0x1734) = iVar1;
  return;
}



// ffff00009074c0d0  size=196  FUN_ffff00009074c0d0

void FUN_ffff00009074c0d0(int param_1)

{
  int iVar1;
  ulong uVar2;
  long lVar3;
  int iVar4;
  
  uVar2 = (ulong)(param_1 != 0x2d && param_1 != 0x2b);
  if ((*(byte *)((&DAT_ffff00009080cd68)[uVar2] + 0x34) & 3) == 0) {
    iVar4 = 20000;
    lVar3 = *(long *)(s_0123456789abcdef_ffff000090804a48 + uVar2 * 8 + 0x10);
    do {
      iVar1 = *(int *)(lVar3 + 0xc);
      FUN_ffff00009077cbd8(0x32);
      if (-1 < iVar1) {
        return;
      }
      iVar4 = iVar4 + -1;
    } while (iVar4 != 0);
    lVar3 = 1;
    if (param_1 != 0x2b) {
      lVar3 = 2;
    }
    lVar3 = lVar3 - uVar2;
    do {
      lVar3 = lVar3 + -1;
    } while (lVar3 != 0);
  }
  return;
}



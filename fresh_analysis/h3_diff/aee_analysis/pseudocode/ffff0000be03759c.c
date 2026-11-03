
void FUN_ffff0000be03759c(int param_1)

{
  int iVar1;
  ulong uVar2;
  long lVar3;
  int iVar4;
  
  uVar2 = (ulong)(param_1 != 0x2d && param_1 != 0x2b);
  if ((*(byte *)((&DAT_ffff0000be0caec8)[uVar2] + 0x34) & 3) == 0) {
    iVar4 = 20000;
    lVar3 = *(long *)(s_0123456789abcdef_ffff0000be0c8230 + uVar2 * 8 + 0x10);
    do {
      iVar1 = *(int *)(lVar3 + 0xc);
      FUN_ffff0000be05ede4(0x32);
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



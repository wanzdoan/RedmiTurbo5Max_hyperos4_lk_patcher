
void FUN_ffff0000b802e304(int param_1,int param_2)

{
  long lVar1;
  ulong uVar2;
  long lVar3;
  int iVar4;
  
  uVar2 = (ulong)(param_1 != 0x2d && param_1 != 0x2b);
  lVar1 = (&DAT_ffff0000b80a5738)[uVar2];
  if ((param_2 != 0) || ((*(uint *)(lVar1 + 0x34) & 3) == 0)) {
    lVar3 = *(long *)(s_0123456789abcdef_ffff0000b80a2a80 + uVar2 * 8 + 0x10);
    *(uint *)(lVar3 + 0xc) = *(uint *)(lVar3 + 0xc) & 0xfffffffb;
    iVar4 = 500000;
    if ((*(byte *)(lVar1 + 0x34) & 3) != 0) {
      iVar4 = 0x4074;
    }
    do {
      if ((*(uint *)(lVar3 + 0xc) >> 2 & 1) != 0) {
        return;
      }
      FUN_ffff0000b804b704(2);
      iVar4 = iVar4 + -1;
    } while (iVar4 != 0);
    lVar1 = 1;
    if (param_1 != 0x2b) {
      lVar1 = 2;
    }
    lVar1 = lVar1 - uVar2;
    do {
      lVar1 = lVar1 + -1;
    } while (lVar1 != 0);
  }
  return;
}




int FUN_ffff0000907985c0(long param_1,int param_2)

{
  int iVar1;
  ulong uVar2;
  undefined4 *puVar3;
  
  if (param_2 < 1) {
    iVar1 = 0;
  }
  else {
    uVar2 = 0;
    iVar1 = 0;
    puVar3 = &DAT_ffff0000908373c4;
    do {
      if ((*(byte *)(puVar3 + -2) >> 1 & 1) != 0) {
        *(undefined4 *)(param_1 + (long)iVar1 * 4) = *puVar3;
        iVar1 = iVar1 + 1;
      }
      if (10 < uVar2) {
        return iVar1;
      }
      puVar3 = puVar3 + 6;
      uVar2 = uVar2 + 1;
    } while (iVar1 < param_2);
  }
  return iVar1;
}



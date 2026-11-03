// ffff00009077efc4  size=132  FUN_ffff00009077efc4

int FUN_ffff00009077efc4(byte *param_1,undefined1 *param_2)

{
  int iVar1;
  int iVar2;
  uint uVar3;
  
  iVar1 = FUN_ffff00009077db9c(*param_1);
  iVar2 = FUN_ffff00009077db9c(*param_2);
  for (iVar1 = iVar1 - iVar2; uVar3 = iVar1 * 0x1000000, uVar3 == 0; iVar1 = iVar1 - iVar2) {
    param_2 = param_2 + 1;
    uVar3 = (uint)*param_1;
    if (*param_1 == 0) break;
    iVar1 = FUN_ffff00009077db9c(param_1[1]);
    iVar2 = FUN_ffff00009077db9c(*param_2);
    param_1 = param_1 + 1;
  }
  return (int)uVar3 >> 0x18;
}



// ffff00009071c898  size=116  FUN_ffff00009071c898

void FUN_ffff00009071c898(long param_1)

{
  int iVar1;
  uint uVar2;
  long lVar3;
  uint uVar4;
  
  iVar1 = FUN_ffff000090781854();
  if (iVar1 != 0) {
    uVar4 = 0;
    do {
      lVar3 = FUN_ffff000090781868(uVar4,2);
      *(long *)(lVar3 + 0x18) = param_1;
      FUN_ffff000090781890(uVar4,2,lVar3);
      uVar4 = uVar4 + 1;
      param_1 = param_1 + (ulong)*(uint *)(lVar3 + 0x20);
      uVar2 = FUN_ffff000090781854();
    } while (uVar4 < uVar2);
  }
  return;
}



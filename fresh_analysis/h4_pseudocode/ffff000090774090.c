
void FUN_ffff000090774090(long param_1)

{
  uint uVar1;
  long lVar2;
  long lVar3;
  long lVar4;
  ulong uVar5;
  
  lVar4 = *(long *)(param_1 + 0x38);
  FUN_ffff000090778f5c(lVar4);
  uVar1 = *(uint *)(param_1 + 0x20);
  if (*(uint *)(lVar4 + 0x28) <= *(uint *)(param_1 + 0x20)) {
    uVar1 = *(uint *)(lVar4 + 0x28);
  }
  uVar5 = (ulong)uVar1;
  if (uVar1 != 0) {
    FUN_ffff00009077eb20(*(undefined8 *)(param_1 + 0x18),*(undefined8 *)(lVar4 + 0x20),uVar5);
    lVar2 = *(long *)(lVar4 + 0x20);
    lVar3 = *(long *)(lVar4 + 0x28);
    *(ulong *)(param_1 + 0x18) = *(long *)(param_1 + 0x18) + uVar5;
    lVar3 = lVar3 - uVar5;
    *(ulong *)(param_1 + 0x28) = *(long *)(param_1 + 0x28) + uVar5;
    *(uint *)(param_1 + 0x20) = *(int *)(param_1 + 0x20) - uVar1;
    *(ulong *)(lVar4 + 0x20) = lVar2 + uVar5;
    *(long *)(lVar4 + 0x28) = lVar3;
    if (lVar3 == 0) {
      *(undefined8 *)(lVar4 + 0x20) = *(undefined8 *)(lVar4 + 0x10);
    }
  }
  return;
}



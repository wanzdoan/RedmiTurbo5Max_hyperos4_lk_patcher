// ffff00009078fd78  size=48  FUN_ffff00009078fd78

void FUN_ffff00009078fd78(long param_1,uint param_2)

{
  undefined8 uVar1;
  ulong uVar2;
  
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
    *(undefined8 *)(param_1 + 0x48) = 0;
    *(ulong *)(param_1 + 0x80) = *(ulong *)(param_1 + 0x80) & (long)~(1 << (ulong)(param_2 & 0x1f));
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
    return;
  }
  *(undefined8 *)(param_1 + 0x48) = 0;
  *(ulong *)(param_1 + 0x80) = *(ulong *)(param_1 + 0x80) & (long)~(1 << (ulong)(param_2 & 0x1f));
  return;
}



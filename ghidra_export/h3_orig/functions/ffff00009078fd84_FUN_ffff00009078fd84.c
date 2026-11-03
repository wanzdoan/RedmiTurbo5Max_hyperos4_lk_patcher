// ffff00009078fd84  size=52  FUN_ffff00009078fd84

void FUN_ffff00009078fd84(long param_1,uint param_2)

{
  ulong uVar1;
  
  uVar1 = daif;
  daif = uVar1 | 0x80;
  *(undefined8 *)(param_1 + 0x48) = 0;
  *(ulong *)(param_1 + 0x80) = *(ulong *)(param_1 + 0x80) & (long)~(1 << (ulong)(param_2 & 0x1f));
  uVar1 = daif;
  daif = uVar1 & 0xffffffffffffff7f;
  return;
}



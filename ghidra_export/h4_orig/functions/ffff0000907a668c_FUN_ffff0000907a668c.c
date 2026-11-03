// ffff0000907a668c  size=52  FUN_ffff0000907a668c

void FUN_ffff0000907a668c(ulong *param_1,long param_2,uint param_3)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  
  if (param_3 != 0) {
    uVar3 = param_3 * 8;
    uVar2 = (ulong)param_3;
    do {
      uVar3 = uVar3 - 8;
      uVar2 = uVar2 - 1;
      uVar1 = (*(ulong *)(param_2 + (ulong)uVar3) & 0xff00ff00ff00ff00) >> 8 |
              (*(ulong *)(param_2 + (ulong)uVar3) & 0xff00ff00ff00ff) << 8;
      uVar1 = (uVar1 & 0xffff0000ffff0000) >> 0x10 | (uVar1 & 0xffff0000ffff) << 0x10;
      *param_1 = uVar1 >> 0x20 | uVar1 << 0x20;
      param_1 = param_1 + 1;
    } while (uVar2 != 0);
  }
  return;
}




void FUN_ffff0000907a2bac(ulong *param_1,long param_2,uint param_3)

{
  bool bVar1;
  ulong uVar2;
  uint uVar3;
  ulong uVar4;
  
  if (0 < (int)param_3) {
    uVar2 = (ulong)((int)*param_1 + param_3);
    uVar4 = *param_1 & 0x3f;
    *param_1 = uVar2;
    if (uVar2 < param_3) {
      param_1[1] = param_1[1] + 1;
    }
    if (uVar4 != 0) {
      uVar3 = 0x40 - (int)uVar4;
      if (uVar3 <= param_3) {
        FUN_ffff00009077eb20((long)(param_1 + 7) + uVar4,param_2,(ulong)uVar3);
        FUN_ffff0000907a2cb8(param_1,param_1 + 7);
        uVar4 = 0;
        param_3 = param_3 - uVar3;
        param_2 = param_2 + (ulong)uVar3;
      }
    }
    uVar3 = param_3;
    if (0x3f < (int)param_3) {
      do {
        FUN_ffff0000907a2cb8(param_1,param_2);
        param_3 = uVar3 - 0x40;
        param_2 = param_2 + 0x40;
        bVar1 = 0x7f < uVar3;
        uVar3 = param_3;
      } while (bVar1);
    }
    if (0 < (int)param_3) {
      FUN_ffff00009077eb20((long)param_1 + uVar4 + 0x38,param_2,param_3);
      return;
    }
  }
  return;
}



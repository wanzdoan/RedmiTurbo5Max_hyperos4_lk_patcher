
undefined4 FUN_ffff0000b8048b84(ulong param_1,ulong param_2,uint param_3,int param_4)

{
  uint uVar1;
  uint uVar2;
  undefined4 uVar3;
  ulong uVar4;
  
  if (-1 < (int)param_3) {
    uVar4 = (ulong)param_3;
    if (!CARRY8(param_2 - param_1,uVar4)) {
      uVar1 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8;
      uVar2 = (*(uint *)(param_1 + 0x20) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x20) & 0xff00ff) << 8;
      uVar1 = (uVar2 >> 0x10 | uVar2 << 0x10) + (uVar1 >> 0x10 | uVar1 << 0x10);
      if ((param_2 - param_1) + uVar4 <= (ulong)uVar1) {
        uVar3 = 0xfffffffc;
        if ((param_1 <= param_2) && (param_3 <= uVar1 + param_4)) {
          uVar2 = (*(uint *)(param_1 + 4) & 0xff00ff00) >> 8 |
                  (*(uint *)(param_1 + 4) & 0xff00ff) << 8;
          if ((uVar2 >> 0x10 | uVar2 << 0x10) < (param_4 - param_3) + uVar1) {
            return 0xfffffffd;
          }
          FUN_ffff0000b804ce58
                    (param_2 + (long)param_4,param_2 + uVar4,(param_1 + uVar1) - (param_2 + uVar4));
          uVar3 = 0;
        }
        return uVar3;
      }
    }
  }
  return 0xfffffffc;
}



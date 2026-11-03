// File offset entry: 0x3474

ulong FUN_02003374(ulong param_1,ulong param_2,ulong param_3)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  long lVar5;
  ulong uVar6;
  ulong uVar7;
  
  uVar3 = (uint)param_1;
  if ((uVar3 & 3) == 3) {
    return 0xfffffffffffffff8;
  }
  uVar2 = uVar3 >> 2 & 3;
  if (3 < uVar2) {
    if ((*(ulong *)(param_1 + 0x18) <= param_2) &&
       (param_2 <= (*(ulong *)(param_1 + 0x18) + *(long *)(param_1 + 0x20)) - 1)) {
      if ((param_2 & 0xfff) != 0) {
        return 0xfffffff8;
      }
      uVar6 = (param_3 & 0xffffffff) * 0x1000;
      if ((*(byte *)(param_1 + 0x10) & 1) == 0) {
        lVar4 = 0;
        lVar5 = 0x27;
      }
      else {
        lVar4 = -0x80000000;
        lVar5 = 0x1f;
      }
      uVar7 = 1L << lVar5;
      if (uVar7 < uVar6 || uVar7 + (param_3 & 0xffffffff) * -0x1000 < param_2 - lVar4) {
        FUN_0208ada0(0x20be388,0x20b8201,0x212,param_2,uVar6,lVar4,uVar7);
      }
      else {
        if (*(long *)(param_1 + 8) != 0) {
          func_0x020030dc(param_2,param_2 - lVar4,uVar6,0x1e,0xc,*(long *)(param_1 + 8));
          DataSynchronizationBarrier(3,3,0);
          return 0;
        }
        FUN_0208ada0(0x20c05ec,0x20b8201,0x217,lVar5,0x1e,0xc);
      }
      return 0xfffffff8;
    }
    return 0xffffffdb;
  }
  uVar6 = *(ulong *)((ulong)(uVar3 & 3) * 8 + 0x20d16f0);
  switch(uVar2) {
  case 1:
    uVar6 = uVar6 | 0x40;
    break;
  case 2:
    uVar6 = uVar6 | 0x80;
    break;
  case 3:
    uVar6 = uVar6 | 0xc0;
  }
  uVar7 = 0x40000000000000;
  if ((param_1 & 4) != 0) {
    uVar7 = 0x20000000000000;
  }
  uVar1 = 0x60000000000000;
  if ((param_1 & 0x10) == 0) {
    uVar1 = uVar7 | (ulong)((uVar3 & 2) >> 1) << 0x32;
  }
  return uVar6 | uVar1 | uVar3 & 0x20;
}



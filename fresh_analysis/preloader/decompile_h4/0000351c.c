// File offset entry: 0x351c

undefined8 FUN_0200341c(long param_1,ulong param_2,ulong param_3)

{
  ulong uVar1;
  long lVar2;
  long lVar3;
  ulong uVar4;
  
  if ((*(ulong *)(param_1 + 0x18) <= param_2) &&
     (param_2 <= (*(ulong *)(param_1 + 0x18) + *(long *)(param_1 + 0x20)) - 1)) {
    if ((param_2 & 0xfff) != 0) {
      return 0xfffffff8;
    }
    uVar1 = (param_3 & 0xffffffff) * 0x1000;
    if ((*(byte *)(param_1 + 0x10) & 1) == 0) {
      lVar2 = 0;
      lVar3 = 0x27;
    }
    else {
      lVar2 = -0x80000000;
      lVar3 = 0x1f;
    }
    uVar4 = 1L << lVar3;
    if (uVar4 < uVar1 || uVar4 + (param_3 & 0xffffffff) * -0x1000 < param_2 - lVar2) {
      FUN_0208ada0(0x20be388,0x20b8201,0x212,param_2,uVar1,lVar2,uVar4);
    }
    else {
      if (*(long *)(param_1 + 8) != 0) {
        func_0x020030dc(param_2,param_2 - lVar2,uVar1,0x1e,0xc,*(long *)(param_1 + 8));
        DataSynchronizationBarrier(3,3,0);
        return 0;
      }
      FUN_0208ada0(0x20c05ec,0x20b8201,0x217,lVar3,0x1e,0xc);
    }
    return 0xfffffff8;
  }
  return 0xffffffdb;
}



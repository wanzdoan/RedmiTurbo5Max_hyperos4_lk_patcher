// File offset entry: 0x339c

undefined8
FUN_0200329c(long param_1,ulong param_2,undefined8 param_3,uint param_4,undefined4 param_5)

{
  uint uVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  
  if ((param_2 < *(ulong *)(param_1 + 0x18)) ||
     ((*(ulong *)(param_1 + 0x18) + *(long *)(param_1 + 0x20)) - 1 < param_2)) {
    uVar2 = 0xffffffdb;
  }
  else if ((((uint)param_3 | (uint)param_2) & 0xfff) == 0) {
    if (param_4 == 0) {
      uVar2 = 0;
    }
    else {
      uVar1 = *(uint *)(param_1 + 0x10);
      uVar2 = func_0x02003374(param_5);
      if ((uVar1 & 1) == 0) {
        uVar3 = 0;
        uVar4 = 0x27;
      }
      else {
        uVar3 = 0xffffffff80000000;
        uVar4 = 0x1f;
      }
      uVar2 = func_0x02002c7c(param_2,param_3,(ulong)param_4 << 0xc,uVar2,uVar3,uVar4,0x1e,0xc,
                              *(undefined8 *)(param_1 + 8));
    }
  }
  else {
    uVar2 = 0xfffffff8;
  }
  return uVar2;
}



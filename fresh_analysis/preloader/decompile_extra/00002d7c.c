// File offset entry: 0x2d7c

undefined8
FUN_02002c7c(long param_1,undefined8 param_2,ulong param_3,undefined8 param_4,long param_5,
            ulong param_6,undefined4 param_7,undefined4 param_8,long param_9)

{
  undefined8 uVar1;
  ulong uVar2;
  
  uVar2 = 1L << (param_6 & 0x3f);
  if (uVar2 < param_3 || uVar2 - param_3 < (ulong)(param_1 - param_5)) {
    FUN_0208ada0(0x20be388,0x20b71ea,0x1f8,param_1,param_3,param_5,uVar2);
  }
  else {
    if (param_9 != 0) {
      uVar1 = func_0x02002d34(param_1,param_1 - param_5,param_2,param_3,param_4,param_7,param_8,
                              param_9);
      DataSynchronizationBarrier(3,3,0);
      return uVar1;
    }
    FUN_0208ada0(0x20c05ec,0x20b71ea,0x1fd);
  }
  return 0xfffffff8;
}



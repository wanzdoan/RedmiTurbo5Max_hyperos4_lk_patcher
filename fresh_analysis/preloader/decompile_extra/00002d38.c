// File offset entry: 0x2d38

undefined8 FUN_02002c38(undefined8 param_1,long param_2)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  ulong in_x5;
  ulong in_x6;
  ulong in_x7;
  ulong uVar4;
  long unaff_x29;
  undefined1 auVar5 [16];
  
  FUN_0208ada0(0x20b233f,0x20c7afc,0x23,param_1);
  uVar3 = 0x20c7ab3;
  auVar5 = FUN_0208b224(0x20b9447,0x24);
  lVar1 = auVar5._0_8_;
  uVar4 = 1L << (in_x5 & 0x3f);
  if (uVar4 < uVar3 || uVar4 - uVar3 < (ulong)(lVar1 - param_2)) {
    FUN_0208ada0(0x20be388,0x20b71ea,0x1f8,lVar1,uVar3,param_2,uVar4);
  }
  else {
    if (unaff_x29 != 0) {
      uVar2 = FUN_02002d34(lVar1,lVar1 - param_2,auVar5._8_8_,uVar3,param_1,in_x6 & 0xffffffff,
                           in_x7 & 0xffffffff,unaff_x29,&stack0xfffffffffffffff0,FUN_02002c7c);
      DataSynchronizationBarrier(3,3,0);
      return uVar2;
    }
    FUN_0208ada0(0x20c05ec,0x20b71ea,0x1fd);
  }
  return 0xfffffff8;
}



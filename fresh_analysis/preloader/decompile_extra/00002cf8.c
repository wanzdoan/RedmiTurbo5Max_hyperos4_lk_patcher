// File offset entry: 0x2cf8

undefined8 FUN_02002bf8(undefined8 param_1)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  ulong in_x5;
  ulong in_x6;
  ulong in_x7;
  ulong uVar4;
  ulong uVar5;
  undefined1 auVar6 [16];
  undefined1 auVar7 [16];
  undefined1 *puStack_20;
  code *pcStack_18;
  
  FUN_0208ada0(0x20c1588,0x20b9442,0x1b,param_1);
  auVar6 = FUN_0208b224(0x20b9447,0x1c,0x20c7ab3);
  pcStack_18 = FUN_02002c38;
  puStack_20 = &stack0xfffffffffffffff0;
  FUN_0208ada0(0x20b233f,0x20c7afc,0x23,auVar6._0_8_);
  uVar3 = 0x20c7ab3;
  auVar7 = FUN_0208b224(0x20b9447,0x24);
  lVar1 = auVar7._0_8_;
  uVar4 = 1L << (in_x5 & 0x3f);
  uVar5 = lVar1 - auVar6._8_8_;
  if (uVar4 < uVar3 || uVar4 - uVar3 < uVar5) {
    FUN_0208ada0(0x20be388,0x20b71ea,0x1f8,lVar1,uVar3,auVar6._8_8_,uVar4);
  }
  else {
    if (puStack_20 != (undefined1 *)0x0) {
      uVar2 = FUN_02002d34(lVar1,uVar5,auVar7._8_8_,uVar3,auVar6._0_8_,in_x6 & 0xffffffff,
                           in_x7 & 0xffffffff,puStack_20,&puStack_20,FUN_02002c7c);
      DataSynchronizationBarrier(3,3,0);
      return uVar2;
    }
    FUN_0208ada0(0x20c05ec,0x20b71ea,0x1fd);
  }
  return 0xfffffff8;
}



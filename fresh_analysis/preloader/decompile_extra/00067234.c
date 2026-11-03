// File offset entry: 0x67234

ulong FUN_02067134(long param_1,uint param_2,uint param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  undefined8 uVar4;
  long lVar5;
  long extraout_x8;
  ulong uVar6;
  undefined1 auVar7 [16];
  
  if (param_2 == 0) {
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20c71a5,0x20b58bc);
    uVar6 = 0xffffffff;
  }
  else {
    uRam000000000211be35 = 1;
    if (0x400000U - *(int *)(lRam000000000211be38 + 4) <= param_3) {
      param_3 = 0;
    }
    uVar6 = (ulong)param_3;
    uVar3 = 0;
    if (0xfffff < param_2) {
      uVar3 = param_2 - 0x100000;
    }
    if (0xfffff < param_2) {
      param_2 = 0x100000;
    }
    uVar1 = (0x400000 - *(int *)(lRam000000000211be38 + 4)) - param_3;
    uVar2 = param_2 - uVar1;
    param_1 = param_1 + (ulong)uVar3;
    lVar5 = uVar6 + lRam000000000211be48 + -0x400000;
    if (param_2 < uVar1 || uVar2 == 0) {
      uVar3 = FUN_0207e954(0x20babff,lVar5,param_1,param_2);
      if (param_2 == uVar3) {
        uRam000000000211be35 = 0;
        return (ulong)(param_3 + param_2);
      }
      uVar4 = 0x20bcb5f;
    }
    else {
      auVar7 = FUN_0207e954(0x20babff,lVar5,param_1,uVar1);
      uRam000000000211be35 = 1;
      if (uVar1 == auVar7._0_4_) {
        uVar4 = FUN_02068620(lRam000000000211be48,auVar7._0_8_,auVar7._8_8_,param_1 + (ulong)uVar1);
        uVar3 = FUN_0207e954(uVar4,extraout_x8 + -0x400000);
        if (uVar2 == uVar3) {
          uRam000000000211be35 = 0;
          return (ulong)uVar2;
        }
        uRam000000000211be35 = 1;
        FUN_0208ada0(0x20c1e0f,0x20b58bc,0x20bac05);
        uRam000000000211be35 = 0;
        return 0;
      }
      uVar4 = 0x20ca15c;
    }
    uRam000000000211be35 = 1;
    FUN_0208ada0(uVar4,0x20b58bc,0x20bac05);
  }
  uRam000000000211be35 = 0;
  return uVar6;
}



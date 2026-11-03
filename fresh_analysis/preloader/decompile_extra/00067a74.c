// File offset entry: 0x67a74

void FUN_02067974(uint param_1)

{
  long lVar1;
  long lVar2;
  undefined1 uVar3;
  int iVar4;
  ulong uVar5;
  undefined8 uVar6;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x9;
  undefined1 auVar7 [16];
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 uStack_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  undefined8 uStack_48;
  undefined4 uStack_40;
  undefined8 uStack_38;
  
  uStack_38 = *puRam0000000002103280;
  uStack_40 = 0;
  uStack_50 = 0;
  uStack_48 = 0;
  uStack_60 = 0;
  uStack_58 = 0;
  uStack_70 = 0;
  uStack_68 = 0;
  FUN_02091664(&uStack_70,0,0x34);
  uVar3 = iRam000000000011df74 == 0x454e454e;
  if (!(bool)uVar3) {
    uRam000000000011df78 = param_1 & 0xf | uRam000000000011df78 << 4;
  }
  FUN_02091664(&uStack_70,0,0x34);
  uVar5 = func_0x02066e44(&uStack_70);
  if ((uVar5 & 1) != 0) {
    if (uRam00000000020e80fc == 0xdeaddead) {
      uRam00000000020e80fc = (uint)uStack_58 & 0xf;
    }
    if (cRam000000000211be70 == '\x01') {
      uStack_58._0_4_ = (uint)uStack_58 & 0xfffffff0;
    }
    else {
      uStack_58._0_4_ = ((uint)uStack_58 & 0xfffffff0 | uRam00000000020e80fc & 0xf) << 4;
    }
    uStack_58 = CONCAT44(0x76657231,(uint)uStack_58 | param_1 & 0xf);
    uVar6 = func_0x02068620(lRam000000000211be48);
    uRam000000000211be35 = 1;
    iVar4 = FUN_0207e954(uVar6,extraout_x8 - extraout_x9,&uStack_70,0x34);
    uRam000000000211be35 = 0;
    uVar3 = iVar4 == 0x34;
    if ((bool)uVar3) {
      cRam000000000211be70 = '\x01';
    }
    else {
      auVar7 = FUN_020685c0(0x20b782c);
      uRam000000000211be35 = 1;
      FUN_0208ada0(auVar7._0_8_,auVar7._8_8_,0x20c0d1f);
      uRam000000000211be35 = 0;
    }
  }
  func_0x020685d4();
  if (!(bool)uVar3) {
    FUN_02091090();
    uRam000000000211be35 = 1;
    lRam000000000211be38 = FUN_020036c8(1);
    uRam000000000211be35 = 1;
    if (lRam000000000211be38 == 0) {
      auVar7 = FUN_020685c0(0x20c8217);
      FUN_0208ada0(auVar7._0_8_,auVar7._8_8_,1);
    }
    else {
      func_0x02068620();
      lRam000000000211be40 = FUN_0207e380();
      if (lRam000000000211be40 == 0) {
        uRam000000000211be35 = 1;
        FUN_0208ada0(0x20b3794,0x20b58bc);
      }
      else {
        auVar7 = FUN_020685c0(lRam000000000211be38);
        uVar5 = (ulong)*(uint *)(extraout_x8_00 + 4);
        lVar1 = *auVar7._0_8_;
        lVar2 = auVar7._0_8_[1];
        lRam000000000211be48 = lVar2 * uVar5;
        FUN_0208ada0(0x20c4e67,auVar7._8_8_,0x20babff,lVar1 * uVar5,(lVar2 + lVar1) * uVar5);
        uRam000000000011df70 = *(undefined4 *)(lRam000000000211be38 + 4);
      }
    }
    uRam000000000211be35 = 0;
    return;
  }
  return;
}



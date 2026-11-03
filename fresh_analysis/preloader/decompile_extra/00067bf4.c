// File offset entry: 0x67bf4

void FUN_02067af4(void)

{
  long lVar1;
  long lVar2;
  ulong uVar3;
  long extraout_x8;
  undefined1 auVar4 [16];
  
  uRam000000000211be35 = 1;
  lRam000000000211be38 = FUN_020036c8(1);
  uRam000000000211be35 = 1;
  if (lRam000000000211be38 == 0) {
    auVar4 = FUN_020685c0(0x20c8217);
    FUN_0208ada0(auVar4._0_8_,auVar4._8_8_,1);
  }
  else {
    func_0x02068620();
    lRam000000000211be40 = FUN_0207e380();
    if (lRam000000000211be40 == 0) {
      uRam000000000211be35 = 1;
      FUN_0208ada0(0x20b3794,0x20b58bc);
    }
    else {
      auVar4 = FUN_020685c0(lRam000000000211be38);
      uVar3 = (ulong)*(uint *)(extraout_x8 + 4);
      lVar1 = *auVar4._0_8_;
      lVar2 = auVar4._0_8_[1];
      lRam000000000211be48 = lVar2 * uVar3;
      FUN_0208ada0(0x20c4e67,auVar4._8_8_,0x20babff,lVar1 * uVar3,(lVar2 + lVar1) * uVar3);
      uRam000000000011df70 = *(undefined4 *)(lRam000000000211be38 + 4);
    }
  }
  uRam000000000211be35 = 0;
  return;
}



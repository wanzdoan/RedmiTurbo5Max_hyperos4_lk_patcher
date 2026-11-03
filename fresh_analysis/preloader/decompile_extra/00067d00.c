// File offset entry: 0x67d00

void FUN_02067c00(void)

{
  int iVar1;
  undefined1 in_ZR;
  ulong uVar2;
  undefined8 extraout_x1;
  ulong uVar3;
  undefined8 uStack_60;
  ulong uStack_58;
  undefined8 uStack_50;
  undefined8 uStack_48;
  undefined8 uStack_40;
  undefined8 uStack_38;
  undefined4 uStack_30;
  undefined8 uStack_28;
  
  uStack_28 = *puRam0000000002103280;
  uStack_30 = 0;
  uStack_40 = 0;
  uStack_38 = 0;
  uStack_50 = 0;
  uStack_48 = 0;
  uStack_60 = 0;
  uStack_58 = 0;
  FUN_02091664(&uStack_60,0,0x34);
  uVar2 = func_0x02066e44(&uStack_60);
  uVar3 = uStack_58 & 0xffffffff;
  if (((uVar2 & 1) != 0) && (in_ZR = (int)uStack_58 == 0x454e454e, (bool)in_ZR)) {
    func_0x0208ad54(1);
    uVar3 = uStack_58 & 0xffffffff;
  }
  uRam000000000211be35 = 1;
  FUN_0208ada0(0x20b8a6a,0x20b58bc,uVar3);
  uRam000000000211be35 = 0;
  func_0x020685d4();
  if ((bool)in_ZR) {
    return;
  }
  FUN_02091090();
  FUN_020685c0(0x20c2ce7,extraout_x1,iRam000000000011df00);
  uRam000000000211be35 = 1;
  FUN_0208ada0();
  iVar1 = iRam000000000011df00;
  uRam000000000211be35 = 0;
  if (iRam000000000011df00 != 0x24013186) {
    FUN_02091664(0x11df00,0,0x100);
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20c5f5e,0x20b58bc);
    iRam000000000011df00 = 0x24013186;
    uRam000000000011df80 = 0x76657231;
    uRam000000000011df0c = 0x5678ef90;
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20c401b,0x20b58bc,0x24013186);
  }
  uRam000000000211be35 = 0;
  uRam000000000211be58 = iVar1 == 0x24013186;
  return;
}



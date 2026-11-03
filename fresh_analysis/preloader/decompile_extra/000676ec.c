// File offset entry: 0x676ec

void FUN_020675ec(void)

{
  long lVar1;
  long lVar2;
  bool bVar3;
  undefined1 in_ZR;
  undefined1 uVar4;
  char cVar5;
  int iVar6;
  uint uVar7;
  ulong uVar8;
  undefined8 uVar9;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x9;
  undefined8 uVar10;
  undefined1 auVar11 [16];
  undefined8 uStack_100;
  undefined8 uStack_f8;
  undefined8 uStack_f0;
  undefined8 uStack_e8;
  undefined8 uStack_e0;
  undefined8 uStack_d8;
  undefined4 uStack_d0;
  undefined8 uStack_c8;
  undefined1 *puStack_c0;
  code *pcStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined1 auStack_90 [12];
  int iStack_84;
  undefined1 auStack_80 [64];
  
  bVar3 = true;
  FUN_020685c0(puRam0000000002103280,0x20b29ef);
  auVar11 = func_0x020685f4();
  uRam000000000211be35 = 1;
  FUN_0208ada0(auVar11._0_8_,auVar11._8_8_,uRam000000000011df10);
  uRam000000000211be35 = 0;
  FUN_02091664(auStack_80,0,0x34);
  FUN_02091664(auStack_90,0,0xc);
  uVar10 = 0x211b000;
  if ((lRam000000000211be38 == 0) || (lRam000000000211be40 == 0)) {
    FUN_020685c0(0x20c3ff0);
    uRam000000000211be35 = 1;
    FUN_0208ada0();
  }
  else {
    in_ZR = iRam000000000011df0c == 0x5678ef90;
    if ((bool)in_ZR) {
      if (lRam000000000011df14 == 0) {
        uVar9 = 0x20b785e;
LAB_02067768:
        uRam000000000211be35 = 1;
        FUN_0208ada0(uVar9,0x20b58bc);
LAB_02067790:
        bVar3 = false;
        uRam000000000211be35 = 0;
      }
      else {
        uRam000000000211be35 = 1;
        if (iRam000000000011df1c != 0x40000) {
          auVar11 = FUN_020685c0(0x20bfde8);
LAB_0206778c:
          FUN_0208ada0(auVar11._0_8_,auVar11._8_8_);
          goto LAB_02067790;
        }
        if ((((byte)uRam000000000011df10 >> 2 & 1) == 0) &&
           (uVar8 = func_0x0208b7e0(), (uVar8 & 1) == 0)) {
          uRam000000000211be35 = 1;
          FUN_0208ada0(0x20c1e43,0x20b58bc,uRam000000000011df10);
          goto LAB_02067790;
        }
        uRam000000000211be35 = 0;
        lRam000000000211be50 = lRam000000000011df14;
        iVar6 = FUN_02066d14();
        if (iVar6 == 0) {
          uVar9 = 0x20ca190;
          goto LAB_02067768;
        }
        if (uRam000000000011df04 < uRam000000000011df08) {
          auVar11._8_8_ = 0x20b58bc;
          auVar11._0_8_ = 0x20ca1a6;
          uRam000000000211be35 = 1;
          goto LAB_0206778c;
        }
        func_0x020672d0();
      }
      cVar5 = FUN_0209a3f4();
      if ((cVar5 == '\b') || (cVar5 = func_0x02099c1c(), cVar5 != '\0')) {
        if ((lRam000000000211be38 == 0) || (lRam000000000211be40 == 0)) {
          uVar9 = 0x20cc61a;
LAB_02067824:
          uRam000000000211be35 = 1;
          FUN_0208ada0(uVar9,0x20b58bc);
        }
        else {
          uVar10 = 1;
          uVar9 = func_0x02068620();
          iStack_84 = 0;
          uRam000000000211be35 = 1;
          iVar6 = FUN_0207e78c(uVar9,0,&iStack_84,4);
          if (iVar6 == 4) {
            uRam000000000211be35 = 1;
            if (iStack_84 != 0) {
              FUN_020685c0(0x20b4919);
              FUN_0208ada0();
              uRam000000000211be35 = 0;
              goto LAB_020678ac;
            }
            uVar9 = 0x20c71e2;
            goto LAB_02067824;
          }
          FUN_020685c0(0x20c4e3e);
          uRam000000000211be35 = 1;
          FUN_0208ada0();
        }
        uRam000000000211be35 = 1;
        FUN_0208ada0(0x20ce629,0x20b58bc);
        uRam000000000211be35 = 0;
        if (!bVar3) {
          func_0x020672d0();
        }
        if (iRam000000000011df74 == 0x454e454e) {
          func_0x02066b24(10);
          FUN_02067974(10);
        }
        bVar3 = true;
        func_0x02086088(0xa09,1,1,0);
      }
LAB_020678ac:
      in_ZR = 0;
      if ((uRam000000000011df04 == 0xe) && (in_ZR = 0, cRam000000000211be36 == '\x01')) {
        auVar11 = FUN_020685c0(0x20b9c07);
        FUN_0208ada0(auVar11._0_8_,auVar11._8_8_,0xe);
        uRam000000000211be35 = 0;
        if (!bVar3) {
          func_0x020672d0();
        }
        in_ZR = iRam000000000011df74 == 0x454e454e;
        if ((bool)in_ZR) {
          func_0x02066b24(5);
          FUN_02067974(5);
        }
        func_0x02086088(0xa09,1,1,0);
      }
      goto LAB_02067738;
    }
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20bba5f,0x20b58bc);
  }
  uRam000000000211be35 = 0;
LAB_02067738:
  func_0x020685d4();
  if ((bool)in_ZR) {
    return;
  }
  uVar7 = FUN_02091090();
  pcStack_b8 = FUN_02067974;
  uStack_a0 = 0x11df10;
  uStack_98 = 0x211b000;
  uStack_c8 = *puRam0000000002103280;
  uStack_d0 = 0;
  uStack_e0 = 0;
  uStack_d8 = 0;
  uStack_f0 = 0;
  uStack_e8 = 0;
  uStack_100 = 0;
  uStack_f8 = 0;
  puStack_c0 = &stack0xffffffffffffffc0;
  uStack_b0 = uVar10;
  FUN_02091664(&uStack_100,0,0x34);
  uVar4 = iRam000000000011df74 == 0x454e454e;
  if (!(bool)uVar4) {
    uRam000000000011df78 = uVar7 & 0xf | uRam000000000011df78 << 4;
  }
  FUN_02091664(&uStack_100,0,0x34);
  uVar8 = func_0x02066e44(&uStack_100);
  if ((uVar8 & 1) != 0) {
    if (uRam00000000020e80fc == 0xdeaddead) {
      uRam00000000020e80fc = (uint)uStack_e8 & 0xf;
    }
    if (cRam000000000211be70 == '\x01') {
      uStack_e8._0_4_ = (uint)uStack_e8 & 0xfffffff0;
    }
    else {
      uStack_e8._0_4_ = ((uint)uStack_e8 & 0xfffffff0 | uRam00000000020e80fc & 0xf) << 4;
    }
    uStack_e8 = CONCAT44(0x76657231,(uint)uStack_e8 | uVar7 & 0xf);
    uVar10 = func_0x02068620(lRam000000000211be48);
    uRam000000000211be35 = 1;
    iVar6 = FUN_0207e954(uVar10,extraout_x8 - extraout_x9,&uStack_100,0x34);
    uRam000000000211be35 = 0;
    uVar4 = iVar6 == 0x34;
    if ((bool)uVar4) {
      cRam000000000211be70 = '\x01';
    }
    else {
      auVar11 = FUN_020685c0(0x20b782c);
      uRam000000000211be35 = 1;
      FUN_0208ada0(auVar11._0_8_,auVar11._8_8_,0x20c0d1f);
      uRam000000000211be35 = 0;
    }
  }
  func_0x020685d4();
  if (!(bool)uVar4) {
    FUN_02091090();
    uRam000000000211be35 = 1;
    lRam000000000211be38 = FUN_020036c8(1);
    uRam000000000211be35 = 1;
    if (lRam000000000211be38 == 0) {
      auVar11 = FUN_020685c0(0x20c8217);
      FUN_0208ada0(auVar11._0_8_,auVar11._8_8_,1);
    }
    else {
      func_0x02068620();
      lRam000000000211be40 = FUN_0207e380();
      if (lRam000000000211be40 == 0) {
        uRam000000000211be35 = 1;
        FUN_0208ada0(0x20b3794,0x20b58bc);
      }
      else {
        auVar11 = FUN_020685c0(lRam000000000211be38);
        uVar8 = (ulong)*(uint *)(extraout_x8_00 + 4);
        lVar1 = *auVar11._0_8_;
        lVar2 = auVar11._0_8_[1];
        lRam000000000211be48 = lVar2 * uVar8;
        FUN_0208ada0(0x20c4e67,auVar11._8_8_,0x20babff,lVar1 * uVar8,(lVar2 + lVar1) * uVar8);
        uRam000000000011df70 = *(undefined4 *)(lRam000000000211be38 + 4);
      }
    }
    uRam000000000211be35 = 0;
    return;
  }
  return;
}



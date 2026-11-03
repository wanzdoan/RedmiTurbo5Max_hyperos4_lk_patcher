// File offset entry: 0x673d0

void FUN_020672d0(void)

{
  uint uVar1;
  long lVar2;
  long lVar3;
  char *pcVar4;
  bool bVar5;
  char *pcVar6;
  undefined1 in_ZR;
  undefined1 uVar7;
  char cVar8;
  int iVar9;
  uint uVar10;
  ulong uVar11;
  undefined8 uVar12;
  undefined8 extraout_x1;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x9;
  char *pcVar13;
  undefined8 unaff_x19;
  undefined8 unaff_x20;
  undefined8 unaff_x21;
  undefined8 uVar14;
  undefined8 unaff_x22;
  undefined8 unaff_x23;
  undefined1 auVar15 [16];
  undefined8 uStack_190;
  undefined8 uStack_188;
  undefined8 uStack_180;
  undefined8 uStack_178;
  undefined8 uStack_170;
  undefined8 uStack_168;
  undefined4 uStack_160;
  undefined8 uStack_158;
  undefined1 **ppuStack_150;
  code *pcStack_148;
  undefined8 uStack_140;
  undefined8 uStack_130;
  undefined8 uStack_128;
  undefined1 auStack_120 [12];
  int iStack_114;
  undefined1 auStack_110 [64];
  undefined1 *puStack_d0;
  code *pcStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined4 uStack_90;
  undefined4 uStack_8c;
  undefined4 uStack_88;
  undefined1 auStack_80 [4];
  undefined4 uStack_7c;
  
  FUN_020685f4(puRam0000000002103280,auStack_80,0,0x34);
  FUN_02091664();
  FUN_02091664(&uStack_90,0,0xc);
  uVar11 = FUN_02066e44(auStack_80);
  if ((uVar11 & 1) != 0) {
    unaff_x22 = 0x211b000;
    unaff_x20 = 0x11df34;
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20b3766,0x20b58bc,iRam000000000011df4c,uRam000000000011df58);
    uVar14 = uRam0000000002103328;
    unaff_x21 = 0x211b000;
    uRam000000000211be35 = 0;
    FUN_0200329c(uRam0000000002103328,lRam000000000211be50,lRam000000000211be50,0x40,5);
    FUN_0200329c(uVar14,CONCAT44(uRam000000000011df38,iRam000000000011df34),
                 CONCAT44(uRam000000000011df38,iRam000000000011df34),
                 (uRam000000000011df3c >> 0xc) + 0xfff & 0x1ff000,5);
    uVar10 = uRam000000000011df58;
    iVar9 = iRam000000000011df4c;
    if ((iRam000000000011df4c == 0) ||
       (uVar1 = iRam000000000011df4c + iRam000000000011df48, uVar1 >> 0x12 != 0)) {
LAB_02067400:
      uRam000000000011df58 = uVar10;
      if (iRam000000000011df4c != 0) goto LAB_0206740c;
      if (uRam000000000011df58 != 0) {
        iVar9 = 0;
        goto LAB_0206740c;
      }
    }
    else {
      if (uRam000000000011df58 == 0) {
        do {
          uVar10 = uRam000000000011df58;
          if (*(char *)(lRam000000000211be50 + (ulong)uVar1 + (ulong)uRam000000000011df58) == '\0')
          break;
          uRam000000000011df58 = *(int *)(lRam000000000211be38 + 4) + uRam000000000011df58;
          uVar10 = 0x40000 - uVar1;
        } while (uRam000000000011df58 < 0x40000 - uVar1);
        goto LAB_02067400;
      }
LAB_0206740c:
      unaff_x23 = 1;
      FUN_020685c0(0x20b58c9,extraout_x1,lRam000000000211be50,
                   iRam000000000011df48 + iVar9 + uRam000000000011df58,uStack_7c);
      uRam000000000211be35 = 1;
      FUN_0208ada0();
      uRam000000000211be35 = 0;
      uStack_8c = uStack_7c;
      uStack_88 = func_0x02067134(lRam000000000211be50,
                                  uRam000000000011df58 + iRam000000000011df48 + iRam000000000011df4c
                                 );
      uStack_90 = 1;
      uStack_7c = uStack_88;
      func_0x02066fb4(&uStack_90,auStack_80);
    }
    pcVar4 = (char *)CONCAT44(uRam000000000011df38,iRam000000000011df34);
    if ((pcVar4 != (char *)0x0) && (uRam000000000011df3c != 0)) {
      pcVar6 = pcVar4;
      do {
        pcVar13 = pcVar6;
        if (*pcVar13 == '\0') break;
        pcVar6 = pcVar13 + *(uint *)(lRam000000000211be38 + 4);
      } while (pcVar13 + *(uint *)(lRam000000000211be38 + 4) <= pcVar4 + uRam000000000011df3c);
      uRam000000000211be35 = 1;
      FUN_0208ada0(0x20c81dc,0x20b58bc,pcVar4,(long)pcVar13 - (long)pcVar4,uStack_7c);
      uRam000000000211be35 = 0;
      if (pcVar13 != (char *)CONCAT44(uRam000000000011df38,iRam000000000011df34)) {
        uStack_8c = uStack_7c;
        uStack_88 = func_0x02067134((char *)CONCAT44(uRam000000000011df38,iRam000000000011df34),
                                    (int)pcVar13 - iRam000000000011df34);
        uStack_90 = 4;
        uStack_7c = uStack_88;
        func_0x02066fb4(&uStack_90,auStack_80);
      }
    }
    if ((lRam000000000011df28 != 0) && (iRam000000000011df30 != 0)) {
      uRam000000000211be35 = 1;
      FUN_0208ada0(0x20c9285,0x20b58bc,lRam000000000011df28,iRam000000000011df30,uStack_7c);
      uRam000000000211be35 = 0;
      uStack_8c = uStack_7c;
      uStack_88 = func_0x02067134(lRam000000000011df28,iRam000000000011df30);
      uStack_90 = 3;
      uStack_7c = uStack_88;
      func_0x02066fb4(&uStack_90,auStack_80);
    }
    unaff_x19 = uRam0000000002103328;
    in_ZR = uRam000000000011df04 == 10;
    if (9 < uRam000000000011df04) {
      uRam000000000011df08 = uRam000000000011df04 * 5;
    }
    FUN_0200341c(uRam0000000002103328,lRam000000000211be50,0x40);
    FUN_0200341c(unaff_x19,CONCAT44(uRam000000000011df38,iRam000000000011df34),
                 (uRam000000000011df3c >> 0xc) + 0xfff & 0x1ff000);
  }
  FUN_020685d4();
  if ((bool)in_ZR) {
    return;
  }
  FUN_02091090();
  pcStack_c8 = FUN_020675ec;
  bVar5 = true;
  puStack_d0 = &stack0xffffffffffffffc0;
  uStack_c0 = unaff_x23;
  uStack_b0 = unaff_x22;
  uStack_a8 = unaff_x21;
  uStack_a0 = unaff_x20;
  uStack_98 = unaff_x19;
  FUN_020685c0(puRam0000000002103280,0x20b29ef);
  auVar15 = FUN_020685f4();
  uRam000000000211be35 = 1;
  FUN_0208ada0(auVar15._0_8_,auVar15._8_8_,uRam000000000011df10);
  uRam000000000211be35 = 0;
  FUN_02091664(auStack_110,0,0x34);
  FUN_02091664(auStack_120,0,0xc);
  uVar14 = 0x211b000;
  if ((lRam000000000211be38 == 0) || (lRam000000000211be40 == 0)) {
    FUN_020685c0(0x20c3ff0);
    uRam000000000211be35 = 1;
    FUN_0208ada0();
  }
  else {
    in_ZR = iRam000000000011df0c == 0x5678ef90;
    if ((bool)in_ZR) {
      if (lRam000000000011df14 == 0) {
        uVar12 = 0x20b785e;
LAB_02067768:
        uRam000000000211be35 = 1;
        FUN_0208ada0(uVar12,0x20b58bc);
LAB_02067790:
        bVar5 = false;
        uRam000000000211be35 = 0;
      }
      else {
        uRam000000000211be35 = 1;
        if (iRam000000000011df1c != 0x40000) {
          auVar15 = FUN_020685c0(0x20bfde8);
LAB_0206778c:
          FUN_0208ada0(auVar15._0_8_,auVar15._8_8_);
          goto LAB_02067790;
        }
        if ((((byte)uRam000000000011df10 >> 2 & 1) == 0) &&
           (uVar11 = FUN_0208b7e0(), (uVar11 & 1) == 0)) {
          uRam000000000211be35 = 1;
          FUN_0208ada0(0x20c1e43,0x20b58bc,uRam000000000011df10);
          goto LAB_02067790;
        }
        uRam000000000211be35 = 0;
        lRam000000000211be50 = lRam000000000011df14;
        iVar9 = FUN_02066d14();
        if (iVar9 == 0) {
          uVar12 = 0x20ca190;
          goto LAB_02067768;
        }
        if (uRam000000000011df04 < uRam000000000011df08) {
          auVar15._8_8_ = 0x20b58bc;
          auVar15._0_8_ = 0x20ca1a6;
          uRam000000000211be35 = 1;
          goto LAB_0206778c;
        }
        FUN_020672d0();
      }
      cVar8 = FUN_0209a3f4();
      if ((cVar8 == '\b') || (cVar8 = FUN_02099c1c(), cVar8 != '\0')) {
        if ((lRam000000000211be38 == 0) || (lRam000000000211be40 == 0)) {
          uVar12 = 0x20cc61a;
LAB_02067824:
          uRam000000000211be35 = 1;
          FUN_0208ada0(uVar12,0x20b58bc);
        }
        else {
          uVar14 = 1;
          uVar12 = FUN_02068620();
          iStack_114 = 0;
          uRam000000000211be35 = 1;
          iVar9 = FUN_0207e78c(uVar12,0,&iStack_114,4);
          if (iVar9 == 4) {
            uRam000000000211be35 = 1;
            if (iStack_114 != 0) {
              FUN_020685c0(0x20b4919);
              FUN_0208ada0();
              uRam000000000211be35 = 0;
              goto LAB_020678ac;
            }
            uVar12 = 0x20c71e2;
            goto LAB_02067824;
          }
          FUN_020685c0(0x20c4e3e);
          uRam000000000211be35 = 1;
          FUN_0208ada0();
        }
        uRam000000000211be35 = 1;
        FUN_0208ada0(0x20ce629,0x20b58bc);
        uRam000000000211be35 = 0;
        if (!bVar5) {
          FUN_020672d0();
        }
        if (iRam000000000011df74 == 0x454e454e) {
          func_0x02066b24(10);
          FUN_02067974(10);
        }
        bVar5 = true;
        func_0x02086088(0xa09,1,1,0);
      }
LAB_020678ac:
      in_ZR = 0;
      if ((uRam000000000011df04 == 0xe) && (in_ZR = 0, cRam000000000211be36 == '\x01')) {
        auVar15 = FUN_020685c0(0x20b9c07);
        FUN_0208ada0(auVar15._0_8_,auVar15._8_8_,0xe);
        uRam000000000211be35 = 0;
        if (!bVar5) {
          FUN_020672d0();
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
  FUN_020685d4();
  if ((bool)in_ZR) {
    return;
  }
  uVar10 = FUN_02091090();
  pcStack_148 = FUN_02067974;
  uStack_130 = 0x11df10;
  uStack_128 = 0x211b000;
  uStack_158 = *puRam0000000002103280;
  uStack_160 = 0;
  uStack_170 = 0;
  uStack_168 = 0;
  uStack_180 = 0;
  uStack_178 = 0;
  uStack_190 = 0;
  uStack_188 = 0;
  ppuStack_150 = &puStack_d0;
  uStack_140 = uVar14;
  FUN_02091664(&uStack_190,0,0x34);
  uVar7 = iRam000000000011df74 == 0x454e454e;
  if (!(bool)uVar7) {
    uRam000000000011df78 = uVar10 & 0xf | uRam000000000011df78 << 4;
  }
  FUN_02091664(&uStack_190,0,0x34);
  uVar11 = FUN_02066e44(&uStack_190);
  if ((uVar11 & 1) != 0) {
    if (uRam00000000020e80fc == 0xdeaddead) {
      uRam00000000020e80fc = (uint)uStack_178 & 0xf;
    }
    if (cRam000000000211be70 == '\x01') {
      uStack_178._0_4_ = (uint)uStack_178 & 0xfffffff0;
    }
    else {
      uStack_178._0_4_ = ((uint)uStack_178 & 0xfffffff0 | uRam00000000020e80fc & 0xf) << 4;
    }
    uStack_178 = CONCAT44(0x76657231,(uint)uStack_178 | uVar10 & 0xf);
    uVar14 = FUN_02068620(lRam000000000211be48);
    uRam000000000211be35 = 1;
    iVar9 = FUN_0207e954(uVar14,extraout_x8 - extraout_x9,&uStack_190,0x34);
    uRam000000000211be35 = 0;
    uVar7 = iVar9 == 0x34;
    if ((bool)uVar7) {
      cRam000000000211be70 = '\x01';
    }
    else {
      auVar15 = FUN_020685c0(0x20b782c);
      uRam000000000211be35 = 1;
      FUN_0208ada0(auVar15._0_8_,auVar15._8_8_,0x20c0d1f);
      uRam000000000211be35 = 0;
    }
  }
  FUN_020685d4();
  if (!(bool)uVar7) {
    FUN_02091090();
    uRam000000000211be35 = 1;
    lRam000000000211be38 = FUN_020036c8(1);
    uRam000000000211be35 = 1;
    if (lRam000000000211be38 == 0) {
      auVar15 = FUN_020685c0(0x20c8217);
      FUN_0208ada0(auVar15._0_8_,auVar15._8_8_,1);
    }
    else {
      FUN_02068620();
      lRam000000000211be40 = FUN_0207e380();
      if (lRam000000000211be40 == 0) {
        uRam000000000211be35 = 1;
        FUN_0208ada0(0x20b3794,0x20b58bc);
      }
      else {
        auVar15 = FUN_020685c0(lRam000000000211be38);
        uVar11 = (ulong)*(uint *)(extraout_x8_00 + 4);
        lVar2 = *auVar15._0_8_;
        lVar3 = auVar15._0_8_[1];
        lRam000000000211be48 = lVar3 * uVar11;
        FUN_0208ada0(0x20c4e67,auVar15._8_8_,0x20babff,lVar2 * uVar11,(lVar3 + lVar2) * uVar11);
        uRam000000000011df70 = *(undefined4 *)(lRam000000000211be38 + 4);
      }
    }
    uRam000000000211be35 = 0;
    return;
  }
  return;
}



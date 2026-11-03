// File offset entry: 0x7ed00

uint *****
thunk_FUN_0207ec28(undefined8 param_1,undefined8 param_2,uint *****param_3,uint *****param_4,
                  uint *****param_5)

{
  ulong uVar1;
  undefined1 auVar2 [16];
  undefined1 auVar3 [16];
  uint ****ppppuVar4;
  undefined1 in_ZR;
  undefined1 uVar5;
  uint uVar6;
  int iVar7;
  undefined4 uVar8;
  uint uVar9;
  long *plVar11;
  undefined8 uVar12;
  uint *****pppppuVar13;
  uint *****pppppuVar14;
  undefined8 *puVar15;
  ulong uVar16;
  long *plVar17;
  uint *****pppppuVar18;
  uint *****pppppuVar19;
  ulong uVar20;
  long lVar21;
  ulong uVar22;
  ulong unaff_x19;
  uint *****pppppuVar23;
  uint *****pppppuVar24;
  ulong uVar25;
  uint *****pppppuVar26;
  long lVar27;
  long lVar28;
  undefined1 auVar29 [16];
  int iStack_388;
  int iStack_384;
  undefined1 auStack_380 [60];
  int iStack_344;
  undefined8 uStack_188;
  undefined1 **ppuStack_180;
  code *pcStack_178;
  long *plStack_170;
  undefined8 uStack_168;
  uint ****ppppuStack_160;
  uint ****ppppuStack_158;
  ulong uStack_150;
  undefined8 uStack_148;
  uint ****ppppuStack_140;
  uint ****ppppuStack_138;
  uint ****ppppuStack_128;
  uint ****ppppuStack_120;
  uint ****ppppuStack_118;
  uint ***pppuStack_110;
  ulong uStack_108;
  uint ****ppppuStack_100;
  undefined8 uStack_f8;
  undefined1 *puStack_f0;
  code *pcStack_e8;
  uint uStack_8c;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  long lVar10;
  
  FUN_0207f4b0();
  if ((bool)in_ZR) {
    return (uint *****)(unaff_x19 & 0xffffffff);
  }
  auVar29 = FUN_02091090();
  lVar10 = auVar29._0_8_;
  uStack_68 = *puRam0000000002103280;
  pppppuVar23 = (uint *****)((ulong)param_4 & 0xffffffff);
  uStack_70 = 0;
  uStack_80 = 0;
  uStack_78 = 0;
  uStack_88 = 0x6573756665;
  lVar28 = *plRam0000000002103338;
  uStack_8c = 0;
  FUN_0208ada0(0x20b2c3f,0x20c744a,&uStack_88);
  FUN_0208c834(&uStack_88);
  uVar6 = FUN_0208cb18();
  pppppuVar26 = (uint *****)(ulong)uVar6;
  FUN_0208ada0(0x20c744f,0x20c744a);
  plVar11 = (long *)FUN_0207e380(&uStack_88);
  if (plVar11 == (long *)0x0) {
    uVar12 = 0x200002;
  }
  else {
    param_5 = (uint *****)(ulong)*(uint *)(plVar11 + 3);
    pppppuVar26 = (uint *****)0x200;
    lVar27 = *plVar11 * (ulong)*(uint *)(lVar10 + 4);
    pppppuVar24 = (uint *****)(lVar28 + 0x764a0);
    param_4 = pppppuVar24;
    uVar12 = FUN_02003ac0(lVar10,lVar27);
    if ((int)uVar12 == 0) {
      FUN_0207eb58(pppppuVar24,0,&uStack_8c);
      param_5 = (uint *****)(ulong)*(uint *)(plVar11 + 3);
      pppppuVar26 = pppppuVar23;
      param_4 = param_3;
      uVar12 = FUN_02003ac0(lVar10,lVar27 + auVar29._8_8_ + (ulong)uStack_8c);
      if (((int)uVar12 == 0) &&
         ((uVar6 == 0 ||
          ((uVar12 = FUN_020a4f4c(&uStack_88,lVar28 + 0x764a8), (int)uVar12 == 0 &&
           (uVar12 = FUN_020a53c4(param_3,pppppuVar23), (int)uVar12 == 0)))))) {
        uVar12 = 0;
      }
    }
  }
  pppppuVar23 = (uint *****)FUN_0207f4b0(uVar12);
  if ((bool)in_ZR) {
    return pppppuVar23;
  }
  auVar29 = FUN_02091090();
  plVar11 = plRam0000000002103338;
  plVar17 = auVar29._8_8_;
  lVar10 = auVar29._0_8_;
  pcStack_e8 = FUN_0207edec;
  uVar25 = (ulong)param_4 & 0xffffffff;
  uStack_f8 = *puRam0000000002103280;
  pppppuVar24 = (uint *****)*plRam0000000002103338;
  pppppuVar23 = pppppuVar26;
  puStack_f0 = &stack0xffffffffffffffa0;
  pppppuVar13 = (uint *****)FUN_0207e76c();
  uStack_108 = 0;
  ppppuStack_100 = (uint ****)0x0;
  if (pppppuVar13 == (uint *****)0x0) {
    uVar12 = 0x20cb7a8;
    pppppuVar13 = param_3;
    pppppuVar14 = param_5;
LAB_0207eef8:
    FUN_0208ada0(uVar12,0x20c744a);
LAB_0207ef04:
    pppppuVar24 = (uint *****)0xffffffff;
    pppppuVar19 = pppppuVar23;
LAB_0207ef08:
    FUN_0207f4b0();
    if ((bool)in_ZR) {
      return pppppuVar24;
    }
LAB_0207f2c8:
    FUN_02091090();
    pppppuVar23 = pppppuVar19;
LAB_0207f2cc:
    FUN_0208ada0(0x20b9fe8,0x20c744a);
    uVar12 = FUN_0207f48c();
    iVar7 = FUN_0208b254(uVar12,0x168);
  }
  else {
    pppppuVar14 = (uint *****)0x20c744a;
    pppppuVar19 = pppppuVar24 + 0xec94;
    lVar28 = *plVar17 * (ulong)*(uint *)(lVar10 + 4) + uVar25;
    ppppuStack_118 = (uint ****)param_5;
    FUN_0208ada0(0x20b2c3f,0x20c744a,pppppuVar13);
    FUN_0208c834(pppppuVar13);
    uVar6 = FUN_0208cb18();
    uVar25 = (ulong)uVar6;
    FUN_0208ada0(0x20cc90a,0x20c744a,uVar25);
    pppppuVar23 = (uint *****)0x200;
    param_4 = pppppuVar19;
    iVar7 = FUN_02003ac0(lVar10,lVar28,0x200,pppppuVar19,(int)plVar17[3]);
    if (iVar7 != 0) goto LAB_0207ef04;
    pppppuVar23 = &ppppuStack_100;
    param_4 = (uint *****)&pppuStack_110;
    iVar7 = FUN_0207ea98(pppppuVar19,&uStack_108,pppppuVar23,param_4,1);
    if (iVar7 != 0) {
      uVar12 = 0x20b8d69;
      goto LAB_0207eef8;
    }
    pppppuVar14 = (uint *****)FUN_02091c18(0);
    if (uVar6 != 0) {
      FUN_0208c034();
      iVar7 = FUN_020a4f4c(pppppuVar13,pppppuVar24 + 0xec95);
      if (iVar7 == 0) {
        iVar7 = FUN_020a7460();
        if (iVar7 != 0) {
          FUN_0208ada0(0x20cb7e7,0x20c744a);
          uVar12 = FUN_0207f48c();
          FUN_0208b254(uVar12,0x16e);
          goto LAB_0207ef88;
        }
        goto LAB_0207efb0;
      }
      goto LAB_0207f2cc;
    }
LAB_0207ef88:
    iVar7 = FUN_020a4f4c(pppppuVar13,pppppuVar24 + 0xec95);
    if (iVar7 != 0) {
      FUN_0208ada0(0x20d10c7,pppppuVar13,iVar7);
    }
LAB_0207efb0:
    uVar12 = FUN_02091c18((ulong)pppppuVar14 & 0xffffffff);
    param_4 = pppppuVar24 + 0xec95;
    ppppuStack_120 = (uint ****)param_4;
    FUN_0208ada0(0x20b1c3a,0x20c744a,pppppuVar13,param_4,uVar12);
    FUN_020915ac(*plVar11 + (ulong)*(uint *)(*plVar11 + 0x766a0) * 0x200 + 0x766a4,pppppuVar19,0x200
                );
    uVar20 = uStack_108;
    *(int *)(*plVar11 + 0x766a0) = *(int *)(*plVar11 + 0x766a0) + 1;
    if (uStack_108 == 0xffffffff) {
      uVar20 = (ulong)*(uint *)pppppuVar26;
      pppppuVar19 = (uint *****)ppppuStack_100;
    }
    else {
      pppppuVar24 = (uint *****)0x20ec1a8;
      ppppuStack_128 = ppppuStack_100;
      for (lVar27 = 0; lVar27 != 0x20; lVar27 = lVar27 + 0x10) {
        plVar11 = (long *)(lVar27 + 0x20ec1a8);
        puVar15 = (undefined8 *)FUN_0206c904(*(undefined8 *)(lVar27 + 0x20ec1f0),0);
        if (puVar15 != (undefined8 *)0x0) {
          *(int *)(lVar27 + 0x20ec1e8) = (int)*puVar15;
          *(int *)(lVar27 + 0x20ec1ec) = (int)puVar15[1];
        }
      }
      uVar16 = (long)ppppuStack_128 + uVar20;
      lVar27 = 0;
      do {
        lVar21 = lVar27;
        pppppuVar19 = (uint *****)ppppuStack_128;
        if (lVar21 + 0x10 == 0x70) goto LAB_0207f10c;
        uVar1 = (ulong)*(uint *)(lVar21 + 0x20ec1a8);
        uVar22 = (ulong)(*(int *)(lVar21 + 0x20ec1ac) + *(uint *)(lVar21 + 0x20ec1a8));
        if (uVar1 <= uVar20 && uVar20 < uVar22) {
LAB_0207f298:
          FUN_0208ada0(0x20bdd55,0x20c744a,*(undefined8 *)(lVar21 + 0x20ec1b0));
          pppppuVar19 = (uint *****)0x20c7ab3;
          FUN_0208b224(0x20cb7c2,0x91);
          pppppuVar14 = (uint *****)0x20;
          goto LAB_0207f2c8;
        }
        if (uVar1 < uVar16) {
          if (uVar16 <= uVar22) goto LAB_0207f298;
          if (uVar20 <= uVar1) break;
        }
        lVar27 = lVar21 + 0x10;
      } while (uVar22 <= uVar20 || uVar16 < uVar22);
      FUN_0208ada0(0x20bdd55,0x20c744a,*(undefined8 *)(lVar21 + 0x20ec1b0));
      FUN_0208b224(0x20cb7c2,0x97,0x20c7ab3);
      pppppuVar19 = (uint *****)ppppuStack_128;
    }
LAB_0207f10c:
    lVar28 = lVar28 + 0x200;
    uVar8 = FUN_02091c18(0);
    iVar7 = FUN_02003ac0(lVar10,lVar28,(ulong)pppppuVar19 & 0xffffffff,uVar20,(int)plVar17[3]);
    if (iVar7 == 0) {
      if (pppppuVar26 != (uint *****)0x0) {
        *(uint *)pppppuVar26 = (uint)uVar20;
      }
      if ((uint *****)ppppuStack_118 != (uint *****)0x0) {
        *(uint *)ppppuStack_118 = (uint)pppppuVar19;
      }
      pppppuVar26 = (uint *****)0x20c002e;
    }
    else {
      pppppuVar26 = (uint *****)0x20b1c61;
    }
    uVar9 = FUN_02091c18(uVar8);
    pppppuVar14 = (uint *****)0x20c744a;
    FUN_0208ada0(0x20b5bc5,0x20c744a,plVar17[4],lVar28,uVar20,pppppuVar26);
    in_ZR = uVar9 == 1;
    if (uVar9 < 2) {
      uVar9 = 1;
    }
    uVar16 = 0;
    if ((ulong)uVar9 != 0) {
      uVar16 = (ulong)pppppuVar19 / (ulong)uVar9;
    }
    pppppuVar23 = (uint *****)(uVar16 * 1000 >> 10);
    param_4 = pppppuVar19;
    FUN_0208ada0(0x20b7b30,0x20c744a,pppppuVar23,pppppuVar19);
    uVar16 = FUN_02091c18(0);
    auVar3._8_8_ = uVar16;
    auVar3._0_8_ = pppppuVar19;
    auVar29._8_8_ = uVar16;
    auVar29._0_8_ = pppppuVar19;
    if (uVar6 == 0) {
      iVar7 = FUN_020a7a00(uVar20,(ulong)pppppuVar19 & 0xffffffff);
      ppppuStack_138 = ppppuStack_118;
      pppppuVar18 = (uint *****)ppppuStack_118;
      if (iVar7 != 0) goto LAB_0207f308;
LAB_0207f220:
      uVar12 = FUN_02091c18(uVar16 & 0xffffffff);
      ppppuVar4 = ppppuStack_120;
      auVar29._8_8_ = ppppuStack_120;
      auVar2._8_8_ = ppppuStack_120;
      auVar2._0_8_ = pppppuVar19;
      param_4 = (uint *****)ppppuStack_120;
      FUN_0208ada0(0x20cd730,0x20c744a,pppppuVar13,ppppuStack_120,uVar12);
      if ((pppppuVar18 == (uint *****)0x0) ||
         (uVar6 = FUN_020a4d44(pppppuVar13,ppppuVar4), uVar6 == 0)) {
        FUN_0200a3bc(ppppuVar4,uVar20);
        FUN_0208c800();
        pppppuVar24 = (uint *****)0x0;
      }
      else {
        pppppuVar24 = (uint *****)(ulong)uVar6;
        FUN_0208ada0(0x20b5bfb,pppppuVar24);
        pppppuVar19 = pppppuVar18;
        auVar29 = auVar2;
      }
      goto LAB_0207ef08;
    }
    FUN_0208ada0(0x20bdd26,0x20c744a);
    iVar7 = FUN_020a53c4(uVar20,(ulong)pppppuVar19 & 0xffffffff);
    pppppuVar18 = (uint *****)ppppuStack_118;
    pppppuVar24 = (uint *****)ppppuStack_118;
    if (iVar7 == 0) {
      FUN_0208ada0(0x20cc927);
      goto LAB_0207f220;
    }
  }
  FUN_0208ada0(0x20bdd36,iVar7);
  uVar12 = FUN_0207f48c();
  iVar7 = FUN_0208b254(uVar12,0x1a5);
  ppppuStack_138 = (uint ****)pppppuVar24;
  auVar3 = auVar29;
LAB_0207f308:
  FUN_0208ada0(0x20bdd36,iVar7);
  uVar12 = FUN_0207f48c();
  auVar29 = FUN_0208b254(uVar12,0x1b3);
  plVar17 = auVar29._8_8_;
  lVar10 = auVar29._0_8_;
  pcStack_178 = FUN_0207f324;
  uStack_188 = *puRam0000000002103280;
  ppuStack_180 = &puStack_f0;
  plStack_170 = plVar11;
  uStack_168 = auVar3._0_8_;
  ppppuStack_160 = (uint ****)pppppuVar14;
  ppppuStack_158 = (uint ****)pppppuVar26;
  uStack_150 = uVar25;
  uStack_148 = auVar3._8_8_;
  ppppuStack_140 = (uint ****)pppppuVar13;
  FUN_02091664(&iStack_388,0,0x200);
  uVar25 = plVar17[1] * (ulong)*(uint *)(lVar10 + 4);
  FUN_02091664(&iStack_388,0,0x200);
  uVar6 = *(uint *)pppppuVar23;
  do {
    uVar20 = (ulong)uVar6;
    uVar5 = uVar25 - uVar20 == 0;
    if (uVar25 < uVar20 || (bool)uVar5) {
      FUN_0208ada0(0x20bdd42,0x20c744a,param_4);
LAB_0207f45c:
      uVar12 = 0xffffffff;
LAB_0207f460:
      pppppuVar26 = (uint *****)FUN_0207f4b0(uVar12);
      if ((bool)uVar5) {
        return pppppuVar26;
      }
      FUN_02091090();
      return (uint *****)0x20cb7c2;
    }
    iVar7 = FUN_02003ac0(lVar10,uVar20 + *plVar17 * (ulong)*(uint *)(lVar10 + 4),0x200,&iStack_388,
                         (int)plVar17[3]);
    if (iVar7 != 0) {
      uVar12 = 0x20cfcd4;
LAB_0207f450:
      FUN_0208ada0(uVar12,0x20c744a);
      goto LAB_0207f45c;
    }
    uVar5 = iStack_388 == 0x58881688;
    if (!(bool)uVar5) {
      uVar12 = 0x20bae65;
      goto LAB_0207f450;
    }
    uVar12 = FUN_0209156c(auStack_380,param_4,0x20);
    if ((int)uVar12 == 0) goto LAB_0207f460;
    uVar6 = *(uint *)pppppuVar23 + ((iStack_384 + iStack_344) - 1U & -iStack_344) + 0x200;
    *(uint *)pppppuVar23 = uVar6;
  } while( true );
}



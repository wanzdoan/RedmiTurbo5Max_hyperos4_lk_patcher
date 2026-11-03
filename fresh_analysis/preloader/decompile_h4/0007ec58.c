// File offset entry: 0x7ec58

/* WARNING: Possible PIC construction at 0x0207f2e0: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0207f2fc: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0207f318: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0207ef7c: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x0207f31c) */
/* WARNING: Removing unreachable block (ram,0x0207f3a4) */
/* WARNING: Removing unreachable block (ram,0x0207f420) */
/* WARNING: Removing unreachable block (ram,0x0207f3ac) */
/* WARNING: Removing unreachable block (ram,0x0207f43c) */
/* WARNING: Removing unreachable block (ram,0x0207f3d4) */
/* WARNING: Removing unreachable block (ram,0x0207f448) */
/* WARNING: Removing unreachable block (ram,0x0207f450) */
/* WARNING: Removing unreachable block (ram,0x0207f45c) */
/* WARNING: Removing unreachable block (ram,0x0207f3e0) */
/* WARNING: Removing unreachable block (ram,0x0207f460) */
/* WARNING: Removing unreachable block (ram,0x0207f488) */
/* WARNING: Removing unreachable block (ram,0x0207f468) */
/* WARNING: Removing unreachable block (ram,0x0207f3f4) */
/* WARNING: Removing unreachable block (ram,0x0207f300) */
/* WARNING: Removing unreachable block (ram,0x0207f2e4) */
/* WARNING: Removing unreachable block (ram,0x0207ef80) */

ulong FUN_0207eb58(int *param_1,uint param_2,int *param_3,int *param_4,undefined4 *param_5)

{
  long lVar1;
  ulong uVar2;
  ulong uVar3;
  ulong uVar4;
  undefined1 in_ZR;
  uint uVar5;
  int iVar6;
  int iVar7;
  undefined4 uVar8;
  long *plVar9;
  undefined8 uVar10;
  undefined8 *puVar11;
  long *plVar12;
  int *piVar13;
  long lVar14;
  long lVar15;
  ulong uVar16;
  ulong uVar17;
  uint *puVar18;
  long lVar19;
  uint *puVar20;
  long lVar21;
  long lVar22;
  undefined1 auVar23 [16];
  undefined1 auStack_170 [8];
  ulong uStack_168;
  ulong uStack_160;
  undefined8 uStack_158;
  undefined1 **ppuStack_150;
  undefined8 uStack_148;
  uint uStack_ec;
  undefined8 uStack_e8;
  undefined8 uStack_e0;
  undefined8 uStack_d8;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  undefined1 *puStack_c0;
  undefined8 uStack_b8;
  int *apiStack_60 [4];
  int *piStack_40;
  int *piStack_38;
  int *piStack_30;
  undefined8 uStack_28;
  
  uStack_28 = *puRam0000000002103280;
  apiStack_60[0] = param_1 + 0xd;
  apiStack_60[1] = param_1 + 0xe;
  apiStack_60[2] = param_1 + 0xf;
  apiStack_60[3] = param_1 + 0x10;
  piStack_40 = param_1 + 0x11;
  piStack_38 = param_1 + 0x12;
  piStack_30 = param_1 + 0x13;
  if (param_3 == (int *)0x0) {
    uVar17 = 0x100000;
  }
  else {
    in_ZR = *param_1 == 0x58881688;
    if (((bool)in_ZR) && (in_ZR = param_1[0xc] == 0x58891689, (bool)in_ZR)) {
      piVar13 = (int *)(ulong)param_2;
      in_ZR = param_2 == 7;
      if (param_2 < 7) {
        uVar17 = 0;
        *param_3 = *apiStack_60[(long)piVar13];
      }
      else {
        uVar17 = 0x101018;
        func_0x0208ada0(0x20b1c12,0x20c744a);
        param_3 = piVar13;
      }
    }
    else {
      uVar17 = 0x101018;
      func_0x0208ada0(0x20bce40,0x20c744a);
    }
  }
  func_0x0207f4b0();
  if ((bool)in_ZR) {
    return uVar17;
  }
  auVar23 = func_0x02091090();
  lVar19 = auVar23._0_8_;
  uStack_b8 = 0x207ec64;
  uStack_c8 = *puRam0000000002103280;
  puVar18 = (uint *)((ulong)param_4 & 0xffffffff);
  uStack_d0 = 0;
  uStack_e0 = 0;
  uStack_d8 = 0;
  uStack_e8 = 0x6573756665;
  lVar22 = *plRam0000000002103338;
  uStack_ec = 0;
  puStack_c0 = &stack0xffffffffffffffe0;
  func_0x0208ada0(0x20b2c3f,0x20c744a,&uStack_e8);
  FUN_0208c834(&uStack_e8);
  uVar5 = FUN_0208cb18();
  puVar20 = (uint *)(ulong)uVar5;
  func_0x0208ada0(0x20c744f,0x20c744a);
  plVar9 = (long *)FUN_0207e380(&uStack_e8);
  if (plVar9 == (long *)0x0) {
    uVar10 = 0x200002;
  }
  else {
    param_5 = (undefined4 *)(ulong)*(uint *)(plVar9 + 3);
    puVar20 = (uint *)0x200;
    lVar21 = *plVar9 * (ulong)*(uint *)(lVar19 + 4);
    piVar13 = (int *)(lVar22 + 0x764a0);
    param_4 = piVar13;
    uVar10 = func_0x02003ac0(lVar19,lVar21,0x200,piVar13);
    if ((int)uVar10 == 0) {
      FUN_0207eb58(piVar13,0,&uStack_ec);
      param_5 = (undefined4 *)(ulong)*(uint *)(plVar9 + 3);
      puVar20 = puVar18;
      param_4 = param_3;
      uVar10 = func_0x02003ac0(lVar19,lVar21 + auVar23._8_8_ + (ulong)uStack_ec,puVar18,param_3);
      if (((int)uVar10 == 0) &&
         ((uVar5 == 0 ||
          ((uVar10 = func_0x020a4f4c(&uStack_e8,lVar22 + 0x764a8), (int)uVar10 == 0 &&
           (uVar10 = func_0x020a53c4(param_3,puVar18), (int)uVar10 == 0)))))) {
        uVar10 = 0;
      }
    }
  }
  uVar17 = func_0x0207f4b0(uVar10);
  if ((bool)in_ZR) {
    return uVar17;
  }
  auVar23 = func_0x02091090();
  plVar9 = plRam0000000002103338;
  plVar12 = auVar23._8_8_;
  lVar22 = auVar23._0_8_;
  uStack_148 = 0x207edec;
  uStack_158 = *puRam0000000002103280;
  lVar19 = *plRam0000000002103338;
  ppuStack_150 = &puStack_c0;
  lVar21 = FUN_0207e76c();
  uStack_168 = 0;
  uStack_160 = 0;
  if (lVar21 == 0) {
    uVar10 = 0x20cb7a8;
LAB_0207eef8:
    func_0x0208ada0(uVar10,0x20c744a);
LAB_0207ef04:
    uVar17 = 0xffffffff;
  }
  else {
    lVar15 = lVar19 + 0x764a0;
    lVar1 = *plVar12 * (ulong)*(uint *)(lVar22 + 4) + ((ulong)param_4 & 0xffffffff);
    func_0x0208ada0(0x20b2c3f,0x20c744a,lVar21);
    FUN_0208c834(lVar21);
    iVar6 = FUN_0208cb18();
    func_0x0208ada0(0x20cc90a,0x20c744a,iVar6);
    iVar7 = func_0x02003ac0(lVar22,lVar1,0x200,lVar15,(int)plVar12[3]);
    if (iVar7 != 0) goto LAB_0207ef04;
    iVar7 = FUN_0207ea98(lVar15,&uStack_168,&uStack_160,auStack_170,1);
    if (iVar7 != 0) {
      uVar10 = 0x20b8d69;
      goto LAB_0207eef8;
    }
    uVar8 = func_0x02091c18(0);
    if (iVar6 == 0) {
      iVar7 = func_0x020a4f4c(lVar21,lVar19 + 0x764a8);
      if (iVar7 != 0) {
        func_0x0208ada0(0x20d10c7,lVar21,iVar7);
      }
    }
    else {
      func_0x0208c034();
      iVar7 = func_0x020a4f4c(lVar21,lVar19 + 0x764a8);
      if (iVar7 != 0) goto LAB_0207f2cc;
      iVar7 = func_0x020a7460();
      if (iVar7 != 0) {
        func_0x0208ada0(0x20cb7e7,0x20c744a);
        return 0x20cb7c2;
      }
    }
    uVar10 = func_0x02091c18(uVar8);
    lVar19 = lVar19 + 0x764a8;
    func_0x0208ada0(0x20b1c3a,0x20c744a,lVar21,lVar19,uVar10);
    lVar14 = *plVar9;
    func_0x020915ac(lVar14 + (ulong)*(uint *)(lVar14 + 0x766a0) * 0x200 + 0x766a4,lVar15,0x200);
    uVar4 = uStack_160;
    uVar17 = uStack_168;
    lVar15 = *plVar9;
    *(int *)(lVar15 + 0x766a0) = *(int *)(lVar15 + 0x766a0) + 1;
    if (uStack_168 == 0xffffffff) {
      uVar17 = (ulong)*puVar20;
    }
    else {
      for (lVar15 = 0; lVar15 != 0x20; lVar15 = lVar15 + 0x10) {
        puVar11 = (undefined8 *)func_0x0206c904(*(undefined8 *)(lVar15 + 0x20ec1f0),0);
        if (puVar11 != (undefined8 *)0x0) {
          *(int *)(lVar15 + 0x20ec1e8) = (int)*puVar11;
          *(int *)(lVar15 + 0x20ec1ec) = (int)puVar11[1];
        }
      }
      uVar3 = uVar4 + uVar17;
      lVar15 = 0;
      do {
        lVar14 = lVar15;
        if (lVar14 + 0x10 == 0x70) goto LAB_0207f10c;
        uVar2 = (ulong)*(uint *)(lVar14 + 0x20ec1a8);
        uVar16 = (ulong)(*(int *)(lVar14 + 0x20ec1ac) + *(uint *)(lVar14 + 0x20ec1a8));
        if (uVar2 <= uVar17 && uVar17 < uVar16) {
LAB_0207f298:
          func_0x0208ada0(0x20bdd55,0x20c744a,*(undefined8 *)(lVar14 + 0x20ec1b0));
          func_0x0208b224(0x20cb7c2,0x91,0x20c7ab3);
          goto LAB_0207f2c8;
        }
        if (uVar2 < uVar3) {
          if (uVar3 <= uVar16) goto LAB_0207f298;
          if (uVar17 <= uVar2) break;
        }
        lVar15 = lVar14 + 0x10;
      } while (uVar16 <= uVar17 || uVar3 < uVar16);
      func_0x0208ada0(0x20bdd55,0x20c744a,*(undefined8 *)(lVar14 + 0x20ec1b0));
      func_0x0208b224(0x20cb7c2,0x97,0x20c7ab3);
    }
LAB_0207f10c:
    lVar1 = lVar1 + 0x200;
    uVar8 = func_0x02091c18(0);
    iVar7 = func_0x02003ac0(lVar22,lVar1,uVar4 & 0xffffffff,uVar17,(int)plVar12[3]);
    if (iVar7 == 0) {
      if (puVar20 != (uint *)0x0) {
        *puVar20 = (uint)uVar17;
      }
      if (param_5 != (undefined4 *)0x0) {
        *param_5 = (int)uVar4;
      }
      uVar10 = 0x20c002e;
    }
    else {
      uVar10 = 0x20b1c61;
    }
    uVar5 = func_0x02091c18(uVar8);
    func_0x0208ada0(0x20b5bc5,0x20c744a,plVar12[4],lVar1,uVar17,uVar10);
    in_ZR = uVar5 == 1;
    if (uVar5 < 2) {
      uVar5 = 1;
    }
    uVar3 = 0;
    if ((ulong)uVar5 != 0) {
      uVar3 = uVar4 / uVar5;
    }
    func_0x0208ada0(0x20b7b30,0x20c744a,uVar3 * 1000 >> 10,uVar4);
    uVar8 = func_0x02091c18(0);
    if (iVar6 == 0) {
      iVar6 = func_0x020a7a00(uVar17,uVar4 & 0xffffffff);
      if (iVar6 != 0) {
        func_0x0208ada0(0x20bdd36,iVar6);
        return 0x20cb7c2;
      }
    }
    else {
      func_0x0208ada0(0x20bdd26,0x20c744a);
      iVar6 = func_0x020a53c4(uVar17,uVar4 & 0xffffffff);
      if (iVar6 != 0) {
        func_0x0208ada0(0x20bdd36,iVar6);
        return 0x20cb7c2;
      }
      func_0x0208ada0(0x20cc927);
    }
    uVar10 = func_0x02091c18(uVar8);
    func_0x0208ada0(0x20cd730,0x20c744a,lVar21,lVar19,uVar10);
    if ((param_5 == (undefined4 *)0x0) ||
       (uVar5 = func_0x020a4d44(lVar21,lVar19,param_5), uVar5 == 0)) {
      func_0x0200a3bc(lVar19,uVar17,uVar4);
      func_0x0208c800();
      uVar17 = 0;
    }
    else {
      uVar17 = (ulong)uVar5;
      func_0x0208ada0(0x20b5bfb,uVar17);
    }
  }
  func_0x0207f4b0();
  if ((bool)in_ZR) {
    return uVar17;
  }
LAB_0207f2c8:
  func_0x02091090();
LAB_0207f2cc:
  func_0x0208ada0(0x20b9fe8,0x20c744a);
  return 0x20cb7c2;
}



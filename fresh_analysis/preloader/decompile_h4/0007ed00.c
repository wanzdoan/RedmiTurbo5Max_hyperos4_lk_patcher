// File offset entry: 0x7ed00

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

ulong thunk_FUN_0207ec28(undefined8 param_1,undefined8 param_2,ulong param_3,ulong param_4,
                        undefined4 *param_5)

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
  ulong uVar11;
  undefined8 *puVar12;
  long *plVar13;
  long lVar14;
  long lVar15;
  ulong uVar16;
  ulong unaff_x19;
  uint *puVar17;
  long lVar18;
  uint *puVar19;
  long lVar20;
  long lVar21;
  undefined1 auVar22 [16];
  undefined1 auStack_110 [8];
  ulong uStack_108;
  ulong uStack_100;
  undefined8 uStack_f8;
  undefined1 *puStack_f0;
  undefined8 uStack_e8;
  uint uStack_8c;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  
  func_0x0207f4b0();
  if ((bool)in_ZR) {
    return unaff_x19 & 0xffffffff;
  }
  auVar22 = func_0x02091090();
  lVar18 = auVar22._0_8_;
  uStack_68 = *puRam0000000002103280;
  puVar17 = (uint *)(param_4 & 0xffffffff);
  uStack_70 = 0;
  uStack_80 = 0;
  uStack_78 = 0;
  uStack_88 = 0x6573756665;
  lVar21 = *plRam0000000002103338;
  uStack_8c = 0;
  func_0x0208ada0(0x20b2c3f,0x20c744a,&uStack_88);
  func_0x0208c834(&uStack_88);
  uVar5 = func_0x0208cb18();
  puVar19 = (uint *)(ulong)uVar5;
  func_0x0208ada0(0x20c744f,0x20c744a);
  plVar9 = (long *)func_0x0207e380(&uStack_88);
  if (plVar9 == (long *)0x0) {
    uVar10 = 0x200002;
  }
  else {
    param_5 = (undefined4 *)(ulong)*(uint *)(plVar9 + 3);
    puVar19 = (uint *)0x200;
    lVar20 = *plVar9 * (ulong)*(uint *)(lVar18 + 4);
    uVar11 = lVar21 + 0x764a0;
    param_4 = uVar11;
    uVar10 = func_0x02003ac0(lVar18,lVar20,0x200,uVar11);
    if ((int)uVar10 == 0) {
      func_0x0207eb58(uVar11,0,&uStack_8c);
      param_5 = (undefined4 *)(ulong)*(uint *)(plVar9 + 3);
      puVar19 = puVar17;
      param_4 = param_3;
      uVar10 = func_0x02003ac0(lVar18,lVar20 + auVar22._8_8_ + (ulong)uStack_8c,puVar17,param_3);
      if (((int)uVar10 == 0) &&
         ((uVar5 == 0 ||
          ((uVar10 = func_0x020a4f4c(&uStack_88,lVar21 + 0x764a8), (int)uVar10 == 0 &&
           (uVar10 = func_0x020a53c4(param_3,puVar17), (int)uVar10 == 0)))))) {
        uVar10 = 0;
      }
    }
  }
  uVar11 = func_0x0207f4b0(uVar10);
  if ((bool)in_ZR) {
    return uVar11;
  }
  auVar22 = func_0x02091090();
  plVar9 = plRam0000000002103338;
  plVar13 = auVar22._8_8_;
  lVar21 = auVar22._0_8_;
  uStack_e8 = 0x207edec;
  uStack_f8 = *puRam0000000002103280;
  lVar18 = *plRam0000000002103338;
  puStack_f0 = &stack0xffffffffffffffa0;
  lVar20 = func_0x0207e76c();
  uStack_108 = 0;
  uStack_100 = 0;
  if (lVar20 == 0) {
    uVar10 = 0x20cb7a8;
LAB_0207eef8:
    func_0x0208ada0(uVar10,0x20c744a);
LAB_0207ef04:
    uVar11 = 0xffffffff;
  }
  else {
    lVar15 = lVar18 + 0x764a0;
    lVar1 = *plVar13 * (ulong)*(uint *)(lVar21 + 4) + (param_4 & 0xffffffff);
    func_0x0208ada0(0x20b2c3f,0x20c744a,lVar20);
    func_0x0208c834(lVar20);
    iVar6 = func_0x0208cb18();
    func_0x0208ada0(0x20cc90a,0x20c744a,iVar6);
    iVar7 = func_0x02003ac0(lVar21,lVar1,0x200,lVar15,(int)plVar13[3]);
    if (iVar7 != 0) goto LAB_0207ef04;
    iVar7 = func_0x0207ea98(lVar15,&uStack_108,&uStack_100,auStack_110,1);
    if (iVar7 != 0) {
      uVar10 = 0x20b8d69;
      goto LAB_0207eef8;
    }
    uVar8 = func_0x02091c18(0);
    if (iVar6 == 0) {
      iVar7 = func_0x020a4f4c(lVar20,lVar18 + 0x764a8);
      if (iVar7 != 0) {
        func_0x0208ada0(0x20d10c7,lVar20,iVar7);
      }
    }
    else {
      func_0x0208c034();
      iVar7 = func_0x020a4f4c(lVar20,lVar18 + 0x764a8);
      if (iVar7 != 0) goto LAB_0207f2cc;
      iVar7 = func_0x020a7460();
      if (iVar7 != 0) {
        func_0x0208ada0(0x20cb7e7,0x20c744a);
        return 0x20cb7c2;
      }
    }
    uVar10 = func_0x02091c18(uVar8);
    lVar18 = lVar18 + 0x764a8;
    func_0x0208ada0(0x20b1c3a,0x20c744a,lVar20,lVar18,uVar10);
    lVar14 = *plVar9;
    func_0x020915ac(lVar14 + (ulong)*(uint *)(lVar14 + 0x766a0) * 0x200 + 0x766a4,lVar15,0x200);
    uVar4 = uStack_100;
    uVar11 = uStack_108;
    lVar15 = *plVar9;
    *(int *)(lVar15 + 0x766a0) = *(int *)(lVar15 + 0x766a0) + 1;
    if (uStack_108 == 0xffffffff) {
      uVar11 = (ulong)*puVar19;
    }
    else {
      for (lVar15 = 0; lVar15 != 0x20; lVar15 = lVar15 + 0x10) {
        puVar12 = (undefined8 *)func_0x0206c904(*(undefined8 *)(lVar15 + 0x20ec1f0),0);
        if (puVar12 != (undefined8 *)0x0) {
          *(int *)(lVar15 + 0x20ec1e8) = (int)*puVar12;
          *(int *)(lVar15 + 0x20ec1ec) = (int)puVar12[1];
        }
      }
      uVar3 = uVar4 + uVar11;
      lVar15 = 0;
      do {
        lVar14 = lVar15;
        if (lVar14 + 0x10 == 0x70) goto LAB_0207f10c;
        uVar2 = (ulong)*(uint *)(lVar14 + 0x20ec1a8);
        uVar16 = (ulong)(*(int *)(lVar14 + 0x20ec1ac) + *(uint *)(lVar14 + 0x20ec1a8));
        if (uVar2 <= uVar11 && uVar11 < uVar16) {
LAB_0207f298:
          func_0x0208ada0(0x20bdd55,0x20c744a,*(undefined8 *)(lVar14 + 0x20ec1b0));
          func_0x0208b224(0x20cb7c2,0x91,0x20c7ab3);
          goto LAB_0207f2c8;
        }
        if (uVar2 < uVar3) {
          if (uVar3 <= uVar16) goto LAB_0207f298;
          if (uVar11 <= uVar2) break;
        }
        lVar15 = lVar14 + 0x10;
      } while (uVar16 <= uVar11 || uVar3 < uVar16);
      func_0x0208ada0(0x20bdd55,0x20c744a,*(undefined8 *)(lVar14 + 0x20ec1b0));
      func_0x0208b224(0x20cb7c2,0x97,0x20c7ab3);
    }
LAB_0207f10c:
    lVar1 = lVar1 + 0x200;
    uVar8 = func_0x02091c18(0);
    iVar7 = func_0x02003ac0(lVar21,lVar1,uVar4 & 0xffffffff,uVar11,(int)plVar13[3]);
    if (iVar7 == 0) {
      if (puVar19 != (uint *)0x0) {
        *puVar19 = (uint)uVar11;
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
    func_0x0208ada0(0x20b5bc5,0x20c744a,plVar13[4],lVar1,uVar11,uVar10);
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
      iVar6 = func_0x020a7a00(uVar11,uVar4 & 0xffffffff);
      if (iVar6 != 0) {
        func_0x0208ada0(0x20bdd36,iVar6);
        return 0x20cb7c2;
      }
    }
    else {
      func_0x0208ada0(0x20bdd26,0x20c744a);
      iVar6 = func_0x020a53c4(uVar11,uVar4 & 0xffffffff);
      if (iVar6 != 0) {
        func_0x0208ada0(0x20bdd36,iVar6);
        return 0x20cb7c2;
      }
      func_0x0208ada0(0x20cc927);
    }
    uVar10 = func_0x02091c18(uVar8);
    func_0x0208ada0(0x20cd730,0x20c744a,lVar20,lVar18,uVar10);
    if ((param_5 == (undefined4 *)0x0) ||
       (uVar5 = func_0x020a4d44(lVar20,lVar18,param_5), uVar5 == 0)) {
      func_0x0200a3bc(lVar18,uVar11,uVar4);
      func_0x0208c800();
      uVar11 = 0;
    }
    else {
      uVar11 = (ulong)uVar5;
      func_0x0208ada0(0x20b5bfb,uVar11);
    }
  }
  func_0x0207f4b0();
  if ((bool)in_ZR) {
    return uVar11;
  }
LAB_0207f2c8:
  func_0x02091090();
LAB_0207f2cc:
  func_0x0208ada0(0x20b9fe8,0x20c744a);
  return 0x20cb7c2;
}



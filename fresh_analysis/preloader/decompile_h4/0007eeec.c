// File offset entry: 0x7eeec

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

int FUN_0207edec(long param_1,long *param_2,uint *param_3,ulong param_4,undefined4 *param_5)

{
  long lVar1;
  ulong uVar2;
  ulong uVar3;
  long *plVar4;
  ulong uVar5;
  undefined1 in_ZR;
  int iVar6;
  int iVar7;
  undefined4 uVar8;
  uint uVar9;
  long lVar10;
  undefined8 uVar11;
  undefined8 *puVar12;
  long lVar13;
  long lVar14;
  ulong uVar15;
  long lVar16;
  ulong uVar17;
  undefined1 auStack_80 [8];
  ulong uStack_78;
  ulong uStack_70;
  undefined8 uStack_68;
  
  plVar4 = plRam0000000002103338;
  uStack_68 = *puRam0000000002103280;
  lVar16 = *plRam0000000002103338;
  lVar10 = FUN_0207e76c();
  uStack_78 = 0;
  uStack_70 = 0;
  if (lVar10 == 0) {
    uVar11 = 0x20cb7a8;
LAB_0207eef8:
    func_0x0208ada0(uVar11,0x20c744a);
LAB_0207ef04:
    iVar6 = -1;
  }
  else {
    lVar14 = lVar16 + 0x764a0;
    lVar1 = *param_2 * (ulong)*(uint *)(param_1 + 4) + (param_4 & 0xffffffff);
    func_0x0208ada0(0x20b2c3f,0x20c744a,lVar10);
    FUN_0208c834(lVar10);
    iVar6 = FUN_0208cb18();
    func_0x0208ada0(0x20cc90a,0x20c744a,iVar6);
    iVar7 = func_0x02003ac0(param_1,lVar1,0x200,lVar14,(int)param_2[3]);
    if (iVar7 != 0) goto LAB_0207ef04;
    iVar7 = FUN_0207ea98(lVar14,&uStack_78,&uStack_70,auStack_80,1);
    if (iVar7 != 0) {
      uVar11 = 0x20b8d69;
      goto LAB_0207eef8;
    }
    uVar8 = func_0x02091c18(0);
    if (iVar6 == 0) {
      iVar7 = func_0x020a4f4c(lVar10,lVar16 + 0x764a8);
      if (iVar7 != 0) {
        func_0x0208ada0(0x20d10c7,lVar10,iVar7);
      }
    }
    else {
      func_0x0208c034();
      iVar7 = func_0x020a4f4c(lVar10,lVar16 + 0x764a8);
      if (iVar7 != 0) goto LAB_0207f2cc;
      iVar7 = func_0x020a7460();
      if (iVar7 != 0) {
        func_0x0208ada0(0x20cb7e7,0x20c744a);
        return 0x20cb7c2;
      }
    }
    uVar11 = func_0x02091c18(uVar8);
    lVar16 = lVar16 + 0x764a8;
    func_0x0208ada0(0x20b1c3a,0x20c744a,lVar10,lVar16,uVar11);
    lVar13 = *plVar4;
    func_0x020915ac(lVar13 + (ulong)*(uint *)(lVar13 + 0x766a0) * 0x200 + 0x766a4,lVar14,0x200);
    uVar5 = uStack_70;
    uVar17 = uStack_78;
    lVar14 = *plVar4;
    *(int *)(lVar14 + 0x766a0) = *(int *)(lVar14 + 0x766a0) + 1;
    if (uStack_78 == 0xffffffff) {
      uVar17 = (ulong)*param_3;
    }
    else {
      for (lVar14 = 0; lVar14 != 0x20; lVar14 = lVar14 + 0x10) {
        puVar12 = (undefined8 *)func_0x0206c904(*(undefined8 *)(lVar14 + 0x20ec1f0),0);
        if (puVar12 != (undefined8 *)0x0) {
          *(int *)(lVar14 + 0x20ec1e8) = (int)*puVar12;
          *(int *)(lVar14 + 0x20ec1ec) = (int)puVar12[1];
        }
      }
      uVar3 = uVar5 + uVar17;
      lVar14 = 0;
      do {
        lVar13 = lVar14;
        if (lVar13 + 0x10 == 0x70) goto LAB_0207f10c;
        uVar2 = (ulong)*(uint *)(lVar13 + 0x20ec1a8);
        uVar15 = (ulong)(*(int *)(lVar13 + 0x20ec1ac) + *(uint *)(lVar13 + 0x20ec1a8));
        if (uVar2 <= uVar17 && uVar17 < uVar15) {
LAB_0207f298:
          func_0x0208ada0(0x20bdd55,0x20c744a,*(undefined8 *)(lVar13 + 0x20ec1b0));
          func_0x0208b224(0x20cb7c2,0x91,0x20c7ab3);
          goto LAB_0207f2c8;
        }
        if (uVar2 < uVar3) {
          if (uVar3 <= uVar15) goto LAB_0207f298;
          if (uVar17 <= uVar2) break;
        }
        lVar14 = lVar13 + 0x10;
      } while (uVar15 <= uVar17 || uVar3 < uVar15);
      func_0x0208ada0(0x20bdd55,0x20c744a,*(undefined8 *)(lVar13 + 0x20ec1b0));
      func_0x0208b224(0x20cb7c2,0x97,0x20c7ab3);
    }
LAB_0207f10c:
    lVar1 = lVar1 + 0x200;
    uVar8 = func_0x02091c18(0);
    iVar7 = func_0x02003ac0(param_1,lVar1,uVar5 & 0xffffffff,uVar17,(int)param_2[3]);
    if (iVar7 == 0) {
      if (param_3 != (uint *)0x0) {
        *param_3 = (uint)uVar17;
      }
      if (param_5 != (undefined4 *)0x0) {
        *param_5 = (int)uVar5;
      }
      uVar11 = 0x20c002e;
    }
    else {
      uVar11 = 0x20b1c61;
    }
    uVar9 = func_0x02091c18(uVar8);
    func_0x0208ada0(0x20b5bc5,0x20c744a,param_2[4],lVar1,uVar17,uVar11);
    in_ZR = uVar9 == 1;
    if (uVar9 < 2) {
      uVar9 = 1;
    }
    uVar3 = 0;
    if ((ulong)uVar9 != 0) {
      uVar3 = uVar5 / uVar9;
    }
    func_0x0208ada0(0x20b7b30,0x20c744a,uVar3 * 1000 >> 10,uVar5);
    uVar8 = func_0x02091c18(0);
    if (iVar6 == 0) {
      iVar6 = func_0x020a7a00(uVar17,uVar5 & 0xffffffff);
      if (iVar6 != 0) {
        func_0x0208ada0(0x20bdd36,iVar6);
        return 0x20cb7c2;
      }
    }
    else {
      func_0x0208ada0(0x20bdd26,0x20c744a);
      iVar6 = func_0x020a53c4(uVar17,uVar5 & 0xffffffff);
      if (iVar6 != 0) {
        func_0x0208ada0(0x20bdd36,iVar6);
        return 0x20cb7c2;
      }
      func_0x0208ada0(0x20cc927);
    }
    uVar11 = func_0x02091c18(uVar8);
    func_0x0208ada0(0x20cd730,0x20c744a,lVar10,lVar16,uVar11);
    if ((param_5 == (undefined4 *)0x0) ||
       (iVar6 = func_0x020a4d44(lVar10,lVar16,param_5), iVar6 == 0)) {
      func_0x0200a3bc(lVar16,uVar17,uVar5);
      func_0x0208c800();
      iVar6 = 0;
    }
    else {
      func_0x0208ada0(0x20b5bfb,iVar6);
    }
  }
  func_0x0207f4b0();
  if ((bool)in_ZR) {
    return iVar6;
  }
LAB_0207f2c8:
  func_0x02091090();
LAB_0207f2cc:
  func_0x0208ada0(0x20b9fe8,0x20c744a);
  return 0x20cb7c2;
}



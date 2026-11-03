// File offset entry: 0x6b8f0

ulong FUN_0206b7f0(ulong param_1,ulong param_2,ulong param_3,ulong param_4,uint param_5,long param_6
                  )

{
  long *plVar1;
  undefined1 *puVar2;
  ulong uVar3;
  long lVar4;
  ulong uVar5;
  uint uVar6;
  undefined4 uVar7;
  bool bVar8;
  int iVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  undefined8 uVar13;
  long lVar14;
  ulong uVar15;
  long lVar16;
  ulong *puVar17;
  undefined1 auVar18 [16];
  ulong uStack_a0;
  uint uStack_74;
  
  plVar1 = plRam0000000002103338;
  uStack_a0 = param_2;
  if (param_6 == 0) {
    uVar13 = 0x20cd3cc;
  }
  else {
    if (param_1 != 0) {
      if ((((uint)param_2 | (uint)param_1) & 0xfff) == 0) {
        if ((param_4 & 0xfff) == 0) {
          if ((param_2 & param_2 - 1) == 0) {
            if ((((0x7e < *(uint *)(*plRam0000000002103338 + 0x7cae8)) || (1 < param_5)) ||
                (0x7e < *(uint *)(*plRam0000000002103338 + 0x7d6f8))) ||
               (uVar10 = FUN_02091524(param_6), 0x7e < uVar10)) {
              FUN_0208ada0(0x20c828b,0x20b0842);
              FUN_0208ada0(0x20c4ece);
              lVar16 = *plVar1;
              FUN_0208ada0(0x20ce668,*(undefined4 *)(lVar16 + 0x7cae8),
                           *(undefined4 *)(lVar16 + 0x7d6f8),param_5,param_6);
              goto LAB_0206bd10;
            }
            lVar16 = *plVar1;
            uStack_a0 = 0x1000;
            if (param_2 != 0) {
              uStack_a0 = param_2 & 0xfffffffffffff000;
            }
            uVar15 = 0;
            uVar6 = *(uint *)(lVar16 + 0x7cae8);
            puVar17 = (ulong *)(lVar16 + 0x7caf8);
            uStack_74 = 0xffffffff;
            for (uVar10 = 0; uVar6 != uVar10; uVar10 = uVar10 + 1) {
              uVar3 = puVar17[-1];
              uVar5 = *puVar17;
              uVar12 = uVar5 + uVar3;
              uVar11 = uVar12;
              if (param_3 <= uVar12) {
                uVar11 = param_3;
              }
              if (param_4 == 0) {
                bVar8 = param_1 <= uVar5;
              }
              else {
                bVar8 = ((param_4 >= uVar3 && param_4 <= uVar12) &&
                        (param_4 < uVar3 || uVar12 != param_4)) && param_1 <= uVar5;
              }
              if (((bVar8 && uVar3 < param_3) && uVar3 < uVar11) &&
                 (uVar3 != 0 || param_1 <= uVar11)) {
                uVar15 = uVar11 - param_1 & -uStack_a0;
                if (param_4 != 0) {
                  uVar15 = param_4;
                }
                uVar11 = FUN_0206bd54(uVar15);
                if (((uVar11 & 1) == 0) &&
                   ((uVar3 <= uVar15 && uVar15 + param_1 <= param_3) && uVar15 + param_1 <= uVar12))
                {
                  uStack_74 = (uint)uVar10;
                }
              }
              puVar17 = puVar17 + 3;
            }
            if (-1 < (int)uStack_74) {
              lVar14 = lVar16 + (ulong)uStack_74 * 0x18;
              lVar4 = *(long *)(lVar14 + 0x7caf0);
              uVar10 = *(long *)(lVar14 + 0x7caf8) + lVar4;
              uVar12 = uVar10;
              if (param_3 <= uVar10) {
                uVar12 = param_3;
              }
              uVar12 = uVar12 - param_1 & -uStack_a0;
              if (param_4 != 0) {
                uVar12 = uVar15;
              }
              iVar9 = FUN_0206be30();
              if (iVar9 != -1) {
                *(ulong *)(lVar16 + (long)iVar9 * 0x98 + 0x7d700) = uVar12;
                plVar1 = plRam0000000002103338;
                *(ulong *)(*plRam0000000002103338 + (long)iVar9 * 0x98 + 0x7d708) = param_1;
                uVar11 = (ulong)uStack_74;
                *(uint *)(*plVar1 + (long)iVar9 * 0x98 + 0x7d710) = param_5;
                lVar16 = *plVar1;
                uVar15 = FUN_02091524(param_6);
                if (uVar15 < 0x80) {
                  uVar13 = FUN_02091524(param_6);
                }
                else {
                  uVar13 = 0x7f;
                }
                FUN_020915ac(lVar16 + (long)iVar9 * 0x98 + 0x7d714,param_6,uVar13);
                uVar15 = FUN_02091524(param_6);
                plVar1 = plRam0000000002103338;
                lVar16 = *plRam0000000002103338 + (long)iVar9 * 0x98;
                puVar2 = (undefined1 *)(lVar16 + (uVar15 & 0xffffffff) + 0x7d714);
                if ((uVar15 & 0xffffff80) != 0) {
                  puVar2 = (undefined1 *)(lVar16 + 0x7d793);
                }
                *puVar2 = 0;
                lVar16 = *plVar1;
                *(int *)(lVar16 + 0x7d6f8) = *(int *)(lVar16 + 0x7d6f8) + 1;
                if (uVar12 - lVar4 == 0) {
                  *(ulong *)(lVar16 + uVar11 * 0x18 + 0x7caf0) = lVar4 + param_1;
                  lVar16 = *plVar1 + uVar11 * 0x18;
                  *(ulong *)(lVar16 + 0x7caf8) = *(long *)(lVar16 + 0x7caf8) - param_1;
                }
                else {
                  lVar14 = uVar10 - (uVar12 + param_1);
                  *(ulong *)(lVar16 + uVar11 * 0x18 + 0x7caf8) = uVar12 - lVar4;
                  plVar1 = plRam0000000002103338;
                  if (lVar14 != 0) {
                    lVar16 = *plRam0000000002103338 + 0x7caf0;
                    uVar7 = *(undefined4 *)(lVar16 + uVar11 * 0x18 + 0x10);
                    FUN_02091610(lVar16 + (ulong)(uStack_74 + 2) * 0x18,
                                 lVar16 + (ulong)(uStack_74 + 1) * 0x18,
                                 (ulong)(*(int *)(*plRam0000000002103338 + 0x7cae8) + ~uStack_74) *
                                 0x18);
                    lVar16 = *plVar1 + (ulong)(uStack_74 + 1) * 0x18;
                    *(ulong *)(lVar16 + 0x7caf0) = uVar12 + param_1;
                    *(long *)(lVar16 + 0x7caf8) = lVar14;
                    *(undefined4 *)(lVar16 + 0x7cb00) = uVar7;
                    *(undefined4 *)(lVar16 + 0x7cb04) = 0;
                    lVar16 = *plVar1;
                    *(int *)(lVar16 + 0x7cae8) = *(int *)(lVar16 + 0x7cae8) + 1;
                  }
                }
                FUN_0208ada0(0x20b8aac,0x20b0842,uVar12,param_1,0,param_3,param_5,param_6);
                FUN_0208ada0(0x20beb6e,uStack_a0);
                return uVar12;
              }
              FUN_0208ada0(0x20bac54,0x20b0842,*(undefined4 *)(lVar16 + 0x7d6f8));
              goto LAB_0206bd10;
            }
            uVar13 = 0x20b381a;
            goto LAB_0206bcec;
          }
          uVar13 = 0x20c92c1;
        }
        else {
          uVar13 = 0x20cd3ef;
        }
      }
      else {
        uVar13 = 0x20b37e6;
      }
      FUN_0208ada0(uVar13,param_6);
      goto LAB_0206bd10;
    }
    uVar13 = 0x20cb4e5;
  }
LAB_0206bcec:
  FUN_0208ada0(uVar13);
LAB_0206bd10:
  FUN_0208ada0(0x20c828b,0x20b0842);
  FUN_0208ada0(0x20cb4ff,param_1,param_5,param_6,0,param_3,uStack_a0,param_4);
  FUN_0206be74();
  uVar13 = FUN_0206cad4();
  auVar18 = FUN_0208b224(uVar13,0x211);
  uVar12 = auVar18._0_8_;
  uVar15 = (ulong)*(uint *)(*plRam0000000002103338 + 0x82300);
  uVar10 = auVar18._8_8_ + uVar12;
  plVar1 = (long *)(*plRam0000000002103338 + 0x82310);
  while( true ) {
    if (uVar15 == 0) {
      return 0;
    }
    if (((ulong)plVar1[-1] <= uVar12) &&
       (uVar11 = *plVar1 + plVar1[-1], uVar12 < uVar11 && uVar10 <= uVar11)) break;
    uVar15 = uVar15 - 1;
    plVar1 = plVar1 + 2;
  }
  if (auVar18._8_8_ == 0) {
    return 0;
  }
  if (uVar10 < 0x100000001) {
    return 0;
  }
  if ((((uVar12 >> 0x20 != 0) && ((uVar12 >> 0x21 != 0 || (uVar10 < 0x200000001)))) &&
      ((uVar12 >> 0x22 != 0 || (uVar10 < 0x400000001)))) &&
     ((uVar12 >> 0x23 != 0 || (uVar10 < 0x800000001)))) {
    bVar8 = uVar12 >> 0x24 != 0;
    return (ulong)((!bVar8 && 0xfffffffff < uVar10) && (bVar8 || uVar10 != 0x1000000000));
  }
  return 1;
}



// ffff000090772d90  size=892  FUN_ffff000090772d90

void FUN_ffff000090772d90(long *param_1)

{
  ushort uVar1;
  ushort uVar2;
  uint uVar3;
  long lVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  byte bVar8;
  short sVar9;
  int iVar10;
  ulong uVar11;
  uint uVar12;
  ulong uVar13;
  ulong uVar14;
  ushort *puVar15;
  long lVar16;
  ulong uVar17;
  ulong uVar18;
  ushort *puVar19;
  uint uVar20;
  long *plVar21;
  
  uVar5 = *(uint *)(param_1 + 10);
  uVar12 = *(uint *)((long)param_1 + 0xb4);
  do {
    uVar6 = *(uint *)((long)param_1 + 0xac);
    uVar20 = (int)param_1[0xd] - (uVar12 + uVar6);
    if ((uVar5 - 0x106) + (int)param_1[10] <= uVar6) {
      FUN_ffff00009077eb20(param_1[0xc],param_1[0xc] + (ulong)uVar5,uVar5 - uVar20);
      uVar6 = *(int *)((long)param_1 + 0xac) - uVar5;
      *(uint *)((long)param_1 + 0xac) = uVar6;
      *(uint *)(param_1 + 0x16) = (int)param_1[0x16] - uVar5;
      param_1[0x13] = param_1[0x13] - (ulong)uVar5;
      if (uVar6 < *(uint *)((long)param_1 + 0x172c)) {
        *(uint *)((long)param_1 + 0x172c) = uVar6;
      }
      uVar12 = *(uint *)((long)param_1 + 0x84);
      uVar14 = (ulong)uVar12;
      uVar7 = *(uint *)(param_1 + 10);
      uVar13 = (ulong)uVar7;
      puVar15 = (ushort *)(param_1[0xf] + uVar14 * 2);
      sVar9 = (short)uVar7;
      if (uVar12 - 1 == 0) {
LAB_ffff000090772e84:
        do {
          puVar15 = puVar15 + -1;
          uVar1 = 0;
          if (uVar7 <= *puVar15) {
            uVar1 = *puVar15 - sVar9;
          }
          uVar12 = (int)uVar14 - 1;
          uVar14 = (ulong)uVar12;
          *puVar15 = uVar1;
        } while (uVar12 != 0);
      }
      else {
        uVar11 = (ulong)(uVar12 - 1) + 1;
        uVar17 = uVar11 & 0x1fffffffe;
        uVar14 = (ulong)(uVar12 - (int)uVar17);
        puVar19 = puVar15 + -1;
        uVar18 = uVar17;
        do {
          uVar1 = 0;
          if (uVar7 <= *puVar19) {
            uVar1 = *puVar19 - sVar9;
          }
          uVar2 = 0;
          if (uVar7 <= puVar19[-1]) {
            uVar2 = puVar19[-1] - sVar9;
          }
          uVar18 = uVar18 - 2;
          *puVar19 = uVar1;
          puVar19[-1] = uVar2;
          puVar19 = puVar19 + -2;
        } while (uVar18 != 0);
        puVar15 = puVar15 + -uVar17;
        if (uVar11 != uVar17) goto LAB_ffff000090772e84;
      }
      puVar15 = (ushort *)(param_1[0xe] + uVar13 * 2);
      if (uVar7 - 1 == 0) {
LAB_ffff000090772f08:
        do {
          puVar15 = puVar15 + -1;
          uVar1 = 0;
          if (uVar7 <= *puVar15) {
            uVar1 = *puVar15 - sVar9;
          }
          uVar12 = (int)uVar13 - 1;
          uVar13 = (ulong)uVar12;
          *puVar15 = uVar1;
        } while (uVar12 != 0);
      }
      else {
        uVar14 = (ulong)(uVar7 - 1) + 1;
        uVar18 = uVar14 & 0x1fffffffe;
        uVar13 = (ulong)(uVar7 - (int)uVar18);
        puVar19 = puVar15 + -1;
        uVar11 = uVar18;
        do {
          uVar1 = 0;
          if (uVar7 <= *puVar19) {
            uVar1 = *puVar19 - sVar9;
          }
          uVar2 = 0;
          if (uVar7 <= puVar19[-1]) {
            uVar2 = puVar19[-1] - sVar9;
          }
          uVar11 = uVar11 - 2;
          *puVar19 = uVar1;
          puVar19[-1] = uVar2;
          puVar19 = puVar19 + -2;
        } while (uVar11 != 0);
        puVar15 = puVar15 + -uVar18;
        if (uVar14 != uVar18) goto LAB_ffff000090772f08;
      }
      uVar20 = uVar5 + uVar20;
    }
    plVar21 = (long *)*param_1;
    uVar7 = *(uint *)(plVar21 + 1);
    if (uVar7 == 0) {
LAB_ffff000090773070:
      uVar11 = param_1[0x2e7];
      uVar13 = param_1[0xd];
      uVar14 = uVar13 - uVar11;
      if (uVar11 <= uVar13 && uVar14 != 0) {
        uVar18 = (ulong)*(uint *)((long)param_1 + 0xb4) + (ulong)*(uint *)((long)param_1 + 0xac);
        if (uVar11 < uVar18) {
          uVar13 = uVar13 - uVar18;
          if (0x101 < uVar13) {
            uVar13 = 0x102;
          }
          FUN_ffff00009077ee50(param_1[0xc] + uVar18,0,uVar13);
          lVar16 = uVar13 + uVar18;
        }
        else {
          uVar13 = (uVar18 + 0x102) - uVar11;
          if (uVar18 + 0x102 < uVar11 || uVar13 == 0) {
            return;
          }
          if (uVar14 <= uVar13) {
            uVar13 = uVar14;
          }
          FUN_ffff00009077ee50(param_1[0xc] + uVar11,0,uVar13 & 0xffffffff);
          lVar16 = param_1[0x2e7] + uVar13;
        }
        param_1[0x2e7] = lVar16;
      }
      return;
    }
    uVar12 = *(uint *)((long)param_1 + 0xb4);
    uVar3 = uVar7;
    if (uVar20 <= uVar7) {
      uVar3 = uVar20;
    }
    uVar13 = (ulong)uVar3;
    if (uVar3 != 0) {
      lVar16 = param_1[0xc];
      *(uint *)(plVar21 + 1) = uVar7 - uVar3;
      lVar16 = lVar16 + (ulong)uVar6 + (ulong)uVar12;
      FUN_ffff00009077eb20(lVar16,*plVar21,uVar13);
      if (*(int *)(plVar21[7] + 0x30) == 2) {
        lVar16 = FUN_ffff00009076ca8c(plVar21[0xc],lVar16,uVar13);
LAB_ffff000090772fa0:
        plVar21[0xc] = lVar16;
      }
      else if (*(int *)(plVar21[7] + 0x30) == 1) {
        lVar16 = FUN_ffff00009076c2d0(plVar21[0xc],lVar16,uVar13);
        goto LAB_ffff000090772fa0;
      }
      uVar12 = *(uint *)((long)param_1 + 0xb4);
      *plVar21 = *plVar21 + uVar13;
      plVar21[2] = plVar21[2] + uVar13;
    }
    iVar10 = *(int *)((long)param_1 + 0x172c);
    uVar12 = uVar12 + uVar3;
    *(uint *)((long)param_1 + 0xb4) = uVar12;
    if (2 < iVar10 + uVar12) {
      lVar16 = param_1[0xc];
      uVar20 = *(int *)((long)param_1 + 0xac) - iVar10;
      bVar8 = *(byte *)(lVar16 + (ulong)uVar20);
      *(uint *)(param_1 + 0x10) = (uint)bVar8;
      uVar7 = *(uint *)((long)param_1 + 0x8c);
      uVar3 = *(uint *)(param_1 + 0x12);
      uVar6 = ((uint)bVar8 << (ulong)(uVar3 & 0x1f) ^ (uint)*(byte *)(lVar16 + (ulong)(uVar20 + 1)))
              & uVar7;
      uVar13 = (ulong)uVar6;
      *(uint *)(param_1 + 0x10) = uVar6;
      do {
        if (iVar10 == 0) break;
        lVar4 = param_1[0xf];
        iVar10 = iVar10 + -1;
        bVar8 = *(byte *)(lVar16 + (ulong)(uVar20 + 2));
        *(int *)((long)param_1 + 0x172c) = iVar10;
        uVar6 = ((int)uVar13 << (ulong)(uVar3 & 0x1f) ^ (uint)bVar8) & uVar7;
        uVar13 = (ulong)uVar6;
        *(uint *)(param_1 + 0x10) = uVar6;
        *(undefined2 *)(param_1[0xe] + (ulong)(*(uint *)(param_1 + 0xb) & uVar20) * 2) =
             *(undefined2 *)(lVar4 + uVar13 * 2);
        *(short *)(lVar4 + uVar13 * 2) = (short)uVar20;
        uVar20 = uVar20 + 1;
      } while (2 < uVar12 + iVar10);
    }
    if ((0x105 < uVar12) || (*(int *)(*param_1 + 8) == 0)) goto LAB_ffff000090773070;
  } while( true );
}



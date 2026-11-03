// ffff000090751218  size=1096  FUN_ffff000090751218

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090751218(int param_1,long param_2)

{
  int iVar1;
  int *piVar2;
  bool bVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  undefined8 extraout_x1;
  undefined8 extraout_x1_00;
  undefined8 extraout_x1_01;
  undefined8 extraout_x1_02;
  undefined8 extraout_x1_03;
  undefined8 extraout_x1_04;
  undefined8 extraout_x1_05;
  undefined8 extraout_x1_06;
  undefined8 extraout_x1_07;
  undefined8 extraout_x1_08;
  undefined8 extraout_x1_09;
  undefined8 uVar7;
  undefined8 extraout_x1_10;
  uint uVar8;
  int *piVar9;
  long *plVar10;
  long lVar11;
  long lVar12;
  uint *puVar13;
  undefined1 *puVar14;
  long lVar15;
  ulong uVar16;
  long lVar17;
  ulong uVar18;
  
  if (*(int *)(param_2 + 0x308) == 2) {
    if (param_1 == 0x2b) {
      param_1 = 0x2d;
    }
    else if (param_1 == 0x2c) {
      return 0;
    }
  }
  uVar8 = 0;
  if (param_1 != 0x2b) {
    uVar8 = (uint)(param_1 != 0x2d);
  }
  uVar18 = (ulong)uVar8;
  lVar17 = 1;
  if (param_1 != 0x2b) {
    lVar17 = 2;
  }
  lVar17 = lVar17 - uVar18;
  piVar2 = (int *)(param_2 + 0x2e0);
  puVar14 = &DAT_ffff0000908781d8 + uVar18 * 0xbd8;
  lVar15 = lVar17;
  do {
    FUN_ffff00009077e858(puVar14,piVar2,0x5b0);
    puVar14 = puVar14 + 0xbd8;
    lVar15 = lVar15 + -1;
  } while (lVar15 != 0);
  if (*(int *)(param_2 + 0xc) != 0) {
    uVar8 = 0;
    if (param_1 != 0x2b) {
      uVar8 = (uint)(param_1 != 0x2d);
    }
    uVar16 = (ulong)uVar8;
    lVar15 = 1;
    if (param_1 != 0x2b) {
      lVar15 = 2;
    }
    lVar15 = lVar15 - uVar16;
    piVar9 = (int *)(&DAT_ffff000090878738 + uVar16 * 0xbd8);
    uVar7 = extraout_x1;
    lVar11 = lVar15;
    do {
      if (*piVar9 == 0) {
        FUN_ffff00009074e1bc(param_1,uVar7,piVar2);
        uVar7 = extraout_x1_00;
      }
      else {
        FUN_ffff00009074de5c(param_1,uVar7,piVar2);
        uVar7 = extraout_x1_01;
      }
      piVar9 = piVar9 + 0x2f6;
      lVar11 = lVar11 + -1;
    } while (lVar11 != 0);
    FUN_ffff00009074dc98();
    iVar1 = _DAT_ffff00009087936c + 1;
    bVar3 = _DAT_ffff00009087936c != 0;
    uVar7 = extraout_x1_02;
    _DAT_ffff00009087936c = iVar1;
    if (bVar3) {
      FUN_ffff00009074c4e0(param_1);
      uVar7 = extraout_x1_03;
    }
    FUN_ffff00009074db78(param_1,uVar7,piVar2);
    FUN_ffff00009074d458
              (param_1,extraout_x1_04,piVar2,
               *(int *)(param_2 + 0x928) << (ulong)(*(int *)(param_2 + 0x308) == 2),
               *(undefined4 *)(param_2 + 0x92c));
    FUN_ffff00009074d6fc(param_1,extraout_x1_05,piVar2);
    piVar9 = (int *)(&DAT_ffff000090878738 + uVar16 * 0xbd8);
    uVar7 = extraout_x1_06;
    lVar11 = lVar15;
    do {
      if (*piVar9 == 0) {
        FUN_ffff00009074e950(param_1,uVar7,piVar2);
        uVar7 = extraout_x1_07;
      }
      else {
        FUN_ffff00009074e70c(param_1,uVar7,piVar2);
        uVar7 = extraout_x1_08;
      }
      piVar9 = piVar9 + 0x2f6;
      lVar11 = lVar11 + -1;
    } while (lVar11 != 0);
    if ((*piVar2 != 0) || ((*(int *)(param_2 + 0x784) == 1 && (*(int *)(param_2 + 0x2e4) != 0)))) {
      piVar9 = (int *)(&DAT_ffff000090878738 + uVar16 * 0xbd8);
      lVar11 = lVar15;
      do {
        if (*piVar9 == 0) {
          FUN_ffff00009074cd40(param_1,uVar7,piVar2);
          uVar7 = extraout_x1_09;
        }
        else {
          FUN_ffff00009074d04c(param_1,uVar7,piVar2);
          uVar7 = extraout_x1_10;
        }
        piVar9 = piVar9 + 0x2f6;
        lVar11 = lVar11 + -1;
      } while (lVar11 != 0);
      if ((param_1 == 0x2d) || (param_1 == 0x2b)) {
        _DAT_ffff000032490110 = _DAT_ffff000032490110 | 0x25;
      }
      if ((*piVar2 != 0) && (uVar8 = *(uint *)(param_2 + 0x828), uVar8 != 0)) {
        uVar4 = *(uint *)(param_2 + 0x82c);
        uVar5 = *(uint *)(param_2 + 0x830);
        uVar6 = *(uint *)(param_2 + 0x834);
        plVar10 = (long *)(s_0123456789abcdef_ffff000090804a48 + uVar16 * 8 + 0x10);
        lVar11 = lVar15;
        do {
          lVar12 = *plVar10;
          lVar11 = lVar11 + -1;
          *(uint *)(lVar12 + 0x1a0) = *(uint *)(lVar12 + 0x1a0) & 0xfffffffc | uVar4 & 3;
          *(uint *)(lVar12 + 0x1a0) = *(uint *)(lVar12 + 0x1a0) & 0xfffffff3 | (uVar5 & 3) << 2;
          *(uint *)(lVar12 + 0x1a0) = *(uint *)(lVar12 + 0x1a0) & 0xffffffef | (uVar8 & 1) << 4;
          *(uint *)(lVar12 + 0x1a0) = *(uint *)(lVar12 + 0x1a0) | 0x20;
          *(uint *)(lVar12 + 0x1a0) = *(uint *)(lVar12 + 0x1a0) & 0xffffffbf;
          *(uint *)(lVar12 + 0x1a0) = *(uint *)(lVar12 + 0x1a0) & 0xffffc0ff | (uVar6 & 0x3f) << 8;
          plVar10 = plVar10 + 1;
        } while (lVar11 != 0);
      }
    }
    if (*(int *)(param_2 + 0x774) != 0) {
      FUN_ffff00009074d27c(param_1,uVar7,piVar2);
    }
    piVar9 = (int *)(&DAT_ffff000090878738 + uVar18 * 0xbd8);
    do {
      if (*piVar9 != 0) {
        _DAT_ffff000032490050 = _DAT_ffff000032490050 & 0xfffbffff;
      }
      piVar9 = piVar9 + 0x2f6;
      lVar17 = lVar17 + -1;
    } while (lVar17 != 0);
    if (((*piVar2 == 0) && (*(int *)(param_2 + 0x3bc) != 0)) &&
       (_DAT_ffff000032490050 =
             (uint)*(ushort *)(param_2 + 0x3c0) | _DAT_ffff000032490050 & 0xffff0000 | 0x20000,
       *(int *)(param_2 + 0x308) == 2)) {
      _DAT_ffff0000324a0050 =
           (uint)*(ushort *)(param_2 + 0x3c0) | _DAT_ffff0000324a0050 & 0xffff0000 | 0x20000;
    }
    FUN_ffff00009074cb5c(param_1);
    plVar10 = (long *)(s_0123456789abcdef_ffff000090804a48 + uVar16 * 8 + 0x10);
    puVar13 = (uint *)(uVar16 * 0xbd8 + -0xffff6f787884);
    do {
      uVar8 = *puVar13;
      if (uVar8 != 0) {
        lVar17 = *plVar10;
        *(uint *)(lVar17 + 0x204) =
             *(uint *)(lVar17 + 0x204) & 0x80000000 |
             *(uint *)(lVar17 + 0x204) & 0xffff | (uVar8 & 0x7fff) << 0x10;
        *(uint *)(lVar17 + 0x204) = *(uint *)(lVar17 + 0x204) | 0x80000000;
        FUN_ffff00009077de54
                  (s_dsi_prefetch_time____d_DSI_TIME_C_ffff0000907d9573,uVar8 & 0x7fff,
                   *(undefined4 *)(lVar17 + 0x204));
      }
      puVar13 = puVar13 + 0x2f6;
      lVar15 = lVar15 + -1;
      plVar10 = plVar10 + 1;
    } while (lVar15 != 0);
  }
  return 0;
}



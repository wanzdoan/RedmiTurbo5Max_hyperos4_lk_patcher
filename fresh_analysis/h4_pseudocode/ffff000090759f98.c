
undefined8 FUN_ffff000090759f98(uint param_1,uint param_2,int param_3)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  undefined *puVar5;
  undefined *puVar6;
  ulong uVar7;
  uint *puVar8;
  uint uVar9;
  uint uVar10;
  undefined4 uVar11;
  uint uVar12;
  undefined4 uVar13;
  uint uVar14;
  uint uVar15;
  undefined4 uVar16;
  uint uVar17;
  uint uVar18;
  uint uVar19;
  long lVar20;
  uint uVar21;
  long lVar22;
  undefined4 *puVar23;
  int *piVar24;
  
  puVar6 = PTR_DAT_ffff0000908399b0;
  if (0x13 < param_2) {
    return 0xffffffff;
  }
  puVar8 = &DAT_ffff00009081f3fc + (ulong)param_2 * 0x4b;
  uVar7 = 0;
  do {
    if (puVar8[uVar7] == 0xffffffff) {
      if ((int)uVar7 == 0) {
        return 0xffffffff;
      }
      break;
    }
    uVar7 = uVar7 + 1;
  } while (uVar7 != 0x4b);
  uVar9 = 1;
  uVar14 = 0;
  switch(puVar8[(int)uVar7 - 1]) {
  case 0xc:
    goto code_r0xffff00009075a040;
  default:
    goto code_r0xffff00009075a39c;
  case 0x2c:
    uVar9 = 2;
  case 0x2b:
  case 0x2d:
    uVar14 = 0;
    if (param_3 != 1) {
      uVar14 = uVar9;
    }
code_r0xffff00009075a040:
    if (3 < param_1) {
      return 0xffffffff;
    }
    uVar15 = 0;
    uVar17 = 0;
    uVar18 = 0;
    uVar10 = 0;
    uVar12 = 0;
    uVar19 = 0;
    uVar21 = 0;
    uVar9 = 0;
    uVar7 = uVar7 & 0xffffffff;
    do {
      uVar3 = *puVar8;
      piVar24 = (int *)(&DAT_ffff00009080d800 + (long)(int)uVar3 * 0xc);
      uVar2 = *(uint *)(&DAT_ffff00009080d804 + (long)(int)uVar3 * 0xc);
      if (uVar2 != 0xffffffff) {
        switch(*(int *)(&DAT_ffff00009080d808 + (long)(int)uVar3 * 0xc)) {
        case 0:
          if (*piVar24 == 0x2d) {
            uVar9 = uVar9 | 0x2800000;
          }
          else {
            uVar9 = 1 << (ulong)(uVar2 & 0x1f) | uVar9;
          }
          break;
        case 1:
          if (*piVar24 == 0x2d) {
            uVar21 = uVar21 | 0x2800000;
          }
          else {
            uVar21 = 1 << (ulong)(uVar2 & 0x1f) | uVar21;
          }
          break;
        case 2:
          if (*piVar24 == 0x2d) {
            uVar19 = uVar19 | 0x2800000;
          }
          else {
            uVar19 = 1 << (ulong)(uVar2 & 0x1f) | uVar19;
          }
          break;
        default:
          if ((uVar3 & 0xfffffffe) == 0xc2) {
            if (*piVar24 == 0x2d) {
              uVar18 = uVar18 | 0x2800000;
            }
            else {
              uVar18 = 1 << (ulong)(uVar2 & 0x1f) | uVar18;
            }
          }
          else if (*(int *)(&DAT_ffff00009080d808 + (long)(int)uVar3 * 0xc) == 0x16) {
            if (*piVar24 == 0x2d) {
              uVar12 = uVar12 | 0x2800000;
            }
            else {
              uVar12 = 1 << (ulong)(uVar2 & 0x1f) | uVar12;
            }
          }
          else if ((uVar3 == 0xc4) || (uVar3 == 0xc1)) {
            if (*piVar24 == 0x2d) {
              uVar10 = uVar10 | 0x2800000;
            }
            else {
              uVar10 = 1 << (ulong)(uVar2 & 0x1f) | uVar10;
            }
          }
          break;
        case 0x14:
          if (*piVar24 == 0x2d) {
            uVar17 = uVar17 | 0x2800000;
          }
          else {
            uVar17 = 1 << (ulong)(uVar2 & 0x1f) | uVar17;
          }
          break;
        case 0x15:
          if (*piVar24 == 0x2d) {
            uVar15 = uVar15 | 0x2800000;
          }
          else {
            uVar15 = 1 << (ulong)(uVar2 & 0x1f) | uVar15;
          }
        }
      }
      uVar7 = uVar7 - 1;
      puVar8 = puVar8 + 1;
    } while (uVar7 != 0);
    lVar4 = (ulong)param_1 * 0x20;
    lVar1 = *(long *)PTR_DAT_ffff0000908399a8 + lVar4;
    *(uint *)(lVar1 + 0x34) = uVar9;
    *(uint *)(lVar1 + 0x38) = uVar21;
    puVar5 = PTR_DAT_ffff0000908399a0;
    uVar14 = uVar14 | uVar14 << 7;
    lVar20 = *(long *)puVar6 + lVar4;
    *(uint *)(lVar20 + 0x34) = uVar19;
    *(uint *)(lVar20 + 0x38) = uVar18;
    *(uint *)(lVar1 + 0x30) = uVar14;
    puVar6 = PTR_DAT_ffff0000908399b8;
    lVar22 = *(long *)puVar5;
    lVar1 = lVar22 + 0x30;
    *(uint *)(lVar1 + lVar4) = uVar14;
    *(uint *)(lVar20 + 0x30) = uVar14;
    lVar20 = *(long *)puVar6;
    *(uint *)(lVar20 + 0x30 + lVar4) = uVar14;
    if (uVar17 != 0 || uVar15 != 0) {
      lVar22 = lVar22 + lVar4;
      *(uint *)(lVar22 + 0x34) = uVar17;
      *(uint *)(lVar22 + 0x38) = uVar15;
    }
    if (uVar12 != 0) {
      *(uint *)(lVar20 + lVar4 + 0x34) = uVar12;
    }
    if (uVar10 != 0) {
      *(uint *)(lVar20 + lVar4 + 0x38) = uVar10;
    }
    if (param_2 == 0x12) {
      lVar22 = 0;
      do {
        if ((&DAT_ffff000090820a40)[lVar22] == -1) {
          if ((int)lVar22 < 1) {
            return 0;
          }
          break;
        }
        lVar22 = lVar22 + 1;
      } while (lVar22 != 0x4b);
      uVar11 = 0;
      uVar13 = 0;
      uVar16 = 0x81;
      switch((&DAT_ffff000090820a40)[(int)lVar22 - 1]) {
      default:
code_r0xffff00009075a39c:
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000907dce7e,0x64b,&DAT_ffff0000907d2506);
      case 0x2c:
        uVar16 = 0x102;
      case 0x2b:
      case 0x2d:
        uVar11 = 0;
        if (param_3 != 1) {
          uVar11 = uVar16;
        }
        uVar13 = 0;
        if (param_3 != 1) {
          uVar13 = 0x102;
        }
      case 0xc:
        lVar22 = 0;
        *(undefined4 *)(lVar20 + 0x30 + lVar4) = uVar11;
        *(undefined4 *)(lVar1 + lVar4) = uVar13;
        do {
          if ((&DAT_ffff000090820a40)[lVar22] == -1) {
            if (lVar22 == 0) {
              return 0;
            }
            break;
          }
          lVar22 = lVar22 + 1;
        } while (lVar22 != 0x4b);
        puVar23 = &DAT_ffff000090820a40;
        do {
          FUN_ffff00009075a3b4((ulong)param_1,*puVar23);
          lVar22 = lVar22 + -1;
          puVar23 = puVar23 + 1;
        } while (lVar22 != 0);
      }
    }
    return 0;
  }
}



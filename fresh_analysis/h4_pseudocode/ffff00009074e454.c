
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009074e454(int param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  uint uVar9;
  uint uVar10;
  uint uVar11;
  long *plVar12;
  uint uVar13;
  int iVar14;
  int *piVar15;
  long lVar16;
  long *plVar17;
  uint *puVar18;
  long lVar19;
  ulong uVar20;
  long lVar21;
  uint uVar22;
  uint uVar23;
  
  uVar13 = 0;
  if (param_1 != 0x2b) {
    uVar13 = (uint)(param_1 != 0x2d);
  }
  uVar20 = (ulong)uVar13;
  lVar21 = 1;
  if (param_1 != 0x2b) {
    lVar21 = 2;
  }
  lVar21 = lVar21 - uVar20;
  piVar15 = (int *)(param_3 + uVar20 * 0x18 + 0x4bc);
  plVar12 = &DAT_ffff00009080cf78 + uVar20;
  lVar16 = lVar21;
  do {
    lVar19 = *plVar12;
    *(uint *)(lVar19 + 800) = *(uint *)(lVar19 + 800) | 1;
    *(undefined4 *)(lVar19 + 0x40) = 0x300500;
    if (*(int *)(param_3 + 0x4ac) != 0) {
      if (piVar15[-3] == 4) {
        puVar18 = (uint *)(lVar19 + 0x220);
      }
      else if (piVar15[-2] == 4) {
        puVar18 = (uint *)(lVar19 + 0x420);
      }
      else if (piVar15[-1] == 4) {
        puVar18 = (uint *)(lVar19 + 0x120);
      }
      else if (*piVar15 == 4) {
        puVar18 = (uint *)(lVar19 + 0x520);
      }
      else {
        puVar18 = (uint *)(lVar19 + 800);
      }
      *puVar18 = *puVar18 | 1;
    }
    lVar16 = lVar16 + -1;
    piVar15 = piVar15 + 6;
    plVar12 = plVar12 + 1;
  } while (lVar16 != 0);
  plVar12 = &DAT_ffff00009080cf78 + uVar20;
  uVar22 = 5;
  uVar13 = 0x1a;
  lVar16 = lVar21;
  do {
    uVar23 = *(uint *)(param_3 + 0xc4);
    if (uVar23 == 0) {
      uVar23 = *(int *)(param_3 + 0xc0) << 1;
    }
    uVar10 = *(uint *)(param_3 + 200);
    lVar19 = *plVar12;
    if (uVar23 < 0x9c4) {
      *(uint *)(lVar19 + 8) = *(uint *)(lVar19 + 8) & 0xffffffc0;
    }
    *(undefined4 *)(lVar19 + 4) = 0x3fff0180;
    FUN_ffff00009077cea0(1000);
    *(undefined4 *)(lVar19 + 4) = 0x3fff00c0;
    *(uint *)(lVar19 + 0x25c) = *(uint *)(lVar19 + 0x25c) & 0xfffffffe;
    *(uint *)(lVar19 + 0x45c) = *(uint *)(lVar19 + 0x45c) & 0xfffffffe;
    *(uint *)(lVar19 + 0x15c) = *(uint *)(lVar19 + 0x15c) & 0xfffffffe;
    *(uint *)(lVar19 + 0x55c) = *(uint *)(lVar19 + 0x55c) & 0xfffffffe;
    *(uint *)(lVar19 + 0x35c) = *(uint *)(lVar19 + 0x35c) & 0xfffffffe;
    *(uint *)(lVar19 + 0x3c) = *(uint *)(lVar19 + 0x3c) & 0xffffcfff;
    *(uint *)(lVar19 + 0x30) = *(uint *)(lVar19 + 0x30) | 0x10;
    *(uint *)(lVar19 + 0x30) = *(uint *)(lVar19 + 0x30) & 0xfe7fffff | 0x800000;
    *(uint *)(lVar19 + 0x30) = *(uint *)(lVar19 + 0x30) & 0xf9ffffff | 0x2000000;
    *(uint *)(lVar19 + 0x28) = *(uint *)(lVar19 + 0x28) | 1;
    FUN_ffff00009077cea0(1000);
    *(uint *)(lVar19 + 0x28) = *(uint *)(lVar19 + 0x28) & 0xfffffffd;
    if (uVar23 == 0) {
      uVar10 = 0;
      uVar23 = 0;
    }
    else {
      if (uVar23 < 2000) {
        if (uVar23 < 1000) {
          if (uVar23 < 500) {
            if (uVar23 < 0xfa) {
              if (uVar23 < 0x7d) {
                    /* WARNING: Subroutine does not return */
                FUN_ffff00009077cfcc
                          (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0xbbd,
                           &DAT_ffff0000907d2506);
              }
              iVar14 = 0x10;
              uVar11 = 0x400;
            }
            else {
              iVar14 = 8;
              uVar11 = 0x300;
            }
          }
          else {
            iVar14 = 4;
            uVar11 = 0x200;
          }
        }
        else {
          iVar14 = 2;
          uVar11 = 0x100;
        }
      }
      else {
        uVar11 = 0;
        iVar14 = 1;
      }
      if (uVar10 != 0) {
        uVar23 = uVar10;
      }
      uVar23 = uVar23 >> 1;
      uVar5 = iVar14 * uVar23;
      uVar9 = 0xed7;
      if (uVar10 != 0) {
        uVar13 = 26000;
        uVar9 = 3799999;
      }
      uVar10 = 0;
      if (uVar13 != 0) {
        uVar10 = uVar5 / uVar13;
      }
      uVar4 = uVar5 >> (uVar9 < uVar5);
      uVar6 = 0;
      if (uVar13 != 0) {
        uVar6 = uVar4 / uVar13;
      }
      uVar4 = (uVar4 - uVar6 * uVar13) * 0x100;
      uVar6 = 0;
      if (uVar13 != 0) {
        uVar6 = uVar4 / uVar13;
      }
      uVar4 = (uVar4 - uVar6 * uVar13) * 0x100;
      uVar7 = 0;
      if (uVar13 != 0) {
        uVar7 = uVar4 / uVar13;
      }
      uVar8 = 0;
      if (uVar13 != 0) {
        uVar8 = ((uVar4 - uVar7 * uVar13) * 0x100) / uVar13;
      }
      *(uint *)(lVar19 + 0x2c) =
           uVar10 << 0x18 | (uVar6 & 0xff) << 0x10 | (uVar7 & 0xff) << 8 | uVar8 & 0xff;
      *(uint *)(lVar19 + 0x30) = *(uint *)(lVar19 + 0x30) & 0xfffff8ff | uVar11;
      *(uint *)(lVar19 + 0x30) = *(uint *)(lVar19 + 0x30) & 0xefffffff;
      uVar10 = (uint)(uVar5 <= uVar9);
      if (*(int *)(param_3 + 0xd0) != 1) {
        *(uint *)(lVar19 + 0x34) = *(uint *)(lVar19 + 0x34) | 1;
        *(uint *)(lVar19 + 0x34) = *(uint *)(lVar19 + 0x34) & 0xffff | 0x1b10000;
        if (*(uint *)(param_3 + 0xd4) != 0) {
          uVar22 = *(uint *)(param_3 + 0xd4);
        }
        if (8 < uVar22) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0xbe6,
                     s_delta1_<__8_ffff0000907eb192);
        }
        uVar11 = 0;
        if (uVar13 != 0) {
          uVar11 = (uVar22 * uVar5) / uVar13;
        }
        uVar11 = (uVar11 << 0x12) / 0x69b68;
        *(uint *)(lVar19 + 0x38) = *(uint *)(lVar19 + 0x38) & 0xffff | uVar11 << 0x10;
        *(uint *)(lVar19 + 0x38) = *(uint *)(lVar19 + 0x38) & 0xffff0000 | uVar11;
      }
    }
    *(uint *)(lVar19 + 0x30) =
         (*(uint *)(lVar19 + 0x30) & 0xffffc000 | *(uint *)(lVar19 + 0x30) & 0x1fff | uVar10 << 0xd)
         ^ 0x2000;
    FUN_ffff00009077cea0(1000);
    *(uint *)(lVar19 + 0x30) = *(uint *)(lVar19 + 0x30) | 1;
    FUN_ffff00009077cea0(1000);
    plVar12 = plVar12 + 1;
    lVar16 = lVar16 + -1;
    if (lVar16 == 0) {
      plVar12 = &DAT_ffff00009080cf78 + uVar20;
      piVar15 = (int *)(param_3 + uVar20 * 0x18 + 0x4f0);
      plVar17 = (long *)(s_0123456789abcdef_ffff000090804c70 + uVar20 * 8 + 0x10);
      lVar16 = lVar21;
      do {
        lVar19 = *plVar17;
        *(uint *)(lVar19 + 0x614) = *(uint *)(lVar19 + 0x614) & 0xfffffffe;
        if (*(int *)(param_3 + 0x4ac) == 0) {
          *(undefined4 *)(lVar19 + 0x618) = 0x543210;
        }
        else {
          uVar22 = piVar15[-0xf];
          uVar13 = piVar15[-0xe];
          uVar10 = piVar15[-0xd];
          uVar11 = piVar15[-0xc];
          _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffeffff;
          *(uint *)(lVar19 + 0x618) = *(uint *)(lVar19 + 0x618) & 0xfffffff8 | piVar15[-0x10] & 7U;
          *(uint *)(lVar19 + 0x618) =
               *(uint *)(lVar19 + 0x618) & 0xffffff80 |
               *(uint *)(lVar19 + 0x618) & 0xf | (uVar22 & 7) << 4;
          *(uint *)(lVar19 + 0x618) =
               *(uint *)(lVar19 + 0x618) & 0xfffff800 |
               *(uint *)(lVar19 + 0x618) & 0xff | (uVar13 & 7) << 8;
          *(uint *)(lVar19 + 0x618) =
               *(uint *)(lVar19 + 0x618) & 0xffff8000 |
               *(uint *)(lVar19 + 0x618) & 0xfff | (uVar10 & 7) << 0xc;
          *(uint *)(lVar19 + 0x618) =
               *(uint *)(lVar19 + 0x618) & 0xff800000 |
               *(uint *)(lVar19 + 0x618) & 0xfffff | (uVar11 & 7) << 0x14;
        }
        lVar19 = *plVar12;
        *(undefined4 *)(lVar19 + 0x224) = 0;
        *(undefined4 *)(lVar19 + 0x424) = 0;
        *(undefined4 *)(lVar19 + 0x124) = 0;
        *(undefined4 *)(lVar19 + 0x524) = 0;
        *(undefined4 *)(lVar19 + 0x324) = 0;
        iVar1 = piVar15[-3];
        iVar14 = piVar15[-2];
        iVar2 = piVar15[-1];
        iVar3 = *piVar15;
        if (piVar15[-4] != 0) {
          *(undefined4 *)(lVar19 + 0x224) = 1;
        }
        if (iVar1 != 0) {
          *(undefined4 *)(lVar19 + 0x424) = 1;
        }
        if (iVar14 != 0) {
          *(undefined4 *)(lVar19 + 0x124) = 1;
        }
        if (iVar2 != 0) {
          *(undefined4 *)(lVar19 + 0x524) = 1;
        }
        if (iVar3 != 0) {
          *(undefined4 *)(lVar19 + 0x324) = 1;
        }
        plVar12 = plVar12 + 1;
        lVar16 = lVar16 + -1;
        piVar15 = piVar15 + 6;
        plVar17 = plVar17 + 1;
      } while (lVar16 != 0);
      plVar12 = &DAT_ffff00009080cf78 + uVar20;
      do {
        if ((uVar23 == 0) || (*(int *)(param_3 + 0xd0) == 1)) {
          lVar16 = *plVar12;
          uVar13 = *(uint *)(lVar16 + 0x34) & 0xfffffffd;
        }
        else {
          lVar16 = *plVar12;
          uVar13 = *(uint *)(lVar16 + 0x34) | 2;
        }
        plVar12 = plVar12 + 1;
        lVar21 = lVar21 + -1;
        *(uint *)(lVar16 + 0x34) = uVar13;
      } while (lVar21 != 0);
      return;
    }
  } while( true );
}



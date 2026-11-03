
undefined8 FUN_ffff0000907258a8(undefined8 param_1,ulong param_2,uint *param_3)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  uint uVar4;
  long lVar5;
  int *piVar6;
  char cVar7;
  uint uVar8;
  uint uVar9;
  bool bVar10;
  uint uVar11;
  uint uVar12;
  ulong uVar13;
  long lVar14;
  uint uVar15;
  uint uVar16;
  ulong uVar17;
  uint uVar18;
  ulong uVar19;
  uint uVar20;
  ulong uVar21;
  uint uVar22;
  uint uVar23;
  long lVar24;
  long lVar25;
  ulong uVar26;
  long lVar27;
  uint uVar28;
  ulong uVar29;
  uint uVar30;
  ulong uVar31;
  ulong local_70;
  ulong local_68;
  
  if ((DAT_ffff00009086d37c & 1) == 0) {
    FUN_ffff0000907239a4(&DAT_ffff00009086d1c0);
    if (DAT_ffff00009086d264 != 0x20190831) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_emi_emi_ffff0000907e45a6,0x8d,
                 s_emi_info_arg_magic_tail____0x201_ffff0000907f455a);
    }
    if (DAT_ffff00009086d238 != 0x20221221) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_emi_emi_ffff0000907e45a6,0x8e,
                 s_emi_info_arg_a2d_info_a2d_magic___ffff0000907fcfc1);
    }
    DAT_ffff000090818aa8 = (ulong)DAT_ffff00009086d23c & 0xf;
    DAT_ffff00009086d370 = (ulong)(DAT_ffff00009086d23c >> 4) & 0xf;
    lVar25 = ((ulong)(DAT_ffff00009086d240 >> 2) & 3) - 1;
    DAT_ffff00009086d350 = '\x01';
    if ((uint)lVar25 < 3) {
      DAT_ffff00009086d3cc = *(uint *)(&DAT_ffff0000908049fc + lVar25 * 4);
    }
    else {
      DAT_ffff00009086d3cc = 7;
    }
    DAT_ffff00009086d384 = DAT_ffff00009086d254 & 0x3f;
    DAT_ffff00009086d3d0 = DAT_ffff00009086d240 >> 8 & 3;
    DAT_ffff00009086d394 = DAT_ffff00009086d254 >> 8 & 0x30 | DAT_ffff00009086d24c >> 0x18 & 0xf;
    DAT_ffff00009086d38c = DAT_ffff00009086d254 >> 6 & 0x3f;
    DAT_ffff00009086d39c = DAT_ffff00009086d244 & 0xf | (DAT_ffff00009086d254 >> 0xe & 3) << 4;
    DAT_ffff00009086d3a4 = DAT_ffff00009086d254 >> 0xc & 0x30 | DAT_ffff00009086d244 >> 4 & 0xf;
    DAT_ffff00009086d3b4 = DAT_ffff00009086d254 >> 0x10 & 0x30 | DAT_ffff00009086d244 >> 0xc & 0xf;
    DAT_ffff00009086d3ac = DAT_ffff00009086d254 >> 0xe & 0x30 | DAT_ffff00009086d244 >> 8 & 0xf;
    DAT_ffff00009086d3bc = DAT_ffff00009086d254 >> 0x12 & 0x30 | DAT_ffff00009086d244 >> 0x10 & 0xf;
    DAT_ffff00009086d3c4 = DAT_ffff00009086d254 >> 0x16 & 0x30 | DAT_ffff00009086d244 >> 0x18 & 0xf;
    DAT_ffff00009086d3c0 = DAT_ffff00009086d254 >> 0x14 & 0x30 | DAT_ffff00009086d244 >> 0x14 & 0xf;
    DAT_ffff00009086d3c8 = DAT_ffff00009086d254 >> 0x18 & 0x30 | DAT_ffff00009086d244 >> 0x1c;
    DAT_ffff00009086d3f4 = DAT_ffff00009086d258 & 1;
    DAT_ffff00009086d400 = DAT_ffff00009086d258 >> 1 & 1;
    DAT_ffff00009086d41c = DAT_ffff00009086d258 >> 0x1f;
    DAT_ffff00009086d3d4 = DAT_ffff00009086d25c >> 7 & 1;
    DAT_ffff00009086d3f8 = DAT_ffff00009086d258 >> 0x1b & 1;
    DAT_ffff00009086d3dc = DAT_ffff00009086d260 & 0x30 | DAT_ffff00009086d25c >> 8 & 0xf;
    DAT_ffff00009086d3d8 = DAT_ffff00009086d260 >> 0x10 & 0x3f;
    DAT_ffff00009086d3e0 = DAT_ffff00009086d260 >> 2 & 0x30 | DAT_ffff00009086d25c >> 0xc & 0xf;
    DAT_ffff00009086d3e4 = DAT_ffff00009086d260 >> 4 & 0x30 | DAT_ffff00009086d25c >> 0x10 & 0xf;
    DAT_ffff00009086d3e8 = DAT_ffff00009086d260 >> 6 & 0x30 | DAT_ffff00009086d25c >> 0x14 & 0xf;
    DAT_ffff00009086d3ec = DAT_ffff00009086d260 >> 8 & 0x30 | DAT_ffff00009086d25c >> 0x18 & 0xf;
    DAT_ffff00009086d3f0 = DAT_ffff00009086d260 >> 10 & 0x30 | DAT_ffff00009086d25c >> 0x1c;
    DAT_ffff00009086d418 = 2;
    if ((DAT_ffff00009086d258 & 0x1000000) == 0) {
      DAT_ffff00009086d418 = 3;
    }
    DAT_ffff00009086d410 = DAT_ffff00009086d258 >> 10 & 1;
    DAT_ffff00009086d424 = (DAT_ffff00009086d258 & 4 | DAT_ffff00009086d258 >> 0xc & 3) + 0xd;
    DAT_ffff00009086d408 = DAT_ffff00009086d418;
    if (DAT_ffff00009086d410 != 0) {
      DAT_ffff00009086d408 = 4;
    }
    DAT_ffff00009086d404 = (DAT_ffff00009086d258 >> 4 & 3) + 9;
    DAT_ffff00009086d3fc =
         DAT_ffff00009086d258 >> 8 & 0xf00 | (DAT_ffff00009086d258 >> 8 & 1) << 0xc;
    DAT_ffff00009086d40c = DAT_ffff00009086d258 >> 0xb & 1;
    if (DAT_ffff00009086d3fc == 0) {
      DAT_ffff00009086d3fc =
           2 << (ulong)((DAT_ffff00009086d424 + DAT_ffff00009086d408 +
                        DAT_ffff00009086d400 + DAT_ffff00009086d404) - 0x14 & 0x1f);
    }
    DAT_ffff00009086d420 = (DAT_ffff00009086d258 >> 1 & 4 | DAT_ffff00009086d258 >> 0xe & 3) + 0xd;
    DAT_ffff00009086d414 = (DAT_ffff00009086d258 >> 6 & 3) + 9;
    if (DAT_ffff00009086d40c != 0) {
      DAT_ffff00009086d418 = 4;
    }
    uVar26 = (ulong)(DAT_ffff00009086d250 >> 0xc) & 0xf0 |
             (ulong)(DAT_ffff00009086d248 >> 0x10) & 0xf;
    if ((int)uVar26 == 0) {
      lVar25 = (long)(1 << (ulong)((DAT_ffff00009086d424 + DAT_ffff00009086d408) - 0x12 & 0x1f));
    }
    else {
      lVar25 = uVar26 << 8;
    }
    uVar26 = (ulong)(DAT_ffff00009086d250 >> 0x10) & 0xf0 |
             (ulong)(DAT_ffff00009086d248 >> 0x14) & 0xf;
    if ((int)uVar26 == 0) {
      if ((DAT_ffff00009086d240 >> 0x11 & 1) == 0) {
        lVar14 = 0;
      }
      else {
        lVar14 = (long)(1 << (ulong)((DAT_ffff00009086d420 + DAT_ffff00009086d418) - 0x12 & 0x1f));
      }
    }
    else {
      lVar14 = uVar26 << 8;
    }
    uVar26 = (ulong)(DAT_ffff00009086d250 >> 0x14) & 0xf0 |
             (ulong)(DAT_ffff00009086d248 >> 0x18) & 0xf;
    if ((int)uVar26 == 0) {
      lVar27 = (long)(1 << (ulong)((DAT_ffff00009086d424 + DAT_ffff00009086d408) - 0x12 & 0x1f));
    }
    else {
      lVar27 = uVar26 << 8;
    }
    uVar26 = (ulong)(DAT_ffff00009086d250 >> 0x18) & 0xf0 | (ulong)(DAT_ffff00009086d248 >> 0x1c);
    if ((int)uVar26 == 0) {
      if ((DAT_ffff00009086d240 >> 0x11 & 1) == 0) {
        lVar24 = 0;
      }
      else {
        lVar24 = (long)(1 << (ulong)((DAT_ffff00009086d420 + DAT_ffff00009086d418) - 0x12 & 0x1f));
      }
    }
    else {
      lVar24 = uVar26 << 8;
    }
    lVar5 = 0;
    if (DAT_ffff00009086d3f4 != 0) {
      lVar5 = lVar24;
    }
    lVar24 = 0;
    if (DAT_ffff00009086d3f4 != 0) {
      lVar24 = lVar14;
    }
    lVar14 = 0;
    if (DAT_ffff00009086d3d0 != 0) {
      lVar14 = lVar5 + lVar27;
    }
    DAT_ffff00009086d360 = lVar24 + lVar25 + lVar14 << ((int)DAT_ffff00009086d370 != 0);
    DAT_ffff00009086d37c = 1;
  }
  if (param_3 == (uint *)0x0) {
    return 0xffffffff;
  }
  param_3[0] = 0xffffffff;
  param_3[1] = 0xffffffff;
  param_3[2] = 0xffffffff;
  cVar7 = DAT_ffff00009086d350;
  param_3[3] = 0xffffffff;
  param_3[4] = 0xffffffff;
  param_3[5] = 0xffffffff;
  param_3[6] = 0xffffffff;
  param_3[7] = 0xffffffff;
  param_3[8] = 0xffffffff;
  param_3[9] = 0xffffffff;
  param_3[10] = 0xffffffff;
  param_3[0xb] = 0xffffffff;
  param_3[0xc] = 0xffffffff;
  param_3[0xd] = 0xffffffff;
  param_3[0xe] = 0xffffffff;
  param_3[0xf] = 0xffffffff;
  uVar26 = 0x80000000;
  if (cVar7 == '\0') {
    uVar26 = 0;
  }
  param_3[0x10] = 0xffffffff;
  param_3[0x11] = 0xffffffff;
  param_3[0x12] = 0xffffffff;
  param_3[0x13] = 0xffffffff;
  param_3[0x14] = 0xffffffff;
  param_3[0x15] = 0xffffffff;
  param_3[0x16] = 0xffffffff;
  param_3[0x17] = 0xffffffff;
  param_3[0x18] = 0xffffffff;
  param_3[0x19] = 0xffffffff;
  param_3[0x1a] = 0xffffffff;
  param_3[0x1b] = 0xffffffff;
  param_3[0x1c] = 0xffffffff;
  uVar19 = DAT_ffff000090818aa8;
  param_3[0x1d] = 0xffffffff;
  if (param_2 < uVar26) {
    return 0xffffffff;
  }
  uVar26 = param_2 - uVar26;
  local_70 = uVar26;
  if (DAT_ffff00009086d360 < uVar26 >> 0x14) {
    return 0xffffffff;
  }
  do {
    cVar7 = '\x01';
    bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
    if (bVar10) {
      local_70 = local_70 & 0xffffffffffffffbf;
      cVar7 = ExclusiveMonitorsStatus();
    }
  } while (cVar7 != '\0');
  do {
    cVar7 = '\x01';
    bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
    if (bVar10) {
      local_70 = local_70 & 0xffffffffffffff7f;
      cVar7 = ExclusiveMonitorsStatus();
    }
  } while (cVar7 != '\0');
  do {
    cVar7 = '\x01';
    bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
    if (bVar10) {
      local_70 = local_70 & 0xfffffffffffffeff;
      cVar7 = ExclusiveMonitorsStatus();
    }
  } while (cVar7 != '\0');
  do {
    cVar7 = '\x01';
    bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
    if (bVar10) {
      local_70 = local_70 & 0xfffffffffffffdff;
      cVar7 = ExclusiveMonitorsStatus();
    }
  } while (cVar7 != '\0');
  do {
    cVar7 = '\x01';
    bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
    if (bVar10) {
      local_70 = local_70 & 0xfffffffffffffbff;
      cVar7 = ExclusiveMonitorsStatus();
    }
  } while (cVar7 != '\0');
  do {
    cVar7 = '\x01';
    bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
    if (bVar10) {
      local_70 = local_70 & 0xfffffffffffff7ff;
      cVar7 = ExclusiveMonitorsStatus();
    }
  } while (cVar7 != '\0');
  do {
    cVar7 = '\x01';
    bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
    if (bVar10) {
      local_70 = local_70 & 0xffffffffffffefff;
      cVar7 = ExclusiveMonitorsStatus();
    }
  } while (cVar7 != '\0');
  do {
    cVar7 = '\x01';
    bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
    if (bVar10) {
      local_70 = local_70 & 0xffffffffffffdfff;
      cVar7 = ExclusiveMonitorsStatus();
    }
  } while (cVar7 != '\0');
  do {
    cVar7 = '\x01';
    bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
    if (bVar10) {
      local_70 = local_70 & 0xffffffffffffbfff;
      cVar7 = ExclusiveMonitorsStatus();
    }
  } while (cVar7 != '\0');
  do {
    cVar7 = '\x01';
    bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
    if (bVar10) {
      local_70 = local_70 & 0xffffffffffff7fff;
      cVar7 = ExclusiveMonitorsStatus();
    }
  } while (cVar7 != '\0');
  do {
    cVar7 = '\x01';
    bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
    if (bVar10) {
      local_70 = local_70 & 0xfffffffffffeffff;
      cVar7 = ExclusiveMonitorsStatus();
    }
  } while (cVar7 != '\0');
  uVar11 = (uint)uVar26;
  uVar23 = uVar11 >> 0x11 & 1;
  uVar15 = uVar11 >> 0x12 & 1;
  uVar20 = uVar11 >> 0x13 & 1;
  uVar28 = (uint)param_2;
  uVar30 = uVar11 >> 0x14 & 1;
  uVar22 = uVar11 >> 0x15 & 1;
  uVar18 = uVar11 >> 0x10 & 1;
  uVar29 = local_70 |
           (uVar23 & DAT_ffff00009086d384 >> 1 ^ uVar28 >> 6 & 1 ^
            uVar15 & DAT_ffff00009086d384 >> 2 ^ uVar20 & DAT_ffff00009086d384 >> 3 ^
           uVar30 & DAT_ffff00009086d384 >> 4 ^ uVar22 & DAT_ffff00009086d384 >> 5 ^
           uVar18 & DAT_ffff00009086d384) << 6 |
           (ulong)((uVar23 & DAT_ffff00009086d38c >> 1 ^ uVar28 >> 7 & 1 ^
                    uVar15 & DAT_ffff00009086d38c >> 2 ^ uVar20 & DAT_ffff00009086d38c >> 3 ^
                   uVar30 & DAT_ffff00009086d38c >> 4 ^ uVar22 & DAT_ffff00009086d38c >> 5 ^
                   uVar18 & DAT_ffff00009086d38c) << 7 |
                  (uVar23 & DAT_ffff00009086d394 >> 1 ^ uVar28 >> 8 & 1 ^
                   uVar15 & DAT_ffff00009086d394 >> 2 ^ uVar20 & DAT_ffff00009086d394 >> 3 ^
                  uVar30 & DAT_ffff00009086d394 >> 4 ^ uVar22 & DAT_ffff00009086d394 >> 5 ^
                  uVar18 & DAT_ffff00009086d394) << 8);
  uVar21 = uVar29 | (uVar23 & DAT_ffff00009086d39c >> 1 ^ uVar28 >> 9 & 1 ^
                     uVar15 & DAT_ffff00009086d39c >> 2 ^ uVar20 & DAT_ffff00009086d39c >> 3 ^
                    uVar30 & DAT_ffff00009086d39c >> 4 ^ uVar22 & DAT_ffff00009086d39c >> 5 ^
                    uVar18 & DAT_ffff00009086d39c) << 9;
  uVar31 = uVar21 | (uVar23 & DAT_ffff00009086d3a4 >> 1 ^ uVar28 >> 10 & 1 ^
                     uVar15 & DAT_ffff00009086d3a4 >> 2 ^ uVar20 & DAT_ffff00009086d3a4 >> 3 ^
                    uVar30 & DAT_ffff00009086d3a4 >> 4 ^ uVar22 & DAT_ffff00009086d3a4 >> 5 ^
                    uVar18 & DAT_ffff00009086d3a4) << 10;
  uVar26 = (ulong)((uVar23 & DAT_ffff00009086d3ac >> 1 ^ uVar28 >> 0xb & 1 ^
                    uVar15 & DAT_ffff00009086d3ac >> 2 ^ uVar20 & DAT_ffff00009086d3ac >> 3 ^
                   uVar30 & DAT_ffff00009086d3ac >> 4 ^ uVar22 & DAT_ffff00009086d3ac >> 5 ^
                   uVar18 & DAT_ffff00009086d3ac) << 0xb);
  uVar13 = uVar31 | uVar26;
  uVar17 = uVar13 | ((uVar23 & DAT_ffff00009086d3b4 >> 1 ^ uVar28 >> 0xc & 1 ^
                      uVar15 & DAT_ffff00009086d3b4 >> 2 ^ uVar20 & DAT_ffff00009086d3b4 >> 3 ^
                     uVar30 & DAT_ffff00009086d3b4 >> 4 ^ uVar22 & DAT_ffff00009086d3b4 >> 5 ^
                     uVar18 & DAT_ffff00009086d3b4) << 0xc |
                     (uVar23 & DAT_ffff00009086d3bc >> 1 ^ uVar28 >> 0xd & 1 ^
                      uVar15 & DAT_ffff00009086d3bc >> 2 ^ uVar20 & DAT_ffff00009086d3bc >> 3 ^
                     uVar30 & DAT_ffff00009086d3bc >> 4 ^ uVar22 & DAT_ffff00009086d3bc >> 5 ^
                     uVar18 & DAT_ffff00009086d3bc) << 0xd |
                     (uVar23 & DAT_ffff00009086d3c0 >> 1 ^ uVar28 >> 0xe & 1 ^
                      uVar15 & DAT_ffff00009086d3c0 >> 2 ^ uVar20 & DAT_ffff00009086d3c0 >> 3 ^
                     uVar30 & DAT_ffff00009086d3c0 >> 4 ^ uVar22 & DAT_ffff00009086d3c0 >> 5 ^
                     uVar18 & DAT_ffff00009086d3c0) << 0xe |
                     (uVar23 & DAT_ffff00009086d3c4 >> 1 ^ uVar28 >> 0xf & 1 ^
                      uVar15 & DAT_ffff00009086d3c4 >> 2 ^ uVar20 & DAT_ffff00009086d3c4 >> 3 ^
                     uVar30 & DAT_ffff00009086d3c4 >> 4 ^ uVar22 & DAT_ffff00009086d3c4 >> 5 ^
                     uVar18 & DAT_ffff00009086d3c4) << 0xf |
                    (uVar23 & DAT_ffff00009086d3c8 >> 1 ^ uVar28 >> 0x10 & 1 ^
                     uVar15 & DAT_ffff00009086d3c8 >> 2 ^ uVar20 & DAT_ffff00009086d3c8 >> 3 ^
                    uVar30 & DAT_ffff00009086d3c8 >> 4 ^ uVar22 & DAT_ffff00009086d3c8 >> 5 ^
                    DAT_ffff00009086d3c8 & uVar11 >> 0x16 & 1) << 0x10);
  uVar23 = (uint)(uVar29 >> 8) & 1;
  uVar20 = (uint)uVar21;
  uVar30 = (uint)uVar31;
  uVar15 = (uint)DAT_ffff000090818aa8;
  if (DAT_ffff000090818aa8 == 0) {
    uVar19 = (ulong)DAT_ffff00009086d3cc;
    param_3[1] = (uint)(uVar17 >> (uVar19 & 0x3f)) & 1;
  }
  else {
    param_3[1] = (uint)(DAT_ffff000090818aa8 >> 3) & 1 & (uint)uVar13 >> 0xb ^
                 (uint)(DAT_ffff000090818aa8 >> 2) & 1 & uVar30 >> 10 ^
                 (uint)(DAT_ffff000090818aa8 >> 1) & 1 & uVar20 >> 9 ^ uVar23 & uVar15;
    if ((uVar19 & 1) == 0) {
      if ((uVar15 >> 1 & 1) == 0) {
        if ((uVar15 >> 2 & 1) == 0) {
          uVar22 = 0xb;
          if ((uVar19 & 8) == 0) {
            uVar22 = 0xffffffff;
          }
          uVar19 = (ulong)uVar22;
        }
        else {
          uVar19 = 10;
        }
      }
      else {
        uVar19 = 9;
      }
    }
    else {
      uVar19 = 8;
    }
  }
  uVar13 = DAT_ffff00009086d370;
  uVar22 = (uint)DAT_ffff00009086d370;
  uVar18 = (uint)uVar19;
  if (DAT_ffff00009086d370 == 0) {
    *param_3 = 0;
    if (DAT_ffff00009086d3d0 == 0) goto LAB_ffff000090726450;
    local_68 = uVar17;
    puVar3 = (uint *)((long)&local_68 + ((ulong)(long)(int)uVar18 >> 5) * 4);
    do {
      cVar7 = '\x01';
      bVar10 = (bool)ExclusiveMonitorPass(puVar3,0x10);
      if (bVar10) {
        *puVar3 = *puVar3 & ~(1 << (ulong)(uVar18 & 0x1f));
        cVar7 = ExclusiveMonitorsStatus();
      }
    } while (cVar7 != '\0');
    uVar26 = (local_68 >> ((ulong)(uVar18 + 1) & 0x3f)) << (uVar19 & 0x3f);
    uVar17 = uVar17 & (-1L << (uVar19 & 0x3f) ^ 0xffffffffffffffffU);
  }
  else {
    uVar11 = 0;
    if ((local_70 & 0x800) != 0 || uVar26 != 0) {
      uVar11 = uVar22 >> 3 & 1;
    }
    *param_3 = uVar11 ^ (uint)(DAT_ffff00009086d370 >> 2) & 1 & uVar30 >> 10 ^
                        (uint)(DAT_ffff00009086d370 >> 1) & 1 & uVar20 >> 9 ^ uVar23 & uVar22;
    if ((uVar13 & 1) == 0) {
      if ((uVar22 >> 1 & 1) == 0) {
        if ((uVar22 >> 2 & 1) == 0) {
          bVar10 = (uVar13 & 8) == 0;
          uVar23 = 0xb;
          if (bVar10) {
            uVar23 = 0xffffffff;
          }
          uVar13 = (ulong)uVar23;
        }
        else {
          bVar10 = false;
          uVar13 = 10;
        }
      }
      else {
        bVar10 = false;
        uVar13 = 9;
      }
    }
    else {
      bVar10 = false;
      uVar13 = 8;
    }
    if ((int)uVar18 < 0) {
      return 0xffffffff;
    }
    if (bVar10) {
      return 0xffffffff;
    }
    uVar23 = (uint)uVar13;
    if (DAT_ffff00009086d3d0 == 0) {
      local_68 = uVar17;
      puVar3 = (uint *)((long)&local_68 + ((ulong)(long)(int)uVar23 >> 5) * 4);
      do {
        cVar7 = '\x01';
        bVar10 = (bool)ExclusiveMonitorPass(puVar3,0x10);
        if (bVar10) {
          *puVar3 = *puVar3 & ~(1 << (ulong)(uVar23 & 0x1f));
          cVar7 = ExclusiveMonitorsStatus();
        }
      } while (cVar7 != '\0');
      uVar26 = (local_68 >> ((ulong)(uVar23 + 1) & 0x3f)) << (uVar13 & 0x3f);
      uVar17 = uVar17 & (-1L << (uVar13 & 0x3f) ^ 0xffffffffffffffffU);
    }
    else {
      if ((int)uVar18 < (int)uVar23) {
        local_68 = uVar17;
        puVar3 = (uint *)((long)&local_68 + ((ulong)(long)(int)uVar23 >> 5) * 4);
        do {
          cVar7 = '\x01';
          bVar10 = (bool)ExclusiveMonitorPass(puVar3,0x10);
          if (bVar10) {
            *puVar3 = *puVar3 & ~(1 << (ulong)(uVar23 & 0x1f));
            cVar7 = ExclusiveMonitorsStatus();
          }
        } while (cVar7 != '\0');
        uVar17 = (local_68 >> ((ulong)(uVar23 + 1) & 0x3f)) << (uVar13 & 0x3f) |
                 uVar17 & (-1L << (uVar13 & 0x3f) ^ 0xffffffffffffffffU);
        puVar3 = (uint *)((long)&local_68 + (ulong)(uVar18 >> 5) * 4);
        local_68 = uVar17;
        do {
          cVar7 = '\x01';
          bVar10 = (bool)ExclusiveMonitorPass(puVar3,0x10);
          if (bVar10) {
            *puVar3 = *puVar3 & ~(1 << (ulong)(uVar18 & 0x1f));
            cVar7 = ExclusiveMonitorsStatus();
          }
        } while (cVar7 != '\0');
        uVar26 = (local_68 >> ((ulong)(uVar18 + 1) & 0x3f)) << (uVar19 & 0x3f);
        uVar13 = uVar19;
      }
      else {
        puVar3 = (uint *)((long)&local_68 + (ulong)(uVar18 >> 5) * 4);
        local_68 = uVar17;
        do {
          cVar7 = '\x01';
          bVar10 = (bool)ExclusiveMonitorPass(puVar3,0x10);
          if (bVar10) {
            *puVar3 = *puVar3 & ~(1 << (ulong)(uVar18 & 0x1f));
            cVar7 = ExclusiveMonitorsStatus();
          }
        } while (cVar7 != '\0');
        uVar17 = (local_68 >> ((ulong)(uVar18 + 1) & 0x3f)) << (uVar19 & 0x3f) |
                 uVar17 & (-1L << (uVar19 & 0x3f) ^ 0xffffffffffffffffU);
        puVar3 = (uint *)((long)&local_68 + ((ulong)(long)(int)uVar23 >> 5) * 4);
        local_68 = uVar17;
        do {
          cVar7 = '\x01';
          bVar10 = (bool)ExclusiveMonitorPass(puVar3,0x10);
          if (bVar10) {
            *puVar3 = *puVar3 & ~(1 << (ulong)(uVar23 & 0x1f));
            cVar7 = ExclusiveMonitorsStatus();
          }
        } while (cVar7 != '\0');
        uVar26 = (local_68 >> ((ulong)(uVar23 + 1) & 0x3f)) << (uVar13 & 0x3f);
      }
      uVar17 = uVar17 & (-1L << (uVar13 & 0x3f) ^ 0xffffffffffffffffU);
    }
  }
  uVar17 = uVar26 | uVar17;
LAB_ffff000090726450:
  uVar23 = DAT_ffff00009086d3d4;
  local_70 = uVar17;
  if (DAT_ffff00009086d3d4 != 0) {
    do {
      cVar7 = '\x01';
      bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
      if (bVar10) {
        local_70 = local_70 & 0xfffffffffffffeff;
        cVar7 = ExclusiveMonitorsStatus();
      }
    } while (cVar7 != '\0');
    do {
      cVar7 = '\x01';
      bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
      if (bVar10) {
        local_70 = local_70 & 0xfffffffffffff7ff;
        cVar7 = ExclusiveMonitorsStatus();
      }
    } while (cVar7 != '\0');
    do {
      cVar7 = '\x01';
      bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
      if (bVar10) {
        local_70 = local_70 & 0xffffffffffffefff;
        cVar7 = ExclusiveMonitorsStatus();
      }
    } while (cVar7 != '\0');
    do {
      cVar7 = '\x01';
      bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
      if (bVar10) {
        local_70 = local_70 & 0xffffffffffffdfff;
        cVar7 = ExclusiveMonitorsStatus();
      }
    } while (cVar7 != '\0');
    do {
      cVar7 = '\x01';
      bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
      if (bVar10) {
        local_70 = local_70 & 0xffffffffffffbfff;
        cVar7 = ExclusiveMonitorsStatus();
      }
    } while (cVar7 != '\0');
    do {
      cVar7 = '\x01';
      bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
      if (bVar10) {
        local_70 = local_70 & 0xffffffffffff7fff;
        cVar7 = ExclusiveMonitorsStatus();
      }
    } while (cVar7 != '\0');
    do {
      cVar7 = '\x01';
      bVar10 = (bool)ExclusiveMonitorPass(&local_70,0x10);
      if (bVar10) {
        local_70 = local_70 & 0xfffffffffffeffff;
        cVar7 = ExclusiveMonitorsStatus();
      }
    } while (cVar7 != '\0');
    uVar16 = (uint)uVar17;
    uVar20 = uVar16 >> 0x10 & 1;
    uVar30 = uVar16 >> 0x11 & 1;
    uVar18 = uVar16 >> 0x12 & 1;
    uVar11 = uVar16 >> 0x13 & 1;
    uVar28 = uVar16 >> 0x14 & 1;
    uVar8 = uVar16 >> 0x15 & 1;
    uVar17 = local_70 |
             (uVar30 & DAT_ffff00009086d3d8 >> 1 ^ uVar16 >> 8 & 1 ^
              uVar18 & DAT_ffff00009086d3d8 >> 2 ^ uVar11 & DAT_ffff00009086d3d8 >> 3 ^
             uVar28 & DAT_ffff00009086d3d8 >> 4 ^ uVar8 & DAT_ffff00009086d3d8 >> 5 ^
             uVar20 & DAT_ffff00009086d3d8) << 8 |
             (ulong)((uVar30 & DAT_ffff00009086d3dc >> 1 ^ uVar16 >> 0xb & 1 ^
                      uVar18 & DAT_ffff00009086d3dc >> 2 ^ uVar11 & DAT_ffff00009086d3dc >> 3 ^
                     uVar28 & DAT_ffff00009086d3dc >> 4 ^ uVar8 & DAT_ffff00009086d3dc >> 5 ^
                     uVar20 & DAT_ffff00009086d3dc) << 0xb |
                    (uVar30 & DAT_ffff00009086d3e0 >> 1 ^ uVar16 >> 0xc & 1 ^
                     uVar18 & DAT_ffff00009086d3e0 >> 2 ^ uVar11 & DAT_ffff00009086d3e0 >> 3 ^
                    uVar28 & DAT_ffff00009086d3e0 >> 4 ^ uVar8 & DAT_ffff00009086d3e0 >> 5 ^
                    uVar20 & DAT_ffff00009086d3e0) << 0xc) |
             (ulong)((uVar30 & DAT_ffff00009086d3e4 >> 1 ^ uVar16 >> 0xd & 1 ^
                      uVar18 & DAT_ffff00009086d3e4 >> 2 ^ uVar11 & DAT_ffff00009086d3e4 >> 3 ^
                     uVar28 & DAT_ffff00009086d3e4 >> 4 ^ uVar8 & DAT_ffff00009086d3e4 >> 5 ^
                     uVar20 & DAT_ffff00009086d3e4) << 0xd |
                     (uVar30 & DAT_ffff00009086d3e8 >> 1 ^ uVar16 >> 0xe & 1 ^
                      uVar18 & DAT_ffff00009086d3e8 >> 2 ^ uVar11 & DAT_ffff00009086d3e8 >> 3 ^
                     uVar28 & DAT_ffff00009086d3e8 >> 4 ^ uVar8 & DAT_ffff00009086d3e8 >> 5 ^
                     uVar20 & DAT_ffff00009086d3e8) << 0xe |
                    (uVar30 & DAT_ffff00009086d3ec >> 1 ^ uVar16 >> 0xf & 1 ^
                     uVar18 & DAT_ffff00009086d3ec >> 2 ^ uVar11 & DAT_ffff00009086d3ec >> 3 ^
                    uVar28 & DAT_ffff00009086d3ec >> 4 ^ uVar8 & DAT_ffff00009086d3ec >> 5 ^
                    uVar20 & DAT_ffff00009086d3ec) << 0xf) |
             (ulong)((uVar30 & DAT_ffff00009086d3f0 >> 1 ^ uVar18 & DAT_ffff00009086d3f0 >> 2 ^
                      uVar11 & DAT_ffff00009086d3f0 >> 3 ^ uVar28 & DAT_ffff00009086d3f0 >> 4 ^
                      uVar8 & DAT_ffff00009086d3f0 >> 5 ^ DAT_ffff00009086d3f0 & uVar16 >> 0x16 & 1
                     ^ uVar20) << 0x10);
  }
  if (DAT_ffff00009086d3f4 == 0) {
    uVar20 = 0;
  }
  else if (DAT_ffff00009086d3f8 == 0) {
    uVar20 = (uint)((ulong)DAT_ffff00009086d3fc <= uVar17 >> 0x14);
  }
  else {
    uVar20 = (uint)(uVar17 >>
                   ((ulong)DAT_ffff00009086d400 +
                    (ulong)DAT_ffff00009086d408 + (ulong)DAT_ffff00009086d404 + 1 & 0x3f));
  }
  param_3[2] = uVar20;
  uVar8 = DAT_ffff00009086d418;
  uVar28 = DAT_ffff00009086d414;
  uVar11 = DAT_ffff00009086d408;
  uVar18 = DAT_ffff00009086d404;
  uVar30 = DAT_ffff00009086d400;
  piVar6 = (int *)&DAT_ffff00009086d410;
  if (uVar20 != 0) {
    piVar6 = (int *)&DAT_ffff00009086d40c;
  }
  if (*piVar6 == 0) {
    uVar16 = DAT_ffff00009086d404;
    if (uVar20 != 0) {
      uVar16 = DAT_ffff00009086d414;
    }
    uVar4 = DAT_ffff00009086d408;
    if (uVar20 != 0) {
      uVar4 = DAT_ffff00009086d418;
    }
    uVar12 = (1 << (ulong)(uVar16 & 0x1f)) - 1U &
             (uint)(uVar17 >> ((ulong)DAT_ffff00009086d400 + 1 & 0x3f));
    uVar16 = (1 << (ulong)(uVar4 & 0x1f)) - 1U &
             (uint)(uVar17 >> ((ulong)uVar16 + (ulong)DAT_ffff00009086d400 + 1 & 0x3f));
  }
  else {
    local_68 = uVar17;
    do {
      cVar7 = '\x01';
      bVar10 = (bool)ExclusiveMonitorPass(&local_68,0x10);
      if (bVar10) {
        local_68 = local_68 & 0xfffffffffffffeff;
        cVar7 = ExclusiveMonitorsStatus();
      }
    } while (cVar7 != '\0');
    uVar16 = DAT_ffff00009086d408;
    if (uVar20 != 0) {
      uVar16 = DAT_ffff00009086d418;
    }
    uVar4 = DAT_ffff00009086d404;
    if (uVar20 != 0) {
      uVar4 = DAT_ffff00009086d414;
    }
    bVar10 = DAT_ffff00009086d41c == 0;
    uVar21 = uVar17 & 0xff | (local_68 >> 9) << 8;
    uVar19 = (ulong)(1 << (ulong)(uVar16 - 1 & 0x1f));
    uVar13 = uVar21 >> ((ulong)uVar4 + (ulong)DAT_ffff00009086d400 + 1 & 0x3f);
    uVar26 = 0;
    if (uVar19 != 0) {
      uVar26 = uVar13 / uVar19;
    }
    uVar13 = uVar13 - uVar26 * uVar19;
    uVar26 = 4;
    if (!bVar10) {
      uVar26 = 2;
    }
    uVar19 = 4;
    if (bVar10) {
      uVar19 = 1;
    }
    uVar12 = (1 << (ulong)(uVar4 & 0x1f)) - 1U &
             (uint)(uVar21 >> ((ulong)DAT_ffff00009086d400 + 1 & 0x3f));
    uVar16 = (uint)(uVar17 >> 6) & 4 | (uint)((uVar26 & uVar13) != 0) << 1 |
             (uint)(uVar13 >> bVar10) & 1 | (uint)((uVar19 & uVar13) != 0) << 3;
  }
  param_3[5] = uVar12;
  param_3[3] = uVar16;
  uVar12 = DAT_ffff00009086d424;
  uVar4 = DAT_ffff00009086d394;
  uVar16 = DAT_ffff00009086d384;
  if (DAT_ffff00009086d3f8 == 0) {
    if (uVar20 != 0) {
      uVar17 = uVar17 - (DAT_ffff00009086d3fc << 0x14);
    }
  }
  else {
    uVar1 = uVar18;
    uVar9 = uVar11;
    if (uVar20 != 0) {
      uVar1 = uVar28;
      uVar9 = uVar8;
    }
    local_68 = uVar17;
    iVar2 = uVar30 + uVar9 + uVar1;
    uVar1 = iVar2 + 1;
    puVar3 = (uint *)((long)&local_68 + ((ulong)(long)(int)uVar1 >> 5) * 4);
    do {
      cVar7 = '\x01';
      bVar10 = (bool)ExclusiveMonitorPass(puVar3,0x10);
      if (bVar10) {
        *puVar3 = *puVar3 & ~(1 << (ulong)(uVar1 & 0x1f));
        cVar7 = ExclusiveMonitorsStatus();
      }
    } while (cVar7 != '\0');
    uVar17 = (local_68 >> ((ulong)(iVar2 + 2) & 0x3f)) << ((ulong)uVar1 & 0x3f) |
             uVar17 & (-1L << ((ulong)uVar1 & 0x3f) ^ 0xffffffffffffffffU);
  }
  bVar10 = uVar20 != 0;
  uVar20 = uVar11;
  if (bVar10) {
    uVar20 = uVar8;
  }
  uVar8 = uVar18;
  if (bVar10) {
    uVar8 = uVar28;
  }
  uVar28 = DAT_ffff00009086d424;
  if (bVar10) {
    uVar28 = DAT_ffff00009086d420;
  }
  param_3[6] = uVar11;
  param_3[7] = uVar12;
  uVar12 = DAT_ffff00009086d3ac;
  uVar11 = DAT_ffff00009086d39c;
  param_3[8] = uVar18;
  param_3[9] = uVar22;
  param_3[0xe] = uVar4;
  param_3[0xf] = uVar11;
  uVar18 = DAT_ffff00009086d3c0;
  uVar22 = DAT_ffff00009086d3b4;
  param_3[10] = uVar15;
  param_3[0xb] = uVar23;
  uVar23 = DAT_ffff00009086d38c;
  param_3[4] = (1 << (ulong)(uVar28 & 0x1f)) - 1U &
               (uint)(uVar17 >> ((ulong)uVar20 + (ulong)(uVar30 + 1) + (ulong)uVar8 & 0x3f));
  uVar20 = DAT_ffff00009086d3c4;
  uVar15 = DAT_ffff00009086d3a4;
  param_3[0xc] = uVar16;
  param_3[0xd] = uVar23;
  param_3[0x10] = uVar15;
  param_3[0x11] = uVar12;
  uVar23 = DAT_ffff00009086d3bc;
  param_3[0x14] = uVar18;
  param_3[0x15] = uVar20;
  param_3[0x12] = uVar22;
  param_3[0x13] = uVar23;
  uVar23 = DAT_ffff00009086d3d8;
  param_3[0x16] = DAT_ffff00009086d3c8;
  param_3[0x17] = uVar23;
  uVar20 = DAT_ffff00009086d3e8;
  uVar15 = DAT_ffff00009086d3e4;
  uVar23 = DAT_ffff00009086d3e0;
  param_3[0x18] = DAT_ffff00009086d3dc;
  param_3[0x19] = uVar23;
  uVar30 = DAT_ffff00009086d3f0;
  uVar23 = DAT_ffff00009086d3ec;
  param_3[0x1a] = uVar15;
  param_3[0x1b] = uVar20;
  param_3[0x1c] = uVar23;
  param_3[0x1d] = uVar30;
  return 0;
}



// ffff000090725664  size=4532  FUN_ffff000090725664

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090725664(undefined8 param_1,ulong param_2,uint *param_3)

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
  
  if ((DAT_ffff00009086d2e4 & 1) == 0) {
    FUN_ffff000090723760(0xffff00009086d128);
    if (_DAT_ffff00009086d1cc != 0x20190831) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_emi_emi_ffff0000907e448b,0x8d,
                 s_emi_info_arg_magic_tail____0x201_ffff0000907f43d6);
    }
    if (_DAT_ffff00009086d1a0 != 0x20221221) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_emi_emi_ffff0000907e448b,0x8e,
                 s_emi_info_arg_a2d_info_a2d_magic___ffff0000907fce19);
    }
    DAT_ffff000090818a88 = (ulong)_DAT_ffff00009086d1a4 & 0xf;
    _DAT_ffff00009086d2d8 = (ulong)(_DAT_ffff00009086d1a4 >> 4) & 0xf;
    lVar25 = ((ulong)(_DAT_ffff00009086d1a8 >> 2) & 3) - 1;
    DAT_ffff00009086d2b8 = '\x01';
    if ((uint)lVar25 < 3) {
      _DAT_ffff00009086d334 = *(uint *)(&DAT_ffff0000908047d4 + lVar25 * 4);
    }
    else {
      _DAT_ffff00009086d334 = 7;
    }
    _DAT_ffff00009086d2ec = _DAT_ffff00009086d1bc & 0x3f;
    _DAT_ffff00009086d338 = _DAT_ffff00009086d1a8 >> 8 & 3;
    _DAT_ffff00009086d2fc = _DAT_ffff00009086d1bc >> 8 & 0x30 | _DAT_ffff00009086d1b4 >> 0x18 & 0xf;
    _DAT_ffff00009086d2f4 = _DAT_ffff00009086d1bc >> 6 & 0x3f;
    _DAT_ffff00009086d304 = _DAT_ffff00009086d1ac & 0xf | (_DAT_ffff00009086d1bc >> 0xe & 3) << 4;
    _DAT_ffff00009086d30c = _DAT_ffff00009086d1bc >> 0xc & 0x30 | _DAT_ffff00009086d1ac >> 4 & 0xf;
    _DAT_ffff00009086d31c =
         _DAT_ffff00009086d1bc >> 0x10 & 0x30 | _DAT_ffff00009086d1ac >> 0xc & 0xf;
    _DAT_ffff00009086d314 = _DAT_ffff00009086d1bc >> 0xe & 0x30 | _DAT_ffff00009086d1ac >> 8 & 0xf;
    _DAT_ffff00009086d324 =
         _DAT_ffff00009086d1bc >> 0x12 & 0x30 | _DAT_ffff00009086d1ac >> 0x10 & 0xf;
    _DAT_ffff00009086d32c =
         _DAT_ffff00009086d1bc >> 0x16 & 0x30 | _DAT_ffff00009086d1ac >> 0x18 & 0xf;
    _DAT_ffff00009086d328 =
         _DAT_ffff00009086d1bc >> 0x14 & 0x30 | _DAT_ffff00009086d1ac >> 0x14 & 0xf;
    _DAT_ffff00009086d330 = _DAT_ffff00009086d1bc >> 0x18 & 0x30 | _DAT_ffff00009086d1ac >> 0x1c;
    _DAT_ffff00009086d35c = _DAT_ffff00009086d1c0 & 1;
    _DAT_ffff00009086d368 = _DAT_ffff00009086d1c0 >> 1 & 1;
    _DAT_ffff00009086d384 = _DAT_ffff00009086d1c0 >> 0x1f;
    _DAT_ffff00009086d33c = _DAT_ffff00009086d1c4 >> 7 & 1;
    _DAT_ffff00009086d360 = _DAT_ffff00009086d1c0 >> 0x1b & 1;
    _DAT_ffff00009086d344 = _DAT_ffff00009086d1c8 & 0x30 | _DAT_ffff00009086d1c4 >> 8 & 0xf;
    _DAT_ffff00009086d340 = _DAT_ffff00009086d1c8 >> 0x10 & 0x3f;
    _DAT_ffff00009086d348 = _DAT_ffff00009086d1c8 >> 2 & 0x30 | _DAT_ffff00009086d1c4 >> 0xc & 0xf;
    _DAT_ffff00009086d34c = _DAT_ffff00009086d1c8 >> 4 & 0x30 | _DAT_ffff00009086d1c4 >> 0x10 & 0xf;
    _DAT_ffff00009086d350 = _DAT_ffff00009086d1c8 >> 6 & 0x30 | _DAT_ffff00009086d1c4 >> 0x14 & 0xf;
    _DAT_ffff00009086d354 = _DAT_ffff00009086d1c8 >> 8 & 0x30 | _DAT_ffff00009086d1c4 >> 0x18 & 0xf;
    _DAT_ffff00009086d358 = _DAT_ffff00009086d1c8 >> 10 & 0x30 | _DAT_ffff00009086d1c4 >> 0x1c;
    _DAT_ffff00009086d380 = 2;
    if ((_DAT_ffff00009086d1c0 & 0x1000000) == 0) {
      _DAT_ffff00009086d380 = 3;
    }
    _DAT_ffff00009086d378 = _DAT_ffff00009086d1c0 >> 10 & 1;
    _DAT_ffff00009086d38c = (_DAT_ffff00009086d1c0 & 4 | _DAT_ffff00009086d1c0 >> 0xc & 3) + 0xd;
    _DAT_ffff00009086d370 = _DAT_ffff00009086d380;
    if (_DAT_ffff00009086d378 != 0) {
      _DAT_ffff00009086d370 = 4;
    }
    _DAT_ffff00009086d36c = (_DAT_ffff00009086d1c0 >> 4 & 3) + 9;
    _DAT_ffff00009086d364 =
         _DAT_ffff00009086d1c0 >> 8 & 0xf00 | (_DAT_ffff00009086d1c0 >> 8 & 1) << 0xc;
    _DAT_ffff00009086d374 = _DAT_ffff00009086d1c0 >> 0xb & 1;
    if (_DAT_ffff00009086d364 == 0) {
      _DAT_ffff00009086d364 =
           2 << (ulong)((_DAT_ffff00009086d38c + _DAT_ffff00009086d370 +
                        _DAT_ffff00009086d368 + _DAT_ffff00009086d36c) - 0x14 & 0x1f);
    }
    _DAT_ffff00009086d388 =
         (_DAT_ffff00009086d1c0 >> 1 & 4 | _DAT_ffff00009086d1c0 >> 0xe & 3) + 0xd;
    _DAT_ffff00009086d37c = (_DAT_ffff00009086d1c0 >> 6 & 3) + 9;
    if (_DAT_ffff00009086d374 != 0) {
      _DAT_ffff00009086d380 = 4;
    }
    uVar26 = (ulong)(_DAT_ffff00009086d1b8 >> 0xc) & 0xf0 |
             (ulong)(_DAT_ffff00009086d1b0 >> 0x10) & 0xf;
    if ((int)uVar26 == 0) {
      lVar25 = (long)(1 << (ulong)((_DAT_ffff00009086d38c + _DAT_ffff00009086d370) - 0x12 & 0x1f));
    }
    else {
      lVar25 = uVar26 << 8;
    }
    uVar26 = (ulong)(_DAT_ffff00009086d1b8 >> 0x10) & 0xf0 |
             (ulong)(_DAT_ffff00009086d1b0 >> 0x14) & 0xf;
    if ((int)uVar26 == 0) {
      if ((_DAT_ffff00009086d1a8 >> 0x11 & 1) == 0) {
        lVar14 = 0;
      }
      else {
        lVar14 = (long)(1 << (ulong)((_DAT_ffff00009086d388 + _DAT_ffff00009086d380) - 0x12 & 0x1f))
        ;
      }
    }
    else {
      lVar14 = uVar26 << 8;
    }
    uVar26 = (ulong)(_DAT_ffff00009086d1b8 >> 0x14) & 0xf0 |
             (ulong)(_DAT_ffff00009086d1b0 >> 0x18) & 0xf;
    if ((int)uVar26 == 0) {
      lVar27 = (long)(1 << (ulong)((_DAT_ffff00009086d38c + _DAT_ffff00009086d370) - 0x12 & 0x1f));
    }
    else {
      lVar27 = uVar26 << 8;
    }
    uVar26 = (ulong)(_DAT_ffff00009086d1b8 >> 0x18) & 0xf0 | (ulong)(_DAT_ffff00009086d1b0 >> 0x1c);
    if ((int)uVar26 == 0) {
      if ((_DAT_ffff00009086d1a8 >> 0x11 & 1) == 0) {
        lVar24 = 0;
      }
      else {
        lVar24 = (long)(1 << (ulong)((_DAT_ffff00009086d388 + _DAT_ffff00009086d380) - 0x12 & 0x1f))
        ;
      }
    }
    else {
      lVar24 = uVar26 << 8;
    }
    lVar5 = 0;
    if (_DAT_ffff00009086d35c != 0) {
      lVar5 = lVar24;
    }
    lVar24 = 0;
    if (_DAT_ffff00009086d35c != 0) {
      lVar24 = lVar14;
    }
    lVar14 = 0;
    if (_DAT_ffff00009086d338 != 0) {
      lVar14 = lVar5 + lVar27;
    }
    _DAT_ffff00009086d2c8 = lVar24 + lVar25 + lVar14 << ((int)_DAT_ffff00009086d2d8 != 0);
    DAT_ffff00009086d2e4 = 1;
  }
  if (param_3 == (uint *)0x0) {
    return 0xffffffff;
  }
  param_3[0] = 0xffffffff;
  param_3[1] = 0xffffffff;
  param_3[2] = 0xffffffff;
  cVar7 = DAT_ffff00009086d2b8;
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
  uVar19 = DAT_ffff000090818a88;
  param_3[0x1d] = 0xffffffff;
  if (param_2 < uVar26) {
    return 0xffffffff;
  }
  uVar26 = param_2 - uVar26;
  local_70 = uVar26;
  if (_DAT_ffff00009086d2c8 < uVar26 >> 0x14) {
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
           (uVar23 & _DAT_ffff00009086d2ec >> 1 ^ uVar28 >> 6 & 1 ^
            uVar15 & _DAT_ffff00009086d2ec >> 2 ^ uVar20 & _DAT_ffff00009086d2ec >> 3 ^
           uVar30 & _DAT_ffff00009086d2ec >> 4 ^ uVar22 & _DAT_ffff00009086d2ec >> 5 ^
           uVar18 & _DAT_ffff00009086d2ec) << 6 |
           (ulong)((uVar23 & _DAT_ffff00009086d2f4 >> 1 ^ uVar28 >> 7 & 1 ^
                    uVar15 & _DAT_ffff00009086d2f4 >> 2 ^ uVar20 & _DAT_ffff00009086d2f4 >> 3 ^
                   uVar30 & _DAT_ffff00009086d2f4 >> 4 ^ uVar22 & _DAT_ffff00009086d2f4 >> 5 ^
                   uVar18 & _DAT_ffff00009086d2f4) << 7 |
                  (uVar23 & _DAT_ffff00009086d2fc >> 1 ^ uVar28 >> 8 & 1 ^
                   uVar15 & _DAT_ffff00009086d2fc >> 2 ^ uVar20 & _DAT_ffff00009086d2fc >> 3 ^
                  uVar30 & _DAT_ffff00009086d2fc >> 4 ^ uVar22 & _DAT_ffff00009086d2fc >> 5 ^
                  uVar18 & _DAT_ffff00009086d2fc) << 8);
  uVar21 = uVar29 | (uVar23 & _DAT_ffff00009086d304 >> 1 ^ uVar28 >> 9 & 1 ^
                     uVar15 & _DAT_ffff00009086d304 >> 2 ^ uVar20 & _DAT_ffff00009086d304 >> 3 ^
                    uVar30 & _DAT_ffff00009086d304 >> 4 ^ uVar22 & _DAT_ffff00009086d304 >> 5 ^
                    uVar18 & _DAT_ffff00009086d304) << 9;
  uVar31 = uVar21 | (uVar23 & _DAT_ffff00009086d30c >> 1 ^ uVar28 >> 10 & 1 ^
                     uVar15 & _DAT_ffff00009086d30c >> 2 ^ uVar20 & _DAT_ffff00009086d30c >> 3 ^
                    uVar30 & _DAT_ffff00009086d30c >> 4 ^ uVar22 & _DAT_ffff00009086d30c >> 5 ^
                    uVar18 & _DAT_ffff00009086d30c) << 10;
  uVar26 = (ulong)((uVar23 & _DAT_ffff00009086d314 >> 1 ^ uVar28 >> 0xb & 1 ^
                    uVar15 & _DAT_ffff00009086d314 >> 2 ^ uVar20 & _DAT_ffff00009086d314 >> 3 ^
                   uVar30 & _DAT_ffff00009086d314 >> 4 ^ uVar22 & _DAT_ffff00009086d314 >> 5 ^
                   uVar18 & _DAT_ffff00009086d314) << 0xb);
  uVar13 = uVar31 | uVar26;
  uVar17 = uVar13 | ((uVar23 & _DAT_ffff00009086d31c >> 1 ^ uVar28 >> 0xc & 1 ^
                      uVar15 & _DAT_ffff00009086d31c >> 2 ^ uVar20 & _DAT_ffff00009086d31c >> 3 ^
                     uVar30 & _DAT_ffff00009086d31c >> 4 ^ uVar22 & _DAT_ffff00009086d31c >> 5 ^
                     uVar18 & _DAT_ffff00009086d31c) << 0xc |
                     (uVar23 & _DAT_ffff00009086d324 >> 1 ^ uVar28 >> 0xd & 1 ^
                      uVar15 & _DAT_ffff00009086d324 >> 2 ^ uVar20 & _DAT_ffff00009086d324 >> 3 ^
                     uVar30 & _DAT_ffff00009086d324 >> 4 ^ uVar22 & _DAT_ffff00009086d324 >> 5 ^
                     uVar18 & _DAT_ffff00009086d324) << 0xd |
                     (uVar23 & _DAT_ffff00009086d328 >> 1 ^ uVar28 >> 0xe & 1 ^
                      uVar15 & _DAT_ffff00009086d328 >> 2 ^ uVar20 & _DAT_ffff00009086d328 >> 3 ^
                     uVar30 & _DAT_ffff00009086d328 >> 4 ^ uVar22 & _DAT_ffff00009086d328 >> 5 ^
                     uVar18 & _DAT_ffff00009086d328) << 0xe |
                     (uVar23 & _DAT_ffff00009086d32c >> 1 ^ uVar28 >> 0xf & 1 ^
                      uVar15 & _DAT_ffff00009086d32c >> 2 ^ uVar20 & _DAT_ffff00009086d32c >> 3 ^
                     uVar30 & _DAT_ffff00009086d32c >> 4 ^ uVar22 & _DAT_ffff00009086d32c >> 5 ^
                     uVar18 & _DAT_ffff00009086d32c) << 0xf |
                    (uVar23 & _DAT_ffff00009086d330 >> 1 ^ uVar28 >> 0x10 & 1 ^
                     uVar15 & _DAT_ffff00009086d330 >> 2 ^ uVar20 & _DAT_ffff00009086d330 >> 3 ^
                    uVar30 & _DAT_ffff00009086d330 >> 4 ^ uVar22 & _DAT_ffff00009086d330 >> 5 ^
                    _DAT_ffff00009086d330 & uVar11 >> 0x16 & 1) << 0x10);
  uVar23 = (uint)(uVar29 >> 8) & 1;
  uVar20 = (uint)uVar21;
  uVar30 = (uint)uVar31;
  uVar15 = (uint)DAT_ffff000090818a88;
  if (DAT_ffff000090818a88 == 0) {
    uVar19 = (ulong)_DAT_ffff00009086d334;
    param_3[1] = (uint)(uVar17 >> (uVar19 & 0x3f)) & 1;
  }
  else {
    param_3[1] = (uint)(DAT_ffff000090818a88 >> 3) & 1 & (uint)uVar13 >> 0xb ^
                 (uint)(DAT_ffff000090818a88 >> 2) & 1 & uVar30 >> 10 ^
                 (uint)(DAT_ffff000090818a88 >> 1) & 1 & uVar20 >> 9 ^ uVar23 & uVar15;
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
  uVar13 = _DAT_ffff00009086d2d8;
  uVar22 = (uint)_DAT_ffff00009086d2d8;
  uVar18 = (uint)uVar19;
  if (_DAT_ffff00009086d2d8 == 0) {
    *param_3 = 0;
    if (_DAT_ffff00009086d338 == 0) goto LAB_ffff00009072620c;
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
    *param_3 = uVar11 ^ (uint)(_DAT_ffff00009086d2d8 >> 2) & 1 & uVar30 >> 10 ^
                        (uint)(_DAT_ffff00009086d2d8 >> 1) & 1 & uVar20 >> 9 ^ uVar23 & uVar22;
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
    if (_DAT_ffff00009086d338 == 0) {
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
LAB_ffff00009072620c:
  uVar23 = _DAT_ffff00009086d33c;
  local_70 = uVar17;
  if (_DAT_ffff00009086d33c != 0) {
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
             (uVar30 & _DAT_ffff00009086d340 >> 1 ^ uVar16 >> 8 & 1 ^
              uVar18 & _DAT_ffff00009086d340 >> 2 ^ uVar11 & _DAT_ffff00009086d340 >> 3 ^
             uVar28 & _DAT_ffff00009086d340 >> 4 ^ uVar8 & _DAT_ffff00009086d340 >> 5 ^
             uVar20 & _DAT_ffff00009086d340) << 8 |
             (ulong)((uVar30 & _DAT_ffff00009086d344 >> 1 ^ uVar16 >> 0xb & 1 ^
                      uVar18 & _DAT_ffff00009086d344 >> 2 ^ uVar11 & _DAT_ffff00009086d344 >> 3 ^
                     uVar28 & _DAT_ffff00009086d344 >> 4 ^ uVar8 & _DAT_ffff00009086d344 >> 5 ^
                     uVar20 & _DAT_ffff00009086d344) << 0xb |
                    (uVar30 & _DAT_ffff00009086d348 >> 1 ^ uVar16 >> 0xc & 1 ^
                     uVar18 & _DAT_ffff00009086d348 >> 2 ^ uVar11 & _DAT_ffff00009086d348 >> 3 ^
                    uVar28 & _DAT_ffff00009086d348 >> 4 ^ uVar8 & _DAT_ffff00009086d348 >> 5 ^
                    uVar20 & _DAT_ffff00009086d348) << 0xc) |
             (ulong)((uVar30 & _DAT_ffff00009086d34c >> 1 ^ uVar16 >> 0xd & 1 ^
                      uVar18 & _DAT_ffff00009086d34c >> 2 ^ uVar11 & _DAT_ffff00009086d34c >> 3 ^
                     uVar28 & _DAT_ffff00009086d34c >> 4 ^ uVar8 & _DAT_ffff00009086d34c >> 5 ^
                     uVar20 & _DAT_ffff00009086d34c) << 0xd |
                     (uVar30 & _DAT_ffff00009086d350 >> 1 ^ uVar16 >> 0xe & 1 ^
                      uVar18 & _DAT_ffff00009086d350 >> 2 ^ uVar11 & _DAT_ffff00009086d350 >> 3 ^
                     uVar28 & _DAT_ffff00009086d350 >> 4 ^ uVar8 & _DAT_ffff00009086d350 >> 5 ^
                     uVar20 & _DAT_ffff00009086d350) << 0xe |
                    (uVar30 & _DAT_ffff00009086d354 >> 1 ^ uVar16 >> 0xf & 1 ^
                     uVar18 & _DAT_ffff00009086d354 >> 2 ^ uVar11 & _DAT_ffff00009086d354 >> 3 ^
                    uVar28 & _DAT_ffff00009086d354 >> 4 ^ uVar8 & _DAT_ffff00009086d354 >> 5 ^
                    uVar20 & _DAT_ffff00009086d354) << 0xf) |
             (ulong)((uVar30 & _DAT_ffff00009086d358 >> 1 ^ uVar18 & _DAT_ffff00009086d358 >> 2 ^
                      uVar11 & _DAT_ffff00009086d358 >> 3 ^ uVar28 & _DAT_ffff00009086d358 >> 4 ^
                      uVar8 & _DAT_ffff00009086d358 >> 5 ^
                      _DAT_ffff00009086d358 & uVar16 >> 0x16 & 1 ^ uVar20) << 0x10);
  }
  if (_DAT_ffff00009086d35c == 0) {
    uVar20 = 0;
  }
  else if (_DAT_ffff00009086d360 == 0) {
    uVar20 = (uint)((ulong)_DAT_ffff00009086d364 <= uVar17 >> 0x14);
  }
  else {
    uVar20 = (uint)(uVar17 >>
                   ((ulong)_DAT_ffff00009086d368 +
                    (ulong)_DAT_ffff00009086d370 + (ulong)_DAT_ffff00009086d36c + 1 & 0x3f));
  }
  param_3[2] = uVar20;
  uVar8 = _DAT_ffff00009086d380;
  uVar28 = _DAT_ffff00009086d37c;
  uVar11 = _DAT_ffff00009086d370;
  uVar18 = _DAT_ffff00009086d36c;
  uVar30 = _DAT_ffff00009086d368;
  piVar6 = (int *)&DAT_ffff00009086d378;
  if (uVar20 != 0) {
    piVar6 = (int *)&DAT_ffff00009086d374;
  }
  if (*piVar6 == 0) {
    uVar16 = _DAT_ffff00009086d36c;
    if (uVar20 != 0) {
      uVar16 = _DAT_ffff00009086d37c;
    }
    uVar4 = _DAT_ffff00009086d370;
    if (uVar20 != 0) {
      uVar4 = _DAT_ffff00009086d380;
    }
    uVar12 = (1 << (ulong)(uVar16 & 0x1f)) - 1U &
             (uint)(uVar17 >> ((ulong)_DAT_ffff00009086d368 + 1 & 0x3f));
    uVar16 = (1 << (ulong)(uVar4 & 0x1f)) - 1U &
             (uint)(uVar17 >> ((ulong)uVar16 + (ulong)_DAT_ffff00009086d368 + 1 & 0x3f));
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
    uVar16 = _DAT_ffff00009086d370;
    if (uVar20 != 0) {
      uVar16 = _DAT_ffff00009086d380;
    }
    uVar4 = _DAT_ffff00009086d36c;
    if (uVar20 != 0) {
      uVar4 = _DAT_ffff00009086d37c;
    }
    bVar10 = _DAT_ffff00009086d384 == 0;
    uVar21 = uVar17 & 0xff | (local_68 >> 9) << 8;
    uVar19 = (ulong)(1 << (ulong)(uVar16 - 1 & 0x1f));
    uVar13 = uVar21 >> ((ulong)uVar4 + (ulong)_DAT_ffff00009086d368 + 1 & 0x3f);
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
             (uint)(uVar21 >> ((ulong)_DAT_ffff00009086d368 + 1 & 0x3f));
    uVar16 = (uint)(uVar17 >> 6) & 4 | (uint)((uVar26 & uVar13) != 0) << 1 |
             (uint)(uVar13 >> bVar10) & 1 | (uint)((uVar19 & uVar13) != 0) << 3;
  }
  param_3[5] = uVar12;
  param_3[3] = uVar16;
  uVar12 = _DAT_ffff00009086d38c;
  uVar4 = _DAT_ffff00009086d2fc;
  uVar16 = _DAT_ffff00009086d2ec;
  if (_DAT_ffff00009086d360 == 0) {
    if (uVar20 != 0) {
      uVar17 = uVar17 - (_DAT_ffff00009086d364 << 0x14);
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
  uVar28 = _DAT_ffff00009086d38c;
  if (bVar10) {
    uVar28 = _DAT_ffff00009086d388;
  }
  param_3[6] = uVar11;
  param_3[7] = uVar12;
  uVar12 = _DAT_ffff00009086d314;
  uVar11 = _DAT_ffff00009086d304;
  param_3[8] = uVar18;
  param_3[9] = uVar22;
  param_3[0xe] = uVar4;
  param_3[0xf] = uVar11;
  uVar18 = _DAT_ffff00009086d328;
  uVar22 = _DAT_ffff00009086d31c;
  param_3[10] = uVar15;
  param_3[0xb] = uVar23;
  uVar23 = _DAT_ffff00009086d2f4;
  param_3[4] = (1 << (ulong)(uVar28 & 0x1f)) - 1U &
               (uint)(uVar17 >> ((ulong)uVar20 + (ulong)(uVar30 + 1) + (ulong)uVar8 & 0x3f));
  uVar20 = _DAT_ffff00009086d32c;
  uVar15 = _DAT_ffff00009086d30c;
  param_3[0xc] = uVar16;
  param_3[0xd] = uVar23;
  param_3[0x10] = uVar15;
  param_3[0x11] = uVar12;
  uVar23 = _DAT_ffff00009086d324;
  param_3[0x14] = uVar18;
  param_3[0x15] = uVar20;
  param_3[0x12] = uVar22;
  param_3[0x13] = uVar23;
  uVar23 = _DAT_ffff00009086d340;
  param_3[0x16] = _DAT_ffff00009086d330;
  param_3[0x17] = uVar23;
  uVar20 = _DAT_ffff00009086d350;
  uVar15 = _DAT_ffff00009086d34c;
  uVar23 = _DAT_ffff00009086d348;
  param_3[0x18] = _DAT_ffff00009086d344;
  param_3[0x19] = uVar23;
  uVar30 = _DAT_ffff00009086d358;
  uVar23 = _DAT_ffff00009086d354;
  param_3[0x1a] = uVar15;
  param_3[0x1b] = uVar20;
  param_3[0x1c] = uVar23;
  param_3[0x1d] = uVar30;
  return 0;
}



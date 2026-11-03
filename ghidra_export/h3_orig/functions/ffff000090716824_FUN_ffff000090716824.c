// ffff000090716824  size=2704  FUN_ffff000090716824

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090716824(undefined4 param_1,ulong param_2)

{
  uint uVar1;
  long lVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  ulong uVar6;
  ulong uVar7;
  uint uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  long lVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong uVar16;
  
  if (((int)param_2 == 0) && (param_2 = cntfrq_el0, (int)param_2 == 0)) {
    FUN_ffff00009077de54
              (s__s__d__Failed_to_initialize_time_ffff0000907ea67f,
               s_arm_generic_timer_init_ffff000090801f3e,0x125);
    return;
  }
  uVar15 = param_2 & 0xffffffff;
  uVar7 = 0;
  if (uVar15 != 0) {
    uVar7 = 0x3e800000000 / uVar15;
  }
  uVar10 = uVar15 << 0x1d;
  uVar16 = uVar10 / 0x7d;
  uVar6 = 0;
  if (uVar15 != 0) {
    uVar6 = 0xf424000000000 / uVar15;
  }
  uVar5 = (uint)param_2;
  _DAT_ffff00009084365c = (uint)uVar7;
  uVar13 = ((ulong)(uint)((int)uVar16 * -1000) << 0x1d) / 0x7d;
  uVar11 = uVar10 / 0x7d00000000;
  _DAT_ffff000090843648 = uVar11 | uVar16 << 0x20;
  _DAT_ffff00009084366c = (uint)uVar6;
  _DAT_ffff000090843660 = 0;
  if (uVar15 != 0) {
    _DAT_ffff000090843660 = (uint)(((ulong)-(_DAT_ffff00009084365c * uVar5) << 0x20) / uVar15);
  }
  uVar9 = uVar13 & 0xffffffff;
  _DAT_ffff000090843650 = (uint)uVar13;
  _DAT_ffff000090843658 = (int)(uVar7 >> 0x20);
  _DAT_ffff000090843670 = 0;
  if (uVar15 != 0) {
    _DAT_ffff000090843670 = (uint)(((ulong)-(_DAT_ffff00009084366c * uVar5) << 0x20) / uVar15);
  }
  _DAT_ffff000090843668 = (uint)(uVar6 >> 0x20);
  uVar7 = (uVar11 - ((long)(uVar16 << 0x20) >> 0x3f)) - (uVar15 + 500) / 1000;
  if (uVar7 != 0) {
    uVar6 = -uVar7;
    if (-1 < (long)uVar7) {
      uVar6 = uVar7;
    }
    if (1 < uVar6) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
      uVar9 = (ulong)_DAT_ffff000090843650;
      uVar11 = _DAT_ffff000090843648 & 0xffffffff;
    }
  }
  uVar6 = _DAT_ffff000090843648 >> 0x20;
  uVar13 = uVar15 * 0x80000000;
  uVar7 = (uVar9 * 0x7fffffff >> 0x20) + (uVar6 * 0x7fffffff & 0xffffffff);
  uVar7 = (uVar11 * 0x7fffffff + (uVar6 * 0x7fffffff >> 0x20) + (uVar7 >> 0x20) +
          (uVar7 >> 0x1f & 1)) - (uVar15 * 0x7fffffff + 500) / 1000;
  if (uVar7 != 0) {
    uVar11 = -uVar7;
    if (-1 < (long)uVar7) {
      uVar11 = uVar7;
    }
    if (1 < uVar11) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
      uVar6 = _DAT_ffff000090843648 >> 0x20;
    }
  }
  uVar7 = ((_DAT_ffff000090843648 >> 0x21 | (_DAT_ffff000090843648 & 0xffffffff) << 0x1f) +
          (uVar6 & 1)) - (uVar15 << 0x1f | 0x1f0) / 1000;
  if (uVar7 != 0) {
    uVar11 = -uVar7;
    if (-1 < (long)uVar7) {
      uVar11 = uVar7;
    }
    if (1 < uVar11) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
      uVar6 = _DAT_ffff000090843648 >> 0x20;
    }
  }
  uVar6 = (uVar6 << 0x20) - uVar6;
  uVar11 = (param_2 << 0x20) - uVar15;
  uVar7 = (((ulong)_DAT_ffff000090843650 << 0x20) - (ulong)_DAT_ffff000090843650 >> 0x20) +
          (uVar6 & 0xffffffff);
  uVar7 = (((_DAT_ffff000090843648 << 0x20) - (_DAT_ffff000090843648 & 0xffffffff)) +
           (uVar6 >> 0x20) + (uVar7 >> 0x20) + (uVar7 >> 0x1f & 1)) - (uVar11 + 500) / 1000;
  if (uVar7 != 0) {
    uVar6 = -uVar7;
    if (-1 < (long)uVar7) {
      uVar6 = uVar7;
    }
    if (1 < uVar6) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
    }
  }
  uVar7 = uVar15 * 0x7fffffff >> 3;
  uVar6 = (ulong)_DAT_ffff00009084365c;
  uVar9 = uVar7 / 0x7d & 0xffffffff;
  lVar2 = uVar6 * uVar9 + (uVar7 / 0x7d00000000) * (ulong)_DAT_ffff000090843660 +
          (uVar9 * _DAT_ffff000090843660 >> 0x20);
  uVar1 = (_DAT_ffff000090843658 * (int)(uVar7 / 0x7d) +
           _DAT_ffff00009084365c * (int)(uVar7 / 0x7d00000000) + (int)((ulong)lVar2 >> 0x20)) -
          ((int)lVar2 >> 0x1f);
  if (uVar1 != 0x7fffffff) {
    uVar8 = 0;
    if (uVar5 != 0) {
      uVar8 = (uVar5 + 999) / uVar5;
    }
    uVar3 = uVar1 + 0x80000001;
    if ((int)(uVar1 + 0x80000001) < 0) {
      uVar3 = uVar1 ^ 0x7fffffff;
    }
    if (uVar8 < uVar3) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
      uVar6 = (ulong)_DAT_ffff00009084365c;
    }
  }
  uVar7 = uVar13 / 1000 & 0xffffffff;
  lVar2 = uVar6 * uVar7 + (ulong)_DAT_ffff000090843660 * (uVar13 / 0x3e800000000) +
          (_DAT_ffff000090843660 * uVar7 >> 0x20);
  uVar1 = ((int)uVar6 * (int)(uVar13 / 0x3e800000000) + _DAT_ffff000090843658 * (int)(uVar13 / 1000)
          + (int)((ulong)lVar2 >> 0x20)) - ((int)lVar2 >> 0x1f);
  if (uVar1 != 0x80000000) {
    uVar1 = uVar1 ^ 0x80000000;
    uVar8 = 0;
    if (uVar5 != 0) {
      uVar8 = (uVar5 + 999) / uVar5;
    }
    uVar3 = -uVar1;
    if (-1 < (int)uVar1) {
      uVar3 = uVar1;
    }
    if (uVar8 < uVar3) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
      uVar6 = (ulong)_DAT_ffff00009084365c;
    }
  }
  uVar11 = uVar11 >> 3;
  uVar7 = uVar11 / 0x7d & 0xffffffff;
  uVar8 = (uint)uVar6;
  lVar2 = uVar6 * uVar7 + (ulong)_DAT_ffff000090843660 * (uVar11 / 0x7d00000000) +
          (_DAT_ffff000090843660 * uVar7 >> 0x20);
  uVar1 = (uVar8 * (int)(uVar11 / 0x7d00000000) + _DAT_ffff000090843658 * (int)(uVar11 / 0x7d) +
          (int)((ulong)lVar2 >> 0x20)) - ((int)lVar2 >> 0x1f);
  if (uVar1 != 0xffffffff) {
    uVar3 = ~uVar1;
    uVar4 = 0;
    if (uVar5 != 0) {
      uVar4 = (uVar5 + 999) / uVar5;
    }
    if (-1 < (int)(uVar1 + 1)) {
      uVar3 = uVar1 + 1;
    }
    if (uVar4 < uVar3) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
      uVar8 = _DAT_ffff00009084365c;
    }
  }
  lVar2 = (ulong)uVar8 * (uVar16 & 0xffffffff) +
          (ulong)_DAT_ffff000090843660 * (uVar10 / 0x7d00000000) +
          ((ulong)_DAT_ffff000090843660 * (uVar16 & 0xffffffff) >> 0x20);
  uVar1 = (uVar8 * (int)(uVar10 / 0x7d00000000) + _DAT_ffff000090843658 * (int)uVar16 +
          (int)((ulong)lVar2 >> 0x20)) - ((int)lVar2 >> 0x1f);
  if (uVar1 != 0) {
    uVar8 = -uVar1;
    if (-1 < (int)uVar1) {
      uVar8 = uVar1;
    }
    uVar1 = 0;
    if (uVar5 != 0) {
      uVar1 = (uVar5 + 999) / uVar5;
    }
    if (uVar1 < uVar8) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
    }
  }
  if (uVar5 < 0x24924925) {
    uVar7 = ((param_2 << 0x23) - (param_2 << 0x20)) / 1000;
  }
  else {
    uVar7 = uVar16 * 7;
  }
  lVar2 = (ulong)_DAT_ffff00009084365c * (uVar7 & 0xffffffff) +
          (uVar7 >> 0x20) * (ulong)_DAT_ffff000090843660 +
          ((uVar7 & 0xffffffff) * (ulong)_DAT_ffff000090843660 >> 0x20);
  uVar1 = (_DAT_ffff000090843658 * (int)uVar7 + _DAT_ffff00009084365c * (int)(uVar7 >> 0x20) +
          (int)((ulong)lVar2 >> 0x20)) - ((int)lVar2 >> 0x1f);
  if (uVar1 != 0) {
    uVar8 = -uVar1;
    if (-1 < (int)uVar1) {
      uVar8 = uVar1;
    }
    uVar1 = 0;
    if (uVar5 != 0) {
      uVar1 = (uVar5 + 999) / uVar5;
    }
    if (uVar1 < uVar8) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
    }
  }
  if (uVar5 < 0x3a83a84) {
    uVar7 = (uVar15 * 0x4600000000) / 1000;
  }
  else {
    uVar7 = uVar16 * 0x46;
  }
  lVar2 = (ulong)_DAT_ffff00009084365c * (uVar7 & 0xffffffff) +
          (uVar7 >> 0x20) * (ulong)_DAT_ffff000090843660 +
          ((uVar7 & 0xffffffff) * (ulong)_DAT_ffff000090843660 >> 0x20);
  uVar1 = (_DAT_ffff000090843658 * (int)uVar7 + _DAT_ffff00009084365c * (int)(uVar7 >> 0x20) +
          (int)((ulong)lVar2 >> 0x20)) - ((int)lVar2 >> 0x1f);
  if (uVar1 != 0) {
    uVar8 = -uVar1;
    if (-1 < (int)uVar1) {
      uVar8 = uVar1;
    }
    uVar1 = 0;
    if (uVar5 != 0) {
      uVar1 = (uVar5 + 999) / uVar5;
    }
    if (uVar1 < uVar8) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
    }
  }
  if (uVar5 < 0x5d9f74) {
    uVar16 = (uVar15 * 0x2bc00000000) / 1000;
  }
  else {
    uVar16 = uVar16 * 700;
  }
  lVar2 = (ulong)_DAT_ffff00009084365c * (uVar16 & 0xffffffff) +
          (uVar16 >> 0x20) * (ulong)_DAT_ffff000090843660 +
          ((uVar16 & 0xffffffff) * (ulong)_DAT_ffff000090843660 >> 0x20);
  uVar1 = (_DAT_ffff000090843658 * (int)uVar16 + _DAT_ffff00009084365c * (int)(uVar16 >> 0x20) +
          (int)((ulong)lVar2 >> 0x20)) - ((int)lVar2 >> 0x1f);
  if (uVar1 != 0) {
    uVar8 = -uVar1;
    if (-1 < (int)uVar1) {
      uVar8 = uVar1;
    }
    uVar1 = 0;
    if (uVar5 != 0) {
      uVar1 = (uVar5 + 999) / uVar5;
    }
    if (uVar1 < uVar8) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
    }
  }
  lVar12 = CONCAT44(_DAT_ffff00009084366c,_DAT_ffff000090843668);
  uVar6 = (ulong)_DAT_ffff000090843670;
  uVar16 = (ulong)_DAT_ffff00009084366c;
  uVar10 = (ulong)_DAT_ffff000090843668;
  uVar7 = (uVar6 * uVar15 >> 0x20) + (uVar16 * uVar15 & 0xffffffff);
  lVar2 = (uVar16 * uVar15 >> 0x20) + uVar10 * uVar15 + (uVar7 >> 0x20) + (uVar7 >> 0x1f & 1);
  if (lVar2 != 1000000) {
    uVar1 = 0;
    if (uVar5 != 0) {
      uVar1 = (uVar5 + 999999) / uVar5;
    }
    uVar7 = lVar2 - 1000000U;
    if ((long)(lVar2 - 1000000U) < 0) {
      uVar7 = 1000000 - lVar2;
    }
    if (uVar1 < uVar7) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
      lVar12 = CONCAT44(_DAT_ffff00009084366c,_DAT_ffff000090843668);
      uVar6 = (ulong)_DAT_ffff000090843670;
      uVar16 = (ulong)_DAT_ffff00009084366c;
      uVar10 = (ulong)_DAT_ffff000090843668;
    }
  }
  uVar11 = uVar16 | lVar12 << 0x20;
  uVar9 = uVar15 * 0x15180 & 0xffffff80;
  uVar13 = uVar15 * 0x15180 >> 0x20;
  uVar14 = uVar6 * uVar13;
  uVar7 = (uVar16 * uVar9 & 0xffffff80) + (uVar14 & 0xffffffff) + (uVar6 * uVar9 >> 0x20);
  lVar2 = (uVar16 * uVar9 >> 0x20) + uVar10 * uVar9 + (uVar14 >> 0x20) + uVar11 * uVar13 +
          (uVar7 >> 0x20) + (uVar7 >> 0x1f & 1);
  if (lVar2 != 86400000000) {
    uVar1 = 0;
    if (uVar5 != 0) {
      uVar1 = (uVar5 + 999999) / uVar5;
    }
    uVar7 = lVar2 - 86400000000U;
    if ((long)(lVar2 - 86400000000U) < 0) {
      uVar7 = 86400000000 - lVar2;
    }
    if (uVar1 < uVar7) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
      uVar6 = (ulong)_DAT_ffff000090843670;
      uVar16 = (ulong)_DAT_ffff00009084366c;
      uVar10 = (ulong)_DAT_ffff000090843668;
      uVar11 = (ulong)_DAT_ffff00009084366c |
               CONCAT44(_DAT_ffff00009084366c,_DAT_ffff000090843668) << 0x20;
    }
  }
  uVar9 = uVar15 * 0x1e13380 & 0xffffff80;
  uVar13 = uVar15 * 0x1e13380 >> 0x20;
  uVar14 = uVar6 * uVar13;
  uVar7 = (uVar16 * uVar9 & 0xffffff80) + (uVar14 & 0xffffffff) + (uVar6 * uVar9 >> 0x20);
  lVar2 = uVar11 * uVar13 + uVar10 * uVar9 + (uVar16 * uVar9 >> 0x20) + (uVar14 >> 0x20) +
          (uVar7 >> 0x20) + (uVar7 >> 0x1f & 1);
  if (lVar2 != 0x1cae8c13e000) {
    uVar1 = 0;
    if (uVar5 != 0) {
      uVar1 = (uVar5 + 999999) / uVar5;
    }
    uVar7 = lVar2 - 0x1cae8c13e000U;
    if ((long)(lVar2 - 0x1cae8c13e000U) < 0) {
      uVar7 = 0x1cae8c13e000 - lVar2;
    }
    if (uVar1 < uVar7) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
      uVar6 = (ulong)_DAT_ffff000090843670;
      uVar16 = (ulong)_DAT_ffff00009084366c;
      uVar10 = (ulong)_DAT_ffff000090843668;
      uVar11 = (ulong)_DAT_ffff00009084366c |
               CONCAT44(_DAT_ffff00009084366c,_DAT_ffff000090843668) << 0x20;
    }
  }
  uVar9 = uVar15 * 0x12cea600 & 0xfffffe00;
  uVar13 = uVar15 * 0x12cea600 >> 0x20;
  uVar14 = uVar6 * uVar13;
  uVar7 = (uVar16 * uVar9 & 0xfffffe00) + (uVar14 & 0xffffffff) + (uVar6 * uVar9 >> 0x20);
  lVar2 = uVar11 * uVar13 + uVar10 * uVar9 + (uVar16 * uVar9 >> 0x20) + (uVar14 >> 0x20) +
          (uVar7 >> 0x20) + (uVar7 >> 0x1f & 1);
  if (lVar2 != 0x11ef9b4758000) {
    uVar1 = 0;
    if (uVar5 != 0) {
      uVar1 = (uVar5 + 999999) / uVar5;
    }
    uVar7 = lVar2 + 0xfffee1064b8a8000U;
    if ((long)(lVar2 + 0xfffee1064b8a8000U) < 0) {
      uVar7 = 0x11ef9b4758000 - lVar2;
    }
    if (uVar1 < uVar7) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
      uVar6 = (ulong)_DAT_ffff000090843670;
      uVar16 = (ulong)_DAT_ffff00009084366c;
      uVar10 = (ulong)_DAT_ffff000090843668;
      uVar11 = (ulong)_DAT_ffff00009084366c |
               CONCAT44(_DAT_ffff00009084366c,_DAT_ffff000090843668) << 0x20;
    }
  }
  uVar13 = uVar15 * 0xbbfac100 & 0xffffff00;
  uVar15 = uVar15 * 0xbbfac100 >> 0x20;
  uVar9 = uVar6 * uVar15;
  uVar7 = (uVar16 * uVar13 & 0xffffff00) + (uVar9 & 0xffffffff) + (uVar6 * uVar13 >> 0x20);
  lVar2 = uVar11 * uVar15 + uVar10 * uVar13 + (uVar16 * uVar13 >> 0x20) + (uVar9 >> 0x20) +
          (uVar7 >> 0x20) + (uVar7 >> 0x1f & 1);
  if (lVar2 != 0xb3456f3724000) {
    uVar1 = 0;
    if (uVar5 != 0) {
      uVar1 = (uVar5 + 999999) / uVar5;
    }
    uVar7 = lVar2 + 0xfff4cba90c8dc000U;
    if ((long)(lVar2 + 0xfff4cba90c8dc000U) < 0) {
      uVar7 = 0xb3456f3724000 - lVar2;
    }
    if (uVar1 < uVar7) {
      FUN_ffff00009077de54
                (s__s__d__FAIL__off_by__llu_ffff000090800465,
                 s_test_time_conversion_check_resul_ffff0000907ebf07,0xdb);
    }
  }
  FUN_ffff000090715fe4(param_1,FUN_ffff0000907172b4,0);
  FUN_ffff000090716408(param_1);
  _DAT_ffff000090843640 = param_1;
  return;
}



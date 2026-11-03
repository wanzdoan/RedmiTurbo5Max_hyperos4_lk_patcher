
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be04b634(void)

{
  int iVar1;
  undefined4 uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  uint uVar9;
  int iVar10;
  int iVar11;
  long lVar12;
  uint uVar13;
  uint uVar14;
  undefined4 uVar15;
  ulong uVar16;
  ulong uVar17;
  uint local_7c [7];
  
  lVar12 = FUN_ffff0000be05f1bc(0x800);
  if (lVar12 == 0) {
    FUN_ffff0000be05fd98
              (s__Thermal__s_alloc_buffer_FAILED__ffff0000be0b3346,
               s_lvts_thermal_cal_prepare_ffff0000be0b3368);
    return;
  }
  uVar3 = thunk_FUN_ffff0000be0364a4(0xc1);
  local_7c[0] = uVar3;
  uVar4 = thunk_FUN_ffff0000be0364a4(0x113);
  local_7c[1] = uVar4;
  uVar5 = thunk_FUN_ffff0000be0364a4(0x114);
  local_7c[2] = uVar5;
  uVar6 = thunk_FUN_ffff0000be0364a4(0x115);
  local_7c[3] = uVar6;
  uVar7 = thunk_FUN_ffff0000be0364a4(0x116);
  local_7c[4] = uVar7;
  uVar8 = thunk_FUN_ffff0000be0364a4(0x124);
  local_7c[5] = uVar8;
  uVar9 = thunk_FUN_ffff0000be0364a4(0x125);
  uVar17 = 0;
  uVar16 = 0;
  local_7c[6] = uVar9;
  do {
    iVar11 = (int)uVar16;
    iVar1 = (int)uVar17;
    if (4 < uVar17) {
      iVar1 = (int)uVar17 + -5;
    }
    if (iVar1 == 0) {
      iVar10 = FUN_ffff0000be0611dc
                         (lVar12 + iVar11,(long)(0x800 - iVar11),s__lvts_cali__ffff0000be0b127c);
      iVar11 = iVar10 + iVar11;
    }
    iVar10 = FUN_ffff0000be0611dc
                       (lVar12 + iVar11,(long)(0x800 - iVar11),s__d__0x_x__ffff0000be0a85cc,
                        uVar17 & 0xffffffff,local_7c[uVar17]);
    uVar16 = (long)iVar11 + (long)iVar10;
    if (iVar1 == 4) {
      iVar11 = FUN_ffff0000be0611dc
                         (lVar12 + uVar16,(long)(0x800 - (int)uVar16),&DAT_ffff0000be0bf441);
      uVar16 = (ulong)(uint)(iVar11 + (int)uVar16);
    }
    uVar17 = uVar17 + 1;
  } while (uVar17 != 7);
  FUN_ffff0000be0611dc(lVar12 + (int)uVar16,(long)(0x800 - (int)uVar16),&DAT_ffff0000be0bf441);
  FUN_ffff0000be05fd98(s__Thermal__s_ffff0000be0b7d6e,lVar12);
  DAT_ffff0000be1260dc = uVar3 & 0xff;
  _DAT_ffff0000be126188 = CONCAT44(uVar4,uVar4 >> 0x10) & 0xffffffffffff;
  _DAT_ffff0000be126190 = CONCAT44(uVar5,uVar5 >> 0x10) & 0xffffffffffff;
  DAT_ffff0000be1261c0 = uVar8 & 0xffffff;
  DAT_ffff0000be1261c4 = uVar9 & 0xffffff;
  _DAT_ffff0000be126198 = CONCAT44(uVar6,uVar6 >> 0x10) & 0xffffffffffff;
  uVar14 = uVar3 >> 8 & 0xff;
  _DAT_ffff0000be1261a0 = CONCAT44(uVar7,uVar7 >> 0x10) & 0xffffffffffff;
  _DAT_ffff0000be1261d0 = CONCAT44(uVar9,uVar8) & 0x3f0000003f000000;
  uVar13 = DAT_ffff0000be1260dc;
  if ((((uVar3 == 0) && (uVar13 = 0, uVar4 == 0)) && (uVar5 == 0)) &&
     (((uVar6 == 0 && (uVar7 == 0)) && ((uVar8 == 0 && (uVar9 == 0)))))) {
    FUN_ffff0000be05fd98(s__Thermal__lvts_cal__This_sample_i_ffff0000be0a4e43,0,0);
    uVar13 = 0x3c;
    uVar14 = 0xaa;
    _DAT_ffff0000be1261d0 = 0;
    _DAT_ffff0000be1261d8 = 0;
    _DAT_ffff0000be126188 = 0x865500008655;
    _DAT_ffff0000be126190 = 0x865500008655;
    _DAT_ffff0000be126198 = 0x865500008655;
    _DAT_ffff0000be1261a0 = 0x865500008655;
    _DAT_ffff0000be1261a8 = 0x865500008655;
    _DAT_ffff0000be1261b0 = 0x865500008655;
    _DAT_ffff0000be1261b8 = 0x865500008655;
    DAT_ffff0000be1261c0 = 0x5e6d;
    DAT_ffff0000be1261c4 = 0x5e6d;
    DAT_ffff0000be1261c8 = '\x01';
    DAT_ffff0000be1260dc = 0x3c;
    DAT_ffff0000be1261cc = '\x01';
  }
  FUN_ffff0000be05fd98(s__Thermal__lvts_cal__g_golden_tem_ffff0000be0b46d4,uVar13,uVar14);
  uVar3 = FUN_ffff0000be0611dc(lVar12,0x800,s__lvts_cal__num_g_count_r_g_count_ffff0000be0b23f3);
  if (uVar3 < 0x800) {
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d__d_ffff0000be0bd75c,0,
                        _DAT_ffff0000be126188 & 0xffffffff,DAT_ffff0000be1261c0);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d__d_ffff0000be0bd75c,1,
                        DAT_ffff0000be12618c,DAT_ffff0000be1261c4);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    uVar15 = 0x5e6d;
    uVar2 = uVar15;
    if (DAT_ffff0000be1261c8 == '\0') {
      uVar2 = 0;
    }
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d__d_ffff0000be0bd75c,2,
                        _DAT_ffff0000be126190 & 0xffffffff,uVar2);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    if (DAT_ffff0000be1261cc == '\0') {
      uVar15 = 0;
    }
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d__d_ffff0000be0bd75c,3,
                        DAT_ffff0000be126194,uVar15);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000be0b3381,4,
                        _DAT_ffff0000be126198 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000be0b3381,5,
                        DAT_ffff0000be12619c);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000be0b3381,6,
                        _DAT_ffff0000be1261a0 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000be0b3381,7,
                        DAT_ffff0000be1261a4);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000be0b3381,8,
                        _DAT_ffff0000be1261a8 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000be0b3381,9,
                        DAT_ffff0000be1261ac);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000be0b3381,10,
                        _DAT_ffff0000be1261b0 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000be0b3381,0xb,
                        DAT_ffff0000be1261b4);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000be0b3381,0xc,
                        _DAT_ffff0000be1261b8 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000be04bb4c;
    FUN_ffff0000be0611dc
              (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000be0b3381,0xd,DAT_ffff0000be1261bc
              );
  }
  else {
LAB_ffff0000be04bb4c:
    FUN_ffff0000be05fd98
              (s__Thermal__s__d_error_ffff0000be0b6cba,s_lvts_thermal_cal_prepare_ffff0000be0b3368,
               0x2b2);
  }
  FUN_ffff0000be05fd98(s__Thermal__s_ffff0000be0a6219,lVar12);
  uVar3 = FUN_ffff0000be0611dc(lVar12,0x800,s_num_op_cali_ffff0000be0a3a5d);
  if (uVar3 < 0x800) {
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d_0x_x_ffff0000be0b2418,0,
                        _DAT_ffff0000be1261d0 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (uVar3 < 0x800) {
      iVar11 = FUN_ffff0000be0611dc
                         (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d_0x_x_ffff0000be0b2418,1,
                          DAT_ffff0000be1261d4);
      uVar3 = iVar11 + uVar3;
      if (uVar3 < 0x800) {
        iVar11 = FUN_ffff0000be0611dc
                           (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d_0x_x_ffff0000be0b2418,2,
                            _DAT_ffff0000be1261d8 & 0xffffffff);
        uVar3 = iVar11 + uVar3;
        if (uVar3 < 0x800) {
          FUN_ffff0000be0611dc
                    (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d_0x_x_ffff0000be0b2418,3,
                     DAT_ffff0000be1261dc);
          goto LAB_ffff0000be04bc38;
        }
      }
    }
  }
  FUN_ffff0000be05fd98
            (s__Thermal__s__d_error_ffff0000be0b6cba,s_lvts_thermal_cal_prepare_ffff0000be0b3368,
             0x2c3);
LAB_ffff0000be04bc38:
  FUN_ffff0000be05fd98(s__Thermal__s_ffff0000be0a6219,lVar12);
  uVar17 = FUN_ffff0000be0611dc(lVar12,0x800,s__lvts_coef_a__ffff0000be0c66e7);
  uVar17 = uVar17 & 0xffffffff;
  uVar16 = 0;
  do {
    uVar3 = (uint)uVar17;
    (&DAT_ffff0000be126150)[uVar16] = 0x5f924;
    if (0x7ff < uVar3) {
      FUN_ffff0000be05fd98
                (s__Thermal__s__d_error_ffff0000be0b6cba,s_lvts_thermal_cal_prepare_ffff0000be0b3368
                 ,0x2d2);
      break;
    }
    iVar11 = FUN_ffff0000be0611dc
                       (lVar12 + uVar17,0x800 - uVar3,s__d__d_ffff0000be0b3381,uVar16 & 0xffffffff,
                        0x5f924);
    uVar16 = uVar16 + 1;
    uVar17 = (ulong)(iVar11 + uVar3);
  } while (uVar16 != 0xe);
  FUN_ffff0000be05fd98(s__Thermal__s_ffff0000be0a6219,lVar12);
  thunk_FUN_ffff0000be09937c(lVar12);
  *(uint *)((ulong)DAT_ffff0000be0de298 + DAT_ffff0000be0de2a0 + 0xf4) = DAT_ffff0000be1260dc;
  return;
}




/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b8042420(void)

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
  
  lVar12 = FUN_ffff0000b804bccc(0x800);
  if (lVar12 == 0) {
    FUN_ffff0000b804c33c
              (s__Thermal__s_alloc_buffer_FAILED__ffff0000b8091cb0,
               s_lvts_thermal_cal_prepare_ffff0000b8091cd2);
    return;
  }
  uVar3 = thunk_FUN_ffff0000b802ce0c(0xc1);
  local_7c[0] = uVar3;
  uVar4 = thunk_FUN_ffff0000b802ce0c(0x113);
  local_7c[1] = uVar4;
  uVar5 = thunk_FUN_ffff0000b802ce0c(0x114);
  local_7c[2] = uVar5;
  uVar6 = thunk_FUN_ffff0000b802ce0c(0x115);
  local_7c[3] = uVar6;
  uVar7 = thunk_FUN_ffff0000b802ce0c(0x116);
  local_7c[4] = uVar7;
  uVar8 = thunk_FUN_ffff0000b802ce0c(0x124);
  local_7c[5] = uVar8;
  uVar9 = thunk_FUN_ffff0000b802ce0c(0x125);
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
      iVar10 = FUN_ffff0000b804d74c
                         (lVar12 + iVar11,(long)(0x800 - iVar11),s__lvts_cali__ffff0000b8090258);
      iVar11 = iVar10 + iVar11;
    }
    iVar10 = FUN_ffff0000b804d74c
                       (lVar12 + iVar11,(long)(0x800 - iVar11),s__d__0x_x__ffff0000b80894b1,
                        uVar17 & 0xffffffff,local_7c[uVar17]);
    uVar16 = (long)iVar11 + (long)iVar10;
    if (iVar1 == 4) {
      iVar11 = FUN_ffff0000b804d74c
                         (lVar12 + uVar16,(long)(0x800 - (int)uVar16),&DAT_ffff0000b809b862);
      uVar16 = (ulong)(uint)(iVar11 + (int)uVar16);
    }
    uVar17 = uVar17 + 1;
  } while (uVar17 != 7);
  FUN_ffff0000b804d74c(lVar12 + (int)uVar16,(long)(0x800 - (int)uVar16),&DAT_ffff0000b809b862);
  FUN_ffff0000b804c33c(s__Thermal__s_ffff0000b8095a73,lVar12);
  DAT_ffff0000b81298dc = uVar3 & 0xff;
  _DAT_ffff0000b8129988 = CONCAT44(uVar4,uVar4 >> 0x10) & 0xffffffffffff;
  _DAT_ffff0000b8129990 = CONCAT44(uVar5,uVar5 >> 0x10) & 0xffffffffffff;
  DAT_ffff0000b81299c0 = uVar8 & 0xffffff;
  DAT_ffff0000b81299c4 = uVar9 & 0xffffff;
  _DAT_ffff0000b8129998 = CONCAT44(uVar6,uVar6 >> 0x10) & 0xffffffffffff;
  uVar14 = uVar3 >> 8 & 0xff;
  _DAT_ffff0000b81299a0 = CONCAT44(uVar7,uVar7 >> 0x10) & 0xffffffffffff;
  _DAT_ffff0000b81299d0 = CONCAT44(uVar9,uVar8) & 0x3f0000003f000000;
  uVar13 = DAT_ffff0000b81298dc;
  if ((((uVar3 == 0) && (uVar13 = 0, uVar4 == 0)) && (uVar5 == 0)) &&
     (((uVar6 == 0 && (uVar7 == 0)) && ((uVar8 == 0 && (uVar9 == 0)))))) {
    FUN_ffff0000b804c33c(s__Thermal__lvts_cal__This_sample_i_ffff0000b8086956,0,0);
    uVar13 = 0x3c;
    uVar14 = 0xaa;
    _DAT_ffff0000b81299d0 = 0;
    _DAT_ffff0000b81299d8 = 0;
    _DAT_ffff0000b8129988 = 0x865500008655;
    _DAT_ffff0000b8129990 = 0x865500008655;
    _DAT_ffff0000b8129998 = 0x865500008655;
    _DAT_ffff0000b81299a0 = 0x865500008655;
    _DAT_ffff0000b81299a8 = 0x865500008655;
    _DAT_ffff0000b81299b0 = 0x865500008655;
    _DAT_ffff0000b81299b8 = 0x865500008655;
    DAT_ffff0000b81299c0 = 0x5e6d;
    DAT_ffff0000b81299c4 = 0x5e6d;
    DAT_ffff0000b81299c8 = '\x01';
    DAT_ffff0000b81298dc = 0x3c;
    DAT_ffff0000b81299cc = '\x01';
  }
  FUN_ffff0000b804c33c(s__Thermal__lvts_cal__g_golden_tem_ffff0000b8092c00,uVar13,uVar14);
  uVar3 = FUN_ffff0000b804d74c(lVar12,0x800,s__lvts_cal__num_g_count_r_g_count_ffff0000b8091137);
  if (uVar3 < 0x800) {
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d__d_ffff0000b809a187,0,
                        _DAT_ffff0000b8129988 & 0xffffffff,DAT_ffff0000b81299c0);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d__d_ffff0000b809a187,1,
                        DAT_ffff0000b812998c,DAT_ffff0000b81299c4);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    uVar15 = 0x5e6d;
    uVar2 = uVar15;
    if (DAT_ffff0000b81299c8 == '\0') {
      uVar2 = 0;
    }
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d__d_ffff0000b809a187,2,
                        _DAT_ffff0000b8129990 & 0xffffffff,uVar2);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    if (DAT_ffff0000b81299cc == '\0') {
      uVar15 = 0;
    }
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d__d_ffff0000b809a187,3,
                        DAT_ffff0000b8129994,uVar15);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000b8091ceb,4,
                        _DAT_ffff0000b8129998 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000b8091ceb,5,
                        DAT_ffff0000b812999c);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000b8091ceb,6,
                        _DAT_ffff0000b81299a0 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000b8091ceb,7,
                        DAT_ffff0000b81299a4);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000b8091ceb,8,
                        _DAT_ffff0000b81299a8 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000b8091ceb,9,
                        DAT_ffff0000b81299ac);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000b8091ceb,10,
                        _DAT_ffff0000b81299b0 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000b8091ceb,0xb,
                        DAT_ffff0000b81299b4);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000b8091ceb,0xc,
                        _DAT_ffff0000b81299b8 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (0x7ff < uVar3) goto LAB_ffff0000b8042938;
    FUN_ffff0000b804d74c
              (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d__d_ffff0000b8091ceb,0xd,DAT_ffff0000b81299bc
              );
  }
  else {
LAB_ffff0000b8042938:
    FUN_ffff0000b804c33c
              (s__Thermal__s__d_error_ffff0000b8094c8d,s_lvts_thermal_cal_prepare_ffff0000b8091cd2,
               0x2b2);
  }
  FUN_ffff0000b804c33c(s__Thermal__s_ffff0000b80878d8,lVar12);
  uVar3 = FUN_ffff0000b804d74c(lVar12,0x800,s_num_op_cali_ffff0000b808594c);
  if (uVar3 < 0x800) {
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d_0x_x_ffff0000b809115c,0,
                        _DAT_ffff0000b81299d0 & 0xffffffff);
    uVar3 = iVar11 + uVar3;
    if (uVar3 < 0x800) {
      iVar11 = FUN_ffff0000b804d74c
                         (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d_0x_x_ffff0000b809115c,1,
                          DAT_ffff0000b81299d4);
      uVar3 = iVar11 + uVar3;
      if (uVar3 < 0x800) {
        iVar11 = FUN_ffff0000b804d74c
                           (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d_0x_x_ffff0000b809115c,2,
                            _DAT_ffff0000b81299d8 & 0xffffffff);
        uVar3 = iVar11 + uVar3;
        if (uVar3 < 0x800) {
          FUN_ffff0000b804d74c
                    (lVar12 + (ulong)uVar3,0x800 - uVar3,s__d_0x_x_ffff0000b809115c,3,
                     DAT_ffff0000b81299dc);
          goto LAB_ffff0000b8042a24;
        }
      }
    }
  }
  FUN_ffff0000b804c33c
            (s__Thermal__s__d_error_ffff0000b8094c8d,s_lvts_thermal_cal_prepare_ffff0000b8091cd2,
             0x2c3);
LAB_ffff0000b8042a24:
  FUN_ffff0000b804c33c(s__Thermal__s_ffff0000b80878d8,lVar12);
  uVar17 = FUN_ffff0000b804d74c(lVar12,0x800,s__lvts_coef_a__ffff0000b80a14cb);
  uVar17 = uVar17 & 0xffffffff;
  uVar16 = 0;
  do {
    uVar3 = (uint)uVar17;
    (&DAT_ffff0000b8129950)[uVar16] = 0x5f924;
    if (0x7ff < uVar3) {
      FUN_ffff0000b804c33c
                (s__Thermal__s__d_error_ffff0000b8094c8d,s_lvts_thermal_cal_prepare_ffff0000b8091cd2
                 ,0x2d2);
      break;
    }
    iVar11 = FUN_ffff0000b804d74c
                       (lVar12 + uVar17,0x800 - uVar3,s__d__d_ffff0000b8091ceb,uVar16 & 0xffffffff,
                        0x5f924);
    uVar16 = uVar16 + 1;
    uVar17 = (ulong)(iVar11 + uVar3);
  } while (uVar16 != 0xe);
  FUN_ffff0000b804c33c(s__Thermal__s_ffff0000b80878d8,lVar12);
  thunk_FUN_ffff0000b8082558(lVar12);
  *(uint *)((ulong)DAT_ffff0000b80f11c8 + DAT_ffff0000b80f11d0 + 0xf4) = DAT_ffff0000b81298dc;
  return;
}


